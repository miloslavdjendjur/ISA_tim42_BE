package rs.ac.uns.ftn.informatika.jpa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.service.LocationService;
import rs.ac.uns.ftn.informatika.jpa.service.PostService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JpaExampleApplicationTests {

	@Autowired
	private PostService postService;

	@Test
	public void contextLoads() {
		// Ensure that the context loads
	}

	@Test
	public void testConcurrentLikes() throws InterruptedException {
		Long postId = 3L;
		Long userId1 = 2L;
		Long userId2 = 3L;
		// Podsetnik: ovi ajdijevi moraju da postoje u bazi da bi radilo. LOGICNO!

		ExecutorService executorService = Executors.newFixedThreadPool(2);

		Runnable task1 = () -> postService.toggleLike(postId, userId1);
		Runnable task2 = () -> postService.toggleLike(postId, userId2);

		executorService.submit(task1);
		executorService.submit(task2);

		executorService.shutdown();
		executorService.awaitTermination(10, TimeUnit.SECONDS);

		Post post = postService.getPostById(postId)
				.orElseThrow(() -> new RuntimeException("Post with ID " + postId + " not found"));
		assert post.getLikes().size() == 2;
	}
}
