package com.honeyloaf.framework;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.honeyloaf.objects.Player;

/**
 * Created by michaelross on 6/11/17.
 */

public class GameWorld {

    public AssetLoader assetLoader;
    public Player player;
    public ObjectHandler handler;
    public GameState currentState;
    public OrthographicCamera cam;
    public float score;
    public final float SCORESCALE = AssetHandler.SCREEN_HEIGHT/800;

    public GameWorld(AssetLoader assetLoader) {
        this.assetLoader = assetLoader;
        initialize();
        AssetHandler.setSprite("guy");
    }

    public enum GameState {READY, RUNNING, MENU, GAMEOVER, ABOUT_SCREEN, SETTINGS_MENU}

    public void update(float delta) {
        assetLoader.manager.update();
        player.update();
        handler.update(delta);
        if(isRunning()) {
            score += (2) * delta;
        }
        if(isGameOver()) {
            if (score > AssetHandler.getHighScore()) {
                AssetHandler.setHighScore((int) score);

            }
            AssetHandler.prefs.flush();
        }
    }

    public boolean isReady() {
        return currentState == GameState.READY;
    }

    public boolean isRunning(){
        return currentState == GameState.RUNNING;
    }

    public boolean isMenu() {
        return currentState == GameState.MENU;
    }

    public boolean isGameOver() {
        return currentState == GameState.GAMEOVER;
    }

    public boolean isAboutScreen() {
        return currentState == GameState.ABOUT_SCREEN;
    }

    public boolean isSettings() {
        return currentState == GameState.SETTINGS_MENU;
    }

    public void setRunning() {
        currentState = GameState.RUNNING;
        //Reset Powerups, Rockets, and Drones
        handler.rocket.reset();
        handler.powerup.reset();
        handler.coin.reset();
        handler.setBirds();

    }

    public void setMenu() {
        currentState = GameState.MENU;
    }

    public void setReady() {
        currentState = GameState.READY;
        //Reset Score and Wind
        player.reset();
        handler.setRectangles();
        score = 0;
    }

    public void setGameOver() {
        currentState = GameState.GAMEOVER;
        handler.powerup.collected = false;
        cam.position.set(AssetHandler.SCREEN_WIDTH / 2, AssetHandler.SCREEN_HEIGHT / 2, 0);
        handler.shake = false;
        handler.shakeIntensity = AssetHandler.SCREEN_WIDTH / 20;
        handler.totalShake = 10;
    }

    public void initialize() {
        //Sets the view to
        cam = new OrthographicCamera(AssetHandler.SCREEN_WIDTH, AssetHandler.SCREEN_HEIGHT);
        player = new Player(this);
        handler = new ObjectHandler(this, cam);
        currentState = GameState.READY;
        if (!AssetHandler.prefs.contains("highScore")) {
            AssetHandler.prefs.putInteger("highScore", 0);
        }
        if (!AssetHandler.prefs.contains("coins")) {
            AssetHandler.prefs.putInteger("coins", 0);
        }
        if(!AssetHandler.prefs.contains("sprite")) {
            AssetHandler.setSprite("guy");
        }
        if(!AssetHandler.prefs.contains("sprite")) {
            AssetHandler.prefs.putString("sprite", "guy");
        }
        AssetHandler.prefs.flush();
        AssetHandler.setSprite("guy");
        score = 0;

    }

}
