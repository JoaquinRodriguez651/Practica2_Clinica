// Práctica 1 - Parte 3: pruebas del TAD Agenda
// Cada prueba imprime OK o FALLO y al final se muestra el total.

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
    agenda.reservar("Bruno", MARTES20, LocalTime.of(10, 0), 60);
    agenda.reservar("Ana", MARTES20, LocalTime.of(9, 0), 40);
    agenda.reservar("David", MARTES20, LocalTime.of(12, 30), 60);
    agenda.reservar("Carmen", MARTES20, LocalTime.of(11, 15), 45);
    agenda.reservar("Ana", LUNES19, LocalTime.of(13, 0), 30);
    agenda.reservar("Elena", MIERCOLES21, LocalTime.of(9, 0), 300);
    IO.println(agenda);

    comprobar("Orden: Ana (lunes), Ana, Bruno, Carmen, David, Elena",
            agenda.citaEn(0).getFecha().equals(LUNES19) && agenda.citaEn(1).getCliente().equals("Ana")
                    && agenda.citaEn(2).getCliente().equals("Bruno") && agenda.citaEn(5).getCliente().equals("Elena"));
    comprobar("Número de citas de Ana = 2", agenda.numCitasDe("Ana") == 2);
    comprobar("Citas del martes 20", clientes(agenda.citasDel(MARTES20)).equals("Ana, Bruno, Carmen, David"));
    comprobar("Minutos libres martes 20 = 95 y miércoles 21 = 0",
            agenda.tiempoLibre(MARTES20) == 95 && agenda.tiempoLibre(MIERCOLES21) == 0);
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
        agenda.reservar("Ana", MARTES20, LocalTime.of(13, 30), 30);
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
            agenda.numCitas() == 0 && agenda.toString().equals("Agenda vacía")
                    && agenda.getApertura().equals(LocalTime.of(9, 0)) && agenda.getCierre().equals(LocalTime.of(20, 0))
                    && agenda.getJornada() == 660);
    comprobar("Con horario 9-20 cabe 19:30-20:00 pero no 19:31-20:01",
            agenda.estaLibre(MARTES20, LocalTime.of(19, 30), 30) && !agenda.estaLibre(MARTES20, LocalTime.of(19, 31), 30));

    agenda = agenda9a14();

    Cita c = new Cita("Ana", MARTES20, LocalTime.of(9, 0), 40);
    comprobar("Cita: getters, fin y toString",
            c.getCliente().equals("Ana") && c.getDuracion() == 40 && c.fin().equals(LocalTime.of(9, 40))
                    && c.toString().equals("09:00-09:40 Ana (40 min)"));

    agenda.reservar("Ana", MARTES20, LocalTime.of(9, 0), 40);
    agenda.reservar("Bruno", MARTES20, LocalTime.of(11, 0), 60);
    agenda.reservar("Carmen", MARTES20, LocalTime.of(10, 0), 30);
    comprobar("reservar: una cita más, en su sitio y las demás igual",
            agenda.numCitas() == 3 && clientes(agenda.citasDel(MARTES20)).equals("Ana, Carmen, Bruno"));

    comprobar("tieneCita y citaDe no distinguen mayúsculas",
            agenda.tieneCita("ANA", MARTES20) && agenda.citaDe("bruno", MARTES20).getInicio().equals(LocalTime.of(11, 0)));
    comprobar("tieneCita: false si no tiene cita ese día", !agenda.tieneCita("Ana", MIERCOLES21));
    comprobar("citasDel: vacía si no hay citas ese día", agenda.citasDel(JUEVES22).isEmpty());
    agenda.citasDel(MARTES20).clear();
    comprobar("citasDel: devuelve una lista nueva", agenda.numCitas() == 3);
    comprobar("tiempoLibre: día sin citas = 300", agenda.tiempoLibre(VIERNES23) == 300);
    comprobar("primerDiaConHueco: devuelve el mismo día si tiene hueco", agenda.primerDiaConHueco(MARTES20, 60).equals(MARTES20));

    agenda.cancelar("carmen", MARTES20);
    comprobar("cancelar: una cita menos y las demás en el mismo orden",
            agenda.numCitas() == 2 && clientes(agenda.citasDel(MARTES20)).equals("Ana, Bruno"));

    agenda.retrasar("bruno", MARTES20, 30);
    Cita bruno = agenda.citaDe("Bruno", MARTES20);
    comprobar("retrasar: mismo número de citas, inicio + minutos, misma duración y mismo nombre",
            agenda.numCitas() == 2 && bruno.getInicio().equals(LocalTime.of(11, 30))
                    && bruno.getDuracion() == 60 && bruno.getCliente().equals("Bruno"));

    Agenda salto = agenda9a14();
    salto.reservar("Ana", MARTES20, LocalTime.of(9, 0), 30);
    salto.reservar("Bruno", MARTES20, LocalTime.of(9, 30), 30);
    salto.retrasar("Ana", MARTES20, 60);
    comprobar("retrasar: la cita puede saltar por encima de otra", clientes(salto.citasDel(MARTES20)).equals("Bruno, Ana"));
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
        new Cita("Ana", MARTES20, LocalTime.of(23, 30), 60);
        comprobar("Cita que pasa de medianoche", false);
    } catch (ArgumentoException e) {
        comprobar("Cita que pasa de medianoche (" + e.getMessage() + ")", true);
    }

    Agenda agenda = agenda9a14();
    agenda.reservar("Ana", MARTES20, LocalTime.of(9, 0), 40);
    agenda.reservar("Bruno", MARTES20, LocalTime.of(10, 0), 60);
    String antes = agenda.toString();

    try {
        agenda.reservar("  ", MARTES20, LocalTime.of(12, 0), 30);
        comprobar("reservar con cliente vacío", false);
    } catch (Exception e) {
        comprobarError("reservar con cliente vacío", e, agenda, antes);
    }
    try {
        agenda.reservar("Carmen", MARTES20, LocalTime.of(12, 0), 0);
        comprobar("reservar con duración 0", false);
    } catch (Exception e) {
        comprobarError("reservar con duración 0", e, agenda, antes);
    }
    try {
        agenda.reservar("Carmen", MARTES20, LocalTime.of(13, 30), 60);
        comprobar("reservar acabando después del cierre", false);
    } catch (Exception e) {
        comprobarError("reservar acabando después del cierre", e, agenda, antes);
    }
    try {
        agenda.reservar("Carmen", MARTES20, LocalTime.of(10, 30), 15);
        comprobar("reservar en un hueco ocupado", false);
    } catch (Exception e) {
        comprobarError("reservar en un hueco ocupado", e, agenda, antes);
    }
    try {
        agenda.reservar("ana", MARTES20, LocalTime.of(12, 0), 30);
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
        agenda.citaEn(2);
        comprobar("citaEn de una posición que no existe", false);
    } catch (Exception e) {
        comprobarError("citaEn de una posición que no existe", e, agenda, antes);
    }
    try {
        agenda.citaDe("Ana", MIERCOLES21);
        comprobar("citaDe sin cita ese día", false);
    } catch (Exception e) {
        comprobarError("citaDe sin cita ese día", e, agenda, antes);
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
        agenda.tiempoLibre(null);
        comprobar("tiempoLibre con fecha null", false);
    } catch (Exception e) {
        comprobarError("tiempoLibre con fecha null", e, agenda, antes);
    }
}

void pruebasCasosLimite() throws Exception {
    IO.println("\n--- Casos límite ---");

    // Orden
    Agenda agenda = agenda9a14();
    agenda.reservar("Bruno", MARTES20, LocalTime.of(11, 0), 30);
    agenda.reservar("Ana", MARTES20, LocalTime.of(9, 0), 30);    // al principio
    agenda.reservar("Carmen", MARTES20, LocalTime.of(13, 0), 30); // al final
    agenda.reservar("David", MARTES20, LocalTime.of(10, 0), 30);  // en medio
    comprobar("Reservar al principio, al final y en medio",
            clientes(agenda.citasDel(MARTES20)).equals("Ana, David, Bruno, Carmen"));
    agenda.reservar("Elena", LUNES19, LocalTime.of(13, 30), 30);
    agenda.reservar("Fran", MIERCOLES21, LocalTime.of(9, 0), 30);
    comprobar("Fecha anterior con hora posterior va delante, y al revés",
            agenda.citaEn(0).getCliente().equals("Elena") && agenda.citaEn(5).getCliente().equals("Fran"));
    agenda.reservar("Gema", JUEVES22, LocalTime.of(9, 0), 30);
    comprobar("Misma hora en días distintos", agenda.citaEn(6).getCliente().equals("Gema"));
    agenda.reservar("Hugo", MARTES20, LocalTime.of(9, 30), 30);
    comprobar("Citas contiguas (9:00-9:30 y 9:30-10:00)", agenda.tieneCita("Hugo", MARTES20));
    agenda.reservar("Iris", MARTES20, LocalTime.of(13, 30), 30);
    comprobar("Cita que termina justo al cierre", agenda.citaDe("Iris", MARTES20).fin().equals(agenda.getCierre()));

    // Fechas
    comprobar("La agenda abre sábados y domingos (el domingo lo cierra la clínica)",
            agenda.estaLibre(SABADO24, LocalTime.of(10, 0), 30) && agenda.estaLibre(DOMINGO25, LocalTime.of(10, 0), 30));
    agenda.reservar("Ana", MIERCOLES21, LocalTime.of(10, 0), 30);
    comprobar("Cliente con citas en dos días", agenda.numCitasDe("Ana") == 2);
    agenda.retrasar("Ana", MIERCOLES21, 60);
    agenda.cancelar("Ana", MARTES20);
    comprobar("Retrasar y cancelar solo afectan a la cita de ese día",
            !agenda.tieneCita("Ana", MARTES20) && agenda.citaDe("Ana", MIERCOLES21).getInicio().equals(LocalTime.of(11, 0)));
    Agenda otra = agenda9a14();
    otra.reservar("Bruno", MARTES20, LocalTime.of(13, 0), 30);
    otra.reservar("Carmen", MIERCOLES21, LocalTime.of(9, 0), 60);
    otra.retrasar("Bruno", MARTES20, 30);
    comprobar("Retrasar la última cita del día cuando la siguiente es de otro día",
            otra.citaEn(0).getInicio().equals(LocalTime.of(13, 30)));
    Agenda llena = agenda9a14();
    llena.reservar("Elena", VIERNES23, LocalTime.of(9, 0), 300);
    comprobar("Primer día con hueco tras un viernes lleno = sábado",
            llena.primerDiaConHueco(VIERNES23, 60).equals(SABADO24));

    // Huecos
    Agenda huecos = agenda9a14();
    huecos.reservar("Ana", JUEVES22, LocalTime.of(10, 0), 60);
    huecos.reservar("Bruno", JUEVES22, LocalTime.of(12, 0), 60);
    comprobar("Hueco al principio del día (9:00)", huecos.primerHueco(JUEVES22, 60).equals(LocalTime.of(9, 0)));
    huecos.reservar("Carmen", JUEVES22, LocalTime.of(9, 0), 60);
    comprobar("Hueco entre dos citas (11:00)", huecos.primerHueco(JUEVES22, 60).equals(LocalTime.of(11, 0)));
    huecos.reservar("David", JUEVES22, LocalTime.of(11, 0), 60);
    comprobar("Hueco al final del día (13:00)", huecos.primerHueco(JUEVES22, 60).equals(LocalTime.of(13, 0)));
    huecos.reservar("Elena", JUEVES22, LocalTime.of(13, 0), 60);
    comprobar("Día sin hueco", !huecos.hayHueco(JUEVES22, 1) && huecos.tiempoLibre(JUEVES22) == 0);
}
