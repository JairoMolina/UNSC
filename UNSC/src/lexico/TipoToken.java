// TipoToken.java
// Define todos los tipos de token que reconoce el compilador UNSC.

package lexico;

public enum TipoToken {

    // palabras reservadas
    SPARTAN, GHOST, CORTANA, SHIELD, FLOOD,
    DEPLOY, RETREAT, MISSION, OPS, TARGET,
    EXTRACT, BROADCAST, SCAN, ARBITER, CHIEF,
    PLASMA, BANSHEE, SENTINEL, ORACLE, GRAVEMIND,
    WARTHOG, ENGAGE, BASE,

    // literales
    ENTERO, DECIMAL, CADENA, BOOLEANO,

    // identificador (nombre de variable o función)
    IDENTIFICADOR,

    // operadores aritméticos
    SUMA, RESTA, MULT, DIV, MOD,

    // operadores de comparación
    IGUAL, DIFERENTE, MENOR, MAYOR, MENOR_IGUAL, MAYOR_IGUAL,

    // operadores lógicos
    AND, OR, NOT,

    // asignación
    ASIGNACION,

    // delimitadores
    PUNTO_COMA, COMA,
    PAREN_ABRE, PAREN_CIERRA,
    LLAVE_ABRE, LLAVE_CIERRA,
    CORCHETE_ABRE, CORCHETE_CIERRA,
    DOS_PUNTOS, FLECHA, PUNTO,

    // especiales
    COMENTARIO,
    EOF,
    ERROR
}
