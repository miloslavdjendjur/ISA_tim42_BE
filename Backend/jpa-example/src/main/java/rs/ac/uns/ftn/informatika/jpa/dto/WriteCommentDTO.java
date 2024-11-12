package rs.ac.uns.ftn.informatika.jpa.dto;

import java.time.LocalDateTime;

public class WriteCommentDTO {
    private String text;
    private LocalDateTime createdTime;
    private Long userId;
    private Long postId;

    public WriteCommentDTO() {}

    public WriteCommentDTO(String text, LocalDateTime createdTime, Long userId, Long postId) {
        this.text = text;
        this.createdTime = createdTime;
        this.userId = userId;
        this.postId = postId;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getPostId() {
        return postId;
    }
    public void setPostId(Long postId) {
        this.postId = postId;
    }

}
