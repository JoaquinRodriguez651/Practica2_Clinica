import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Clínica veterinaria. De momento solo tiene su horario, que necesitan los
 * veterinarios y las citas; el resto de la clínica se añade en el hito 3.
 *
 * @author Joaquín Rodríguez, Mario Cancela, Antonio Cano, Nicolas Zippold
 * @version 1.0
 */
public class Clinica {

    /** Hora de apertura, de lunes a sábado. */
    public static final LocalTime APERTURA = LocalTime.of(9, 0);
    /** Hora de cierre, de lunes a sábado. */
    public static final LocalTime CIERRE = LocalTime.of(20, 0);

    /**
     * Dice si la clínica abre un día. Cierra los domingos.
     *
     * @param fecha día que se consulta
     * @return false si es domingo, true en otro caso
     */
    public static boolean abre(LocalDate fecha) {
        return fecha.getDayOfWeek() != DayOfWeek.SUNDAY;
    }
}
