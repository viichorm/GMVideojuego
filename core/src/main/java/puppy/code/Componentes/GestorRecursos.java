package puppy.code.Componentes;

/**
 *
 * @author Vicente
 */
public interface GestorRecursos<T> {
    T cargar(String ruta); // Método genérico para cargar un recurso
    void reproducir(T recurso); // Reproducir el recurso
    void detener(T recurso); // Detener la reproducción del recurso, si aplica
    void liberar(T recurso); // Liberar recursos para evitar fugas de memoria
}
