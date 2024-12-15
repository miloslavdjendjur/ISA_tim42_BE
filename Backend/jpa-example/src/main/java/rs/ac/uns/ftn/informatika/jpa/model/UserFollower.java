package rs.ac.uns.ftn.informatika.jpa.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_followers")
public class UserFollower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    @Column(name = "followed_since", nullable = false)
    private LocalDateTime followedSince = LocalDateTime.now();

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public LocalDateTime getFollowedSince() {
        return followedSince;
    }

    public void setFollowedSince(LocalDateTime followedSince) {
        this.followedSince = followedSince;
    }
}
