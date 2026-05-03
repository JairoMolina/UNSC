// MenuEjemplos.java
// Permite cargar uno de los 10 programas de prueba (5 correctos, 5 con errores)
// y compilarlo mostrando el resultado en pantalla.

package menu;

import nucleo.Compilador;
import nucleo.ResultadoCompilacion;
import java.util.*;

public class MenuEjemplos {

    // 5 programas válidos y 5 con errores intencionados
    private static final String[] NOMBRES = {
        "[V1] Variables y tipos de dato",
        "[V2] Condicional deploy / retreat",
        "[V3] Ciclo mission (while)",
        "[V4] Ciclo ops con sentinel (for + continue)",
        "[V5] Funcion engage / extract",
        "[E1] Error lexico: caracter no permitido (@)",
        "[E2] Error lexico: cadena sin cerrar",
        "[E3] Error sintactico: falta punto y coma",
        "[E4] Error sintactico: parentesis sin cerrar",
        "[E5] Error sintactico: llave de cierre faltante"
    };

    private static final String[] CODIGOS = {
        // V1 — tipos básicos
        "// Declara variables de distintos tipos y las imprime.\n"
        + "base() {\n"
        + "    spartan nivel := 117;\n"
        + "    ghost velocidad := 9.8;\n"
        + "    cortana nombre := \"Master Chief\";\n"
        + "    shield activo := shield:true;\n"
        + "    broadcast(\"Spartan:\", nombre, \"Nivel:\", nivel);\n"
        + "    broadcast(\"Velocidad:\", velocidad, \"Activo:\", activo);\n"
        + "}",

        // V2 — deploy/retreat
        "// Evalua la energia del Spartan con un condicional.\n"
        + "base() {\n"
        + "    spartan energia := 75;\n"
        + "    spartan escudo := 50;\n"
        + "    deploy (energia > 60 && escudo >= 50) {\n"
        + "        broadcast(\"Estado: COMBATE\");\n"
        + "    } retreat {\n"
        + "        broadcast(\"Estado: RETIRADA\");\n"
        + "    }\n"
        + "}",

        // V3 — mission
        "// Suma los primeros 10 enteros con un ciclo mission.\n"
        + "base() {\n"
        + "    spartan n := 10;\n"
        + "    spartan suma := 0;\n"
        + "    spartan i := 1;\n"
        + "    mission (i <= n) {\n"
        + "        suma := suma + i;\n"
        + "        i := i + 1;\n"
        + "    }\n"
        + "    broadcast(\"Suma del 1 al 10:\", suma);\n"
        + "}",

        // V4 — ops + sentinel
        "// Tabla del 7 con ciclo ops, salta el 5 con sentinel.\n"
        + "base() {\n"
        + "    spartan i;\n"
        + "    spartan base := 7;\n"
        + "    ops (i := 1; i <= 10; i := i + 1) {\n"
        + "        deploy (i == 5) {\n"
        + "            sentinel;\n"
        + "        }\n"
        + "        broadcast(base, \"x\", i, \"=\", base * i);\n"
        + "    }\n"
        + "}",

        // V5 — función
        "// Funcion que calcula el factorial de un numero.\n"
        + "engage spartan factorial(spartan n) {\n"
        + "    spartan resultado := 1;\n"
        + "    spartan i;\n"
        + "    ops (i := 1; i <= n; i := i + 1) {\n"
        + "        resultado := resultado * i;\n"
        + "    }\n"
        + "    extract resultado;\n"
        + "}\n"
        + "base() {\n"
        + "    spartan f := factorial(6);\n"
        + "    broadcast(\"Factorial de 6:\", f);\n"
        + "}",

        // E1 — carácter @
        "// Error lexico: el caracter '@' no pertenece al alfabeto de UNSC.\n"
        + "base() {\n"
        + "    spartan danio := 100;\n"
        + "    spartan reduccion := danio @ 0.25;\n"
        + "    broadcast(\"Danio:\", reduccion);\n"
        + "}",

        // E2 — cadena sin cerrar
        "// Error lexico: cadena sin comilla de cierre.\n"
        + "base() {\n"
        + "    cortana mensaje := \"Iniciando protocolo Spartan;\n"
        + "    broadcast(mensaje);\n"
        + "}",

        // E3 — falta ;
        "// Error sintactico: falta punto y coma en la declaracion.\n"
        + "base() {\n"
        + "    spartan energia := 100\n"
        + "    spartan escudo := 50;\n"
        + "    broadcast(energia, escudo);\n"
        + "}",

        // E4 — paréntesis sin cerrar
        "// Error sintactico: falta ')' en la condicion del deploy.\n"
        + "base() {\n"
        + "    spartan nivel := 5;\n"
        + "    deploy (nivel > 3 {\n"
        + "        broadcast(\"Nivel alto\");\n"
        + "    }\n"
        + "}",

        // E5 — llave sin cerrar
        "// Error sintactico: llave de cierre de base() faltante.\n"
        + "base() {\n"
        + "    spartan x := 42;\n"
        + "    broadcast(\"El valor es:\", x);\n"
    };

    public static void mostrar(Scanner sc) {
        Compilador compilador = new Compilador();
        boolean salir = false;

        while (!salir) {
            System.out.println();
            System.out.println("  ==========================");
            System.out.println("    EJEMPLOS DE CODIGO UNSC");
            System.out.println("  ==========================");
            for (int i = 0; i < NOMBRES.length; i++) {
                System.out.printf("  %2d. %s%n", i + 1, NOMBRES[i]);
            }
            System.out.println("   0. Volver");
            System.out.print("  Opcion: ");

            String input = sc.nextLine().trim();
            if (input.equals("0")) { salir = true; continue; }

            int opcion;
            try {
                opcion = Integer.parseInt(input) - 1;
            } catch (NumberFormatException e) {
                System.out.println("  Opcion no valida.");
                continue;
            }

            if (opcion < 0 || opcion >= CODIGOS.length) {
                System.out.println("  Opcion fuera de rango.");
                continue;
            }

            // mostrar el código y compilarlo
            System.out.println();
            System.out.println("  -- CODIGO --");
            for (String linea : CODIGOS[opcion].split("\n"))
                System.out.println("  " + linea);

            ResultadoCompilacion rc = compilador.compilar(CODIGOS[opcion]);
            compilador.imprimirResultado(rc);

            System.out.println("  Presiona Enter para continuar...");
            sc.nextLine();
        }
    }
}
