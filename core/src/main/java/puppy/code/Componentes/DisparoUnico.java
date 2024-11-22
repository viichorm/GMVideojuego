package puppy.code.Componentes;

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
        bullets.add(new Bullet(nave.getX(), nave.getY(), 0, 10,nave.getTxBala())); // Dispara una bala recta hacia arriba
    }
}
