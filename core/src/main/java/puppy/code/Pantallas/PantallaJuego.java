package puppy.code.Pantallas;

import java.util.ArrayList;
import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import puppy.code.Componentes.DisparoMultiple;
import puppy.code.Componentes.DisparoUnico;
import puppy.code.Componentes.EstrategiaDisparo;
import puppy.code.Entidades.Ball2;
import puppy.code.Entidades.Bullet;
import puppy.code.Entidades.Nave4;
import puppy.code.Utils.SoundUtils;
import puppy.code.Utils.MusicUtils;

public class PantallaJuego extends PantallaBase {

    private Sound explosionSound;
    private Music gameMusic;
    private Sound deadSound;
    private SoundUtils soundManager;
    private MusicUtils musicManager;

    private int score;
    private int ronda;
    private int velXAsteroides;
    private int velYAsteroides;
    private int cantAsteroides;
    private EstrategiaDisparo estrategiaDisparo;
    int highScore = cargarHighScore();  // Cargar el HighScore al inicio
    
    private Nave4 nave;
    private ArrayList<Ball2> balls1 = new ArrayList<>();
    private ArrayList<Ball2> fragmentos = new ArrayList<>();
    private ArrayList<Bullet> balas = new ArrayList<>();

    public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int score,
                     int velXAsteroides, int velYAsteroides, int cantAsteroides) {
    super(game);  // Llamada al constructor de PantallaBase

    this.ronda = ronda;
    this.score = score;  // Inicializar el puntaje con el valor de entrada
    if (this.score > highScore) {
        highScore = this.score;  // Actualizar el HighScore en memoria
        guardarHighScore(highScore);  // Guardarlo en las preferencias
    }
    this.velXAsteroides = velXAsteroides;
    this.velYAsteroides = velYAsteroides;
    this.cantAsteroides = cantAsteroides;

    soundManager = SoundUtils.getInstancia();
    musicManager = MusicUtils.getInstancia();

    // Cargar sonidos y música con los gestores
    explosionSound = soundManager.cargar("explosion.wav");
    deadSound = soundManager.cargar("dead.wav");
    gameMusic = musicManager.cargar("piano-loops.mp3");
    gameMusic.setLooping(true);

    // Usar el volumen global de PantallaBase
    gameMusic.setVolume(getVolumenGlobal());
    gameMusic.play();

    // Inicializar la estrategia de disparo según la ronda
    if (ronda >= 2) {
        estrategiaDisparo = new DisparoMultiple();  // Disparo múltiple a partir de la ronda 3
    } else {
        estrategiaDisparo = new DisparoUnico();  // Disparo único para las primeras 2 rondas
    }

    // Crear la nave
    nave = new Nave4(Gdx.graphics.getWidth() / 2 - 50, 30,
             new Texture(Gdx.files.internal("SpaceShipNormal.png")),  // Textura normal
             new Texture(Gdx.files.internal("SpaceshipLowHP.png")),   // Textura crítica
             soundManager.cargar("hurt.ogg"),
             new Texture(Gdx.files.internal("Rocket2.png")),
             soundManager.cargar("pop-sound.mp3"));
    nave.setVidas(vidas);

    // Crear asteroides
    Random r = new Random();
    for (int i = 0; i < cantAsteroides; i++) {
        boolean isExplosive = r.nextBoolean();
        int fragmentCount = isExplosive ? 3 : 0;
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
    
    // Método público para agregar fragmentos
    public void agregarFragmento(Ball2 fragment) {
        fragmentos.add(fragment);
    }

    @Override
    public void render(float delta) {
        super.render(delta); // Renderiza el fondo de PantallaBase

        // Detecta la tecla ESC para pausar el juego
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            gameMusic.pause();
            game.setScreen(new PantallaPausa(game, this)); // Cambia a la pantalla de pausa
            return;
        }

        batch.begin();
        dibujaEncabezado();

        if (!nave.estaHerido()) {
            actualizarBalas();
            actualizarAsteroidesYFragmentos();
            verificarColisionesNave();
            manejarInput();  // Llama a la lógica de disparo
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
        b.draw(batch);

        for (int j = 0; j < balls1.size(); j++) {
            if (b.checkCollision(balls1.get(j))) {
                soundManager.reproducir(explosionSound);
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
	        soundManager.reproducir(deadSound);
	        nave.desintegrar();
	
	        // Verificar y guardar el high score si es superado al final del juego
	        if (score > highScore) {
	            highScore = score;
	            guardarHighScore(highScore);  // Guardar el high score si es necesario
	        }
	        
	        game.cambiarAPantallaGameOver();
	        dispose();
	    }
    }

    private void verificarAvanceNivel() {
        if (balls1.isEmpty()) {
            nave.incrementarVida();
            // Usar metodo para cambiar a una nueva PantallaJuego
            game.cambiarAPantallaJuego(ronda + 1, nave.getVidas(), score,
                    velXAsteroides + 3, velYAsteroides + 3, cantAsteroides + 10);
            dispose();
        }
    }

    private boolean fueraDePantalla(Ball2 ball) {
        return ball.getSprite().getX() < 0 || ball.getSprite().getX() > Gdx.graphics.getWidth()
                || ball.getSprite().getY() < 0 || ball.getSprite().getY() > Gdx.graphics.getHeight();
    }
    
    public void guardarHighScore(int highScore) {
    Preferences prefs = Gdx.app.getPreferences("MyGamePreferences");  // Nombre del archivo de preferencias
    prefs.putInteger("highScore", highScore);  // Guardar el valor del HighScore
    prefs.flush();  // Guardar los cambios
}
    public int cargarHighScore() {
    Preferences prefs = Gdx.app.getPreferences("MyGamePreferences");
    return prefs.getInteger("highScore", 0);  // Leer el HighScore, si no existe, devuelve 0
}


    
    public void dibujaEncabezado() {
    CharSequence str = "Vidas: " + nave.getVidas() + " Ronda: " + ronda;
    game.getFont().getData().setScale(2f);

    float alturaPantalla = Gdx.graphics.getHeight();
    float posicionY = alturaPantalla - 10;

    game.getFont().draw(batch, str, 10, 30);  // Vidas y ronda a la izquierda
    game.getFont().draw(batch, "Score:" + this.score, Gdx.graphics.getWidth() - 150, 30);  // Score a la derecha
    game.getFont().draw(batch, "HighScore:" + highScore, Gdx.graphics.getWidth() / 2 - 100, 30);  // HighScore al centro
}



    public boolean agregarBala(Bullet bb) {
        return balas.add(bb);
    }

    @Override
    protected void manejarInput() {
        if  (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            // Utiliza la estrategia de disparo correspondiente
            estrategiaDisparo.shoot(nave, balas);
        }
    }

    @Override
    public void dispose() {
        soundManager.liberar(explosionSound);
        soundManager.liberar(deadSound);
        musicManager.liberar(gameMusic);
        super.dispose();
    }

    @Override
    public void show() {
        gameMusic.play();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
    }
}
