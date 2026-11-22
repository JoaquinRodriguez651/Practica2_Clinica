/**
 * Excepción comprobada que se lanza cuando una operación no cumple las normas
 * de la clínica, por ejemplo programar una cita en domingo.
 *
 * @author Joaquín Rodríguez, Mario Cancela, Antonio Cano, Nicolas Zippold
 * @version 1.0
 */
public class ClinicaException extends Exception {

    /**
     * Crea la excepción con un mensaje que explica qué norma no se cumple.
     *
     * @param mensaje descripción del error
     */
    public ClinicaException(String mensaje) {
        super(mensaje);
    }
}
