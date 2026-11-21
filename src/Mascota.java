import java.io.Serializable;
import java.time.LocalDate;

/**
 * Mascota de un cliente de la clínica. Sus datos no cambian una vez dada de alta.
 *
 * @author Joaquín Rodríguez, Mario Cancela, Antonio Cano, Nicolas Zippold
 * @version 1.0
 */
public class Mascota implements Serializable {

    private final String id;
    private final String nombre;
    private final String propietario;
    private final Especie especie;
    private final LocalDate nacimiento;
    private final int peso;

    /**
     * Crea una mascota.
     *
     * @param id          identificador, por ejemplo "M001"
     * @param nombre      nombre de la mascota
     * @param propietario nombre del dueño
     * @param especie     especie de la mascota
     * @param nacimiento  fecha de nacimiento
     * @param peso        peso en gramos
     * @throws ArgumentoException si algún texto está vacío, la especie o la fecha
     *                            son null o el peso no es mayor que 0
     */
    public Mascota(String id, String nombre, String propietario, Especie especie, LocalDate nacimiento, int peso)
            throws ArgumentoException {
        if (vacio(id) || vacio(nombre) || vacio(propietario)) {
            throw new ArgumentoException("El id, el nombre y el propietario no pueden estar vacíos");
        }
        if (especie == null || nacimiento == null) {
            throw new ArgumentoException("La especie y la fecha de nacimiento no pueden ser null");
        }
        if (peso <= 0) {
            throw new ArgumentoException("El peso tiene que ser mayor que 0");
        }
        this.id = id;
        this.nombre = nombre;
        this.propietario = propietario;
        this.especie = especie;
        this.nacimiento = nacimiento;
        this.peso = peso;
    }

    /** @return identificador de la mascota */
    public String getId() {
        return id;
    }

    /** @return nombre de la mascota */
    public String getNombre() {
        return nombre;
    }

    /** @return nombre del propietario */
    public String getPropietario() {
        return propietario;
    }

    /** @return especie de la mascota */
    public Especie getEspecie() {
        return especie;
    }

    /** @return fecha de nacimiento */
    public LocalDate getNacimiento() {
        return nacimiento;
    }

    /** @return peso en gramos */
    public int getPeso() {
        return peso;
    }

    /**
     * Calcula la edad en años cumplidos. Recibe la fecha de hoy para que las
     * pruebas no dependan del día en que se ejecutan.
     *
     * @param hoy fecha actual, no anterior al nacimiento
     * @return años cumplidos
     */
    public int getEdad(LocalDate hoy) {
        int edad = hoy.getYear() - nacimiento.getYear();
        // si este año todavía no ha llegado su cumpleaños, tiene uno menos
        if (nacimiento.plusYears(edad).isAfter(hoy)) {
            edad--;
        }
        return edad;
    }

    /**
     * @return la mascota como "M001 Toby (Perro) - Ana García"
     */
    @Override
    public String toString() {
        return id + " " + nombre + " (" + especie + ") - " + propietario;
    }

    private static boolean vacio(String s) {
        return s == null || s.trim().isEmpty();
    }
}
