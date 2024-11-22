package puppy.code.Componentes;

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
        bullets.add(new Bullet(nave.getX() - 10, nave.getY(), -2, 10, nave.getTxBala())); // Bala hacia la izquierda
        bullets.add(new Bullet(nave.getX(), nave.getY(), 0, 10, nave.getTxBala()));       // Bala recta
        bullets.add(new Bullet(nave.getX() + 10, nave.getY(), 2, 10, nave.getTxBala()));  // Bala hacia la derecha
    }
}
