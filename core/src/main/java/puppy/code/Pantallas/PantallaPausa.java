package puppy.code.Pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import puppy.code.Componentes.PantallaBase;
import puppy.code.Utils.SoundUtils;
import puppy.code.Utils.MusicUtils;

public class PantallaPausa extends PantallaBase {

    private float elapsedTime;
    private boolean showText;
    private PantallaJuego pantallaJuego;
    
    private SoundUtils soundManager;
    private MusicUtils musicManager;
    private Sound sonidoPausa;
    private Music musicaPausa;
    private Sound optionMoveSound; // Nuevo sonido para mover opciones del menú

    private int opcionSeleccionada = 0; // Índice para el menú

    private final String[] opciones = {"Reanudar", "Salir"};

    public PantallaPausa(SpaceNavigation game, PantallaJuego pantallaJuego) {
        super(game);
        this.pantallaJuego = pantallaJuego;
        this.elapsedTime = 0;
        this.showText = true;

        // Inicialización de los gestores de recursos
        soundManager = SoundUtils.getInstancia();
        musicManager = MusicUtils.getInstancia();

        // Cargar el sonido y la música para la pausa
        sonidoPausa = soundManager.cargar("SonidoPausa.wav");
        musicaPausa = musicManager.cargar("Pausa.ogg");
        musicaPausa.setLooping(true); // Repetir la música de fondo en bucle

        // Cargar sonido para el movimiento del menú
        optionMoveSound = soundManager.cargar("opcion-menu.wav");
    }

    @Override
    public void show() {
        // Reproduce el sonido de pausa y comienza la música de fondo
        soundManager.reproducir(sonidoPausa);
        musicManager.reproducir(musicaPausa);
    }

    @Override
    public void render(float delta) {
        super.render(delta);  // Renderiza el fondo de PantallaBase
        
        elapsedTime += delta;
        if (elapsedTime >= 0.5f) {
            showText = !showText;
            elapsedTime = 0;
        }

        batch.begin();
        if (showText) {
            font.draw(batch, "Juego en Pausa", Gdx.graphics.getWidth() / 2f - 100, Gdx.graphics.getHeight() / 2f + 50);
        }
        // Mostrar opciones del menú
        for (int i = 0; i < opciones.length; i++) {
            if (i == opcionSeleccionada) {
                font.draw(batch, "> " + opciones[i], Gdx.graphics.getWidth() / 2f - 100, Gdx.graphics.getHeight() / 2f - i * 30);
            } else {
                font.draw(batch, opciones[i], Gdx.graphics.getWidth() / 2f - 100, Gdx.graphics.getHeight() / 2f - i * 30);
            }
        }
        batch.end();

        manejarInput();
    }

    @Override
    protected void manejarInput() {
        // Navegación del menú
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            opcionSeleccionada = (opcionSeleccionada - 1 + opciones.length) % opciones.length;
            soundManager.reproducir(optionMoveSound); // Reproduce el sonido al mover hacia arriba
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            opcionSeleccionada = (opcionSeleccionada + 1) % opciones.length;
            soundManager.reproducir(optionMoveSound); // Reproduce el sonido al mover hacia abajo
        }

        // Selección de la opción con ENTER
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            switch (opcionSeleccionada) {
                case 0: // Reanudar
                    musicManager.detener(musicaPausa);
                    game.setScreen(pantallaJuego);
                    break;
                case 1: // Salir
                    Gdx.app.exit(); // Salir del juego
                    break;
            }
        }

        // Opción para salir de la pausa con ESCAPE también
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            musicManager.detener(musicaPausa);
            game.setScreen(pantallaJuego);
        }
    }

    @Override
    public void dispose() {
        // Liberar recursos al salir de la pantalla de pausa
        soundManager.liberar(sonidoPausa);
        soundManager.liberar(optionMoveSound); // Liberar el sonido del menú
        musicManager.liberar(musicaPausa);
        super.dispose();
    }

    @Override
    public void hide() {
        // Detiene la música al ocultar la pantalla de pausa
        musicManager.detener(musicaPausa);
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}
}
