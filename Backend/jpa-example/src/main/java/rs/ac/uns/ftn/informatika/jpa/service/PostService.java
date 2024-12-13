package rs.ac.uns.ftn.informatika.jpa.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.informatika.jpa.dto.CommentDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.PostDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.PostViewDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.WriteCommentDTO;
import rs.ac.uns.ftn.informatika.jpa.mapper.CommentDTOMapper;
import rs.ac.uns.ftn.informatika.jpa.model.*;
import rs.ac.uns.ftn.informatika.jpa.repository.CommentRepository;
import rs.ac.uns.ftn.informatika.jpa.repository.PostRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;import java.util.stream.Collectors;


@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final ImageService imageService;
    private final LocationService locationService;
    private final CommentService commentService;
    private final CommentDTOMapper commentDTOMapper;

    @Autowired
    public PostService(PostRepository postRepository, UserService userService, LocationService locationService,
                       CommentDTOMapper commentDTOMapper, ImageService imageService, CommentService commentService) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.locationService = locationService;
        this.commentDTOMapper = commentDTOMapper;
        this.imageService = imageService;
        this.commentService = commentService;
    }

    @org.springframework.transaction.annotation.Transactional
    public PostDTO createPost(PostDTO postDTO, MultipartFile file, Double latitude, Double longitude, String address) throws IOException {
        Long userId = postDTO.getUserId();
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Image image = imageService.saveImage(file);

        // Create or fetch location
        Location location = locationService.createLocation(latitude, longitude, address);

        // Create post
        Post post = new Post();
        post.setDescription(postDTO.getDescription());
        post.setCreatedTime(LocalDateTime.now());
        post.setUser(user);
        post.setImage(image);
        post.setLocation(location); // Associate the location with the post

        Post savedPost = postRepository.save(post);

        postDTO.setId(savedPost.getId());
        postDTO.setImageId(image.getId());
        return postDTO;
    }

    @Transactional
    public Optional<Post> getPostById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        post.ifPresent(p -> Hibernate.initialize(p.getLikes())); // Ensure likes are initialized
        return post;
    }

    @Transactional
    public List<PostViewDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        posts.forEach(post -> Hibernate.initialize(post.getComments()));
        //List<Comment> comments = new ArrayList<>();
        List<PostViewDTO> postDTOs = new ArrayList<>();
        for (Post post : posts) {
            PostViewDTO postDTO = new PostViewDTO(
                    post.getId(),
                    post.getDescription(),
                    post.getImage().getPath(),
                    post.getUser().getId(),
                    post.getLikes().size(),
                    post.getCreatedTime()
            );
            postDTOs.add(postDTO);
        }
        return postDTOs;
    }

    @Transactional
    public List<CommentDTO> getPostComments(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        Hibernate.initialize(post.getComments());

        return post.getComments().stream()
                .sorted((c1, c2) -> c2.getCreatedTime().compareTo(c1.getCreatedTime())) // Sort by newest
                .map(comment -> new CommentDTO(
                        comment.getId(),
                        comment.getText(),
                        comment.getCreatedTime(),
                        comment.getUser().getId(),
                        comment.getPost().getId(),
                        comment.getUser().getUsername()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Fetch and delete the associated location
        Location location = post.getLocation();
        if (location != null) {
            locationService.deleteLocation(location.getId()); // Trigger location deletion and cache eviction
        }

        // Delete the post itself
        postRepository.deleteById(postId);
    }

    @Transactional
    public void addCommentToPost(WriteCommentDTO commentDTO) {
        commentService.addComment(commentDTO.getPostId(), commentDTO.getUserId(), commentDTO.getText());
    }

    @Transactional
    public ResponseEntity<Map<String, String>> toggleLike(Long postId, Long userId) {
        Map<String, String> response = new HashMap<>();

        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        synchronized (this) {
            boolean alreadyLiked = post.getLikes().contains(user);

            if (alreadyLiked) {
                response.put("message", "Already liked");
            } else {
                post.getLikes().add(user);
                postRepository.incrementLikes(postId); // Ensure database-level atomicity
                response.put("message", "Post liked");
            }
            response.put("likesCount", String.valueOf(post.getLikes().size()));
        }

        return ResponseEntity.ok(response);
    }
}
