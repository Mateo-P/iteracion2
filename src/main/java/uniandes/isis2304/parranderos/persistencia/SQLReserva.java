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


import uniandes.isis2304.parranderos.negocio.Reserva;


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
	public long adicionarReserva (PersistenceManager pm,long idReserva, long idInmueble,long idCliente,Timestamp fechaInicio, Timestamp fechaFin, Timestamp fechaGeneracion,Timestamp fechaCancelacion,char cancelado ,int numeroPersonas) 
	{
		long retorno =0;
		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaReserva () + "(ID_RESERVA,FECHA_INICIO_RESERVA,FECHA_FINAL_RESERVA,FECHA_GENERACION_RESERVA,FECHA_CANCELACION,CANCELADO,NUMERO_PERSONAS,ID_CLIENTE,ID_INMUEBLE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
		q.setParameters( idReserva,fechaInicio,fechaFin,fechaGeneracion,fechaCancelacion, cancelado,numeroPersonas , idCliente, idInmueble);
		retorno= (long)q.executeUnique();   
		double monto =0;
		Query q1 = pm.newQuery(SQL,"SELECT INMUEBLE.COSTO_NOCHE FROM "+pp.darTablaInmueble()+" WHERE INMUEBLE.ID_INMUEBLE=?");
		q1.setParameters(idInmueble);
		monto = (double)q1.executeUnique()*calcularIntervaloDias(fechaInicio, fechaFin);
		//falta averiguar si hay servicios incluidos
		
		Query q2 = pm.newQuery(SQL, "INSERT INTO "+ pp.darTablaCxc() + " (ID_RESERVA,MONTO) VALUES (?,?)");
		q2.setParameters(idReserva,monto);
		q2.executeUnique();
		return retorno;
	}

	/**
	 * Crea y ejecuta la sentencia SQL para eliminar UN reserva de la base de datos de Parranderos, por sus identificador
	 * @param pm - El manejador de persistencia
	 * @param idBar - El identificador del bar
	 * @param idBebida - El identificador de la bebida
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarReserva (PersistenceManager pm, long idReserva) 
	{
		Query q1 = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaReserva () + " WHERE ID_RESERVA = ?");
		q1.setResultClass(Reserva.class);
		q1.setParameters(idReserva);
		Reserva reserva=  (Reserva) q1.executeUnique();
		Timestamp fechafin = reserva.getFechaFin();
		Timestamp fechainicio = reserva.getFechaInicio();
		int margenDias = 7*24*60*60*1000;
		double penalizacion=0;
		if(fechafin.getTime()-fechainicio.getTime()<7*24*60*60*1000){
			margenDias=3*24*60*60*1000;
		}
		int diaActual= (int) System.currentTimeMillis();
		if(diaActual<fechainicio.getTime()-margenDias){
			penalizacion=0.1;
		}
		if(fechainicio.getTime()-margenDias<diaActual && diaActual< fechainicio.getTime()){
			penalizacion=0.3;
		}	
		if(fechainicio.getTime()<diaActual&& diaActual< fechafin.getTime()){
			penalizacion=0.5;	   
		}
		//UPDATE CXC SET MONTO=MONTO*0.1 WHERE ID_RESERVA=2234567898;
		Query q2=pm.newQuery(SQL,"UPDATE "+pp.darTablaCxc()+ " SET MONTO=MONTO*"+penalizacion+" WHERE ID_RESERVA=?");
		q2.setParameters(idReserva);
		q2.executeUnique();
		Timestamp fechahoy = new Timestamp(System.currentTimeMillis());
		Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaReserva () + "SET FECHA_CANCELACION=? , CANCELADO=? WHERE ID_RESERVA = ?");
		q.setParameters(fechahoy,'Y',idReserva);
		return (long) q.executeUnique(); 
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
	public int calcularIntervaloDias(Timestamp fechaInicio, Timestamp fechafin)
	{
		int dias=0;
		int rango= (int) ( fechaInicio.getTime()-fechafin.getTime());
		dias=rango/86400000;
		return dias;
		
	}
}
