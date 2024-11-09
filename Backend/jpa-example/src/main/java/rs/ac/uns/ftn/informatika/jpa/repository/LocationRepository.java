package rs.ac.uns.ftn.informatika.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.informatika.jpa.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    // Custom query methods as needed
}
