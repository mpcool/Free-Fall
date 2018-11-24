package com.honeyloaf.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.honeyloaf.freefall.FreeFallGame;

/**
 * Created by User on 6/12/2017.
 */

public class InputHandler implements InputProcessor {

    public GameWorld world;
    public GameRenderer renderer;
    public FreeFallGame.AdHandler adHandler;
    public Sound buttonSound;
    public long id;


    public InputHandler(GameWorld world, GameRenderer renderer, FreeFallGame.AdHandler adHandler) {
        this.world = world;
        this.renderer = renderer;
        this.adHandler = adHandler;
        buttonSound = AssetLoader.manager.get(AssetLoader.BUTTON_SOUND, Sound.class);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(world.isReady()) {
            world.setRunning();
        }
        if (world.isGameOver()) {
            System.out.println("X " + screenX + "\nY " + (AssetHandler.SCREEN_HEIGHT - screenY));
            if(renderer.playButton.button.getBoundingRectangle().contains(screenX, AssetHandler.SCREEN_HEIGHT - screenY)) {
                id = buttonSound.play();
                world.setReady();
                renderer.fade = 1;
            } else if (renderer.resumeButton.button.getBoundingRectangle().contains(screenX, AssetHandler.SCREEN_HEIGHT - screenY)) {
                id = buttonSound.play();
                adHandler.showVideoAd();
                world.setRunning();
                renderer.fade = 1;
            } else if (renderer.shopButton.button.getBoundingRectangle().contains(screenX, AssetHandler.SCREEN_HEIGHT - screenY)) {
                Gdx.input.setInputProcessor(renderer.shop.stage);
                id = buttonSound.play();
                world.setMenu();
                renderer.fade = 1;
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        //world.player.velocityX = 0;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }


}
