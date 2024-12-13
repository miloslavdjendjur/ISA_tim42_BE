package rs.ac.uns.ftn.informatika.jpa.dto;


import java.time.LocalDateTime;
import java.util.List;

public class PostViewDTO {
    private Long id;
    private String description;
    private String imagePath;
    private Long user_id;
    private String username;
    private Integer likes;
    private LocalDateTime createdDate;
    private boolean likedByUser;

    public PostViewDTO(Long id, String description, String imagePath, Long user_id, String username, Integer likes, LocalDateTime createdDate, boolean likedByUser) {
        this.id = id;
        this.description = description;
        this.imagePath = imagePath;
        this.user_id = user_id != null ? user_id : -1; // Default to -1 if user is null
        this.username = username != null ? username : "Unknown";
        this.likes = likes;
        this.createdDate = createdDate;
        this.likedByUser = likedByUser;
    }
    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Long getUserId() {
        return user_id;
    }

    public Integer getLikes() {
        return likes;
    }

    public LocalDateTime getCreatedDate(){
        return createdDate;
    }

    public boolean isLikedByUser() {
        return likedByUser;
    }

    public void setLikedByUser(boolean likedByUser) {
        this.likedByUser = likedByUser;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}

