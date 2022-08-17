package springc1.miniproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springc1.miniproject.domain.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByModifiedAtDesc();
}
