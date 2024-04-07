package com.smartjobandina.user.repository;

import com.smartjobandina.user.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

  Optional<Token> findByToken(String token);
}
