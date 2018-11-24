package com.honeyloaf.framework;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by michaelross on 8/18/17.
 */

public class AssetLoader {

    public static final String
            PLAY_BUTTON = "data/Play_Button.png",
            RESUME_BUTTON = "data/Resume_Button.png",
            SHOP_BUTTON = "data/Shop_Button.png",
            SKIN = "data/uiskin.json",
            FONT = "data/default.fnt",
            GUY_FALLING = "data/Default_Falling.png",
            GUY_POPPING = "data/Guy_Pop.png",
            GIRL_FALLING = "data/Girl_Falling.png",
            GIRL_POPPING = "data/Girl_Pop.png",
            CLOUD1 = "data/Cloud_1.png",
            CLOUD2 = "data/Cloud_2.png",
            CLOUD3 = "data/Cloud_3.png",
            COIN = "data/Coin.png",
            DRONE = "data/Drone-sheet.png",
            POWERUP_SPEED = "data/Speed_Powerup.png",
            POWERUP_SHIELD = "data/Shield_Powerup.png",
            ROCKET1 = "data/Rocket1.png",
            ROCKET2 = "data/Rocket2.png",
            ROCKET3 = "data/Rocket3.png",
            WIND_LEFT = "data/Wind_Left-sheet.png",
            WIND_RIGHT = "data/Wind_Right-sheet.png",
            WIND_ANGRY_LEFT = "data/Wind_Angry_Left-sheet.png",
            WIND_ANGRY_RIGHT = "data/Wind_Angry_Right-sheet.png",
            BUTTON_SOUND = "data/click.wav",
            COIN_SOUND = "data/coin.wav",
            POWERUP_SOUND = "data/powerup.wav",
            PROGRESS_BAR = "data/progress-bar.png",
            PROGRESS_BAR_BACKGROUND = "data/progress-bar-background.png",
            PINEAPPLE = "data/Pineapple.png",
            PINEAPPLE_MOVE = "data/Pineapple-Move-Sheet.png";
    public static AssetManager manager;

    public AssetLoader() {
        manager = new AssetManager();
    }

    public static void load() {
        manager.load(PLAY_BUTTON, Texture.class);
        manager.load(RESUME_BUTTON, Texture.class);
        manager.load(SHOP_BUTTON, Texture.class);
        manager.load(SKIN, Skin.class);
        manager.load(FONT, BitmapFont.class);
        manager.load(GUY_FALLING, Texture.class);
        manager.load(GUY_POPPING, Texture.class);
        manager.load(GIRL_FALLING, Texture.class);
        manager.load(GIRL_POPPING, Texture.class);
        manager.load(CLOUD1, Texture.class);
        manager.load(CLOUD2, Texture.class);
        manager.load(CLOUD3, Texture.class);
        manager.load(DRONE, Texture.class);
        manager.load(POWERUP_SPEED, Texture.class);
        manager.load(POWERUP_SHIELD, Texture.class);
        manager.load(ROCKET1, Texture.class);
        manager.load(ROCKET2, Texture.class);
        manager.load(ROCKET3, Texture.class);
        manager.load(WIND_LEFT, Texture.class);
        manager.load(WIND_RIGHT, Texture.class);
        manager.load(WIND_ANGRY_LEFT, Texture.class);
        manager.load(WIND_ANGRY_RIGHT, Texture.class);
        manager.load(BUTTON_SOUND, Sound.class);
        manager.load(COIN_SOUND, Sound.class);
        manager.load(POWERUP_SOUND, Sound.class);
        manager.load(PROGRESS_BAR, Texture.class);
        manager.load(PROGRESS_BAR_BACKGROUND, Texture.class);
        manager.load(PINEAPPLE, Texture.class);
        manager.load(PINEAPPLE_MOVE, Texture.class);
    }

    public static void loadSplashScreen() {
        manager.load(COIN, Texture.class);
    }

}
