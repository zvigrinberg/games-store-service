package org.acme.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.acme.mapper.ModelDtoMapper;
import org.acme.model.Game;
import org.acme.dto.GameDto;
import org.acme.repository.GameRepository;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Objects;

import static config.AppStartupLifecycleHook.getBuildVersion;

@Alternative
@Priority(1)
@Singleton
public class GameServiceMongo implements GameService {

    @Inject
    private GameRepository gameRepository;
    private static final Logger LOG = Logger.getLogger(GameServiceMongo.class);

    @Override
    public GameDto getOneById(Integer id) {
        checkIfInputNotMissing(id);
        Game game = gameRepository.findById(id);
        if (Objects.isNull(game) ) {
            throwNotFoundException(id, "Game not found");
        }
        return ModelDtoMapper.toDto(game);

    }


    @Override
    public GameDto getOneByName(String name) {
        checkIfInputNotMissing(name);
        Game game = gameRepository.findByShortTitle(name);
        if (Objects.isNull(game) ) {
            throw new NotFoundException("Game not found by shortTitle: " + name);
        }
        return ModelDtoMapper.toDto(game);
    }

    @Override
    public List<GameDto> getAll() {
        return gameRepository.listAll().stream().map(ModelDtoMapper::toDto).toList();
    }

    @Override
    public GameDto create(GameDto game) {
        Game gameInDbModel = ModelDtoMapper.toGame(game);
        Integer maximum;
        if(gameRepository.listAll().size() > 0) {
            maximum = gameRepository.listAll().stream().map(Game::getId).max(Integer::compare).get();
        }
        else
        {
            maximum = 0;
        }
        gameInDbModel.setId(++maximum);
        gameRepository.persist(gameInDbModel);
        return ModelDtoMapper.toDto(gameInDbModel);

    }

    @Override
    public void update(GameDto game) {
        Game gameInDbModel = ModelDtoMapper.toGame(game);
        if (gameInDbModel.getId() != null) {
            Game gameFromDB = gameRepository.findById((gameInDbModel.getId()));
            if (Objects.nonNull(gameFromDB) ) {
                gameRepository.updateGame(gameInDbModel);
            }
            else {
                throwNotFoundException(game.getGameId() ,", update is not possible");
            }
        }
        else {
            checkIfInputNotMissing(game.getGameId());
        }
    }

    @Override
    public void delete(Integer id) {
        checkIfInputNotMissing(id);
        Game gameFromDB = gameRepository.findById(id);
        if (Objects.nonNull(gameFromDB)) {
            gameRepository.deleteGameById(id);
        }
        else {
            throwNotFoundException(id,", delete is not possible");
        }
    }

    private static void throwNotFoundException(Integer id, String operationSuffixMessage) {
        throw new NotFoundException("Can't find gameId=" + id + " in DB , " + operationSuffixMessage);
    }

    private static void checkIfInputNotMissing(Integer id) {
        if (Objects.isNull(id) || id <= 0) {
            throw new BadRequestException("game id must be not empty");
        }
    }
    private static void checkIfInputNotMissing(String name) {
        if (Objects.isNull(name) || name.trim() == "") {
            throw new BadRequestException("game name must be populated");
        }
    }
    @PostConstruct
    public void startUp() {
        LOG.infof("Welcome! , games-store MongoDB Service started!!, Service API build version=%s", getBuildVersion());
    }


}
