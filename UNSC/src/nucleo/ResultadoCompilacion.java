// ResultadoCompilacion.java
// Contenedor con los resultados léxicos y sintácticos de una compilación.

package nucleo;

import lexico.ResultadoLexico;
import sintactico.ResultadoSintactico;

public class ResultadoCompilacion {

    public final ResultadoLexico    lexico;
    public final ResultadoSintactico sintactico;

    public ResultadoCompilacion(ResultadoLexico lexico, ResultadoSintactico sintactico) {
        this.lexico    = lexico;
        this.sintactico = sintactico;
    }
}
