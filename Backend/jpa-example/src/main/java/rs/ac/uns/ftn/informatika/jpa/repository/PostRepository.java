package rs.ac.uns.ftn.informatika.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.informatika.jpa.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // Custom query methods can go here, if needed
}
