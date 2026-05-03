// MenuComponentes.java
// Muestra las reglas del lenguaje UNSC en tablas: palabras reservadas,
// operaciones, símbolos permitidos y estructuras de instrucciones.

package menu;

import nucleo.Compilador;
import java.util.*;

public class MenuComponentes {

    public static void mostrar(Scanner sc) {
        boolean salir = false;
        while (!salir) {
            System.out.println();
            System.out.println("  ============================");
            System.out.println("    COMPONENTES DEL LENGUAJE");
            System.out.println("  ============================");
            System.out.println("  1. Palabras reservadas");
            System.out.println("  2. Operaciones permitidas");
            System.out.println("  3. Simbolos permitidos");
            System.out.println("  4. Estructuras de cada instruccion");
            System.out.println("  0. Volver");
            System.out.print("  Opcion: ");

            String op = sc.nextLine().trim();
            switch (op) {
                case "1" -> palabrasReservadas();
                case "2" -> operaciones();
                case "3" -> simbolos();
                case "4" -> estructuras();
                case "0" -> salir = true;
                default  -> System.out.println("  Opcion no valida.");
            }
        }
    }

    // ── palabras reservadas ───────────────────────────────────

    private static void palabrasReservadas() {
        System.out.println();
        System.out.println("  PALABRAS RESERVADAS DEL LENGUAJE UNSC");
        System.out.println("  United Nations Space Code");
        System.out.println();

        List<String[]> filas = Arrays.asList(
            new String[]{ "spartan",   "int",      "Entero",   "Fuerza base — tipo entero"           },
            new String[]{ "ghost",     "float",    "Decimal",  "Vehiculo agil — tipo decimal"         },
            new String[]{ "cortana",   "string",   "Cadena",   "IA de informacion — tipo texto"       },
            new String[]{ "shield",    "bool",     "Booleano", "Escudo activo/inactivo — tipo logico"  },
            new String[]{ "flood",     "void",     "Vacio",    "Sin retorno — funciones void"         },
            new String[]{ "deploy",    "if",       "Si",       "Despliegue — condicional"             },
            new String[]{ "retreat",   "else",     "SiNo",     "Retirada — rama alternativa"          },
            new String[]{ "mission",   "while",    "Mientras", "Mision en curso — ciclo while"        },
            new String[]{ "ops",       "for",      "Para",     "Operaciones — ciclo for"              },
            new String[]{ "target",    "switch",   "Segun",    "Seleccion de objetivo — switch"       },
            new String[]{ "extract",   "return",   "Retornar", "Extraer resultado — return"           },
            new String[]{ "broadcast", "print",    "Imprimir", "Difusion de mensaje — salida"         },
            new String[]{ "scan",      "input",    "Entrada",  "Escaneo de datos — entrada"           },
            new String[]{ "arbiter",   "class",    "Clase",    "Jerarquia definida — clase"           },
            new String[]{ "chief",     "object",   "Objeto",   "Jefe Maestro — instancia"             },
            new String[]{ "plasma",    "array",    "Arreglo",  "Energia acumulada — arreglo"          },
            new String[]{ "banshee",   "break",    "Romper",   "Maniobra evasiva — break"             },
            new String[]{ "sentinel",  "continue", "Continuar","Patrulla — continue"                  },
            new String[]{ "oracle",    "try",      "Intentar", "Guia — bloque try"                    },
            new String[]{ "gravemind", "catch",    "Capturar", "Entidad que atrapa — catch"           },
            new String[]{ "warthog",   "new",      "Nuevo",    "Vehiculo desplegado — new"            },
            new String[]{ "engage",    "function", "Funcion",  "Iniciar combate — declarar funcion"   },
            new String[]{ "base",      "main",     "Principal","Punto de entrada del programa"        }
        );

        Compilador.imprimirTabla(
            new String[]{ "PALABRA UNSC", "EQUIVALENTE", "TRADUCCION", "DESCRIPCION" },
            filas
        );
    }

    // ── operaciones ───────────────────────────────────────────

    private static void operaciones() {
        System.out.println();
        System.out.println("  OPERACIONES PERMITIDAS EN UNSC");
        System.out.println();

        List<String[]> filas = Arrays.asList(
            new String[]{ "Aritmetica",  "+",   "Suma",            "spartan r := a + b;"           },
            new String[]{ "Aritmetica",  "-",   "Resta",           "spartan r := a - b;"           },
            new String[]{ "Aritmetica",  "*",   "Multiplicacion",  "spartan r := a * b;"           },
            new String[]{ "Aritmetica",  "/",   "Division",        "spartan r := a / b;"           },
            new String[]{ "Aritmetica",  "%",   "Modulo",          "spartan r := a % b;"           },
            new String[]{ "Comparacion", "==",  "Igual a",         "deploy (a == b) { }"           },
            new String[]{ "Comparacion", "!=",  "Diferente de",    "deploy (a != b) { }"           },
            new String[]{ "Comparacion", "<",   "Menor que",       "deploy (a < b) { }"            },
            new String[]{ "Comparacion", ">",   "Mayor que",       "deploy (a > b) { }"            },
            new String[]{ "Comparacion", "<=",  "Menor o igual",   "deploy (a <= b) { }"           },
            new String[]{ "Comparacion", ">=",  "Mayor o igual",   "deploy (a >= b) { }"           },
            new String[]{ "Logica",      "&&",  "Y logico",        "deploy (a > 0 && b > 0) { }"   },
            new String[]{ "Logica",      "||",  "O logico",        "deploy (a > 0 || b > 0) { }"   },
            new String[]{ "Logica",      "!",   "Negacion",        "deploy (!activo) { }"          },
            new String[]{ "Asignacion",  ":=",  "Asignar valor",   "spartan x := 10;"              }
        );

        Compilador.imprimirTabla(
            new String[]{ "CATEGORIA", "OPERADOR", "NOMBRE", "EJEMPLO" },
            filas
        );
    }

    // ── símbolos permitidos ───────────────────────────────────

    private static void simbolos() {
        System.out.println();
        System.out.println("  SIMBOLOS PERMITIDOS EN UNSC");
        System.out.println();

        List<String[]> filas = Arrays.asList(
            new String[]{ ":=",  "Asignacion",          "spartan x := 5;"        },
            new String[]{ "+",   "Suma",                "x + y"                  },
            new String[]{ "-",   "Resta / Negacion",    "x - y  o  -x"           },
            new String[]{ "*",   "Multiplicacion",      "x * y"                  },
            new String[]{ "/",   "Division",            "x / y"                  },
            new String[]{ "%",   "Modulo",              "x % y"                  },
            new String[]{ "==",  "Igual",               "x == y"                 },
            new String[]{ "!=",  "Diferente",           "x != y"                 },
            new String[]{ "<",   "Menor",               "x < y"                  },
            new String[]{ ">",   "Mayor",               "x > y"                  },
            new String[]{ "<=",  "Menor o igual",       "x <= y"                 },
            new String[]{ ">=",  "Mayor o igual",       "x >= y"                 },
            new String[]{ "&&",  "Y logico",            "a && b"                 },
            new String[]{ "||",  "O logico",            "a || b"                 },
            new String[]{ "!",   "NOT logico",          "!activo"                },
            new String[]{ "->",  "Flecha (case)",       "case 1 -> { }"          },
            new String[]{ "(",   "Parentesis abre",     "deploy (x > 0)"         },
            new String[]{ ")",   "Parentesis cierra",   "broadcast(x)"           },
            new String[]{ "{",   "Llave abre",          "base() {"               },
            new String[]{ "}",   "Llave cierra",        "}"                      },
            new String[]{ ";",   "Punto y coma",        "spartan x := 0;"        },
            new String[]{ ",",   "Coma",                "broadcast(a, b)"        },
            new String[]{ "//",  "Comentario linea",    "// esto es comentario"  },
            new String[]{ "/**/","Comentario bloque",   "/* bloque */"           },
            new String[]{ "\"\"","Cadena de texto",     "cortana s := \"hola\";" }
        );

        Compilador.imprimirTabla(
            new String[]{ "SIMBOLO", "DESCRIPCION", "EJEMPLO DE USO" },
            filas
        );
    }

    // ── estructuras ───────────────────────────────────────────

    private static void estructuras() {
        System.out.println();
        System.out.println("  ESTRUCTURAS DE CADA INSTRUCCION EN UNSC");
        System.out.println();

        List<String[]> filas = Arrays.asList(
            new String[]{ "Declaracion",        "tipo identificador [ := expr ] ;",
                          "spartan nivel := 117;"                                           },
            new String[]{ "Asignacion",         "identificador := expr ;",
                          "nivel := nivel + 1;"                                             },
            new String[]{ "Condicional (if)",   "deploy ( condicion ) { } [ retreat { } ]",
                          "deploy (x > 0) { broadcast(x); } retreat { broadcast(0); }"     },
            new String[]{ "Ciclo while",        "mission ( condicion ) { }",
                          "mission (i < 10) { i := i + 1; }"                               },
            new String[]{ "Ciclo for",          "ops ( init ; condicion ; actualizacion ) { }",
                          "ops (spartan i := 0; i < 5; i := i + 1) { broadcast(i); }"     },
            new String[]{ "Switch",             "target ( expr ) { case val -> { } default -> { } }",
                          "target (x) { case 1 -> { broadcast(\"uno\"); } }"               },
            new String[]{ "Funcion",            "engage tipo nombre ( params ) { extract val; }",
                          "engage spartan suma(spartan a, spartan b) { extract a + b; }"   },
            new String[]{ "Llamada",            "nombre ( args ) ;",
                          "spartan r := suma(3, 4);"                                        },
            new String[]{ "Retorno",            "extract [ expr ] ;",
                          "extract resultado;"                                              },
            new String[]{ "Salida",             "broadcast ( expr { , expr } ) ;",
                          "broadcast(\"Hola\", nombre);"                                   },
            new String[]{ "Entrada",            "scan ( identificador ) ;",
                          "scan(nombre);"                                                   },
            new String[]{ "Break",              "banshee ;",
                          "banshee;"                                                        },
            new String[]{ "Continue",           "sentinel ;",
                          "sentinel;"                                                       },
            new String[]{ "Try-catch",          "oracle { } gravemind ( id ) { }",
                          "oracle { calcular(); } gravemind (e) { broadcast(e); }"         },
            new String[]{ "Comentario",         "// texto   o   /* texto */",
                          "// esto es un comentario"                                        }
        );

        Compilador.imprimirTabla(
            new String[]{ "INSTRUCCION", "SINTAXIS", "EJEMPLO" },
            filas
        );
    }
}
