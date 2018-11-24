package com.honeyloaf.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.TimeUtils;
import com.honeyloaf.objects.Coin;
import com.honeyloaf.objects.Drone;
import com.honeyloaf.objects.Cloud;
import com.honeyloaf.objects.Player;
import com.honeyloaf.objects.Powerup;
import com.honeyloaf.objects.Rocket;
import com.honeyloaf.objects.Wind;

import java.util.Random;

/**
 * Created by michaelross on 6/11/17.
 */

public class ObjectHandler {


    public ShapeRenderer shapeRenderer;
    public SpriteBatch batcher;
    public OrthographicCamera cam;
    public Random rand;
    public int buffer, cloudBuffer, birdBuffer;
    public int rectX, rectY, rectHeight;
    public float runTime;
    //Game Objects
    public GameWorld world;
    private Player player;
    public Wind wind1, wind2, wind3;
    public Cloud cloud1, cloud2, cloud3, cloud4;
    public Drone drone1, drone2;
    public Rocket rocket;
    public Powerup powerup;
    public float powerupFade;
    public Coin coin;
    public float coinFade;

    public Sound coinSound, powerupSound;

    public int index;
    public boolean shake, rWarning, particle, cParticle, pParticle, sParticle;

    public ParticleEffect cloudParticle, coinParticle, powerupParticle, shieldParticle;

    public Texture warning;

    public AssetLoader assetLoader;


    public ObjectHandler(GameWorld world, OrthographicCamera cam) {
        this.world = world;
        this.cam = cam;
        assetLoader = world.assetLoader;
        initialize();
    }

    public void update(float delta) {
        shapeRenderer.setProjectionMatrix(cam.combined);
        batcher.setProjectionMatrix(cam.combined);
        if (world.isRunning()) {
            cloud1.update();
            cloud2.update();
            cloud3.update();
            cloud4.update();
            wind1.update();
            wind2.update();
            wind3.update();
            drone1.update();
            drone2.update();
            rocket.update();
            powerup.update();
            coin.update();
            checkWindCollision();
            checkEnemyCollision();
            checkCollectableCollision();
            resetObjects();
            shakeCamera();

            coin.playerX = player.x;
            coin.playerY = player.y + player.circleRad*3;
        }

    }

    public void resetObjects() {
        //Check if the wind is above the screen. If it is, put it back below the screen
        if (wind1.isOffScreen()) {
            wind1.reset(wind3.getBoundingRectangle().y - buffer - rand.nextInt(AssetHandler.SCREEN_HEIGHT / 4));
        }
        if (wind2.isOffScreen()) {
            wind2.reset(wind1.getBoundingRectangle().y - buffer - rand.nextInt(AssetHandler.SCREEN_HEIGHT / 4));
        }
        if (wind3.isOffScreen()) {
            wind3.reset(wind2.getBoundingRectangle().y - buffer - rand.nextInt(AssetHandler.SCREEN_HEIGHT / 4));
        }

        //Again, check if the clouds go off the screen and then reset them.
        cloudBuffer = rand.nextInt(AssetHandler.SCREEN_HEIGHT / 4) + AssetHandler.SCREEN_HEIGHT / 4;
        if (cloud1.isOffScreen()) {
            cloud1.reset(cloud4.y - cloudBuffer);
        }
        if (cloud2.isOffScreen()) {
            cloud2.reset(cloud1.y - cloudBuffer);
        }
        if (cloud3.isOffScreen()) {
            cloud3.reset(cloud2.y - cloudBuffer);
        }
        if (cloud4.isOffScreen()) {
            cloud4.reset(cloud3.y - cloudBuffer);
        }

        //Check if birds (drones) go above the screen and reset them
        birdBuffer = rand.nextInt(AssetHandler.SCREEN_HEIGHT / 2 + AssetHandler.SCREEN_HEIGHT / 3) + AssetHandler.SCREEN_HEIGHT / 2;
        if (drone1.isOffScreen()) {
            drone1.reset(drone2.y - birdBuffer);
        }
        birdBuffer = rand.nextInt(AssetHandler.SCREEN_HEIGHT / 2 + AssetHandler.SCREEN_HEIGHT / 3) + AssetHandler.SCREEN_HEIGHT / 2;
        if (drone2.isOffScreen()) {
            drone2.reset(drone1.y - birdBuffer);
        }

    }

    public void drawClouds() {
        batcher.begin();
        cloud1.cloud.draw(batcher);
        cloud2.cloud.draw(batcher);
        cloud3.cloud.draw(batcher);
        cloud4.cloud.draw(batcher);
        batcher.end();
    }

    public void drawRocket(float delta) {

        runTime += delta;

        /*
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.PURPLE);
            shapeRenderer.rect(rocket.boundingRectangle.getX(), rocket.boundingRectangle.getY(), rocket.boundingRectangle.getWidth(), rocket.boundingRectangle.getHeight());
            shapeRenderer.rect(player.circleX - AssetHandler.SCREEN_WIDTH/20, AssetHandler.SCREEN_HEIGHT/20, AssetHandler.SCREEN_WIDTH/10, AssetHandler.SCREEN_WIDTH/5);
            shapeRenderer.end();
         */

        batcher.begin();
        batcher.draw(rocket.rocketAnimation.getKeyFrame(runTime), rocket.x, rocket.y, rocket.width, rocket.height);
        batcher.end();

        if ((TimeUtils.timeSinceNanos(rocket.startTime) >= rocket.launchTime - 3000000000L) && !rocket.launched && world.isRunning()) {
            if (TimeUtils.timeSinceNanos(rocket.startTime) >= rocket.launchTime - 500000000L) {
                rWarning = true;
            }
            if ((int) (runTime) % 2 != 0) {
                batcher.begin();
                batcher.draw(warning, rocket.x - ((warning.getWidth() * AssetHandler.SCALE) / 2) + rocket.width / 2, AssetHandler.SCREEN_HEIGHT / 20, warning.getWidth() * AssetHandler.SCALE, warning.getHeight() * AssetHandler.SCALE);
                batcher.end();
            }
        } else {
            rWarning = false;
        }
    }

    public void drawPowerup() {
        batcher.begin();
        powerup.draw(batcher);
        batcher.end();
    }

    public void drawCoin() {
        batcher.begin();
        coin.draw(batcher);
        batcher.end();
    }

    public void drawWind(float delta) {


        /*
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(wind1.getSpriteRectangle().x, wind1.getSpriteRectangle().y, wind1.getSpriteRectangle().width, wind1.spriteRectangle.height);
        shapeRenderer.rect(wind2.getSpriteRectangle().x, wind2.getSpriteRectangle().y, wind2.getSpriteRectangle().width, wind2.getSpriteRectangle().height);
        shapeRenderer.rect(wind3.getSpriteRectangle().x, wind3.getSpriteRectangle().y, wind3.getSpriteRectangle().width, wind3.getSpriteRectangle().height);
        shapeRenderer.end();
        */


        /*
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(wind1.getBoundingRectangle().x, wind1.getBoundingRectangle().y, wind1.getBoundingRectangle().width, wind1.getBoundingRectangle().height);
        shapeRenderer.rect(wind2.getBoundingRectangle().x, wind2.getBoundingRectangle().y, wind2.getBoundingRectangle().width, wind2.getBoundingRectangle().height);
        shapeRenderer.rect(wind3.getBoundingRectangle().x, wind3.getBoundingRectangle().y, wind3.getBoundingRectangle().width, wind3.getBoundingRectangle().height);
        shapeRenderer.end();
        */


        wind1.draw(batcher, delta);
        wind2.draw(batcher, delta);
        wind3.draw(batcher, delta);

    }

    public void drawBirds(float delta) {

        /*
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(drone1.x, drone1.y, drone1.boundingRectangle.getWidth(), drone1.boundingRectangle.getHeight());
        shapeRenderer.rect(drone2.x, drone2.y, drone2.boundingRectangle.getWidth(), drone2.boundingRectangle.getHeight());
        shapeRenderer.end();
        */



        drone1.draw(batcher, delta);
        drone2.draw(batcher, delta);

    }


    public void setRectangles() {
        rectY = 0 - rectHeight;
        boolean left = rand.nextBoolean();
        rectX = rand.nextInt(AssetHandler.SCREEN_WIDTH - (int) wind1.width);
        System.out.println("Wind 1 X:" + rectX);
        wind1.setWind(rectX, rectY, left);

        rectY = rectY - buffer - rand.nextInt(AssetHandler.SCREEN_HEIGHT / 4);
        left = rand.nextBoolean();
        rectX = rand.nextInt(AssetHandler.SCREEN_WIDTH - (int) wind1.width);
        System.out.println("Wind 2 X:" + rectX);
        wind2.setWind(rectX, rectY, left);

        rectY = rectY - buffer - rand.nextInt(AssetHandler.SCREEN_HEIGHT / 4);
        left = rand.nextBoolean();
        rectX = rand.nextInt(AssetHandler.SCREEN_WIDTH - (int) wind1.width);
        System.out.println("Wind 3 X:" + rectX);
        wind3.setWind(rectX, rectY, left);
    }


    public void setClouds() {
        cloud1.reset(rand.nextInt(AssetHandler.SCREEN_HEIGHT / 2) + AssetHandler.SCREEN_HEIGHT / 2);
        cloudBuffer = rand.nextInt(AssetHandler.SCREEN_HEIGHT / 4) + AssetHandler.SCREEN_HEIGHT / 4;
        cloud2.reset(cloud1.y - cloudBuffer);
        cloudBuffer = rand.nextInt(AssetHandler.SCREEN_HEIGHT / 4) + AssetHandler.SCREEN_HEIGHT / 4;
        cloud3.reset(cloud2.y - cloudBuffer);
        cloudBuffer = rand.nextInt(AssetHandler.SCREEN_HEIGHT / 4) + AssetHandler.SCREEN_HEIGHT / 4;
        cloud4.reset(cloud3.y - cloudBuffer);
    }


    public void setBirds() {
        birdBuffer = rand.nextInt(AssetHandler.SCREEN_HEIGHT / 2 + AssetHandler.SCREEN_HEIGHT / 3) + AssetHandler.SCREEN_HEIGHT / 2;
        drone1.y = 0 - birdBuffer;
        birdBuffer = rand.nextInt(AssetHandler.SCREEN_HEIGHT / 2 + AssetHandler.SCREEN_HEIGHT / 3) + AssetHandler.SCREEN_HEIGHT / 2;
        ;
        drone2.y = drone1.y - birdBuffer;
    }


    float camShake = 0f;
    int shakeIntensity = AssetHandler.SCREEN_WIDTH / 45;
    int totalShake = 40;

    public void shakeCamera() {
        if (rocket.launched || rWarning) {
            shakeIntensity = AssetHandler.SCREEN_WIDTH / 45;
            totalShake = 90;
            shake = true;
        } else {
            shakeIntensity = AssetHandler.SCREEN_WIDTH / 20;
            totalShake = 10;
        }
        if (shake) {
            if (index > totalShake) {
                totalShake = 90;
            }
            if (totalShake == 90) {
                shakeIntensity = AssetHandler.SCREEN_WIDTH / 45;
            }
            if (index < totalShake) {
                if (index == 0) {
                    camShake += (shakeIntensity - camShake) * .1f;
                    cam.position.set(cam.position.x + (camShake / 2f), cam.position.y, 0);
                } else if (index < totalShake / 2) {
                    camShake += (shakeIntensity - camShake) * .1;
                    if (index % 2 != 0) {
                        cam.position.set(cam.position.x - camShake, cam.position.y, 0);
                    } else {
                        cam.position.set(cam.position.x + camShake, cam.position.y, 0);
                    }
                } else {
                    camShake -= (shakeIntensity - camShake) * .1f;
                    if (index % 2 != 0) {
                        cam.position.set(cam.position.x - camShake, cam.position.y, 0);
                    } else {
                        cam.position.set(cam.position.x + camShake, cam.position.y, 0);
                    }
                }
                index++;
            }
        }
        if (index >= totalShake) {
            shake = false;
            index = 0;
            cam.position.set(AssetHandler.SCREEN_WIDTH / 2, cam.position.y, 0);
        }
    }


    public void drawParticle() {

        if(particle) {
            batcher.begin();
            cloudParticle.draw(batcher, Gdx.graphics.getDeltaTime());
            batcher.end();
            if(cloudParticle.isComplete()) {
                particle = false;
            }
        }

        if(cParticle) {
            batcher.begin();
            coinParticle.draw(batcher, Gdx.graphics.getDeltaTime());
            batcher.end();
            if(coinParticle.isComplete()) {
                cParticle = false;
            }
        }

        if(pParticle) {
            batcher.begin();
            powerupParticle.draw(batcher, Gdx.graphics.getDeltaTime());
            batcher.end();
            if(powerupParticle.isComplete()) {
                pParticle = false;
            }
        }

        if(sParticle) {
            batcher.begin();
            shieldParticle.draw(batcher, Gdx.graphics.getDeltaTime());
            batcher.end();
            if(shieldParticle.isComplete()) {
                sParticle = false;
            }
        }


    }

    public void checkWindCollision() {

        /*
         *  Checks Wind Collision For Player Velocity Disruption and Camera Shake
         */


        //Check 1st Wind

        if (Intersector.overlaps(player.boundingCircle, wind1.boundingRectangle)) {
            if (!wind1.angry) {
                //Blow Right
                if (wind1.left && (player.x + AssetHandler.WIND_STRENGTH + player.circleRad < AssetHandler.SCREEN_WIDTH)) {
                    player.x += AssetHandler.WIND_STRENGTH;
                } else if (!wind1.left && player.x - AssetHandler.WIND_STRENGTH - player.circleRad > 0) {
                    player.x -= AssetHandler.WIND_STRENGTH;
                }
            } else {
                wind1.boundingRectangle.x = -wind1.boundingRectangle.width;
                player.paralyzed = true;
                player.paralyzedStart = TimeUtils.nanoTime();
                shake = true;
            }

        }


        //Check 2nd wind

        if (Intersector.overlaps(player.boundingCircle, wind2.boundingRectangle)) {
            if (!wind2.angry) {
                if (wind2.left && (player.x + AssetHandler.WIND_STRENGTH + player.circleRad < AssetHandler.SCREEN_WIDTH)) {
                    player.x += AssetHandler.WIND_STRENGTH;
                } else if (!wind2.left && player.x - AssetHandler.WIND_STRENGTH - player.circleRad > 0) {
                    player.x -= AssetHandler.WIND_STRENGTH;
                }
            } else {
                wind2.boundingRectangle.x = -wind2.boundingRectangle.width;
                player.paralyzed = true;
                player.paralyzedStart = TimeUtils.nanoTime();
                shake = true;
            }
        }


        //Check 3rd wind

        if (Intersector.overlaps(player.boundingCircle, wind3.boundingRectangle)) {
            if (!wind3.angry) {
                if (wind3.left && (player.x + AssetHandler.WIND_STRENGTH + player.circleRad < AssetHandler.SCREEN_WIDTH)) {
                    player.x += AssetHandler.WIND_STRENGTH;
                } else if (!wind3.left && player.x - AssetHandler.WIND_STRENGTH - player.circleRad > 0) {
                    player.x -= AssetHandler.WIND_STRENGTH;
                }
            } else {
                wind3.boundingRectangle.x = -wind3.boundingRectangle.width;
                player.paralyzed = true;
                player.paralyzedStart = TimeUtils.nanoTime();
                shake = true;
            }
        }


        /*
         *  Check Wind Collision For Particle Effect Generation
         */


        //Check 1st Wind

        if(Intersector.overlaps(player.boundingCircle, wind1.spriteRectangle)) {
            if(!particle) {
                cloudParticle.setPosition(player.x, wind1.y + wind1.height/2);
                if(cloudParticle.isComplete())
                    cloudParticle.reset();
                particle = true;
            } else {
                cloudParticle.setPosition(cloudParticle.getEmitters().first().getX(), wind1.y + wind1.height/2);
            }
            if(wind1.angry) {
                cloudParticle.setFlip(false, true);
            } else {
                cloudParticle.setFlip(false, false);
            }
        }


        //Check 2nd Wind

        if(Intersector.overlaps(player.boundingCircle, wind2.spriteRectangle)) {
            if(!particle) {
                cloudParticle.setPosition(player.x, wind2.y + wind2.height/2);
                if(cloudParticle.isComplete())
                    cloudParticle.reset();
                particle = true;
            } else {
                cloudParticle.setPosition(cloudParticle.getEmitters().first().getX(), wind2.y + wind2.height/2);
            }
            if(wind2.angry) {
                cloudParticle.setFlip(false, true);
            } else {
                cloudParticle.setFlip(false, false);
            }
        }


        //Check 3rd Wind

        if(Intersector.overlaps(player.boundingCircle, wind3.spriteRectangle)) {
            if(!particle) {
                cloudParticle.setPosition(player.x, wind3.y + wind3.height/2);
                if(cloudParticle.isComplete())
                    cloudParticle.reset();
                particle = true;
            } else {
                cloudParticle.setPosition(cloudParticle.getEmitters().first().getX(), wind3.y + wind3.height/2);
            }
            if(wind3.angry) {
                cloudParticle.setFlip(false, true);
            } else {
                cloudParticle.setFlip(false, false);
            }
        }

    }

    public void checkEnemyCollision() {


        //Check Drones

        if (Intersector.overlaps(player.boundingCircle, drone1.boundingRectangle)) {
            if (!player.shield) {
                world.setGameOver();
            } else {
                if (drone1.left) {
                    drone1.x = AssetHandler.SCREEN_WIDTH * 2;
                } else {
                    drone1.x = -drone1.width * 2;
                }
                shieldParticle.setPosition(player.x, player.y);
                shieldParticle.reset();
                sParticle = true;
                player.shield = false;
                shake = true;
            }
        }
        if (Intersector.overlaps(player.boundingCircle, drone2.boundingRectangle)) {
            if (!player.shield) {
                world.setGameOver();
            } else {
                if (drone2.left) {
                    drone2.x = AssetHandler.SCREEN_WIDTH * 2;
                } else {
                    drone2.x = -drone2.width * 2;
                }
                shieldParticle.setPosition(player.x, player.y);
                shieldParticle.reset();
                sParticle = true;
                player.shield = false;
                shake = true;
            }
        }


        //Check Rocket

        if (Intersector.overlaps(player.boundingCircle, rocket.boundingRectangle)) {
            if (!player.shield) {
                world.setGameOver();
            } else {
                rocket.reset();
                shieldParticle.setPosition(player.x, player.y);
                shieldParticle.reset();
                sParticle = true;
                world.player.shield = false;
            }
        }
    }

    public void checkCollectableCollision() {


        //Check Powerups

        if (Intersector.overlaps(powerup.boundingCircle, player.boundingCircle)) {
            powerupParticle.reset();
            pParticle = true;
            powerupParticle.setPosition(powerup.x + powerup.radius, powerup.y + powerup.radius);
            powerup.collected();
            shake = true;
            powerupSound.play(1f);
        }


        //Check Coins

        if (Intersector.overlaps(coin.boundingCircle, player.boundingCircle)) {
            coinParticle.reset();
            //cParticle = true;
            coinParticle.setPosition(coin.x + coin.radius, coin.y + coin.radius);
            AssetHandler.setCoins(AssetHandler.getCoins() + 1);
            AssetHandler.prefs.flush();
            coin.fade = 1f;
            shake = true;
            coinSound.play(0.3f);
            coin.y = AssetHandler.SCREEN_HEIGHT/4;
        }
    }

    public void initialize() {
        //Instantiate Objects
        player = world.player;
        wind1 = new Wind(assetLoader);
        wind2 = new Wind(assetLoader);
        wind3 = new Wind(assetLoader);
        cloud1 = new Cloud(assetLoader);
        cloud2 = new Cloud(assetLoader);
        cloud3 = new Cloud(assetLoader);
        cloud4 = new Cloud(assetLoader);
        drone1 = new Drone(assetLoader);
        drone2 = new Drone(assetLoader);
        rocket = new Rocket(player, assetLoader);
        powerup = new Powerup(player, assetLoader);
        coin = new Coin(assetLoader, player);
        shapeRenderer = new ShapeRenderer();
        batcher = new SpriteBatch();
        rand = new Random();
        rectHeight = AssetHandler.SCREEN_HEIGHT / 6;
        buffer = (int) (rectHeight * 4.5);
        runTime = 0f;
        index = 0;
        powerupFade = 0f;
        coinFade = 0f;
        shake = false;
        //Set position of objects
        setRectangles();
        setClouds();
        setBirds();

        coinSound = AssetLoader.manager.get(AssetLoader.COIN_SOUND, Sound.class);
        powerupSound = AssetLoader.manager.get(AssetLoader.POWERUP_SOUND, Sound.class);

        cloudParticle = new ParticleEffect();
        cloudParticle.load(Gdx.files.internal("data/cloud-particle.p"), Gdx.files.internal("data"));
        cloudParticle.scaleEffect(AssetHandler.SCALE);
        cloudParticle.start();

        coinParticle = new ParticleEffect();
        coinParticle.load(Gdx.files.internal("data/powerup-particle.p"), Gdx.files.internal("data"));
        coinParticle.getEmitters().first().getTint().setColors(new float[]{1f, 191/255f, 54/255f});
        coinParticle.scaleEffect(AssetHandler.SCALE);
        coinParticle.start();


        powerupParticle = new ParticleEffect();
        powerupParticle.load(Gdx.files.internal("data/powerup-particle.p"), Gdx.files.internal("data"));
        powerupParticle.getEmitters().first().getTint().setColors(new float[]{104/255f, 43/255f, 130/255f});
        powerupParticle.scaleEffect(AssetHandler.SCALE);
        powerupParticle.start();

        shieldParticle = new ParticleEffect();
        shieldParticle.load(Gdx.files.internal("data/powerup-particle.p"), Gdx.files.internal("data"));
        shieldParticle.getEmitters().first().getTint().setColors(new float[] {131/255f, 224/255f, 76/255f});
        shieldParticle.scaleEffect(AssetHandler.SCALE);
        shieldParticle.start();



        rWarning = false;
        warning = new Texture(Gdx.files.internal("data/Warning.png"));
    }

}
