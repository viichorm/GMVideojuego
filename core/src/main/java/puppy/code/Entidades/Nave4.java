package puppy.code.Entidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import puppy.code.Pantallas.PantallaJuego;

public class Nave4 {

    private boolean destruida = false;
    private int vidas = 3;
    private float xVel = 0;
    private float yVel = 0;
    private final float MAX_VELOCIDAD = 4f; // Límite de velocidad
    private final float ACELERACION = 0.2f; // Aceleración al presionar teclas
    private final float FRICTION = 0.98f; // Fricción para reducir velocidad
    private Sprite spr;
    private Sound sonidoHerido;
    private Sound soundBala;

    public boolean isDestruida() {
        return destruida;
    }

    public void setDestruida(boolean destruida) {
        this.destruida = destruida;
    }

    public float getxVel() {
        return xVel;
    }

    public void setxVel(float xVel) {
        this.xVel = xVel;
    }

    public float getyVel() {
        return yVel;
    }

    public void setyVel(float yVel) {
        this.yVel = yVel;
    }

    public Sprite getSpr() {
        return spr;
    }

    public void setSpr(Sprite spr) {
        this.spr = spr;
    }

    public Sound getSonidoHerido() {
        return sonidoHerido;
    }

    public void setSonidoHerido(Sound sonidoHerido) {
        this.sonidoHerido = sonidoHerido;
    }

    public Sound getSoundBala() {
        return soundBala;
    }

    public void setSoundBala(Sound soundBala) {
        this.soundBala = soundBala;
    }

    public Texture getTxBala() {
        return txBala;
    }

    public void setTxBala(Texture txBala) {
        this.txBala = txBala;
    }

    public boolean isHerido() {
        return herido;
    }

    public void setHerido(boolean herido) {
        this.herido = herido;
    }

    public int getTiempoHeridoMax() {
        return tiempoHeridoMax;
    }

    public void setTiempoHeridoMax(int tiempoHeridoMax) {
        this.tiempoHeridoMax = tiempoHeridoMax;
    }

    public int getTiempoHerido() {
        return tiempoHerido;
    }

    public void setTiempoHerido(int tiempoHerido) {
        this.tiempoHerido = tiempoHerido;
    }
    private Texture txBala;
    private boolean herido = false;
    private int tiempoHeridoMax = 50;
    private int tiempoHerido;

    public Nave4(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala) {
        sonidoHerido = soundChoque;
        this.soundBala = soundBala;
        this.txBala = txBala;
        spr = new Sprite(tx);
        spr.setPosition(x, y);
        spr.setBounds(x, y, 45, 45);
    }

    public void draw(SpriteBatch batch, PantallaJuego juego) {
        float x = spr.getX();
        float y = spr.getY();

        if (!herido) {
            // Movimiento suave con teclas
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) xVel -= ACELERACION;
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) xVel += ACELERACION;
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) yVel -= ACELERACION;
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) yVel += ACELERACION;

            // Aplicar fricción para desacelerar cuando no se presionen teclas
            xVel *= FRICTION;
            yVel *= FRICTION;

            // Limitar la velocidad
            xVel = MathUtils.clamp(xVel, -MAX_VELOCIDAD, MAX_VELOCIDAD);
            yVel = MathUtils.clamp(yVel, -MAX_VELOCIDAD, MAX_VELOCIDAD);

            // Mantener dentro de los bordes de la pantalla
            if (x + xVel < 0 || x + xVel + spr.getWidth() > Gdx.graphics.getWidth()) xVel = 0;
            if (y + yVel < 0 || y + yVel + spr.getHeight() > Gdx.graphics.getHeight()) yVel = 0;

            // Actualizar posición
            spr.setPosition(x + xVel, y + yVel);

            // Dibujar sprite
            spr.draw(batch);
        } else {
            spr.setX(spr.getX() + MathUtils.random(-2, 2));
            spr.draw(batch);
            spr.setX(x);
            tiempoHerido--;
            if (tiempoHerido <= 0) herido = false;
        }

        // Disparo
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            Bullet bala = new Bullet(spr.getX() + spr.getWidth() / 2 - 5, spr.getY() + spr.getHeight() - 5, 0, 3, txBala);
            juego.agregarBala(bala);
            soundBala.play();
        }
    }

    public boolean checkCollision(Ball2 b) {
        if (!herido && b.getArea().overlaps(spr.getBoundingRectangle())) {
            // Rebote
            if (xVel == 0) xVel += b.getXSpeed() / 2;
            if (b.getXSpeed() == 0) b.setXSpeed(b.getXSpeed() + (int) xVel / 2);
            xVel = -xVel;
            b.setXSpeed(-b.getXSpeed());

            if (yVel == 0) yVel += b.getySpeed() / 2;
            if (b.getySpeed() == 0) b.setySpeed(b.getySpeed() + (int) yVel / 2);
            yVel = -yVel;
            b.setySpeed(-b.getySpeed());

            // Actualizar vidas y herir
            vidas--;
            herido = true;
            tiempoHerido = tiempoHeridoMax;
            sonidoHerido.play();
            if (vidas <= 0)
                destruida = true;
            return true;
        }
        return false;
    }

    public boolean estaDestruido() {
        return !herido && destruida;
    }

    public boolean estaHerido() {
        return herido;
    }

    public int getVidas() {
        return vidas;
    }

    public int getX() {
        return (int) spr.getX();
    }

    public int getY() {
        return (int) spr.getY();
    }

    public void setVidas(int vidas2) {
        vidas = vidas2;
    }
}
