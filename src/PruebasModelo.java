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
        pruebasClinica();
        pruebasVeterinario();
        pruebasPuedeAtender();

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

// Un veterinario de cada especialidad
Veterinario generalista() throws ArgumentoException {
    return new Veterinario("V01", "Laura Pérez", Especialidad.GENERAL);
}

Veterinario cirujano() throws ArgumentoException {
    return new Veterinario("V02", "Marcos Sanz", Especialidad.CIRUGIA);
}

Veterinario exoticos() throws ArgumentoException {
    return new Veterinario("V03", "Sara Vidal", Especialidad.EXOTICOS);
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

void pruebasClinica() throws Exception {
    IO.println("\n--- Clínica ---");
    comprobar("Horario de la clínica: de 9:00 a 20:00",
            Clinica.APERTURA.equals(LocalTime.of(9, 0)) && Clinica.CIERRE.equals(LocalTime.of(20, 0)));
    comprobar("La clínica abre de lunes a sábado y cierra los domingos",
            Clinica.abre(LUNES19) && Clinica.abre(HOY) && Clinica.abre(SABADO24) && !Clinica.abre(DOMINGO25));
    try {
        Clinica.abre(null);
        comprobar("abre con fecha null", false);
    } catch (ArgumentoException e) {
        comprobar("abre con fecha null (" + e.getMessage() + ")", true);
    }
}


void pruebasVeterinario() throws Exception {
    IO.println("\n--- Veterinario ---");
    Veterinario v = generalista();
    comprobar("Getters del veterinario",
            v.getCodigo().equals("V01") && v.getNombre().equals("Laura Pérez")
                    && v.getEspecialidad() == Especialidad.GENERAL);
    comprobar("Su agenda tiene el horario de la clínica y empieza vacía",
            v.getAgenda().getApertura().equals(Clinica.APERTURA) && v.getAgenda().getCierre().equals(Clinica.CIERRE)
                    && v.getAgenda().getNumCitas() == 0);

    v.getAgenda().reservar(new CitaPrueba("M001", LUNES19, LocalTime.of(10, 0), 30));
    comprobar("getAgenda devuelve la misma agenda, no una copia",
            v.getAgenda() == v.getAgenda() && v.getAgenda().getNumCitas() == 1);

    Veterinario otro = cirujano();
    comprobar("Cada veterinario tiene su propia agenda",
            otro.getAgenda() != v.getAgenda() && otro.getAgenda().getNumCitas() == 0);

    try {
        new Veterinario("  ", "Laura Pérez", Especialidad.GENERAL);
        comprobar("Veterinario con código vacío", false);
    } catch (ArgumentoException e) {
        comprobar("Veterinario con código vacío (" + e.getMessage() + ")", true);
    }
    try {
        new Veterinario("V04", "Pablo Ros", null);
        comprobar("Veterinario sin especialidad", false);
    } catch (ArgumentoException e) {
        comprobar("Veterinario sin especialidad (" + e.getMessage() + ")", true);
    }

}

void pruebasPuedeAtender() throws Exception {
    IO.println("\n--- puedeAtender ---");
    Veterinario g = generalista();
    Veterinario c = cirujano();
    Veterinario e = exoticos();
    comprobar("Generalista: atiende General, no Cirugía ni Exóticos",
            g.puedeAtender(Especialidad.GENERAL) && !g.puedeAtender(Especialidad.CIRUGIA)
                    && !g.puedeAtender(Especialidad.EXOTICOS));
    comprobar("Cirujano: atiende General y Cirugía, no Exóticos",
            c.puedeAtender(Especialidad.GENERAL) && c.puedeAtender(Especialidad.CIRUGIA)
                    && !c.puedeAtender(Especialidad.EXOTICOS));
    comprobar("Especialista en exóticos: atiende General y Exóticos, no Cirugía",
            e.puedeAtender(Especialidad.GENERAL) && e.puedeAtender(Especialidad.EXOTICOS)
                    && !e.puedeAtender(Especialidad.CIRUGIA));
}