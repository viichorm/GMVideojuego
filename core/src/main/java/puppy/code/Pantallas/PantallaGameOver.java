package puppy.code.Pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import puppy.code.Componentes.PantallaBase;


public class PantallaGameOver extends PantallaBase {

    public PantallaGameOver(SpaceNavigation game) {
        super(game);
    }

    @Override
    public void render(float delta) {
        super.render(delta);  // Utiliza el fondo compartido de PantallaBase

        game.getBatch().begin();
        game.getFont().draw(game.getBatch(), "Game Over !!! ", 120, 400, 400, 1, true);
        game.getFont().draw(game.getBatch(), "Pincha en cualquier lado para reiniciar ...", 100, 300);
        game.getBatch().end();

        if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            Screen ss = new PantallaJuego(game, 1, 3, 0, 1, 1, 10);
            ss.resize(1200, 800);
            game.setScreen(ss);
            dispose();
        }
    }

    @Override
    protected void manejarInput() {
        // No se necesita manejo de input en PantallaGameOver
    }

    @Override
    public void show() {
        
    }

    @Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

    @Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

    @Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

    @Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
}