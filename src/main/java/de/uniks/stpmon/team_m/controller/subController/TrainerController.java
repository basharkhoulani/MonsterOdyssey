package de.uniks.stpmon.team_m.controller.subController;

import de.uniks.stpmon.team_m.controller.Controller;
import de.uniks.stpmon.team_m.dto.Trainer;
import de.uniks.stpmon.team_m.service.PresetsService;
import de.uniks.stpmon.team_m.utils.SpriteAnimation;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import javax.inject.Inject;

public class TrainerController extends Controller {
    private static final int DELAY = 100;
    private static final int DELAY_LONG = 500;

    private SpriteAnimation spriteAnimation;
    private final GraphicsContext graphicsContext;
    private Trainer trainer;
    private Image trainerChunk;

    @Inject
    PresetsService presetsService;
    private Boolean isWalking = false;


    @Override
    public void init() {
        super.init();
        int duration;
        if (isWalking) {
            duration = DELAY;
        } else {
            duration = DELAY_LONG;
        }
        spriteAnimation = new SpriteAnimation(trainerChunk, trainer, duration, graphicsContext);
        spriteAnimation.stay(trainer.direction());
    }

    public void startAnimations() {
        spriteAnimation.start();
    }

    @Override
    public Parent render() {
        return null;
    }

    public TrainerController(Trainer trainer, Image trainerChunk, GraphicsContext graphicsContext) {
        this.trainerChunk = trainerChunk;
        this.trainer = trainer;
        this.graphicsContext = graphicsContext;
    }


}
