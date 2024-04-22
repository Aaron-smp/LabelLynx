package com.LabelLynx.literals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Properties;

public class LiteralManager{

    private static final Logger logger = LogManager.getLogger("LiteralManager");
    public static volatile LiteralManager instance;
    Properties properties;

    private LiteralManager(Properties properties){
        this.properties = properties;
        try{
            chooseLanguage();
        }catch (Exception e){
            logger.error("No se pudo seleccionar el idioma, se establecerá el castellano por defecto, {}", e.toString());
        }
    }

    public static LiteralManager getInstance() {
        LiteralManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized(LiteralManager.class) {
            if (instance == null) {
                instance = new LiteralManager(new Properties());
            }
            return instance;
        }
    }

    private void chooseLanguage()  {
        String idioma = null;
        try{
            properties.load(new InputStreamReader(Objects.requireNonNull(LiteralManager.class.getResource("/properties/config.properties")).openStream()));
            idioma = properties.get("language").toString();
            logger.info("Idioma seleccionado: {}.", idioma);
        }catch (Exception e){
           logger.error("No ha sido posible acceder al fichero de configuración.");
        }
        chargeLanguage(idioma);
    }

    public void changeLanguaje(String newLanguage){
        chargeLanguage(newLanguage);
        logger.info("El idioma ha sido cambiado.");
    }

    private void chargeLanguage(String language){
        try{
            switch(Objects.requireNonNull(language)){
                case "EN":
                    properties.load(new InputStreamReader(Objects.requireNonNull(LiteralManager.class.getResource("/properties/english.properties")).openStream()));
                    break;
                case "FR":
                    properties.load(new InputStreamReader(Objects.requireNonNull(LiteralManager.class.getResource("/properties/french.properties")).openStream()));
                    break;
                case "GR":
                    properties.load(new InputStreamReader(Objects.requireNonNull(LiteralManager.class.getResource("/properties/germany.properties")).openStream()));
                    break;
            }
        }catch (Exception e){
            logger.error("No ha sido posible cargar el fichero con los literales en el idioma: {}", language);
        }
        logger.info("Idioma cargado: ".concat(language));
    }

    public String getLiteral(String key){
        try{
            return properties.get(key).toString();
        }catch (Exception e){
            logger.error("No se pudo recuperar el literal(key): {}", key);
        }
        return "";
    }


}
