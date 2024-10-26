package puppy.code.utils;

import java.util.Random;
import com.badlogic.gdx.graphics.Texture;
import puppy.code.Entidades.Ball2;
import puppy.code.Pantallas.PantallaJuego;

public class FragmentUtils {

    private static final Random random = new Random();

    public static void generateFragments(Ball2 parentBall, int fragmentCount, PantallaJuego pantallaJuego, Texture fragmentTexture) {
        for (int i = 0; i < fragmentCount; i++) {
            int fragmentXSpeed = random.nextInt(4) - 2;
            int fragmentYSpeed = random.nextInt(4) - 2;

            Ball2 fragment = new Ball2(
                    (int) parentBall.getSprite().getX(), (int) parentBall.getSprite().getY(),
                    10, fragmentXSpeed, fragmentYSpeed,
                    fragmentTexture, false, 0, pantallaJuego
            );
            pantallaJuego.agregarFragmento(fragment);
        }
    }
}
