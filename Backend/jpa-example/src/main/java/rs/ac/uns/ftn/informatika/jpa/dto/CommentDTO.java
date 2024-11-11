package rs.ac.uns.ftn.informatika.jpa.dto;

import java.time.LocalDateTime;

public class CommentDTO {
    private Long id;
    private String text;
    private LocalDateTime createdTime;
    private Long userId;
    private Long postId;

    public CommentDTO() {
    }

    public CommentDTO(Long id, String text, LocalDateTime createdTime, Long userId, Long postId) {
        this.id = id;
        this.text = text;
        this.createdTime = createdTime;
        this.userId = userId;
        this.postId = postId;
    }
    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getPostId() {
        return postId;
    }
}
