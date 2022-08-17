package springc1.miniproject.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import springc1.miniproject.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

}
