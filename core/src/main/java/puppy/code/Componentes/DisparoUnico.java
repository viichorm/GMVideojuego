package puppy.code.Componentes;

import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.ArrayList;
import puppy.code.Entidades.Bullet;
import puppy.code.Entidades.Nave4;



/**
 *
 * @author alfonso
 */
public class DisparoUnico implements EstrategiaDisparo {
    
    @Override
public void shoot(Nave4 nave, ArrayList<Bullet> bullets) {
    // Obtener posición y dimensiones de la nave
    Sprite spr = nave.getSpr();
    float naveWidth = spr.getWidth();
    float naveHeight = spr.getHeight();
    float naveX = spr.getX();
    float naveY = spr.getY();

    // Calcular posición centrada para la bala
    float balaX = naveX + naveWidth / 2 - nave.getTxBala().getWidth() / 2;
    float balaY = naveY + naveHeight; // Aparece arriba de la nave

    // Crear la bala
    int velocidadY = 5; // Velocidad vertical
    bullets.add(new Bullet(balaX, balaY, 0, velocidadY, nave.getTxBala()));
}
}
