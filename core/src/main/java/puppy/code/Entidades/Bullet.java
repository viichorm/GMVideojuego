package puppy.code.Entidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import puppy.code.Componentes.Destruible;
import puppy.code.Componentes.EntidadMovil;

public class Bullet extends EntidadMovil implements Destruible {
    private boolean destroyed = false;

    public Bullet(float x, float y, int xSpeed, int ySpeed, Texture tx) {
        super(xSpeed, ySpeed);
        this.spr = new Sprite(tx);
        this.spr.setPosition(x, y);
    }

    @Override
    public void update() {
        if (!destroyed) {
            spr.setPosition(spr.getX() + xSpeed, spr.getY() + ySpeed);
            if (isOutOfScreen()) {
                destruir();
            }
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

    private boolean isOutOfScreen() {
        return (spr.getX() < 0 || spr.getX() + spr.getWidth() > Gdx.graphics.getWidth() ||
                spr.getY() < 0 || spr.getY() + spr.getHeight() > Gdx.graphics.getHeight());
    }

    // Métodos adicionales para acceder a las propiedades del sprite

    public Sprite getSprite() {
        return spr;
    }

    public float getWidth() {
        return spr.getWidth();
    }

    public float getHeight() {
        return spr.getHeight();
    }

    public float getX() {
        return spr.getX();
    }

    public float getY() {
        return spr.getY();
    }
}
