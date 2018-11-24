package com.honeyloaf.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by User on 6/21/2017.
 */

public class Button {

    public Texture texture;
    public Sprite button;
    public float x, y;
    public final int PLAY_BUTTON = 0;
    public final int RESUME_BUTTON = 1;
    public final int SHOP_BUTTON = 2;
    public final int SCALE = AssetHandler.SCREEN_WIDTH/80;

    public Button(int type, AssetLoader assetLoader) {
        if(type == PLAY_BUTTON) {
            texture = assetLoader.manager.get(AssetLoader.PLAY_BUTTON, Texture.class);
        } else if (type == RESUME_BUTTON) {
            texture = assetLoader.manager.get(AssetLoader.RESUME_BUTTON, Texture.class);
        } else if (type == SHOP_BUTTON) {
            texture = assetLoader.manager.get(AssetLoader.SHOP_BUTTON, Texture.class);
        }
        //Instantiate Button with correct texture based on Type, and set size according to screen size
        button = new Sprite(texture);
        button.setSize(button.getWidth()*SCALE, button.getHeight()*SCALE);


        //Set position based on type of button
        if(type == PLAY_BUTTON) {
            x = AssetHandler.SCREEN_WIDTH/12;
            y = AssetHandler.SCREEN_HEIGHT/4;
        } else if (type == RESUME_BUTTON) {
            x = (AssetHandler.SCREEN_WIDTH/12) + (AssetHandler.SCREEN_WIDTH/2);
            y = AssetHandler.SCREEN_HEIGHT/4;
        } else if (type == SHOP_BUTTON) {
            x = AssetHandler.SCREEN_WIDTH/2 - (button.getWidth()/2);
            y = AssetHandler.SCREEN_HEIGHT / 16;
        }

        button.setPosition(x,y);
    }
}
