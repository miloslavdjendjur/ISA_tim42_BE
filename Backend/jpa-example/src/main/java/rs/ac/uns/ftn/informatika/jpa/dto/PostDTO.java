package rs.ac.uns.ftn.informatika.jpa.dto;

import rs.ac.uns.ftn.informatika.jpa.model.Comment;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.User;

import java.time.LocalDateTime;
import java.util.Set;

public class PostDTO {

    private Long id;
    private String description;
    private String imagePath;
    private LocalDateTime createdTime;
    private LocationDTO location;
    private User user; // Using User directly instead of UserDTO
    private Set<User> likes;
    private Set<Comment> comments;

    public PostDTO(Post post) {
        this.id = post.getId();
        this.description = post.getDescription();
        this.imagePath = post.getImagePath();
        this.createdTime = post.getCreatedTime();
        this.location = new LocationDTO(post.getLocation());
        this.user = post.getUser(); // Direct reference to User
        this.likes = post.getLikes();
        this.comments = post.getComments();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public User getUser() {
        return user;
    }

    public Set<User> getLikes() {
        return likes;
    }

    public Set<Comment> getComments() {
        return comments;
    }
}

