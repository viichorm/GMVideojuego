package puppy.code.Pantallas;

import puppy.code.Componentes.PantallaBase;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaMenu extends PantallaBase {

    private OrthographicCamera camera;
    private Music menuMusic;
    private Sound menuOptionSound;
    private String[] menuOptions = {"Jugar", "Opciones", "Salir"};
    private int selectedIndex = 0;

    public PantallaMenu(SpaceNavigation game) {
        super(game);
        
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1200, 800);

        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("menu-song.wav"));
        menuMusic.setLooping(true);
        menuMusic.setVolume(getVolumenGlobal());  // Aplicar el volumen global

        menuOptionSound = Gdx.audio.newSound(Gdx.files.internal("opcion-menu.wav"));
    }

    @Override
    public void show() {
        menuMusic.play();
    }

    @Override
    public void render(float delta) {
        // Renderizar utilizando el color de fondo compartido en PantallaBase
        super.render(delta); // Llama a render de PantallaBase para limpiar la pantalla con el color global

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        // Dibujar el título del menú
        font.draw(batch, "Void Odyssey", 500, 700);

        // Dibujar las opciones del menú
        for (int i = 0; i < menuOptions.length; i++) {
            if (i == selectedIndex) {
                font.getData().setScale(2.5f);  // Resaltar opción seleccionada
                font.draw(batch, "> " + menuOptions[i], 540, 500 - i * 60);
                font.getData().setScale(2f);  // Restaurar tamaño
            } else {
                font.draw(batch, menuOptions[i], 550, 500 - i * 60);
            }
        }
        batch.end();

        manejarInput();  // Procesar la entrada de usuario
    }

    protected void manejarInput() {
        // Navegar por las opciones del menú
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedIndex = (selectedIndex + 1) % menuOptions.length;
            menuOptionSound.play();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedIndex = (selectedIndex - 1 + menuOptions.length) % menuOptions.length;
            menuOptionSound.play();
        }

        // Seleccionar la opción con Enter
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            switch (selectedIndex) {
                case 0:
                    game.setScreen(new PantallaJuego(game, 1, 3, 0, 1, 1, 10));
                    dispose();
                    break;
                case 1:
                    game.setScreen(new PantallaOpciones(game));
                    dispose();
                    break;
                case 2:
                    Gdx.app.exit();
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
        // Dejar vacío si no es necesario
    }

    @Override
    public void resume() {
        // Dejar vacío si no es necesario
    }

    @Override
    public void hide() {
        menuMusic.stop(); // Detener la música al ocultar la pantalla
    }

    @Override
    public void dispose() {
        menuMusic.dispose();
        menuOptionSound.dispose();
        batch.dispose();
        font.dispose();
    }
}
