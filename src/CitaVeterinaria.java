import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Cita de la clínica: una mascota con un veterinario. En la agenda, el cliente
 * de la cita es el id de la mascota. Cada tipo de cita (consulta, cirugía) es
 * una subclase.
 *
 * @author Joaquín Rodríguez, Mario Cancela, Antonio Cano, Nicolas Zippold
 * @version 1.0
 */
public abstract class CitaVeterinaria extends Cita {

    private final Mascota mascota;
    private final Veterinario veterinario;

    /**
     * Crea una cita.
     *
     * @param mascota     mascota que viene a la cita
     * @param veterinario veterinario que la atiende
     * @param fecha       día de la cita
     * @param inicio      hora de inicio
     * @param duracion    duración en minutos
     * @throws ArgumentoException si algún dato es null o no es válido para una Cita
     */
    protected CitaVeterinaria(Mascota mascota, Veterinario veterinario, LocalDate fecha, LocalTime inicio,
                              int duracion) throws ArgumentoException {
        // en la agenda el cliente de la cita es el id de la mascota
        super(idDe(mascota), fecha, inicio, duracion);
        if (veterinario == null) {
            throw new ArgumentoException("El veterinario no puede ser null");
        }
        this.mascota = mascota;
        this.veterinario = veterinario;
    }

    // Antes de llamar a super no se puede usar this, por eso es static
    private static String idDe(Mascota mascota) throws ArgumentoException {
        if (mascota == null) {
            throw new ArgumentoException("La mascota no puede ser null");
        }
        return mascota.getId();
    }

    /**
     * Comprueba un texto que recibe una cita (motivo, intervención...) y lo devuelve sin
     * espacios al principio ni al final.
     *
     * @param s              texto que se comprueba
     * @param que            nombre del dato para el mensaje de error, por ejemplo "el motivo"
     * @param vacioPermitido si el texto puede estar vacío
     * @return el texto sin espacios sobrantes
     * @throws ArgumentoException si es null, o si está vacío y no se permite
     */
    protected static String texto(String s, String que, boolean vacioPermitido) throws ArgumentoException {
        if (s == null) {
            throw new ArgumentoException("Hay que indicar " + que);
        }
        if (!vacioPermitido && s.trim().isEmpty()) {
            throw new ArgumentoException("Hay que indicar " + que);
        }
        return s.trim();
    }

    /** @return la mascota de la cita */
    public Mascota getMascota() {
        return mascota;
    }

    /** @return el veterinario de la cita */
    public Veterinario getVeterinario() {
        return veterinario;
    }

    /** @return precio de la cita en céntimos */
    public abstract int precio();

    /** @return nombre del tipo de cita: "Consulta" o "Cirugía" */
    public abstract String getTipo();

    /** @return especialidad que tiene que tener el veterinario */
    public abstract Especialidad especialidadRequerida();

    /** @return los datos propios del tipo de cita, por ejemplo el motivo */
    public abstract String descripcion();

    /**
     * Comprueba las normas para programar la cita, en este orden: domingo,
     * horario, pasado y especialidad. Al final llama a comprobarNormasPropias
     * para las normas de cada tipo de cita. Es final para que las subclases no
     * se puedan saltar las normas comunes.
     *
     * @param ahora fecha y hora actuales
     * @throws ClinicaException con el motivo si alguna norma no se cumple
     */
    public final void comprobarNormas(LocalDateTime ahora) throws ClinicaException {
        if (!Clinica.abre(getFecha())) {
            throw new ClinicaException("La clínica cierra los domingos");
        }
        if (getInicio().isBefore(Clinica.APERTURA) || getFin().isAfter(Clinica.CIERRE)) {
            throw new ClinicaException("La cita tiene que empezar y terminar entre las "
                    + Clinica.APERTURA + " y las " + Clinica.CIERRE);
        }
        if (LocalDateTime.of(getFecha(), getInicio()).isBefore(ahora)) {
            throw new ClinicaException("No se puede programar una cita en el pasado");
        }
        if (!veterinario.puedeAtender(especialidadRequerida())) {
            throw new ClinicaException(veterinario.getNombre() + " no puede atender esta cita, hace falta "
                    + especialidadRequerida());
        }
        comprobarNormasPropias(ahora);
    }

    /**
     * Normas propias de cada tipo de cita. Por defecto no hay ninguna; la
     * cirugía lo redefine.
     *
     * @param ahora fecha y hora actuales
     * @throws ClinicaException si alguna norma no se cumple
     */
    protected void comprobarNormasPropias(LocalDateTime ahora) throws ClinicaException {
    }
}
