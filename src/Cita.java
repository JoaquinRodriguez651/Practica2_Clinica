import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Cita de la agenda: cliente, fecha, hora de inicio y duración en minutos.
 * Es abstracta porque cada tipo de cita (consulta, cirugía...) es una subclase.
 * Es inmutable: una vez creada no cambia.
 * Invariante: el cliente no está vacío, la duración es mayor que 0 y la cita
 * termina antes de medianoche.
 *
 * @author Joaquín Rodríguez, Mario Cancela, Antonio Cano, Nicolas Zippold
 * @version 2.0
 */
public abstract class Cita {

    private final String cliente;
    private final LocalDate fecha;
    private final LocalTime inicio;
    private final int duracion;

    /**
     * Crea una cita. Es protected porque solo la llaman las subclases.
     * Pre: ningún dato es null, el cliente no está vacío, duracion mayor que 0 y la cita
     * termina antes de medianoche.
     * Post: se crea la cita con esos datos.
     *
     * @param cliente  identificador del cliente
     * @param fecha    día de la cita
     * @param inicio   hora de inicio
     * @param duracion duración en minutos
     * @throws ArgumentoException si no se cumple la precondición
     */
    protected Cita(String cliente, LocalDate fecha, LocalTime inicio, int duracion) throws ArgumentoException {
        if (cliente == null || cliente.trim().isEmpty()) {
            throw new ArgumentoException("El cliente no puede estar vacío");
        }
        if (fecha == null || inicio == null) {
            throw new ArgumentoException("La fecha y la hora no pueden ser null");
        }
        if (duracion <= 0) {
            throw new ArgumentoException("La duración debe ser mayor que 0");
        }
        if (inicio.getHour() * 60 + inicio.getMinute() + duracion >= 1440) {
            throw new ArgumentoException("La cita no puede pasar de medianoche");
        }
        this.cliente = cliente;
        this.fecha = fecha;
        this.inicio = inicio;
        this.duracion = duracion;
    }

    // Los getters son final para que ninguna subclase pueda romper el invariante

    /** Pre: ninguna. Post: devuelve el cliente. */
    public final String getCliente() {
        return cliente;
    }

    /** Pre: ninguna. Post: devuelve la fecha. */
    public final LocalDate getFecha() {
        return fecha;
    }

    /** Pre: ninguna. Post: devuelve la hora de inicio. */
    public final LocalTime getInicio() {
        return inicio;
    }

    /** Pre: ninguna. Post: devuelve la duración en minutos. */
    public final int getDuracion() {
        return duracion;
    }

    /**
     * Pre: ninguna. Post: devuelve inicio + duracion.
     *
     * @return hora a la que termina la cita
     */
    public final LocalTime getFin() {
        return inicio.plusMinutes(duracion);
    }

    /**
     * Crea una copia de esta cita que empieza a otra hora. Cada subclase la
     * implementa devolviendo una cita de su mismo tipo.
     * Pre: nuevoInicio no es null y la cita sigue terminando antes de medianoche.
     * Post: devuelve una cita nueva igual a esta salvo el inicio. Esta no cambia.
     *
     * @param nuevoInicio nueva hora de inicio
     * @return la cita nueva
     * @throws ArgumentoException si no se cumple la precondición
     */
    public abstract Cita conInicio(LocalTime nuevoInicio) throws ArgumentoException;

    /**
     * Pre: ninguna. Post: devuelve la cita como "09:00-09:40 M001 (40 min)".
     *
     * @return la cita en forma de texto
     */
    @Override
    public String toString() {
        return inicio + "-" + getFin() + " " + cliente + " (" + duracion + " min)";
    }
}
