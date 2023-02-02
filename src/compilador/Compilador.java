/*
 * Nombre: Compilador.java
 * Descripción: Analisis lexico
 * Autor: Aníbal Uriel Guijarro Rocha
 * Autor: Emmanuel Gómez Trujillo
 * Fecha: 02 de febrero de 2023
 */
package compilador;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * @author Anibal Uriel Guijarro Rocha
 * @author Emmanuel Gómez Trujillo
 */
public class Compilador {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        escritura(lectura("suma.txt"), "prueba.txt");
        escritura(detector("prueba.txt", "reservadas.txt"), "hola.txt");
        
    } //Fin de main
    
    /**
     * Método para leer un archivo de texto, posteriormente realiza una serie de clasificaciones (analisis lexico)
     * donde se eliminan los espacios en blanco (duplicados y del final de la linea), lineas en blanco y los comentarios; dejando solamente el codigo
     * @param archivo Nombre del archivo a leer
     * @return Cadena de texto con el contenido del archivo de texto ya procesado
     * @throws FileNotFoundException 
     */
    public static String lectura(String archivo) throws FileNotFoundException{
        //VARIABLES
        Scanner in = null; //Para leer el archivo txt
        String linea = ""; //Variable que almacenará la línea en curso del archivo que se está leyendo
        String contenido = ""; //Variable que tendrá el contenido ya procesado para el nuevo archivo de texto
        String [] eliminarComentarios; //Arreglo para eliminar los comentarios simples (Es decir, solo para cuando comience con //)
        int contador = 0; //Contador de puntos y comas dentro del programa analizado
        boolean comentario = false; //Para identificar los comentarios de varias lineas
        
        try{
            
            in = new Scanner(new FileReader(archivo)); //Abrir el fichero de texto con FileReader (Iniciador)
            
            while(in.hasNextLine()){
                
                /**
                 * Notas de quitar espacios en blanco:
                 * - s.replaceAll("\\s+", " "); //Elimina todos los espacios en blanco duplicados (NO LOS DEL INICIO Y FINAL)
                 * - s.trim().replaceAll("\\s+", " "); //Elimina todos los espacios en blanco duplicados, incluyendo los del inicio y final de linea
                 */
                linea = in.nextLine().trim(); //Eliminar los espacios en blanco del inicio
                
                /**
                 * Nota: Serie de 'IFs' para detectar los comentarios de varias lineas para ignorarlos y no guardarlos en el nuevo fichero
                 */
                
                if(linea.startsWith("/*")){ //En caso de detectar el INICIO de un comentario de varias lineas
                    
                    comentario = true;
                    
                }
                
                if(linea.isBlank()==false && comentario==false){ //En caso de NO ser una linea en blanco y no ser un comentario de varias lineas
                    
                    eliminarComentarios = linea.split("//"); //Guardar en un arreglo la parte de código [0] y la parte de comentario [1] (Separación)
                    
                    if(eliminarComentarios[0].isBlank()==false){ //En caso de que la primera posicion del arreglo NO sea una linea en blanco
                        
                        //eliminarComentarios[0] = eliminarComentarios[0].replaceAll("\\s*$",""); //Eliminar los espacio en blanco del final y guardar en el mismo campo
                        
                        contenido = contenido + eliminarComentarios[0].trim().replaceAll("\\s+"," ") + "\n"; //Guardar en 'contenido' la parte del código primoridial
                        
                        if(eliminarComentarios[0].endsWith(";")){ //En caso de detectar un punto y coma al final de la instrucciones
                            
                            contador++;
                            
                        }
                        
                    }
                    
                }
                
                if(linea.startsWith("*/")){ //En caso de detectar el FIN de un comentario de varias lineas
                    
                    comentario = false;
                    
                }
                
            }
            
            //contenido = contenido + "CANTIDAD DE PUNTOS Y COMAS (;) = " + contador;
            
            return contenido;
            
        }finally{
            if(in!=null){ //En caso de estar abierto el documento
                in.close(); //Cerrar el documento
            }
        }
    } //Fin de método 'lectura'
    
    /**
     * Método para la escritura de un archivo de texto. Escribe directamente el parametro de texto en el fichero
     * @param contenido Cadena de texto que contiene todo el texto a escribir en el nuevo archivo
     * @param nuevoArchivo Nombre del nuevo archivo a escribir
     * @throws IOException 
     */
    public static void escritura(String contenido, String nuevoArchivo) throws IOException{
        //VARIABLES
        PrintWriter out = null;
        
        try{
            
            out = new PrintWriter(new FileWriter(nuevoArchivo));
            out.println(contenido); //Escritura en el fichero
            
        }finally{
            if(out!=null){ //En caso de estar abierto el documento
                out.close(); //Cerrar el documento
            }
        }
        
    } //Fin de método 'escritura'
    
    public static String detector(String archivo, String reservadas) throws FileNotFoundException{
        //VARIABLES
        Scanner in = null; //Para leer el 'archivo.txt'
        Scanner in2 = null; //Para leer el 'reservadas.txt'
        boolean bandera = false;
        String linea;
        String palabra;
        String contenidoSeparado= "";
        
        try{
            
            in = new Scanner(new FileReader(archivo)); //Abrir el fichero de texto con FileReader (Iniciador)
            
            
            while(in.hasNextLine()){
                
                in2 = new Scanner(new FileReader(reservadas)); //Abrir el fichero de texto con FileReader (Iniciador)
                linea = in.nextLine();
                bandera = false;
                
                while(bandera==false && in2.hasNextLine()){
                    
                    palabra = in2.nextLine();
                    
                    if(linea.startsWith(palabra))
                    {
                        contenidoSeparado = contenidoSeparado + palabra + "\n";
                        //System.out.println("Entro 1" + contenidoSeparado);
                        
                        if(linea.split(palabra).length<1){
                            bandera = true;
                        }else{
                            linea = linea.split(palabra)[1];
                        }
                        
                    }else if(linea.contains(palabra)){
                        System.out.println(palabra + "--- jojo --- " + linea);
                        System.out.println(linea.split("\\{").length);
                        contenidoSeparado = contenidoSeparado + linea.split(palabra)[0] + "\n" + palabra + "\n";
                        linea = linea.replace(linea.split(palabra)[0], "");
                        linea = linea.replace(palabra, "");
                    }
                    
                    
                    
                }
                
                
                
            }
            
            return contenidoSeparado;
            
        }finally{
            if(in!=null){ //En caso de estar abierto el documento
                in.close(); //Cerrar el documento
            }
            if(in2!=null){ //En caso de estar abierto el documento
                in2.close(); //Cerrar el documento
            }
        }
        
    } //Fin de método 'detector'
    
} //Fin de clase