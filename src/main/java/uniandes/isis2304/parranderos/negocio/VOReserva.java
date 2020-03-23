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

/**
 * Interfaz para los métodos get de SIRVEN.
 * Sirve para proteger la información del negocio de posibles manipulaciones desde la interfaz 
 * 
 * @author Germán Bravo
 */
public interface VOReserva
{
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * @return El idBar
	 */
	public long getIdReserva();

	/**
	 * @return El idBebida
	 */
	public long getIdInmueble();
	public long getIdCliente();
	/**
	 * @return El horario en que el bar sirve la bebida
	 */
	public Timestamp getFechaInicio();

	/**
	 * @return El horario en que el bar sirve la bebida
	 */
	public Timestamp getFechaFin();
	
	/**
	 * @return El horario en que el bar sirve la bebida
	 */
	public Timestamp getFechaGeneracion();
	public Timestamp getFechaCancelacion();
	
	/**
	 * @return El horario en que el bar sirve la bebida
	 */
	public int getNumeroPersonas();
	
	
	/** 
	 * @return Una cadena con la información básica
	 */
	@Override
	public String toString();

}
