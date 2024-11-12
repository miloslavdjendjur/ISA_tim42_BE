package rs.ac.uns.ftn.informatika.jpa.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.dto.CommentDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.PostDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.PostViewDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.WriteCommentDTO;
import rs.ac.uns.ftn.informatika.jpa.mapper.CommentDTOMapper;
import rs.ac.uns.ftn.informatika.jpa.model.Comment;
import rs.ac.uns.ftn.informatika.jpa.model.Location;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.User;
import rs.ac.uns.ftn.informatika.jpa.repository.CommentRepository;
import rs.ac.uns.ftn.informatika.jpa.repository.PostRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final LocationService locationService;
    private final CommentDTOMapper commentDTOMapper;
    private final CommentRepository commentRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserService userService, LocationService locationService, CommentDTOMapper commentDTOMapper, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.locationService = locationService;
        this.commentDTOMapper = commentDTOMapper;
        this.commentRepository = commentRepository;
    }

    public Post createPost(String description, String imagePath, double latitude, double longitude, String address, Long userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Location location = locationService.createLocation(latitude, longitude, address);

        Post post = new Post(description, imagePath, LocalDateTime.now(), user, location);
        return postRepository.save(post);
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
                    post.getImagePath(),
                    post.getUser().getId(),
                    post.getLikes().size()
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
        Hibernate.initialize(post.getComments());
        Comment comment = new Comment(commentDTO.getText(), commentDTO.getCreatedTime(), user, post);
        post.getComments().add(comment); // Dodaj komentar u listu komentara posta

        postRepository.save(post); // Sačuvaj post (Hibernate će sačuvati komentar zbog CascadeType.ALL)
    }

}
