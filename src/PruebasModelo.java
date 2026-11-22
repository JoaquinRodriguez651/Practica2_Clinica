// Práctica 2 - Hito 2: pruebas de las mascotas, los veterinarios y las citas
// Cada prueba imprime OK o FALLO y al final se muestra el total.
// La fecha actual es fija (viernes 16/10/2026 a las 10:00, como en el enunciado)
// para que las pruebas no dependan del día en que se ejecutan.

final LocalDate HOY = LocalDate.of(2026, 10, 16);
final LocalDateTime AHORA = LocalDateTime.of(2026, 10, 16, 10, 0);
final LocalDate LUNES19 = LocalDate.of(2026, 10, 19);
final LocalDate MARTES20 = LocalDate.of(2026, 10, 20);
final LocalDate SABADO24 = LocalDate.of(2026, 10, 24);
final LocalDate DOMINGO25 = LocalDate.of(2026, 10, 25);

int pruebas = 0;
int fallos = 0;

void main() {
    try {
        pruebasEnumerados();
        pruebasMascota();
        pruebasEdad();
        pruebasHorarioClinica();
        pruebasVeterinario();
        pruebasPuedeAtender();
        pruebasConsulta();
        pruebasCirugia();
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

// Mascotas del enunciado (el peso va en gramos)
Mascota toby() throws ArgumentoException {
    return new Mascota("M001", "Toby", "Ana García", Especie.PERRO, LocalDate.of(2020, 3, 15), 28000);
}

Mascota misi() throws ArgumentoException {
    return new Mascota("M002", "Misi", "Bruno López", Especie.GATO, LocalDate.of(2022, 6, 1), 4000);
}

Mascota kiwi() throws ArgumentoException {
    return new Mascota("M003", "Kiwi", "Carla Ruiz", Especie.EXOTICO, LocalDate.of(2024, 1, 10), 1800);
}

Mascota luna() throws ArgumentoException {
    return new Mascota("M004", "Luna", "Diego Martín", Especie.PERRO, LocalDate.of(2025, 2, 20), 8000);
}

// Veterinarios del enunciado
Veterinario laura() throws ArgumentoException {
    return new Veterinario("V1", "Laura Martín", Especialidad.GENERAL);
}

Veterinario pablo() throws ArgumentoException {
    return new Veterinario("V2", "Pablo Ortega", Especialidad.CIRUGIA);
}

Veterinario elena() throws ArgumentoException {
    return new Veterinario("V3", "Elena Sanz", Especialidad.EXOTICOS);
}

void pruebasEnumerados() {
    IO.println("--- Enumerados ---");
    comprobar("Especialidad tiene GENERAL, CIRUGIA y EXOTICOS",
            Especialidad.values().length == 3 && Especialidad.valueOf("EXOTICOS") == Especialidad.EXOTICOS);
    comprobar("Perro: consulta de 30 min con un generalista",
            Especie.PERRO.getNombre().equals("Perro") && Especie.PERRO.getDuracionConsulta() == 30
                    && Especie.PERRO.getEspecialidad() == Especialidad.GENERAL);
    comprobar("Gato: consulta de 30 min con un generalista",
            Especie.GATO.getDuracionConsulta() == 30 && Especie.GATO.getEspecialidad() == Especialidad.GENERAL);
    comprobar("Exótico: consulta de 45 min con un especialista en exóticos",
            Especie.EXOTICO.getDuracionConsulta() == 45 && Especie.EXOTICO.getEspecialidad() == Especialidad.EXOTICOS);
    comprobar("toString de Especie devuelve el nombre con tilde", Especie.EXOTICO.toString().equals("Exótico"));
}

void pruebasMascota() throws Exception {
    IO.println("\n--- Mascota ---");
    Mascota m = toby();
    comprobar("Getters de Toby",
            m.getId().equals("M001") && m.getNombre().equals("Toby") && m.getPropietario().equals("Ana García")
                    && m.getEspecie() == Especie.PERRO && m.getNacimiento().equals(LocalDate.of(2020, 3, 15))
                    && m.getPeso() == 28000);
    comprobar("toString de Toby", m.toString().equals("M001 Toby (Perro) - Ana García"));
    try {
        new Mascota("  ", "Toby", "Ana García", Especie.PERRO, LocalDate.of(2020, 3, 15), 28000);
        comprobar("Mascota con id vacío", false);
    } catch (ArgumentoException e) {
        comprobar("Mascota con id vacío (" + e.getMessage() + ")", true);
    }
    try {
        new Mascota("M005", "Rex", "Eva Gil", null, LocalDate.of(2020, 3, 15), 28000);
        comprobar("Mascota sin especie", false);
    } catch (ArgumentoException e) {
        comprobar("Mascota sin especie (" + e.getMessage() + ")", true);
    }
    try {
        new Mascota("M005", "Rex", "Eva Gil", Especie.PERRO, LocalDate.of(2020, 3, 15), 0);
        comprobar("Mascota con peso 0", false);
    } catch (ArgumentoException e) {
        comprobar("Mascota con peso 0 (" + e.getMessage() + ")", true);
    }
}

void pruebasEdad() throws Exception {
    IO.println("\n--- Edad ---");
    comprobar("Toby (15/03/2020) tiene 6 años el 16/10/2026", toby().getEdad(HOY) == 6);
    comprobar("El día antes de su cumpleaños todavía tiene 5", toby().getEdad(LocalDate.of(2026, 3, 14)) == 5);
    comprobar("El día de su cumpleaños ya tiene 6", toby().getEdad(LocalDate.of(2026, 3, 15)) == 6);
    comprobar("Luna (20/02/2025) tiene 1 año y Kiwi (10/01/2024) 2",
            luna().getEdad(HOY) == 1 && kiwi().getEdad(HOY) == 2);
    comprobar("Una mascota nacida hoy tiene 0 años",
            new Mascota("M009", "Bebé", "Eva Gil", Especie.GATO, HOY, 300).getEdad(HOY) == 0);
}

void pruebasHorarioClinica() {
    IO.println("\n--- Horario de la clínica ---");
    comprobar("La clínica abre a las 9:00 y cierra a las 20:00",
            Clinica.APERTURA.equals(LocalTime.of(9, 0)) && Clinica.CIERRE.equals(LocalTime.of(20, 0)));
    comprobar("Abre de lunes a sábado", Clinica.abre(LUNES19) && Clinica.abre(SABADO24));
    comprobar("Cierra los domingos", !Clinica.abre(DOMINGO25));
}

void pruebasVeterinario() throws Exception {
    IO.println("\n--- Veterinario ---");
    Veterinario v = laura();
    comprobar("Getters de Laura",
            v.getCodigo().equals("V1") && v.getNombre().equals("Laura Martín")
                    && v.getEspecialidad() == Especialidad.GENERAL);
    comprobar("toString de Laura", v.toString().equals("V1 Laura Martín (GENERAL)"));
    comprobar("Su agenda empieza vacía y tiene el horario de la clínica",
            v.getAgenda().getNumCitas() == 0 && v.getAgenda().getApertura().equals(Clinica.APERTURA)
                    && v.getAgenda().getCierre().equals(Clinica.CIERRE));
    comprobar("getAgenda devuelve siempre la misma agenda", v.getAgenda() == v.getAgenda());
    try {
        new Veterinario("", "Laura Martín", Especialidad.GENERAL);
        comprobar("Veterinario con código vacío", false);
    } catch (ArgumentoException e) {
        comprobar("Veterinario con código vacío (" + e.getMessage() + ")", true);
    }
    try {
        new Veterinario("V4", "Hugo Pérez", null);
        comprobar("Veterinario sin especialidad", false);
    } catch (ArgumentoException e) {
        comprobar("Veterinario sin especialidad (" + e.getMessage() + ")", true);
    }
}

void pruebasPuedeAtender() throws Exception {
    IO.println("\n--- puedeAtender ---");
    Veterinario general = laura();
    Veterinario cirujano = pablo();
    Veterinario exoticos = elena();
    comprobar("Todos pueden atender lo que exige General",
            general.puedeAtender(Especialidad.GENERAL) && cirujano.puedeAtender(Especialidad.GENERAL)
                    && exoticos.puedeAtender(Especialidad.GENERAL));
    comprobar("Solo el cirujano puede operar",
            cirujano.puedeAtender(Especialidad.CIRUGIA) && !general.puedeAtender(Especialidad.CIRUGIA)
                    && !exoticos.puedeAtender(Especialidad.CIRUGIA));
    comprobar("Solo la especialista en exóticos puede atender exóticos",
            exoticos.puedeAtender(Especialidad.EXOTICOS) && !general.puedeAtender(Especialidad.EXOTICOS)
                    && !cirujano.puedeAtender(Especialidad.EXOTICOS));
}

void pruebasConsulta() throws Exception {
    IO.println("\n--- Consulta ---");
    Veterinario v1 = laura();
    Mascota toby = toby();
    // Variables de tipo CitaVeterinaria: cada método se ejecuta según la clase real (polimorfismo)
    CitaVeterinaria perro = new Consulta(toby, v1, LUNES19, LocalTime.of(9, 0), "cojera");
    CitaVeterinaria gato = new Consulta(misi(), v1, LUNES19, LocalTime.of(12, 0), "pérdida de apetito");
    CitaVeterinaria exotico = new Consulta(kiwi(), elena(), LUNES19, LocalTime.of(10, 0), "revisión anual");
    comprobar("Consulta de perro: 35,00 €, 30 min, General",
            perro.precio() == 3500 && perro.getTipo().equals("Consulta") && perro.getDuracion() == 30
                    && perro.especialidadRequerida() == Especialidad.GENERAL);
    comprobar("Consulta de gato: 35,00 €, 30 min, General",
            gato.precio() == 3500 && gato.getDuracion() == 30 && gato.especialidadRequerida() == Especialidad.GENERAL);
    comprobar("Consulta de exótico: 35,00 €, 45 min, Exóticos",
            exotico.precio() == 3500 && exotico.getDuracion() == 45
                    && exotico.especialidadRequerida() == Especialidad.EXOTICOS);
    comprobar("La cita guarda la mascota y el veterinario, y el cliente es el id de la mascota",
            perro.getMascota() == toby && perro.getVeterinario() == v1 && perro.getCliente().equals("M001"));
    comprobar("descripcion de la consulta", perro.descripcion().equals("Motivo: cojera"));

    Cita copia = perro.conInicio(LocalTime.of(11, 0));
    comprobar("conInicio devuelve otra Consulta con los mismos datos",
            copia instanceof Consulta && copia != perro && copia.getInicio().equals(LocalTime.of(11, 0))
                    && ((Consulta) copia).getMotivo().equals("cojera"));

    v1.getAgenda().reservar(perro);
    v1.getAgenda().retrasar("M001", LUNES19, 30);
    comprobar("Al retrasarla en la agenda sigue siendo una Consulta",
            v1.getAgenda().getCitaDe("M001", LUNES19) instanceof Consulta);
    try {
        new Consulta(toby, v1, LUNES19, LocalTime.of(9, 0), "   ");
        comprobar("Consulta sin motivo", false);
    } catch (ArgumentoException e) {
        comprobar("Consulta sin motivo (" + e.getMessage() + ")", true);
    }
    try {
        new Consulta(toby, null, LUNES19, LocalTime.of(9, 0), "cojera");
        comprobar("Consulta sin veterinario", false);
    } catch (ArgumentoException e) {
        comprobar("Consulta sin veterinario (" + e.getMessage() + ")", true);
    }
    try {
        new Consulta(null, v1, LUNES19, LocalTime.of(9, 0), "cojera");
        comprobar("Consulta sin mascota", false);
    } catch (ArgumentoException e) {
        comprobar("Consulta sin mascota (" + e.getMessage() + ")", true);
    }
}

void pruebasCirugia() throws Exception {
    IO.println("\n--- Cirugia ---");
    Veterinario v2 = pablo();
    CitaVeterinaria toby = new Cirugia(toby(), v2, MARTES20, LocalTime.of(9, 0), 120, "extirpación de un quiste");
    comprobar("Cirugía de 120 min a Toby (28 kg): 180,00 € + 42,00 € = 222,00 €", toby.precio() == 22200);
    CitaVeterinaria kiwi = new Cirugia(kiwi(), v2, MARTES20, LocalTime.of(12, 0), 30, "corte de uñas");
    comprobar("Cirugía de 30 min a Kiwi (1,8 kg): 45,00 € + 2,70 € = 47,70 €", kiwi.precio() == 4770);
    comprobar("Tipo, especialidad y descripción de la cirugía",
            toby.getTipo().equals("Cirugía") && toby.especialidadRequerida() == Especialidad.CIRUGIA
                    && toby.descripcion().equals("Intervención: extirpación de un quiste"));

    Cita copia = toby.conInicio(LocalTime.of(10, 0));
    comprobar("conInicio devuelve otra Cirugia con la misma duración e intervención",
            copia instanceof Cirugia && copia.getDuracion() == 120
                    && ((Cirugia) copia).getIntervencion().equals("extirpación de un quiste"));

    // Polimorfismo: la misma llamada da un precio distinto según el tipo de cita
    ArrayList<CitaVeterinaria> citas = new ArrayList<>();
    citas.add(new Consulta(luna(), laura(), MARTES20, LocalTime.of(11, 0), "vómitos"));
    citas.add(toby);
    int total = 0;
    for (CitaVeterinaria c : citas) {
        total = total + c.precio();
    }
    comprobar("Una consulta y la cirugía de Toby suman 257,00 €", total == 25700);
    try {
        new Cirugia(toby(), v2, MARTES20, LocalTime.of(9, 0), 120, "");
        comprobar("Cirugía sin intervención", false);
    } catch (ArgumentoException e) {
        comprobar("Cirugía sin intervención (" + e.getMessage() + ")", true);
    }
}
