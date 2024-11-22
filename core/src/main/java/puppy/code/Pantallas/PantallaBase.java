package puppy.code.Pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import puppy.code.Pantallas.SpaceNavigation;

public abstract class PantallaBase implements Screen {
    protected SpaceNavigation game;
    protected OrthographicCamera camera;
    protected SpriteBatch batch;
    protected BitmapFont font;
    // Volumen global para la música, inicializado al 50%
    protected static float volumenGlobal = 0.5f;
    
    // Color de fondo compartido entre todas las pantallas
    protected static float[] fondoColor = {0, 0, 0.2f};  // Azul oscuro por defecto

    // Método para establecer el volumen global
    public static void setVolumenGlobal(float nuevoVolumen) {
        volumenGlobal = Math.max(0, Math.min(nuevoVolumen, 1));  // Limitar entre 0 y 1
    }

    // Método para obtener el volumen global
    public static float getVolumenGlobal() {
        return volumenGlobal;
    }
    
    // Constructor
    public PantallaBase(SpaceNavigation game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1200, 800);

        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2f);
    }

    @Override
    public void render(float delta) {
        // Usar el color de fondo compartido
        ScreenUtils.clear(fondoColor[0], fondoColor[1], fondoColor[2], 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
    }

    // Método para cambiar el color de fondo de forma global
    public static void setFondoColor(float[] nuevoColor) {
        fondoColor = nuevoColor;
    }
    
    @Override
    public void resize(int width, int height) {
    	camera.setToOrtho(false, width, height);
    }

    @Override
    public void dispose() {
        // Liberar recursos comunes
        batch.dispose();
        font.dispose();
    }

    protected abstract void manejarInput();
}

