package com.honeyloaf.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.honeyloaf.framework.AssetHandler;
import com.honeyloaf.framework.AssetLoader;
import com.honeyloaf.framework.GameRenderer;
import com.honeyloaf.framework.GameWorld;
import com.honeyloaf.framework.InputHandler;
import com.honeyloaf.freefall.FreeFallGame;

/**
 * Created by michaelross on 6/11/17.
 */

public class GameScreen implements Screen {

    public GameWorld world;
    public GameRenderer renderer;
    public static InputHandler inputHandler;
    public AssetLoader assetLoader;
    public ScreenViewport viewport;

    public GameScreen(FreeFallGame.AdHandler adHandler, AssetLoader assetLoader) {
        this.assetLoader = assetLoader;
        world = new GameWorld(assetLoader);
        renderer = new GameRenderer(world);
        inputHandler = new InputHandler(world, renderer, adHandler);
        Gdx.input.setInputProcessor(inputHandler);
        viewport = new ScreenViewport(world.cam);
        viewport.apply();
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        if(assetLoader.manager.update()) {
            world.update(delta);
            renderer.render(delta);
        } else {

        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        renderer.shapeRenderer.setProjectionMatrix(world.cam.combined);
        renderer.batcher.setProjectionMatrix(world.cam.combined);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        assetLoader.manager.dispose();
        world.handler.coinSound.dispose();
        world.handler.powerupSound.dispose();
        inputHandler.buttonSound.dispose();
    }
}
