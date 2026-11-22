import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Cirugía de una mascota. Su precio depende de la duración y del peso de la
 * mascota (anestesia), y tiene normas propias para programarla.
 *
 * @author Joaquín Rodríguez, Mario Cancela, Antonio Cano, Nicolas Zippold
 * @version 1.0
 */
public class Cirugia extends CitaVeterinaria {

    /** Precio por hora de quirófano en céntimos (90,00 €). */
    public static final int PRECIO_HORA = 9000;
    /** Precio de la anestesia por kilo en céntimos (1,50 €). */
    public static final int ANESTESIA_POR_KG = 150;
    /** Hora a la que tiene que haber terminado una cirugía. */
    public static final LocalTime FIN_MANANA = LocalTime.of(14, 0);
    /** Duración mínima en minutos; también es el tamaño de los tramos. */
    public static final int DURACION_MINIMA = 30;
    /** Duración máxima en minutos. */
    public static final int DURACION_MAXIMA = 240;

    private final String intervencion;

    /**
     * Crea una cirugía.
     *
     * @param mascota      mascota que se opera
     * @param veterinario  veterinario que opera
     * @param fecha        día de la cirugía
     * @param inicio       hora de inicio
     * @param duracion     duración en minutos
     * @param intervencion qué se le va a hacer
     * @throws ArgumentoException si algún dato es null o la intervención está vacía
     */
    public Cirugia(Mascota mascota, Veterinario veterinario, LocalDate fecha, LocalTime inicio, int duracion,
                   String intervencion) throws ArgumentoException {
        super(mascota, veterinario, fecha, inicio, duracion);
        this.intervencion = texto(intervencion, "la intervención", false);
    }

    /** @return la intervención */
    public String getIntervencion() {
        return intervencion;
    }

    /**
     * Precio = horas de quirófano + anestesia según el peso. Las divisiones son enteras.
     *
     * @return precio en céntimos
     */
    @Override
    public int precio() {
        return PRECIO_HORA * getDuracion() / 60 + ANESTESIA_POR_KG * getMascota().getPeso() / 1000;
    }

    /** @return "Cirugía" */
    @Override
    public String getTipo() {
        return "Cirugía";
    }

    /** @return siempre CIRUGIA */
    @Override
    public Especialidad especialidadRequerida() {
        return Especialidad.CIRUGIA;
    }

    /** @return la intervención, como "Intervención: extirpación de un quiste" */
    @Override
    public String descripcion() {
        return "Intervención: " + intervencion;
    }

    /**
     * Normas de la cirugía: se programa al menos el día anterior, termina como
     * muy tarde a las 14:00 y dura de 30 a 240 minutos en tramos de 30.
     *
     * @param ahora fecha y hora actuales
     * @throws ClinicaException si alguna norma no se cumple
     */
    @Override
    protected void comprobarNormasPropias(LocalDateTime ahora) throws ClinicaException {
        if (!getFecha().isAfter(ahora.toLocalDate())) {
            throw new ClinicaException("Las cirugías se programan como tarde el día anterior");
        }
        if (getFin().isAfter(FIN_MANANA)) {
            throw new ClinicaException("Una cirugía tiene que terminar como muy tarde a las " + FIN_MANANA);
        }
        if (getDuracion() < DURACION_MINIMA || getDuracion() > DURACION_MAXIMA
                || getDuracion() % DURACION_MINIMA != 0) {
            throw new ClinicaException("Una cirugía dura de " + DURACION_MINIMA + " a " + DURACION_MAXIMA
                    + " minutos en tramos de " + DURACION_MINIMA);
        }
    }

    /**
     * @param nuevoInicio nueva hora de inicio
     * @return una cirugía igual a esta pero empezando a otra hora
     * @throws ArgumentoException si la nueva hora no es válida
     */
    @Override
    public Cirugia conInicio(LocalTime nuevoInicio) throws ArgumentoException {
        return new Cirugia(getMascota(), getVeterinario(), getFecha(), nuevoInicio, getDuracion(), intervencion);
    }
}
