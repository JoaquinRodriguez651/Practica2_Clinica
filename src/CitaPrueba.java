import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Tipo de cita mínimo que solo se usa en las pruebas de la agenda. Hace falta
 * porque Cita es abstracta y Consulta y Cirugia todavía no existen (hito 2).
 *
 * @author Joaquín Rodríguez, Mario Cancela, Antonio Cano, Nicolas Zippold
 * @version 2.0
 */
public class CitaPrueba extends Cita {

    /**
     * Crea la cita. Pre y post: las del constructor de Cita.
     *
     * @param cliente  identificador del cliente
     * @param fecha    día de la cita
     * @param inicio   hora de inicio
     * @param duracion duración en minutos
     * @throws ArgumentoException si algún dato no es válido
     */
    public CitaPrueba(String cliente, LocalDate fecha, LocalTime inicio, int duracion) throws ArgumentoException {
        super(cliente, fecha, inicio, duracion);
    }

    /**
     * Pre: la de Cita.conInicio. Post: devuelve una CitaPrueba igual a esta con otro inicio.
     *
     * @param nuevoInicio nueva hora de inicio
     * @return la cita nueva
     * @throws ArgumentoException si nuevoInicio no es válido
     */
    @Override
    public CitaPrueba conInicio(LocalTime nuevoInicio) throws ArgumentoException {
        return new CitaPrueba(getCliente(), getFecha(), nuevoInicio, getDuracion());
    }
}
