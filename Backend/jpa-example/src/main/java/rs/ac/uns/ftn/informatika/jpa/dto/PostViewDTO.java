package rs.ac.uns.ftn.informatika.jpa.dto;


import java.util.List;

public class PostViewDTO {
    private Long id;
    private String description;
    private String imagePath;
    private Long user_id;
    private Integer likes;
    private List<CommentDTO> comments;

    public PostViewDTO(Long id, String description, String imagePath, Long user_id, Integer likes, List<CommentDTO> comments) {
        this.id = id;
        this.description = description;
        this.imagePath = imagePath;
        this.user_id = user_id;
        this.likes = likes;
        this.comments = comments;
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

    public List<CommentDTO> getComments() {
        return comments;
    }
}

