package com.cg.alterman.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Ground {
    private PolygonShape groundBox;
    private Body groundBody;
    private Texture groundTexture;
    private int levelWidth;

    public Ground(World world, int levelWidth) {
        this.levelWidth = levelWidth;

        createGroundTexture();
        createGround(world);
    }

    private void createGroundTexture() {
        groundTexture = new Texture(Gdx.files.internal("ground.png"));
        groundTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }

    private void createGround(World world) {
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(new Vector2(this.levelWidth / 2f, 0));

        groundBody = world.createBody(groundBodyDef);

        groundBox = new PolygonShape();
        groundBox.setAsBox(this.levelWidth / 2f, groundTexture.getHeight() / 2f);

        groundBody.createFixture(groundBox, 0.0f);
    }

    public void render(SpriteBatch batch) {
        batch.draw(groundTexture, groundBody.getPosition().x - (this.levelWidth / 2f),
                groundBody.getPosition().y - (groundTexture.getHeight() / 2f),
                0, 0,
                this.levelWidth, groundTexture.getHeight());
    }

    public int getHeight() {
        return this.groundTexture.getHeight() / 2; // Divided by two as only half the ground is shown
    }

    public void dispose() {
        groundBox.dispose();
        groundTexture.dispose();
    }
}
