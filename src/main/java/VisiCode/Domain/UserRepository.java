package VisiCode.Domain;

import com.google.cloud.datastore.Key;
import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;

import java.util.Optional;

public interface UserRepository extends DatastoreRepository<User, Key> {
    Optional<User> findByUsername(String username);
}
