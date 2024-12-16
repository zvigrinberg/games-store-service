package org.acme.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ApplicationPath;
import org.acme.model.Game;

@ApplicationScoped
public class GameRepository implements PanacheMongoRepositoryBase<Game,Integer> {

    public Game findByShortTitle(String shortTitle) {
        return find("shortTitle", shortTitle).firstResult();
    }

    public Game findById(Integer id) {
        return find("id", id).firstResult();


    }

    public boolean deleteGameById(Integer id) {
        return delete("id",id) == 1;
    }

    public void updateGame(Game game) {
        game.update();
    }
}
