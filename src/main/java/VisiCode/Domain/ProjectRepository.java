package VisiCode.Domain;

import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;

import java.util.Optional;

public interface ProjectRepository extends DatastoreRepository<Project, Long> {
    Optional<Project> findByEditorId(String editorId);
    Optional<Project> findByViewerId(String viewerId);
}
