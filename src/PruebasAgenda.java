// Práctica 2 - Hito 1: pruebas del TAD Agenda adaptado
// Cada prueba imprime OK o FALLO y al final se muestra el total.
// Usamos CitaPrueba porque Cita es abstracta.

final LocalDate LUNES19 = LocalDate.of(2026, 10, 19);
final LocalDate MARTES20 = LocalDate.of(2026, 10, 20);
final LocalDate MIERCOLES21 = LocalDate.of(2026, 10, 21);
final LocalDate JUEVES22 = LocalDate.of(2026, 10, 22);
final LocalDate VIERNES23 = LocalDate.of(2026, 10, 23);
final LocalDate SABADO24 = LocalDate.of(2026, 10, 24);
final LocalDate DOMINGO25 = LocalDate.of(2026, 10, 25);

int pruebas = 0;
int fallos = 0;

void main() {
    try {
        pruebaEjemplo();
        pruebasPostcondiciones();
        pruebasPrecondiciones();
        pruebasCasosLimite();
        pruebasSerializable();
    } catch (Exception e) {
        fallos++;
        IO.println("Error inesperado: " + e.getMessage());
    }
    IO.println("\nPruebas: " + pruebas + "   Fallos: " + fallos);
}

void comprobar(String descripcion, boolean correcto) {
    pruebas++;
    if (correcto) {
        IO.println("OK     " + descripcion);
    } else {
        fallos++;
        IO.println("FALLO  " + descripcion);
    }
}

// Comprueba que se ha lanzado ArgumentoException y que la agenda no ha cambiado
void comprobarError(String descripcion, Exception e, Agenda agenda, String antes) {
    comprobar(descripcion + " (" + e.getMessage() + ")",
            e instanceof ArgumentoException && agenda.toString().equals(antes));
}

// Para no escribir LocalTime.of cada vez
Cita cita(String cliente, LocalDate fecha, int hora, int minuto, int duracion) throws ArgumentoException {
    return new CitaPrueba(cliente, fecha, LocalTime.of(hora, minuto), duracion);
}

// Agenda con el horario de la práctica 1 (9:00 a 14:00) para reutilizar sus ejemplos
Agenda agenda9a14() throws ArgumentoException {
    return new Agenda(LocalTime.of(9, 0), LocalTime.of(14, 0));
}

// Nombres de los clientes de una lista separados por comas
String clientes(ArrayList<Cita> lista) {
    String texto = "";
    for (Cita c : lista) {
        if (!texto.isEmpty()) {
            texto = texto + ", ";
        }
        texto = texto + c.getCliente();
    }
    return texto;
}

void pruebaEjemplo() throws Exception {
    IO.println("--- Ejemplo del enunciado ---");
    Agenda agenda = agenda9a14();
    agenda.reservar(cita("Bruno", MARTES20, 10, 0, 60));
    agenda.reservar(cita("Ana", MARTES20, 9, 0, 40));
    agenda.reservar(cita("David", MARTES20, 12, 30, 60));
    agenda.reservar(cita("Carmen", MARTES20, 11, 15, 45));
    agenda.reservar(cita("Ana", LUNES19, 13, 0, 30));
    agenda.reservar(cita("Elena", MIERCOLES21, 9, 0, 300));
    IO.println(agenda);

    comprobar("Orden: Ana (lunes), Ana, Bruno, Carmen, David, Elena",
            agenda.getCitaEn(0).getFecha().equals(LUNES19) && agenda.getCitaEn(1).getCliente().equals("Ana")
                    && agenda.getCitaEn(2).getCliente().equals("Bruno") && agenda.getCitaEn(5).getCliente().equals("Elena"));
    comprobar("Citas del martes 20", clientes(agenda.getCitasDel(MARTES20)).equals("Ana, Bruno, Carmen, David"));
    comprobar("Minutos libres martes 20 = 95 y miércoles 21 = 0",
            agenda.getTiempoLibre(MARTES20) == 95 && agenda.getTiempoLibre(MIERCOLES21) == 0);
    comprobar("Libre el martes 20 a las 9:40 durante 20 min", agenda.estaLibre(MARTES20, LocalTime.of(9, 40), 20));
    comprobar("No libre el martes 20 a las 10:30 durante 15 min", !agenda.estaLibre(MARTES20, LocalTime.of(10, 30), 15));
    comprobar("La agenda abre el sábado 24 (los domingos los cierra la clínica)", agenda.estaLibre(SABADO24, LocalTime.of(10, 0), 30));
    comprobar("Primer hueco de 30 min el martes 20 = 12:00", agenda.primerHueco(MARTES20, 30).equals(LocalTime.of(12, 0)));
    comprobar("No hay hueco de 45 min el martes 20", !agenda.hayHueco(MARTES20, 45));
    comprobar("Primer día con hueco de 45 min desde el martes 20 = jueves 22",
            agenda.primerDiaConHueco(MARTES20, 45).equals(JUEVES22));
    comprobar("Primer día con hueco de 30 min desde el sábado 24 = sábado 24",
            agenda.primerDiaConHueco(SABADO24, 30).equals(SABADO24));

    String antes = agenda.toString();
    try {
        agenda.reservar(cita("Ana", MARTES20, 13, 30, 30));
        comprobar("Otra cita para Ana el martes 20 no es posible", false);
    } catch (Exception e) {
        comprobarError("Otra cita para Ana el martes 20 no es posible", e, agenda, antes);
    }

    agenda.cancelar("Bruno", MARTES20);
    comprobar("Tras cancelar a Bruno, primer hueco de 45 min = 9:40",
            agenda.primerHueco(MARTES20, 45).equals(LocalTime.of(9, 40)));
}

void pruebasPostcondiciones() throws Exception {
    IO.println("\n--- Postcondiciones ---");
    Agenda agenda = new Agenda(LocalTime.of(9, 0), LocalTime.of(20, 0));
    comprobar("Agenda(9:00, 20:00): sin citas, getters y jornada de 660 min",
            agenda.getNumCitas() == 0 && agenda.toString().equals("Agenda vacía")
                    && agenda.getApertura().equals(LocalTime.of(9, 0)) && agenda.getCierre().equals(LocalTime.of(20, 0))
                    && agenda.getJornada() == 660);
    comprobar("Con horario 9-20 cabe 19:30-20:00 pero no 19:31-20:01",
            agenda.estaLibre(MARTES20, LocalTime.of(19, 30), 30) && !agenda.estaLibre(MARTES20, LocalTime.of(19, 31), 30));

    agenda = agenda9a14();

    Cita c = cita("Ana", MARTES20, 9, 0, 40);
    comprobar("Cita: getters, getFin y toString",
            c.getCliente().equals("Ana") && c.getDuracion() == 40 && c.getFin().equals(LocalTime.of(9, 40))
                    && c.toString().equals("09:00-09:40 Ana (40 min)"));

    Cita copia = c.conInicio(LocalTime.of(10, 0));
    comprobar("conInicio: cita nueva del mismo tipo y la original no cambia",
            copia instanceof CitaPrueba && copia.getInicio().equals(LocalTime.of(10, 0))
                    && copia.getDuracion() == 40 && c.getInicio().equals(LocalTime.of(9, 0)));

    agenda.reservar(cita("Ana", MARTES20, 9, 0, 40));
    agenda.reservar(cita("Bruno", MARTES20, 11, 0, 60));
    agenda.reservar(cita("Carmen", MARTES20, 10, 0, 30));
    comprobar("reservar: una cita más, en su sitio y las demás igual",
            agenda.getNumCitas() == 3 && clientes(agenda.getCitasDel(MARTES20)).equals("Ana, Carmen, Bruno"));

    comprobar("tieneCita y getCitaDe no distinguen mayúsculas",
            agenda.tieneCita("ANA", MARTES20) && agenda.getCitaDe("bruno", MARTES20).getInicio().equals(LocalTime.of(11, 0)));
    comprobar("tieneCita: false si no tiene cita ese día", !agenda.tieneCita("Ana", MIERCOLES21));
    comprobar("getCitasDel: vacía si no hay citas ese día", agenda.getCitasDel(JUEVES22).isEmpty());
    agenda.getCitasDel(MARTES20).clear();
    comprobar("getCitasDel: devuelve una lista nueva", agenda.getNumCitas() == 3);
    comprobar("getTiempoLibre: día sin citas = 300", agenda.getTiempoLibre(VIERNES23) == 300);
    comprobar("primerDiaConHueco: devuelve el mismo día si tiene hueco", agenda.primerDiaConHueco(MARTES20, 60).equals(MARTES20));

    agenda.reservar(cita("Diego", LUNES19, 12, 0, 30));
    agenda.reservar(cita("Elena", MIERCOLES21, 9, 0, 30));
    agenda.reservar(cita("Fran", JUEVES22, 9, 0, 30));
    comprobar("getCitasEntre: ambas fechas incluidas y en orden",
            clientes(agenda.getCitasEntre(MARTES20, MIERCOLES21)).equals("Ana, Carmen, Bruno, Elena"));
    comprobar("getCitasEntre: un solo día y periodo sin citas",
            clientes(agenda.getCitasEntre(LUNES19, LUNES19)).equals("Diego")
                    && agenda.getCitasEntre(VIERNES23, DOMINGO25).isEmpty());

    agenda.cancelar("carmen", MARTES20);
    comprobar("cancelar: una cita menos y las demás en el mismo orden",
            agenda.getNumCitas() == 5 && clientes(agenda.getCitasDel(MARTES20)).equals("Ana, Bruno"));

    agenda.retrasar("bruno", MARTES20, 30);
    Cita bruno = agenda.getCitaDe("Bruno", MARTES20);
    comprobar("retrasar: mismo número de citas, inicio + minutos, misma duración y mismo nombre",
            agenda.getNumCitas() == 5 && bruno.getInicio().equals(LocalTime.of(11, 30))
                    && bruno.getDuracion() == 60 && bruno.getCliente().equals("Bruno"));
    comprobar("retrasar: la cita sigue siendo del mismo tipo (polimorfismo)", bruno instanceof CitaPrueba);

    Agenda salto = agenda9a14();
    salto.reservar(cita("Ana", MARTES20, 9, 0, 30));
    salto.reservar(cita("Bruno", MARTES20, 9, 30, 30));
    salto.retrasar("Ana", MARTES20, 60);
    comprobar("retrasar: la cita puede saltar por encima de otra", clientes(salto.getCitasDel(MARTES20)).equals("Bruno, Ana"));
}

void pruebasPrecondiciones() throws Exception {
    IO.println("\n--- Precondiciones ---");
    try {
        new Agenda(LocalTime.of(14, 0), LocalTime.of(9, 0));
        comprobar("Agenda con apertura después del cierre", false);
    } catch (ArgumentoException e) {
        comprobar("Agenda con apertura después del cierre (" + e.getMessage() + ")", true);
    }
    try {
        new Agenda(null, LocalTime.of(14, 0));
        comprobar("Agenda con apertura null", false);
    } catch (ArgumentoException e) {
        comprobar("Agenda con apertura null (" + e.getMessage() + ")", true);
    }
    try {
        cita("  ", MARTES20, 9, 0, 30);
        comprobar("Cita con cliente vacío", false);
    } catch (ArgumentoException e) {
        comprobar("Cita con cliente vacío (" + e.getMessage() + ")", true);
    }
    try {
        cita("Ana", MARTES20, 9, 0, 0);
        comprobar("Cita con duración 0", false);
    } catch (ArgumentoException e) {
        comprobar("Cita con duración 0 (" + e.getMessage() + ")", true);
    }
    try {
        cita("Ana", MARTES20, 23, 30, 60);
        comprobar("Cita que pasa de medianoche", false);
    } catch (ArgumentoException e) {
        comprobar("Cita que pasa de medianoche (" + e.getMessage() + ")", true);
    }

    Agenda agenda = agenda9a14();
    agenda.reservar(cita("Ana", MARTES20, 9, 0, 40));
    agenda.reservar(cita("Bruno", MARTES20, 10, 0, 60));
    String antes = agenda.toString();

    try {
        agenda.reservar(null);
        comprobar("reservar null", false);
    } catch (Exception e) {
        comprobarError("reservar null", e, agenda, antes);
    }
    try {
        agenda.reservar(cita("Carmen", MARTES20, 8, 30, 30));
        comprobar("reservar antes de la apertura", false);
    } catch (Exception e) {
        comprobarError("reservar antes de la apertura", e, agenda, antes);
    }
    try {
        agenda.reservar(cita("Carmen", MARTES20, 13, 30, 60));
        comprobar("reservar acabando después del cierre", false);
    } catch (Exception e) {
        comprobarError("reservar acabando después del cierre", e, agenda, antes);
    }
    try {
        agenda.reservar(cita("Carmen", MARTES20, 10, 30, 15));
        comprobar("reservar en un hueco ocupado", false);
    } catch (Exception e) {
        comprobarError("reservar en un hueco ocupado", e, agenda, antes);
    }
    try {
        agenda.reservar(cita("ana", MARTES20, 12, 0, 30));
        comprobar("reservar a \"ana\" si Ana ya tiene cita ese día", false);
    } catch (Exception e) {
        comprobarError("reservar a \"ana\" si Ana ya tiene cita ese día", e, agenda, antes);
    }
    try {
        agenda.cancelar("Ana", MIERCOLES21);
        comprobar("cancelar una cita que no existe", false);
    } catch (Exception e) {
        comprobarError("cancelar una cita que no existe", e, agenda, antes);
    }
    try {
        agenda.retrasar("Ana", MARTES20, 0);
        comprobar("retrasar 0 minutos", false);
    } catch (Exception e) {
        comprobarError("retrasar 0 minutos", e, agenda, antes);
    }
    try {
        agenda.retrasar("Carmen", MARTES20, 15);
        comprobar("retrasar una cita que no existe", false);
    } catch (Exception e) {
        comprobarError("retrasar una cita que no existe", e, agenda, antes);
    }
    try {
        agenda.retrasar("Ana", MARTES20, 30);
        comprobar("retrasar pisando la siguiente (D3)", false);
    } catch (Exception e) {
        comprobarError("retrasar pisando la siguiente (D3)", e, agenda, antes);
    }
    try {
        agenda.retrasar("Bruno", MARTES20, 240);
        comprobar("retrasar saliéndose del horario", false);
    } catch (Exception e) {
        comprobarError("retrasar saliéndose del horario", e, agenda, antes);
    }
    try {
        agenda.getCitaEn(2);
        comprobar("getCitaEn de una posición que no existe", false);
    } catch (Exception e) {
        comprobarError("getCitaEn de una posición que no existe", e, agenda, antes);
    }
    try {
        agenda.getCitaDe("Ana", MIERCOLES21);
        comprobar("getCitaDe sin cita ese día", false);
    } catch (Exception e) {
        comprobarError("getCitaDe sin cita ese día", e, agenda, antes);
    }
    try {
        agenda.getCitasEntre(MIERCOLES21, MARTES20);
        comprobar("getCitasEntre con desde posterior a hasta", false);
    } catch (Exception e) {
        comprobarError("getCitasEntre con desde posterior a hasta", e, agenda, antes);
    }
    try {
        agenda.estaLibre(MARTES20, LocalTime.of(12, 0), 0);
        comprobar("estaLibre con duración 0", false);
    } catch (Exception e) {
        comprobarError("estaLibre con duración 0", e, agenda, antes);
    }
    try {
        agenda.primerHueco(MARTES20, 300);
        comprobar("primerHueco en un día sin hueco", false);
    } catch (Exception e) {
        comprobarError("primerHueco en un día sin hueco", e, agenda, antes);
    }
    try {
        agenda.primerDiaConHueco(MARTES20, 301);
        comprobar("primerDiaConHueco con más minutos que la jornada", false);
    } catch (Exception e) {
        comprobarError("primerDiaConHueco con más minutos que la jornada", e, agenda, antes);
    }
    try {
        agenda.getTiempoLibre(null);
        comprobar("getTiempoLibre con fecha null", false);
    } catch (Exception e) {
        comprobarError("getTiempoLibre con fecha null", e, agenda, antes);
    }
}

void pruebasCasosLimite() throws Exception {
    IO.println("\n--- Casos límite ---");

    // Orden
    Agenda agenda = agenda9a14();
    agenda.reservar(cita("Bruno", MARTES20, 11, 0, 30));
    agenda.reservar(cita("Ana", MARTES20, 9, 0, 30));    // al principio
    agenda.reservar(cita("Carmen", MARTES20, 13, 0, 30)); // al final
    agenda.reservar(cita("David", MARTES20, 10, 0, 30));  // en medio
    comprobar("Reservar al principio, al final y en medio",
            clientes(agenda.getCitasDel(MARTES20)).equals("Ana, David, Bruno, Carmen"));
    agenda.reservar(cita("Elena", LUNES19, 13, 30, 30));
    agenda.reservar(cita("Fran", MIERCOLES21, 9, 0, 30));
    comprobar("Fecha anterior con hora posterior va delante, y al revés",
            agenda.getCitaEn(0).getCliente().equals("Elena") && agenda.getCitaEn(5).getCliente().equals("Fran"));
    agenda.reservar(cita("Gema", JUEVES22, 9, 0, 30));
    comprobar("Misma hora en días distintos", agenda.getCitaEn(6).getCliente().equals("Gema"));
    agenda.reservar(cita("Hugo", MARTES20, 9, 30, 30));
    comprobar("Citas contiguas (9:00-9:30 y 9:30-10:00)", agenda.tieneCita("Hugo", MARTES20));
    agenda.reservar(cita("Iris", MARTES20, 13, 30, 30));
    comprobar("Cita que termina justo al cierre", agenda.getCitaDe("Iris", MARTES20).getFin().equals(agenda.getCierre()));

    // Fechas
    comprobar("La agenda abre sábados y domingos (el domingo lo cierra la clínica)",
            agenda.estaLibre(SABADO24, LocalTime.of(10, 0), 30) && agenda.estaLibre(DOMINGO25, LocalTime.of(10, 0), 30));
    agenda.reservar(cita("Ana", MIERCOLES21, 10, 0, 30));
    comprobar("Cliente con citas en dos días", agenda.tieneCita("Ana", MARTES20) && agenda.tieneCita("Ana", MIERCOLES21));
    agenda.retrasar("Ana", MIERCOLES21, 60);
    agenda.cancelar("Ana", MARTES20);
    comprobar("Retrasar y cancelar solo afectan a la cita de ese día",
            !agenda.tieneCita("Ana", MARTES20) && agenda.getCitaDe("Ana", MIERCOLES21).getInicio().equals(LocalTime.of(11, 0)));
    Agenda otra = agenda9a14();
    otra.reservar(cita("Bruno", MARTES20, 13, 0, 30));
    otra.reservar(cita("Carmen", MIERCOLES21, 9, 0, 60));
    otra.retrasar("Bruno", MARTES20, 30);
    comprobar("Retrasar la última cita del día cuando la siguiente es de otro día",
            otra.getCitaEn(0).getInicio().equals(LocalTime.of(13, 30)));
    Agenda llena = agenda9a14();
    llena.reservar(cita("Elena", VIERNES23, 9, 0, 300));
    comprobar("Primer día con hueco tras un viernes lleno = sábado",
            llena.primerDiaConHueco(VIERNES23, 60).equals(SABADO24));

    // Huecos
    Agenda huecos = agenda9a14();
    huecos.reservar(cita("Ana", JUEVES22, 10, 0, 60));
    huecos.reservar(cita("Bruno", JUEVES22, 12, 0, 60));
    comprobar("Hueco al principio del día (9:00)", huecos.primerHueco(JUEVES22, 60).equals(LocalTime.of(9, 0)));
    huecos.reservar(cita("Carmen", JUEVES22, 9, 0, 60));
    comprobar("Hueco entre dos citas (11:00)", huecos.primerHueco(JUEVES22, 60).equals(LocalTime.of(11, 0)));
    huecos.reservar(cita("David", JUEVES22, 11, 0, 60));
    comprobar("Hueco al final del día (13:00)", huecos.primerHueco(JUEVES22, 60).equals(LocalTime.of(13, 0)));
    huecos.reservar(cita("Elena", JUEVES22, 13, 0, 60));
    comprobar("Día sin hueco", !huecos.hayHueco(JUEVES22, 1) && huecos.getTiempoLibre(JUEVES22) == 0);
}

void pruebasSerializable() throws Exception {
    IO.println("\n--- Serializable ---");
    Agenda agenda = agenda9a14();
    agenda.reservar(cita("Ana", MARTES20, 9, 0, 40));
    agenda.reservar(cita("Bruno", MIERCOLES21, 10, 0, 60));

    // La escribimos en memoria (como si fuera un fichero) y la volvemos a leer
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    ObjectOutputStream salida = new ObjectOutputStream(bytes);
    salida.writeObject(agenda);
    salida.close();
    ObjectInputStream entrada = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()));
    Agenda copia = (Agenda) entrada.readObject();
    entrada.close();

    comprobar("Una agenda se puede guardar y volver a leer igual",
            copia.toString().equals(agenda.toString()) && copia.getCierre().equals(agenda.getCierre()));
    comprobar("Las citas leídas mantienen su tipo", copia.getCitaEn(0) instanceof CitaPrueba);
}
