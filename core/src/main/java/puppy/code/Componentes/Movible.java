package puppy.code.Componentes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Movible {
    void update();
    void draw(SpriteBatch batch);
    boolean checkCollision(Movible other);
}
