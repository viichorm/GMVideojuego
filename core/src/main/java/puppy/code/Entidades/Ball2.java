package puppy.code.Entidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.Random;
import puppy.code.Componentes.EntidadMovil;
import puppy.code.Pantallas.PantallaJuego;

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
        if (spr.getX() < 0 || spr.getX() + spr.getWidth() > Gdx.graphics.getWidth()) {
            xSpeed = -xSpeed;
            spr.setX(Math.max(0, Math.min(spr.getX(), Gdx.graphics.getWidth() - spr.getWidth())));
        }

        if (spr.getY() < 0 || spr.getY() + spr.getHeight() > Gdx.graphics.getHeight()) {
            ySpeed = -ySpeed;
            spr.setY(Math.max(0, Math.min(spr.getY(), Gdx.graphics.getHeight() - spr.getHeight())));
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    @Override
    protected void handleCollision(EntidadMovil other) {
        // Rebotar y gestionar la explosión
        xSpeed = -xSpeed;
        ySpeed = -ySpeed;

        if (isExplosive && fragmentCount > 0) {
            explode();
        }
    }

    public void explode() {
        if (!isExplosive || fragmentCount == 0) return;

        // Generar fragmentos en direcciones aleatorias
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
        isExplosive = false; // Evitar futuras explosiones
    }

    // Método getter para isExplosive
    public boolean isExplosive() {
        return isExplosive;
    }

    public Sprite getSprite() {
        return this.spr;
    }
}
