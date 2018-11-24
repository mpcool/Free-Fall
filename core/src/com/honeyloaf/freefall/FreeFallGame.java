package com.honeyloaf.freefall;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.honeyloaf.framework.AssetLoader;
import com.honeyloaf.screen.GameScreen;
import com.honeyloaf.screen.SplashScreen;


public class FreeFallGame extends Game {

    public AdHandler adHandler;
    public GameScreen gameScreen;
    public AssetLoader assetLoader;


	@Override
	public void create () {
        assetLoader = new AssetLoader();
        assetLoader.loadSplashScreen();
        assetLoader.manager.finishLoading();
        Texture.setAssetManager(assetLoader.manager);
        setScreen(new SplashScreen(this));

	}

    public interface AdHandler {
        void showVideoAd();
        boolean getAndroid();
    }

    public void setAdHandler(AdHandler adHandler) {
        this.adHandler = adHandler;
    }

    public void getVideoReward() {
        gameScreen.world.setRunning();
    }

}
