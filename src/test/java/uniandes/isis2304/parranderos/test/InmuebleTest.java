package uniandes.isis2304.parranderos.test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import org.apache.log4j.Logger;
import uniandes.isis2304.parranderos.negocio.Parranderos;

import javax.swing.*;
import java.io.FileReader;

public class InmuebleTest {

    /* ****************************************************************
     * 			Constantes
     *****************************************************************/
    /**
     * Logger para escribir la traza de la ejecución
     */
    private static Logger log = Logger.getLogger(ReservaTest.class.getName());

    /**
     * Ruta al archivo de configuración de los nombres de tablas de la base de datos: La unidad de persistencia existe y el esquema de la BD también
     */
    private static final String CONFIG_TABLAS_A = "./src/main/resources/config/TablasBD_A.json";

    /* ****************************************************************
     * 			Atributos
     *****************************************************************/
    /**
     * Objeto JSON con los nombres de las tablas de la base de datos que se quieren utilizar
     */
    private JsonObject tableConfig;

    /**
     * La clase que se quiere probar
     */
    private Parranderos parranderos;

    /* ****************************************************************
     * 			Métodos de prueba para la tabla Reserva - Creación y borrado
     *****************************************************************/
    /**
     * Método que prueba las operaciones sobre la tabla  Inmueble
     * 1. Adicionar un tipo de bebida
     * 2. Listar el contenido de la tabla con 0, 1 y 2 registros insertados
     * 3. Borrar un tipo de bebida por su identificador
     * 4. Borrar un tipo de bebida por su nombre
     */

    /* ****************************************************************
     * 			Métodos de configuración
     *****************************************************************/
    /**
     * Lee datos de configuración para la aplicación, a partir de un archivo JSON o con valores por defecto si hay errores.
     * @param tipo - El tipo de configuración deseada
     * @param archConfig - Archivo Json que contiene la configuración
     * @return Un objeto JSON con la configuración del tipo especificado
     * 			NULL si hay un error en el archivo.
     */
    private JsonObject openConfig (String archConfig)
    {
        JsonObject config = null;
        try
        {
            Gson gson = new Gson( );
            FileReader file = new FileReader (archConfig);
            JsonReader reader = new JsonReader ( file );
            config = gson.fromJson(reader, JsonObject.class);
            log.info ("Se encontró un archivo de configuración de tablas válido");
        }
        catch (Exception e)
        {
            //			e.printStackTrace ();
            log.info ("NO se encontró un archivo de configuración válido");
            JOptionPane.showMessageDialog(null, "No se encontró un archivo de configuración de tablas válido: ", "TipoBebidaTest", JOptionPane.ERROR_MESSAGE);
        }
        return config;
    }

}
