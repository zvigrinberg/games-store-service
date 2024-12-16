package org.acme.model;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Objects;

@MongoEntity(collection = "Games", database = "playground")
public class Game extends PanacheMongoEntityBase {
    @BsonId
    @BsonProperty("id")
    public Integer id;
    public java.lang.String title;
    public java.lang.String shortTitle;
    public GameGenre genre;
    public java.lang.String description;
    public Integer overAgeRestriction;


    public Game(Integer id, java.lang.String title, java.lang.String shortTitle, GameGenre genre, java.lang.String description, Integer overAgeRestriction) {
        this.id = id;
        this.title = title;
        this.shortTitle = shortTitle;
        this.genre = genre;
        this.description = description;
        this.overAgeRestriction = overAgeRestriction;
    }

    public Game() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public java.lang.String getTitle() {
        return title;
    }

    public void setTitle(java.lang.String title) {
        this.title = title;
    }

    public java.lang.String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(java.lang.String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public GameGenre getGenre() {
        return genre;
    }

    public void setGenre(GameGenre genre) {
        this.genre = genre;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public Integer getOverAgeRestriction() {
        return overAgeRestriction;
    }

    public void setOverAgeRestriction(Integer overAgeRestriction) {
        this.overAgeRestriction = overAgeRestriction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Game game = (Game) o;
        return Objects.equals(id, game.id) && Objects.equals(title, game.title) && Objects.equals(shortTitle, game.shortTitle) && genre == game.genre && Objects.equals(description, game.description) && Objects.equals(overAgeRestriction, game.overAgeRestriction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, shortTitle, genre, description, overAgeRestriction);
    }

    @Override
    public java.lang.String toString() {
        return "Game{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", shortTitle='" + shortTitle + '\'' +
                ", genre=" + genre +
                ", description='" + description + '\'' +
                ", overAgeRestriction=" + overAgeRestriction +
                '}';
    }
}
