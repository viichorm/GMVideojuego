package puppy.code.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import puppy.code.Componentes.GestorRecursos;

public class SoundUtils implements GestorRecursos<Sound> {
	// Instancia única de la clase
    private static SoundUtils instancia;

    // Constructor privado para evitar instanciación externa
    private SoundUtils() {}

    // Método estático para obtener la instancia única
    public static SoundUtils getInstancia() {
        if (instancia == null) {
            instancia = new SoundUtils();
        }
        return instancia;
    }

    @Override
    public Sound cargar(String ruta) {
        return Gdx.audio.newSound(Gdx.files.internal(ruta));
    }

    @Override
    public void reproducir(Sound sonido) {
        if (sonido != null) {
            sonido.play();
        }
    }

    @Override
    public void detener(Sound sonido) {
        if (sonido != null) {
            sonido.stop();
        }
    }

    @Override
    public void liberar(Sound sonido) {
        if (sonido != null) {
            sonido.dispose();
        }
    }
}
