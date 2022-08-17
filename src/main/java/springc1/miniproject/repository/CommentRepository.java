package springc1.miniproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springc1.miniproject.domain.Comment;
import springc1.miniproject.domain.Post;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);
}