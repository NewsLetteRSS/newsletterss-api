package dev.newsletterss.api.repository;

import dev.newsletterss.api.entity.TokenStorage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TokenStorageRepository extends JpaRepository<TokenStorage, String>{

}
