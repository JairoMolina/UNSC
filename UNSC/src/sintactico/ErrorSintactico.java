// ErrorSintactico.java
// Guarda un error sintáctico: qué se esperaba, qué se encontró y en qué posición.

package sintactico;

public class ErrorSintactico {

    public final String descripcion;
    public final String esperado;
    public final String encontrado;
    public final int    linea;
    public final int    columna;

    public ErrorSintactico(String descripcion, String esperado, String encontrado,
                           int linea, int columna) {
        this.descripcion = descripcion;
        this.esperado    = esperado;
        this.encontrado  = encontrado;
        this.linea       = linea;
        this.columna     = columna;
    }

    @Override
    public String toString() {
        return String.format("[SINTÁCTICO] L%d:C%d — %s | Esperado: \"%s\" | Encontrado: \"%s\"",
                linea, columna, descripcion, esperado, encontrado);
    }
}
