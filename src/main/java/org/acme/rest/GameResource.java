package org.acme.rest;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.acme.dto.GameDto;
import org.acme.service.GameService;
import org.jboss.resteasy.reactive.RestQuery;

import java.util.List;

@Path("/v1/game")
public class GameResource {

    @Inject
    private GameService gameService;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public GameDto getOneById(Integer id) {
        return gameService.getOneById(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public GameDto getOneByName(@RestQuery("name") String name) {
        return gameService.getOneByName(name);
    }

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<GameDto> getAllGames() {
        return gameService.getAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public GameDto createOne(@Valid GameDto gameDto) {
        return gameService.create(gameDto);

    }
    @DELETE()
    @Path("{id}")
    public String deleteGame(Integer id) {
        gameService.delete(id);
        return "Successfully Deleted game with id= " + id;
    }

    @PUT
    public String updateGame(@Valid GameDto game) {
        gameService.update(game);
        return "Successfully updated game with id= " + game.getGameId();
    }

}
