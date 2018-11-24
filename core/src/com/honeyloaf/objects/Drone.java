package com.honeyloaf.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.honeyloaf.framework.AssetHandler;
import com.honeyloaf.framework.AssetLoader;

import java.util.Random;

/**
 * Created by User on 6/13/2017.
 */

public class Drone {

    public int width, height;
    public float x, y, runTime;
    public Rectangle boundingRectangle;
    public Random rand;
    public Texture texture;
    public Animation<TextureRegion> drone;
    public boolean left;

    private Array<TextureRegion> frames;

    public Drone(AssetLoader assetLoader) {
        frames = new Array<>();
        width = 26 * AssetHandler.SCALE;
        height = 17 * AssetHandler.SCALE;
        rand = new Random();
        x = rand.nextInt(AssetHandler.SCREEN_WIDTH - width);
        y = 0;
        runTime = 0;
        boundingRectangle = new Rectangle(x, y, width, height * .8f);
        left = rand.nextBoolean();
        texture = assetLoader.manager.get(AssetLoader.DRONE, Texture.class);
        for(int i = 0; i < 36; i++) {
            frames.add(new TextureRegion(texture, i * 26, 0, 26, 17));
        }
        drone = new Animation<>(1/30f, frames);
        drone.setPlayMode(Animation.PlayMode.LOOP);
        //float scale = width/drone.getWidth();
        //drone.setSize(drone.getWidth() * AssetHandler.SCALE, drone.getHeight() * AssetHandler.SCALE );
    }

    public void draw(SpriteBatch batcher, float delta) {
        runTime += delta;
        batcher.begin();
        batcher.draw(drone.getKeyFrame(runTime), x, y, 26 * AssetHandler.SCALE, 17 * AssetHandler.SCALE);
        batcher.end();
    }

    public void update() {
        boundingRectangle.setPosition(x, y);
        //drone.setPosition(x, y);
        y += AssetHandler.VELOCITY;
        if (isOnScreen()) {
            if (left) {
                x += AssetHandler.VELOCITY / 4;
            } else {
                x -= AssetHandler.VELOCITY / 4;
            }
        }
        //drone.setPosition(x, y);
    }

    public void reset(float y) {
        this.y = y;
        x = rand.nextInt((AssetHandler.SCREEN_WIDTH*2) - width) - AssetHandler.SCREEN_WIDTH/2;
        left = x + (width / 2) <= AssetHandler.SCREEN_WIDTH/2;
    }

    public boolean isOffScreen() {
        return y > AssetHandler.SCREEN_HEIGHT;
    }

    public boolean isOnScreen() {
        return y > 0 && !isOffScreen();
    }

}
