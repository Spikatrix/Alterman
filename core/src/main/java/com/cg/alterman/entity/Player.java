package com.cg.alterman.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends InputAdapter {
    private Texture playerCurrentTexture;
    private boolean flipTextureX, flipTextureY;
    private Body playerBody;
    private PolygonShape playerBox;
    private WorldChangeListener worldChangeListener;
    private int groundHeight;

    private Texture playerIdleTexture;
    private Texture playerWalkTexture;
    private Texture playerJumpTexture;

    public Player(World world, int groundHeight, WorldChangeListener worldChangeListener) {
        this.worldChangeListener = worldChangeListener;
        this.groundHeight = groundHeight;

        createPlayerTexture();
        createPlayer(world);

    }

    private void createPlayerTexture() {
        playerIdleTexture = new Texture(Gdx.files.internal("player/player_stand.png"));
        playerWalkTexture = new Texture(Gdx.files.internal("player/player_walk1.png"));
        playerJumpTexture = new Texture(Gdx.files.internal("player/player_jump.png"));
        playerCurrentTexture = playerIdleTexture;
    }

    private void createPlayer(World world) {
        BodyDef playerBodyDef = new BodyDef();
        playerBodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBodyDef.position.set(new Vector2(playerCurrentTexture.getWidth(), groundHeight + (playerCurrentTexture.getHeight() / 2f)));

        playerBody = world.createBody(playerBodyDef);

        playerBox = new PolygonShape();
        playerBox.setAsBox(playerCurrentTexture.getWidth() / 2f, playerCurrentTexture.getHeight() / 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = playerBox;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f; // Bounce

        playerBody.createFixture(fixtureDef);
    }

    public void render(SpriteBatch batch) {
        batch.draw(playerCurrentTexture, playerBody.getPosition().x - (playerCurrentTexture.getWidth() / 2f),
                playerBody.getPosition().y - (playerCurrentTexture.getHeight() / 2f),
                playerCurrentTexture.getWidth(), playerCurrentTexture.getHeight(),
                0, 0,
                playerCurrentTexture.getWidth(), playerCurrentTexture.getHeight(),
                flipTextureX, flipTextureY);
    }

    @Override
    public boolean keyDown(int keycode) {
        Vector2 playerVelocity = playerBody.getLinearVelocity();
        if ((keycode == Input.Keys.W || keycode == Input.Keys.UP) && playerVelocity.y == 0) {
            /*playerBody.applyLinearImpulse(new Vector2(0, 10600),
                    playerBody.getWorldCenter(), true);*/
            //playerBody.setLinearVelocity(playerVelocity.x, 1000);
            playerCurrentTexture = playerJumpTexture;
            if (!flipTextureY) {
                playerBody.applyForceToCenter(0, 10000, true);
            } else {
                playerBody.applyForceToCenter(0, -10000, true);
            }
        } else if (keycode == Input.Keys.A || keycode == Input.Keys.LEFT) {
            /*playerBody.applyLinearImpulse(new Vector2(600, 0),
                    playerBody.getWorldCenter(), true);*/
            //playerBody.setLinearVelocity(-200, playerVelocity.y);
            playerBody.applyForceToCenter(-10000, 0, true);
            playerCurrentTexture = playerWalkTexture;
            flipTextureX = true;
        } else if ((keycode == Input.Keys.S || keycode == Input.Keys.DOWN) && playerVelocity.y == 0) {
            Vector2 pos = playerBody.getPosition();
            if (!flipTextureY) {
                pos.y -= ((groundHeight * 2) + playerCurrentTexture.getHeight());
            } else {
                pos.y += ((groundHeight * 2) + playerCurrentTexture.getHeight());
            }
            playerBody.setTransform(pos, 0);
            worldChangeListener.onSwitchWorld();
            flipTextureY = !flipTextureY;
        } else if (keycode == Input.Keys.D || keycode == Input.Keys.RIGHT) {
            //playerBody.setLinearVelocity(200, playerVelocity.y);
            playerBody.applyForceToCenter(10000, 0, true);
            playerCurrentTexture = playerWalkTexture;
            flipTextureX = false;
        } else {
            return false;
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.A || keycode == Input.Keys.D
                || keycode == Input.Keys.LEFT || keycode == Input.Keys.RIGHT) {
            playerBody.setLinearVelocity(0, playerBody.getLinearVelocity().y);
            playerCurrentTexture = playerIdleTexture;
            return true;
        }

        return false;
    }

    public void dispose() {
        playerIdleTexture.dispose();
        playerWalkTexture.dispose();
        playerJumpTexture.dispose();
    }

    public float getXPosition() {
        if (flipTextureY) {
            return playerBody.getPosition().x + (playerCurrentTexture.getWidth() / 2f) + (playerCurrentTexture.getHeight() / 2f);
        } else {
            return playerBody.getPosition().x + (playerCurrentTexture.getWidth() / 2f) - (playerCurrentTexture.getHeight() / 2f);
        }
    }

    public interface WorldChangeListener {
        void onSwitchWorld();
    }
}
