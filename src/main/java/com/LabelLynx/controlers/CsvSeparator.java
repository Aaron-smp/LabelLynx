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
    private String[] fichero;

    private String SEPARATOR = null;
    private int sizeStringEncabezado;

    public CsvSeparator() {
        this(",");
    }

    public CsvSeparator(String separador) {
        SEPARATOR = separador;
        encabezados = new ArrayList<>();
        lineas = new ArrayList<>();
        fichero = new String[2];
        logger.info("Encabezado: {}", encabezados);
    }

    /*
    *  Devuelve en primera posición el header y en la segunda el contenido
    * */
    public String[] getFileSeparate(File file) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsoluteFile())));
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

        return fichero;
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

    public ArrayList<List<String>> analiseLines(ArrayList<String> lineas){
        //EL ARRAY DE LINEAS, CONTIENE LAS LINEAS TAL CUAL LAS CONTIENE EL ARCHIVO O EDITOR!
        ArrayList<List<String>> contenido = new ArrayList<>();
        //Por cada linea del texto
        lineas = lineas == null ? this.lineas : lineas;

        for (String linea : lineas) {
            contenido.add(analiseLine(linea));
        }
        this.contenido = contenido;
        fillContenidoWithNull();
        logger.info("Contenido: \n {}", contenido);
        return contenido;
    }

    public List<String> analiseLine(String line){
        StringBuilder palabra = new StringBuilder();
        boolean insideComillas = false;
        //Por cada caracter de la linea
        int numCharacters = line.length();
        List<String> lineaPalabras = new ArrayList<>();
        int cont = 0;
        for(int e = 0; e < line.length(); e++){
            String caracter = String.valueOf(line.charAt(e));
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
    * Rellena la lista de contenido con texto vacio cuando sea nulo,
    * para evitar null pointers
    */
    private void fillContenidoWithNull(){
        for (List<String> strings : contenido) {
            if (strings.size() != encabezados.size()) {
                strings.add("");
            }
        }
    }

    public static boolean isCsvOrProp(File file){
        String extension = file.getPath().substring(file.getPath().lastIndexOf("."));
        logger.info("La extension del archivo seleccionado es: {}", extension);
        return extension.equals(".csv") || extension.equals(".properties");
    }
}
