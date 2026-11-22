import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Cita de la clínica: una mascota con un veterinario. En la agenda, el cliente
 * de la cita es el id de la mascota. Mientras está pendiente su resultado es
 * null; cuando termina se guarda el resultado y las notas.
 * Cada tipo de cita (consulta, cirugía) es una subclase.
 *
 * @author Joaquín Rodríguez, Mario Cancela, Antonio Cano, Nicolas Zippold
 * @version 1.0
 */
public abstract class CitaVeterinaria extends Cita {

    private final Mascota mascota;
    private final Veterinario veterinario;
    private Resultado resultado;
    private String notas;

    /**
     * Crea una cita pendiente.
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
        resultado = null;
        notas = "";
    }

    // Antes de llamar a super no se puede usar this, por eso es static
    private static String idDe(Mascota mascota) throws ArgumentoException {
        if (mascota == null) {
            throw new ArgumentoException("La mascota no puede ser null");
        }
        return mascota.getId();
    }

    /**
     * Comprueba un texto que recibe una cita (motivo, intervención, notas...) y lo devuelve sin
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

    /** @return el resultado, o null si la cita está pendiente */
    public Resultado getResultado() {
        return resultado;
    }

    /** @return las notas del veterinario ("" si no hay) */
    public String getNotas() {
        return notas;
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

    /**
     * Termina la cita con un resultado. Solo se puede hacer una vez.
     *
     * @param resultado cómo ha terminado la cita
     * @param notas     notas del veterinario (pueden estar vacías)
     * @throws ArgumentoException si el resultado o las notas son null o la cita ya estaba terminada
     */
    public void terminar(Resultado resultado, String notas) throws ArgumentoException {
        if (resultado == null) {
            throw new ArgumentoException("El resultado no puede ser null");
        }
        if (this.resultado != null) {
            throw new ArgumentoException("La cita ya estaba terminada");
        }
        this.notas = texto(notas, "las notas", true);
        this.resultado = resultado;
    }

    /**
     * @return el precio si la cita fue atendida, 0 en otro caso
     */
    public int ingreso() {
        if (resultado == Resultado.ATENDIDA) {
            return precio();
        }
        return 0;
    }

    /**
     * @return los datos comunes a todas las citas, por ejemplo
     * "Consulta de Toby (M001) con Laura Martín"
     */
    protected String camposComunes() {
        return getTipo() + " de " + mascota.getNombre() + " (" + mascota.getId() + ") con " + veterinario.getNombre();
    }

    /**
     * @return la cita como "09:00-09:30 Consulta de Toby (M001) con Laura Martín.
     * Motivo: cojera [pendiente]"
     */
    @Override
    public String toString() {
        String estado = "pendiente";
        if (resultado != null) {
            estado = resultado.toString();
            if (!notas.isEmpty()) {
                estado = estado + ": " + notas;
            }
        }
        return getInicio() + "-" + getFin() + " " + camposComunes() + ". " + descripcion() + " [" + estado + "]";
    }
}
