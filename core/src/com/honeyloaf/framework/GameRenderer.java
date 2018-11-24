package com.honeyloaf.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.honeyloaf.screen.AboutScreen;
import com.honeyloaf.screen.SettingsMenu;

/**
 * Created by michaelross on 6/11/17.
 */

public class GameRenderer {

    public GameWorld world;
    public OrthographicCamera cam;
    public Viewport viewport;
    public ShapeRenderer shapeRenderer;
    public BitmapFont font;
    public SpriteBatch batcher, hudBatch;
    public GlyphLayout glyphLayout;
    public String score;
    public float fade, fScale, coinFScale, runTime, deathTime;
    public Button playButton, resumeButton, shopButton;
    public final int PLAY = 0;
    public final int RESUME = 1;
    public final int SHOP = 2;


    public Shop shop;
    public AboutScreen aboutScreen;
    public SettingsMenu settingsMenu;
    public Sprites sprites;


    public GameRenderer(GameWorld world) {
        this.world = world;
        shapeRenderer = new ShapeRenderer();
        cam = world.cam;
        cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);
        //viewport = new ScreenViewport(cam);
        //viewport.apply(true);
        batcher = new SpriteBatch();
        hudBatch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("data/default.fnt"));
        fScale = AssetHandler.SCREEN_HEIGHT / 300;
        coinFScale = fScale * 0.75f;
        font.getData().setScale(fScale, fScale);
        glyphLayout = new GlyphLayout();
        fade = 1;
        playButton = new Button(PLAY, world.assetLoader);
        resumeButton = new Button(RESUME, world.assetLoader);
        shopButton = new Button(SHOP, world.assetLoader);
        shop = new Shop(world, world.assetLoader);
        sprites = new Sprites(world.assetLoader, world);

        runTime = 0;
        deathTime = 0;


        aboutScreen = new AboutScreen(world);
        settingsMenu = new SettingsMenu(world);

    }

    public void render(float delta) {
        Gdx.gl.glClearColor(73 / 255f, 227 / 255f, 218 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        cam.update();
        shapeRenderer.setProjectionMatrix(cam.combined);
        batcher.setProjectionMatrix(cam.combined);
        sprites.update(delta);

        world.handler.drawClouds();
        if (!world.isMenu()) {
            if (world.isReady()) {
                printTitle();
                deathTime = 0;
            }
            if (!world.isReady()) {
                world.handler.drawParticle();
                world.handler.drawWind(delta);
                world.handler.drawBirds(delta);
                world.handler.drawRocket(delta);
                world.handler.drawPowerup();
                world.handler.drawCoin();
                if (world.isGameOver()) {
                    fade += (0f - fade) * .1;
                }
                drawScore(fade);
            }
            drawCircle(delta);
            if (world.isGameOver()) {
                printGameOver();
                drawGameOverButtons();
            }
        } else if (world.isMenu()) {
            drawShop(delta);
        } else if (world.isSettings()) {
            drawSettings(delta);
        } else if (world.isAboutScreen()) {
            drawAbout(delta);
        }


        if (world.handler.coin.fade > .7) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(1, 191 / 255f, 54 / 255f, 1);
            shapeRenderer.rect(0, 0, AssetHandler.SCREEN_WIDTH, AssetHandler.SCREEN_HEIGHT);
            shapeRenderer.end();
        }
        if (world.handler.powerup.fade > .7) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            if (world.handler.powerup.type == world.handler.powerup.SPEED_BOOST) {
                shapeRenderer.setColor(56 / 255f, 152 / 255f, 1, 1);
            } else {
                shapeRenderer.setColor(131 / 255f, 224 / 255f, 76 / 255f, 1);
            }
            shapeRenderer.rect(0, 0, AssetHandler.SCREEN_WIDTH, AssetHandler.SCREEN_HEIGHT);
            shapeRenderer.end();
        }


    }

    public void drawShop(float delta) {
        shop.render(delta);
    }

    public void drawSettings(float delta) {
        settingsMenu.render(delta);
    }

    public void drawAbout(float delta) {
        aboutScreen.render(delta);
    }

    public void drawGameOverButtons() {
        batcher.begin();
        playButton.button.draw(batcher);
        resumeButton.button.draw(batcher);
        shopButton.button.draw(batcher);
        batcher.end();
    }

    public void printGameOver() {
        String title = "Game Over";
        String score = "Score: " + (int) world.score;
        String highScore = "Best: " + AssetHandler.prefs.getInteger("highScore");
        glyphLayout.setText(font, title);
        batcher.begin();
        font.setColor(Color.BLACK);
        font.draw(batcher, title, AssetHandler.SCREEN_WIDTH / 2 - (glyphLayout.width / 2) + 6, AssetHandler.SCREEN_HEIGHT - (AssetHandler.SCREEN_HEIGHT / 10) - 6);
        font.setColor(Color.WHITE);
        font.draw(batcher, title, AssetHandler.SCREEN_WIDTH / 2 - (glyphLayout.width / 2), AssetHandler.SCREEN_HEIGHT - (AssetHandler.SCREEN_HEIGHT / 10));

        glyphLayout.setText(font, score);
        font.setColor(Color.BLACK);
        font.draw(batcher, score, AssetHandler.SCREEN_WIDTH / 2 - (glyphLayout.width / 2) + 6, AssetHandler.SCREEN_HEIGHT - (AssetHandler.SCREEN_HEIGHT / 10) * 3 - 6);
        font.setColor(Color.WHITE);
        font.draw(batcher, score, AssetHandler.SCREEN_WIDTH / 2 - (glyphLayout.width / 2), AssetHandler.SCREEN_HEIGHT - (AssetHandler.SCREEN_HEIGHT / 10) * 3);

        glyphLayout.setText(font, highScore);
        font.setColor(Color.BLACK);
        font.draw(batcher, highScore, AssetHandler.SCREEN_WIDTH / 2 - (glyphLayout.width / 2) + 6, AssetHandler.SCREEN_HEIGHT - (AssetHandler.SCREEN_HEIGHT / 10) * 4 - 6);
        font.setColor(Color.WHITE);
        font.draw(batcher, highScore, AssetHandler.SCREEN_WIDTH / 2 - (glyphLayout.width / 2), AssetHandler.SCREEN_HEIGHT - (AssetHandler.SCREEN_HEIGHT / 10) * 4);

        batcher.end();
    }

    public void printTitle() {
        String title = "Free Fall";
        glyphLayout.setText(font, title);
        batcher.begin();
        font.getData().setScale(fScale, fScale);
        font.setColor(Color.BLACK);
        font.draw(batcher, title, AssetHandler.SCREEN_WIDTH / 2 - (glyphLayout.width / 2) + 6, AssetHandler.SCREEN_HEIGHT - (AssetHandler.SCREEN_HEIGHT / 20) - 6);
        font.setColor(Color.WHITE);
        font.draw(batcher, title, AssetHandler.SCREEN_WIDTH / 2 - (glyphLayout.width / 2), AssetHandler.SCREEN_HEIGHT - (AssetHandler.SCREEN_HEIGHT / 20));

        batcher.end();
    }

    public void drawScore(float fade) {
        float coinX, coinY, scoreX, scoreY, height;
        score = "" + (int) world.score;
        String coins = "" + AssetHandler.getCoins();

        font.getData().setScale(fScale, fScale);
        glyphLayout.setText(font, score);
        scoreX = 20;
        scoreY = AssetHandler.SCREEN_HEIGHT - glyphLayout.height / 2;


        font.getData().setScale(coinFScale, coinFScale);
        glyphLayout.setText(font, coins);
        coinX = AssetHandler.SCREEN_WIDTH - 20 - glyphLayout.width;
        coinY = AssetHandler.SCREEN_HEIGHT - glyphLayout.height / 2;
        height = glyphLayout.height;


        hudBatch.begin();

        //Black Score Shadow
        font.setColor(0, 0, 0, fade);
        font.getData().setScale(fScale, fScale);
        font.draw(hudBatch, score, scoreX + 6, scoreY);

        //Black Coins Shadow
        font.getData().setScale(coinFScale, coinFScale);
        font.draw(hudBatch, coins, coinX + 6, coinY);

        //White Score
        font.setColor(1, 1, 1, fade);
        font.getData().setScale(fScale, fScale);
        font.draw(hudBatch, score, scoreX, scoreY);

        //Yellow Coins
        font.setColor(255 / 255f, 255 / 255f, 70 / 255f, fade);
        font.getData().setScale(coinFScale, coinFScale);
        font.draw(hudBatch, coins, coinX, coinY);

        if (world.isRunning()) {
            hudBatch.draw(world.handler.coin.coin, coinX - (world.handler.coin.coin.getWidth() + 6), coinY - world.handler.coin.coin.getHeight(), height, height);
        }

        hudBatch.end();

    }

    public void printHighScore() {
        String highscore = "Score: " + world.score + "\nGame Over";
        batcher.begin();
        font.draw(batcher, highscore, world.player.x, cam.position.y / 2 + (AssetHandler.SCREEN_HEIGHT / 2));
        batcher.end();
    }


    public void drawCircle(float delta) {

        float shieldFade = 1;
        runTime += delta;

        if (world.player.shield) {
            if (TimeUtils.timeSinceNanos(world.handler.powerup.effectStart) < world.handler.powerup.effectElapsed - 3000000000L) {
                if (world.score % 2 == 0) {
                    fade = 0;
                } else {
                    fade = 1;
                }
            }
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(131 / 255f, 224 / 255f, 76 / 255f, shieldFade);
            shapeRenderer.circle(world.player.boundingCircle.x, world.player.boundingCircle.y, world.player.boundingCircle.radius);
            shapeRenderer.end();
        }


        batcher.begin();
        sprites.sprite.draw(batcher);
        batcher.end();

    }

}
