package com.gamesList.gamesList.repo;

import com.gamesList.gamesList.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GamesRepo extends JpaRepository<Game, String> {
    Optional<Game> bindById(String id);
}
