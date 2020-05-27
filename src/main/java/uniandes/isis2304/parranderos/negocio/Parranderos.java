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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import com.google.gson.JsonObject;
import uniandes.isis2304.parranderos.persistencia.PersistenciaAlohandes;

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
	private PersistenciaAlohandes pp;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * El constructor por defecto
	 */
	public Parranderos ()
	{
		pp = PersistenciaAlohandes.getInstance ();
	}

	/**
	 * El constructor qye recibe los nombres de las tablas en tableConfig
	 * @param tableConfig - Objeto Json con los nombres de las tablas y de la unidad de persistencia
	 */
	public Parranderos (JsonObject tableConfig)
	{
		pp = PersistenciaAlohandes.getInstance (tableConfig);
	}

	/**
	 * Cierra la conexión con la base de datos (Unidad de persistencia)
	 */
	public void cerrarUnidadPersistencia ()
	{
		pp.cerrarUnidadPersistencia ();
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
	public Reserva adicionarReserva(long idInmueble,long idCliente,Timestamp fechaInicio, Timestamp fechaFin, Timestamp fechaGeneracion,Timestamp fechaCancelacion,char cancelado ,int numeroPersonas)
	{
		log.info ("Adicionando Reservas");
		Reserva resp = pp.adicionarReserva(idInmueble, idCliente, fechaInicio, fechaFin, fechaGeneracion, fechaCancelacion, cancelado, numeroPersonas);
		log.info ("Adicionando Reserva: " + resp + "tuplas Adicionadas");

		return resp;
	}
	public List<Reserva> adicionarReservaMasivas(String tipoInmueble,long idCliente,Timestamp fechaInicio, Timestamp fechaFin, Timestamp fechaGeneracion,Timestamp fechaCancelacion,char cancelado ,int cantidad)
	{
		log.info ("Adicionando ReservasMasivas");
		List<Reserva> resp = pp.adicionarReservasMasivas(tipoInmueble, cantidad, fechaInicio, fechaFin, idCliente, fechaGeneracion, fechaCancelacion, cancelado);
		log.info ("Adicionando Reserva: " + resp + "tuplas Adicionadas");

		return resp;
	}
	/**
	 * Elimina de manera persistente el hecho que una reserva es tomada por un cliente
	 * Adiciona entradas al log de la aplicación
	 * @param idReserva - El identificador de la reserva
	 * @return El número de tuplas eliminadas
	 */
	public long eliminarReserva (long idReserva)
	{
		log.info ("Eliminando Reservas");
		long resp = pp.eliminarReserva (idReserva);
		log.info ("Eliminando Reserva: " + resp + "tuplas eliminadas");
		return resp;
	}
	public long eliminarReservaMasiva (long idCliente,Timestamp fechaInicio, Timestamp fechaFin, Timestamp fechaGeneracion)
	{
		log.info ("Eliminando ReservaMasiva");
		long resp = pp.eliminarReservasMasivas(idCliente, fechaInicio, fechaFin, fechaGeneracion);
		log.info ("Eliminando ReservaMasiva: " + resp + "tuplas eliminadas");
		return resp;
	}
	/**
	 * RCF8
	 * @param muebleId
	 * @return
	 */
	public List<Cliente> darInformacionCLientesMasFrecuentes (long muebleId)
	{
		log.info ("Listando Clientes");
		List<Cliente> clientes = pp.darInfoClientesFrecuentes(muebleId);	
		log.info ("Listando Clientes: " + clientes.size() + " clientes existentes");
		return clientes;
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

	/**
	 * Adiciona de manera persistente un apartamento
	 * Adiciona entradas al log de la aplicación
	 * @return un apartamento crado
	 */
	public Apartamento adicionarApartamento(int numeroHabitaciones, boolean amoblado,boolean serviciosIncluidos ,String nombre, String tipoInmueble,String ubicacion,int capacidad,boolean disponible,String foto,String descripcion,int veceReservada,double costoXNoche,long idOperador)
	{
		log.info ("Adicionando Apartamento");
		Apartamento resp = pp.adicionarApartamento(numeroHabitaciones,  amoblado, serviciosIncluidos , nombre,  tipoInmueble, ubicacion, capacidad, disponible, foto, descripcion, veceReservada, costoXNoche, idOperador);
		log.info ("Adicionando Apartamento: " + resp + "tuplas Adicionadas");
		return resp;
	}

	/**
	 * Elimina de manera persistente el hecho de quitar una oferta de alojamiento
	 * Adiciona entradas al log de la aplicación
	 * @param idInmueble - El identificador del Inmueble
	 * @return El número de tuplas eliminadas
	 */
	public long eliminarInmueble (long idInmueble)
	{
		log.info ("Eliminando Inmueble");
		long resp = pp.eliminarInmueble(idInmueble);
		log.info ("Eliminando Inmueble: " + resp + "tuplas eliminadas");
		return resp;
	}

	/**
	 * Encuentra todos los INMUEBLES en Parranderos
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos RESERVA con todos los GUSTAN que conoce la aplicación, llenos con su información básica
	 */
	public List<Inmueble> darInmuebles()
	{
		log.info ("Listando Inmueble");
		List<Inmueble> inmuebles = pp.darInmuebles ();
		log.info ("Listando inmuebles: " + inmuebles.size() + " Reservas existentes");
		return inmuebles;
	}
	/**
	 * RFC3
	 * @return
	 */
	public List<Double> darIndicesDeOcupacionInmuebles()
	{
		List<Double> indices = new ArrayList<Double>();
		log.info ("Calculado indice de ocupacion de los Inmuebles");
		List<Inmueble> inmuebles = pp.darInmuebles ();
		for(int i=0;i<inmuebles.size();i++)
		{
			List<Reserva> reservas = pp.darReservaPorMueble(inmuebles.get(i).getId());
			Timestamp hoy = new Timestamp(System.currentTimeMillis());
			int antiguedad= calcularIntervaloDias(reservas.get(0).getFechaInicio(), hoy);
			int ocupacion=0;
			if(reservas.size()!=0){
				for(int j=0;j<reservas.size();j++)
				{
					ocupacion+=calcularIntervaloDias(reservas.get(j).getFechaInicio(),reservas.get(j).getFechaFin());
				}
			}
			double indice= ocupacion/antiguedad;
			indices.add(indice);
			log.info ("El inmueble: " + inmuebles.get(i).getNombre() + " tiene un indice de ocupacion de:" + indice);
		}
													
		//										DIA 46/60 Matenme plis :/
		return indices;
	}
	/**
	 * Encuentra todos los RESERVA en Parranderos y los devuelve como VO
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos RESERVA con todos los RESERVA que conoce la aplicación, llenos con su información básica
	 */
	public List<VOInmueble> darVOInmueble ()
	{
		log.info ("Generando los VO de Inmueble");
		List<VOInmueble> voGustan = new LinkedList<VOInmueble> ();
		for (VOInmueble inmueble: pp.darInmuebles ())
		{
			voGustan.add (inmueble);
		}
		log.info ("Generando los VO de inmueble: " + voGustan.size () + " Inmueble existentes");
		return voGustan;
	}

	public List<VOApartamento> darVOApartamento()
	{
		log.info ("Generando los VO de Apartamento");
		List<VOApartamento> voGustan = new LinkedList<VOApartamento> ();
		for (VOApartamento inmueble: pp.darApartamentos ())
		{
			voGustan.add (inmueble);
		}
		log.info ("Generando los VO de inmueble: " + voGustan.size () + " Inmueble existentes");
		return voGustan;
	}




	/* ****************************************************************
	 * 			Métodos para manejar la relación Cxc
	 *****************************************************************/

	/**
	 * Adiciona de manera persistente el hecho que un bebedor visita un bar
	 * Adiciona entradas al log de la aplicación
	 *
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
	public List<Inmueble> darMejorYPeorInmueble()
	{
		return pp.darMejorYPeorInmueble();
	}
	
	public List<Cliente> darClientesTop1()
	{
		return pp.darClientesTop1();
	}
	
	public List<Cliente> darClientesTop2()
	{
		return pp.darClientesTop2();
	}
	
	public List<Cliente> consultarConsumo(Long idInmuble, Timestamp fecha1, Timestamp fecha2){
		return pp.consultarConsumoAlohAndes(idInmuble, fecha1, fecha2);
	}
	public List<Cliente> consultarConsumoV2(Long idInmuble, Timestamp fecha1, Timestamp fecha2){
		return pp.consultarConsumoAlohAndesV2(idInmuble, fecha1, fecha2);
	}
	
	public List<Cliente> darClientesTop3()
	{
		return pp.darClientesTop3();
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

	public VOReserva darReservaPorId(long idReserva) {
		// TODO Auto-generated method stub
		return pp.darReservaPorId(idReserva);
	}

	public VOInmueble darInmueblePorId(long idInmueble) {
		// TODO Auto-generated method stub
		return pp.darInmueblePorId(idInmueble);
	}
	public Operador adicionarOperador()
	{
		return pp.adicionarOperador("hola", "", "", 12);
	}
	public int calcularIntervaloDias(Timestamp fechaInicio, Timestamp fechafin)
	{
		int dias=0;
		int rango= (int) ( fechaInicio.getTime()-fechafin.getTime());
		dias=rango/86400000;
		return dias;

	}

	public List<VOCliente> darVOCliente ()
	{
		log.info ("Generando los VO de Cxc");
		List<VOCliente> voCxc = new LinkedList<VOCliente> ();
		for (VOCliente vis: pp.darCliente ())
		{
			voCxc.add (vis);
		}
		log.info ("Generando los VO de Cxc: " + voCxc.size () + " Clientes existentes");
		return voCxc;
	}
	
	public long deshabilitarAlojamiento(long idInmueble){
		log.info ("Deshabilitando alojamiento");
		long resp = pp.deshabilitarAlojamiento(idInmueble);
		log.info ("Reubicando Reservas: " + resp + "reservas reubicadas");
		return resp;
		
	}
	
	public long habilitarAlojamiento(long idInmueble){
		log.info ("Habilitando alojamiento");
		long resp = pp.habilitarAlojamiento(idInmueble);
		log.info ("Alojamiento habilitado: " + resp + "con id"+idInmueble);
		return resp;
	}
}
