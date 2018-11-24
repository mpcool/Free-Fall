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

public class Powerup {

    public Texture powerup_speed, powerup_shield, progressBar, progressBarBackground;
    public Sprite sprite;
    private Random rand;
    public Circle boundingCircle;
    private Player player;

    public long startTime, deployTime, effectStart, effectElapsed;
    public float x, y, radius, fade, scale, barFade, originalVelocity, newVelocity;
    public boolean deployed, collected;
    public int type, width;
    public final int SPEED_BOOST = 0;
    public final int SHIELD = 1;

    public Powerup(Player player, AssetLoader assetLoader) {

        this.player = player;
        rand = new Random();
        startTime = TimeUtils.nanoTime();
        deployTime = (rand.nextInt(7) + 7) * 1000000000L;
        effectElapsed = 10000000000L;
        radius = AssetHandler.SCREEN_WIDTH / 20;


        //Set Powerup Type/Sprite
        type = rand.nextInt(2);
        powerup_speed = assetLoader.manager.get(AssetLoader.POWERUP_SPEED, Texture.class);
        powerup_shield = assetLoader.manager.get(AssetLoader.POWERUP_SHIELD, Texture.class);
        progressBar = AssetLoader.manager.get(AssetLoader.PROGRESS_BAR, Texture.class);
        progressBarBackground = AssetLoader.manager.get(AssetLoader.PROGRESS_BAR_BACKGROUND, Texture.class);
        sprite = new Sprite(powerup_speed);
        width = (int) radius * 2;
        if (type == SPEED_BOOST) {
            sprite.setTexture(powerup_speed);
        } else if (type == SHIELD) {
            sprite.setTexture(powerup_shield);
        }
        sprite.setSize(sprite.getWidth() * AssetHandler.SCALE, sprite.getHeight() * AssetHandler.SCALE);


        x = rand.nextInt(AssetHandler.SCREEN_WIDTH);
        y = -radius * 2;
        boundingCircle = new Circle(x, y, radius);
        originalVelocity = player.baseVelocityX;
        newVelocity = (player.baseVelocityX * 2);

        barFade = 1;


    }

    public void update() {
        if (TimeUtils.timeSinceNanos(startTime) >= deployTime) {
            deployed = true;
            startTime = TimeUtils.nanoTime();
        }
        if (deployed) {
            y += AssetHandler.VELOCITY;
            if (y > AssetHandler.SCREEN_HEIGHT && !collected) {
                reset();
            }
        }
        if (collected) {
            if (TimeUtils.timeSinceNanos(effectStart) < effectElapsed) {
                if (type == SPEED_BOOST) {
                    player.baseVelocityX = newVelocity;
                } else if (type == SHIELD){

                }
            } else {

                player.baseVelocityX = originalVelocity;
                player.shield = false;
                collected = false;
            }
        }
        boundingCircle.setY(y);
        sprite.setPosition(x - radius, y - radius);
    }

    public void draw(SpriteBatch batcher) {
        if (fade == 0) {
            sprite.draw(batcher);
            scale = AssetHandler.SCALE;
        } else {
            scale += (AssetHandler.SCALE * 12 - scale) * .25f;
            fade += (0 - fade) * .1f;
            boundingCircle.setX(AssetHandler.SCREEN_WIDTH * 2);
            batcher.setColor(1, 1, 1, fade);
            batcher.draw(sprite.getTexture(), x - ((sprite.getTexture().getWidth() * scale) / 2),
                    y - ((sprite.getTexture().getHeight() * scale) / 2),
                    sprite.getTexture().getWidth() * scale, sprite.getTexture().getHeight() * scale);
            batcher.setColor(1, 1, 1, 1);
        }

        if (collected) {
            if (TimeUtils.timeSinceNanos(effectStart) < effectElapsed) {
                if (type == SPEED_BOOST || (type == SHIELD && player.shield)) {
                    batcher.draw(sprite.getTexture(), AssetHandler.SCREEN_WIDTH/2 - radius,
                            AssetHandler.SCREEN_HEIGHT * .915f,
                            sprite.getTexture().getWidth() * AssetHandler.SCALE * 1.35f,
                            sprite.getTexture().getHeight() * AssetHandler.SCALE * 1.35f);


                    //barFade += (0 - barFade) * 0.003f;

                    //batcher.setColor(1, 1, 1, barFade);

                    batcher.draw(progressBarBackground,
                            (AssetHandler.SCREEN_WIDTH / 2) - (progressBar.getWidth()/2 * AssetHandler.SCALE),
                            AssetHandler.SCREEN_HEIGHT * .87f,
                            progressBar.getWidth() * AssetHandler.SCALE,
                            progressBar.getHeight() * AssetHandler.SCALE);

                    batcher.draw(progressBar,
                            (AssetHandler.SCREEN_WIDTH / 2) - (progressBar.getWidth()/2 * AssetHandler.SCALE),
                            AssetHandler.SCREEN_HEIGHT * .87f,
                            (int) (progressBar.getWidth() * AssetHandler.SCALE * ((effectElapsed - TimeUtils.timeSinceNanos(effectStart))/(float)effectElapsed)),
                            progressBar.getHeight() * AssetHandler.SCALE,
                            0, 0,
                            (int) (progressBar.getWidth() * ((effectElapsed - TimeUtils.timeSinceNanos(effectStart))/(float)effectElapsed)),
                            progressBar.getHeight(),
                            false, false);

                    //batcher.setColor(1, 1, 1, 1);
                }
            }
        } else {
            barFade = 1;
        }

    }

    public void reset() {
        deployed = false;
        y = -radius * 2;
        x = rand.nextInt(AssetHandler.SCREEN_WIDTH);
        boundingCircle.setX(x);
        startTime = TimeUtils.nanoTime();
        deployTime = (rand.nextInt(7) + 4) * 1000000000L;
        type = rand.nextInt(2);
        if (type == SPEED_BOOST) {
            sprite.setTexture(powerup_speed);
        } else if (type == SHIELD) {
            sprite.setTexture(powerup_shield);
        }
        fade = 0;
    }

    public void collected() {
        //reset();
        boundingCircle.setX(AssetHandler.SCREEN_WIDTH * 2);
        collected = true;
        fade = 1;
        effectStart = TimeUtils.nanoTime();

        if(type == SHIELD) {
            player.shield = true;
        }
    }
}
