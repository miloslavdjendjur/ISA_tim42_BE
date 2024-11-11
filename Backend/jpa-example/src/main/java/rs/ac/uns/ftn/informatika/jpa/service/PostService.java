package rs.ac.uns.ftn.informatika.jpa.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.dto.CommentDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.PostDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.PostViewDTO;
import rs.ac.uns.ftn.informatika.jpa.mapper.CommentDTOMapper;
import rs.ac.uns.ftn.informatika.jpa.model.Comment;
import rs.ac.uns.ftn.informatika.jpa.model.Location;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.User;
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

    @Autowired
    public PostService(PostRepository postRepository, UserService userService, LocationService locationService,CommentDTOMapper commentDTOMapper) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.locationService = locationService;
        this.commentDTOMapper = commentDTOMapper;
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
        List<Comment> comments = new ArrayList<>();
        List<PostViewDTO> postDTOs = new ArrayList<>();
        for (Post post : posts) {
            List<CommentDTO> commentDTOs = new ArrayList<>();
            for (Comment comment : post.getComments()) {
                CommentDTO commentDTO = commentDTOMapper.fromCommentToDTO(comment);
                commentDTOs.add(commentDTO);
            }
            PostViewDTO postDTO = new PostViewDTO(
                    post.getId(),
                    post.getDescription(),
                    post.getImagePath(),
                    post.getUser().getId(),
                    post.getLikes().size(),
                    commentDTOs
            );
            postDTOs.add(postDTO);
        }
        return postDTOs;
    }
}
