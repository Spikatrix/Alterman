package com.cg.alterman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cg.alterman.Alterman;
import com.cg.alterman.Constants;
import com.cg.alterman.ShapeDrawer;
import com.cg.alterman.entity.Ground;
import com.cg.alterman.entity.Player;

public class GameScreen extends ScreenAdapter implements InputProcessor {
    final Alterman game;

    private Viewport viewport;
    private ShapeDrawer shapeDrawer;
    private SpriteBatch batch;
    private Player player;
    private Array<Texture> usedTextures;
    private boolean centerCamera;
    private World gameWorld;
    private Ground ground;
    private int levelWidth = 768;
    private Box2DDebugRenderer debugRenderer;

    public GameScreen(Alterman game) {
        this.game = game;

        this.viewport = new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        this.centerCamera = true;
        this.usedTextures = new Array<>();
        this.batch = new SpriteBatch();
        this.shapeDrawer = new ShapeDrawer(this.batch, this.usedTextures);

        initWorld();
    }

    private void initWorld() {
        gameWorld = new World(new Vector2(0, -10f), true);
        ground = new Ground(gameWorld, levelWidth);
        this.player = new Player(gameWorld, ground.getHeight(), () -> {
            Vector2 gravity = gameWorld.getGravity();
            gravity.y *= -1;
            gameWorld.setGravity(gravity);
        });
        debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputMultiplexer(this, player));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, centerCamera);
        centerCamera = false; // Center camera only the first time around
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        followPlayer(delta);

        batch.begin();
        shapeDrawer.filledRectangle(0, 0, levelWidth, viewport.getWorldHeight(),
                Constants.SKY_COLOR, Constants.SKY_COLOR, Constants.GROUND_COLOR, Constants.GROUND_COLOR);
        shapeDrawer.filledRectangle(0, 0, levelWidth, -viewport.getWorldHeight(),
                Constants.UNDERWORLD_SKY_COLOR, Constants.UNDERWORLD_SKY_COLOR, Constants.UNDERWORLD_COLOR, Constants.UNDERWORLD_COLOR);
        ground.render(batch);
        player.render(batch);
        batch.end();

        debugRenderer.render(gameWorld, viewport.getCamera().combined);

        gameWorld.step(delta * 4, 6, 2);
    }

    private void followPlayer(float delta) {
        float lerp = 1.8f;
        Vector3 position = viewport.getCamera().position;

        float posX = player.getXPosition();
        //posX = MathUtils.clamp(posX - (viewport.getWorldWidth() / 2), 0, levelWidth);
        // TODO: Clamp this properly ^

        float posY = viewport.getWorldHeight() / 2;
        if (gameWorld.getGravity().y > 0) {
            posY *= -1;
        }

        position.x += (posX - position.x) * lerp * delta;
        position.y += (posY - position.y) * lerp * delta;
    }

    @Override
    public void dispose() {
        gameWorld.dispose();
        batch.dispose();
        ground.dispose();
        player.dispose();
        debugRenderer.dispose();
        for (Texture texture : usedTextures) {
            texture.dispose();
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        /*OrthographicCamera camera = (OrthographicCamera) viewport.getCamera();
        if (keycode == Input.Keys.DOWN) {
            camera.translate(0, -40);
        } else if (keycode == Input.Keys.UP) {
            camera.translate(0, 40);
        } else if (keycode == Input.Keys.RIGHT) {
            camera.translate(40, 0);
        } else if (keycode == Input.Keys.LEFT) {
            camera.translate(-40, 0);
        } else if (keycode == Input.Keys.MINUS) {
            camera.zoom += .15f;
        } else if (keycode == Input.Keys.EQUALS) {
            camera.zoom -= .15f;
        } else {
            return false;
        }*/

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
