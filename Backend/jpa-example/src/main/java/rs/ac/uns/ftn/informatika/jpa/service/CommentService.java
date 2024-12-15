package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rs.ac.uns.ftn.informatika.jpa.model.Comment;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.User;
import rs.ac.uns.ftn.informatika.jpa.repository.CommentRepository;
import rs.ac.uns.ftn.informatika.jpa.repository.UserFollowerRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserFollowerRepository userFollowerRepository;
    private final UserService userService;
    private final PostHelper postHelper;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserFollowerRepository userFollowerRepository, UserService userService, PostHelper postHelper) {
        this.commentRepository = commentRepository;
        this.userFollowerRepository = userFollowerRepository;
        this.userService = userService;
        this.postHelper = postHelper;
    }

    public Comment addComment(Long postId, Long userId, String text) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postHelper.getPostById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Prvoera da li je pratilac ili vlasnik posta
        if (!post.getUser().equals(user) && !userFollowerRepository.existsByUserIdAndFollowerId(post.getUser().getId(), user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must follow the post's owner to comment.");
        }

        // Provera za limit od 60 komentara u satu
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        long commentCount = commentRepository.countByUserIdAndCreatedTimeAfter(userId, oneHourAgo);
        if (commentCount >= 60) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only comment 60 times per hour.");
        }

        Comment comment = new Comment(text, LocalDateTime.now(), user, post);
        return commentRepository.save(comment);
    }


    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }
}
