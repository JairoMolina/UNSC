#!/bin/bash
# build.sh — compila y ejecuta el compilador UNSC
# Uso: ./build.sh          (compila y ejecuta)
#      ./build.sh compile  (solo compila)

SRC=src
OUT=out

# crear carpeta de salida si no existe
mkdir -p $OUT

echo "Compilando UNSC..."
javac -encoding UTF-8 -d $OUT -sourcepath $SRC $SRC/Main.java

if [ $? -ne 0 ]; then
    echo "Error de compilacion."
    exit 1
fi

echo "Compilacion exitosa."

if [ "$1" != "compile" ]; then
    echo "Iniciando UNSC..."
    echo ""
    java -Dfile.encoding=UTF-8 -cp $OUT Main
fi
