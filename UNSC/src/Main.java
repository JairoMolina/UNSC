// Main.java
// Punto de entrada del compilador UNSC (United Nations Space Code).
// Muestra la bienvenida y el menú principal.

import menu.MenuComponentes;
import menu.MenuEjemplos;
import menu.MenuTerminal;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        bienvenida();
        menuPrincipal(sc);
        System.out.println();
        System.out.println("  Hasta la proxima, Spartan.");
        System.out.println();
        sc.close();
    }

    private static void bienvenida() {
        System.out.println();
        System.out.println("  ============================================================");
        System.out.println("    U N S C   C O M P I L E R");
        System.out.println("    United Nations Space Code");
        System.out.println("  ============================================================");
        System.out.println("    Lenguaje de programacion inspirado en la saga Halo.");
        System.out.println("    Version 1.0");
        System.out.println("  ============================================================");
        System.out.println();
    }

    private static void menuPrincipal(Scanner sc) {
        boolean salir = false;
        while (!salir) {
            System.out.println("  MENU PRINCIPAL");
            System.out.println("  --------------");
            System.out.println("  1. Terminal");
            System.out.println("  2. Componentes del lenguaje");
            System.out.println("  3. Ejemplos de codigo");
            System.out.println("  0. Salir");
            System.out.print("  Opcion: ");

            String op = sc.nextLine().trim();
            switch (op) {
                case "1" -> MenuTerminal.mostrar(sc);
                case "2" -> MenuComponentes.mostrar(sc);
                case "3" -> MenuEjemplos.mostrar(sc);
                case "0" -> salir = true;
                default  -> System.out.println("  Opcion no valida.");
            }
            System.out.println();
        }
    }
}
