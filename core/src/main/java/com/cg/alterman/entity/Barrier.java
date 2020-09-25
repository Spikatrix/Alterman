package com.cg.alterman.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Barrier {
    private Texture wall1;
    private Texture wall2;

    private Body wallBody;
    private PolygonShape wallBox;

    public Barrier(World world) {
        this.wall1 = new Texture("wall1.png");
        this.wall2 = new Texture("wall2.png");

        createWall(world);
    }

    private void createWall(World world) {
        BodyDef wallBodyDef = new BodyDef();
        wallBodyDef.type = BodyDef.BodyType.StaticBody;
        wallBodyDef.position.set(new Vector2(wall1.getWidth() / 2f, wall1.getHeight() / 2f));

        wallBody = world.createBody(wallBodyDef);

        wallBox = new PolygonShape();
        wallBox.setAsBox(wall1.getWidth() / 2f, wall1.getHeight() / 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = wallBox;
        fixtureDef.density = 1f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0f; // Bounce

        wallBody.createFixture(fixtureDef);
    }

    public void setPosition(Vector2 position) {
        position.y += (wall1.getHeight() / 2f);
        this.wallBody.setTransform(position, 0);
    }

    public void render(SpriteBatch batch) {
        Texture wallTexture;
        Vector2 wallPos = wallBody.getPosition();

        if (wallPos.y < 0) {
            wallTexture = wall2;
        } else {
            wallTexture = wall1;
        }

        batch.draw(wallTexture, wallPos.x - (wallTexture.getWidth() / 2f), wallPos.y - (wallTexture.getHeight() / 2f));
    }

    public Rectangle getPositionRect() {
        return new Rectangle(wallBody.getPosition().x, wallBody.getPosition().y,
                wall1.getWidth(), wall1.getHeight());
    }

    public void dispose() {
        wall1.dispose();
        wall2.dispose();
    }
}
