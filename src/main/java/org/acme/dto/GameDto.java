package org.acme.dto;


import jakarta.validation.constraints.*;
import org.acme.model.Game;
import org.acme.model.GameGenre;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link Game}
 */
public class GameDto implements Serializable {
    @Parameter(description = "unique game id", allowEmptyValue = true, required = false)
    private Integer gameId;
    @NotBlank
    @Parameter(description = "Full name of the game", allowEmptyValue = false, required = true, example= "Call Of Duty")
    private String name;
    @NotBlank
    @Parameter(description = "Short name of the game without spaces", allowEmptyValue = false, required = true,example= "call-of-duty")
    private String shortName;

//    private transient final String enumsRegex = Arrays.stream(GameGenre.class.getEnumConstants()).map(Enum::toString).collect(Collectors.joining("|"));
    @Pattern(message = "Game Genre must be one of a predefined Genres" , regexp = "ARCADE|SHOOTING|PLATFORM|STRATEGY|SPORT|FIGHT")
    @Parameter(description = "Game Genre", allowEmptyValue = false, required = true,example= "FIGHT", schema = @Schema(type = SchemaType.STRING ))
    private  String genre;
    @NotBlank
    @Parameter(description = "Free text of Game Description", allowEmptyValue = false, required = true,example= "Very challenging and acclaimed fighting game!")
    private String description;
    @Min(message = "age Restriction, age value must meet minimum requirements", value = 2 )
    @Max(message = "age Restirction maximum value violated", value = 21)
    @Parameter(description = "What is the minimum age for which the game is allowed", allowEmptyValue = false, required = true,example= "12",schema = @Schema(implementation = Integer.class))
    private Integer ageRestriction;

    public GameDto(Integer gameId, java.lang.String name, java.lang.String shortName, String genre, java.lang.String description, Integer ageRestriction) {
        this.gameId = gameId;
        this.name = name;
        this.shortName = shortName;
        this.genre = genre;
        this.description = description;
        this.ageRestriction = ageRestriction;

    }

    public GameDto() {
    }

    public Integer getGameId() {
        return gameId;
    }

    public java.lang.String getName() {
        return name;
    }

    public java.lang.String getShortName() {
        return shortName;
    }

    public String getGenre() {
        return genre;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public Integer getAgeRestriction() {
        return ageRestriction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;
        GameDto entity = (GameDto) o;
        return Objects.equals(this.gameId, entity.gameId) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.shortName, entity.shortName) &&
                Objects.equals(this.genre, entity.genre) &&
                Objects.equals(this.description, entity.description) &&
                Objects.equals(this.ageRestriction, entity.ageRestriction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, name, shortName, genre, description, ageRestriction);
    }

    @Override
    public java.lang.String toString() {
        return getClass().getSimpleName() + "(" +
                "gameId = " + gameId + ", " +
                "name = " + name + ", " +
                "shortName = " + shortName + ", " +
                "genre = " + genre + ", " +
                "description = " + description + ", " +
                "ageRestriction = " + ageRestriction + ")";
    }
}