package puppy.code.Componentes;

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
    
    // Color de fondo compartido entre todas las pantallas
    protected static float[] fondoColor = {0, 0, 0.2f};  // Azul oscuro por defecto

    // Constructor
    public PantallaBase(SpaceNavigation game) {
        this.game = game;
        
        // Configurar la cámara
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1200, 800);
        
        // Inicializar el batch y la fuente
        batch = new SpriteBatch();
        font = new BitmapFont();  // Usa la fuente predeterminada
        font.getData().setScale(2f);  // Aumentar el tamaño de la fuente
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

