package VisiCode.Controllers;

import VisiCode.Domain.*;
import VisiCode.Domain.Exceptions.EntityException;
import VisiCode.Domain.Exceptions.UserException;
import VisiCode.Payload.NoteResponse;
import VisiCode.Payload.ProjectCreationRequest;
import VisiCode.Payload.ProjectRemovalRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Controller
@RequestMapping("/api")
public class ProjectController extends UserAuthenticable {
    @Autowired
    NoteRepository noteRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    UserRepository userRepository;

    @ResponseBody
    @GetMapping("/project")
    public List<String> myProjects(Authentication auth) {
        User user = getAuthenticated(auth);
        Iterable<Project> existingProjects = projectRepository.findAllById(user.getProjects());
        List<String> names = new ArrayList<>();
        for(Project project : existingProjects)
            names.add(project.getName());
        return names;
    }

    @ResponseBody
    @PostMapping("/project/create")
    public Project createProject(Authentication auth, @RequestBody @Valid ProjectCreationRequest request) {
        User user = getAuthenticated(auth);
        Iterable<Project> existingProjects = projectRepository.findAllById(user.getProjects());
        for (Project p : existingProjects) {
            if (p.getName().equals(request.getName()))
                throw EntityException.duplicateProject(request.getName());
        }

        Project project = Project.create(request.getName());
        projectRepository.save(project);
        if (user.addProject(project.getId())) {
            userRepository.save(user);
            project.clearId();
            return project;
        } else
            throw EntityException.duplicateProject(request.getName());
    }

    @PostMapping("/project/remove")
    @ResponseStatus(value = HttpStatus.OK)
    public void removeProject(Authentication auth, @RequestBody @Valid ProjectRemovalRequest request) {
        User user = getAuthenticated(auth);

        Long id = request.getId();
        if (user.removeProject(id)) {
            userRepository.save(user);
            Project project = projectRepository.findById(id).orElseThrow(() -> EntityException.noSuchProject(id));
            projectRepository.deleteById(id);
            for(Long i : project.getNotes()) {
                noteRepository.deleteById(i);
            }
        } else
            throw UserException.notOwner(user.getUsername());
    }

    @ResponseBody
    @GetMapping("/project/{name}")
    public Project viewOwnProject(Authentication auth, @PathVariable String name) {
        User user = getAuthenticated(auth);
        Project project = projectRepository
                .findByName(name)
                .orElseThrow(() -> EntityException.noSuchProject(name));
        if (user.getProjects().contains(project.getId())) {
            project.clearId();
            return project;
        } else
            throw UserException.notOwner(user.getUsername());
    }

    @ResponseBody
    @GetMapping("/project/visit/{id}")
    public Project viewOtherProject(@PathVariable String id) {
        Project viewable = getViewable(id);
        viewable.clearId();
        return viewable;
    }

    @ResponseBody
    @PostMapping("/note/file")
    public NoteResponse addFileNote(@RequestParam String editorId, @RequestParam("file") MultipartFile file) throws IOException {
        Project editableProject = getEditable(editorId);

        Note note = Note.makeFileNote(file);
        return addNote(editableProject, note);
    }

    @ResponseBody
    @PostMapping("/note/text")
    public NoteResponse addTextNote(@RequestParam String editorId, @RequestBody String text) {
        Project editableProject = getEditable(editorId);

        // https://www.codejava.net/frameworks/spring-boot/spring-boot-file-upload-tutorial
        Note note = Note.makeTextNote(text);
        return addNote(editableProject, note);
    }

    @ResponseBody
    @DeleteMapping("/note/{noteId}")
    public NoteResponse removeNote(@PathVariable Long noteId, @RequestParam String editorId) {
        Project editableProject = getEditable(editorId);
        Note note = noteRepository.findById(noteId).orElseThrow(()->EntityException.noSuchNote(noteId));
        if (editableProject.removeNote(note)) {
            projectRepository.save(editableProject);
            noteRepository.deleteById(note.getId());
            return new NoteResponse(note);
        }
        throw EntityException.noSuchNote(noteId);
    }

    @ResponseBody
    @GetMapping("/note/{noteId}")
    public Note viewNote(@PathVariable Long noteId, @RequestParam String viewerOrEditorId) {
        Project viewableProject = getViewable(viewerOrEditorId);
        if (viewableProject.getNotes().contains(noteId)) {
            return noteRepository.findById(noteId).orElseThrow(() -> EntityException.noSuchNote(noteId));
        } else
            throw EntityException.noSuchNote(noteId);
    }

    private Project getEditable(String editorId) {
        return projectRepository.findByEditorId(editorId).orElseThrow(()->EntityException.cannotEditById(editorId));
    }

    private Project getViewable(String editOrViewId) {
        AtomicBoolean canSeeEditorId = new AtomicBoolean(true);
        Project project = projectRepository.findByEditorId(editOrViewId)
                .or(() -> {
                    canSeeEditorId.set(false);
                    return projectRepository.findByViewerId(editOrViewId);
                })
                .orElseThrow(()->EntityException.cannotViewById(editOrViewId));
        if (!canSeeEditorId.get()) {
            project.clearEditorId();
            project.setPermissionToView();
        }
        return project;
    }

    private NoteResponse addNote(Project project, Note note) {
        noteRepository.save(note);
        if (project.addNote(note)) {
            projectRepository.save(project);
            return new NoteResponse(note);
        } else
            throw EntityException.duplicateNote(note.getId());
    }
}
