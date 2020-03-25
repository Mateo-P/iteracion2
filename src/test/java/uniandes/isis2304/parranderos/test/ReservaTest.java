/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad	de	los	Andes	(Bogotá	- Colombia)
 * Departamento	de	Ingeniería	de	Sistemas	y	Computación
 * Licenciado	bajo	el	esquema	Academic Free License versión 2.1
 * 		
 * Curso: isis2304 - Sistemas Transaccionales
 * Proyecto: Parranderos Uniandes
 * @version 1.0
 * @author Germán Bravo
 * Julio de 2018
 * 
 * Revisado por: Claudia Jiménez, Christian Ariza
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

package uniandes.isis2304.parranderos.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.FileReader;
import java.sql.Timestamp;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import uniandes.isis2304.parranderos.negocio.Parranderos;
import uniandes.isis2304.parranderos.negocio.VOReserva;


/**
 * Clase con los métdos de prueba de funcionalidad sobre TIPOBEBIDA
 * @author Germán Bravo
 *
 */
public class ReservaTest
{
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
	 * Método que prueba las operaciones sobre la tabla TipoBebida
	 * 1. Adicionar un tipo de bebida
	 * 2. Listar el contenido de la tabla con 0, 1 y 2 registros insertados
	 * 3. Borrar un tipo de bebida por su identificador
	 * 4. Borrar un tipo de bebida por su nombre
	 */
	@Test
	public void CRDReserva() 
	{
		// Probar primero la conexión a la base de datos
		try
		{
			log.info ("Probando las operaciones CRD sobre TipoBebida");
			parranderos = new Parranderos (openConfig (CONFIG_TABLAS_A));
		}
		catch (Exception e)
		{
			//			e.printStackTrace();
			log.info ("Prueba de CRD de Tipobebida incompleta. No se pudo conectar a la base de datos !!. La excepción generada es: " + e.getClass ().getName ());
			log.info ("La causa es: " + e.getCause ().toString ());

			String msg = "Prueba de CRD de Tipobebida incompleta. No se pudo conectar a la base de datos !!.\n";
			msg += "Revise el log de parranderos y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);
			fail (msg);
		}

		// Ahora si se pueden probar las operaciones
		try
		{
			// Lectura de las Reserva con la tabla vacía
			List <VOReserva> lista = parranderos.darVOReserva();
			assertEquals ("Debe haber Reservas creadass!!", 8, lista.size ());

			// Lectura de los tipos de bebida con un tipo de bebida adicionado
			Timestamp fechaGeneracion = new Timestamp(System.currentTimeMillis());
			Timestamp fechaInicio = new Timestamp(System.currentTimeMillis()+10000000);
			Timestamp fechaFin = new Timestamp(System.currentTimeMillis()+10000000+2000000);
			VOReserva Reserva = parranderos.adicionarReserva(1, 1007863890, fechaInicio, fechaFin, fechaGeneracion, null, 'N', 4);
			lista = parranderos.darVOReserva();
			assertEquals ("Debe haber una Reserva mas!!", 9, lista.size ());
			assertEquals ("El objeto creado y el traido de la BD deben ser iguales !!", Reserva, lista.get (0));

			// Prueba de eliminación de un tipo de bebida, dado su identificador
			long tbEliminados = parranderos.eliminarReserva(  Reserva.getIdReserva());
			assertEquals ("Debe haberse actualizado una Cxc !!", 9, tbEliminados);
			lista = parranderos.darVOReserva();
			assertEquals ("Debe haber 9 Reservas !!", 9, lista.size ());
			assertTrue ("La Reserva debe estar en la tabla", Reserva.equals (lista.get (0)));

		}
		catch (Exception e)
		{
			//			e.printStackTrace();
			String msg = "Error en la ejecución de las pruebas de operaciones sobre la tabla TipoBebida.\n";
			msg += "Revise el log de parranderos y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);

			fail ("Error en las pruebas sobre la tabla TipoBebida");
		}
		finally
		{
			parranderos.limpiarParranderos ();
			parranderos.cerrarUnidadPersistencia ();    		
		}
	}

	/**
	 * Método de prueba de la restricción de unicidad sobre el nombre de TipoBebida
	 */
	@Test
	public void unicidadReservaTest() 
	{
		// Probar primero la conexión a la base de datos
		try
		{
			log.info ("Probando la restricción de UNICIDAD del id de la reserva");
			parranderos = new Parranderos (openConfig (CONFIG_TABLAS_A));
		}
		catch (Exception e)
		{
			//			e.printStackTrace();
			log.info ("Prueba de UNICIDAD de Reserva incompleta. No se pudo conectar a la base de datos !!. La excepción generada es: " + e.getClass ().getName ());
			log.info ("La causa es: " + e.getCause ().toString ());

			String msg = "Prueba de UNICIDAD de Reserva incompleta. No se pudo conectar a la base de datos !!.\n";
			msg += "Revise el log de parranderos y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);
			fail (msg);
		}

		// Ahora si se pueden probar las operaciones
		try
		{
			// Lectura de los tipos de bebida con la tabla vacía
			List <VOReserva> lista = parranderos.darVOReserva();
			assertEquals ("Debe haber 8 Reservas creadas!!", 8, lista.size ());

			// Lectura de los tipos de bebida con un tipo de bebida adicionado
			Timestamp fechaGeneracion = new Timestamp(System.currentTimeMillis());
			Timestamp fechaInicio = new Timestamp(System.currentTimeMillis()+10000000);
			Timestamp fechaFin = new Timestamp(System.currentTimeMillis()+10000000+2000000);
			VOReserva Reserva = parranderos.adicionarReserva(1, 1007863890, fechaInicio, fechaFin, fechaGeneracion, null, 'N', 4);
			lista = parranderos.darVOReserva();
			assertEquals ("Debe haber un tipo de bebida creado !!", 1, lista.size ());

			VOReserva Reserva2 = parranderos.adicionarReserva(1, 1007863890, fechaInicio, fechaFin, fechaGeneracion, null, 'N', 4);
			assertNull ("No puede adicionar dos Reservas para el mismo inmuble en la misma fecha !!", Reserva2);
		}
		catch (Exception e)
		{
			//			e.printStackTrace();
			String msg = "Error en la ejecución de las pruebas de UNICIDAD sobre la tabla RESERVA.\n";
			msg += "Revise el log de parranderos y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);

			fail ("Error en las pruebas de UNICIDAD sobre la tabla TipoBebida");
		}    				
		finally
		{
			parranderos.limpiarParranderos ();
			parranderos.cerrarUnidadPersistencia ();    		
		}
	}

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
