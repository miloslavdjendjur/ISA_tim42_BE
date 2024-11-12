package rs.ac.uns.ftn.informatika.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.informatika.jpa.dto.CommentDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.PostViewDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.WriteCommentDTO;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.service.PostService;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestParam("description") String description,
                                           @RequestParam("latitude") double latitude,
                                           @RequestParam("longitude") double longitude,
                                           @RequestParam("address") String address,
                                           @RequestParam("file") MultipartFile file,
                                           @RequestParam("userId") Long userId) {
        try {
            // Save the image file
            String uploadDir = "uploads/images/";
            String filePath = uploadDir + file.getOriginalFilename();
            File destinationFile = new File(filePath);
            file.transferTo(destinationFile);

            // Create post
            Post post = postService.createPost(description, filePath, latitude, longitude, address, userId);
            return new ResponseEntity<>(post, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Optional<Post> post = postService.getPostById(id);
        return post.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Add this method for GET /api/posts to fetch all posts
    @GetMapping("/all")
    public ResponseEntity<List<PostViewDTO>> getAllPosts() {
        List<PostViewDTO> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }
    @GetMapping("/all-comments/{id}")
    public ResponseEntity<List<CommentDTO>> getAllComments(@PathVariable Long id){
        List<CommentDTO> comments = postService.getPostComments(id);
        return ResponseEntity.ok(comments);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/add-comment")
    public ResponseEntity<Post> addComment(@RequestBody WriteCommentDTO comment) {
        postService.addCommentToPost(comment);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/like/{postId}")
    public ResponseEntity<Map<String, String>> toggleLike(@PathVariable Long postId, @RequestParam Long userId) {
        //String response = postService.toggleLike(postId, userId);
        return postService.toggleLike(postId, userId);
    }
}
