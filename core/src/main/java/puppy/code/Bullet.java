package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bullet extends EntidadMovil implements Destruible {
    private boolean destroyed = false;

    public Bullet(float x, float y, int xSpeed, int ySpeed, Texture tx) {
        super(xSpeed, ySpeed);
        spr = new Sprite(tx);
        spr.setPosition(x, y);
    }

    @Override
    public void update() {
        spr.setPosition(spr.getX() + xSpeed, spr.getY() + ySpeed);
        if (spr.getX() < 0 || spr.getX() + spr.getWidth() > Gdx.graphics.getWidth() || 
            spr.getY() < 0 || spr.getY() + spr.getHeight() > Gdx.graphics.getHeight()) {
            destruir();
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (!destroyed) {
            spr.draw(batch);
        }
    }

    @Override
    public boolean checkCollision(EntidadMovil other) {
        if (spr.getBoundingRectangle().overlaps(other.getArea())) {
            destruir();
            return true;
        }
        return false;
    }

    @Override
    public boolean estaDestruido() {
        return destroyed;
    }

    @Override
    public void destruir() {
        destroyed = true;
    }
}