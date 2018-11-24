package com.honeyloaf.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.utils.TimeUtils;
import com.honeyloaf.framework.AssetHandler;
import com.honeyloaf.framework.AssetLoader;

import java.util.Random;

/**
 * Created by User on 7/17/2017.
 */

public class Coin {

    private Random rand;
    public Circle boundingCircle;
    private Texture texture;
    public Sprite coin;
    public Player player;


    private int width;
    private long startTime, deployTime;
    public float x, y, radius, scale, fade, playerX, playerY, coinPlayerX, coinPlayerY;
    public boolean deployed;


    public Coin(AssetLoader assetLoader, Player player) {

        this.player = player;

        radius = AssetHandler.SCREEN_WIDTH/27;

        rand = new Random();
        startTime = TimeUtils.nanoTime();
        deployTime = (rand.nextInt(9) + 7) * 1000000000L;

        texture = assetLoader.manager.get(AssetLoader.COIN, Texture.class);
        coin = new Sprite(texture);
        width = (int) radius * 2;
        coin.setSize(coin.getWidth() * AssetHandler.SCALE, coin.getHeight() * AssetHandler.SCALE);


        x = rand.nextInt(AssetHandler.SCREEN_WIDTH);
        y = -radius*2;
        boundingCircle = new Circle(x, y, radius);
    }

    public void update() {
        if (TimeUtils.timeSinceNanos(startTime) >= deployTime){
            deployed = true;
            startTime = TimeUtils.nanoTime();
        }
        if(deployed) {
            y += AssetHandler.VELOCITY;
            if(y > AssetHandler.SCREEN_HEIGHT) {
                reset();
            }
        }
        boundingCircle.setY(y);
        coin.setPosition(x - radius, y - radius);
    }

    public void draw(SpriteBatch batcher) {
        if(fade == 0) {
            coin.draw(batcher);
            scale = AssetHandler.SCALE;
            coinPlayerX = x;
            coinPlayerY = y;
        } else if (player.world.isRunning()){
            scale += (AssetHandler.SCALE*12 - scale) * .25f;
            fade += (0 - fade) * .1f;
            boundingCircle.setX(AssetHandler.SCREEN_WIDTH * 2);
            batcher.setColor(1, 1, 1, fade);
            batcher.draw(coin.getTexture(), x - ((texture.getWidth() * scale) / 2), y - ((texture.getHeight() * scale) / 2), texture.getWidth() * scale, texture.getHeight() * scale);
            batcher.setColor(1, 1, 1, 1);
            coinPlayerX += (playerX - coinPlayerX) * .15f;
            coinPlayerY += (playerY - coinPlayerY) * .15f;
            batcher.draw(coin.getTexture(), coinPlayerX - ((texture.getWidth() * AssetHandler.SCALE) / 2), coinPlayerY - ((texture.getHeight() * AssetHandler.SCALE) / 2), texture.getWidth() * AssetHandler.SCALE, texture.getHeight() * AssetHandler.SCALE);
        }
    }

    public void reset() {
        deployed = false;
        y = -radius*2;
        x = rand.nextInt(AssetHandler.SCREEN_WIDTH);
        boundingCircle.setPosition(x, y);
        startTime = TimeUtils.nanoTime();
        deployTime = (rand.nextInt(6) + 5) * 1000000000L;
        fade = 0;
    }

}
