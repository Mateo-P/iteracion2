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

package uniandes.isis2304.parranderos.persistencia;

import java.sql.Timestamp;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.parranderos.negocio.Bebida;
import uniandes.isis2304.parranderos.negocio.Reserva;
import uniandes.isis2304.parranderos.negocio.TipoBebida;

/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos para el concepto SIRVEN de Parranderos
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 * 
 * @author Germán Bravo
 */
class SQLReserva
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos
	 * Se renombra acá para facilitar la escritura de las sentencias
	 */
	private final static String SQL = PersistenciaParranderos.SQL;

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia general de la aplicación
	 */
	private PersistenciaParranderos pp;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * Constructor
	 * @param pp - El Manejador de persistencia de la aplicación
	 */
	public SQLReserva (PersistenciaParranderos pp)
	{
		this.pp = pp;
	}

	/**
	 * Crea y ejecuta la sentencia SQL para adicionar una RESERVA a la base de datos de Alohandes
	 * @param pm - El manejador de persistencia
	 * @param idBar - El identificador del bar
	 * @param idBebida - El identificador de la bebida
	 * @param horario - El horario en que el bar sirve la bebida (DIURNO, NOCTURNO, TDOOS)
	 * @return EL número de tuplas insertadas
	 */
	public long adicionarReserva (PersistenceManager pm, long idReserva,Timestamp fechaInicioReserva,Timestamp fechaFinReserva,Timestamp fechaGeneracionReserva,int numeroPersonas,long idInmueble) 
	{
		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaReserva () + "(ID_RESERVA,FECHA_INICIO_RESERVA,FECHA_FINAL_RESERVA,FECHA_GENERACION_RESERVA,NUMERO_PERSONAS,ID_INMUEBLE) VALUES (?, ?, ?, ?, ?, ?)");
		q.setParameters(idReserva, fechaInicioReserva, fechaFinReserva,fechaGeneracionReserva,numeroPersonas,idInmueble);
		return (long)q.executeUnique();            
	}

	/**
	 * Crea y ejecuta la sentencia SQL para eliminar UN SIRVEN de la base de datos de Parranderos, por sus identificador
	 * @param pm - El manejador de persistencia
	 * @param idBar - El identificador del bar
	 * @param idBebida - El identificador de la bebida
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarReserva (PersistenceManager pm, long idReserva, long idInmueble) 
	{
		Query q1 = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaReserva () + " WHERE ID_RESERVA = ?");
		q1.setResultClass(TipoBebida.class);
		q1.setParameters(idReserva);
		Reserva reserva=  (Reserva) q1.executeUnique();
		Timestamp fechafin = reserva.getFechaFin();
		Timestamp fechainicio = reserva.getFechaInicio();
		int margenDias = 7*24*60*60*1000;
		int diaActual= (int) System.currentTimeMillis();

		if(diaActual<fechainicio.getTime()-margenDias)
		{
			//10%
		}

		if(fechainicio.getTime()-margenDias<diaActual && diaActual< fechainicio.getTime())
		{
			//30%
		}	
		if(fechainicio.getTime()<diaActual&& diaActual< fechafin.getTime())
		{
			Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaReserva () + " WHERE ID_RESERVA = ? AND ID_INMUEBLE = ?");
			q.setParameters(idReserva, idInmueble);
			return (long) q.executeUnique();    
		}
		return 0;
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de los SIRVEN de la 
	 * base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos SIRVEN
	 */
	public List<Reserva> darReservas (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaReserva ());
		q.setResultClass(Reserva.class);
		return (List<Reserva>) q.execute();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de UNA RESERVA de la 
	 * base de datos de Parranderos, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param idReserva - El identificador de la reserva
	 * @return El objeto RESERVA que tiene el identificador dado
	 */
	public Reserva buscarReservaPorId (PersistenceManager pm, long idReserva) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaReserva () + " WHERE RESERVA.ID_RESERVA = ?");
		q.setResultClass(Reserva.class);
		q.setParameters(idReserva);
		return (Reserva) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de UNA RESERVA de la 
	 * base de datos de Parranderos, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param idReserva - El identificador de la reserva
	 * @return El objeto RESERVA que tiene el identificador dado
	 */
	public Reserva ValidarReserva (PersistenceManager pm, Timestamp fechaInicio,Timestamp fechafin, long idInmueble) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaReserva () + "WHERE RESERVA.ID_INMUEBLE = ? AND RESERVA.FECHA_INICIO_RESERVA=? AND RESERVA.FECHA_FINAL_RESERVA=?");
		q.setResultClass(Reserva.class);
		q.setParameters(idInmueble,fechaInicio,fechafin);
		return (Reserva) q.executeUnique();
	}
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar el identificador y el número de bebidas que sirven los bares de la 
	 * base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @return Una lista de parejas de objetos, el primer elemento de cada pareja representa el identificador de un bar,
	 * 	el segundo elemento representa el número de bebidas que sirve (Una bebida que se sirve en dos horarios cuenta dos veces)
	 */
	public List<Object []> darBaresYCantidadBebidasSirven (PersistenceManager pm)
	{
		String sql = "SELECT idBar, count (*) as numBebidas";
		sql += " FROM " + pp.darTablaReserva ();
		sql	+= " GROUP BY idBar";
		Query q = pm.newQuery(SQL, sql);
		return q.executeList();
	}

}
