package dev.newsletterss.api.repository;

import java.util.Optional;
import dev.newsletterss.api.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, String>{
	Optional<Member> findByUsername(String username);

}
