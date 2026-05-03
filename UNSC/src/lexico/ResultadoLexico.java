// ResultadoLexico.java
// Agrupa lo que produce el analizador léxico: tokens, errores y tabla de símbolos.

package lexico;

import java.util.List;
import java.util.Map;

public class ResultadoLexico {

    public final List<Token>      tokens;
    public final List<ErrorLexico> errores;
    public final Map<String, Simbolo> tablaSimbolos;

    public ResultadoLexico(List<Token> tokens,
                           List<ErrorLexico> errores,
                           Map<String, Simbolo> tablaSimbolos) {
        this.tokens        = tokens;
        this.errores       = errores;
        this.tablaSimbolos = tablaSimbolos;
    }
}
