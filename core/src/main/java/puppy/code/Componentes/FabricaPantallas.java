package puppy.code.Componentes;

import puppy.code.Pantallas.PantallaBase;
import puppy.code.Pantallas.PantallaJuego;
import puppy.code.Pantallas.SpaceNavigation;

public interface FabricaPantallas {
	PantallaBase crearPantallaMenu();
    PantallaBase crearPantallaOpciones();
    PantallaBase crearPantallaPausa(PantallaJuego pantallaJuego);
    PantallaBase crearPantallaJuego(int ronda, int vidas, int score, int velocidadX, int velocidadY, int asteroides);
    PantallaBase crearPantallaGameOver();
}
