package puppy.code.Pantallas;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import puppy.code.Componentes.FabricaPantallas;




public class SpaceNavigation extends Game {
	private FabricaPantallas fabricaPantallas;
	private String nombreJuego = "Void Odyssey";
	private SpriteBatch batch;
	private BitmapFont font;
	private int highScore;	

    @Override
    public void create() {
    	highScore = 0;
		batch = new SpriteBatch();
		font = new BitmapFont(); // usa Arial font x defecto
		font.getData().setScale(2f);
		
    	// Inicializar la fábrica
        fabricaPantallas = new FabricaPantallasConcreta(this);
        // Establecer la pantalla inicial usando la fábrica
        setScreen(fabricaPantallas.crearPantallaMenu());
    }
    
	public void render() {
		super.render(); // important!
	}

	public void dispose() {
		batch.dispose();
		font.dispose();
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public BitmapFont getFont() {
		return font;
	}

	public int getHighScore() {
		return highScore;
	}

	public void setHighScore(int highScore) {
		this.highScore = highScore;
	}
    
    public void cambiarAPantallaMenu() {
        setScreen(fabricaPantallas.crearPantallaMenu());
    }

    public void cambiarAPantallaOpciones() {
        setScreen(fabricaPantallas.crearPantallaOpciones());
    }

    public void cambiarAPantallaPausa(PantallaJuego pantallaJuego) {
        setScreen(fabricaPantallas.crearPantallaPausa(pantallaJuego));
    }

    public void cambiarAPantallaJuego(int nivel, int vidas, int puntuacion, int velocidad, int dificultad, int asteroides) {
        setScreen(fabricaPantallas.crearPantallaJuego(nivel, vidas, puntuacion, velocidad, dificultad, asteroides));
    }
    
    public void cambiarAPantallaGameOver() {
        setScreen(fabricaPantallas.crearPantallaGameOver());
    }
}