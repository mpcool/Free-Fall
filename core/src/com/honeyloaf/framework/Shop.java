package com.honeyloaf.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.honeyloaf.screen.GameScreen;

/**
 * Created by michaelross on 7/27/17.
 */

public class Shop {

    public Stage stage;
    private Table table;
    private Skin skin;
    private ScrollPane scrollPane;


    private com.badlogic.gdx.scenes.scene2d.ui.Button back, guy, girl;
    private Texture backT, guyT, guySE, girlT, girlSE;
    private Sprite backS, guyS, girlS;
    private SpriteDrawable backSD, guySD, girlSD;


    private int girlCoins;


    private GameWorld world;
    private float coinFScale;
    private BitmapFont font;
    private GlyphLayout glyphLayout;


    public Shop(GameWorld gameWorld, AssetLoader assetLoader) {
        world = gameWorld;
        stage = new Stage();
        skin = assetLoader.manager.get(AssetLoader.SKIN, Skin.class);
        coinFScale = (AssetHandler.SCREEN_HEIGHT/600) * 0.75f;
        font = assetLoader.manager.get(AssetLoader.FONT, BitmapFont.class);
        glyphLayout = new GlyphLayout();

        initButtons();
        initListeners();


        table = new Table(skin);
        table.setHeight(AssetHandler.SCREEN_HEIGHT);
        table.setWidth(AssetHandler.SCREEN_WIDTH);
        table.row();
        table.add(guy).width(AssetHandler.SCREEN_WIDTH/3).height(AssetHandler.SCREEN_WIDTH/3).pad(AssetHandler.SCREEN_WIDTH/12);
        table.add(girl).width(AssetHandler.SCREEN_WIDTH/3).height(AssetHandler.SCREEN_WIDTH/3).pad(AssetHandler.SCREEN_WIDTH/12);
        table.setFillParent(true);
        table.setBounds(0, 0, AssetHandler.SCREEN_WIDTH, AssetHandler.SCREEN_HEIGHT);



        scrollPane = new ScrollPane(table);
        scrollPane.setBounds(0, 0, AssetHandler.SCREEN_WIDTH, AssetHandler.SCREEN_HEIGHT);
        back.setBounds(AssetHandler.SCREEN_HEIGHT/30, (AssetHandler.SCREEN_HEIGHT/10)*9, AssetHandler.SCREEN_HEIGHT/8, (AssetHandler.SCREEN_HEIGHT/8)/(100/53f));
        stage.addActor(scrollPane);
        stage.addActor(back);


        setCoins();

    }

    public void render(float delta) {

        setSelected();


        String coins = "" + AssetHandler.getCoins();


        Gdx.gl.glClearColor(73/255f,227/255f,218/255f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        stage.act(delta);
        stage.draw();
        font.setColor(0, 0, 0, 1);
        font.getData().setScale(coinFScale,coinFScale);
        glyphLayout.setText(font, coins);



        world.handler.batcher.begin();
        font.draw(world.handler.batcher, coins, (AssetHandler.SCREEN_WIDTH - 20 - glyphLayout.width) + 6, AssetHandler.SCREEN_HEIGHT - (AssetHandler.SCREEN_HEIGHT/160) - 16);
        font.setColor(255/255f, 255/255f, 70/255f, 1);
        font.draw(world.handler.batcher, coins, AssetHandler.SCREEN_WIDTH - 20 - glyphLayout.width, AssetHandler.SCREEN_HEIGHT - (AssetHandler.SCREEN_HEIGHT/160) - 10);
        world.handler.batcher.draw(world.handler.coin.coin, AssetHandler.SCREEN_WIDTH - 30 - glyphLayout.width - (world.handler.coin.coin.getWidth()), AssetHandler.SCREEN_HEIGHT - (AssetHandler.SCREEN_HEIGHT / 160) - 16 - (int) (glyphLayout.height), world.handler.coin.coin.getWidth(), world.handler.coin.coin.getWidth());
        world.handler.batcher.end();
    }

    public void setSelected() {
        switch (AssetHandler.getSprite()) {
            case "guy": resetTextures();
                guyS.setTexture(guySE);
                break;
            case "girl": resetTextures();
                girlS.setTexture(girlSE);
                break;
        }
    }

    public void resetTextures() {
        guyS.setTexture(guyT);
        girlS.setTexture(girlT);
    }

    public void initButtons() {
        backT = new Texture(Gdx.files.internal("data/Play_Button.png"));
        backS = new Sprite(backT);
        backSD = new SpriteDrawable(backS);
        back = new Button(backSD);

        guyT = new Texture(Gdx.files.internal("data/Guy_Button.png"));
        guySE = new Texture(Gdx.files.internal("data/Guy_Selected_Button.png"));
        guyS = new Sprite(guyT);
        guySD = new SpriteDrawable(guyS);
        guy = new Button(guySD);

        girlT = new Texture(Gdx.files.internal("data/Girl_Button.png"));
        girlSE = new Texture(Gdx.files.internal("data/Girl_Selected_Button.png"));
        girlS = new Sprite(girlT);
        girlSD = new SpriteDrawable(girlS);
        girl = new Button(girlSD);
        if(!AssetHandler.prefs.contains("girl")) {
            AssetHandler.setOwned("girl", false);
            AssetHandler.prefs.flush();
        }
    }

    public void initListeners() {
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                world.setReady();
                Gdx.input.setInputProcessor(GameScreen.inputHandler);
            }
        });

        guy.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                AssetHandler.setSprite("guy");
                AssetHandler.prefs.flush();
            }
        });

        girl.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if(AssetHandler.getOwned("girl")) {
                    System.out.println("True");
                    AssetHandler.setSprite("girl");
                    AssetHandler.prefs.flush();
                } else if(girlCoins <= AssetHandler.getCoins()) {
                    AssetHandler.setCoins(AssetHandler.getCoins() - girlCoins);
                    AssetHandler.setOwned("girl", true);
                    AssetHandler.setSprite("girl");
                    AssetHandler.prefs.flush();
                }
            }
        });
    }

    public void setCoins() {
        girlCoins = 25;
    }

}
