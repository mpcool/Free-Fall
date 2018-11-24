package com.honeyloaf.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.honeyloaf.framework.AssetHandler;
import com.honeyloaf.framework.AssetLoader;

import java.util.Random;


/**
 * Created by User on 6/19/2017.
 */

public class Rocket {

    public float x, y, width, height, scale;
    public Rectangle boundingRectangle;
    public Texture texture, rocket1, rocket2, rocket3;
    public Animation<Texture> rocketAnimation;
    public Sprite rocket;
    public long startTime, launchTime;
    public Player player;
    public boolean launched;
    private Random rand;

    public Rocket(Player player, AssetLoader assetLoader) {
        rand = new Random();
        scale = AssetHandler.SCREEN_HEIGHT/200;
        this.player = player;
        x = player.x;
        y = 0 - height;
        startTime = TimeUtils.nanoTime();
        boundingRectangle = new Rectangle();
        launched = false;
        texture = assetLoader.manager.get(AssetLoader.ROCKET1, Texture.class);
        rocket = new Sprite(texture);
        rocket.setSize(rocket.getWidth()*scale, rocket.getHeight()*scale);
        width = rocket.getWidth();
        height = rocket.getHeight();
        boundingRectangle.set(x + (width /12), y, width - (width/6), height - (height/8));
        rocket1 = assetLoader.manager.get(AssetLoader.ROCKET1, Texture.class);
        rocket2 = assetLoader.manager.get(AssetLoader.ROCKET2, Texture.class);
        rocket3 = assetLoader.manager.get(AssetLoader.ROCKET3, Texture.class);
        Texture[] rockets = {rocket1, rocket2, rocket3};
        rocketAnimation = new Animation<Texture>(1/15f, rockets);
        rocketAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        launchTime = (rand.nextInt(16) + 15) * 1000000000L;
    }

    public void update() {



        if ((TimeUtils.timeSinceNanos(startTime) <= launchTime - 1500000000L) && !launched) {
            x = player.x - (width/2);
        }
        if (TimeUtils.timeSinceNanos(startTime) >= launchTime){
            launched = true;
            boundingRectangle.setPosition(x + (width/12), y);
            rocket.setPosition(x, y);
            startTime = TimeUtils.nanoTime();
        }
        if(launched) {
            y += AssetHandler.VELOCITY*2.25;
            boundingRectangle.setY(y);
            rocket.setY(y);
            if(y > AssetHandler.SCREEN_HEIGHT) {
                reset();
            }
        }
    }

    public void reset() {
        launched = false;
        y = 0 - height;
        boundingRectangle.setY(y);
        rocket.setY(y);
        startTime = TimeUtils.nanoTime();
        launchTime = (rand.nextInt(16) + 15) * 1000000000L;

    }

}
