import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Consulta de una mascota. Su duración y la especialidad que exige dependen
 * de la especie de la mascota.
 *
 * @author Joaquín Rodríguez, Mario Cancela, Antonio Cano, Nicolas Zippold
 * @version 1.0
 */
public class Consulta extends CitaVeterinaria {

    /** Precio de una consulta en céntimos (35,00 €). */
    public static final int PRECIO = 3500;

    private final String motivo;

    /**
     * Crea una consulta pendiente. La duración la pone la especie de la mascota.
     *
     * @param mascota     mascota que viene a la consulta
     * @param veterinario veterinario que la atiende
     * @param fecha       día de la consulta
     * @param inicio      hora de inicio
     * @param motivo      motivo de la consulta
     * @throws ArgumentoException si algún dato es null o el motivo está vacío
     */
    public Consulta(Mascota mascota, Veterinario veterinario, LocalDate fecha, LocalTime inicio, String motivo)
            throws ArgumentoException {
        // la duración la pone la especie; si la mascota es null, CitaVeterinaria lanza la excepción
        super(mascota, veterinario, fecha, inicio, mascota == null ? 0 : mascota.getEspecie().getDuracionConsulta());
        this.motivo = texto(motivo, "el motivo", false);
    }

    /** @return motivo de la consulta */
    public String getMotivo() {
        return motivo;
    }

    /** @return el precio fijo de una consulta, en céntimos */
    @Override
    public int precio() {
        return PRECIO;
    }

    /** @return "Consulta" */
    @Override
    public String getTipo() {
        return "Consulta";
    }

    /** @return la especialidad que exige la especie de la mascota */
    @Override
    public Especialidad especialidadRequerida() {
        return getMascota().getEspecie().getEspecialidad();
    }

    /** @return el motivo, como "Motivo: cojera" */
    @Override
    public String descripcion() {
        return "Motivo: " + motivo;
    }

    /**
     * @param nuevoInicio nueva hora de inicio
     * @return una consulta igual a esta pero empezando a otra hora
     * @throws ArgumentoException si la nueva hora no es válida
     */
    @Override
    public Consulta conInicio(LocalTime nuevoInicio) throws ArgumentoException {
        return new Consulta(getMascota(), getVeterinario(), getFecha(), nuevoInicio, motivo);
    }
}
