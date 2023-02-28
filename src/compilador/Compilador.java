/*
 * Nombre: Compilador.java
 * Descripción: Analisis lexico
 * Autor: Aníbal Uriel Guijarro Rocha
 * Autor: Emmanuel Gómez Trujillo
 * Autor: Mario Alessandro López García
 * Fecha: 24 de febrero de 2023
 */
package compilador;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Anibal Uriel Guijarro Rocha
 * @author Emmanuel Gómez Trujillo
 * @author Mario Alessandro López García
 */
public class Compilador {
    
    public static ArrayList entrada = new ArrayList();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        escritura(eliminarComentarios("codigoFuente.txt"), "codigoSinComentarios.txt");
        escritura(analisisLexico("codigoSinComentarios.txt"), "tokens.txt");
        escritura(separarTokens("tokens.txt"), "tokens.txt");
        analisis("gramatica.csv", "reglas.txt");
        
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
                        contenido = contenido + linea + " STRING\n"; //Terminar el String con retorno
                    }
                    else if(esCadena == false){ //En caso de NO ser una cadena de texto (String)

                        separador = linea.split("\\s+"); //Elementos separados por espacios
                        
                        for(int i=0; i<separador.length; i++){
                            
                            if(separador[i].isBlank()==false){ //En caso de NO ser un elemento en blanco (validación)
                                
                                if(separador[i].matches("\\d+")){ //Numeros enteros
                                    contenido = contenido + separador[i] + " 1\n";
                                    entrada.add(1);
                                }else if(separador[i].matches("^-?[0-9]+(\\.[0-9]+)?$")){ //Numeros reales
                                    contenido = contenido + separador[i] + " 2\n";
                                    entrada.add(2);
                                }else if(separador[i].startsWith("\"") && separador[i].endsWith("\"")){ //Cadenas de texto
                                    contenido = contenido + separador[i] + " 3\n";
                                    entrada.add(3);
                                }else if(separador[i].equals("int") || separador[i].equals("string") || separador[i].equals("char") ||
                                        separador[i].equals("long") || separador[i].equals("double") || separador[i].equals("bool")){
                                    contenido = contenido + separador[i] + " 4\n";
                                    entrada.add(4);
                                }else if(separador[i].equals("+")){
                                    contenido = contenido + separador[i] + " 5\n";
                                    entrada.add(5);
                                }else if(separador[i].equals("*")){
                                    contenido = contenido + separador[i] + " 6\n";
                                    entrada.add(6);
                                }else if(separador[i].equals("<") || separador[i].equals(">")){
                                    contenido = contenido + separador[i] + " 7\n";
                                    entrada.add(7);
                                }else if(separador[i].equals("||")){
                                    contenido = contenido + separador[i] + " 8\n";
                                    entrada.add(8);
                                }else if(separador[i].equals("&&")){
                                    contenido = contenido + separador[i] + " 9\n";
                                    entrada.add(9);
                                }else if(separador[i].equals("!=")){
                                    contenido = contenido + separador[i] + " 10\n";
                                    entrada.add(10);
                                }else if(separador[i].equals("==")){
                                    contenido = contenido + separador[i] + " 11\n";
                                    entrada.add(11);
                                }else if(separador[i].equals(";")){
                                    contenido = contenido + separador[i] + " 12\n";
                                    entrada.add(12);
                                }else if(separador[i].equals(",")){
                                    contenido = contenido + separador[i] + " 13\n";
                                    entrada.add(13);
                                }else if(separador[i].equals("(")){
                                    contenido = contenido + separador[i] + " 14\n";
                                    entrada.add(14);
                                }else if(separador[i].equals(")")){
                                    contenido = contenido + separador[i] + " 15\n";
                                    entrada.add(15);
                                }else if(separador[i].equals("{")){
                                    contenido = contenido + separador[i] + " 16\n";
                                    entrada.add(16);
                                }else if(separador[i].equals("}")){
                                    contenido = contenido + separador[i] + " 17\n";
                                    entrada.add(17);
                                }else if(separador[i].equals("=")){
                                    contenido = contenido + separador[i] + " 18\n";
                                    entrada.add(18);
                                }else if(separador[i].equals("if")){
                                    contenido = contenido + separador[i] + " 19\n";
                                    entrada.add(19);
                                }else if(separador[i].equals("while")){
                                    contenido = contenido + separador[i] + " 20\n";
                                    entrada.add(20);
                                }else if(separador[i].equals("return")){
                                    contenido = contenido + separador[i] + " 21\n";
                                    entrada.add(21);
                                }else if(separador[i].equals("else")){
                                    contenido = contenido + separador[i] + " 22\n";
                                    entrada.add(22);
                                }else{ //Identificadores
                                    contenido = contenido + separador[i] + " 0\n";
                                    entrada.add(0);
                                }
                                
                            }
                            
                        }
                        
                    }
                    
                    if(esCadena){ //En caso de ser una cadena de texto (String)
                        contenido = contenido + linea; //Guardar la linea sin retorno (esto para juntar todo el string en una sola linea)
                    }
                    
                }
                
            }
            
            entrada.add("$");
            return contenido;
            
        }finally{
            if(in!=null){ //En caso de estar abierto el documento
                in.close(); //Cerrar el documento
            }
        }
    } //Método 'separarTokens'
    
    public static void analisis(String gramatica, String reglas) throws FileNotFoundException{
        //VARIABLES
        Scanner in = null; //Para leer el archivo de texto
        Scanner in2 = null; //Para leer el archivo de texto
        String linea; //Para almacenar toda la linea en curso del archivo
        ArrayList pila = new ArrayList();
        String accion = "A"; //Para almacenar la acción que se está realizando por parte de la tabla (Elemento de tabla)
        String [] info; //Para almacenar de manera separada cada elemento de una linea del archivo
        int indice = 0; //Para almacenar el indice de donde se encuentra un elemento dentro de la tabla (Indice solo en eje X - 'Horizontal')
        boolean existente = false; //Para conocer si el caracter buscado en la tabla existe o no en ella
        boolean reglaEncontrada = false;
        String lineaRegla = "";
        int remover = 0;
        
        try{
            
            pila.add("0");
            
            while(!accion.equals("r0") && !accion.equals("")){ //Bucle hasta que la cadena sea aceptada o negada por la gramatica
                
                in = new Scanner(new FileReader(gramatica)); //Abrir el fichero de texto con FileReader (Iniciador)
                
                    linea = in.nextLine();
                    info = linea.split(",");
                    
                    //Buscar el primer caracter de la cadena de entrada con respecto al encabezado de la tabla
                    if(info[0].contains("NUMEROS")){
                        
                        for(int i=1; i<info.length; i++){

                            if(entrada.get(0).toString().startsWith(info[i])){ //Si se encuentra el elemento

                                indice = i;
                                existente = true;

                            }

                        }

                    }
                    
                    if(existente){
                        
                        //Buscar el 'tope' de la pila con respecto a los ID (parte izquierda) de la tabla
                        while(in.hasNextLine()){

                            linea = in.nextLine();
                            info = linea.split(",");
                            
                            if(pila.get(pila.size()-1).equals(info[0])){ //Si se encuentra el 'tope' de la pila en la tabla

                                accion = info[indice];
                                break;
                            }
                            
                        }
                        
                        if(accion.equals("r0")){ //En caso de ser aceptada
                            
                            System.out.println("-> CADENA ACEPTADA");
                            
                        }else if(accion.isBlank()){ //En caso de ser negada
                            
                            System.out.println("-> CADENA NEGADA");
                            
                        }else if(accion.startsWith("d")){ //En caso de ser un 'desplazamiento' (agregar elementos a la pila)
                            
                            pila.add(entrada.get(0));
                            pila.add(accion.substring(1));
                            entrada.remove(0);
                            
                        }else if(accion.startsWith("r")){ //En caso de ser un 'reemplazo' (remplzar elementos de la pila)
                            
                            in2 = new Scanner(new FileReader(reglas)); //Abrir el fichero de texto con FileReader (Iniciador)
                            
                            while(reglaEncontrada == false){
                                
                                lineaRegla = in2.nextLine();
                                
                                if(lineaRegla.startsWith(accion.toUpperCase())){
                                    
                                    remover = lineaRegla.split("::=")[1].trim().split("\\s+").length * 2;
                                    reglaEncontrada = true;
                                    
                                }
                                
                            }
                            
                            int tam = pila.size();
                            
                            for(int i=tam-1; i>tam-remover; i--){
                                
                                pila.remove(i);
                                
                            }
                            
                            in = new Scanner(new FileReader(gramatica)); //Abrir el fichero de texto con FileReader (Iniciador)
                            in.nextLine();
                            linea = in.nextLine();
                            info = linea.split(",");
                            
                            if(info[0].contains("TEXTO")){
                                
                                lineaRegla = lineaRegla.split("::=")[0].split("\\s+")[1].replaceAll("><", "");

                                for(int i=1; i<info.length; i++){

                                    if(lineaRegla.equals(info[i])){ //Si se encuentra el elemento

                                        pila.add(i-1);

                                    }

                                }

                            }
                            
                            while(in.hasNextLine()){
                                
                                linea = in.nextLine();
                                info = linea.split(",");
                                
                                if(pila.get(pila.size()-1).toString().contains(info[0])){ //Si se encuentra el 'tope' de la pila en la tabla

                                    accion = info[indice];
                                    break;
                                    
                                }

                            }
                            
                            pila.add(accion);
                            
                        }
                        
                    }
                
            }
            
        }finally{
            if(in!=null){ //En caso de estar abierto el documento
                in.close(); //Cerrar el documento
            }
        }
        
        
    } //Fin de método 'analisis'
    
} //Fin de clase