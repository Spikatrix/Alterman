package com.cg.alterman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.cg.alterman.Alterman;
import com.cg.alterman.scene2d.StageMaker;

/**
 * First screen of the application. Displayed after the application is created.
 */
public class MenuScreen extends ScreenAdapter {
    final Alterman game;

    private Stage stage;
    private Array<Texture> usedTextures;

    public MenuScreen(Alterman game) {
        this.game = game;

        this.stage = new Stage(new ScreenViewport());
        this.usedTextures = new Array<>();
    }

    @Override
    public void show() {
        StageMaker stageMaker = new StageMaker(game.getSkin(), usedTextures);
        stageMaker.setUpStage(this.getClass().getName(), stage);
        stageMaker.setButtonClickListener(button -> {
            if (button == StageMaker.Button.MENU_SCREEN_PLAY) {
                transitionScreen();
            }
        });
        Gdx.input.setInputProcessor(stage);
    }

    private void transitionScreen() {
        Gdx.app.postRunnable(() -> {
            dispose();
            game.setScreen(new GameScreen(game));
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        for (Texture texture : usedTextures) {
            texture.dispose();
        }
    }
}