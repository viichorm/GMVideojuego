package puppy.code.Pantallas;

import puppy.code.Componentes.PantallaBase;
import puppy.code.utils.SoundUtils;  // Importar la clase SoundUtils
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaOpciones extends PantallaBase {

    private String[] opciones = {"Color Fondo: Azul", "Volver"};
    private int selectedIndex = 0;
    private float[] fondoColor = {0, 0, 0.2f};  // Color inicial del fondo (azul oscuro)

    private Sound optionMoveSound;  // Sonido para moverse entre opciones y seleccionar
    private Music backgroundMusic;  // Música de fondo

    public PantallaOpciones(SpaceNavigation game) {
        super(game);

        // Cargar los sonidos
        optionMoveSound = SoundUtils.loadSound("opcion-menu.wav");  // Sonido para moverse/seleccionar
        backgroundMusic = SoundUtils.loadMusic("menu-song.wav", true, 0.5f);  // Música de fondo, en bucle, con volumen ajustado
    }

    @Override
    public void show() {
        // Reproducir la música de fondo al mostrar la pantalla de opciones
        backgroundMusic.play();
    }

    @Override
    public void render(float delta) {
        // Establecer el color del fondo dinámicamente
        ScreenUtils.clear(fondoColor[0], fondoColor[1], fondoColor[2], 1);

        super.render(delta);

        batch.begin();
        // Dibujar las opciones del menú de opciones
        for (int i = 0; i < opciones.length; i++) {
            if (i == selectedIndex) {
                font.getData().setScale(2.5f);  // Resaltar opción seleccionada
                font.draw(batch, "> " + opciones[i], 540, 500 - i * 60);
                font.getData().setScale(2f);  // Restaurar tamaño
            } else {
                font.draw(batch, opciones[i], 550, 500 - i * 60);
            }
        }
        batch.end();

        manejarInput();  // Manejar entrada de usuario
    }

    @Override
protected void manejarInput() {
    // Navegar por las opciones del menú
    if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
        selectedIndex++;
        if (selectedIndex >= opciones.length) {
            selectedIndex = 0;
        }
        SoundUtils.playSound(optionMoveSound);  // Reproducir sonido al moverse
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
        selectedIndex--;
        if (selectedIndex < 0) {
            selectedIndex = opciones.length - 1;
        }
        SoundUtils.playSound(optionMoveSound);  // Reproducir sonido al moverse
    }

    // Seleccionar la opción con Enter
    if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
        SoundUtils.playSound(optionMoveSound);  // Reproducir sonido de selección (mismo sonido)
        switch (selectedIndex) {
            case 0:
                cambiarColorFondo();  // Cambiar color de fondo y actualizar texto
                break;
            case 1:
                game.setScreen(new PantallaMenu(game));  // Volver al menú
                dispose();
                break;
        }
    }
}


    private int colorIndex = 0;  // Índice del color actual
private float[][] colores = {  // Lista de colores
    {0, 0, 0.2f},   // Azul oscuro
    {0.5f, 0.1f, 0.6f},  // Morado
    {0, 0.5f, 0.2f},   // Verde oscuro
    {0.2f, 0.2f, 0.5f}   // Gris azulado
};

// Cambia el color de fondo al siguiente en la lista
private void cambiarColorFondo() {
    colorIndex = (colorIndex + 1) % colores.length;  // Avanza al siguiente color en la lista
    PantallaBase.setFondoColor(colores[colorIndex]);  // Actualiza el color de fondo globalmente
    actualizarTextoColor();  // Actualiza el texto de la opción en el menú
}


// Actualiza el texto de la opción en función del color actual
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


    @Override
    public void dispose() {
        super.dispose();
        // Liberar recursos de los sonidos y la música
        SoundUtils.dispose(optionMoveSound);
        SoundUtils.dispose(backgroundMusic);
    }

    @Override
    public void resize(int width, int height) {
            }

    @Override
    public void pause() {
        
    }

    @Override
    public void resume() {
        
    }

    @Override
    public void hide() {
        
    }
}
