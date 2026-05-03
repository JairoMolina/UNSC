// AnalizadorLexico.java
// Convierte el código fuente UNSC en una lista de tokens.
// Aplica las expresiones regulares en orden de prioridad y registra errores léxicos.

package lexico;

import java.util.*;
import java.util.regex.*;

public class AnalizadorLexico {

    // mapa de palabras reservadas → tipo de token
    private static final Map<String, TipoToken> RESERVADAS = new HashMap<>();
    static {
        RESERVADAS.put("spartan",   TipoToken.SPARTAN);
        RESERVADAS.put("ghost",     TipoToken.GHOST);
        RESERVADAS.put("cortana",   TipoToken.CORTANA);
        RESERVADAS.put("shield",    TipoToken.SHIELD);
        RESERVADAS.put("flood",     TipoToken.FLOOD);
        RESERVADAS.put("deploy",    TipoToken.DEPLOY);
        RESERVADAS.put("retreat",   TipoToken.RETREAT);
        RESERVADAS.put("mission",   TipoToken.MISSION);
        RESERVADAS.put("ops",       TipoToken.OPS);
        RESERVADAS.put("target",    TipoToken.TARGET);
        RESERVADAS.put("extract",   TipoToken.EXTRACT);
        RESERVADAS.put("broadcast", TipoToken.BROADCAST);
        RESERVADAS.put("scan",      TipoToken.SCAN);
        RESERVADAS.put("arbiter",   TipoToken.ARBITER);
        RESERVADAS.put("chief",     TipoToken.CHIEF);
        RESERVADAS.put("plasma",    TipoToken.PLASMA);
        RESERVADAS.put("banshee",   TipoToken.BANSHEE);
        RESERVADAS.put("sentinel",  TipoToken.SENTINEL);
        RESERVADAS.put("oracle",    TipoToken.ORACLE);
        RESERVADAS.put("gravemind", TipoToken.GRAVEMIND);
        RESERVADAS.put("warthog",   TipoToken.WARTHOG);
        RESERVADAS.put("engage",    TipoToken.ENGAGE);
        RESERVADAS.put("base",      TipoToken.BASE);
    }

    // mapa de operadores de 2 caracteres
    private static final Map<String, TipoToken> OP2 = new LinkedHashMap<>();
    static {
        OP2.put(":=", TipoToken.ASIGNACION);
        OP2.put("==", TipoToken.IGUAL);
        OP2.put("!=", TipoToken.DIFERENTE);
        OP2.put("<=", TipoToken.MENOR_IGUAL);
        OP2.put(">=", TipoToken.MAYOR_IGUAL);
        OP2.put("&&", TipoToken.AND);
        OP2.put("||", TipoToken.OR);
        OP2.put("->", TipoToken.FLECHA);
    }

    // mapa de operadores de 1 carácter
    private static final Map<Character, TipoToken> OP1 = new HashMap<>();
    static {
        OP1.put('+', TipoToken.SUMA);
        OP1.put('-', TipoToken.RESTA);
        OP1.put('*', TipoToken.MULT);
        OP1.put('/', TipoToken.DIV);
        OP1.put('%', TipoToken.MOD);
        OP1.put('<', TipoToken.MENOR);
        OP1.put('>', TipoToken.MAYOR);
        OP1.put('!', TipoToken.NOT);
    }

    // mapa de delimitadores de 1 carácter
    private static final Map<Character, TipoToken> DELIM = new HashMap<>();
    static {
        DELIM.put(';', TipoToken.PUNTO_COMA);
        DELIM.put(',', TipoToken.COMA);
        DELIM.put('(', TipoToken.PAREN_ABRE);
        DELIM.put(')', TipoToken.PAREN_CIERRA);
        DELIM.put('{', TipoToken.LLAVE_ABRE);
        DELIM.put('}', TipoToken.LLAVE_CIERRA);
        DELIM.put('[', TipoToken.CORCHETE_ABRE);
        DELIM.put(']', TipoToken.CORCHETE_CIERRA);
        DELIM.put('.', TipoToken.PUNTO);
    }

    // patrones compilados
    private static final Pattern PAT_COM_BL = Pattern.compile("^/\\*[\\s\\S]*?\\*/");
    private static final Pattern PAT_COM_LN = Pattern.compile("^//[^\n]*");
    private static final Pattern PAT_BOOL   = Pattern.compile("^shield:(true|false)");
    private static final Pattern PAT_DEC    = Pattern.compile("^\\d+\\.\\d+");
    private static final Pattern PAT_INT    = Pattern.compile("^\\d+");
    private static final Pattern PAT_STR    = Pattern.compile("^\"([^\"\\\\]|\\\\.)*\"");
    private static final Pattern PAT_ID     = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*");

    // estado del lexer durante el análisis
    private String src;
    private int pos, linea, columna;
    private List<Token> tokens;
    private List<ErrorLexico> errores;
    private Map<String, Simbolo> tablaSimbolos;

    public ResultadoLexico analizar(String codigo) {
        src          = codigo;
        pos          = 0;
        linea        = 1;
        columna      = 1;
        tokens       = new ArrayList<>();
        errores      = new ArrayList<>();
        tablaSimbolos = new LinkedHashMap<>();

        while (pos < src.length()) {
            char c = src.charAt(pos);

            // saltar espacios y tabuladores
            if (c == ' ' || c == '\t' || c == '\r') { avanzar(1); continue; }

            // salto de línea
            if (c == '\n') { linea++; columna = 1; pos++; continue; }

            // intentar cada patrón en orden
            if (intentarComentarioBloque()) continue;
            if (intentarComentarioLinea())  continue;
            if (intentarBooleano())         continue;
            if (intentarDecimal())          continue;
            if (intentarEntero())           continue;
            if (intentarCadena())           continue;
            if (intentarOperador2())        continue;
            if (intentarOperador1())        continue;
            if (intentarDelimitador())      continue;
            if (intentarIdentificador())    continue;

            // ningún patrón coincidió: carácter inválido
            errores.add(new ErrorLexico(
                "Carácter no permitido: '" + c + "'",
                "No pertenece al alfabeto de UNSC",
                linea, columna, String.valueOf(c)
            ));
            avanzar(1);
        }

        tokens.add(new Token(TipoToken.EOF, "EOF", linea, columna));
        return new ResultadoLexico(tokens, errores, tablaSimbolos);
    }

    // avanza el cursor n posiciones actualizando columna
    private void avanzar(int n) {
        for (int i = 0; i < n; i++) {
            if (pos < src.length() && src.charAt(pos) == '\n') {
                linea++; columna = 1;
            } else {
                columna++;
            }
            pos++;
        }
    }

    // aplica un patrón al resto del src desde pos
    private Matcher match(Pattern p) {
        return p.matcher(src.substring(pos));
    }

    private boolean intentarComentarioBloque() {
        Matcher m = match(PAT_COM_BL);
        if (!m.find()) return false;
        tokens.add(new Token(TipoToken.COMENTARIO, m.group(), linea, columna));
        avanzar(m.group().length());
        return true;
    }

    private boolean intentarComentarioLinea() {
        Matcher m = match(PAT_COM_LN);
        if (!m.find()) return false;
        tokens.add(new Token(TipoToken.COMENTARIO, m.group(), linea, columna));
        avanzar(m.group().length());
        return true;
    }

    private boolean intentarBooleano() {
        Matcher m = match(PAT_BOOL);
        if (!m.find()) return false;
        tokens.add(new Token(TipoToken.BOOLEANO, m.group(), linea, columna));
        avanzar(m.group().length());
        return true;
    }

    private boolean intentarDecimal() {
        Matcher m = match(PAT_DEC);
        if (!m.find()) return false;
        tokens.add(new Token(TipoToken.DECIMAL, m.group(), linea, columna));
        avanzar(m.group().length());
        return true;
    }

    private boolean intentarEntero() {
        Matcher m = match(PAT_INT);
        if (!m.find()) return false;
        tokens.add(new Token(TipoToken.ENTERO, m.group(), linea, columna));
        avanzar(m.group().length());
        return true;
    }

    private boolean intentarCadena() {
        // cadena bien formada
        Matcher m = match(PAT_STR);
        if (m.find()) {
            tokens.add(new Token(TipoToken.CADENA, m.group(), linea, columna));
            avanzar(m.group().length());
            return true;
        }
        // cadena que abre pero nunca cierra
        if (pos < src.length() && src.charAt(pos) == '"') {
            int colInicio = columna;
            int finLinea  = src.indexOf('\n', pos);
            int largo     = finLinea < 0 ? src.length() - pos : finLinea - pos;
            String frag   = src.substring(pos, Math.min(pos + 20, pos + largo));
            errores.add(new ErrorLexico("Cadena sin cerrar", "Falta comilla de cierre \"", linea, colInicio, frag));
            avanzar(largo);
            return true;
        }
        return false;
    }

    private boolean intentarOperador2() {
        if (pos + 1 >= src.length()) return false;
        String dos = src.substring(pos, pos + 2);
        TipoToken t = OP2.get(dos);
        if (t == null) return false;
        tokens.add(new Token(t, dos, linea, columna));
        avanzar(2);
        return true;
    }

    private boolean intentarOperador1() {
        char c = src.charAt(pos);
        TipoToken t = OP1.get(c);
        if (t == null) return false;
        tokens.add(new Token(t, String.valueOf(c), linea, columna));
        avanzar(1);
        return true;
    }

    private boolean intentarDelimitador() {
        char c = src.charAt(pos);
        TipoToken t = DELIM.get(c);
        if (t == null) return false;
        tokens.add(new Token(t, String.valueOf(c), linea, columna));
        avanzar(1);
        return true;
    }

    private boolean intentarIdentificador() {
        Matcher m = match(PAT_ID);
        if (!m.find()) return false;
        String lex  = m.group();
        TipoToken t = RESERVADAS.getOrDefault(lex.toLowerCase(), TipoToken.IDENTIFICADOR);
        tokens.add(new Token(t, lex, linea, columna));
        // registrar en tabla de símbolos solo identificadores (no palabras reservadas)
        if (t == TipoToken.IDENTIFICADOR && !tablaSimbolos.containsKey(lex)) {
            tablaSimbolos.put(lex, new Simbolo(lex, linea, columna));
        }
        avanzar(lex.length());
        return true;
    }
}
