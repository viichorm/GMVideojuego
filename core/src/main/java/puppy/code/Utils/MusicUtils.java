package puppy.code.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import puppy.code.Componentes.GestorRecursos;

public class MusicUtils implements GestorRecursos<Music> {
	
	// Instancia única de la clase
    private static MusicUtils instancia;

    // Constructor privado para evitar instanciación externa
    private MusicUtils() {}

    // Método estático para obtener la instancia única
    public static MusicUtils getInstancia() {
        if (instancia == null) {
            instancia = new MusicUtils();
        }
        return instancia;
    }

    @Override
    public Music cargar(String ruta) {
        return Gdx.audio.newMusic(Gdx.files.internal(ruta));
    }

    @Override
    public void reproducir(Music musica) {
        if (musica != null) {
            musica.play();
        }
    }

    @Override
    public void detener(Music musica) {
        if (musica != null) {
            musica.stop();
        }
    }

    @Override
    public void liberar(Music musica) {
        if (musica != null) {
            musica.dispose();
        }
    }
}
