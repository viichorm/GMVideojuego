package puppy.code.Pantallas;

import puppy.code.Componentes.PantallaBase;
import puppy.code.utils.SoundUtils;
import puppy.code.utils.MusicUtils;  // Importar la clase MusicUtils
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ScreenUtils;
import puppy.code.Componentes.GestorRecursos;

public class PantallaOpciones extends PantallaBase {
    // Opciones del menú, incluyendo la nueva opción de volumen
    private String[] opciones = {"Color Fondo: Azul", "Volumen: 50%", "Volver"};
    private float volumen = 0.5f;  // Volumen inicial al 50%
    
    private GestorRecursos<Sound> soundGestor;
    private GestorRecursos<Music> musicGestor;  // Cambiar a GestorRecursos<Music> para manejar música
    
    private int selectedIndex = 0;
    private float[] fondoColor = {0, 0, 0.2f};

    private Sound optionMoveSound;
    private Music backgroundMusic;
    private float[] colorActual;

    public PantallaOpciones(SpaceNavigation game) {
        super(game);
        soundGestor = new SoundUtils();
        musicGestor = new MusicUtils();

        optionMoveSound = soundGestor.cargar("opcion-menu.wav");
        backgroundMusic = musicGestor.cargar("ObservingTheStar.ogg");
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(volumen);
        musicGestor.reproducir(backgroundMusic);
    }

    @Override
    public void show() {
        // Reproducir la música de fondo al mostrar la pantalla de opciones
        musicGestor.reproducir(backgroundMusic);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(fondoColor[0], fondoColor[1], fondoColor[2], 1);

        super.render(delta);

        batch.begin();
        for (int i = 0; i < opciones.length; i++) {
            if (i == selectedIndex) {
                font.getData().setScale(2.5f);
                font.draw(batch, "> " + opciones[i], 540, 500 - i * 60);
                font.getData().setScale(2f);
            } else {
                font.draw(batch, opciones[i], 550, 500 - i * 60);
            }
        }
        batch.end();

        manejarInput();
    }

    protected void manejarInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedIndex = (selectedIndex + 1) % opciones.length;
            soundGestor.reproducir(optionMoveSound);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedIndex = (selectedIndex - 1 + opciones.length) % opciones.length;
            soundGestor.reproducir(optionMoveSound);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            soundGestor.reproducir(optionMoveSound);
            switch (selectedIndex) {
                case 0:
                    cambiarColorFondo();
                    break;
                case 1:
                    ajustarVolumen();  // Ajustar el volumen cuando se selecciona la opción
                    break;
                case 2:
                    game.setScreen(new PantallaMenu(game));
                    dispose();
                    break;
            }
        }
    }

    private int colorIndex = 0;
    private float[][] colores = {
        {0, 0, 0.2f},
        {0.5f, 0.1f, 0.6f},
        {0, 0.5f, 0.2f},
        {0.2f, 0.2f, 0.5f}
    };

    private void cambiarColorFondo() {
        colorIndex = (colorIndex + 1) % colores.length;
        PantallaBase.setFondoColor(colores[colorIndex]);
        actualizarTextoColor();
    }

    private void actualizarTextoColor() {
        String nombreColor;
        switch (colorIndex) {
            case 0: nombreColor = "Azul Oscuro"; break;
            case 1: nombreColor = "Morado"; break;
            case 2: nombreColor = "Verde Oscuro"; break;
            case 3: nombreColor = "Gris Azulado"; break;
            default: nombreColor = "Desconocido"; break;
        }
        opciones[0] = "Color Fondo: " + nombreColor;
    }

    private void ajustarVolumen() {
        volumen += 0.1f;
        if (volumen > 1f) volumen = 0f;

        setVolumenGlobal(volumen);  // Actualizar el volumen global
        backgroundMusic.setVolume(volumen);  // Aplicar el volumen a la música actual
        opciones[1] = "Volumen: " + (int)(volumen * 100) + "%";  // Actualizar el texto
    }

    @Override
    public void dispose() {
        super.dispose();
        soundGestor.liberar(optionMoveSound);
        musicGestor.liberar(backgroundMusic);
    }


    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}
}
