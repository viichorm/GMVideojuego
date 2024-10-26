package puppy.code.Pantallas;

import java.util.ArrayList;
import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import puppy.code.Entidades.Ball2;
import puppy.code.Entidades.Bullet;
import puppy.code.Entidades.Nave4;

public class PantallaJuego implements Screen {

    private SpaceNavigation game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Sound explosionSound;
    private Music gameMusic;
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

        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav"));
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.5f);
        gameMusic.play();

        nave = new Nave4(Gdx.graphics.getWidth() / 2 - 50, 30, new Texture(Gdx.files.internal("MainShip3.png")),
                         Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")),
                         new Texture(Gdx.files.internal("Rocket2.png")),
                         Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3")));
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
                    isExplosive, fragmentCount, this); // Pasar 'this' como referencia a PantallaJuego
            balls1.add(bb);
        }
    }

    // Método para agregar fragmentos a la lista
    public void agregarFragmento(Ball2 fragment) {
        fragmentos.add(fragment);
    }

    // Dibujar encabezado
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
            // Actualizar balas y verificar colisiones
            for (int i = 0; i < balas.size(); i++) {
                Bullet b = balas.get(i);
                b.update();
                for (int j = 0; j < balls1.size(); j++) {
                    if (b.checkCollision(balls1.get(j))) {
                        explosionSound.play();
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

            // Actualizar y dibujar asteroides
            for (Ball2 ball : balls1) {
                ball.update();
                ball.draw(batch);
            }

            // Actualizar y dibujar fragmentos
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

            // Verificar colisiones entre asteroides
            for (int i = 0; i < balls1.size(); i++) {
                Ball2 ball1 = balls1.get(i);
                for (int j = i + 1; j < balls1.size(); j++) {
                    ball1.checkCollision(balls1.get(j));
                }
                for (int j = 0; j < fragmentos.size(); j++) {
                    ball1.checkCollision(fragmentos.get(j));
                }
            }

            // Verificar colisiones entre fragmentos
            for (int i = 0; i < fragmentos.size(); i++) {
                Ball2 frag1 = fragmentos.get(i);
                for (int j = i + 1; j < fragmentos.size(); j++) {
                    frag1.checkCollision(fragmentos.get(j));
                }
            }
        }

        // Dibujar balas
        for (Bullet b : balas) {
            b.draw(batch);
        }

        // Dibujar nave
        nave.draw(batch, this);

        // Verificar colisiones entre la nave y los asteroides
        for (int i = 0; i < balls1.size(); i++) {
            Ball2 b = balls1.get(i);
            if (nave.checkCollision(b)) {
                balls1.remove(i);
                i--;
            }
        }

        // Verificar colisiones entre la nave y los fragmentos
        for (int i = 0; i < fragmentos.size(); i++) {
            Ball2 frag = fragmentos.get(i);
            if (nave.checkCollision(frag)) {
                fragmentos.remove(i);
                i--;
            }
        }

        // Si la nave es destruida, mostrar pantalla de Game Over
        if (nave.estaDestruido()) {
            if (score > game.getHighScore())
                game.setHighScore(score);
            Screen ss = new PantallaGameOver(game);
            ss.resize(1200, 800);
            game.setScreen(ss);
            dispose();
        }

        batch.end();

        // Si no quedan asteroides ni fragmentos, avanzar de nivel
        if (balls1.isEmpty() && fragmentos.isEmpty()) {
            Screen ss = new PantallaJuego(game, ronda + 1, nave.getVidas(), score,
                    velXAsteroides + 3, velYAsteroides + 3, cantAsteroides + 10);
            ss.resize(1200, 800);
            game.setScreen(ss);
            dispose();
        }
    }

    // Método para verificar si un fragmento está fuera de la pantalla
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
        explosionSound.dispose();
        gameMusic.dispose();
    }
}