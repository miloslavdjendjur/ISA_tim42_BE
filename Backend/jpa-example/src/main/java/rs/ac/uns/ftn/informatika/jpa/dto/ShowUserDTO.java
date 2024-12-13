package rs.ac.uns.ftn.informatika.jpa.dto;

import java.util.List;

public class ShowUserDTO {
    private Long id;
    private String fullName;
    private String email;
    private long postNumber;
    private int numberOfFollowers;
    private int followsPeople;
    private String username;
    private List<Long> followerIds;

    public ShowUserDTO(Long id, String fullName, String email, long postNumber, int followsPeople,String username,int numberOfFollowers,List<Long> followerIds) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.postNumber = postNumber;
        this.followsPeople = followsPeople;
        this.username = username;
        this.numberOfFollowers = numberOfFollowers;
        this.followerIds = followerIds;
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
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public int getNumberOfFollowers() { return numberOfFollowers; }
    public void setNumberOfFollowers(int numberOfFollowers) { this.numberOfFollowers = numberOfFollowers; }
    public List<Long> getFollowerIds() { return followerIds; }

    public void setFollowerIds(List<Long> followerIds) { this.followerIds = followerIds; }
}
