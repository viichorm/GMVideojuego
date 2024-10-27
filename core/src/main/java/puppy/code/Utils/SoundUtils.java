package puppy.code.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import puppy.code.Componentes.GestorRecursos;

public class SoundUtils implements GestorRecursos<Sound> {

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
