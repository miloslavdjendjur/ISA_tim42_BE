package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.repository.PostRepository;

import java.util.Optional;

@Service
public class PostHelper {

    private final PostRepository postRepository;

    @Autowired
    public PostHelper(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Optional<Post> getPostById(Long postId) {
        return postRepository.findById(postId);
    }
}
