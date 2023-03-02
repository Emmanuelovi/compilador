/*
 * Nombre: Compilador.java
 * Descripción: Analisis lexico
 * Autor: Aníbal Uriel Guijarro Rocha
 * Autor: Emmanuel Gómez Trujillo
 * Autor: Mario Alessandro López García
 * Fecha: 02 de Marzo de 2023
 */
package compilador;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @author Anibal Uriel Guijarro Rocha
 * @author Emmanuel Gómez Trujillo
 * @author Mario Alessandro López García
 */
public class Compilador {
    
    static ArrayList entrada = new ArrayList();
    
    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        escritura(eliminarComentarios("codigoFuente.txt"), "codigoSinComentarios.txt");
        escritura(analisisLexico("codigoSinComentarios.txt"), "tokens.txt");
        
        String [] informacion = separarTokens("tokens.txt"); //informacion[0] = contenido separado <---> informacion[1] = entrada (identificadores)
        
        escritura(informacion[0], "tokens.txt");
        analisisSintactico("gramatica.csv", "reglas.txt", informacion[1]);
        
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
     * Método para leer un archivo de texto, posteriormente realiza una serie de clasificaciones
     * donde se eliminan los espacios en blanco del inicio, lineas en blanco y los comentarios; dejando solamente el codigo
     * @param archivo Nombre del archivo a leer
     * @return Cadena de texto con el contenido del archivo de texto ya procesado
     * @throws FileNotFoundException 
     */
    public static String eliminarComentarios(String archivo) throws FileNotFoundException{
        //VARIABLES
        Scanner in = null; //Para leer el archivo txt
        String linea; //Variable que almacenará la línea en curso del archivo que se está leyendo
        String contenido = ""; //Variable que tendrá el contenido ya procesado para el nuevo archivo de texto
        String [] eliminarComentarios; //Arreglo para eliminar los comentarios simples (Es decir, solo para cuando comience con //)
        boolean comentario = false; //Para identificar los comentarios de varias lineas (/* --- */)
        
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
        String contenidoSeparado = ""; //Variable que contendrá todo el contenido ya procesado (anáslisis léxico), es decir, con las partes identificadas.
        
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
                    
                }
                
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
     * Separados por retornos o espacios (a excepción de los String "cadenas juntas por las comillas", esto para una mejor organización)
     * @param archivo Nombre del archivo en donde se pretende identificar y separar los tokens
     * @return Arreglo String de 2 posiciones = String con todos los token separados apropiadamente (contenido) y String con la entrada
     * @throws FileNotFoundException 
     */
    public static String[] separarTokens(String archivo) throws FileNotFoundException{
        //VARIABLES
        Scanner in = null; //Variable para leer el archivo txt
        String linea; //Variable que almacenará la línea en curso del archivo que se está leyendo
        String contenido = ""; //Variable que tendrá el contenido ya procesado para el nuevo archivo de texto (Variable a retornar)
        boolean esCadena = false; //Variable para identificar cuando se trate de un String, para juntarlos en una sola linea
        String [] separadorTokens; //Variable para separar los tokens (por espacios y retornos)
        String entrada = ""; //Variable para guardar la entrada (identificadores de cada token, lexema o patrón) -> Para realizar el análisis sintáctico
        
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

                        separadorTokens = linea.split("\\s+"); //Elementos separados por espacios
                        
                        for(int i=0; i<separadorTokens.length; i++){
                            
                            if(separadorTokens[i].isBlank()==false){ //En caso de NO ser un elemento en blanco (validación)
                                
                                if(separadorTokens[i].matches("\\d+")){ //Numeros enteros
                                    contenido = contenido + separadorTokens[i] + " 1\n";
                                    entrada = entrada + "1,";
                                }else if(separadorTokens[i].matches("^-?[0-9]+(\\.[0-9]+)?$")){ //Numeros reales
                                    contenido = contenido + separadorTokens[i] + " 2\n";
                                    entrada = entrada + "2,";
                                }else if(separadorTokens[i].startsWith("\"") && separadorTokens[i].endsWith("\"")){ //Cadenas de texto
                                    contenido = contenido + separadorTokens[i] + " 3\n";
                                    entrada = entrada + "3,";
                                }else if(separadorTokens[i].equals("int") || separadorTokens[i].equals("string") || separadorTokens[i].equals("char") ||
                                        separadorTokens[i].equals("long") || separadorTokens[i].equals("double") || separadorTokens[i].equals("bool")){
                                    contenido = contenido + separadorTokens[i] + " 4\n";
                                    entrada = entrada + "4,";
                                }else if(separadorTokens[i].equals("+")){
                                    contenido = contenido + separadorTokens[i] + " 5\n";
                                    entrada = entrada + "5,";
                                }else if(separadorTokens[i].equals("*")){
                                    contenido = contenido + separadorTokens[i] + " 6\n";
                                    entrada = entrada + "6,";
                                }else if(separadorTokens[i].equals("<") || separadorTokens[i].equals(">")){
                                    contenido = contenido + separadorTokens[i] + " 7\n";
                                    entrada = entrada + "7,";
                                }else if(separadorTokens[i].equals("||")){
                                    contenido = contenido + separadorTokens[i] + " 8\n";
                                    entrada = entrada + "8,";
                                }else if(separadorTokens[i].equals("&&")){
                                    contenido = contenido + separadorTokens[i] + " 9\n";
                                    entrada = entrada + "9,";
                                }else if(separadorTokens[i].equals("!=")){
                                    contenido = contenido + separadorTokens[i] + " 10\n";
                                    entrada = entrada + "10,";
                                }else if(separadorTokens[i].equals("==")){
                                    contenido = contenido + separadorTokens[i] + " 11\n";
                                    entrada = entrada + "11,";
                                }else if(separadorTokens[i].equals(";")){
                                    contenido = contenido + separadorTokens[i] + " 12\n";
                                    entrada = entrada + "12,";
                                }else if(separadorTokens[i].equals(",")){
                                    contenido = contenido + separadorTokens[i] + " 13\n";
                                    entrada = entrada + "13,";
                                }else if(separadorTokens[i].equals("(")){
                                    contenido = contenido + separadorTokens[i] + " 14\n";
                                    entrada = entrada + "14,";
                                }else if(separadorTokens[i].equals(")")){
                                    contenido = contenido + separadorTokens[i] + " 15\n";
                                    entrada = entrada + "15,";
                                }else if(separadorTokens[i].equals("{")){
                                    contenido = contenido + separadorTokens[i] + " 16\n";
                                    entrada = entrada + "16,";
                                }else if(separadorTokens[i].equals("}")){
                                    contenido = contenido + separadorTokens[i] + " 17\n";
                                    entrada = entrada + "17,";
                                }else if(separadorTokens[i].equals("=")){
                                    contenido = contenido + separadorTokens[i] + " 18\n";
                                    entrada = entrada + "18,";
                                }else if(separadorTokens[i].equals("if")){
                                    contenido = contenido + separadorTokens[i] + " 19\n";
                                    entrada = entrada + "19,";
                                }else if(separadorTokens[i].equals("while")){
                                    contenido = contenido + separadorTokens[i] + " 20\n";
                                    entrada = entrada + "20,";
                                }else if(separadorTokens[i].equals("return")){
                                    contenido = contenido + separadorTokens[i] + " 21\n";
                                    entrada = entrada + "21,";
                                }else if(separadorTokens[i].equals("else")){
                                    contenido = contenido + separadorTokens[i] + " 22\n";
                                    entrada = entrada + "22,";
                                }else{ //Identificadores
                                    contenido = contenido + separadorTokens[i] + " 0\n";
                                    entrada = entrada + "0,";
                                }
                                
                            }
                            
                        }
                        
                    }
                    
                    if(esCadena){ //En caso de ser una cadena de texto (String)
                        contenido = contenido + linea; //Guardar la linea sin retorno (esto para juntar todo el string en una sola linea)
                    }
                    
                }
                
            }
            
            entrada = entrada + "23"; //El final de la entrada ($)
            
            String [] informacion = {contenido, entrada};
            
            return informacion;
            
        }finally{
            if(in!=null){ //En caso de estar abierto el documento
                in.close(); //Cerrar el documento
            }
        }
    } //Método 'separarTokens'
    
    /**
     * Método para realizar el análisis sintáctico, es decir, manejar una pila y entrada de datos para conocer si es correcta la sintaxis del código fuente
     * @param gramatica Nombre del archivo que contiene toda la gramatica del lenguaje (archivo.csv)
     * @param reglas Nombre del archivo que contiene todas las reglas del lenguaje (archivo.txt)
     * @param identificadores String que contiene la entrada (identificadores) de los tokens, lexemas y/o patrones
     * @throws FileNotFoundException 
     */
    public static void analisisSintactico(String gramatica, String reglas, String identificadores) throws FileNotFoundException{
        //VARIABLES
        Scanner inGramatica = null; //Para leer el archivo de texto 'gramatica'
        Scanner inReglas = null; //Para leer el archivo de texto 'reglas'
        String linea; //Para almacenar toda la linea en curso del archivo
        ArrayList pila = new ArrayList(); //Estructura dinámica para manejar la pila
        ArrayList entrada = new ArrayList(Arrays.asList(identificadores.split(","))); //Estructura dinámica para manejar la entrada de datos
        //"entrada" almacena valores de 'identificadores'
        String accion = ""; //Para almacenar la acción que se está realizando por parte de la tabla (Elemento de tabla)
        String [] info; //Para almacenar de manera separada cada elemento de una linea del archivo
        int indice = 0; //Para almacenar el indice de donde se encuentra un elemento dentro de la tabla (Indice solo en eje X - 'Horizontal')
        boolean existente = false; //Para conocer si el caracter buscado en la tabla existe o no en ella
        boolean reglaEncontrada; //Variable para saber si la regla (produccion) a sido encontrada en el archivo de 'reglas'
        String lineaRegla = ""; //Variable para almacenar la linea que contendrá la regla (produccion) completa
        int remover = 0; //Variable para almacenar el tamaño de elementos a eliminar de la pila (solo para reglas que comiencen con 'R')
        
        try{
            
            pila.add("0"); //Inicio de pila
            
            while(!accion.equals("r0") && !accion.equals("null")){ //Bucle hasta que la cadena sea aceptada o negada por la gramatica
                //Imprimir pila y entrada en consola
                System.out.println("");
                System.out.print("\nPILA: " + pila);
                System.out.print("\nENTRADA: " + entrada);
                
                inGramatica = new Scanner(new FileReader(gramatica)); //Abrir el fichero de texto con FileReader (Iniciador)
                linea = inGramatica.nextLine();
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
                    while(inGramatica.hasNextLine()){

                        linea = inGramatica.nextLine();
                        info = linea.split(",");

                        if(pila.get(pila.size()-1).equals(info[0])){ //Si se encuentra el 'tope' de la pila en la tabla

                            accion = info[indice];
                            System.out.print("\nACCION: " + accion); //Imprimir la acción realizada en consola
                            break;
                        }

                    }

                    if(accion.equals("r0")){ //En caso de ser aceptada

                        System.out.println("\n\n-> CADENA ACEPTADA");

                    }else if(accion.equals("null")){ //En caso de ser negada

                        System.out.println("\n\n-> CADENA NEGADA");

                    }else if(accion.startsWith("d")){ //En caso de ser un 'desplazamiento' (agregar elementos a la pila)

                        pila.add(entrada.get(0));
                        pila.add(accion.substring(1));
                        entrada.remove(0);

                    }else if(accion.startsWith("r")){ //En caso de ser un 'reemplazo' (remplzar elementos de la pila)

                        inReglas = new Scanner(new FileReader(reglas)); //Abrir el fichero de texto con FileReader (Iniciador)
                        reglaEncontrada = false;

                        while(reglaEncontrada == false){

                            lineaRegla = inReglas.nextLine();

                            if(lineaRegla.startsWith(accion.toUpperCase())){

                                remover = lineaRegla.split("::=")[1].trim().split("\\s+").length * 2;
                                reglaEncontrada = true;

                            }

                        }

                        System.out.print("\nPRODUCCION: " + lineaRegla); //Imprimir la regla (produccion) en consola
                        
                        if(!lineaRegla.split("::=")[1].trim().equals("e")){
                            int tam = pila.size(); //Tamaño de la pila

                            for(int i=tam-1; i>=tam-remover; i--){

                                pila.remove(i);

                            }

                        }

                        inGramatica = new Scanner(new FileReader(gramatica)); //Abrir el fichero de texto con FileReader (Iniciador)
                        inGramatica.nextLine();
                        linea = inGramatica.nextLine();
                        info = linea.split(",");

                        if(info[0].contains("TEXTO")){

                            lineaRegla = lineaRegla.split("::=")[0].split("\\s+")[1].replaceAll(">|<", "");

                            for(int i=1; i<info.length; i++){

                                if(lineaRegla.equals(info[i])){ //Si se encuentra el elemento

                                    pila.add(i-1);
                                    indice = i;

                                }

                            }

                        }

                        while(inGramatica.hasNextLine()){

                            linea = inGramatica.nextLine();
                            info = linea.split(",");

                            if(pila.get(pila.size()-2).toString().equals(info[0])){ //Si se encuentra el 'tope' de la pila en la tabla

                                accion = info[indice];
                                break;

                            }

                        }

                        pila.add(accion);

                    }

                }
                
            }
            
        }finally{
            if(inGramatica!=null){ //En caso de estar abierto el documento
                inGramatica.close(); //Cerrar el documento
            }
            if(inReglas!=null){ //En caso de estar abierto el documento
                inReglas.close(); //Cerrar el documento
            }
        }
        
    } //Fin de método 'analisisSintactico'
    
} //Fin de clase