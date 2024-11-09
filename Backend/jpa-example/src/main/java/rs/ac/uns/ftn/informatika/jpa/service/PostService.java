package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.model.Location;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.User;
import rs.ac.uns.ftn.informatika.jpa.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final LocationService locationService;

    @Autowired
    public PostService(PostRepository postRepository, UserService userService, LocationService locationService) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.locationService = locationService;
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

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
}
