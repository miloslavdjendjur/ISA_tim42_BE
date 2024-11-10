package rs.ac.uns.ftn.informatika.jpa.service;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.informatika.jpa.dto.PostDTO;
import rs.ac.uns.ftn.informatika.jpa.model.Location;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.User;
import rs.ac.uns.ftn.informatika.jpa.repository.PostRepository;

import java.io.File;
import java.io.IOException;
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

    private static final String DEFAULT_LATITUDE = "45.2671";
    private static final String DEFAULT_LONGITUDE = "19.8335";
    private static final String DEFAULT_ADDRESS = "Novi Sad, Serbia";

    public PostDTO createPost(String description, MultipartFile file, String latitude, String longitude, String address, Long userId) {
        User user = userId != null ? userService.findById(userId) : null;
        if (user == null) {
            user = userService.findById(1L);
            if (user == null) {
                throw new RuntimeException("Default user not found. Please ensure a default user with ID 1 exists in the database.");
            }
        }

        Location location = locationService.createOrRetrieveLocation(
                latitude != null ? latitude : DEFAULT_LATITUDE,
                longitude != null ? DEFAULT_LONGITUDE : DEFAULT_LONGITUDE,
                address != null ? address : DEFAULT_ADDRESS
        );

        Post post = new Post();
        post.setDescription(description);
        post.setUser(user);
        post.setLocation(location);
        post.setCreatedTime(LocalDateTime.now());

        if (file != null && !file.isEmpty()) {
            post.setImagePath(uploadImage(file));
        }

        postRepository.save(post);
        return new PostDTO(post);
    }

    private String uploadImage(MultipartFile file) {
        try {
            // Define the directory where images will be stored
            String directoryPath = "C:/Users/jocic/Uploads"; // Use an absolute path or a known directory
            File directory = new File(directoryPath);

            // Check if directory exists; if not, create it
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Define the file name and create the file within the directory
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File destinationFile = new File(directory, fileName);

            // Transfer the file to the target location
            file.transferTo(destinationFile);

            // Return the file path relative to the directory
            return destinationFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
}
