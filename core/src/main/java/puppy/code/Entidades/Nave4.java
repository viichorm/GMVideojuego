package puppy.code.Entidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import puppy.code.Pantallas.PantallaJuego;

public class Nave4 {

    private boolean destruida = false;
    private boolean explotando = false;  // Controla si está en animación de explosión
    private int vidas = 3;
    private float xVel = 0, yVel = 0;
    private final float MAX_VELOCIDAD = 4f;
    private final float ACELERACION = 0.2f;
    private final float FRICTION = 0.98f;

    private Sprite spr;
    private Sound sonidoHerido, soundBala;
    private Texture txBala;
    private Texture textureNormal;
private Texture textureCritico;

// Variables de control
private boolean inmune = false;     // Si la nave está en estado inmune
private float tiempoInmunidad = 0;  // Temporizador para la inmunidad
private float duracionInmunidad = 2f;  // Duración de la inmunidad en segundos
private float tiempoParpadeo = 0;   // Control del tiempo entre parpadeos
private float intervaloParpadeo = 0.1f; // Intervalo de tiempo para el parpadeo
private boolean visible = true;     // Si la nave es visible o no



    private Animation<TextureRegion> explosionAnim;  // Animación de explosión
    private float explosionTime = 0f;  // Tiempo acumulado de la animación

    private float freezeTime = 0.2f;  // Tiempo en segundos para "freezar" la pantalla
    private float freezeTimer = 0f;   // Controlador del tiempo de congelación

    private boolean herido = false;
    private int tiempoHeridoMax = 50;
    private int tiempoHerido;

    public Nave4(int x, int y, Texture textureNormal, Texture textureCritico, Sound soundChoque, Texture txBala, Sound soundBala) {
    this.textureNormal = textureNormal;
    this.textureCritico = textureCritico;  // Textura para cuando la vida es baja
    this.sonidoHerido = soundChoque;
    this.soundBala = soundBala;
    this.txBala = txBala;
    this.spr = new Sprite(textureNormal);  // Usamos la textura normal inicialmente
    spr.setPosition(x, y);
    spr.setBounds(x, y, 45, 45);

    // Cargar animación de explosión
    Array<TextureRegion> frames = new Array<>();
    frames.add(new TextureRegion(new Texture("explosion0.png")));
    frames.add(new TextureRegion(new Texture("explosion1.png")));
    frames.add(new TextureRegion(new Texture("explosion2.png")));

    explosionAnim = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);
}


    public void draw(SpriteBatch batch, PantallaJuego juego) {
    float x = spr.getX();
    float y = spr.getY();

    if (explotando) {
        // Controlar el tiempo de "freeze"
        if (freezeTimer > 0) {
            freezeTimer -= Gdx.graphics.getDeltaTime();
            return;  // Pausar el dibujo de la nave durante el "freeze"
        }

        // Mostrar animación de explosión con velocidad reducida
        explosionTime += Gdx.graphics.getDeltaTime() * 0.5f;  // Ralentizar la explosión al 50%
        TextureRegion frame = explosionAnim.getKeyFrame(explosionTime);

        batch.draw(frame, x, y, spr.getWidth(), spr.getHeight());

        // Verificar si la animación ha terminado
        if (explosionAnim.isAnimationFinished(explosionTime)) {
            explotando = false;
            destruida = true;  // Marcar como destruida tras la explosión
        }
    } else if (!destruida) {
        // Si la nave tiene 1 vida, activar el estado crítico y la inmunidad
        if (vidas == 1 && !inmune) {
            spr.setTexture(textureCritico);
            inmune = true;
            tiempoInmunidad = duracionInmunidad; // Iniciar el tiempo de inmunidad
        }

        // Controlar el estado de inmunidad
        if (inmune) {
            tiempoInmunidad -= Gdx.graphics.getDeltaTime(); // Reducir el tiempo de inmunidad
            tiempoParpadeo -= Gdx.graphics.getDeltaTime(); // Reducir el tiempo del parpadeo

            // Alternar visibilidad de la nave para el parpadeo
            if (tiempoParpadeo <= 0) {
                visible = !visible; // Cambiar entre visible/invisible
                tiempoParpadeo = intervaloParpadeo; // Reiniciar el temporizador de parpadeo
            }

            if (tiempoInmunidad <= 0) {
                inmune = false;  // Termina la inmunidad
                visible = true;  // Asegurarse que la nave sea visible al final
            }
        }

        if (!herido) {
            // Movimiento suave con teclas
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) xVel -= ACELERACION;
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) xVel += ACELERACION;
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) yVel -= ACELERACION;
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) yVel += ACELERACION;

            // Aplicar fricción para desacelerar
            xVel *= FRICTION;
            yVel *= FRICTION;

            // Limitar velocidad
            xVel = MathUtils.clamp(xVel, -MAX_VELOCIDAD, MAX_VELOCIDAD);
            yVel = MathUtils.clamp(yVel, -MAX_VELOCIDAD, MAX_VELOCIDAD);

            // Mantener dentro de los límites de la pantalla
            if (x + xVel < 0 || x + xVel + spr.getWidth() > Gdx.graphics.getWidth()) xVel = 0;
            if (y + yVel < 0 || y + yVel + spr.getHeight() > Gdx.graphics.getHeight()) yVel = 0;

            // Actualizar posición
            spr.setPosition(x + xVel, y + yVel);

            // Dibujar sprite de la nave si es visible
            if (visible) {
                spr.draw(batch);
            }
        } else {
            // Movimiento errático mientras está herida
            spr.setX(spr.getX() + MathUtils.random(-2, 2));
            if (visible) {
                spr.draw(batch);
            }
            spr.setX(x);

            tiempoHerido--;
            if (tiempoHerido <= 0) herido = false;  // Dejar de estar herida
        }

        
    }
}


    public boolean checkCollision(Ball2 b) {
        if (!herido && b.getArea().overlaps(spr.getBoundingRectangle())) {
            // Rebote
            if (xVel == 0) xVel += b.getXSpeed() / 2;
            if (b.getXSpeed() == 0) b.setXSpeed(b.getXSpeed() + (int) xVel / 2);
            xVel = -xVel;
            b.setXSpeed(-b.getXSpeed());

            if (yVel == 0) yVel += b.getYSpeed() / 2; // Cambiado a getYSpeed()
            if (b.getYSpeed() == 0) b.setYSpeed(b.getYSpeed() + (int) yVel / 2); // Cambiado a getYSpeed() y setYSpeed()
            yVel = -yVel;
            b.setYSpeed(-b.getYSpeed()); // Cambiado a setYSpeed()

            herido = true;
            tiempoHerido = tiempoHeridoMax;
            sonidoHerido.play();
            if (vidas <= 0) destruida = true;
            return true;
        }
        return false;
    }
    
    
    
    
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

    public boolean estaDestruido() {
        return destruida && !explotando;
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

    public void reducirVida() {
    vidas--;
    herido = true;
    tiempoHerido = tiempoHeridoMax;

    // Cambiar a la textura de vida crítica si la vida es 1
    if (vidas == 0) {
        spr.setTexture(textureCritico);  // Cambiar la textura
    }
    }



    public void desintegrar() {
        freezeTimer = freezeTime;  // Iniciar el freeze
        explotando = true;
        explosionTime = 0f;  // Reiniciar la animación
    }

    public void incrementarVida() {
    vidas++;
    }
    
    public boolean estaExplotando() {
        return explotando;
    }
}