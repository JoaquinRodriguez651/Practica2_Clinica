import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Agenda de citas de un veterinario. Abre de lunes a viernes de apertura a cierre.
 * Invariante: apertura es anterior a cierre y las citas están en día laborable y dentro
 * del horario, ordenadas por fecha y hora, sin solaparse, y cada cliente tiene como mucho
 * una cita al día.
 *
 * @author Joaquín Rodríguez, Mario Cancela, Antonio Cano, Nicolas Zippold
 * @version 2.0
 */
public class Agenda {

    private final LocalTime apertura;
    private final LocalTime cierre;
    private ArrayList<Cita> citas;

    /**
     * Crea una agenda sin citas con el horario indicado.
     * Pre: apertura y cierre no son null y apertura es anterior a cierre.
     * Post: numCitas() == 0.
     *
     * @param apertura hora de apertura
     * @param cierre   hora de cierre
     * @throws ArgumentoException si no se cumple la precondición
     */
    public Agenda(LocalTime apertura, LocalTime cierre) throws ArgumentoException {
        if (apertura == null || cierre == null) {
            throw new ArgumentoException("La apertura y el cierre no pueden ser null");
        }
        if (!apertura.isBefore(cierre)) {
            throw new ArgumentoException("La apertura tiene que ser antes del cierre");
        }
        this.apertura = apertura;
        this.cierre = cierre;
        citas = new ArrayList<>();
    }

    /** Pre: ninguna. Post: devuelve la hora de apertura. */
    public LocalTime getApertura() {
        return apertura;
    }

    /** Pre: ninguna. Post: devuelve la hora de cierre. */
    public LocalTime getCierre() {
        return cierre;
    }

    /**
     * Pre: ninguna. Post: devuelve los minutos entre la apertura y el cierre.
     *
     * @return minutos de la jornada
     */
    public int getJornada() {
        return minutos(cierre) - minutos(apertura);
    }

    /**
     * Añade una cita en su sitio según el orden cronológico.
     * Pre: datos válidos, día laborable, dentro del horario, intervalo libre y el cliente
     * no tiene ya cita ese día.
     * Post: hay una cita más y las demás no cambian.
     *
     * @param cliente  nombre del cliente
     * @param fecha    día de la cita
     * @param inicio   hora de inicio
     * @param duracion duración en minutos
     * @throws ArgumentoException si no se cumple la precondición
     */
    public void reservar(String cliente, LocalDate fecha, LocalTime inicio, int duracion) throws ArgumentoException {
        Cita nueva = new Cita(cliente, fecha, inicio, duracion); // comprueba cliente, null y duración
        if (!enHorario(fecha, minutos(inicio), duracion)) {
            throw new ArgumentoException("La cita tiene que ser de lunes a viernes, entre las " + apertura + " y las " + cierre);
        }
        if (!libre(fecha, minutos(inicio), duracion, -1)) {
            throw new ArgumentoException("El hueco está ocupado");
        }
        if (posicionDe(cliente, fecha) != -1) {
            throw new ArgumentoException(cliente + " ya tiene cita ese día");
        }
        int pos = 0;
        while (pos < citas.size() && !vaAntes(nueva, citas.get(pos))) {
            pos++;
        }
        citas.add(pos, nueva);
    }

    /**
     * Elimina la cita de un cliente en una fecha.
     * Pre: el cliente no está vacío y tiene cita ese día.
     * Post: hay una cita menos y las demás siguen en el mismo orden.
     *
     * @param cliente nombre del cliente
     * @param fecha   día de la cita
     * @throws ArgumentoException si no se cumple la precondición
     */
    public void cancelar(String cliente, LocalDate fecha) throws ArgumentoException {
        citas.remove(buscar(cliente, fecha));
    }

    /**
     * Retrasa unos minutos la cita de un cliente, el mismo día y con la misma duración.
     * Pre: minutos mayor que 0, el cliente tiene cita ese día y el nuevo intervalo está en
     * horario y no pisa ninguna otra cita.
     * Post: es como cancelar y volver a reservar con el nuevo inicio. Las demás citas no cambian.
     *
     * @param cliente nombre del cliente
     * @param fecha   día de la cita
     * @param minutos minutos que se retrasa
     * @throws ArgumentoException si no se cumple la precondición
     */
    public void retrasar(String cliente, LocalDate fecha, int minutos) throws ArgumentoException {
        if (minutos <= 0) {
            throw new ArgumentoException("Los minutos deben ser mayores que 0");
        }
        int pos = buscar(cliente, fecha);
        Cita c = citas.get(pos);
        int nuevoInicio = minutos(c.getInicio()) + minutos;
        if (!libre(fecha, nuevoInicio, c.getDuracion(), pos)) {
            throw new ArgumentoException("La cita retrasada se sale del horario o pisa otra cita");
        }
        citas.remove(pos);
        reservar(c.getCliente(), fecha, LocalTime.of(nuevoInicio / 60, nuevoInicio % 60), c.getDuracion());
    }

    /**
     * Pre: ninguna. Post: devuelve el número de citas.
     *
     * @return número de citas
     */
    public int numCitas() {
        return citas.size();
    }

    /**
     * Devuelve la cita de una posición en orden cronológico.
     * Pre: i entre 0 y numCitas() - 1. Post: devuelve la cita de la posición i.
     *
     * @param i posición
     * @return la cita de esa posición
     * @throws ArgumentoException si la posición no existe
     */
    public Cita citaEn(int i) throws ArgumentoException {
        if (i < 0 || i >= citas.size()) {
            throw new ArgumentoException("No existe la posición " + i);
        }
        return citas.get(i);
    }

    /**
     * Pre: fecha no es null. Post: devuelve una lista nueva con las citas de ese día, en orden.
     *
     * @param fecha día
     * @return citas de ese día
     * @throws ArgumentoException si la fecha es null
     */
    public ArrayList<Cita> citasDel(LocalDate fecha) throws ArgumentoException {
        comprobarFecha(fecha);
        ArrayList<Cita> lista = new ArrayList<>();
        for (Cita c : citas) {
            if (c.getFecha().equals(fecha)) {
                lista.add(c);
            }
        }
        return lista;
    }

    /**
     * Pre: el cliente no está vacío. Post: devuelve true si tiene cita ese día.
     *
     * @param cliente nombre del cliente
     * @param fecha   día
     * @return true si tiene cita
     * @throws ArgumentoException si el cliente está vacío o la fecha es null
     */
    public boolean tieneCita(String cliente, LocalDate fecha) throws ArgumentoException {
        comprobarCliente(cliente);
        comprobarFecha(fecha);
        return posicionDe(cliente, fecha) != -1;
    }

    /**
     * Pre: el cliente tiene cita ese día. Post: devuelve esa cita (es única).
     *
     * @param cliente nombre del cliente
     * @param fecha   día
     * @return la cita del cliente ese día
     * @throws ArgumentoException si no se cumple la precondición
     */
    public Cita citaDe(String cliente, LocalDate fecha) throws ArgumentoException {
        return citas.get(buscar(cliente, fecha));
    }

    /**
     * Pre: el cliente no está vacío. Post: devuelve cuántas citas tiene en total.
     *
     * @param cliente nombre del cliente
     * @return número de citas del cliente
     * @throws ArgumentoException si el cliente está vacío
     */
    public int numCitasDe(String cliente) throws ArgumentoException {
        comprobarCliente(cliente);
        int contador = 0;
        for (Cita c : citas) {
            if (c.getCliente().equalsIgnoreCase(cliente)) {
                contador++;
            }
        }
        return contador;
    }

    /**
     * Pre: duracion mayor que 0. Post: devuelve true si el intervalo es de un día laborable,
     * está en horario y no se solapa con ninguna cita.
     *
     * @param fecha    día
     * @param inicio   hora de inicio
     * @param duracion duración en minutos
     * @return true si está libre
     * @throws ArgumentoException si algún dato es null o la duración no es mayor que 0
     */
    public boolean estaLibre(LocalDate fecha, LocalTime inicio, int duracion) throws ArgumentoException {
        comprobarFecha(fecha);
        if (inicio == null || duracion <= 0) {
            throw new ArgumentoException("Hora o duración no válidas");
        }
        return libre(fecha, minutos(inicio), duracion, -1);
    }

    /**
     * Pre: duracion mayor que 0. Post: devuelve true si existe una hora h con estaLibre(fecha, h, duracion).
     *
     * @param fecha    día
     * @param duracion duración en minutos
     * @return true si hay hueco
     * @throws ArgumentoException si la fecha es null o la duración no es mayor que 0
     */
    public boolean hayHueco(LocalDate fecha, int duracion) throws ArgumentoException {
        return primerMinutoLibre(fecha, duracion) != -1;
    }

    /**
     * Pre: duracion mayor que 0 y hayHueco(fecha, duracion).
     * Post: devuelve la menor hora h con estaLibre(fecha, h, duracion).
     *
     * @param fecha    día
     * @param duracion duración en minutos
     * @return hora del primer hueco
     * @throws ArgumentoException si no se cumple la precondición
     */
    public LocalTime primerHueco(LocalDate fecha, int duracion) throws ArgumentoException {
        int h = primerMinutoLibre(fecha, duracion);
        if (h == -1) {
            throw new ArgumentoException("No hay hueco de " + duracion + " minutos ese día");
        }
        return LocalTime.of(h / 60, h % 60);
    }

    /**
     * Pre: duracion entre 1 y getJornada().
     * Post: devuelve la menor fecha igual o posterior a desde con hayHueco.
     *
     * @param desde    primer día que se mira
     * @param duracion duración en minutos
     * @return primer día con hueco
     * @throws ArgumentoException si no se cumple la precondición
     */
    public LocalDate primerDiaConHueco(LocalDate desde, int duracion) throws ArgumentoException {
        if (duracion > getJornada()) {
            throw new ArgumentoException("La duración no puede superar la jornada");
        }
        LocalDate dia = desde;
        while (!hayHueco(dia, duracion)) {
            dia = dia.plusDays(1);
        }
        return dia;
    }

    /**
     * Pre: fecha es un día laborable. Post: devuelve getJornada() menos las duraciones de las citas de ese día.
     *
     * @param fecha día
     * @return minutos libres
     * @throws ArgumentoException si la fecha es null o es fin de semana
     */
    public int tiempoLibre(LocalDate fecha) throws ArgumentoException {
        comprobarFecha(fecha);
        if (!laborable(fecha)) {
            throw new ArgumentoException("Los fines de semana la clínica está cerrada");
        }
        int libres = getJornada();
        for (Cita c : citasDel(fecha)) {
            libres = libres - c.getDuracion();
        }
        return libres;
    }

    /**
     * Pre: ninguna. Post: devuelve las citas agrupadas por día, o "Agenda vacía".
     *
     * @return texto con todas las citas
     */
    @Override
    public String toString() {
        if (citas.isEmpty()) {
            return "Agenda vacía";
        }
        String texto = "";
        LocalDate anterior = null;
        for (Cita c : citas) {
            if (!c.getFecha().equals(anterior)) {
                LocalDate f = c.getFecha();
                texto = texto + String.format("%02d/%02d/%d", f.getDayOfMonth(), f.getMonthValue(), f.getYear()) + "\n";
                anterior = f;
            }
            texto = texto + "   " + c + "\n";
        }
        return texto;
    }

    // ---------- métodos auxiliares ----------

    // Pasa una hora a minutos desde las 00:00 (9:40 -> 580)
    private int minutos(LocalTime h) {
        return h.getHour() * 60 + h.getMinute();
    }

    private boolean laborable(LocalDate f) {
        return f.getDayOfWeek() != DayOfWeek.SATURDAY && f.getDayOfWeek() != DayOfWeek.SUNDAY;
    }

    private boolean enHorario(LocalDate f, int inicio, int duracion) {
        return laborable(f) && inicio >= minutos(apertura) && inicio + duracion <= minutos(cierre);
    }

    // La hora de fin no cuenta: 10:00-11:00 y 11:00-11:45 no se solapan
    private boolean solapa(Cita c, LocalDate f, int inicio, int duracion) {
        int ini = minutos(c.getInicio());
        return c.getFecha().equals(f) && ini < inicio + duracion && inicio < ini + c.getDuracion();
    }

    // En horario y sin solaparse con ninguna cita, salvo la de la posición ignorar (-1 = ninguna)
    private boolean libre(LocalDate f, int inicio, int duracion, int ignorar) {
        if (!enHorario(f, inicio, duracion)) {
            return false;
        }
        for (int i = 0; i < citas.size(); i++) {
            if (i != ignorar && solapa(citas.get(i), f, inicio, duracion)) {
                return false;
            }
        }
        return true;
    }

    // Primer minuto del día en el que cabe la duración, o -1 si no hay hueco
    private int primerMinutoLibre(LocalDate f, int duracion) throws ArgumentoException {
        comprobarFecha(f);
        if (duracion <= 0) {
            throw new ArgumentoException("La duración debe ser mayor que 0");
        }
        for (int h = minutos(apertura); h + duracion <= minutos(cierre); h++) {
            if (libre(f, h, duracion, -1)) {
                return h;
            }
        }
        return -1;
    }

    // Posición de la cita del cliente ese día (sin distinguir mayúsculas, D2), o -1
    private int posicionDe(String cliente, LocalDate fecha) {
        for (int i = 0; i < citas.size(); i++) {
            if (citas.get(i).getFecha().equals(fecha) && citas.get(i).getCliente().equalsIgnoreCase(cliente)) {
                return i;
            }
        }
        return -1;
    }

    // Como posicionDe, pero comprueba los datos y lanza excepción si no hay cita
    private int buscar(String cliente, LocalDate fecha) throws ArgumentoException {
        comprobarCliente(cliente);
        comprobarFecha(fecha);
        int pos = posicionDe(cliente, fecha);
        if (pos == -1) {
            throw new ArgumentoException(cliente + " no tiene cita ese día");
        }
        return pos;
    }

    private boolean vaAntes(Cita a, Cita b) {
        return a.getFecha().isBefore(b.getFecha())
                || (a.getFecha().equals(b.getFecha()) && a.getInicio().isBefore(b.getInicio()));
    }

    private void comprobarCliente(String cliente) throws ArgumentoException {
        if (cliente == null || cliente.trim().isEmpty()) {
            throw new ArgumentoException("El cliente no puede estar vacío");
        }
    }

    private void comprobarFecha(LocalDate fecha) throws ArgumentoException {
        if (fecha == null) {
            throw new ArgumentoException("La fecha no puede ser null");
        }
    }
}
