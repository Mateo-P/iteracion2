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

package uniandes.isis2304.parranderos.negocio;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import com.google.gson.JsonObject;
import uniandes.isis2304.parranderos.persistencia.PersistenciaParranderos;

/**
 * Clase principal del negocio
 * Sarisface todos los requerimientos funcionales del negocio
 *
 * @author Germán Bravo
 */
public class Parranderos 
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(Parranderos.class.getName());
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia
	 */
	private PersistenciaParranderos pp;
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * El constructor por defecto
	 */
	public Parranderos ()
	{
		pp = PersistenciaParranderos.getInstance ();
	}
	
	/**
	 * El constructor qye recibe los nombres de las tablas en tableConfig
	 * @param tableConfig - Objeto Json con los nombres de las tablas y de la unidad de persistencia
	 */
	public Parranderos (JsonObject tableConfig)
	{
		pp = PersistenciaParranderos.getInstance (tableConfig);
	}
	
	/**
	 * Cierra la conexión con la base de datos (Unidad de persistencia)
	 */
	public void cerrarUnidadPersistencia ()
	{
		pp.cerrarUnidadPersistencia ();
	}
	
	

	/* ****************************************************************
	 * 			Métodos para manejar las BEBIDAS
	 *****************************************************************/
	
	
	/**
	 * Elimina una bebida por su nombre
	 * Adiciona entradas al log de la aplicación
	 * @param nombre - El nombre de la bebida a eliminar
	 * @return El número de tuplas eliminadas
	 */
	public long eliminarBebidaPorNombre (String nombre)
	{
        log.info ("Eliminando bebida por nombre: " + nombre);
        long resp = pp.eliminarBebidaPorNombre (nombre);
        log.info ("Eliminando bebida por nombre: " + resp + " tuplas eliminadas");
        return resp;
	}
	
	/**
	 * Elimina una bebida por su identificador
	 * Adiciona entradas al log de la aplicación
	 * @param idBebida - El identificador de la bebida a eliminar
	 * @return El número de tuplas eliminadas (1 o 0)
	 */
	public long eliminarBebidaPorId (long idBebida)
	{
        log.info ("Eliminando bebida por id: " + idBebida);
        long resp = pp.eliminarBebidaPorId (idBebida);
        log.info ("Eliminando bebida por id: " + resp + " tuplas eliminadas");
        return resp;
	}
	
	

	/* ****************************************************************
	 * 			Métodos para manejar los OPERADOR
	 *****************************************************************/
	/**
	 * Adiciona de manera persistente un bar 
	 * Adiciona entradas al log de la aplicación
	 * @param nombre - El nombre del bar
	 * @param presupuesto - El presupuesto del bar (ALTO, MEDIO, BAJO)
	 * @param ciudad - La ciudad del bar
	 * @param sedes - El número de sedes que tiene el bar en la ciudad (Mayor que 0)
	 * @return El objeto Bar adicionado. null si ocurre alguna Excepción
	 */
	public Operador adicionarBar (String nombre, String presupuesto, String ciudad, int sedes)
	{
        log.info ("Adicionando bar: " + nombre);
        Operador bar = pp.adicionarOperador (nombre, presupuesto, ciudad, sedes);
        log.info ("Adicionando bar: " + bar);
        return bar;
	}
	
	/**
	 * Elimina un bar por su nombre
	 * Adiciona entradas al log de la aplicación
	 * @param nombre - El nombre del bar a eliminar
	 * @return El número de tuplas eliminadas
	 */
	public long eliminarBarPorNombre (String nombre)
	{
        log.info ("Eliminando bar por nombre: " + nombre);
        long resp = pp.eliminarBarPorNombre (nombre);
        log.info ("Eliminando bar: " + resp + " tuplas eliminadas");
        return resp;
	}
	
	/**
	 * Elimina un bebedor por su identificador
	 * Adiciona entradas al log de la aplicación
	 * @param idBar - El identificador del bar a eliminar
	 * @return El número de tuplas eliminadas
	 */
	public long eliminarBarPorId (long idBar)
	{
        log.info ("Eliminando bar por id: " + idBar);
        long resp = pp.eliminarOperadorPorId (idBar);
        log.info ("Eliminando bar: " + resp);
        return resp;
	}
	
	/**
	 * Encuentra todos los bares en Parranderos
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos Bar con todos las bares que conoce la aplicación, llenos con su información básica
	 */
	public List<Operador> darBares ()
	{
        log.info ("Listando Bares");
        List<Operador> bares = pp.darBares ();	
        log.info ("Listando Bares: " + bares.size() + " bares existentes");
        return bares;
	}

	/**
	 * Encuentra todos los bares en Parranderos y los devuelce como VO
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos Bar con todos las bares que conoce la aplicación, llenos con su información básica
	 */
	public List<VOOperador> darVOBares ()
	{
		log.info ("Generando los VO de Bares");
		List<VOOperador> voBares = new LinkedList<VOOperador> ();
		for (Operador bar: pp.darBares ())
		{
			voBares.add (bar);
		}
		log.info ("Generando los VO de Bares: " + voBares.size () + " bares existentes");
		return voBares;
	}

	/**
	 * Aumenta en 1 el número de sedes de los bares de una ciudad
	 * Adiciona entradas al log de la aplicación
	 * @param ciudad - La ciudad en la cual se aumenta el número de sedes de los bares
	 * @return El número de tuplas actualizadas
	 */
	public long aumentarSedesBaresCiudad (String ciudad)
	{
        log.info ("Aumentando sedes de bares de una ciudad: " + ciudad);
        long resp = pp.aumentarSedesBaresCiudad (ciudad);
        log.info ("Aumentando sedes de bares de una ciudad: " + resp + " tuplas actualizadas");
        return resp;
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar la relación RESERVA
	 *****************************************************************/
/**
 * Adiciona de manera persistente una reserva 
 * Adiciona entradas al log de la aplicación
 * @param idReserva
 * @param idInmueble
 * @param idCliente
 * @param fechaInicio
 * @param fechaFin
 * @param fechaGeneracion
 * @param fechaCancelacion
 * @param cancelado
 * @param numeroPersonas
 * @return
 */
	public Reserva adicionarReserva(long idReserva, long idInmueble,long idCliente,Timestamp fechaInicio, Timestamp fechaFin, Timestamp fechaGeneracion,Timestamp fechaCancelacion,char cancelado ,int numeroPersonas)
	{
		 log.info ("Adicionando Reservas");
	        Reserva resp = pp.adicionarReserva(idReserva, idInmueble, idCliente, fechaInicio, fechaFin, fechaGeneracion, fechaCancelacion, cancelado, numeroPersonas);
	        log.info ("Adicionando Reserva: " + resp + "tuplas Adicionadas");
	        return resp;
	}
	
	/**
	 * Elimina de manera persistente el hecho que una reserva es tomada por un cliente
	 * Adiciona entradas al log de la aplicación
	 * @param idReserva - El identificador de la reserva
	 * @param idInmueble - El identificador del Inmueble
	 * @return El número de tuplas eliminadas
	 */
	public long eliminarReserva (long idReserva, long idInmueble)
	{
        log.info ("Eliminando Reservas");
        long resp = pp.eliminarReserva (idReserva, idInmueble);
        log.info ("Eliminando Reserva: " + resp + "tuplas eliminadas");
        return resp;
	}
	
	/**
	 * Encuentra todos los RESERVA en Parranderos
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos RESERVA con todos los GUSTAN que conoce la aplicación, llenos con su información básica
	 */
	public List<Reserva> darReservas ()
	{
        log.info ("Listando Reservas");
        List<Reserva> sirven = pp.darReservas ();	
        log.info ("Listando Reservas: " + sirven.size() + " Reservas existentes");
        return sirven;
	}

	/**
	 * Encuentra todos los RESERVA en Parranderos y los devuelve como VO
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos RESERVA con todos los RESERVA que conoce la aplicación, llenos con su información básica
	 */
	public List<VOReserva> darVOReserva ()
	{
		log.info ("Generando los VO de Reserva");
		List<VOReserva> voGustan = new LinkedList<VOReserva> ();
		for (VOReserva reserva: pp.darReservas ())
		{
			voGustan.add (reserva);
		}
		log.info ("Generando los VO de Reserva: " + voGustan.size () + " Reservas existentes");
		return voGustan;
	}

	/* ****************************************************************
	 * 			Métodos para manejar la relación VISITAN
	 *****************************************************************/

	/**
	 * Adiciona de manera persistente el hecho que un bebedor visita un bar
	 * Adiciona entradas al log de la aplicación
	 * @param idBebedor - El identificador del bebedor
	 * @param idBar - El identificador del bar
	 * @param fecha - La fecha en la que se realizó la visita
	 * @param horario - El horario en el que se sirve la bebida (DIURNO, NOCTURNO, TODOS)
	 * @return Un objeto Visitan con los valores dados
	 */
	public Cxc adicionarCxc (long idReserva,double monto)
	{
        log.info ("Adicionando Cxc [" + idReserva + ", " + monto + "]");
        Cxc resp = pp.adicionarCxc (idReserva, monto);
        log.info ("Adicionando Cxc: " + resp + " tuplas insertadas");
        return resp;
	}
	
	/**
	 * Elimina de manera persistente el hecho que un bebedor visita un bar
	 * Adiciona entradas al log de la aplicación
	 * @param idBebedor - El identificador del bebedor
	 * @param idBar - El identificador del bar
	 * @return El número de tuplas eliminadas
	 */
	public long eliminarCxc (long idReserva)
	{
        log.info ("Eliminando Cxc");
        long resp = pp.eliminarCxcPorId (idReserva);
        log.info ("Eliminando Cxc: " + resp + " tuplas eliminadas");
        return resp;
	}
	
	/**
	 * Encuentra todos los VISITAN en Parranderos
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos VISITAN con todos los GUSTAN que conoce la aplicación, llenos con su información básica
	 */
	public List<Cxc> darCxc ()
	{
        log.info ("Listando Cxc");
        List<Cxc> visitan = pp.darCxc();	
        log.info ("Listando Cxc: Listo!");
        return visitan;
	}

	/**
	 * Encuentra todos los visitan en Parranderos y los devuelve como VO
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos Visitan con todos los Visitan que conoce la aplicación, llenos con su información básica
	 */
	public List<VOCxc> darVOCxc ()
	{
		log.info ("Generando los VO de Cxc");
		List<VOCxc> voCxc = new LinkedList<VOCxc> ();
		for (VOCxc vis: pp.darCxc ())
		{
			voCxc.add (vis);
		}
		log.info ("Generando los VO de Cxc: " + voCxc.size () + " Cxc existentes");
		return voCxc;
	}

	/* ****************************************************************
	 * 			Métodos para administración
	 *****************************************************************/

	/**
	 * Elimina todas las tuplas de todas las tablas de la base de datos de Parranderos
	 * @return Un arreglo con 7 números que indican el número de tuplas borradas en las tablas GUSTAN, SIRVEN, VISITAN, BEBIDA,
	 * TIPOBEBIDA, BEBEDOR y BAR, respectivamente
	 */
	public long [] limpiarParranderos ()
	{
        log.info ("Limpiando la BD de Parranderos");
        long [] borrrados = pp.limpiarParranderos();	
        log.info ("Limpiando la BD de Parranderos: Listo!");
        return borrrados;
	}
}
