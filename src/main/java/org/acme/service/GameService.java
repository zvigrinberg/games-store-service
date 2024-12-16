package org.acme.service;

import org.acme.dto.GameDto;

import java.util.List;

public interface GameService {

    public GameDto getOneById(Integer id);
    public List<GameDto> getAll();
    public GameDto create(GameDto game);
    public void update(GameDto game);
    public void delete(Integer id);

    public GameDto getOneByName(String name);
}
