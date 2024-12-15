package rs.ac.uns.ftn.informatika.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.informatika.jpa.model.Post;

import java.time.LocalDateTime;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // Custom query methods can go here, if needed
    long countByUserId(Long userId);

    @Modifying
    @Query("UPDATE Post p SET p.likesCount = p.likesCount + 1 WHERE p.id = :postId")
    int incrementLikes(@Param("postId") Long postId);

    @Modifying
    @Query("UPDATE Post p SET p.likesCount = p.likesCount - 1 WHERE p.id = :postId")
    int decrementLikes(@Param("postId") Long postId);

    @Query("SELECT COUNT(pl) FROM Post p JOIN p.likes pl WHERE p.user.id = :userId AND p.createdTime > :since")
    long countLikesInLast7Days(@Param("userId") Long userId, @Param("since") LocalDateTime since);

    @Query("SELECT COUNT(p) FROM Post p WHERE p.user.id = :userId AND p.createdTime > :since")
    long countByUserIdAndCreatedTimeAfter(@Param("userId") Long userId, @Param("since") LocalDateTime since);
}
