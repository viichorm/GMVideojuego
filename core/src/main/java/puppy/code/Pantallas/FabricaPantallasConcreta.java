package puppy.code.Pantallas;

import puppy.code.Componentes.*;
import puppy.code.Pantallas.*;

public class FabricaPantallasConcreta implements FabricaPantallas {
    private SpaceNavigation game;

    public FabricaPantallasConcreta(SpaceNavigation game) {
        this.game = game;
    }

    @Override
    public PantallaBase crearPantallaMenu() {
        return new PantallaMenu(game);
    }

    @Override
    public PantallaBase crearPantallaOpciones() {
        return new PantallaOpciones(game);
    }

    @Override
    public PantallaBase crearPantallaPausa(PantallaJuego pantallaJuego) {
        return new PantallaPausa(game, pantallaJuego);
    }

    @Override
    public PantallaBase crearPantallaJuego(int nivel, int vidas, int puntuacion, int velocidad, int dificultad, int asteroides) {
        return new PantallaJuego(game, nivel, vidas, puntuacion, velocidad, dificultad, asteroides);
    }
    
    @Override
    public PantallaBase crearPantallaGameOver() {
    	return new PantallaGameOver(game);
    }
}
