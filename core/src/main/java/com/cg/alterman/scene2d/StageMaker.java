package com.cg.alterman.scene2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.cg.alterman.screens.MenuScreen;
import com.rafaskoberg.gdx.typinglabel.TypingAdapter;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;

// Not to be confused with AWS Sagemaker :p
public class StageMaker {
    private Skin skin;
    private Array<Texture> usedTextures;

    private ButtonClickListener buttonClickListener;

    public StageMaker(Skin skin, Array<Texture> usedTextures) {
        this.skin = skin;
        this.usedTextures = usedTextures;
    }

    public void setUpStage(String className, Stage stage) {
        if (className.equals(MenuScreen.class.getName())) {
            setUpMenuScreen(stage);
        }
    }

    private void setUpMenuScreen(Stage stage) {
        Table centerTable = new Table();
        centerTable.setFillParent(true);

        String gameNameString = "{COLOR=0FA7F9}{FADE=0;1;1.5}ALTER{WAIT}{COLOR=A1A1A1}MAN";
        TypingLabel gameNameLabel = new TypingLabel(gameNameString, skin);

        Texture playTexture = new Texture(Gdx.files.internal("ui/ic_play.png"));
        usedTextures.add(playTexture);
        HoverImageButton playButton = new HoverImageButton(new TextureRegionDrawable(playTexture));
        playButton.setHoverAlpha(.75f);
        playButton.setClickAlpha(.5f);
        playButton.getColor().a = 0f;

        gameNameLabel.setTypingListener(new TypingAdapter() {
            @Override
            public void end() {
                playButton.addAction(Actions.sequence(
                        Actions.delay(.75f),
                        Actions.fadeIn(.5f, Interpolation.smooth))
                );
            }
        });

        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (buttonClickListener != null) {
                    buttonClickListener.onButtonClick(Button.MENU_SCREEN_PLAY);
                }
            }
        });

        centerTable.add(gameNameLabel).space(25f);
        centerTable.row();
        centerTable.add(playButton).space(25f);

        stage.addActor(centerTable);
    }

    public void setButtonClickListener(ButtonClickListener buttonClickListener) {
        this.buttonClickListener = buttonClickListener;
    }

    public enum Button {
        MENU_SCREEN_PLAY,
        MENU_SCREEN_EXIT,
    }

    public interface ButtonClickListener {
        void onButtonClick(Button button);
    }
}
