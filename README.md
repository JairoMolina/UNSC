# UNSC — United Nations Space Code

Compilador educativo para el lenguaje de programación **UNSC**, inspirado en la saga de videojuegos **Halo**. Cada palabra reservada del lenguaje toma el nombre de un elemento icónico del universo Halo.

El compilador corre completamente en la terminal e implementa análisis léxico, análisis sintáctico y construcción del árbol de derivación (AST).

---

## Requisitos

- Java 17 o superior instalado.
- No requiere dependencias externas.

Verificar instalación:

```bash
java -version
```

---

## Cómo ejecutar

**Con el JAR:**

```bash
java -jar UNSC.jar
```

**Compilando desde el código fuente:**

```bash
# Compilar
javac -encoding UTF-8 -d out -sourcepath src src/Main.java

# Ejecutar
java -cp out Main
```

**Con el script incluido:**

```bash
chmod +x build.sh
./build.sh
```

---

## Estructura del proyecto

```
UNSC/
├── src/
│   ├── Main.java                       Punto de entrada, menú principal
│   │
│   ├── lexico/
│   │   ├── TipoToken.java              Enum con todos los tipos de token
│   │   ├── Token.java                  Clase Token (tipo, valor, línea, columna)
│   │   ├── ErrorLexico.java            Error léxico con descripción y posición
│   │   ├── Simbolo.java                Entrada de la tabla de símbolos
│   │   ├── ResultadoLexico.java        Contenedor de salida del lexer
│   │   └── AnalizadorLexico.java       Lexer: convierte código en tokens
│   │
│   ├── sintactico/
│   │   ├── NodoAST.java                Nodo del árbol de derivación
│   │   ├── ErrorSintactico.java        Error sintáctico con posición
│   │   ├── ResultadoSintactico.java    Contenedor de salida del parser
│   │   └── AnalizadorSintactico.java   Parser de descenso recursivo
│   │
│   ├── nucleo/
│   │   ├── Compilador.java             Orquesta léxico + sintáctico + impresión
│   │   └── ResultadoCompilacion.java   Agrupa ambos resultados
│   │
│   └── menu/
│       ├── MenuTerminal.java           Terminal interactiva para escribir código
│       ├── MenuComponentes.java        Tablas de reglas del lenguaje
│       └── MenuEjemplos.java           10 programas de prueba cargables
│
├── UNSC.jar                            Ejecutable listo para usar
└── build.sh                            Script de compilación y ejecución
```

---

## El lenguaje UNSC

### Palabras reservadas

Cada palabra reservada equivale a una instrucción estándar de programación y recibe el nombre de un elemento de la saga Halo.

| Palabra UNSC | Equivalente | Descripción |
|---|---|---|
| `spartan` | `int` | Tipo entero — fuerza base del Spartan |
| `ghost` | `float` | Tipo decimal — vehículo ligero y ágil |
| `cortana` | `string` | Tipo texto — IA que maneja información |
| `shield` | `bool` | Tipo booleano — escudo activo/inactivo |
| `flood` | `void` | Sin retorno — invasión sin resultado |
| `deploy` | `if` | Condicional — despliegue de acción |
| `retreat` | `else` | Alternativa — retirada táctica |
| `mission` | `while` | Ciclo con condición — misión en curso |
| `ops` | `for` | Ciclo contado — operaciones repetitivas |
| `target` | `switch` | Selección múltiple — selección de objetivo |
| `extract` | `return` | Retorno de valor — extraer resultado |
| `broadcast` | `print` | Salida estándar — difusión de mensaje |
| `scan` | `input` | Entrada de datos — escaneo |
| `arbiter` | `class` | Clase — jerarquía definida |
| `chief` | `object` | Objeto — instancia única (Jefe Maestro) |
| `plasma` | `array` | Arreglo — energía acumulada |
| `banshee` | `break` | Romper ciclo — maniobra evasiva |
| `sentinel` | `continue` | Continuar ciclo — patrulla constante |
| `oracle` | `try` | Bloque de intento — guía en incertidumbre |
| `gravemind` | `catch` | Captura de error — entidad que atrapa |
| `warthog` | `new` | Crear instancia — vehículo desplegado |
| `engage` | `function` | Declarar función — iniciar combate |
| `base` | `main` | Punto de entrada — base de operaciones |

### Tipos de dato

```
spartan   →  entero        spartan nivel := 117;
ghost     →  decimal       ghost velocidad := 9.8;
cortana   →  texto         cortana nombre := "Chief";
shield    →  booleano      shield activo := shield:true;
flood     →  void          engage flood saludar() { }
```

Los literales booleanos tienen sintaxis propia: `shield:true` y `shield:false`.

### Operadores

| Categoría | Operadores |
|---|---|
| Aritméticos | `+`  `-`  `*`  `/`  `%` |
| Comparación | `==`  `!=`  `<`  `>`  `<=`  `>=` |
| Lógicos | `&&`  `\|\|`  `!` |
| Asignación | `:=` |

> El operador de asignación es `:=` — el símbolo `=` solo no es válido en UNSC.

### Símbolos permitidos

```
:=    Asignación
+  -  *  /  %    Aritméticos
==  !=  <  >  <=  >=    Comparación
&&  ||  !    Lógicos
->    Flecha (usado en target/case)
(  )    Paréntesis
{  }    Llaves de bloque
;    Punto y coma (fin de instrucción)
,    Coma (separador de argumentos)
"..."    Cadena de texto
//    Comentario de línea
/* */    Comentario de bloque
```

### Estructuras del lenguaje

**Punto de entrada — obligatorio en todo programa:**

```
base() {
    // instrucciones
}
```

**Declaración de variable:**

```
spartan x := 10;
cortana nombre := "Spartan";
shield activo;
```

**Asignación:**

```
x := x + 1;
```

**Condicional:**

```
deploy (condicion) {
    // si verdadero
} retreat {
    // si falso
}
```

**Ciclo while:**

```
mission (condicion) {
    // cuerpo
}
```

**Ciclo for:**

```
ops (spartan i := 0; i < 10; i := i + 1) {
    // cuerpo
}
```

**Función:**

```
engage spartan suma(spartan a, spartan b) {
    extract a + b;
}
```

**Salida e entrada:**

```
broadcast("Hola,", nombre);
scan(nombre);
```

**Control de ciclos:**

```
banshee;     // break
sentinel;    // continue
```

**Manejo de errores:**

```
oracle {
    // código que puede fallar
} gravemind (error) {
    // manejo del error
}
```

**Comentarios:**

```
// comentario de una línea

/* comentario
   de varias líneas */
```

---

## Menú del compilador

Al ejecutar el programa aparece un menú con tres opciones:

```
MENU PRINCIPAL
--------------
1. Terminal
2. Componentes del lenguaje
3. Ejemplos de codigo
0. Salir
```

**1. Terminal**

Editor interactivo línea a línea. Comandos disponibles:

| Comando | Acción |
|---|---|
| `RUN` | Compila el código escrito y muestra el resultado |
| `SHOW` | Muestra el código acumulado hasta el momento |
| `CLEAR` | Borra el código y reinicia el editor |
| `EXIT` | Vuelve al menú principal |

**2. Componentes del lenguaje**

Submenú con cuatro tablas de referencia:

- Palabras reservadas con su equivalente y descripción
- Operaciones permitidas con ejemplos
- Símbolos permitidos con ejemplos
- Estructuras de cada instrucción con su sintaxis completa

**3. Ejemplos de código**

10 programas precargados para compilar de inmediato — 5 correctos y 5 con errores intencionados para ilustrar la detección de errores.

---

## Análisis léxico

El analizador léxico (`AnalizadorLexico.java`) convierte el código fuente en una secuencia de tokens aplicando expresiones regulares en orden de prioridad.

**Flujo:**

```
Código fuente  →  [Lexer]  →  Lista de tokens + Errores léxicos + Tabla de símbolos
```

**Expresiones regulares aplicadas (en orden):**

| Token | Patrón |
|---|---|
| Comentario bloque | `/\*[\s\S]*?\*/` |
| Comentario línea | `//[^\n]*` |
| Booleano | `shield:(true\|false)` |
| Decimal | `\d+\.\d+` |
| Entero | `\d+` |
| Cadena | `"([^"\\]\|\\.)* "` |
| Identificador / Palabra reservada | `[a-zA-Z_][a-zA-Z0-9_]*` |
| Operadores 2 caracteres | `:= == != <= >= && \|\| ->` |
| Operadores 1 carácter | `+ - * / % < > !` |
| Delimitadores | `; , ( ) { } [ ] .` |

**Salida del análisis léxico:**

Al compilar, el programa muestra:

- Tabla de tokens con tipo, valor, línea y columna
- Tabla de errores léxicos con descripción, causa y posición
- Tabla de símbolos con los identificadores declarados

**Ejemplo de tabla de tokens:**

```
+----+---------------+---------+-----+-----+
| #  | TIPO          | VALOR   | LIN | COL |
+----+---------------+---------+-----+-----+
| 1  | BASE          | base    | 1   | 1   |
| 2  | PAREN_ABRE    | (       | 1   | 5   |
| 3  | PAREN_CIERRA  | )       | 1   | 6   |
| 4  | LLAVE_ABRE    | {       | 1   | 8   |
| 5  | SPARTAN       | spartan | 2   | 5   |
| 6  | IDENTIFICADOR | x       | 2   | 13  |
| 7  | ASIGNACION    | :=      | 2   | 15  |
| 8  | ENTERO        | 42      | 2   | 18  |
+----+---------------+---------+-----+-----+
```

---

## Análisis sintáctico

El analizador sintáctico (`AnalizadorSintactico.java`) recibe la lista de tokens y construye el árbol de derivación (AST) verificando que la secuencia sea gramaticalmente correcta.

Implementa un **parser de descenso recursivo**: cada método corresponde a un no-terminal de la gramática EBNF.

**Flujo:**

```
Lista de tokens  →  [Parser]  →  AST + Errores sintácticos
```

**Gramática EBNF (resumen):**

```
programa        ::= { declaracion_funcion } base '(' ')' bloque
bloque          ::= '{' { instruccion } '}'
instruccion     ::= declaracion_var | asignacion | condicional
                  | ciclo_mission | ciclo_ops | llamada ';'
                  | retorno | broadcast | scan | banshee | sentinel
declaracion_var ::= tipo identificador [ ':=' expresion ] ';'
tipo            ::= spartan | ghost | cortana | shield | flood
asignacion      ::= identificador ':=' expresion ';'
condicional     ::= deploy '(' expr_logica ')' bloque [ retreat bloque ]
ciclo_mission   ::= mission '(' expr_logica ')' bloque
ciclo_ops       ::= ops '(' init ';' expr_logica ';' actualizacion ')' bloque
funcion         ::= engage tipo nombre '(' [ params ] ')' bloque
retorno         ::= extract [ expresion ] ';'
broadcast       ::= broadcast '(' expresion { ',' expresion } ')' ';'
expresion       ::= termino { ( '+' | '-' ) termino }
termino         ::= factor { ( '*' | '/' | '%' ) factor }
factor          ::= numero | cadena | booleano | identificador | llamada | '(' expresion ')'
expr_logica     ::= expr_comp { ( '&&' | '||' ) expr_comp } | '!' expr_logica
expr_comp       ::= expresion ( '==' | '!=' | '<' | '>' | '<=' | '>=' ) expresion
```

**Ejemplo de árbol de derivación:**

Código:
```
base() {
    spartan x := 42;
    broadcast("Hola", x);
}
```

Árbol generado:
```
L__ Programa
    L__ EntradaPrincipal: base
        L__ Bloque
            |-- DeclaracionVar
            |   |-- Tipo: spartan
            |   |-- Identificador: x
            |   L__ Entero: 42
            L__ Broadcast: broadcast
                |-- Cadena: "Hola"
                L__ Identificador: x
```

**Recuperación de errores:**

Cuando el parser encuentra un error no se detiene — aplica recuperación por pánico: avanza hasta un token de sincronización (`;` o `}`) y continúa analizando para reportar todos los errores en una sola pasada.

---

## Ejemplos de código

### Programas válidos

**Variables y tipos:**

```
base() {
    spartan nivel := 117;
    ghost velocidad := 9.8;
    cortana nombre := "Master Chief";
    shield activo := shield:true;
    broadcast("Spartan:", nombre, "Nivel:", nivel);
}
```

**Condicional:**

```
base() {
    spartan energia := 75;
    deploy (energia > 60) {
        broadcast("Estado: COMBATE");
    } retreat {
        broadcast("Estado: RETIRADA");
    }
}
```

**Ciclo mission:**

```
base() {
    spartan i := 1;
    spartan suma := 0;
    mission (i <= 10) {
        suma := suma + i;
        i := i + 1;
    }
    broadcast("Suma:", suma);
}
```

**Ciclo ops:**

```
base() {
    spartan i;
    ops (i := 1; i <= 10; i := i + 1) {
        broadcast("Iteracion:", i);
    }
}
```

**Función:**

```
engage spartan factorial(spartan n) {
    spartan resultado := 1;
    spartan i;
    ops (i := 1; i <= n; i := i + 1) {
        resultado := resultado * i;
    }
    extract resultado;
}

base() {
    spartan f := factorial(6);
    broadcast("Factorial de 6:", f);
}
```

### Programas con errores

**Carácter no permitido:**

```
base() {
    spartan x := 10 @ 2;   // '@' no pertenece al alfabeto
}
```

Error detectado: `[LÉXICO] Carácter no permitido: '@'`

**Cadena sin cerrar:**

```
base() {
    cortana msg := "Hola Spartan;   // falta comilla de cierre
    broadcast(msg);
}
```

Error detectado: `[LÉXICO] Cadena sin cerrar`

**Falta punto y coma:**

```
base() {
    spartan x := 10      // falta ;
    broadcast(x);
}
```

Error detectado: `[SINTÁCTICO] Se esperaba ;`

**Paréntesis sin cerrar:**

```
base() {
    deploy (x > 5 {      // falta )
        broadcast("ok");
    }
}
```

Error detectado: `[SINTÁCTICO] Se esperaba )`

**Llave de cierre faltante:**

```
base() {
    spartan x := 42;
    broadcast(x);
                         // falta }
```

Error detectado: `[SINTÁCTICO] Bloque sin cerrar: falta }`

---

## Errores detectados

### Errores léxicos

| Error | Causa | Ejemplo |
|---|---|---|
| Carácter no permitido | Símbolo fuera del alfabeto UNSC | `x @ 2` |
| Cadena sin cerrar | Falta comilla de cierre | `"Hola Spartan` |

### Errores sintácticos

| Error | Causa | Ejemplo |
|---|---|---|
| Falta `;` | Instrucción sin punto y coma final | `spartan x := 5` |
| Paréntesis sin cerrar | Falta `)` en condición o llamada | `deploy (x > 0 {` |
| Llave sin cerrar | Falta `}` de cierre de bloque | `base() { spartan x := 1;` |
| Falta `base()` | El programa no tiene punto de entrada | programa sin `base()` |
| Instrucción inválida | Token inesperado como instrucción | `42;` suelto |
| Tipo de retorno faltante | `engage` sin tipo declarado | `engage miFuncion() { }` |
