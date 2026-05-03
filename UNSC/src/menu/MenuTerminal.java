// MenuTerminal.java
// Terminal interactiva para escribir programas UNSC directamente.
// Permite escribir línea a línea; escribe "RUN" para compilar o "EXIT" para salir.

package menu;

import nucleo.Compilador;
import nucleo.ResultadoCompilacion;
import java.util.Scanner;

public class MenuTerminal {

    public static void mostrar(Scanner sc) {
        Compilador compilador = new Compilador();
        System.out.println();
        System.out.println("  ========================");
        System.out.println("    TERMINAL UNSC");
        System.out.println("  ========================");
        System.out.println("  Escribe tu programa UNSC linea por linea.");
        System.out.println("  Comandos especiales:");
        System.out.println("    RUN    -> compila y muestra el resultado");
        System.out.println("    CLEAR  -> borra el codigo actual");
        System.out.println("    SHOW   -> muestra el codigo actual");
        System.out.println("    EXIT   -> volver al menu principal");
        System.out.println();

        StringBuilder codigo = new StringBuilder();
        int numLinea = 1;

        while (true) {
            System.out.printf("  %3d | ", numLinea);
            String linea = sc.nextLine();

            switch (linea.trim().toUpperCase()) {
                case "RUN" -> {
                    if (codigo.length() == 0) {
                        System.out.println("  El editor esta vacio.");
                    } else {
                        ResultadoCompilacion rc = compilador.compilar(codigo.toString());
                        compilador.imprimirResultado(rc);
                    }
                    // no resetear: el usuario puede seguir editando
                }
                case "CLEAR" -> {
                    codigo.setLength(0);
                    numLinea = 1;
                    System.out.println("  Codigo borrado.");
                    continue;
                }
                case "SHOW" -> {
                    System.out.println();
                    System.out.println("  -- CODIGO ACTUAL --");
                    int n = 1;
                    for (String l : codigo.toString().split("\n", -1))
                        System.out.printf("  %3d | %s%n", n++, l);
                    System.out.println();
                }
                case "EXIT" -> { return; }
                default -> {
                    // agregar la línea al código
                    codigo.append(linea).append("\n");
                    numLinea++;
                    continue;
                }
            }
            // después de RUN o SHOW mantener el número de línea
        }
    }
}
