package com.cg.alterman;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.cg.alterman.screens.MenuScreen;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Alterman extends Game {
    private Skin skin;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Gdx.app.LOG_DEBUG);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        skin.getFont("subtitle").getData().setScale(5.0f);

        setScreen(new MenuScreen(this));
    }

    public Skin getSkin() {
        return skin;
    }
}