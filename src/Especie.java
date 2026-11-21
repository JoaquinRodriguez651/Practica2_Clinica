/**
 * Especies de mascota que atiende la clínica. Cada especie tiene la duración
 * de su consulta y la especialidad que hace falta para atenderla.
 *
 * @author Joaquín Rodríguez, Mario Cancela, Antonio Cano, Nicolas Zippold
 * @version 1.0
 */
public enum Especie {
    PERRO("Perro", 30, Especialidad.GENERAL),
    GATO("Gato", 30, Especialidad.GENERAL),
    EXOTICO("Exótico", 45, Especialidad.EXOTICOS);

    private final String nombre;
    private final int duracionConsulta;
    private final Especialidad especialidad;

    Especie(String nombre, int duracionConsulta, Especialidad especialidad) {
        this.nombre = nombre;
        this.duracionConsulta = duracionConsulta;
        this.especialidad = especialidad;
    }

    /**
     * @return nombre de la especie para mostrar, por ejemplo "Perro"
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @return minutos que dura una consulta de esta especie
     */
    public int getDuracionConsulta() {
        return duracionConsulta;
    }

    /**
     * @return especialidad que hace falta para la consulta
     */
    public Especialidad getEspecialidad() {
        return especialidad;
    }

    /**
     * @return el nombre de la especie
     */
    @Override
    public String toString() {
        return nombre;
    }
}
