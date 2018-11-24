package com.honeyloaf.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Created by michaelross on 6/11/17.
 */

public class AssetHandler {

    public static final int SCREEN_WIDTH = Gdx.graphics.getWidth();
    public static final int SCREEN_HEIGHT = Gdx.graphics.getHeight();
    public static final int VELOCITY = SCREEN_HEIGHT/85;
    public static final int WIND_STRENGTH = SCREEN_WIDTH/90;
    public static final int BACKGROUND_VELOCITY = SCREEN_HEIGHT/800;
    public static final int SCALE = SCREEN_HEIGHT/200;

    public static Preferences prefs = Gdx.app.getPreferences("FreeFall");



    public static void setHighScore(int val) {
        prefs.putInteger("highScore", val);
    }

    public static int getHighScore() {
        return prefs.getInteger("highScore");
    }

    public static void setCoins(int val) {
        prefs.putInteger("coins", val);
    }

    public static int getCoins() {
        return prefs.getInteger("coins");
    }

    public static void setSprite(String sprite) {
        prefs.putString("sprite", sprite);
    }

    public static String getSprite() {
        return prefs.getString("sprite");
    }

    public static void setOwned(String sprite, boolean owned) {
        prefs.putBoolean(sprite, owned);
    }

    public static boolean getOwned(String sprite) {
        return prefs.getBoolean(sprite);
    }
}
