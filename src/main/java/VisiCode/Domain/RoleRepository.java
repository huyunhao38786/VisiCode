package LocationSearch.Domain;

import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;

import java.util.Optional;

public interface RoleRepository extends DatastoreRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
