package rs.ac.uns.ftn.informatika.jpa.dto;

public class ShowUserDTO {
    private Long id;
    private String fullName;
    private String email;
    private long postNumber;
    private int followsPeople;

    public ShowUserDTO(Long id, String fullName, String email, long postNumber, int followsPeople) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.postNumber = postNumber;
        this.followsPeople = followsPeople;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public long getPostNumber() {
        return postNumber;
    }
    public void setPostNumber(long postNumber) {
        this.postNumber = postNumber;
    }
    public int getFollowsPeople() {
        return followsPeople;
    }
    public void setFollowsPeople(int followsPeople) {
        this.followsPeople = followsPeople;
    }
}
