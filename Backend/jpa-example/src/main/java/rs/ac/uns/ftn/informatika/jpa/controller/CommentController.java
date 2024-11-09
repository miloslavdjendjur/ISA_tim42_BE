package rs.ac.uns.ftn.informatika.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.informatika.jpa.dto.CommentDTO;
import rs.ac.uns.ftn.informatika.jpa.mapper.CommentDTOMapper;
import rs.ac.uns.ftn.informatika.jpa.model.Comment;
import rs.ac.uns.ftn.informatika.jpa.service.CommentService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final CommentDTOMapper commentDTOMapper;

    @Autowired
    public CommentController(CommentService commentService, CommentDTOMapper commentDTOMapper) {
        this.commentService = commentService;
        this.commentDTOMapper = commentDTOMapper;
    }

    @PostMapping
    public ResponseEntity<CommentDTO> addComment(@RequestParam Long postId,
                                                 @RequestParam Long userId,
                                                 @RequestParam String text) {
        Comment comment = commentService.addComment(postId, userId, text);
        CommentDTO commentDTO = commentDTOMapper.fromCommentToDTO(comment);
        return new ResponseEntity<>(commentDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable Long id) {
        Optional<Comment> comment = commentService.getCommentById(id);
        return comment.map(value -> new ResponseEntity<>(commentDTOMapper.fromCommentToDTO(value), HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Comment>> getAllComments() {
        List<Comment> comments = commentService.getAllComments();
        return ResponseEntity.ok(comments);
    }
}
