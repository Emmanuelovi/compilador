/*
 * Nombre: Nodo.java
 * Descripción: Creación de nodos para un árbol
 * Autor: Aníbal Uriel Guijarro Rocha
 * Autor: Emmanuel Gómez Trujillo
 * Autor: Mario Alessandro López García
 * Fecha: 25 de Marzo de 2023
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
    private int info;
    private Nodo padre;
    private ArrayList<Nodo> hijos = new ArrayList();
    private String regla;
    
    //CONSTRUCTORES
    public Nodo(){
        this.info = 0;
        this.padre = null;
        this.regla = null;
    }
    
    public Nodo(int info, Nodo padre, ArrayList hijos, String regla) {
        this.info = info;
        this.padre = padre;
        this.hijos = hijos;
        this.regla = regla;
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
    
    public Nodo getHijoUnico(int indice){
        return hijos.get(indice);
    }

    public ArrayList<Nodo> getHijos() {
        return hijos;
    }

    public void setHijoUnico(Nodo hijo) {
        hijo.setPadre(this);
        hijos.add(hijo);
    }
    
    public void setHijos(ArrayList<Nodo> hijos) {
        this.hijos = hijos;
    }
    
    public String getRegla(){
        return regla;
    }
    
    public void setRegla(String regla){
        this.regla = regla;
    }
    
} //Fin de clase
