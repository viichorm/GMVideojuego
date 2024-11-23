package puppy.code.Componentes;

import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.ArrayList;
import puppy.code.Entidades.Bullet;
import puppy.code.Entidades.Nave4;
/**
 *
 * @author alfonso
 */
public class DisparoMultiple implements EstrategiaDisparo {
    
    @Override
public void shoot(Nave4 nave, ArrayList<Bullet> bullets) {
    // Obtener posición y dimensiones de la nave
    Sprite spr = nave.getSpr();
    float naveWidth = spr.getWidth();
    float naveHeight = spr.getHeight();
    float naveX = spr.getX();
    float naveY = spr.getY();

    // Calcular posición base para las balas
    float balaXCentral = naveX + naveWidth / 2 - nave.getTxBala().getWidth() / 2;
    float balaY = naveY + naveHeight; // Aparece arriba de la nave

    // Velocidades de las balas
    int velocidadY = 5;   // Velocidad vertical
    int velocidadX = 3;    // Velocidad horizontal para las balas laterales
    float desplazamientoLateral = 15; // Separación lateral

    // Crear las balas: izquierda, central y derecha
    bullets.add(new Bullet(balaXCentral - desplazamientoLateral, balaY, -velocidadX, velocidadY, nave.getTxBala())); // Izquierda
    bullets.add(new Bullet(balaXCentral, balaY, 0, velocidadY, nave.getTxBala()));                                   // Central
    bullets.add(new Bullet(balaXCentral + desplazamientoLateral, balaY, velocidadX, velocidadY, nave.getTxBala()));  // Derecha
}
}