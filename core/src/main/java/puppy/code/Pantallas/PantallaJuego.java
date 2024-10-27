package puppy.code.Pantallas;

import java.util.ArrayList;
import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import puppy.code.Entidades.Ball2;
import puppy.code.Entidades.Bullet;
import puppy.code.Entidades.Nave4;
import puppy.code.utils.SoundUtils; // Importar SoundUtils
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class PantallaJuego implements Screen {

    private SpaceNavigation game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Sound explosionSound;
    private Music gameMusic;
    private Sound deadSound;

    private int score;
    private int ronda;
    private int velXAsteroides;
    private int velYAsteroides;
    private int cantAsteroides;

    private Nave4 nave;
    private ArrayList<Ball2> balls1 = new ArrayList<>();
    private ArrayList<Ball2> fragmentos = new ArrayList<>();
    private ArrayList<Bullet> balas = new ArrayList<>();

    public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int score,
                         int velXAsteroides, int velYAsteroides, int cantAsteroides) {
        this.game = game;
        this.ronda = ronda;
        this.score = score;
        this.velXAsteroides = velXAsteroides;
        this.velYAsteroides = velYAsteroides;
        this.cantAsteroides = cantAsteroides;

        batch = game.getBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 640);

        // Cargar sonidos usando SoundUtils
        explosionSound = SoundUtils.loadSound("explosion.ogg");
        deadSound = SoundUtils.loadSound("dead.wav");
        gameMusic = SoundUtils.loadMusic("piano-loops.wav", true, 0.5f);

        gameMusic.play();

        // Crear la nave
        nave = new Nave4(Gdx.graphics.getWidth() / 2 - 50, 30,
                         new Texture(Gdx.files.internal("MainShip3.png")),
                         SoundUtils.loadSound("hurt.ogg"),
                         new Texture(Gdx.files.internal("Rocket2.png")),
                         SoundUtils.loadSound("pop-sound.mp3"));
        nave.setVidas(vidas);

        // Crear asteroides
        Random r = new Random();
        for (int i = 0; i < cantAsteroides; i++) {
            boolean isExplosive = r.nextBoolean();
            int fragmentCount = isExplosive ? 3 : 0; // Solo asteroides explosivos generan fragmentos
            Ball2 bb = new Ball2(r.nextInt(Gdx.graphics.getWidth()),
                    50 + r.nextInt(Gdx.graphics.getHeight() - 50),
                    20 + r.nextInt(10),
                    velXAsteroides + r.nextInt(4),
                    velYAsteroides + r.nextInt(4),
                    new Texture(Gdx.files.internal("aGreyMedium4.png")),
                    isExplosive, fragmentCount, this);
            balls1.add(bb);
        }
    }

    public void agregarFragmento(Ball2 fragment) {
        fragmentos.add(fragment);
    }

    public void dibujaEncabezado() {
        CharSequence str = "Vidas: " + nave.getVidas() + " Ronda: " + ronda;
        game.getFont().getData().setScale(2f);
        game.getFont().draw(batch, str, 10, 30);
        game.getFont().draw(batch, "Score:" + this.score, Gdx.graphics.getWidth() - 150, 30);
        game.getFont().draw(batch, "HighScore:" + game.getHighScore(), Gdx.graphics.getWidth() / 2 - 100, 30);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        dibujaEncabezado();

        if (!nave.estaHerido()) {
            actualizarBalas();
            actualizarAsteroidesYFragmentos();
            verificarColisionesNave();
        }

        nave.draw(batch, this);
        batch.end();

        verificarGameOver();
        verificarAvanceNivel();
    }

    private void actualizarBalas() {
        for (int i = 0; i < balas.size(); i++) {
            Bullet b = balas.get(i);
            b.update();
            for (int j = 0; j < balls1.size(); j++) {
                if (b.checkCollision(balls1.get(j))) {
                    SoundUtils.playSound(explosionSound); // Usar SoundUtils para reproducir sonido
                    balls1.get(j).explode();
                    balls1.remove(j);
                    j--;
                    score += 10;
                }
            }
            if (b.estaDestruido()) {
                balas.remove(i);
                i--;
            }
        }
    }

    private void actualizarAsteroidesYFragmentos() {
        for (Ball2 ball : balls1) {
            ball.update();
            ball.draw(batch);
        }

        for (int i = 0; i < fragmentos.size(); i++) {
            Ball2 fragment = fragmentos.get(i);
            fragment.update();
            if (fueraDePantalla(fragment)) {
                fragmentos.remove(i);
                i--;
            } else {
                fragment.draw(batch);
            }
        }
    }

    private void verificarColisionesNave() {
        for (int i = 0; i < balls1.size(); i++) {
            Ball2 b = balls1.get(i);
            if (nave.checkCollision(b)) {
                balls1.remove(i);
                i--;
                nave.reducirVida();
            }
        }

        for (int i = 0; i < fragmentos.size(); i++) {
            Ball2 frag = fragmentos.get(i);
            if (nave.checkCollision(frag)) {
                fragmentos.remove(i);
                i--;
                nave.reducirVida();
            }
        }
    }

    private void verificarGameOver() {
        if (nave.estaDestruido()) {
            SoundUtils.playSound(deadSound); // Reproducir sonido de muerte usando SoundUtils
            nave.desintegrar(); // Efecto de desintegración

            if (score > game.getHighScore()) {
                game.setHighScore(score);
            }
            game.setScreen(new PantallaGameOver(game));
            dispose();
        }
    }

    private void verificarAvanceNivel() {
        if (balls1.isEmpty()) {
            nave.incrementarVida(); // Aumentar vida al avanzar de nivel
            game.setScreen(new PantallaJuego(game, ronda + 1, nave.getVidas(), score,
                    velXAsteroides + 3, velYAsteroides + 3, cantAsteroides + 10));
            dispose();
        }
    }

    private boolean fueraDePantalla(Ball2 ball) {
        return ball.getSprite().getX() < 0 || ball.getSprite().getX() > Gdx.graphics.getWidth()
                || ball.getSprite().getY() < 0 || ball.getSprite().getY() > Gdx.graphics.getHeight();
    }

    public boolean agregarBala(Bullet bb) {
        return balas.add(bb);
    }

    @Override
    public void show() {
        gameMusic.play();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        SoundUtils.dispose(explosionSound); // Usar SoundUtils para liberar recursos
        SoundUtils.dispose(deadSound);
        SoundUtils.dispose(gameMusic);
    }
}
