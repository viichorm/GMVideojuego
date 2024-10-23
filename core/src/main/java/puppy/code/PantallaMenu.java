package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaMenu implements Screen {

    private SpaceNavigation game;
    private OrthographicCamera camera;
    private Music menuMusic;
    private Sound menuOptionSound;
    private SpriteBatch batch;
    private BitmapFont font;

    private String[] menuOptions = {"Jugar", "Opciones", "Salir"};
    private int selectedIndex = 0;

    public PantallaMenu(SpaceNavigation game) {
        this.game = game;

        // Configurar la cámara
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1200, 800);

        // Cargar la música del menú y sonidos
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("menu-song.wav"));
        menuMusic.setLooping(true);  // Hacer que la música se repita

        menuOptionSound = Gdx.audio.newSound(Gdx.files.internal("opcion-menu.wav"));

        // Inicializar el batch y la fuente
        batch = new SpriteBatch();
        font = new BitmapFont();  // Usa la fuente predeterminada
        font.getData().setScale(2f);  // Aumentar el tamaño de la fuente
    }

    @Override
    public void show() {
        // Reproducir la música al mostrar la pantalla del menú
        menuMusic.play();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        // Dibujar el título del menú
        font.draw(batch, "Space Navigation", 500, 700);

        // Dibujar las opciones del menú
        for (int i = 0; i < menuOptions.length; i++) {
            if (i == selectedIndex) {
                // Resaltar la opción seleccionada cambiando el color o el tamaño
                font.getData().setScale(2.5f); // Aumentar tamaño de la opción seleccionada
                font.draw(batch, "> " + menuOptions[i], 540, 500 - i * 60);
                font.getData().setScale(2f); // Restaurar el tamaño original
            } else {
                font.draw(batch, menuOptions[i], 550, 500 - i * 60);
            }
        }
        batch.end();

        // Navegar por las opciones del menú con las flechas arriba y abajo
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedIndex++;
            if (selectedIndex >= menuOptions.length) {
                selectedIndex = 0;  // Volver al inicio si se pasa del último
            }
            menuOptionSound.play();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedIndex--;
            if (selectedIndex < 0) {
                selectedIndex = menuOptions.length - 1;  // Volver al final si se pasa del primero
            }
            menuOptionSound.play();
        }

        // Ejecutar la opción seleccionada con la tecla Enter
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            switch (selectedIndex) {
                case 0:  // Jugar
                    menuOptionSound.play();
                    Screen ss = new PantallaJuego(game, 1, 3, 0, 1, 1, 10);
                    ss.resize(1200, 800);
                    game.setScreen(ss);
                    dispose();
                    break;
                case 1:  // Opciones
                    menuOptionSound.play();
                    System.out.println("Se ha seleccionado Opciones");
                    // Aquí podrías cambiar a otra pantalla de opciones si lo implementas
                    break;
                case 2:  // Salir
                    menuOptionSound.play();
                    Gdx.app.exit();  // Cerrar el juego
                    break;
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void pause() {
        // No es necesario para esta implementación
    }

    @Override
    public void resume() {
        // No es necesario para esta implementación
    }

    @Override
    public void hide() {
        // Detener la música cuando se oculte la pantalla
        menuMusic.stop();
    }

    @Override
    public void dispose() {
        // Liberar los recursos cuando la pantalla se elimine
        menuMusic.dispose();
        menuOptionSound.dispose();
        batch.dispose();
        font.dispose();
    }
}
