package com.cg.alterman.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
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

    public void switchWorld(int yPos) {
        Vector2 pos = playerBody.getPosition();
        if (!flipTextureY) {
            pos.y -= (yPos + playerCurrentTexture.getHeight());
        } else {
            pos.y += (yPos + playerCurrentTexture.getHeight());
        }
        playerBody.setTransform(pos, 0);
        flipTextureY = !flipTextureY;
    }

    public void render(SpriteBatch batch) {
        if (playerBody.getPosition().x - (playerCurrentTexture.getHeight() / 2f) > 2000 && playerBody.getLinearVelocity().x > 0) {
            stopLeftRightMovement();
        }

        batch.draw(playerCurrentTexture, playerBody.getPosition().x - (playerCurrentTexture.getWidth() / 2f),
                playerBody.getPosition().y - (playerCurrentTexture.getHeight() / 2f),
                playerCurrentTexture.getWidth(), playerCurrentTexture.getHeight(),
                0, 0,
                playerCurrentTexture.getWidth(), playerCurrentTexture.getHeight(),
                flipTextureX, flipTextureY);
    }

    public void stopLeftRightMovement() {
        playerBody.setLinearVelocity(0, playerBody.getLinearVelocity().y);
        playerCurrentTexture = playerIdleTexture;
    }

    @Override
    public boolean keyDown(int keycode) {
        // TODO: Fix movement and those gigantic values
        Vector2 playerVelocity = playerBody.getLinearVelocity();
        if ((keycode == Input.Keys.W || keycode == Input.Keys.UP) && playerVelocity.y == 0) {
            playerCurrentTexture = playerJumpTexture;
            if (!flipTextureY) {
                playerBody.applyForceToCenter(0, 8000, true);
            } else {
                playerBody.applyForceToCenter(0, -8000, true);
            }
        } else if (keycode == Input.Keys.A || keycode == Input.Keys.LEFT) {
            playerBody.applyForceToCenter(-10000, 0, true);
            playerCurrentTexture = playerWalkTexture;
            flipTextureX = true;
        } else if ((keycode == Input.Keys.S || keycode == Input.Keys.DOWN) && playerVelocity.y == 0) {
            worldChangeListener.onSwitchWorld();
        } else if ((keycode == Input.Keys.D || keycode == Input.Keys.RIGHT) && playerBody.getPosition().x < 2048) {
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
        if ((keycode == Input.Keys.A || keycode == Input.Keys.LEFT && playerBody.getLinearVelocity().x < 0) ||
                (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D && playerBody.getLinearVelocity().x > 0)) {
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
        return playerBody.getPosition().x;
    }

    public float getYPosition() {
        return playerBody.getPosition().y;
    }

    public boolean intersectsBarrier(Barrier wall, int yPos) {
        // Kinda broken but I don't have time to fix it
        float y;

        Rectangle wallRect = wall.getPositionRect();
        if (!flipTextureY) {
            y = -(yPos + playerCurrentTexture.getHeight() + wallRect.getHeight() / 2);
        } else {
            y = (yPos + playerCurrentTexture.getHeight());
        }

        y += playerBody.getPosition().y;

        Rectangle playerRect = new Rectangle(playerBody.getPosition().x, y,
                playerCurrentTexture.getWidth(), playerCurrentTexture.getHeight());

        return playerRect.overlaps(wallRect) ||
                (y - wallRect.getHeight() < groundHeight && y + groundHeight > -groundHeight);
    }

    public interface WorldChangeListener {
        void onSwitchWorld();
    }
}
