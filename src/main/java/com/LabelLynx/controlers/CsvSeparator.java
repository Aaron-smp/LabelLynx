package com.LabelLynx.controlers;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Getter
public class CsvSeparator {

    /*
    * 1- Leer archivo
    * 2- Separar encabezado del contenido
    * 3- Leer cada linea añadir al documento
    *   3.1- Separar el contenido de los separadores en orden -> palabra|separador|palabra
    *   3.2- Una linea puede contener el separador dentro de "" comillas
    *   3.3- Añadir el contenido al documento con su estilo correspondiente palabra o separador
    *   3.4- Si una linea tiene menos de palabras que el numero de encabezado se subrayara de rojo
    */

    private static final Logger logger = LogManager.getLogger(CsvSeparator.class);
    private ArrayList<String> encabezados;
    private ArrayList<String> lineas;
    private ArrayList<List<String>> contenido;
    private int[][] lineaStartFinal;
    private String SEPARATOR = null;
    private int sizeStringEncabezado;

    public CsvSeparator(String separador) throws IOException {
        SEPARATOR = separador;
        encabezados = new ArrayList<>();
        lineas = new ArrayList<>();
        logger.info("Encabezado: {}", encabezados);
    }

    /*
    *  Devuelve en primera posición el header y en la segunda el contenido
    * */
    public String[] getFileSeparate(File file) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsoluteFile())));
        String[] fichero = new String[2];
        String encabezadoLn = br.readLine().concat("\n");
        sizeStringEncabezado = encabezadoLn.length();
        logger.info("Tamaño encabezado: {}", encabezadoLn.length());
        encabezados.addAll(Arrays.asList(encabezadoLn.split(SEPARATOR)));

        StringBuilder sb = new StringBuilder();
        String linea;

        while((linea = br.readLine()) != null){
            lineas.add(linea);
            sb.append(linea).append("\n");
        }

        fichero[0] = encabezadoLn;
        fichero[1] = sb.toString();

        calculateLongLine();

        return fichero;
    }

    public boolean isValidLine(String nuevaLinea){
        return nuevaLinea.split(SEPARATOR).length == encabezados.size();
    }

    /*
     * Linea donde se actualizara el string
     * Nueva linea es el string nuevo a remplazar
     *
     * El método recalculará las posiciones hacia abajo de la posición indicada
     * y actualizara la linea y el contenido
     *
     * 1- Comprueba que la nueva línea esté bien
     * 2- Comprueba que la posición sea válida
     * 3- Se separa la línea nueva en las partes correspondientes
     * 4- Se actualizan los arrays correspondientes
     *
     */
    public void updateAndReordenateLine(int linea, String nuevaLinea){
        if (!isValidLine(nuevaLinea)) return;
        if (linea >= lineas.size()) return;
        int inicio = lineaStartFinal[linea][0];

        lineas.set(linea, nuevaLinea);

        for(int i = linea; i < lineas.size(); i++){
            inicio = inicio + lineas.get(i).length();

            lineaStartFinal[i][1] = inicio;
            lineaStartFinal[i][0] = lineaStartFinal[i][1]-lineas.get(i).length();
        }

        ArrayList<String> updateLine = separateLine(nuevaLinea);
        contenido.set(linea, updateLine);
    }

    public ArrayList<String> separateLine(String linea){
        ArrayList<String> stringArrayList = new ArrayList<>();
        StringBuilder palabra = new StringBuilder();
        boolean insideComillas = false;
        //Por cada caracter de la linea
        int numCharacters = linea.length();
        ArrayList<String> lineaPalabras = new ArrayList<>();
        int cont = 0;
        for(int e = 0; e < linea.length(); e++){
            String caracter = String.valueOf(linea.charAt(e));
            if(caracter.equals("\\" + SEPARATOR)){
                insideComillas = !insideComillas;
            }
            if (insideComillas){
                palabra.append(caracter);
            }else{
                if(caracter.equals(SEPARATOR)){
                    lineaPalabras.add(palabra.toString());
                    palabra.setLength(0);
                    cont = cont+1;
                }else{
                    palabra.append(caracter);
                    if (numCharacters-1 == e){
                        lineaPalabras.add(palabra.toString());
                        cont = cont+1;
                    }
                }
            }
        }

        return lineaPalabras;
    }

    /*
     * Calcula la longitud y almacena el inicio y final
     * de cada línea.
     */
    public void calculateLongLine(){
        lineaStartFinal = new int[lineas.size()][2];
        logger.info("Primera linea: {}", lineas.get(0));

        int inicio = sizeStringEncabezado;

        for(int i = 0; i < lineas.size(); i++){
            lineaStartFinal[i][0] = inicio;
            inicio = inicio + lineas.get(i).length();
            inicio++; //Añade una posición mas por el salto de linea

            lineaStartFinal[i][1] = inicio;

            logger.info("Tabla posiciones: {}, {}", lineaStartFinal[i][0], lineaStartFinal[i][1]);
        }

        logger.info("Total caracteres fichero: {}", lineaStartFinal[lineas.size()-1][1]);
    }

    public HashMap<Integer, String> getWrongLines(){
        HashMap<Integer, String> listaLineaError = new HashMap<>();

        int contPalabra = 1;
        //Recorre cada linea del archivo, cada linea puede contener x palabras
        for (List<String> listPalabra : contenido) {
            int numPalabrasLinea = 0;

            //Recorre cada palabra de cada linea
            for(String palabra : listPalabra){
                numPalabrasLinea++;
            }

            if(numPalabrasLinea != encabezados.size()){
                StringBuilder mensaje = new StringBuilder("La linea " + contPalabra);
                String errorMsg = numPalabrasLinea > encabezados.size() ? mensaje.append(" tiene mas contenido que columnas").toString() : mensaje.append(" le falta contenido en alguna columna").toString();
                listaLineaError.put(contPalabra, errorMsg);
            }

            contPalabra++;
        }

        return listaLineaError;
    }

    public void analiseLines(){
        ArrayList<List<String>> contenido = new ArrayList<>();
        //Por cada linea del texto
        for(int i = 0; i < lineas.size(); i++){
            StringBuilder palabra = new StringBuilder();
            boolean insideComillas = false;
            //Por cada caracter de la linea
            int numCharacters = lineas.get(i).length();
            List<String> lineaPalabras = new ArrayList<>();
            int cont = 0;
            for(int e = 0; e < lineas.get(i).length(); e++){
                String caracter = String.valueOf(lineas.get(i).charAt(e));
                if(caracter.equals("\\" + SEPARATOR)){
                    insideComillas = !insideComillas;
                }
                if (insideComillas){
                    palabra.append(caracter);
                }else{
                    if(caracter.equals(SEPARATOR)){
                        lineaPalabras.add(palabra.toString());
                        palabra.setLength(0);
                        cont = cont+1;
                    }else{
                        palabra.append(caracter);
                        if (numCharacters-1 == e){
                            lineaPalabras.add(palabra.toString());
                            cont = cont+1;
                        }
                    }
                }
            }
            contenido.add(lineaPalabras);
            cont = 0;
        }
        this.contenido = contenido;
        fillContenidoWithNull();
        logger.info("Contenido: \n {}", contenido);
    }

    /*
    * Rellena la lista de contenido con texto vacio cuando sea nulo,
    * para evitar null pointers
    */
    private void fillContenidoWithNull(){
        for(int i = 0; i < contenido.size(); i++){
            if(contenido.get(i).size() != encabezados.size()){
                contenido.get(i).add("");
            }
        }
    }
    public String printFileContent(){
        StringBuilder sb = new StringBuilder();

        for (List<String> listPalabra : contenido) {
            //Recorre cada palabra de cada linea
            int cont = 1;
            for(String palabra : listPalabra){
                if(cont == listPalabra.size()){
                    sb.append(palabra);
                }else{
                    sb.append(palabra).append(",");
                }
                cont++;
            }

            sb.append("\n");
        }

        return sb.toString();
    }

    public String printFileHeaders(){
        StringBuilder sb = new StringBuilder();

        int cont = 1;
        for(String palabra : encabezados){
            if(cont == encabezados.size()){
                sb.append(palabra);
            }else{
                sb.append(palabra).append(",");
            }
            cont++;
        }

        sb.append("\n");

        return sb.toString();
    }

    public int getLineInit(int linea){
        return lineaStartFinal[linea-1][0];
    }

    public int getLineFinish(int linea){
        return lineaStartFinal[linea-1][1];
    }
    public static boolean isCsvOrProp(File file){
        String extension = file.getPath().substring(file.getPath().lastIndexOf("."));
        logger.info("La extension del archivo seleccionado es: {}", extension);
        return extension.equals(".csv") || extension.equals(".properties");
    }

    public String getText(File file){
        String line = "";
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((line = br.readLine()) != null) {
                 sb.append(line).append("\n");
            }
            logger.info("Tamaño texto actual: " + sb.toString().length());
            return sb.toString();
        } catch (IOException e) {
            logger.error("Error leyendo el archivo.");
        }
        return "";
    }
}
