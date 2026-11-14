/**
 * Excepción comprobada que se lanza cuando un argumento es null o tiene un
 * valor imposible. Por ejemplo, un cliente vacío, una duración negativa o una
 * cita que no cabe en la agenda.
 *
 * @author Joaquín Rodríguez, Mario Cancela, Antonio Cano, Nicolas Zippold
 * @version 2.0
 */
public class ArgumentoException extends Exception {

    /**
     * Crea la excepción con un mensaje que explica el error.
     *
     * @param mensaje descripción del error
     */
    public ArgumentoException(String mensaje) {
        super(mensaje);
    }
}
