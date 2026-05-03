// ResultadoSintactico.java
// Agrupa lo que produce el analizador sintáctico: el AST y los errores.

package sintactico;

import java.util.List;

public class ResultadoSintactico {

    public final NodoAST           ast;
    public final List<ErrorSintactico> errores;

    public ResultadoSintactico(NodoAST ast, List<ErrorSintactico> errores) {
        this.ast     = ast;
        this.errores = errores;
    }
}
