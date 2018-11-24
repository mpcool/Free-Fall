package com.honeyloaf.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.honeyloaf.framework.AssetHandler;
import com.honeyloaf.framework.AssetLoader;

import java.util.Random;

/**
 * Created by User on 6/14/2017.
 */

public class Cloud {
    public Sprite cloud;
    public Texture cloud1, cloud2, cloud3;
    public float x, y;
    public float scale;
    public Random rand;


    public Cloud(AssetLoader assetLoader){
        cloud1 = assetLoader.manager.get(AssetLoader.CLOUD1, Texture.class);
        cloud2 = assetLoader.manager.get(AssetLoader.CLOUD2, Texture.class);
        cloud3 = assetLoader.manager.get(AssetLoader.CLOUD3, Texture.class);
        rand = new Random();
        cloud = new Sprite(cloud1);
        getSprite();
        //cloud.setSize(cloud.getWidth() * AssetHandler.SCALE, cloud.getHeight()*AssetHandler.SCALE);
        x = rand.nextInt((int) (AssetHandler.SCREEN_WIDTH - cloud.getWidth()));
        y = rand.nextInt(AssetHandler.SCREEN_HEIGHT/2);
        cloud.setPosition(x,y);
    }

    public void update() {
        y += AssetHandler.BACKGROUND_VELOCITY;
        cloud.setY(y);
    }

    public void reset(float y) {
        getSprite();
        x = rand.nextInt((int) (AssetHandler.SCREEN_WIDTH - cloud.getWidth()/2));
        this.y = y;
        cloud.setPosition(x,y);
    }

    public boolean isOffScreen() {
        if(y >= AssetHandler.SCREEN_HEIGHT) {
            return true;
        }
        return false;
    }

    public void getSprite() {
        int random = rand.nextInt(3) + 1;
        if(random == 1) {
            cloud.setTexture(cloud1);
        } else if (random == 2) {
            cloud.setTexture(cloud2);
        } else if (random == 3) {
            cloud.setTexture(cloud3);
        }
        cloud.setSize(cloud.getTexture().getWidth() * AssetHandler.SCALE, cloud.getTexture().getHeight()*AssetHandler.SCALE);
    }
}
