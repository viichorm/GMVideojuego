package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.Random;

public class Ball2 extends EntidadMovil {

    private PantallaJuego pantallaJuego;
    private static final Random random = new Random();
    private boolean isExplosive;
    private int fragmentCount;
    private static Texture fragmentTexture;

    public Ball2(int x, int y, int size, int xSpeed, int ySpeed, Texture tx, boolean isExplosive, 
                 int fragmentCount, PantallaJuego pantallaJuego) {
        super(xSpeed, ySpeed);
        this.spr = new Sprite(tx);
        this.isExplosive = isExplosive;
        this.fragmentCount = fragmentCount;
        this.pantallaJuego = pantallaJuego;

        if (fragmentTexture == null) {
            fragmentTexture = new Texture(Gdx.files.internal("fragment.png"));
        }

        this.spr.setPosition(x, y);
        this.spr.setSize(size, size); // Ajustar tamaño del sprite
    }

    @Override
    public void update() {
        // Actualizar la posición del sprite
        spr.setPosition(spr.getX() + xSpeed, spr.getY() + ySpeed);

        // Rebote en los bordes de la pantalla
        if (spr.getX() < 0) {
            spr.setX(0); // Asegura que no se salga del borde izquierdo
            xSpeed = -xSpeed; // Cambia la dirección horizontal
        } else if (spr.getX() + spr.getWidth() > Gdx.graphics.getWidth()) {
            spr.setX(Gdx.graphics.getWidth() - spr.getWidth()); // Asegura que no se salga del borde derecho
            xSpeed = -xSpeed; // Cambia la dirección horizontal
        }

        if (spr.getY() < 0) {
            spr.setY(0); // Asegura que no se salga del borde inferior
            ySpeed = -ySpeed; // Cambia la dirección vertical
        } else if (spr.getY() + spr.getHeight() > Gdx.graphics.getHeight()) {
            spr.setY(Gdx.graphics.getHeight() - spr.getHeight()); // Asegura que no se salga del borde superior
            ySpeed = -ySpeed; // Cambia la dirección vertical
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    @Override
    public boolean checkCollision(EntidadMovil other) {
        if (spr.getBoundingRectangle().overlaps(other.getArea())) {
            if (isExplosive && fragmentCount > 0) {
                explode(); // Evita que fragmentos exploten de nuevo
            }
            xSpeed = -xSpeed;
            ySpeed = -ySpeed;
            return true;
        }
        return false;
    }

    public void explode() {
        if (!isExplosive || fragmentCount == 0) return; // No explota si es fragmento o ya explotó

        // Generar los fragmentos en direcciones aleatorias
        for (int i = 0; i < fragmentCount; i++) {
            int fragmentXSpeed = random.nextInt(4) - 2;
            int fragmentYSpeed = random.nextInt(4) - 2;

            Ball2 fragment = new Ball2(
                    (int) spr.getX(), (int) spr.getY(), 
                    10, fragmentXSpeed, fragmentYSpeed, 
                    fragmentTexture, false, 0, pantallaJuego
            );
            pantallaJuego.agregarFragmento(fragment);
        }
        isExplosive = false; // Evita que el asteroide explote de nuevo
    }

    // Método getter para isExplosive
    public boolean isExplosive() {
        return isExplosive;
    }

    public Sprite getSprite() {
        return this.spr;
    }

    int getySpeed() {
        return this.ySpeed;
    }

    void setySpeed(int i) {
        this.ySpeed = i;
    }
}
