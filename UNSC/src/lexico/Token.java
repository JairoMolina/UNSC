// Token.java
// Representa una unidad léxica: su tipo, valor y posición en el código.

package lexico;

public class Token {

    public final TipoToken tipo;
    public final String valor;
    public final int linea;
    public final int columna;

    public Token(TipoToken tipo, String valor, int linea, int columna) {
        this.tipo    = tipo;
        this.valor   = valor;
        this.linea   = linea;
        this.columna = columna;
    }

    @Override
    public String toString() {
        return String.format("Token(%s, \"%s\", L%d:C%d)", tipo, valor, linea, columna);
    }
}
