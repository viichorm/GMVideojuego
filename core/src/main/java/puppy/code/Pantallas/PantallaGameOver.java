package puppy.code.Pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import puppy.code.Utils.SoundUtils;
import puppy.code.Utils.MusicUtils;

public class PantallaGameOver extends PantallaBase {
    private Music gameOverSound;
    private MusicUtils gestorMusica;  // Instancia de MusicManager    private GestorRecursos<Music> gestorSonido;
    	
    private float elapsedTime;
    private float restartCounter;  // Contador de tiempo hasta que se permita reiniciar
    private boolean showText;
    private boolean canRestart;  // Controla cuándo el usuario puede reiniciar
    private final float freezeTime = 5.0f;  // Tiempo de espera antes de permitir reiniciar

    public PantallaGameOver(SpaceNavigation game) {
        super(game);
        
        gestorMusica = MusicUtils.getInstancia();
        
        // Cargar y reproducir el sonido de Game Over
        gameOverSound = gestorMusica.cargar("game-over.mp3");
        gameOverSound.setLooping(false);

        // Configurar el volumen de acuerdo con el volumen global
        gameOverSound.setVolume(PantallaBase.getVolumenGlobal());

        gestorMusica.reproducir(gameOverSound);
        
        elapsedTime = 0;
        restartCounter = 0;
        canRestart = false;
    }

    @Override
    public void render(float delta) {
	    super.render(delta);  // Renderiza el fondo de PantallaBase
	
	    // Actualizar el tiempo transcurrido para el parpadeo del texto
	    elapsedTime += delta;
	    if (elapsedTime >= 0.5f) {
	        showText = !showText;
	        elapsedTime = 0;
	    }
	
	    // Inicia el contador de reinicio tras el freezeTime
	    if (!canRestart && restartCounter < freezeTime) {
	        restartCounter += delta;
	    } else {
	        canRestart = true;  // Permitir reiniciar después del freezeTime
	    }
	
	    game.getBatch().begin();
	    // Mostrar "Game Over !!!" con parpadeo
	    if (showText) {
	        game.getFont().draw(game.getBatch(), "GAME OVER", 120, 400, 400, 1, true);
	    }
	    // Mostrar el mensaje de reinicio y el tiempo restante
	    if (!canRestart) {
	        int segundosRestantes = (int) (freezeTime - restartCounter + 1);
	        game.getFont().draw(game.getBatch(), "Podrás reiniciar en " + segundosRestantes + " segundos", 100, 300);
	    } else {
	        game.getFont().draw(game.getBatch(), "Presiona cualquier tecla para reiniciar ...", 100, 300);
	    }
	    game.getBatch().end();
	
	    // Llamar a manejarInput para controlar la entrada del usuario
	    manejarInput();
    }

    @Override
    public void dispose() {
        // Liberar el sonido de Game Over al cambiar de pantalla
        if (gameOverSound != null) {
            gameOverSound.dispose();
        }
        super.dispose();
    }

    @Override
    protected void manejarInput() {
    // Verificar si se permite reiniciar el juego y si el usuario interactúa
	    if (canRestart && (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY))) {
	    	// Cambiar pantalla usando SpaceNavigation
	        game.cambiarAPantallaJuego(1, 3, 0, 1, 1, 10);
	        dispose();
	    }
    }

    @Override
    public void show() {
        // Método vacío; no se necesita lógica adicional al mostrar la pantalla
    }

    @Override
    public void resize(int width, int height) {
        // Método vacío; no es necesario redimensionar
    }

    @Override
    public void pause() {
        // Método vacío; no es necesario manejar pausa
    }

    @Override
    public void resume() {
        // Método vacío; no es necesario manejar reanudación
    }

    @Override
    public void hide() {
        // Método vacío; no es necesario manejar ocultación
    }
}
