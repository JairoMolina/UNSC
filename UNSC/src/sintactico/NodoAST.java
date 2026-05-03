// NodoAST.java
// Nodo del árbol de sintaxis abstracta (AST).
// Cada nodo tiene un tipo, un valor opcional y una lista de hijos.

package sintactico;

import java.util.ArrayList;
import java.util.List;

public class NodoAST {

    public final String tipo;
    public final String valor;
    public final List<NodoAST> hijos;

    public NodoAST(String tipo, String valor) {
        this.tipo  = tipo;
        this.valor = valor;
        this.hijos = new ArrayList<>();
    }

    public NodoAST(String tipo) {
        this(tipo, null);
    }

    // agrega un hijo y devuelve this para encadenar
    public NodoAST add(NodoAST hijo) {
        if (hijo != null) hijos.add(hijo);
        return this;
    }

    // texto que representa este nodo en el árbol
    public String etiqueta() {
        return (valor != null && !valor.isEmpty()) ? tipo + ": " + valor : tipo;
    }
}
