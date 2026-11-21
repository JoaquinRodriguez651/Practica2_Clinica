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
