package rs.ac.uns.ftn.informatika.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.informatika.jpa.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Additional query methods if required
}
