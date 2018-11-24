package com.honeyloaf.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.honeyloaf.framework.AssetHandler;
import com.honeyloaf.framework.AssetLoader;
import com.honeyloaf.freefall.FreeFallGame;

/**
 * Created by michaelross on 8/26/17.
 */

public class SplashScreen implements Screen {

    private FreeFallGame game;
    private SpriteBatch batch;
    private Texture splashTexture;
    private BitmapFont font;
    private GlyphLayout layout;
    float alpha = 0;

    public SplashScreen(FreeFallGame game) {
        this.game = game;
        batch = new SpriteBatch();
        splashTexture = AssetLoader.manager.get(AssetLoader.COIN, Texture.class);
        font = new BitmapFont(Gdx.files.internal("data/default.fnt"));
        font.getData().setScale(AssetHandler.SCREEN_WIDTH/220);
        layout = new GlyphLayout();
    }

    @Override
    public void show() {
        AssetLoader.load();
        AssetLoader.loadSplashScreen();

    }

    @Override
    public void render(float delta) {
        String title = "Honey Loaf Games";
        layout.setText(font, title);

        alpha += (1 - alpha) * 0.05f;


        Gdx.gl.glClearColor(73 / 255f, 227 / 255f, 218 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.setColor(1, 1, 1, alpha);
        batch.draw(splashTexture, AssetHandler.SCREEN_WIDTH/2 - (splashTexture.getWidth() * AssetHandler.SCALE),
                AssetHandler.SCREEN_HEIGHT / 2,
                splashTexture.getWidth() * AssetHandler.SCALE * 2,
                splashTexture.getHeight() * AssetHandler.SCALE * 2);


        font.setColor(0, 0, 0, alpha);
        font.draw(batch, title,
                (AssetHandler.SCREEN_WIDTH / 2) - (layout.width/2) + 8,
                AssetHandler.SCREEN_HEIGHT * .8f - 8);

        font.setColor(1, 1, 1, alpha);
        font.draw(batch, title,
                (AssetHandler.SCREEN_WIDTH / 2) - (layout.width/2),
                AssetHandler.SCREEN_HEIGHT * .8f);

        batch.end();



        if(AssetLoader.manager.update()) {
            game.gameScreen = new GameScreen(game.adHandler, game.assetLoader);
            game.setScreen(game.gameScreen);
        }
    }

    @Override
    public void resize(int width, int height) {

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

    }
}
