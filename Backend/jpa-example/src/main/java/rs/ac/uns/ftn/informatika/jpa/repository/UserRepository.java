package rs.ac.uns.ftn.informatika.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.informatika.jpa.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Additional query methods can be added here, if needed
    User findByEmail(String email);
}

