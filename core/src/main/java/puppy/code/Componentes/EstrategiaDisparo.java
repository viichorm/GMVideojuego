package puppy.code.Componentes;

import puppy.code.Entidades.Nave4;
import puppy.code.Entidades.Bullet;
import java.util.ArrayList;
/**
 *
 * @author alfonso
 */

public interface EstrategiaDisparo {
    void shoot(Nave4 nave, ArrayList<Bullet> bullets);
}
