package puppy.code.Componentes;

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

    // Método plantilla que define la estructura general del ciclo
    public final void runCycle(SpriteBatch batch, EntidadMovil other) {
        update();                // Actualizar estado
        draw(batch);             // Dibujar la entidad
        if (checkCollision(other)) {
            handleCollision(other); // Manejar la colisión (puede ser personalizado)
        }
    }

    // Métodos abstractos a implementar por las subclases
    protected abstract void update();
    protected abstract void draw(SpriteBatch batch);

    // Método con implementación predeterminada para la colisión
    protected boolean checkCollision(EntidadMovil other) {
        return getArea().overlaps(other.getArea());
    }

    // Método gancho para manejar colisiones (puede sobrescribirse)
    protected void handleCollision(EntidadMovil other) {
        // Implementación predeterminada vacía
    }

    // Método común para obtener el área del sprite
    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }

    // Métodos de acceso y modificación de velocidad
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
