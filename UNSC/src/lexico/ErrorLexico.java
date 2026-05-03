// ErrorLexico.java
// Guarda la información de un error léxico: qué pasó, dónde y por qué.

package lexico;

public class ErrorLexico {

    public final String descripcion;
    public final String causa;
    public final int    linea;
    public final int    columna;
    public final String fragmento;

    public ErrorLexico(String descripcion, String causa, int linea, int columna, String fragmento) {
        this.descripcion = descripcion;
        this.causa       = causa;
        this.linea       = linea;
        this.columna     = columna;
        this.fragmento   = fragmento;
    }

    @Override
    public String toString() {
        return String.format("[LÉXICO] L%d:C%d — %s | Fragmento: \"%s\"",
                linea, columna, descripcion, fragmento);
    }
}
