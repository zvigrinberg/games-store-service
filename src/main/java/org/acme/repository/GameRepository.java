package org.acme.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ApplicationPath;
import org.acme.model.Game;

import java.util.Objects;

@ApplicationScoped
public class GameRepository implements PanacheMongoRepositoryBase<Game,Integer> {

    public Game findByShortTitle(String shortTitle) {
        return find("shortTitle", shortTitle).firstResult();
    }

    public Game findById(Integer id) {
        Game theGame = find("id", id).firstResult();
        if (Objects.isNull(theGame)) {
            theGame = findByIdOptional(id).orElse(null);
        }

        return theGame;
    }

    public boolean deleteGameById(Integer id) {
        return delete("id",id) == 1;
    }

    public void updateGame(Game game) {
        game.update();
    }
}
