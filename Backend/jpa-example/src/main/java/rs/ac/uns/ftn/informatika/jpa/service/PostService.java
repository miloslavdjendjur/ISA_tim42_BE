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
import rs.ac.uns.ftn.informatika.jpa.model.Comment;
import rs.ac.uns.ftn.informatika.jpa.model.Image;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.User;
import rs.ac.uns.ftn.informatika.jpa.repository.CommentRepository;
import rs.ac.uns.ftn.informatika.jpa.repository.PostRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final ImageService imageService;
    private final LocationService locationService;
    private final CommentDTOMapper commentDTOMapper;
    private final CommentRepository commentRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserService userService, LocationService locationService,
                       CommentDTOMapper commentDTOMapper, ImageService imageService, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.locationService = locationService;
        this.commentDTOMapper = commentDTOMapper;
        this.imageService = imageService;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public PostDTO createPost(PostDTO postDTO, MultipartFile file) throws IOException {
        User user = userService.getUserById(1L)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Image image = imageService.saveImage(file);

        Post post = new Post();
        post.setDescription(postDTO.getDescription());
        post.setCreatedTime(LocalDateTime.now());
        post.setUser(user);
        post.setImage(image);

        Post savedPost = postRepository.save(post);

        postDTO.setId(savedPost.getId());
        postDTO.setImageId(image.getId());
        return postDTO;
    }


    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
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
    public List<CommentDTO> getPostComments(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        Hibernate.initialize(post.getComments());
        List<CommentDTO> commentDTOs = new ArrayList<>();
        for (Comment comment : post.getComments()){
            CommentDTO commentDTO = new CommentDTO(comment.getId(),comment.getText(),comment.getCreatedTime(),comment.getUser().getId(),
                    comment.getPost().getId(),comment.getUser().getUsername());
            commentDTOs.add(commentDTO);
        }
        return commentDTOs;
    }
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }
    @Transactional
    public void addCommentToPost(WriteCommentDTO commentDTO) {
        Post post = postRepository.findById(commentDTO.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + commentDTO.getPostId()));
        User user = userService.getUserById(commentDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        //Hibernate.initialize(post.getComments());
        Comment comment = new Comment(commentDTO.getText(), commentDTO.getCreatedTime(), user, post);
        post.getComments().add(comment);

        postRepository.save(post);
    }
    /*@Transactional
    public Post likePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (!post.getLikes().contains(user)) {
            post.getLikes().add(user);
            postRepository.save(post);
        }
        return post;
    }*/
    @Transactional
    public ResponseEntity<Map<String, String>> toggleLike(Long postId, Long userId) {
        Map<String, String> response = new HashMap<>();
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (post.getLikes().contains(user)) {
            response.put("message", "Already liked");
        } else {
            post.getLikes().add(user);
            postRepository.save(post);
            response.put("message", "Post liked");
        }
        response.put("likesCount", String.valueOf(post.getLikes().size()));
        return ResponseEntity.ok(response);
    }


}
