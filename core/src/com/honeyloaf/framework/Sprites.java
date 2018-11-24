package com.honeyloaf.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.honeyloaf.objects.Player;

/**
 * Created by michaelross on 7/29/17.
 */

public class Sprites {

    private Array<TextureRegion> frames;

    public Animation<TextureRegion> guyFalling,
            guyPop,
            girlFalling,
            girlPop,
            pineappleRight,
            pineappleLeft;
    public Texture guyFall,
            guyP,
            girlFall,
            girlP,
            pineapple,
            pAppleMove;
    public Sprite sprite;
    private AssetLoader assetLoader;
    private GameWorld world;
    private Player player;
    private float velocityTime, elapsedTime, popTime;


    public Sprites(AssetLoader assetLoader, GameWorld world) {
        this.assetLoader = assetLoader;
        this.world = world;
        this.player = world.player;
        frames = new Array<>();
        instantiateGuy();
        instantiateGirl();
        instantiatePineapple();
        sprite = new Sprite(guyFalling.getKeyFrame(0f));
        sprite.setScale(AssetHandler.SCALE);
        velocityTime = 0f;
        elapsedTime = 0f;
        popTime = 0f;

    }

    public void update(float delta) {

        elapsedTime += delta;
        if(world.isGameOver())
            popTime += delta;
        else
            popTime = 0f;

        if(player.velocityX == 0) {
            velocityTime = 0f;
            pineappleRight.setPlayMode(Animation.PlayMode.REVERSED);
            pineappleLeft.setPlayMode(Animation.PlayMode.REVERSED);
        }
        else
            velocityTime += delta;
        pineappleLeft.setPlayMode(Animation.PlayMode.NORMAL);
        pineappleRight.setPlayMode(Animation.PlayMode.NORMAL);

        sprite.setPosition(world.player.x, world.player.y);

        switch (AssetHandler.getSprite()) {
            case "pineapple":
                if (!world.isGameOver()) {
                    sprite.setRegion(pineappleRight.getKeyFrame(velocityTime));
                }
                break;
            case "guy":
                if(!world.isGameOver())
                    sprite.setRegion(guyFalling.getKeyFrame(elapsedTime));
                else
                    sprite.setRegion(guyPop.getKeyFrame(popTime));
                break;
            case "girl":
                if(!world.isGameOver())
                    sprite.setRegion(girlFalling.getKeyFrame(elapsedTime));
                else
                    sprite.setRegion(girlPop.getKeyFrame(popTime));
                break;

        }

    }

    public void instantiatePineapple() {
        pineapple = assetLoader.manager.get(AssetLoader.PINEAPPLE, Texture.class);

        //Load Textures
        pAppleMove = assetLoader.manager.get(AssetLoader.PINEAPPLE_MOVE, Texture.class);

        //Create Animations
        for(int i = 0; i <= 6; i++) {
            frames.add(new TextureRegion(pAppleMove, i * 17, 0, 17, 30));
        }
        pineappleRight = new Animation<>(1/25f, frames);
        pineappleRight.setPlayMode(Animation.PlayMode.NORMAL);
        frames.clear();
        for(int i = 7; i <= 12; i++) {
            frames.add(new TextureRegion(pAppleMove, i * 17, 0, 17, 30));
        }
        pineappleLeft = new Animation<>(1/25f, frames);
        pineappleLeft.setPlayMode(Animation.PlayMode.NORMAL);
        frames.clear();


    }

    public void instantiateGuy() {

        guyFall = assetLoader.manager.get(AssetLoader.GUY_FALLING, Texture.class);
        guyP = assetLoader.manager.get(AssetLoader.GUY_POPPING, Texture.class);

        //Guy Falling Animation
        for(int i = 0; i < 18; i ++) {
            frames.add(new TextureRegion(guyFall, i * 16, 0, 16, 22));
        }
        guyFalling = new Animation<TextureRegion>(1/18f, frames);
        guyFalling.setPlayMode(Animation.PlayMode.LOOP);
        frames.clear();

        //Guy Popping Animation
        for(int i = 0; i < 20; i ++) {
            frames.add(new TextureRegion(guyP, i * 16, 0, 16, 22));
        }
        guyPop = new Animation<TextureRegion>(1/25f, frames);
        guyPop.setPlayMode(Animation.PlayMode.NORMAL);
        frames.clear();


    }

    public void instantiateGirl() {
        girlFall = assetLoader.manager.get(AssetLoader.GIRL_FALLING, Texture.class);
        girlP = assetLoader.manager.get(AssetLoader.GIRL_POPPING, Texture.class);

        //Guy Falling Animation
        for(int i = 0; i < 18; i ++) {
            frames.add(new TextureRegion(girlFall, i * 16, 0, 16, 22));
        }
        girlFalling = new Animation<TextureRegion>(1/18f, frames);
        girlFalling.setPlayMode(Animation.PlayMode.LOOP);
        frames.clear();

        //Guy Popping Animation
        for(int i = 0; i < 20; i ++) {
            frames.add(new TextureRegion(girlP, i * 16, 0, 16, 22));
        }
        girlPop = new Animation<TextureRegion>(1/25f, frames);
        girlPop.setPlayMode(Animation.PlayMode.NORMAL);
        frames.clear();
    }

}
