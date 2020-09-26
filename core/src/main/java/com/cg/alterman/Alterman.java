package com.cg.alterman;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.cg.alterman.screens.MenuScreen;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Alterman extends Game {
    private Skin skin;
    private Music music;
    private Sound useSoundEffect;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Gdx.app.LOG_DEBUG);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        skin.getFont("subtitle").getData().setScale(5.0f);

        setScreen(new MenuScreen(this));

        useSoundEffect = Gdx.audio.newSound(Gdx.files.internal("useSound.wav"));
    }

    public Skin getSkin() {
        return skin;
    }

    public void cueMusic() {
        if (music == null) {
            // Echo Blue Nova
            music = Gdx.audio.newMusic(Gdx.files.internal("nova.mp3"));
            music.setLooping(true);
        }

        if (!music.isPlaying()) {
            music.play();
        }
    }

    public Sound getUseSoundEffect() {
        return useSoundEffect;
    }

    @Override
    public void dispose() {
        super.dispose();
        skin.dispose();
        music.dispose();
        useSoundEffect.dispose();
    }
}