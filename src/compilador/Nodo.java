/*
 * Nombre: Nodo.java
 * Descripción: Creación de nodos para un árbol
 * Autor: Aníbal Uriel Guijarro Rocha
 * Autor: Emmanuel Gómez Trujillo
 * Autor: Mario Alessandro López García
 * Fecha: 23 de Marzo de 2023
 */
package compilador;

import java.util.ArrayList;

/**
 * @author Anibal Uriel Guijarro Rocha
 * @author Emmanuel Gómez Trujillo
 * @author Mario Alessandro López García
 */
public class Nodo {
    //VARIABLES
    int info;
    Nodo padre;
    ArrayList <Nodo> hijos = new ArrayList();
    
    //CONSTRUCTORES
    public Nodo(){
        this.info = 0;
        this.padre = null;
        this.hijos = null;
    }
    
    public Nodo(int info, Nodo padre, ArrayList hijos) {
        this.info = info;
        this.padre = padre;
        this.hijos = hijos;
    }
    
    //MÉTODOS
    public int getInfo() {
        return info;
    }

    public void setInfo(int info) {
        this.info = info;
    }

    public Nodo getPadre() {
        return padre;
    }

    public void setPadre(Nodo padre) {
        this.padre = padre;
    }

    public ArrayList getHijos() {
        return hijos;
    }

    public void setHijos(ArrayList hijos) {
        this.hijos = hijos;
    }
    
    /*
    int info;
    Nodo padre;
    String tipo = "Terminal | No terminal";
    
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
    
*/



    

} //Fin de clase
