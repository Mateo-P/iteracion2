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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.JDODataStoreException;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;
import org.apache.log4j.Logger;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import uniandes.isis2304.parranderos.negocio.*;
import uniandes.isis2304.parranderos.negocio.Reserva.cmpFechaGeneracion;
import uniandes.isis2304.parranderos.negocio.Restriccion_Inmueble.TIPO_INMUEBLE;

/**
 * Clase para el manejador de persistencia del proyecto Parranderos Traduce la
 * información entre objetos Java y tuplas de la base de datos, en ambos
 * sentidos Sigue un patrón SINGLETON (Sólo puede haber UN objeto de esta clase)
 * para comunicarse de manera correcta con la base de datos Se apoya en las
 * clases SQLBar, SQLBebedor, SQLBebida, SQLGustan, SQLSirven, SQLTipoBebida y
 * SQLVisitan, que son las que realizan el acceso a la base de datos
 * 
 * @author Germán Bravo
 */
public class PersistenciaAlohandes {
	/*
	 * ****************************************************************
	 * Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(PersistenciaAlohandes.class.getName());

	/**
	 * Cadena para indicar el tipo de sentencias que se va a utilizar en una
	 * consulta
	 */
	public final static String SQL = "javax.jdo.query.SQL";

	/*
	 * ****************************************************************
	 * Atributos
	 *****************************************************************/
	/**
	 * Atributo privado que es el único objeto de la clase - Patrón SINGLETON
	 */
	private static PersistenciaAlohandes instance;

	/**
	 * Fábrica de Manejadores de persistencia, para el manejo correcto de las
	 * transacciones
	 */
	private PersistenceManagerFactory pmf;

	/**
	 * Arreglo de cadenas con los nombres de las tablas de la base de datos, en
	 * su orden: Secuenciador, tipoBebida, bebida, bar, bebedor, gustan, sirven
	 * y visitan
	 */
	private List<String> tablas;

	/**
	 * Atributo para el acceso a las sentencias SQL propias a
	 * PersistenciaParranderos
	 */
	private SQLUtil sqlUtil;
	/**
	 * Atributo para el acceso a la tabla INMUEBLE de la base de datos
	 */
	private SQLInmueble sqlInmueble;

	/**
	 * Atributo para el acceso a la tabla Apartamento de la base de datos
	 */
	private SQLApartamento sqlApartamento;
	/**
	 * Atributo para el acceso a la tabla BAR de la base de datos
	 */
	private SQLOperador sqlOperador;

	/**
	 * Atributo para el acceso a la tabla SIRVEN de la base de datos
	 */
	private SQLReserva sqlReserva;

	/**
	 * Atributo para el acceso a la tabla VISITAN de la base de datos
	 */
	private SQLCxc sqlCxc;

	/*
	 * **************************************************************** Métodos
	 * del MANEJADOR DE PERSISTENCIA
	 *****************************************************************/

	/**
	 * Constructor privado con valores por defecto - Patrón SINGLETON
	 */
	private PersistenciaAlohandes() {
		pmf = JDOHelper.getPersistenceManagerFactory("Parranderos");
		crearClasesSQL();

		// Define los nombres por defecto de las tablas de la base de datos
		tablas = new LinkedList<String>();
		tablas.add("Parranderos_sequence");
		tablas.add("OPERADOR");
		tablas.add("CLIENTE");
		tablas.add("PAGO");
		tablas.add("RESERVA");
		tablas.add("CXC");
		tablas.add("INMUEBLE");
		tablas.add("HABITACION_PERSONA_NATURAL");
		tablas.add("HABITACION_HOSTEL");
		tablas.add("HABITACION_HOTEL");
		tablas.add("APARTAMENTO");
		tablas.add("VIVIENDA_CEDIDA");
		tablas.add("HABITACION_UNIVERSITARIA");
		tablas.add("RESTRICCION_INMUEBLE");

		tablas.add("SERVICIO_ADICIONAL");
		tablas.add("OFRECEN");
	}

	/**
	 * Constructor privado, que recibe los nombres de las tablas en un objeto
	 * Json - Patrón SINGLETON
	 * 
	 * @param tableConfig
	 *            - Objeto Json que contiene los nombres de las tablas y de la
	 *            unidad de persistencia a manejar
	 */
	private PersistenciaAlohandes(JsonObject tableConfig) {
		crearClasesSQL();
		tablas = leerNombresTablas(tableConfig);

		String unidadPersistencia = tableConfig.get("unidadPersistencia").getAsString();
		log.trace("Accediendo unidad de persistencia: " + unidadPersistencia);
		pmf = JDOHelper.getPersistenceManagerFactory(unidadPersistencia);
	}

	/**
	 * @return Retorna el único objeto PersistenciaParranderos existente -
	 *         Patrón SINGLETON
	 */
	public static PersistenciaAlohandes getInstance() {
		if (instance == null) {
			instance = new PersistenciaAlohandes();
		}
		return instance;
	}

	/**
	 * Constructor que toma los nombres de las tablas de la base de datos del
	 * objeto tableConfig
	 * 
	 * @param tableConfig
	 *            - El objeto JSON con los nombres de las tablas
	 * @return Retorna el único objeto PersistenciaParranderos existente -
	 *         Patrón SINGLETON
	 */
	public static PersistenciaAlohandes getInstance(JsonObject tableConfig) {
		if (instance == null) {
			instance = new PersistenciaAlohandes(tableConfig);
		}
		return instance;
	}

	/**
	 * Cierra la conexión con la base de datos
	 */
	public void cerrarUnidadPersistencia() {
		pmf.close();
		instance = null;
	}

	/**
	 * Genera una lista con los nombres de las tablas de la base de datos
	 * 
	 * @param tableConfig
	 *            - El objeto Json con los nombres de las tablas
	 * @return La lista con los nombres del secuenciador y de las tablas
	 */
	private List<String> leerNombresTablas(JsonObject tableConfig) {
		JsonArray nombres = tableConfig.getAsJsonArray("tablas");

		List<String> resp = new LinkedList<String>();
		for (JsonElement nom : nombres) {
			resp.add(nom.getAsString());
		}

		return resp;
	}

	/**
	 * Crea los atributos de clases de apoyo SQL
	 */
	private void crearClasesSQL() {

		sqlOperador = new SQLOperador(this);
		sqlInmueble = new SQLInmueble(this);
		sqlReserva = new SQLReserva(this);
		sqlApartamento = new SQLApartamento(this);
		sqlCxc = new SQLCxc(this);
		sqlUtil = new SQLUtil(this);
	}

	/**
	 * @return La cadena de caracteres con el nombre del secuenciador de
	 *         parranderos
	 */
	public String darSeqParranderos() {
		return tablas.get(0);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de OPERADOR de
	 *         parranderos
	 */
	public String darTablaOperador() {
		return tablas.get(1);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Cliente de
	 *         parranderos
	 */
	public String darTablaCliente() {
		return tablas.get(2);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Pago de
	 *         parranderos
	 */
	public String darTablaPago() {
		return tablas.get(3);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Reserva de
	 *         parranderos
	 */
	public String darTablaReserva() {
		return tablas.get(4);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Cxc de
	 *         parranderos
	 */
	public String darTablaCxc() {
		return tablas.get(5);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Inmueble de
	 *         parranderos
	 */
	public String darTablaInmueble() {
		return tablas.get(6);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de
	 *         Habitacion_Persona_Natural de parranderos
	 */
	public String darTablaHabitacionPersonaNatural() {
		return tablas.get(7);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de
	 *         HABITACION_HOSTEL de parranderos
	 */
	public String darTablaHabitacionHostel() {
		return tablas.get(8);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de
	 *         HABITACION_HOTEL de parranderos
	 */
	public String darTablaHabitacionHotel() {
		return tablas.get(9);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de APARTAMENTO
	 *         de parranderos
	 */
	public String darTablaApartamento() {
		return tablas.get(10);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de
	 *         VIVIENDA_CEDIDA de parranderos
	 */
	public String darTablaViviendaCedida() {
		return tablas.get(11);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de
	 *         HABITACION_UNIVERSITARIA de parranderos
	 */
	public String darTablaHabitacionUniversitaria() {
		return tablas.get(12);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de
	 *         RESTRICCION_INMUEBLE de parranderos
	 */
	public String darTablaRestriccionInmueble() {
		return tablas.get(13);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de
	 *         RESTRICCION_INMUEBLE de parranderos
	 */
	public String darTablaServiciosAdicional() {
		return tablas.get(14);
	}

	public String darTablaOfrecen() {
		return tablas.get(15);
	}

	/**
	 * Transacción para el generador de secuencia de Parranderos Adiciona
	 * entradas al log de la aplicación
	 * 
	 * @return El siguiente número del secuenciador de Parranderos
	 */
	private long nextval() {
		long resp = sqlUtil.nextval(pmf.getPersistenceManager());
		log.trace("Generando secuencia: " + resp);
		return resp;
	}

	/**
	 * Extrae el mensaje de la exception JDODataStoreException embebido en la
	 * Exception e, que da el detalle específico del problema encontrado
	 * 
	 * @param e
	 *            - La excepción que ocurrio
	 * @return El mensaje de la excepción JDO
	 */
	private String darDetalleException(Exception e) {
		String resp = "";
		if (e.getClass().getName().equals("javax.jdo.JDODataStoreException")) {
			JDODataStoreException je = (javax.jdo.JDODataStoreException) e;
			return je.getNestedExceptions()[0].getMessage();
		}
		return resp;
	}
	/*
	 * **************************************************************** Métodos
	 * para manejar los OPERADOR
	 *****************************************************************/

	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla BAR
	 * Adiciona entradas al log de la aplicación
	 * 
	 * @param nombre
	 *            - El nombre del bar
	 * @param ciudad
	 *            - La ciudad del bar
	 * @param presupuesto
	 *            - El presupuesto del bar (ALTO, MEDIO, BAJO)
	 * @param sedes
	 *            - El número de sedes del bar en la ciudad (Mayor que 0)
	 * @return El objeto Bar adicionado. null si ocurre alguna Excepción
	 */
	public Operador adicionarOperador(String nombre, String ciudad, String presupuesto, int sedes) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long idBar = nextval();
			long tuplasInsertadas = sqlOperador.adicionarOperador(pm);
			tx.commit();

			log.trace("Inserción de Bar: " + nombre + ": " + tuplasInsertadas + " tuplas insertadas");

			return new Operador(idBar, nombre, ciudad, presupuesto, presupuesto, sedes, presupuesto, sedes,
					presupuesto);
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que elimina, de manera transaccional, una tupla en la tabla BAR,
	 * dado el identificador del bar Adiciona entradas al log de la aplicación
	 * 
	 * @param idBar
	 *            - El identificador del bar
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public long eliminarOperadorPorId(long idBar) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long resp = sqlOperador.eliminarBarPorId(pm, idBar);
			tx.commit();

			return resp;
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que consulta todas las tuplas en la tabla BAR
	 * 
	 * @return La lista de objetos BAR, construidos con base en las tuplas de la
	 *         tabla BAR
	 */
	public List<Operador> darOperadores() {
		return sqlOperador.darBares(pmf.getPersistenceManager());
	}

	/**
	 * Método que consulta todas las tuplas en la tabla BAR
	 * 
	 * @return La lista de objetos Inmueble, construidos con base en las tuplas
	 *         de la tabla BAR
	 */

	public List<Inmueble> darInmuebles() {
		return sqlInmueble.darInmuebles(pmf.getPersistenceManager());
	}

	public List<Inmueble> darInmueblesDisponibles(PersistenceManager pm, List<Inmueble> inmuebles,
			Timestamp fechaInicio, Timestamp fechaFin) {

		List<Inmueble> disponibles = new ArrayList<Inmueble>();
		for (int i = 0; i < inmuebles.size(); i++) {
			List<Reserva> reservas = sqlReserva.darReservasporInmuebleId(pm, inmuebles.get(i).getId());
			boolean validar = true;
			for (int j = 0; j < reservas.size(); j++) {
				if (reservas.get(j).getFechaFin().compareTo(fechaInicio) < 0
						|| reservas.get(j).getFechaInicio().compareTo(fechaFin) > 0) {
					validar = !validar;
				}
			}
			if (validar) {
				disponibles.add(inmuebles.get(i));
			}

		}
		return disponibles;
	}

	public List<Apartamento> darApartamentos() {
		return sqlApartamento.darApartamentos(pmf.getPersistenceManager());
	}

	/**
	 * Método que consulta todas las tuplas en la tabla INmueble que tienen el
	 * identificador dado
	 * 
	 * @param idInmueble
	 *            - El identificador del bar
	 * @return El objeto BAR, construido con base en la tuplas de la tabla BAR,
	 *         que tiene el identificador dado
	 */

	public Inmueble darInmueblePorId(long idInmueble) {
		return sqlInmueble.darInmueblePorId(pmf.getPersistenceManager(), idInmueble);
	}

	/**
	 * Método que consulta todas las tuplas en la tabla BAR que tienen el nombre
	 * dado
	 * 
	 * @param nombreBar
	 *            - El nombre del bar
	 * @return La lista de objetos BAR, construidos con base en las tuplas de la
	 *         tabla BAR
	 */
	public List<Operador> darOperadoresPorNombre(String nombreBar) {
		return sqlOperador.darBaresPorNombre(pmf.getPersistenceManager(), nombreBar);
	}

	/**
	 * Método que consulta todas las tuplas en la tabla BAR que tienen el
	 * identificador dado
	 * 
	 * @param idOperador
	 *            - El identificador del OPerador
	 * @return El objeto BAR, construido con base en la tuplas de la tabla BAR,
	 *         que tiene el identificador dado
	 */
	public Operador darOperadorPorId(long idOperador) {
		return sqlOperador.darBarPorId(pmf.getPersistenceManager(), idOperador);
	}

	/*
	 * **************************************************************** Métodos
	 * para manejar la relación RESERVA
	 *****************************************************************/
	/**
	 * Método que consulta todas las tuplas en la tabla BAR que tienen el
	 * identificador dado
	 * 
	 * @param idReserva
	 *            - El identificador del bar
	 * @return El objeto BAR, construido con base en la tuplas de la tabla BAR,
	 *         que tiene el identificador dado
	 */
	public Reserva darReservaPorId(long idReserva) {
		return sqlReserva.buscarReservaPorId(pmf.getPersistenceManager(), idReserva);
	}

	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla
	 * RESERVA Adiciona entradas al log de la aplicación
	 * 
	 * @param idCliente
	 *            - El identificador del cliente- Debe haber un RESERVA con ese
	 *            identificador
	 * @param idInmueble
	 *            - El identificador dEl INMUEBLE - Debe haber una INMUEBLE con
	 *            ese identificador
	 * 
	 * @return Un objeto RESERVA con la información dada. Null si ocurre alguna
	 *         Excepción
	 */
	public Reserva adicionarReserva(long idInmueble, long idCliente, Timestamp fechaInicio, Timestamp fechaFin,
			Timestamp fechaGeneracion, Timestamp fechaCancelacion, char cancelado, int numeroPersonas) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {

			tx.begin();
			long idReserva = nextval();
			System.out.println("antes");
			long tuplasInsertadas = sqlReserva.adicionarReserva(pmf.getPersistenceManager(), idReserva, idInmueble,
					idCliente, fechaInicio, fechaFin, fechaGeneracion, fechaCancelacion, cancelado, numeroPersonas);
			tx.commit();

			Reserva reservaNueva = new Reserva(idReserva, idInmueble, idCliente, fechaInicio, fechaFin, fechaGeneracion,
					fechaCancelacion, cancelado, numeroPersonas);
			System.out.println("" + reservaNueva.getFechaFin());
			log.trace("Inserción de Reserva: [" + idReserva + ", " + idInmueble + "]. " + tuplasInsertadas
					+ " tuplas insertadas");

			return reservaNueva;
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	public List<Reserva> adicionarReservasMasivas(String tipoInmueble, int cantidad, Timestamp fechaInicio,
			Timestamp fechaFin, long idCliente, Timestamp fechaGeneracion, Timestamp fechaCancelacion, char cancelado) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		List<Inmueble> inmuebles = sqlInmueble.darInmueblesPorTipo(pm, tipoInmueble);
		List<Inmueble> disponibles = darInmueblesDisponibles(pm, inmuebles, fechaInicio, fechaFin);
		List<Reserva> nuevasReservas = new ArrayList<Reserva>();
		int capacidad = 0;
		for (int i = 0; i < disponibles.size(); i++) {
			capacidad += disponibles.get(i).getCapacidad();
		}
		if (capacidad > cantidad) {
			try {
				tx.begin();

				long tuplasInsertadas = 0;
				for (int i = 0; i < disponibles.size() && cantidad > 0; i++) {
					long idReserva = 8000 + i;
					long tuplasInsetadas = sqlReserva.adicionarReserva(pm, idReserva, disponibles.get(i).getId(),
							idCliente, fechaInicio, fechaFin, fechaGeneracion, fechaCancelacion, cancelado,
							disponibles.get(i).getCapacidad());
					log.trace("Inserción de Reserva: [" + idReserva + ", " + disponibles.get(i).getId() + "]. ");
					tuplasInsertadas++;

					Reserva reservaNueva = new Reserva(idReserva, disponibles.get(i).getId(), idCliente, fechaInicio,
							fechaFin, fechaGeneracion, fechaCancelacion, cancelado, disponibles.get(i).getCapacidad());
					System.out.println("" + reservaNueva.getFechaFin());
					nuevasReservas.add(reservaNueva);
				}
				log.trace(tuplasInsertadas + " tuplas insertadas");
				tx.commit();
				return nuevasReservas;
			} catch (Exception e) {
				// e.printStackTrace();
				log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));

			} finally {
				if (tx.isActive()) {
					tx.rollback();
				}
				pm.close();
			}
		}
		return null;
	}

	/**
	 * Método que elimina, de manera transaccional, una tupla en la tabla
	 * RESERVA, dados los identificadores de RESERVA e Inmueble
	 * 
	 * @param idReserva
	 *            - El identificador de la Reserva
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public long eliminarReserva(long idReserva) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long resp = sqlReserva.eliminarReserva(pm, idReserva);
			tx.commit();
			log.trace("Se eliminino " + resp + " reserva ");
			return resp;
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	public long eliminarReservasMasivas(long idCliente, Timestamp fechaInicio, Timestamp fechaFin,
			Timestamp fechaGeneracion) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		List<Reserva> reservaMasiva = sqlReserva.darReservasMasivas(pm, idCliente, fechaInicio, fechaFin,
				fechaGeneracion);
		try {
			tx.begin();
			long resp = 0;
			for (int i = 0; i < reservaMasiva.size(); i++) {
				resp += sqlReserva.eliminarReserva(pm, reservaMasiva.get(i).getIdReserva());
			}
			log.trace(resp + " tuplas eliminadas");
			tx.commit();

			return resp;
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla
	 * Apartamento Adiciona entradas al log de la aplicación
	 * 
	 * @return Un objeto Apartamentocon la información dada. Null si ocurre
	 *         alguna Excepción
	 */
	public Apartamento adicionarApartamento(int numeroHabitaciones, boolean amoblado, boolean serviciosIncluidos,
			String nombre, String tipoInmueble, String ubicacion, int capacidad, boolean disponible, String foto,
			String descripcion, int veceReservada, double costoXNoche, long idOperador) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long idApartamento = nextval();
			long tuplasInsertadas = sqlApartamento.adicionarApartamento(pmf.getPersistenceManager(), idApartamento,
					numeroHabitaciones, amoblado, serviciosIncluidos, nombre, tipoInmueble, ubicacion, capacidad,
					disponible, foto, descripcion, veceReservada, costoXNoche, idOperador);

			tx.commit();

			log.trace("Inserción de Apartamento: [" + idApartamento + ", " + nombre + "]. " + tuplasInsertadas
					+ " tuplas insertadas");

			return new Apartamento(numeroHabitaciones, amoblado, serviciosIncluidos, idApartamento);
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que elimina, de manera transaccional, una tupla en la tabla
	 * INmueble, dados los identificadores de RESERVA e Inmueble
	 * 
	 * @param idInmueble
	 *            - El identificador del Inmueble
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public long eliminarInmueble(long idInmueble) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long resp = sqlInmueble.eliminarInmueble(pm, idInmueble);
			tx.commit();

			return resp;
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que consulta todas las tuplas en la tabla RESERVA
	 * 
	 * @return La lista de objetos RESERVA, construidos con base en las tuplas
	 *         de la tabla RESERVA
	 */
	public List<Reserva> darReservas() {
		return sqlReserva.darReservas(pmf.getPersistenceManager());

	}

	public List<Reserva> darReservaPorMueble(long muebleId) {
		return sqlReserva.darReservasporInmuebleId(pmf.getPersistenceManager(), muebleId);

	}

	/*
	 * **************************************************************** Métodos
	 * para manejar la relación CXC
	 *****************************************************************/

	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla
	 * VISITAN Adiciona entradas al log de la aplicación
	 * 
	 * @return Un objeto VISITAN con la información dada. Null si ocurre alguna
	 *         Excepción
	 */
	public Cxc adicionarCxc(long idReserva, double monto) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long tuplasInsertadas = sqlCxc.adicionarCxc(pm, idReserva, monto);
			tx.commit();

			log.trace(
					"Inserción de Cxc: [" + idReserva + ", " + monto + "]. " + tuplasInsertadas + " tuplas insertadas");

			return new Cxc(idReserva, monto);
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que elimina, de manera transaccional, una tupla en la tabla
	 * VISITAN, dados los identificadores de bebedor y bar
	 * 
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public long eliminarCxcPorId(long idReserva) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long resp = sqlCxc.eliminarCxcPorIdReserva(pm, idReserva);
			tx.commit();

			return resp;
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que elimina, de manera transaccional, una tupla en la tabla
	 * VISITAN, dados el identificador del bebedor
	 * 
	 * @param idBebedor
	 *            - El identificador del bebedor
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public long eliminarCxcPorIdReserva(long idBebedor) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long visitasEliminadas = sqlCxc.eliminarCxcPorIdReserva(pm, idBebedor);
			tx.commit();

			return visitasEliminadas;
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que consulta todas las tuplas en la tabla VISITAN
	 * 
	 * @return La lista de objetos VISITAN, construidos con base en las tuplas
	 *         de la tabla VISITAN
	 */
	public List<Cxc> darCxc() {
		return sqlCxc.darCxc(pmf.getPersistenceManager());
	}

	/**
	 * Elimina todas las tuplas de todas las tablas de la base de datos de
	 * Parranderos Crea y ejecuta las sentencias SQL para cada tabla de la base
	 * de datos - EL ORDEN ES IMPORTANTE
	 * 
	 * @return Un arreglo con 7 números que indican el número de tuplas borradas
	 *         en las tablas GUSTAN, SIRVEN, VISITAN, BEBIDA, TIPOBEBIDA,
	 *         BEBEDOR y BAR, respectivamente
	 */
	public long[] limpiarParranderos() {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long[] resp = sqlUtil.limpiarParranderos(pm);
			tx.commit();
			log.info("Borrada la base de datos");
			return resp;
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return new long[] { -1, -1, -1, -1, -1, -1, -1 };
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}

	}

	public List<Reserva> reservasActuales(Long idInmueble) {
		PersistenceManager pm = pmf.getPersistenceManager();
		List<Reserva> resp = new ArrayList<Reserva>();
		List<Reserva> vigentes = sqlReserva.darReservasporInmuebleId(pm, idInmueble);
		Timestamp fechaHoy = new Timestamp(System.currentTimeMillis());
		for (int i = 0; i < vigentes.size(); i++) {
			if (vigentes.get(i).getFechaInicio().getTime() < fechaHoy.getTime()
					&& vigentes.get(i).getFechaFin().getTime() > fechaHoy.getTime()
					&& vigentes.get(i).getIdReserva() < 8000) {
				resp.add(vigentes.get(i));
			}
		}
		return resp;
	}

	public List<Reserva> reservasFuturas(Long idInmueble) {
		PersistenceManager pm = pmf.getPersistenceManager();
		List<Reserva> resp = new ArrayList<Reserva>();
		List<Reserva> vigentes = sqlReserva.darReservasporInmuebleId(pm, idInmueble);
		Timestamp fechaHoy = new Timestamp(System.currentTimeMillis());
		for (int i = 0; i < vigentes.size(); i++) {
			if (vigentes.get(i).getFechaInicio().getTime() > fechaHoy.getTime()
					&& vigentes.get(i).getIdReserva() < 8000) {
				resp.add(vigentes.get(i));
			}
		}
		return resp;
	}

	public List<Reserva> reservasMasivas(Long idInmueble) {
		PersistenceManager pm = pmf.getPersistenceManager();
		List<Reserva> resp = new ArrayList<Reserva>();
		List<Reserva> vigentes = sqlReserva.darReservasporInmuebleId(pm, idInmueble);
		for (int i = 0; i < vigentes.size(); i++) {
			if (vigentes.get(i).getIdReserva() >= 8000) {
				resp.add(vigentes.get(i));
			}
		}
		return resp;
	}

	public boolean disponibleRangoFechas(Timestamp fechaInicio, Timestamp fechaFinal, Long idInmueble) {
		PersistenceManager pm = pmf.getPersistenceManager();
		boolean disponible = true;
		List<Reserva> vigentes = sqlReserva.darReservasporInmuebleId(pm, idInmueble);
		for (int i = 0; i < vigentes.size(); i++) {
			Timestamp inicio = vigentes.get(i).getFechaInicio();
			Timestamp fin = vigentes.get(i).getFechaFin();
			if (inicio.getTime() > fechaInicio.getTime() && inicio.getTime() < fechaFinal.getTime()
					|| fin.getTime() > fechaInicio.getTime() && fin.getTime() < fechaFinal.getTime()) {
				disponible = false;
			}
		}
		return disponible;
	}

	public void deshabilitarAlojamiento(Long idInmubele) {

		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		List<Reserva> futuros = reservasFuturas(idInmubele);
		List<Reserva> actuales = reservasActuales(idInmubele);
		List<Reserva> masivas = reservasMasivas(idInmubele);
		cmpFechaGeneracion cmp = new cmpFechaGeneracion();
	    Collections.sort(futuros,cmp);
	    
	    try{
	    	tx.begin();
		for (int i = 0; i < actuales.size(); i++) {
			TIPO_INMUEBLE tipoInmueble = sqlInmueble.darInmueblePorId(pm, actuales.get(i).getIdInmueble())
					.getTipoInmueble();
			List<Inmueble> inmMismoTipo = sqlInmueble.darInmueblesPorTipo(pm, tipoInmueble.name());
			boolean encontrado = false;
			int numPersonas = actuales.get(i).getNumeroPersonas();
			for (int j = 0; j < inmMismoTipo.size(); j++) {
				boolean disponible = disponibleRangoFechas(actuales.get(i).getFechaInicio(),
						actuales.get(i).getFechaInicio(), inmMismoTipo.get(i).getId());
				if (inmMismoTipo.get(j).getCapacidad() >= numPersonas && !encontrado && disponible) {
					long idInmNuevo = inmMismoTipo.get(j).getId();
					encontrado = true;
					long idReserva = nextval();
					Timestamp fechaHoy = new Timestamp(System.currentTimeMillis());
					sqlReserva.adicionarReserva(pm, idReserva, idInmNuevo, actuales.get(i).getIdCliente(), fechaHoy,
							actuales.get(i).getFechaFin(), fechaHoy, null, 'N', actuales.get(i).getNumeroPersonas());
				}
			}
		}

		for (int i = 0; i < futuros.size(); i++) {
			TIPO_INMUEBLE tipoInmueble = sqlInmueble.darInmueblePorId(pm, futuros.get(i).getIdInmueble())
					.getTipoInmueble();
			List<Inmueble> inmMismoTipo = sqlInmueble.darInmueblesPorTipo(pm, tipoInmueble.name());
			boolean encontrado = false;
			int numPersonas = futuros.get(i).getNumeroPersonas();
			for (int j = 0; j < inmMismoTipo.size(); j++) {
				boolean disponible = disponibleRangoFechas(actuales.get(i).getFechaInicio(),
						actuales.get(i).getFechaInicio(), inmMismoTipo.get(i).getId());
				if (inmMismoTipo.get(j).getCapacidad() >= numPersonas && !encontrado && disponible) {
					long idInmNuevo = inmMismoTipo.get(j).getId();
					encontrado = true;
					long idReserva = nextval();
					Timestamp fechaHoy = new Timestamp(System.currentTimeMillis());
					sqlReserva.adicionarReserva(pm, idReserva, idInmNuevo, futuros.get(i).getIdCliente(),
							futuros.get(i).getFechaInicio(), futuros.get(i).getFechaFin(), fechaHoy, null, 'N',
							futuros.get(i).getNumeroPersonas());
				}
			}
		}

		int numeroPersonas = 0;
		int capacidad = 0;
		Timestamp inicio = masivas.get(0).getFechaInicio();
		Timestamp fin = masivas.get(0).getFechaFin();
		TIPO_INMUEBLE tipoInmueble = sqlInmueble.darInmueblePorId(pm, masivas.get(0).getIdInmueble()).getTipoInmueble();
		List<Inmueble> inmMismoTipo = sqlInmueble.darInmueblesPorTipo(pm, tipoInmueble.name());
		for (int i = 0; i < inmMismoTipo.size(); i++) {
			if (disponibleRangoFechas(inicio, fin, inmMismoTipo.get(i).getId()))
				capacidad = capacidad + inmMismoTipo.get(i).getCapacidad();
		}
		for (int i = 0; i < masivas.size(); i++) {
			numeroPersonas = numeroPersonas + masivas.get(i).getNumeroPersonas();
		}

		for (int i = 0; i < masivas.size(); i++) {
			if (capacidad > numeroPersonas) {
				boolean encontrado = false;
				for (int j = 0; j < inmMismoTipo.size(); j++) {
					boolean disponible = disponibleRangoFechas(inicio, fin, inmMismoTipo.get(i).getId());
					if (disponible && !encontrado) {
						long idInmNuevo = inmMismoTipo.get(j).getId();
						encontrado = true;
						long idReserva = nextval();
						Timestamp fechaHoy = new Timestamp(System.currentTimeMillis());
						sqlReserva.adicionarReserva(pm, idReserva, idInmNuevo, masivas.get(i).getIdCliente(),
								masivas.get(i).getFechaInicio(), masivas.get(i).getFechaFin(), fechaHoy, null, 'N',
								masivas.get(i).getNumeroPersonas());
					}

				}

			}
			sqlReserva.eliminarReserva(pm, masivas.get(i).getIdReserva());
		}
		tx.commit();
	    }

	catch (Exception e) {
			// e.printStackTrace();
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}

	}

}
