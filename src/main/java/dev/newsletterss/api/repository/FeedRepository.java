package dev.newsletterss.api.repository;

import dev.newsletterss.api.entity.Rss;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface FeedRepository extends JpaRepository<Rss, String>{

    Optional<Rss> findByfeedName(String rssFeedNm);

    List<Rss> findAll();
}
