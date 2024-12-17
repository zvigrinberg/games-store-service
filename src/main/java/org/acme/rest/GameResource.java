package org.acme.rest;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.acme.dto.GameDto;
import org.acme.service.GameService;
import org.eclipse.microprofile.openapi.annotations.Components;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.RestQuery;

import java.util.List;

@Path("/v1/game")
public class GameResource {

    @Inject
    private GameService gameService;
    private static final Logger LOG = Logger.getLogger(GameService.class);
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description= "This endpoint returns a game based on unique game id input", summary = "returns a game based on unique game id input")
    @APIResponse(responseCode = "200", description = "Get one game by id", content = {
            @Content(mediaType = "application/json", schema= @Schema(type = SchemaType.OBJECT))})
    @APIResponse(responseCode = "404", description = "Game not found by id", content = {
            @Content(mediaType ="text/plain", schema= @Schema(type = SchemaType.STRING))})
    @APIResponse(responseCode = "400", description = "Game unique id input is missing", content = {
            @Content(mediaType ="text/plain", schema= @Schema(type = SchemaType.STRING))})
    public GameDto getOneById(@Parameter(description = "Unique game id number", allowEmptyValue = false, required = true, example= "5", schema = @Schema(
            type = SchemaType.INTEGER))
                               Integer id) {
        LOG.info("Handling a new request get request with id " + id);
        return gameService.getOneById(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description= "This endpoint returns a game based on name of game input, this should be the short name of the game, without spaces.", summary = "returns a games according to short game name input")
    @APIResponse(responseCode = "200", description = "Get one game by short name", content = {
            @Content(mediaType = "application/json", schema= @Schema(type = SchemaType.OBJECT))})
    @APIResponse(responseCode = "404", description = "Game not found by short name", content = {
            @Content(mediaType ="text/plain", schema= @Schema(type = SchemaType.STRING))})
    @APIResponse(responseCode = "400", description = "Game short name input is missing", content = {
            @Content(mediaType ="text/plain", schema= @Schema(type = SchemaType.STRING))})
    public GameDto getOneByName(@RestQuery("name")
                                @Parameter(description = "Short name of the game without spaces", allowEmptyValue = false, required = true,example= "call-of-duty")
                                String name) {
        LOG.info("Handling a new request get request with short name= " +  name);
        return gameService.getOneByName(name);
    }

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description= "This endpoint returns a list of all games in the service' DB", summary = "returns a list of all games in the service' DB")
    @APIResponse(responseCode = "200", description = "Get All games from database as list , if DB is empty, returns an empty list", content = {
            @Content(mediaType = "application/json", schema= @Schema(type = SchemaType.ARRAY))})
    public List<GameDto> getAllGames() {
        LOG.info("Handling a new request get all games from Service");
        return gameService.getAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description= "This endpoint Receive a game DTO, and persist it into DB. returning Unique key for game entity for future references", summary = "Creates a game in the service' DB")
    @APIResponse(responseCode = "200", description = "Create a single game based on request body ,and persist it to DB, returns An object with auto generated game id unique key.", content = {
            @Content(mediaType = "application/json", schema= @Schema(type = SchemaType.OBJECT))})
    public GameDto createOne(@Valid GameDto gameDto) {
        LOG.info("Got a request to create new game");
        LOG.debugf("Creating a new game : %s", gameDto);
        return gameService.create(gameDto);

    }
    @DELETE
    @Path("{id}")
    @ResponseStatus(200)
    @Operation(description= "This endpoint Receive a game unique key id, and deletes it from DB if it exists there. returning a message that game was deleted", summary = "Deletes a game from the service' DB")
    @APIResponse(responseCode = "200", description = "delete one game by unique game id", content = {
            @Content(mediaType = "text/plain", schema= @Schema(type = SchemaType.STRING))})
    @APIResponse(responseCode = "404", description = "Game not found by game id, cannot delete", content = {
            @Content(mediaType ="text/plain", schema= @Schema(type = SchemaType.STRING))})
    @APIResponse(responseCode = "400", description = "Game id input is missing, cannot delete", content = {
            @Content(mediaType ="text/plain", schema= @Schema(type = SchemaType.STRING))})
    public String deleteGame(@Parameter(description = "Unique game id number", allowEmptyValue = false, required = true,example= "5",schema = @Schema(implementation = String.class))
                             Integer id) {
        LOG.infof("Handling a new request to delete game with id %s", id);
        gameService.delete(id);
        return "Successfully Deleted game with id= " + id;
    }

    @Operation(description= "This endpoint Updates a game according to its unique key id ( fetch it first from DB to ensure it exists), and then if exists, updates it in DB . returning a message that game was updated successfully ", summary = "Updates a game in the service' DB")
    @APIResponse(responseCode = "200", description = "update one game by unique game id", content = {
            @Content(mediaType = "text/plain", schema= @Schema(type = SchemaType.STRING))})
    @APIResponse(responseCode = "404", description = "Game not found by game id, cannot update", content = {
            @Content(mediaType ="text/plain", schema= @Schema(type = SchemaType.STRING))})
    @APIResponse(responseCode = "400", description = "Game id input is missing, cannot update", content = {
            @Content(mediaType ="text/plain", schema= @Schema(type = SchemaType.STRING))})
    @PUT
    public String updateGame(@Valid GameDto game) {
        LOG.infof("Handling a new request to update a game with id %s", game.getGameId());
        gameService.update(game);
        return "Successfully updated game with id= " + game.getGameId();
    }

}
