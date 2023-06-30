package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.dto.Trainer;
import de.uniks.stpmon.team_m.service.PresetsService;
import de.uniks.stpmon.team_m.utils.SpriteAnimation;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import javax.inject.Inject;

import static de.uniks.stpmon.team_m.Constants.TILE_SIZE;

public class TrainerController extends Controller {
    private static final int DELAY = 100;
    private static final int DELAY_LONG = 500;

    private SpriteAnimation spriteAnimation;
    private final GraphicsContext graphicsContext;
    private Trainer trainer;
    private Image trainerChunk;
    private int oldTrainerX;
    private int oldTrainerY;
    private int trainerX;
    private int trainerY;
    private int trainerDirection;
    private int trainerTargetX;
    private int trainerTargetY;

    @Inject
    PresetsService presetsService;
    private Boolean isWalking = false;

    public int getTrainerX() {
        return trainerX;
    }

    public int getTrainerY() {
        return trainerY;
    }

    @Override
    public void init() {
        super.init();
        int duration;
        if (isWalking) {
            duration = DELAY;
        } else {
            duration = DELAY_LONG;
        }
        spriteAnimation = new SpriteAnimation(this, trainerChunk, trainer, duration, graphicsContext);
        spriteAnimation.stay(trainerDirection);
    }

    public void startAnimations() {
        spriteAnimation.start();
    }

    @Override
    public Parent render() {
        return null;
    }

    /** Note: oldTrainerX, oldTrainerY, targetTrainerX and targetTrainerY are given in tiles, not in pixels!
     *  BUT trainerTargetX and trainerTargetY are given in pixels!
     */

    public TrainerController(Trainer trainer, Image trainerChunk, GraphicsContext graphicsContext) {
        this.trainerChunk = trainerChunk;
        this.trainer = trainer;
        this.graphicsContext = graphicsContext;
        this.oldTrainerX = trainerTargetX = trainer.x();
        this.oldTrainerY = trainerTargetY = trainer.y();
        this.trainerX = trainer.x() * TILE_SIZE;
        this.trainerY = trainer.y() * TILE_SIZE;
        this.trainerDirection = trainer.direction();
    }

    public SpriteAnimation getSpriteAnimation() {
        return spriteAnimation;
    }

    public void setTrainerTargetPosition(int x, int y) {
        trainerTargetX = x;
        trainerTargetY = y;
    }

    public void walk() {
        if (trainerX != trainerTargetX * TILE_SIZE || trainerY != trainerTargetY * TILE_SIZE) {
            isWalking = true;
            if (trainerX < trainerTargetX * TILE_SIZE) {
                trainerX++;
                trainerDirection = 0;
            } else if (trainerX > trainerTargetX * TILE_SIZE) {
                trainerX--;
                trainerDirection = 2;
            } else if (trainerY < trainerTargetY * TILE_SIZE) {
                trainerY++;
                trainerDirection = 3;
            } else if (trainerY > trainerTargetY * TILE_SIZE) {
                trainerY--;
                trainerDirection = 1;
            }
            spriteAnimation.walk(trainerDirection);
        }
        else {
            isWalking = false;
            spriteAnimation.stay(trainerDirection);
        }
    }

    public void setTrainerDirection(int direction) {
        trainerDirection = direction;
    }

    public int getTrainerDirection() {
        return trainerDirection;
    }
}
