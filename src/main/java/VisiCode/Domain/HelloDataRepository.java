package LocationSearch.Domain;


import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;

public interface HelloDataRepository extends DatastoreRepository<HelloData, Long> {
}
