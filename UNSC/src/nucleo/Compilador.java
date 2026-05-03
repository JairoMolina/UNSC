// Compilador.java
// Orquesta el análisis léxico y sintáctico.
// Recibe código fuente y devuelve el resultado completo para mostrarlo en la terminal.

package nucleo;

import lexico.*;
import sintactico.*;

import java.util.*;

public class Compilador {

    private final AnalizadorLexico    lexer  = new AnalizadorLexico();
    private final AnalizadorSintactico parser = new AnalizadorSintactico();

    public ResultadoCompilacion compilar(String src) {
        ResultadoLexico    rl = lexer.analizar(src);
        ResultadoSintactico rs = parser.analizar(rl.tokens);
        return new ResultadoCompilacion(rl, rs);
    }

    // ── impresión de resultados ───────────────────────────────

    public void imprimirResultado(ResultadoCompilacion rc) {
        int totalErr = rc.lexico.errores.size() + rc.sintactico.errores.size();

        System.out.println();
        if (totalErr == 0) {
            System.out.println("  [ OK ]  Compilacion exitosa - sin errores.");
        } else {
            System.out.println("  [ ERROR ]  " + totalErr + " error(es) detectado(s).");
        }
        System.out.println("  Tokens: "   + contarTokensVisibles(rc.lexico.tokens)
                         + "  |  Lexicos: " + rc.lexico.errores.size()
                         + "  |  Sintacticos: " + rc.sintactico.errores.size()
                         + "  |  Simbolos: " + rc.lexico.tablaSimbolos.size());
        System.out.println();

        imprimirErrores(rc);
        imprimirTokens(rc);
        imprimirAST(rc.sintactico.ast, "", true);
        imprimirSimbolos(rc);
    }

    private int contarTokensVisibles(List<Token> tokens) {
        int c = 0;
        for (Token t : tokens)
            if (t.tipo != TipoToken.COMENTARIO && t.tipo != TipoToken.EOF) c++;
        return c;
    }

    // ── errores ───────────────────────────────────────────────

    private void imprimirErrores(ResultadoCompilacion rc) {
        List<String[]> filas = new ArrayList<>();
        for (ErrorLexico e : rc.lexico.errores)
            filas.add(new String[]{ "LEXICO", e.descripcion, e.causa, e.fragmento,
                                    String.valueOf(e.linea), String.valueOf(e.columna) });
        for (ErrorSintactico e : rc.sintactico.errores)
            filas.add(new String[]{ "SINTACTICO", e.descripcion, e.esperado, e.encontrado,
                                    String.valueOf(e.linea), String.valueOf(e.columna) });

        if (filas.isEmpty()) return;

        System.out.println("  ERRORES:");
        imprimirTabla(
            new String[]{ "TIPO", "DESCRIPCION", "ESPERADO/CAUSA", "FRAGMENTO", "LIN", "COL" },
            filas
        );
    }

    // ── tokens ────────────────────────────────────────────────

    private void imprimirTokens(ResultadoCompilacion rc) {
        List<String[]> filas = new ArrayList<>();
        int num = 1;
        for (Token t : rc.lexico.tokens) {
            if (t.tipo == TipoToken.COMENTARIO || t.tipo == TipoToken.EOF) continue;
            String val = t.valor.length() > 28 ? t.valor.substring(0, 28) + "..." : t.valor;
            filas.add(new String[]{ String.valueOf(num++), t.tipo.name(), val,
                                    String.valueOf(t.linea), String.valueOf(t.columna) });
        }
        System.out.println("  TOKENS:");
        imprimirTabla(new String[]{ "#", "TIPO", "VALOR", "LIN", "COL" }, filas);
    }

    // ── árbol de derivación ───────────────────────────────────

    public void imprimirAST(NodoAST nodo, String prefijo, boolean esUltimo) {
        if (nodo == null) return;
        String conector = esUltimo ? "L__ " : "|-- ";
        System.out.println(prefijo + conector + nodo.etiqueta());
        String nuevoPrefijo = prefijo + (esUltimo ? "    " : "|   ");
        for (int i = 0; i < nodo.hijos.size(); i++) {
            imprimirAST(nodo.hijos.get(i), nuevoPrefijo, i == nodo.hijos.size() - 1);
        }
    }

    // ── tabla de símbolos ─────────────────────────────────────

    private void imprimirSimbolos(ResultadoCompilacion rc) {
        if (rc.lexico.tablaSimbolos.isEmpty()) return;
        List<String[]> filas = new ArrayList<>();
        for (Simbolo s : rc.lexico.tablaSimbolos.values())
            filas.add(new String[]{ s.nombre, s.tipo, s.valor != null ? s.valor : "--",
                                    String.valueOf(s.linea), String.valueOf(s.columna) });
        System.out.println("  TABLA DE SIMBOLOS:");
        imprimirTabla(new String[]{ "NOMBRE", "TIPO", "VALOR", "LINEA", "COL" }, filas);
    }

    // ── utilidad: tabla con columnas ajustadas ─────────────────

    public static void imprimirTabla(String[] headers, List<String[]> filas) {
        // calcular anchos máximos por columna
        int[] anchos = new int[headers.length];
        for (int i = 0; i < headers.length; i++) anchos[i] = headers[i].length();
        for (String[] f : filas)
            for (int i = 0; i < Math.min(f.length, anchos.length); i++)
                anchos[i] = Math.max(anchos[i], f[i].length());

        String sep   = buildSep(anchos);
        String header = buildFila(headers, anchos);

        System.out.println("  " + sep);
        System.out.println("  " + header);
        System.out.println("  " + sep);
        for (String[] f : filas)
            System.out.println("  " + buildFila(f, anchos));
        System.out.println("  " + sep);
        System.out.println();
    }

    private static String buildSep(int[] anchos) {
        StringBuilder sb = new StringBuilder("+");
        for (int a : anchos) sb.append("-").append("-".repeat(a)).append("-+");
        return sb.toString();
    }

    private static String buildFila(String[] celdas, int[] anchos) {
        StringBuilder sb = new StringBuilder("|");
        for (int i = 0; i < anchos.length; i++) {
            String c = (i < celdas.length) ? celdas[i] : "";
            sb.append(" ").append(pad(c, anchos[i])).append(" |");
        }
        return sb.toString();
    }

    private static String pad(String s, int n) {
        if (s.length() >= n) return s.substring(0, n);
        return s + " ".repeat(n - s.length());
    }
}
