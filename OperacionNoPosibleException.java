/**
 * Excepción que se lanza cuando los datos son correctos, pero la operación no
 * se puede hacer por cómo está la agenda. Por ejemplo, cuando el hueco está
 * ocupado, el cliente ya tiene cita ese día o la cita no existe.
 *
 * @author Joaquín Rodríguez, Mario Cancela, Antonio Cano, Nicolas Zippold
 * @version 1.0
 */
public class OperacionNoPosibleException extends Exception {

    /**
     * Crea la excepción con un mensaje que explica el error.
     *
     * @param mensaje Descripción del error
     */
    public OperacionNoPosibleException(String mensaje) {
        super(mensaje);
    }
}
