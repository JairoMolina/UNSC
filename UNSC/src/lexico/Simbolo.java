// Simbolo.java
// Entrada de la tabla de símbolos: nombre, tipo inferido, valor y posición.

package lexico;

public class Simbolo {

    public final String nombre;
    public String tipo;   // se actualiza si el parser infiere el tipo
    public String valor;
    public final int linea;
    public final int columna;

    public Simbolo(String nombre, int linea, int columna) {
        this.nombre  = nombre;
        this.tipo    = "desconocido";
        this.valor   = null;
        this.linea   = linea;
        this.columna = columna;
    }
}
