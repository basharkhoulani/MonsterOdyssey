package de.uniks.stpmon.team_m.utils;

import de.uniks.stpmon.team_m.dto.*;
import javafx.scene.image.Image;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Singleton
public class TrainerStorage {

    private Region region;
    private String trainerName;
    private int x;
    private int y;
    private int direction;
    private Trainer trainer;
    private String trainerImageUrl;
    private Image trainerSpriteChunk;
    private List<Monster> monsters;
    private List<Item> items;

    @Inject
    public TrainerStorage() {
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public void setTrainerSprite(String url) {
        this.trainerImageUrl = url;
    }

    public void addItem(Item item) {
        for (Item item1 : this.items) {
            if (Objects.equals(item1.type(), item.type())) {
                return;
            }
        }

        this.items.add(item);
    }

    public void updateItem(Item item) {
        this.items.stream().filter(i -> i._id().equals(item._id())).findFirst().ifPresent(i -> this.items.set(this.items.indexOf(i), item));

        this.items = this.items.stream().filter(i -> i.amount() > 0).collect(Collectors.toList());
    }

    public void useItem(int itemType) {
        this.items.stream().filter(i -> i.type() == itemType).findFirst().ifPresent(i -> {
            int amount = i.amount() - 1;
            Item item = new Item(i._id(), i.trainer(), i.type(), amount);
            this.items.set(this.items.indexOf(i), item);
        });
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items.stream().filter(i -> i.amount() > 0).collect(Collectors.toList());
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public String getTrainerName() {
        return this.trainerName;
    }

    public String getTrainerSprite() {
        return this.trainerImageUrl;
    }

    public Image getTrainerSpriteChunk() {
        return trainerSpriteChunk;
    }

    public void setTrainerSpriteChunk(Image trainerSpriteChunk) {
        this.trainerSpriteChunk = trainerSpriteChunk;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public List<Monster> getMonsters() {
        return monsters;
    }

    public void setMonsters(List<Monster> monsters) {
        this.monsters = monsters;
    }

}
