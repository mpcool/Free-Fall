package com.honeyloaf.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.honeyloaf.framework.AssetHandler;
import com.honeyloaf.framework.AssetLoader;

import java.util.Random;

/**
 * Created by michaelross on 6/11/17.
 */

public class Wind {

    public Rectangle boundingRectangle, spriteRectangle;
    public boolean left, angry;
    public Texture wRight, wLeft, wAngryRight, wAngryLeft;
    public Animation<TextureRegion> windRight, windLeft, windAngryRight, windAngryLeft;
    public float x, y, height, width;
    public float runTime;
    public Random rand;

    private Array<TextureRegion> frames;


    public Wind(AssetLoader assetLoader) {
        angry = false;
        frames = new Array<>();
        boundingRectangle = new Rectangle();
        spriteRectangle = new Rectangle();
        rand = new Random();
        //Instantiate Sprites
        wRight = assetLoader.manager.get(AssetLoader.WIND_RIGHT, Texture.class);
        wLeft = assetLoader.manager.get(AssetLoader.WIND_LEFT, Texture.class);
        wAngryRight = assetLoader.manager.get(AssetLoader.WIND_ANGRY_RIGHT, Texture.class);
        wAngryLeft = assetLoader.manager.get(AssetLoader.WIND_ANGRY_LEFT, Texture.class);


        height = 47*AssetHandler.SCALE;
        width = 85*AssetHandler.SCALE;

        TextureRegion[][] left = TextureRegion.split(wLeft, 85, 47);
        int index = 0;
        while (index < 58) {
            for (int i = 0; i < 5; i++) {
                for(int j = 0; j < 12; j++) {
                    frames.add(left[i][j]);
                    index++;
                    if(index >57)
                        break;
                }

            }
        }
        windLeft = new Animation<>(1/29f, frames);
        windLeft.setPlayMode(Animation.PlayMode.LOOP);
        frames.clear();

        TextureRegion[][] right = TextureRegion.split(wRight, 85, 47);
        index = 0;
        while (index < 58) {
            for (int i = 0; i < 5; i++) {
                for(int j = 0; j < 12; j++) {
                    frames.add(right[i][j]);
                    index++;
                    if(index >57)
                        break;
                }

            }
        }
        windRight = new Animation<>(1/29f, frames);
        windRight.setPlayMode(Animation.PlayMode.LOOP);
        frames.clear();


        for(int i = 0; i < 28; i++) {
            frames.add(new TextureRegion(wAngryLeft, i * 51, 0, 51, 50));
        }
        windAngryLeft = new Animation<>(1/20f, frames);
        windAngryLeft.setPlayMode(Animation.PlayMode.LOOP);
        frames.clear();

        for(int i = 0; i < 28; i++) {
            frames.add(new TextureRegion(wAngryRight, i * 51, 0, 51, 50));
        }
        windAngryRight = new Animation<>(1/20f, frames);
        windAngryRight.setPlayMode(Animation.PlayMode.LOOP);
        frames.clear();

        runTime = 0;

    }

    public void draw(SpriteBatch batcher, float delta) {
        runTime += delta;
        batcher.begin();
        if(left) {
            if (!angry) {
                batcher.draw(windLeft.getKeyFrame(runTime), x, y, 85 * AssetHandler.SCALE * .8f, 47 * AssetHandler.SCALE * .8f);
            }
            else {
                batcher.draw(windAngryLeft.getKeyFrame(runTime), x, y, 58 * AssetHandler.SCALE * .8f, 51 * AssetHandler.SCALE * .8f);
            }
        }
        else {
            if(!angry) {
                batcher.draw(windRight.getKeyFrame(runTime), x, y, 85 * AssetHandler.SCALE * .8f, 47 * AssetHandler.SCALE * .8f);
            } else {
                batcher.draw(windAngryRight.getKeyFrame(runTime), x, y, 58 * AssetHandler.SCALE * .8f, 51 * AssetHandler.SCALE * .8f);
            }
        }
        batcher.end();
    }

    public Rectangle getBoundingRectangle() {
        return boundingRectangle;
    }

    public Rectangle getSpriteRectangle() {
        return spriteRectangle;
    }

    public void setWind(int x, int y, boolean left) {
        this.x = x;
        this.y = y;
        this.left = left;
        if(left) {
            boundingRectangle.set(x + (width/4), y, width - (width/4), height * .8f);
            spriteRectangle.set(x, y, 51 * AssetHandler.SCALE * .8f, height * .5f);
        } else {
            boundingRectangle.set(x, y, width - (width/4), height * .8f);
            spriteRectangle.set((x + width) - (51 * AssetHandler.SCALE), y, 51 * AssetHandler.SCALE * .5f, height * .5f);
        }

    }

    public void update() {
        y += AssetHandler.VELOCITY;
        boundingRectangle.setY(y);
        spriteRectangle.setY(y);
    }

    public void reset(float y) {
        this.y = y;
        x = rand.nextInt(AssetHandler.SCREEN_WIDTH - (int) width);
        left = rand.nextBoolean();
        angry = rand.nextInt(10) == 0;
        if(!angry) {
            if (left) {
                boundingRectangle.set(x + (width / 4), y, width - (width / 4), height * .8f);
                spriteRectangle.set(x, y, 51 * AssetHandler.SCALE * .8f, height * .5f);
            } else {
                boundingRectangle.set(x, y, width - (width / 4), height * .8f);
                spriteRectangle.set((x + width) - (51 * AssetHandler.SCALE), y, 51 * AssetHandler.SCALE * .8f, height * .5f);
            }
        } else {
            boundingRectangle.set(x, y, 51 * AssetHandler.SCALE * .8f, (50 * AssetHandler.SCALE) * .6f);
            spriteRectangle.set(x, y, 51 * AssetHandler.SCALE * .8f, 50 * AssetHandler.SCALE * .8f);
        }


    }

    public boolean isOffScreen() {
        return y > AssetHandler.SCREEN_HEIGHT;
    }
}
