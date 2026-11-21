import java.io.Serializable;

/**
 * Veterinario de la clínica. Cada veterinario tiene su propia agenda, que crea
 * él mismo con el horario de la clínica (composición: la agenda no existe sin
 * el veterinario).
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
     * Pre: el código y el nombre no están vacíos y la especialidad no es null.
     * Post: getAgenda() tiene el horario de la clínica y ninguna cita.
     *
     * @param codigo       identificador del veterinario
     * @param nombre       nombre del veterinario
     * @param especialidad especialidad del veterinario
     * @throws ArgumentoException si no se cumple la precondición
     */
    public Veterinario(String codigo, String nombre, Especialidad especialidad) throws ArgumentoException {
        if (vacio(codigo) || vacio(nombre)) {
            throw new ArgumentoException("El código y el nombre no pueden estar vacíos");
        }
        if (especialidad == null) {
            throw new ArgumentoException("La especialidad no puede ser null");
        }
        this.codigo = codigo;
        this.nombre = nombre;
        this.especialidad = especialidad;
        // la crea el propio veterinario: es suya y no se comparte (composición)
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

    /**
     * Devuelve la agenda del veterinario, no una copia, para que la clínica
     * pueda reservar y cancelar citas en ella.
     *
     * @return la agenda del veterinario
     */
    public Agenda getAgenda() {
        return agenda;
    }


    /**
     * Pre: ninguna. Post: devuelve true si la especialidad que exige la cita es
     * General o es la del veterinario. Así un cirujano o un especialista en
     * exóticos también pueden atender consultas normales.
     *
     * @param requerida especialidad que exige la cita
     * @return true si puede atenderla
     */
    public boolean puedeAtender(Especialidad requerida) {
        return requerida == Especialidad.GENERAL || requerida == especialidad;
    }

    private static boolean vacio(String s) {
        return s == null || s.trim().isEmpty();
    }


}
