package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.model.Comment;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.User;
import rs.ac.uns.ftn.informatika.jpa.repository.CommentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostHelper postHelper;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserService userService, PostHelper postHelper) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.postHelper = postHelper;
    }

    public Comment addComment(Long postId, Long userId, String text) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postHelper.getPostById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Check if the user follows the post owner
        if (!post.getUser().getFollowers().contains(user)) {
            throw new RuntimeException("You must follow the post's owner to comment.");
        }

        // Check if the user exceeded the comment limit
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        long commentCount = commentRepository.countByUserIdAndCreatedTimeAfter(userId, oneHourAgo);
        if (commentCount >= 60) {
            throw new RuntimeException("You can only comment 60 times per hour.");
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
