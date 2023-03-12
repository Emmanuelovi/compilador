/*
 * Nombre: Nodo.java
 * Descripción: Creación de nodos para un árbol
 * Autor: Aníbal Uriel Guijarro Rocha
 * Autor: Emmanuel Gómez Trujillo
 * Autor: Mario Alessandro López García
 * Fecha: 12 de Marzo de 2023
 */
package compilador;

/**
 * @author Anibal Uriel Guijarro Rocha
 * @author Emmanuel Gómez Trujillo
 * @author Mario Alessandro López García
 */
public class Nodo {
    //VARIABLES
    int info; //Información a almacenar en nodos (Identificadores)
    Nodo terminal; //Nodo terminal hijo
    Nodo noTerminal; //Nodo no terminal hijo
    Nodo padre; //Nodo raíz (padre)
    
    //CONSTRUCTORES
    public Nodo(){
        this.info = 0;
        this.terminal = null;
        this.noTerminal = null;
        this.padre = null;
    }
    
    public Nodo(int info, Nodo terminal, Nodo noTerminal, Nodo padre){
        this.info = info;
        this.terminal = terminal;
        this.noTerminal = noTerminal;
        this.padre = padre;
    }
    
    //MÉTODOS
    public int getInfo() {
        return info;
    }

    public void setInfo(int info) {
        this.info = info;
    }

    public Nodo getTerminal() {
        return terminal;
    }

    public void setTerminal(Nodo terminal) {
        this.terminal = terminal;
    }

    public Nodo getNoTerminal() {
        return noTerminal;
    }

    public void setNoTerminal(Nodo noTerminal) {
        this.noTerminal = noTerminal;
    }

    public Nodo getPadre() {
        return padre;
    }

    public void setPadre(Nodo padre) {
        this.padre = padre;
    }
    
} //Fin de clase
