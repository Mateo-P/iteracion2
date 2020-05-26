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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.parranderos.negocio.Cliente;
import uniandes.isis2304.parranderos.negocio.Reserva;


/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos para el concepto VISITAN de Parranderos
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 * 
 * @author Germán Bravo
 */
class SQLCliente 
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos
	 * Se renombra acá para facilitar la escritura de las sentencias
	 */
	private final static String SQL = PersistenciaAlohandes.SQL;

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia general de la aplicación
	 */
	private PersistenciaAlohandes pp;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * Constructor
	 * @param pp - El Manejador de persistencia de la aplicación
	 */
	public SQLCliente (PersistenciaAlohandes pp)
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar un VISITAN a la base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @param idBebedor - El identificador del bebedor
	 * @param idBar - El identificador del bar
	 * @param fecha - La fecha en que se realizó la visita
	 * @param horario - EL horario en que se realizó la visita (DIURNO, NOCTURNO, TODOS)
	 * @return EL número de tuplas insertadas
	 */
	public long adicionarCliente (PersistenceManager pm, long idReserva, double monto) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaCliente() + "(ID_RESERVA, MONTO) values (?, ?)");
        q.setParameters(idReserva, monto);
        return (long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para eliminar TODAS LAS VISITAS de la base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarCxc (PersistenceManager pm) 
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaCxc ());
        return (long) q.executeUnique();
	}

	public Cliente buscarClientePorId (PersistenceManager pm, long idCliente) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaCliente() + " WHERE ID_CLIENTE = ?");
		q.setResultClass(Reserva.class);
		q.setParameters(idCliente);
		return (Cliente) q.executeUnique();
	}

	

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de los VISITAN de la 
	 * base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos VISITAN
	 */
	public List<Cliente> darClientes (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaCliente());
		q.setResultClass(Cliente.class);
		return (List<Cliente>) q.execute();
	}

	public List<Cliente> darClientesTOP1 (PersistenceManager pm, Timestamp semana)
	{
		Query q = pm.newQuery(SQL, "WHERE RESERVA.ID_CLIENTE=CLIENTE.ID_CLIENTE and reserva.fecha_inicio_reserva < ? "
				+ "and reserva.fecha_final_reserva > ? "
				+ "group by cliente.id_cliente,cliente.nombre_cliente,cliente.cedula_cliente,cliente.habilitado_cliente,cliente.telefono_cliente,cliente.correo_cliente,cliente.tipo_vinculo_cliente");
		q.setResultClass(Cliente.class);
		q.setParameters(semana, semana);
		return (List<Cliente>) q.execute();
	} 	
	
	public List<Cliente> darClientesTOP2 (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT cliente.id_cliente,cliente.nombre_cliente,cliente.cedula_cliente,cliente.habilitado_cliente,cliente.telefono_cliente,cliente.correo_cliente,cliente.tipo_vinculo_cliente FROM " 
				+ pp.darTablaReserva()+","+ pp.darTablaCliente()+","+ pp.darTablaInmueble()
				+"WHERE RESERVA.ID_CLIENTE=CLIENTE.ID_CLIENTE AND RESERVA.ID_INMUEBLE=inmueble.id_inmueble AND inmueble.costo_noche>149"
				+" group by cliente.id_cliente,cliente.nombre_cliente,cliente.cedula_cliente,cliente.habilitado_cliente,cliente.telefono_cliente,cliente.correo_cliente,cliente.tipo_vinculo_cliente");
		q.setResultClass(Cliente.class);
		return (List<Cliente>) q.execute();
	} 	
	
	public List<Cliente> darClientesTOP3 (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT cliente.id_cliente,cliente.nombre_cliente,cliente.cedula_cliente,cliente.habilitado_cliente,cliente.telefono_cliente,cliente.correo_cliente,cliente.tipo_vinculo_cliente "
				+ "FROM RESERVA, CLIENTE,INMUEBLE,HABITACION_HOTEL "
				+ "WHERE RESERVA.ID_CLIENTE=CLIENTE.ID_CLIENTE AND "
				+ "RESERVA.ID_INMUEBLE=inmueble.id_inmueble AND "
				+ "inmueble.id_inmueble = habitacion_hotel.id_inmueble_hh AND "
				+ "habitacion_hotel.tipo_habitacion=SUITE "
				+ "group by cliente.id_cliente,cliente.nombre_cliente,cliente.cedula_cliente,cliente.habilitado_cliente,cliente.telefono_cliente,cliente.correo_cliente,cliente.tipo_vinculo_cliente");
		q.setResultClass(Cliente.class);
		return (List<Cliente>) q.execute();
	} 
}
