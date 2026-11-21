import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Clínica veterinaria. De momento solo tiene el horario, que necesitan los
 * veterinarios y las citas; el resto de la clínica se hace en el hito 3.
 * El horario es static: se usa sin crear ninguna clínica (Clinica.APERTURA).
 *
 * @author Joaquín Rodríguez, Mario Cancela, Antonio Cano, Nicolas Zippold
 * @version 1.0
 */
public class Clinica implements Serializable {

    /** Hora a la que abre la clínica. */
    public static final LocalTime APERTURA = LocalTime.of(9, 0);

    /** Hora a la que cierra la clínica. */
    public static final LocalTime CIERRE = LocalTime.of(20, 0);

    /**
     * Pre: fecha no es null. Post: devuelve true si la clínica abre ese día
     * (abre de lunes a sábado y cierra los domingos).
     *
     * @param fecha día
     * @return true si abre
     * @throws ArgumentoException si la fecha es null
     */
    public static boolean abre(LocalDate fecha) throws ArgumentoException {
        if (fecha == null) {
            throw new ArgumentoException("La fecha no puede ser null");
        }
        return fecha.getDayOfWeek() != DayOfWeek.SUNDAY;
    }
}
