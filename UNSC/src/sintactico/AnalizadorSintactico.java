// AnalizadorSintactico.java
// Parser de descenso recursivo para el lenguaje UNSC.
// Recibe la lista de tokens del lexer y construye el AST.
// Cada método corresponde a un no-terminal de la gramática.

package sintactico;

import lexico.TipoToken;
import lexico.Token;

import java.util.*;

public class AnalizadorSintactico {

    private List<Token> tokens;
    private int pos;
    private List<ErrorSintactico> errores;

    // ── utilidades de navegación ──────────────────────────────

    // token actual
    private Token actual() {
        return tokens.get(Math.min(pos, tokens.size() - 1));
    }

    // token siguiente sin consumir
    private Token siguiente() {
        return tokens.get(Math.min(pos + 1, tokens.size() - 1));
    }

    private boolean es(TipoToken t) {
        return actual().tipo == t;
    }

    // consume el token si es del tipo esperado; si no, registra error
    private Token comer(TipoToken tipo, String descripcion) {
        Token tok = actual();
        if (tok.tipo == tipo) {
            pos++;
            return tok;
        }
        error("Se esperaba " + descripcion, descripcion, tok.valor, tok.linea, tok.columna);
        return null;
    }

    // consume solo si coincide, sin registrar error
    private Token comerSi(TipoToken tipo) {
        if (es(tipo)) { Token t = actual(); pos++; return t; }
        return null;
    }

    private void error(String desc, String esp, String enc, int l, int c) {
        errores.add(new ErrorSintactico(desc, esp, enc, l, c));
    }

    // avanza hasta encontrar un token de sincronización (recuperación de errores)
    private void sincronizar(TipoToken... siguientes) {
        Set<TipoToken> set = new HashSet<>(Arrays.asList(siguientes));
        while (!es(TipoToken.EOF) && !set.contains(actual().tipo)) pos++;
    }

    private boolean esTipo() {
        TipoToken t = actual().tipo;
        return t == TipoToken.SPARTAN  || t == TipoToken.GHOST    ||
               t == TipoToken.CORTANA  || t == TipoToken.SHIELD   ||
               t == TipoToken.FLOOD    || t == TipoToken.PLASMA   ||
               t == TipoToken.ARBITER;
    }

    // ── punto de entrada ──────────────────────────────────────

    public ResultadoSintactico analizar(List<Token> listaTokens) {
        // filtrar comentarios, no tienen valor gramatical
        this.tokens  = new ArrayList<>();
        for (Token t : listaTokens) {
            if (t.tipo != TipoToken.COMENTARIO) this.tokens.add(t);
        }
        this.pos     = 0;
        this.errores = new ArrayList<>();

        NodoAST ast = parsePrograma();
        return new ResultadoSintactico(ast, errores);
    }

    // ── gramática ─────────────────────────────────────────────

    // programa ::= { declaracion_funcion } base '(' ')' bloque
    private NodoAST parsePrograma() {
        NodoAST n = new NodoAST("Programa");

        while (!es(TipoToken.EOF)) {
            if (es(TipoToken.ENGAGE))    n.add(parseFuncion());
            else if (esTipo())           n.add(parseDeclVar());
            else if (es(TipoToken.BASE)) break;
            else {
                Token t = actual();
                error("Instrucción inválida en ámbito global", "engage / base", t.valor, t.linea, t.columna);
                pos++;
            }
        }

        if (es(TipoToken.BASE)) {
            NodoAST entrada = new NodoAST("EntradaPrincipal", "base");
            comer(TipoToken.BASE, "base");
            comer(TipoToken.PAREN_ABRE, "(");
            comer(TipoToken.PAREN_CIERRA, ")");
            entrada.add(parseBloque());
            n.add(entrada);
        } else {
            Token t = actual();
            error("Falta el punto de entrada base()", "base()", t.valor, t.linea, t.columna);
        }

        return n;
    }

    // bloque ::= '{' { instruccion } '}'
    private NodoAST parseBloque() {
        NodoAST n = new NodoAST("Bloque");
        if (comer(TipoToken.LLAVE_ABRE, "{") == null) {
            sincronizar(TipoToken.LLAVE_CIERRA, TipoToken.EOF);
            return n;
        }
        while (!es(TipoToken.LLAVE_CIERRA) && !es(TipoToken.EOF)) {
            NodoAST instr = parseInstruccion();
            if (instr != null) n.add(instr);
        }
        if (comer(TipoToken.LLAVE_CIERRA, "}") == null) {
            Token t = actual();
            error("Bloque sin cerrar: falta }", "}", t.valor, t.linea, t.columna);
        }
        return n;
    }

    // instruccion ::= declVar | asignacion | deploy | mission | ops | ...
    private NodoAST parseInstruccion() {
        if (esTipo())                       return parseDeclVar();
        if (es(TipoToken.DEPLOY))           return parseDeploy();
        if (es(TipoToken.MISSION))          return parseMission();
        if (es(TipoToken.OPS))              return parseOps();
        if (es(TipoToken.TARGET))           return parseTarget();
        if (es(TipoToken.EXTRACT))          return parseExtract();
        if (es(TipoToken.BROADCAST))        return parseBroadcast();
        if (es(TipoToken.SCAN))             return parseScan();
        if (es(TipoToken.ORACLE))           return parseOracle();
        if (es(TipoToken.BANSHEE)) {
            comer(TipoToken.BANSHEE, "banshee");
            comer(TipoToken.PUNTO_COMA, ";");
            return new NodoAST("Banshee", "banshee");
        }
        if (es(TipoToken.SENTINEL)) {
            comer(TipoToken.SENTINEL, "sentinel");
            comer(TipoToken.PUNTO_COMA, ";");
            return new NodoAST("Sentinel", "sentinel");
        }
        if (es(TipoToken.IDENTIFICADOR)) {
            if (siguiente().tipo == TipoToken.ASIGNACION) return parseAsignacion();
            if (siguiente().tipo == TipoToken.PAREN_ABRE) {
                NodoAST ll = parseLlamada();
                comer(TipoToken.PUNTO_COMA, ";");
                return ll;
            }
        }
        Token t = actual();
        error("Instrucción no reconocida: \"" + t.valor + "\"", "instrucción válida", t.valor, t.linea, t.columna);
        pos++;
        return null;
    }

    // tipo_dato identificador [ ':=' expresion ] ';'
    private NodoAST parseDeclVar() {
        Token tipo = actual();
        NodoAST n  = new NodoAST("DeclaracionVar");
        n.add(new NodoAST("Tipo", tipo.valor));
        pos++;

        Token id = actual();
        if (comer(TipoToken.IDENTIFICADOR, "identificador") == null) {
            sincronizar(TipoToken.PUNTO_COMA);
            comerSi(TipoToken.PUNTO_COMA);
            return n;
        }
        n.add(new NodoAST("Identificador", id.valor));

        if (comerSi(TipoToken.ASIGNACION) != null) n.add(parseExpresion());

        if (comer(TipoToken.PUNTO_COMA, ";") == null)
            error("Falta ; al final de la declaración", ";", actual().valor, actual().linea, actual().columna);
        return n;
    }

    // identificador ':=' expresion ';'
    private NodoAST parseAsignacion() {
        Token id = actual();
        NodoAST n = new NodoAST("Asignacion");
        n.add(new NodoAST("Identificador", id.valor));
        comer(TipoToken.IDENTIFICADOR, "identificador");
        comer(TipoToken.ASIGNACION, ":=");
        n.add(parseExpresion());
        if (comer(TipoToken.PUNTO_COMA, ";") == null)
            error("Falta ;", ";", actual().valor, actual().linea, actual().columna);
        return n;
    }

    // deploy '(' expr_logica ')' bloque [ retreat bloque ]
    private NodoAST parseDeploy() {
        NodoAST n = new NodoAST("Condicional", "deploy");
        comer(TipoToken.DEPLOY, "deploy");
        comer(TipoToken.PAREN_ABRE, "(");
        n.add(new NodoAST("Condicion").add(parseExprLogica()));
        comer(TipoToken.PAREN_CIERRA, ")");
        n.add(new NodoAST("Entonces").add(parseBloque()));
        if (comerSi(TipoToken.RETREAT) != null)
            n.add(new NodoAST("SiNo").add(parseBloque()));
        return n;
    }

    // mission '(' expr_logica ')' bloque
    private NodoAST parseMission() {
        NodoAST n = new NodoAST("CicloMission", "mission");
        comer(TipoToken.MISSION, "mission");
        comer(TipoToken.PAREN_ABRE, "(");
        n.add(new NodoAST("Condicion").add(parseExprLogica()));
        comer(TipoToken.PAREN_CIERRA, ")");
        n.add(parseBloque());
        return n;
    }

    // ops '(' init ';' condicion ';' actualizacion ')' bloque
    private NodoAST parseOps() {
        NodoAST n = new NodoAST("CicloOps", "ops");
        comer(TipoToken.OPS, "ops");
        comer(TipoToken.PAREN_ABRE, "(");

        // init: puede ser declaración de tipo o asignación simple
        NodoAST init = new NodoAST("Init");
        if (esTipo()) {
            Token tipo = actual(); init.add(new NodoAST("Tipo", tipo.valor)); pos++;
            Token id   = actual(); comer(TipoToken.IDENTIFICADOR, "id");
            init.add(new NodoAST("Identificador", id.valor));
            if (comerSi(TipoToken.ASIGNACION) != null) init.add(parseExpresion());
        } else if (es(TipoToken.IDENTIFICADOR)) {
            Token id = actual(); comer(TipoToken.IDENTIFICADOR, "id");
            comer(TipoToken.ASIGNACION, ":=");
            init.add(new NodoAST("Identificador", id.valor));
            init.add(parseExpresion());
        }
        n.add(init);
        comer(TipoToken.PUNTO_COMA, ";");

        n.add(new NodoAST("Condicion").add(parseExprLogica()));
        comer(TipoToken.PUNTO_COMA, ";");

        // actualizacion: id := expr (sin ; final)
        NodoAST upd = new NodoAST("Actualizacion");
        if (es(TipoToken.IDENTIFICADOR)) {
            Token id = actual(); comer(TipoToken.IDENTIFICADOR, "id");
            comer(TipoToken.ASIGNACION, ":=");
            upd.add(new NodoAST("Identificador", id.valor));
            upd.add(parseExpresion());
        }
        n.add(upd);
        comer(TipoToken.PAREN_CIERRA, ")");
        n.add(parseBloque());
        return n;
    }

    // target '(' expr ')' '{' { case | default } '}'
    private NodoAST parseTarget() {
        NodoAST n = new NodoAST("Target", "target");
        comer(TipoToken.TARGET, "target");
        comer(TipoToken.PAREN_ABRE, "(");
        n.add(parseExpresion());
        comer(TipoToken.PAREN_CIERRA, ")");
        comer(TipoToken.LLAVE_ABRE, "{");
        while (!es(TipoToken.LLAVE_CIERRA) && !es(TipoToken.EOF)) {
            Token ct = actual();
            if (ct.tipo == TipoToken.IDENTIFICADOR && ct.valor.equals("case")) {
                NodoAST c = new NodoAST("Case"); pos++;
                c.add(parseExpresion()); comer(TipoToken.FLECHA, "->"); c.add(parseBloque()); n.add(c);
            } else if (ct.tipo == TipoToken.IDENTIFICADOR && ct.valor.equals("default")) {
                NodoAST d = new NodoAST("Default"); pos++;
                comer(TipoToken.FLECHA, "->"); d.add(parseBloque()); n.add(d);
            } else {
                error("Se esperaba case o default", "case / default", ct.valor, ct.linea, ct.columna);
                pos++;
            }
        }
        comer(TipoToken.LLAVE_CIERRA, "}");
        return n;
    }

    // extract [ expresion ] ';'
    private NodoAST parseExtract() {
        NodoAST n = new NodoAST("Extract", "extract");
        comer(TipoToken.EXTRACT, "extract");
        if (!es(TipoToken.PUNTO_COMA)) n.add(parseExpresion());
        if (comer(TipoToken.PUNTO_COMA, ";") == null)
            error("Falta ;", ";", actual().valor, actual().linea, actual().columna);
        return n;
    }

    // broadcast '(' expr { ',' expr } ')' ';'
    private NodoAST parseBroadcast() {
        NodoAST n = new NodoAST("Broadcast", "broadcast");
        comer(TipoToken.BROADCAST, "broadcast");
        comer(TipoToken.PAREN_ABRE, "(");
        n.add(parseExpresion());
        while (comerSi(TipoToken.COMA) != null) n.add(parseExpresion());
        comer(TipoToken.PAREN_CIERRA, ")");
        if (comer(TipoToken.PUNTO_COMA, ";") == null)
            error("Falta ;", ";", actual().valor, actual().linea, actual().columna);
        return n;
    }

    // scan '(' identificador ')' ';'
    private NodoAST parseScan() {
        NodoAST n = new NodoAST("Scan", "scan");
        comer(TipoToken.SCAN, "scan");
        comer(TipoToken.PAREN_ABRE, "(");
        Token id = actual(); comer(TipoToken.IDENTIFICADOR, "identificador");
        n.add(new NodoAST("Identificador", id.valor));
        comer(TipoToken.PAREN_CIERRA, ")");
        if (comer(TipoToken.PUNTO_COMA, ";") == null)
            error("Falta ;", ";", actual().valor, actual().linea, actual().columna);
        return n;
    }

    // oracle bloque [ gravemind '(' id ')' bloque ]
    private NodoAST parseOracle() {
        NodoAST n = new NodoAST("Oracle", "oracle");
        comer(TipoToken.ORACLE, "oracle");
        n.add(new NodoAST("TryBloque").add(parseBloque()));
        if (comerSi(TipoToken.GRAVEMIND) != null) {
            comer(TipoToken.PAREN_ABRE, "(");
            Token e = actual(); comer(TipoToken.IDENTIFICADOR, "identificador");
            comer(TipoToken.PAREN_CIERRA, ")");
            n.add(new NodoAST("GravemindBloque", e.valor).add(parseBloque()));
        }
        return n;
    }

    // engage tipo_retorno nombre '(' [ params ] ')' bloque
    private NodoAST parseFuncion() {
        NodoAST n = new NodoAST("Funcion");
        comer(TipoToken.ENGAGE, "engage");

        if (esTipo() || es(TipoToken.FLOOD)) {
            n.add(new NodoAST("TipoRetorno", actual().valor)); pos++;
        } else {
            error("Tipo de retorno esperado", "tipo de dato", actual().valor, actual().linea, actual().columna);
        }

        Token id = actual(); comer(TipoToken.IDENTIFICADOR, "nombre de función");
        n.add(new NodoAST("NombreFuncion", id.valor));

        comer(TipoToken.PAREN_ABRE, "(");
        NodoAST params = new NodoAST("Parametros");
        if (!es(TipoToken.PAREN_CIERRA)) {
            params.add(parseParametro());
            while (comerSi(TipoToken.COMA) != null) params.add(parseParametro());
        }
        n.add(params);
        comer(TipoToken.PAREN_CIERRA, ")");
        n.add(parseBloque());
        return n;
    }

    // tipo identificador
    private NodoAST parseParametro() {
        NodoAST n = new NodoAST("Parametro");
        if (!esTipo()) {
            error("Tipo esperado en parámetro", "tipo de dato", actual().valor, actual().linea, actual().columna);
            return n;
        }
        n.add(new NodoAST("Tipo", actual().valor)); pos++;
        Token id = actual(); comer(TipoToken.IDENTIFICADOR, "identificador");
        n.add(new NodoAST("Identificador", id.valor));
        return n;
    }

    // identificador '(' [ args ] ')'
    private NodoAST parseLlamada() {
        Token id  = actual();
        NodoAST n = new NodoAST("LlamadaFuncion", id.valor);
        comer(TipoToken.IDENTIFICADOR, "identificador");
        comer(TipoToken.PAREN_ABRE, "(");
        NodoAST args = new NodoAST("Argumentos");
        if (!es(TipoToken.PAREN_CIERRA)) {
            args.add(parseExpresion());
            while (comerSi(TipoToken.COMA) != null) args.add(parseExpresion());
        }
        n.add(args);
        comer(TipoToken.PAREN_CIERRA, ")");
        return n;
    }

    // ── expresiones ───────────────────────────────────────────

    // expresion ::= termino { ( '+' | '-' ) termino }
    private NodoAST parseExpresion() {
        NodoAST n = parseTermino();
        while (es(TipoToken.SUMA) || es(TipoToken.RESTA)) {
            Token op = actual(); pos++;
            NodoAST b = new NodoAST("Op", op.valor);
            b.add(n); b.add(parseTermino());
            n = b;
        }
        return n;
    }

    // termino ::= factor { ( '*' | '/' | '%' ) factor }
    private NodoAST parseTermino() {
        NodoAST n = parseFactor();
        while (es(TipoToken.MULT) || es(TipoToken.DIV) || es(TipoToken.MOD)) {
            Token op = actual(); pos++;
            NodoAST b = new NodoAST("Op", op.valor);
            b.add(n); b.add(parseFactor());
            n = b;
        }
        return n;
    }

    // factor ::= numero | cadena | bool | id | llamada | '(' expr ')' | '-' factor
    private NodoAST parseFactor() {
        Token t = actual();
        if (es(TipoToken.ENTERO))       { pos++; return new NodoAST("Entero",   t.valor); }
        if (es(TipoToken.DECIMAL))      { pos++; return new NodoAST("Decimal",  t.valor); }
        if (es(TipoToken.CADENA))       { pos++; return new NodoAST("Cadena",   t.valor); }
        if (es(TipoToken.BOOLEANO))     { pos++; return new NodoAST("Booleano", t.valor); }
        if (es(TipoToken.IDENTIFICADOR)) {
            if (siguiente().tipo == TipoToken.PAREN_ABRE) return parseLlamada();
            pos++; return new NodoAST("Identificador", t.valor);
        }
        if (es(TipoToken.PAREN_ABRE)) {
            pos++;
            NodoAST e = parseExpresion();
            comer(TipoToken.PAREN_CIERRA, ")");
            return e;
        }
        if (es(TipoToken.RESTA)) {
            pos++;
            NodoAST neg = new NodoAST("Negacion", "-");
            neg.add(parseFactor());
            return neg;
        }
        error("Expresión inválida: \"" + t.valor + "\"", "expresión", t.valor, t.linea, t.columna);
        pos++;
        return new NodoAST("Error", t.valor);
    }

    // expr_logica ::= expr_comp { ( '&&' | '||' ) expr_comp } | '!' expr_logica
    private NodoAST parseExprLogica() {
        if (es(TipoToken.NOT)) {
            pos++;
            NodoAST n = new NodoAST("Not", "!");
            n.add(parseExprLogica());
            return n;
        }
        NodoAST n = parseExprComparacion();
        while (es(TipoToken.AND) || es(TipoToken.OR)) {
            Token op = actual(); pos++;
            NodoAST b = new NodoAST("OpLog", op.valor);
            b.add(n); b.add(parseExprComparacion());
            n = b;
        }
        return n;
    }

    // expr_comp ::= expresion ( '==' | '!=' | '<' | '>' | '<=' | '>=' ) expresion
    private NodoAST parseExprComparacion() {
        NodoAST iz = parseExpresion();
        Set<TipoToken> ops = EnumSet.of(
            TipoToken.IGUAL, TipoToken.DIFERENTE,
            TipoToken.MENOR, TipoToken.MAYOR,
            TipoToken.MENOR_IGUAL, TipoToken.MAYOR_IGUAL
        );
        if (ops.contains(actual().tipo)) {
            Token op = actual(); pos++;
            NodoAST b = new NodoAST("Comp", op.valor);
            b.add(iz); b.add(parseExpresion());
            return b;
        }
        return iz;
    }
}
