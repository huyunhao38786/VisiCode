package VisiCode.Domain;

import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;

import java.util.Optional;

public interface UserRepository extends DatastoreRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
