/*
 * Nombre: Compilador.java
 * Descripción: Analisis lexico
 * Autor: Aníbal Uriel Guijarro Rocha
 * Autor: Emmanuel Gómez Trujillo
 * Autor: Mario Alessandro
 * Fecha: 09 de febrero de 2023
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
 * @author Mario Alessandro
 */
public class Compilador {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        escritura(eliminarComentarios("codigoFuente.txt"), "codigoSinComentarios.txt");
        escritura(analisisLexico("codigoSinComentarios.txt"), "tokens.txt");
        escritura(separarTokens("tokens.txt"), "tokens.txt");
        
    } //Fin de main
    
    //MÉTODOS
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
    
    /**
     * Método para leer un archivo de texto, posteriormente realiza una serie de clasificaciones (analisis lexico)
     * donde se eliminan los espacios en blanco del inicio, lineas en blanco y los comentarios; dejando solamente el codigo
     * @param archivo Nombre del archivo a leer
     * @return Cadena de texto con el contenido del archivo de texto ya procesado
     * @throws FileNotFoundException 
     */
    public static String eliminarComentarios(String archivo) throws FileNotFoundException{
        //VARIABLES
        Scanner in = null; //Para leer el archivo txt
        String linea = ""; //Variable que almacenará la línea en curso del archivo que se está leyendo
        String contenido = ""; //Variable que tendrá el contenido ya procesado para el nuevo archivo de texto
        String [] eliminarComentarios; //Arreglo para eliminar los comentarios simples (Es decir, solo para cuando comience con //)
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
                        contenido = contenido + eliminarComentarios[0] + "\n"; //Guardar en 'contenido' la parte del código primoridial
                    }
                    
                }
                
                if(linea.endsWith("*/")){ //En caso de detectar el FIN de un comentario de varias lineas
                    comentario = false;
                }
                
            }
            
            return contenido;
            
        }finally{
            if(in!=null){ //En caso de estar abierto el documento
                in.close(); //Cerrar el documento
            }
        }
    } //Fin de método 'eliminarComentarios'
    
    /**
     * Método para identificar las partes primordiales del código (caracteres especiales, metacaracteres y operadores)
     * @param archivo Nombre del archivo a leer
     * @return Cadena de texto con el contenido del archivo de texto ya procesado (Identificación de caracteres y operadores)
     * @throws FileNotFoundException 
     */
    public static String analisisLexico(String archivo) throws FileNotFoundException{
        //VARIABLES
        Scanner in = null; //Variable para leer el archivo txt (programa fuente sin comentarios)
        String linea; //Variable que almacenará la línea en curso del archivo que se está leyendo
        String [] caracteres; //Variable (arreglo) que almacenará cada uno de los caracteres de la linea, para identificar 'caracteres especiales'
        
        String contenidoSeparado = "";
        
        try{
            
            in = new Scanner(new FileReader(archivo)); //Abrir el fichero de texto con FileReader (Iniciador)
            
            while(in.hasNextLine()){
                
                linea = in.nextLine();
                caracteres = linea.split("");
                
                for(int i=0; i<caracteres.length; i++){
                    //Identificar caracteres especiales o metacaracteres
                    if(caracteres[i].matches(";")){
                        linea = linea.replaceAll(";","\n;\n");
                    }
                    else if(caracteres[i].equals(",")){
                        linea = linea.replaceAll(",","\n,\n");
                    }
                    else if(caracteres[i].matches("\\(")){
                        linea = linea.replaceAll("\\(","\n(\n");
                    }
                    else if(caracteres[i].matches("\\)")){
                        linea = linea.replaceAll("\\)","\n)\n");
                    }
                    else if(caracteres[i].matches("\\.")){
                        linea = linea.replaceAll("\\.", "\n.\n");
                    }
                    else if(caracteres[i].matches("\\{")){
                        linea = linea.replaceAll("\\{", "\n{\n");
                    }
                    else if(caracteres[i].matches("\\}")){
                        linea = linea.replaceAll("\\}", "\n}\n");
                    }
                    else if(caracteres[i].matches("\\[")){
                        linea = linea.replaceAll("\\[", "\n[\n");
                    }
                    else if(caracteres[i].matches("\\]")){
                        linea = linea.replaceAll("\\]", "\n]\n");
                    }
                    else if(caracteres[i].matches("\\+")){
                        linea = linea.replaceAll("\\+", "\n+\n");
                    }
                    else if(caracteres[i].matches("\\-")){
                        linea = linea.replaceAll("\\-", "\n-\n");
                    }
                    else if(caracteres[i].matches("\\*")){
                        linea = linea.replaceAll("\\*", "\n*\n");
                    }
                    else if(caracteres[i].matches("/")){
                        linea = linea.replaceAll("/", "\n/\n");
                    }
                    else if(caracteres[i].matches("\"")){
                        linea = linea.replaceAll("\"", "\n\"\n");
                    }
                    else if(caracteres[i].matches("\\/")){
                        linea = linea.replaceAll("\\/", "\n/\n");
                    }
                }
                
                //Identificar operadores de comparación (Varios IF's para caso de haber varios en una misma linea)
                if(linea.contains("||")){
                    linea = linea.replaceAll("||", "\n||\n");
                }
                
                if(linea.contains(">=")){
                    linea = linea.replaceAll(">=", "\n>=\n");
                }
                else if(linea.contains(">")){
                    linea = linea.replaceAll(">", "\n>\n");
                }
                
                if(linea.contains("<=")){
                    linea = linea.replaceAll("<=", "\n<=\n");
                }
                else if(linea.contains("<")){
                    linea = linea.replaceAll("<", "\n<\n");
                }
                
                if(linea.contains("==")){
                    linea = linea.replaceAll("==", "\n==\n");
                }
                else if(linea.contains("=")){
                    
                    if(linea.contains("!=")){
                        linea = linea.replaceAll("!=", "\n!=\n");
                    }
                    else{
                        linea = linea.replaceAll("=", "\n=\n");
                    }
                    
                } //Fin de la identificación de operadores de comparación
                
                contenidoSeparado = contenidoSeparado + linea + "\n";
                
            }
            
            return contenidoSeparado;
            
        }finally{
            if(in!=null){ //En caso de estar abierto el documento
                in.close(); //Cerrar el documento
            }
        }
        
    } //Fin de método 'analisisLexico'
    
    /**
     * Método para la correcta separación de los tokens identificados del método 'analisisLexico'.
     * Separados por retornos o espacios ( a excepción de los String que se dejan juntos, esto para una mejor organización)
     * @param archivo Nombre del archivo en donde se pretende identificar y separar los tokens
     * @return String con todos los token separados apropiadamente
     * @throws FileNotFoundException 
     */
    public static String separarTokens(String archivo) throws FileNotFoundException{
        //VARIABLES
        Scanner in = null; //Variable para leer el archivo txt
        String linea = ""; //Variable que almacenará la línea en curso del archivo que se está leyendo
        String contenido = ""; //Variable que tendrá el contenido ya procesado para el nuevo archivo de texto
        boolean esCadena = false; //Variable para identificar cuando se trate de un String, para juntarlos en una sola linea
        String [] separador; //Variable para separar los tokens (por espacios y retornos)
        
        try{
            
            in = new Scanner(new FileReader(archivo)); //Abrir el fichero de texto con FileReader (Iniciador)
            
            while(in.hasNextLine()){
                
                linea = in.nextLine();
                
                if(linea.isBlank()==false){ //Entrar en caso de no ser una linea en blanco
                    
                    if(linea.startsWith("\"") && esCadena==false){ //En caso de comenzar una cadena de texto (String)
                        esCadena = true;
                    }
                    else if(linea.startsWith("\"") && esCadena){ //En caso de terminar una cadena de texto (String)
                        esCadena = false;
                        contenido = contenido + linea + "\n"; //Terminar el String con retorno
                    }
                    else if(esCadena == false){ //En caso de NO ser una cadena de texto (String)

                        separador = linea.split("\\s+"); //Elementos separados por espacios

                        for(int i=0; i<separador.length; i++){

                            if(separador[i].isBlank()==false){ //En caso de NO ser un elemento en blanco (validación)
                                contenido = contenido + separador[i] + "\n"; //Guardar en una sola linea al token o variable identificada
                            }
                            
                        }

                    }
                    
                    if(esCadena){ //En caso de ser una cadena de texto (String)
                        contenido = contenido + linea; //Guardar la linea sin retorno (esto para juntar todo el string en una sola linea)
                    }
                    
                }
                
            }
            
            return contenido;
            
        }finally{
            if(in!=null){ //En caso de estar abierto el documento
                in.close(); //Cerrar el documento
            }
        }
    } //Método 'separarTokens'
    
} //Fin de clase