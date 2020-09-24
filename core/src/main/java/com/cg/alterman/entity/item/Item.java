package com.cg.alterman.entity.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

public class Item {
    private Texture texture;
    private Vector2 position;
    private boolean useable;
    private boolean isUsing;

    private String itemInfo;
    private String usedInfo;
    private String unusedInfo;

    public Item(String itemTextureLocation) {
        this.texture = new Texture(Gdx.files.internal(itemTextureLocation));
        this.position = new Vector2();
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void render(SpriteBatch batch, boolean worldInverted, BitmapFont font, float playerXPosition) {
        if (texture == null) {
            return;
        }

        batch.draw(texture, position.x, position.y);

        if (!isUsing && Math.abs(playerXPosition - position.x - texture.getWidth() / 2f) < (texture.getWidth() * 2)) {
            if (worldInverted && position.y < 0) {
                font.setColor(Color.WHITE);
                font.draw(batch, "Press E to use", position.x - 20, position.y - 60);
                useable = true;
            } else if (!worldInverted && position.y > 0) {
                font.setColor(Color.BLACK);
                font.draw(batch, "Press E to use", position.x - 20, position.y + 120);
                useable = true;
            } else {
                useable = false;
            }
        } else {
            useable = false;
        }
    }

    protected void setItemInfo(String itemInfo, String usedInfo, String unusedInfo) {
        this.itemInfo = itemInfo;
        this.usedInfo = usedInfo;
        this.unusedInfo = unusedInfo;
    }

    public void useItem(Stage stage, Skin skin) {
        this.isUsing = true;

        final Dialog dialog = new Dialog("", skin) {
            @Override
            protected void result(Object object) {
                boolean b = (boolean) object;
                if (b) {
                    if (usedInfo != null) {
                        showUsedDialog(usedInfo, stage, skin);
                    }
                    dispose();
                } else {
                    if (unusedInfo != null) {
                        showNotUsedDialog(unusedInfo, stage, skin);
                    } else {
                        isUsing = false;
                    }
                }
            }
        };
        Label label = new Label(itemInfo, skin);
        label.setAlignment(Align.center);
        dialog.getContentTable().add(label).pad(20f);
        TextButton noButton = new TextButton("No", skin);
        TextButton yesButton = new TextButton("Yes", skin);
        noButton.pad(10f);
        yesButton.pad(10f);
        dialog.button(noButton, false);
        dialog.button(yesButton, true);
        dialog.getButtonTable().padBottom(20f);

        dialog.show(stage, Actions.fadeIn(0.2f));
        dialog.setPosition(Math.round((stage.getWidth() - dialog.getWidth()) / 2), Math.round((stage.getHeight() - dialog.getHeight()) / 2));
    }

    private void showUsedDialog(String usedInfo, Stage stage, Skin skin) {
        final Dialog dialog = new Dialog("", skin);
        Label label = new Label(usedInfo, skin);
        label.setAlignment(Align.center);
        dialog.getContentTable().add(label).pad(20f);
        TextButton continueButton = new TextButton("Continue", skin);
        continueButton.pad(10f);
        dialog.button(continueButton);
        dialog.getButtonTable().padBottom(20f);

        dialog.show(stage, Actions.fadeIn(0.2f));
        dialog.setPosition(Math.round((stage.getWidth() - dialog.getWidth()) / 2), Math.round((stage.getHeight() - dialog.getHeight()) / 2));
    }

    private void showNotUsedDialog(String unusedInfo, Stage stage, Skin skin) {
        final Dialog dialog = new Dialog("", skin) {
            @Override
            protected void result(Object object) {
                isUsing = false;
            }
        };
        Label label = new Label(unusedInfo, skin);
        label.setAlignment(Align.center);
        dialog.getContentTable().add(label).pad(20f);
        TextButton continueButton = new TextButton("Continue", skin);
        continueButton.pad(10f);
        dialog.button(continueButton);
        dialog.getButtonTable().padBottom(20f);

        dialog.show(stage, Actions.fadeIn(0.2f));
        dialog.setPosition(Math.round((stage.getWidth() - dialog.getWidth()) / 2), Math.round((stage.getHeight() - dialog.getHeight()) / 2));
    }

    public boolean isUseable() {
        return this.useable;
    }

    public void dispose() {
        if (texture != null) {
            texture.dispose();
            texture = null;
        }
    }

    public int getHeight() {
        if (texture != null) {
            return this.texture.getHeight();
        }

        return 0;
    }
}

