package com.gamesList.gamesList.repo;

import com.gamesList.gamesList.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GamesRepo extends JpaRepository<Game, String> {
    Optional<Game> findById(String id);
}
