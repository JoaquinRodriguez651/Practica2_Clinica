import java.io.Serializable;

/**
 * Veterinario de la clínica. Tiene una especialidad, que decide qué citas
 * puede atender, y su propia agenda con el horario de la clínica.
 *
 * @author Joaquín Rodríguez, Mario Cancela, Antonio Cano, Nicolas Zippold
 * @version 1.0
 */
public class Veterinario implements Serializable {

    private final String codigo;
    private final String nombre;
    private final Especialidad especialidad;
    private final Agenda agenda;

    /**
     * Crea un veterinario con la agenda vacía.
     *
     * @param codigo       código, por ejemplo "V1"
     * @param nombre       nombre del veterinario
     * @param especialidad su especialidad
     * @throws ArgumentoException si el código o el nombre están vacíos o la especialidad es null
     */
    public Veterinario(String codigo, String nombre, Especialidad especialidad) throws ArgumentoException {
        if (codigo == null || codigo.trim().isEmpty() || nombre == null || nombre.trim().isEmpty()) {
            throw new ArgumentoException("El código y el nombre no pueden estar vacíos");
        }
        if (especialidad == null) {
            throw new ArgumentoException("La especialidad no puede ser null");
        }
        this.codigo = codigo;
        this.nombre = nombre;
        this.especialidad = especialidad;
        agenda = new Agenda(Clinica.APERTURA, Clinica.CIERRE);
    }

    /** @return código del veterinario */
    public String getCodigo() {
        return codigo;
    }

    /** @return nombre del veterinario */
    public String getNombre() {
        return nombre;
    }

    /** @return especialidad del veterinario */
    public Especialidad getEspecialidad() {
        return especialidad;
    }

    /** @return la agenda del veterinario (no una copia) */
    public Agenda getAgenda() {
        return agenda;
    }

    /**
     * Dice si puede atender una cita que exige una especialidad. Puede si la
     * especialidad es General o es la suya.
     *
     * @param requerida especialidad que exige la cita
     * @return true si la puede atender
     */
    public boolean puedeAtender(Especialidad requerida) {
        return requerida == Especialidad.GENERAL || requerida == especialidad;
    }

    /**
     * @return el veterinario como "V1 Laura Martín (GENERAL)"
     */
    @Override
    public String toString() {
        return codigo + " " + nombre + " (" + especialidad + ")";
    }
}
