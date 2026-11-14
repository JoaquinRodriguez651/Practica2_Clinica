/**
 * Excepción que se lanza cuando un dato que se pasa a un método no es válido
 * por sí mismo, sin tener en cuenta el estado de la agenda. Por ejemplo, un
 * nombre de cliente vacío, una duración menor o igual que cero o un día de
 * fin de semana.
 *
 * @author Joaquín Rodríguez, Mario Cancela, Antonio Cano, Nicolas Zippold
 * @version 1.0
 */
public class DatoNoValidoException extends Exception {

    /**
     * Crea la excepción con un mensaje que explica el error.
     *
     * @param mensaje Descripción del error
     */
    public DatoNoValidoException(String mensaje) {
        super(mensaje);
    }
}
