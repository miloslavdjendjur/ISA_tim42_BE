package rs.ac.uns.ftn.informatika.jpa.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.informatika.jpa.dto.CommentDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.PostDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.PostViewDTO;
import rs.ac.uns.ftn.informatika.jpa.mapper.CommentDTOMapper;
import rs.ac.uns.ftn.informatika.jpa.model.Comment;
import rs.ac.uns.ftn.informatika.jpa.model.Image;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.User;
import rs.ac.uns.ftn.informatika.jpa.repository.PostRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final ImageService imageService;
    private final LocationService locationService;
    private final CommentDTOMapper commentDTOMapper;

    @Autowired
    public PostService(PostRepository postRepository, UserService userService, LocationService locationService,
                       CommentDTOMapper commentDTOMapper, ImageService imageService) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.locationService = locationService;
        this.commentDTOMapper = commentDTOMapper;
        this.imageService = imageService;
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
            List<CommentDTO> commentDTOs = new ArrayList<>();
            for (Comment comment : post.getComments()) {
                //CommentDTO commentDTO = commentDTOMapper.fromCommentToDTO(comment);
                CommentDTO commentDTO = new CommentDTO(comment.getId(),comment.getText(),comment.getCreatedTime(),comment.getUser().getId(),
                        comment.getPost().getId());
                commentDTOs.add(commentDTO);
            }
            PostViewDTO postDTO = new PostViewDTO(
                    post.getId(),
                    post.getDescription(),
                    post.getImage().getPath(),
                    post.getUser().getId(),
                    post.getLikes().size(),
                    commentDTOs
            );
            postDTOs.add(postDTO);
        }
        return postDTOs;
    }
}
