package puppy.code;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class EntidadMovil {
    protected Sprite spr;
    protected int xSpeed;
    protected int ySpeed;

    public EntidadMovil(int xSpeed, int ySpeed) {
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    // Métodos abstractos para que cada clase los implemente
    public abstract void update();
    public abstract void draw(SpriteBatch batch);
    public abstract boolean checkCollision(EntidadMovil other);

    // Métodos comunes que todas las entidades usarán
    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }
    
    public int getXSpeed() {
        return xSpeed;
    }
    
    public void setXSpeed(int xSpeed) {
        this.xSpeed = xSpeed;
    }

    public int getYSpeed() {
        return ySpeed;
    }
    
    public void setYSpeed(int ySpeed) {
        this.ySpeed = ySpeed;
    }
}
