package com.cg.alterman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cg.alterman.Alterman;
import com.cg.alterman.Constants;
import com.cg.alterman.ShapeDrawer;
import com.cg.alterman.entity.Barrier;
import com.cg.alterman.entity.Ground;
import com.cg.alterman.entity.Player;
import com.cg.alterman.entity.item.Item;
import com.cg.alterman.entity.item.Jam;
import com.cg.alterman.entity.item.Paper;
import com.cg.alterman.entity.item.Paper2;
import com.cg.alterman.entity.item.Signpost;
import com.cg.alterman.entity.item.Signpost2;
import com.cg.alterman.entity.item.Signpost3;
import com.cg.alterman.entity.item.Win;
import com.cg.alterman.entity.item.Zebra;

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
    private Barrier[] walls;
    private Array<Item> items;
    private int levelWidth = 2048;
    private boolean worldInverted;
    private Stage stage;

    public GameScreen(Alterman game) {
        this.game = game;

        this.viewport = new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        this.centerCamera = true;
        this.usedTextures = new Array<>();
        this.batch = new SpriteBatch();
        this.stage = new Stage(new ScreenViewport());
        this.shapeDrawer = new ShapeDrawer(this.batch, this.usedTextures);

        initWorld();
        initWorldObjects();
    }

    private void initWorld() {
        gameWorld = new World(new Vector2(0, -100f), true);
        ground = new Ground(gameWorld, levelWidth);
        this.player = new Player(gameWorld, ground.getHeight(), this::invertWorld);
    }

    private void initWorldObjects() {
        items = new Array<>();

        Signpost signpost = new Signpost();
        signpost.setPosition(new Vector2(100f, ground.getHeight()));
        items.add(signpost);

        Jam jam = new Jam();
        jam.setPosition(new Vector2(510, ground.getHeight()));
        items.add(jam);

        Paper paper = new Paper();
        paper.setPosition(new Vector2(150, -ground.getHeight() - paper.getHeight()));
        items.add(paper);

        Zebra zebra = new Zebra();
        zebra.setPosition(new Vector2(1020, 100 + ground.getHeight()));
        items.add(zebra);

        Signpost2 signpost2 = new Signpost2();
        signpost2.setPosition(new Vector2(1200, ground.getHeight()));
        items.add(signpost2);

        Signpost3 signpost3 = new Signpost3();
        signpost3.setPosition(new Vector2(1500, ground.getHeight()));
        items.add(signpost3);

        Paper2 paper2 = new Paper2();
        paper2.setPosition(new Vector2(levelWidth - 670, 400 + ground.getHeight()));
        items.add(paper2);

        Win win = new Win();
        win.setPosition(new Vector2(levelWidth - 920, 350 + ground.getHeight()));
        items.add(win);

        Vector2[] wallPos = new Vector2[]{
                new Vector2(0,          ground.getHeight()       ),
                new Vector2(0,        ground.getHeight() + 50 ),
                new Vector2(0,        ground.getHeight() + 100),
                new Vector2(0,       -ground.getHeight() - 50 ),
                new Vector2(0,       -ground.getHeight() - 100),
                new Vector2(0,       -ground.getHeight() - 150),
                new Vector2(levelWidth, -ground.getHeight() - 150),
                new Vector2(levelWidth, -ground.getHeight() - 100),
                new Vector2(levelWidth, -ground.getHeight() - 50 ),
                new Vector2(levelWidth,     ground.getHeight()      ),
                new Vector2(levelWidth,  ground.getHeight() + 50 ),
                new Vector2(levelWidth,  ground.getHeight() + 100),

                new Vector2(250,            ground.getHeight()),
                new Vector2(250,  +50+   ground.getHeight()),
                new Vector2(250,  -50-   ground.getHeight()),
                new Vector2(300,  -50-   ground.getHeight()),
                new Vector2(300,  -100-   ground.getHeight()),
                new Vector2(300,  -150-   ground.getHeight()),

                new Vector2(450,            ground.getHeight()),
                new Vector2(450, +50+    ground.getHeight()),
                new Vector2(450, -50-    ground.getHeight()),
                new Vector2(600,            ground.getHeight()),
                new Vector2(600, +50+    ground.getHeight()),
                new Vector2(600, +100+   ground.getHeight()),
                new Vector2(600, +150+   ground.getHeight()),
                new Vector2(600, +200+   ground.getHeight()),
                new Vector2(600, +250+   ground.getHeight()),

                new Vector2(800,            ground.getHeight()),
                new Vector2(800, +50+    ground.getHeight()),
                new Vector2(800, -50-    ground.getHeight()),
                new Vector2(800, -100-   ground.getHeight()),
                new Vector2(850, -50-    ground.getHeight()),

                new Vector2(1050,            ground.getHeight()),
                new Vector2(1050, +50+    ground.getHeight()),
                new Vector2(1050, -50-    ground.getHeight()),
                new Vector2(1050, -100-   ground.getHeight()),
                new Vector2(1100, -50-   ground.getHeight()),

                new Vector2(1400,            ground.getHeight()),
                new Vector2(1400, +50+    ground.getHeight()),
                new Vector2(1400, -50-    ground.getHeight()),
                new Vector2(1400, -100-   ground.getHeight()),
                new Vector2(1450,            ground.getHeight()),

                new Vector2(1700,            ground.getHeight()),
                new Vector2(1700, +50+    ground.getHeight()),
                new Vector2(1700, -50-    ground.getHeight()),
                new Vector2(1700, -100-   ground.getHeight()),
                new Vector2(1750, -50-    ground.getHeight()),
                new Vector2(1750, -100-   ground.getHeight()),
                new Vector2(1750, -150-   ground.getHeight()),
                new Vector2(1750, -200-   ground.getHeight()),

                new Vector2(levelWidth,  ground.getHeight() + 150),
                new Vector2(levelWidth - 100,  ground.getHeight() + 200),
                new Vector2(levelWidth - 150,  ground.getHeight() + 250),
                new Vector2(levelWidth - 200,  ground.getHeight() + 300),
                new Vector2(levelWidth - 350,  ground.getHeight() + 300),
                new Vector2(levelWidth - 500,  ground.getHeight() + 300),
                new Vector2(levelWidth - 650,  ground.getHeight() + 350),
                new Vector2(levelWidth - 900,  ground.getHeight() + 300),
        };
        this.walls = new Barrier[wallPos.length];
        for (int i = 0; i < wallPos.length; i++) {
            this.walls[i] = new Barrier(gameWorld);
            this.walls[i].setPosition(wallPos[i]);
        }
    }

    private void invertWorld() {
        Vector2 gravity = gameWorld.getGravity();
        gravity.y *= -1;
        worldInverted = !worldInverted;
        gameWorld.setGravity(gravity);

        int yPos = (ground.getHeight() * 2);
        boolean repeatLoop;
        do {
            repeatLoop = false;
            for (Barrier wall : walls) {
                if (player.intersectsBarrier(wall, yPos)) {
                    yPos += 10;
                    repeatLoop = true;
                    break;
                }
            }
        } while (repeatLoop);

        player.switchWorld(yPos);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputMultiplexer(this, stage, player));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, centerCamera);
        stage.getViewport().update(width, height, true);
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
        for (Barrier wall : walls) {
            wall.render(batch);
        }
        for (Item item : items) {
            item.render(batch, worldInverted, game.getSkin().getFont("list"), player.getXPosition(), player.getYPosition());
        }
        player.render(batch);
        batch.end();

        stage.act(delta);
        stage.draw();

        gameWorld.step(delta, 6, 2);
    }

    private void followPlayer(float delta) {
        float lerp = 1.8f;
        Vector3 position = viewport.getCamera().position;

        float posX = player.getXPosition();
        posX = MathUtils.clamp(posX, viewport.getScreenWidth() / 2f, levelWidth - (viewport.getScreenWidth() / 2f));

        float posY = viewport.getWorldHeight() / 2;
        if (worldInverted) {
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
        for (Barrier wall : walls) {
            wall.dispose();
        }
        for (Item item : items) {
            item.dispose();
        }
        for (Texture texture : usedTextures) {
            texture.dispose();
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.E) {
            for (Item item : items) {
                if (item.isUseable()) {
                    player.stopLeftRightMovement();
                    item.useItem(stage, game.getSkin());
                    return true;
                }
            }
        }

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
