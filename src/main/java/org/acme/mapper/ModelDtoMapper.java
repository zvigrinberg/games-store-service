package org.acme.mapper;

import org.acme.model.Game;
import org.acme.dto.GameDto;
import org.acme.model.GameGenre;

public class ModelDtoMapper {

    public static GameDto toDto(Game game) {
        return new GameDto(game.getId(),game.getTitle(),game.getShortTitle(),game.getGenre().name(),game.getDescription(), game.getOverAgeRestriction());

    }

    public static Game toGame(GameDto gameDto) {
        return new Game(gameDto.getGameId(), gameDto.getName(), gameDto.getShortName(), GameGenre.valueOf(gameDto.getGenre()),gameDto.getDescription(),gameDto.getAgeRestriction());
    }
}
