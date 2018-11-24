package com.honeyloaf.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.utils.TimeUtils;
import com.honeyloaf.framework.AssetHandler;
import com.honeyloaf.framework.GameWorld;


/**
 * Created by michaelross on 6/11/17.
 */

public class Player {



    public float x, y, width, baseVelocityX, velocityX;
    public long paralyzedStart, paralyzedElapsed;
    public Circle boundingCircle;
    public int circleX, circleY, circleRad;
    public GameWorld world;
    public boolean shield, paralyzed, left;

    public Player(GameWorld world) {
        this.world = world;
        circleRad = AssetHandler.SCALE * 8;
        circleX = (AssetHandler.SCREEN_WIDTH/2);
        circleY = (AssetHandler.SCREEN_HEIGHT/4) * 3;
        boundingCircle = new Circle(circleX, circleY, circleRad);
        baseVelocityX = AssetHandler.SCREEN_WIDTH / 50;

        x = circleX;
        y = circleY;
        width = circleRad * 2;

        shield = false;
        paralyzed = false;

        paralyzedStart = 0;
        paralyzedElapsed = 1000000000L;
        left = false;
    }

    public void update() {

        handleTouch();
        circleX = (int) x;
        circleY = (int) y;
        boundingCircle.setX(circleX);

        if(TimeUtils.timeSinceNanos(paralyzedStart) > paralyzedElapsed) {
            paralyzed = false;
        }
    }

    public void handleTouch() {
        if (world.isRunning()) {
            if (Gdx.input.isTouched(0)) {
                if (!paralyzed) {
                    if ((Gdx.input.getX() < AssetHandler.SCREEN_WIDTH / 2)) {
                        if ((int) x - baseVelocityX - circleRad >= 0.0) {
                            velocityX += (baseVelocityX - velocityX) * .05f;
                            if(velocityX > 3) {
                                velocityX += (baseVelocityX - velocityX) * .25f;
                            }
                            x -= velocityX;
                        }
                        left = true;
                    } else {
                        if (x + circleRad + baseVelocityX <= AssetHandler.SCREEN_WIDTH) {
                            velocityX += (baseVelocityX - velocityX) * .05f;
                            if(velocityX > 3) {
                                velocityX += (baseVelocityX - velocityX) * .25f;
                            }
                            x += velocityX;
                        }
                        left = false;
                    }
                }
            } else {
                velocityX = 0;
            }
        }
    }

    public void reset() {
        circleX = (AssetHandler.SCREEN_WIDTH/2);
        circleY = (AssetHandler.SCREEN_HEIGHT/4) * 3;
        boundingCircle = new Circle(circleX, circleY, circleRad);
        baseVelocityX = AssetHandler.SCREEN_WIDTH / 50;

        x = circleX;
        y = circleY;
    }

}
