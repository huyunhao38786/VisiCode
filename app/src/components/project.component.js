import React, {useEffect, useState} from "react";
import "../Project.css"
import {useParams} from "react-router-dom";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";
import Id from "./id.component";
import NoteAdd from "./note/NoteAdd/NoteAdd.component"
import "./note/note.css"
import "./project.component.css";

function projApi(str) {
    return `/api/project${str}`
}

function noteApi(str) {
    return `/api/note${str}`
}

function Project(props) {
    const {projectName} = useParams();
    const external = sessionStorage.getItem('external');
    const [project, setProject] = useState(null);
    const [noteBookData, setNoteBookData] = useState([]);

    const deleteNote = (id) => {
        console.log(id);
        console.log(project?.editorId);
        axios
            .delete(noteApi(`/${id}`), {params: {editorId: project?.editorId}})
            .then(response => {
                if (response.data.error == null) {
                    // setProject(response.data)
                } else {
                    props.router.navigate("/projects")
                }
            });
    };

    const viewNote = (id) => {
        let text = "";
        axios
            .get(noteApi(`/${id}`), {params: {viewerOrEditorId: project?.editorId}})
            .then(response => {
                if (response.data.error == null) {
                    text = response.data;
                } else {
                    props.router.navigate("/projects")
                }
            });
        return text;
    }
    // component did mount / unmount
    useEffect(() => {
        if (external && external !== '') {
            // using other person's project
            // the permission will be set in project.permission as either view or edit
            axios
                .get(projApi(`/visit/${external}`))
                .then(response => {
                    if (response.data.error == null) {
                        setProject(response.data)
                    } else {
                        props.router.navigate("/projects")
                    }
                });
        } else {
            // viewing my own project
            axios
                .get(projApi(`/${projectName}`))
                .then(response => {
                    if (response.data.error == null) {
                        setProject(response.data)
                        console.log(response.data)
                    } else {
                        props.router.navigate("/projects")
                    }
                });
        }
        return function cleanup() {
            sessionStorage.removeItem('external');
        }
    }, []);

    return <div className="app">
        <h1>{projectName}</h1>
        <div className="project info">
            {project?.viewerId && <Id label="View with id" value={project.viewerId}/>}
            {project?.editorId && <Id label="Edit with id" value={project.editorId}/>}
        </div>
        <div className="note-section">
            <NoteAdd editorId={project?.editorId}/>
            <section className="notebook-container">
                <div className="notebook">

                    {
                        // (project?.notes && <Notes
                        //     link={ project?.editorId || project?.viewerId }
                        //     deletable={ project?.editorId }
                        //     notes = {project?.notes}/>)

                        project?.notes.map((note) => (
                            <React.Fragment key={1}>
                                <div className="notebookInfo" key={note}>
                                    <div className="notebookInfo-title">
                                        <h3>{note}</h3>
                                        <div
                                            className="remove"
                                            onClick={() => deleteNote(note)}
                                        >
                                            🗑️
                                        </div>
                                    </div>
                                    <div className="notebookInfo-description">
                                        <p>{viewNote(note)}</p>
                                    </div>
                                </div>
                            </React.Fragment>
                        ))
                    }
                </div>
            </section>
        </div>
    </div>
}

export default Project;