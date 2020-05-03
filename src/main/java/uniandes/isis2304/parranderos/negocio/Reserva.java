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
import java.util.Comparator;
/**
 * Clase para modelar la relación SIRVEN del negocio de los Parranderos:
 * Cada objeto de esta clase representa el hecho que un bar sirve una bebida y viceversa.
 * Se modela mediante los identificadores del bar y de la bebida respectivamente
 * Debe existir un bar con el identificador dado
 * Debe existir una bebida con el identificador dado 
 * Adicionalmente contiene el horario (DIURNO, NOCTURNO, TODOS) en el cual el bar sirve la bebida
 * 
 * @author Germán Bravo
 */
public class Reserva implements VOReserva
{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El identificador del bar que sirve la bebida
	 */
	private long idReserva;
	private char cancelado;
	private long idCliente;
	/**
	 * El identificador de la bebida que es servida en el bar
	 */
	private long idInmueble;
	
	/**
	 * El horario en que sirve la bebida en el bar (DIURNO, NOCTURNO, TODOS)
	 */
	private Timestamp fechaInicio;
	
	/**
	 * El horario en que sirve la bebida en el bar (DIURNO, NOCTURNO, TODOS)
	 */
	private Timestamp fechaFin;
	
	/**
	 * El horario en que sirve la bebida en el bar (DIURNO, NOCTURNO, TODOS)
	 */
	private Timestamp fechaGeneracion;
	private Timestamp fechaCancelacion;
	private int numeroPersonas;
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * Constructor por defecto
	 */
	public Reserva () 
	{
		this.idReserva = 0;
		this.idInmueble = 0;
		this.setIdCliente(0);
		this.setCancelado('N');
		this.fechaInicio = new Timestamp (0);
		this.fechaFin = new Timestamp (0);
		this.fechaGeneracion = new Timestamp (0);
		this.setFechaCancelacion(new Timestamp(0));
		this.numeroPersonas=0;
	}

	/**
	 * Constructor con valores
	 * @param idBar - El identificador del bar. Debe exixtir un bar con dicho identificador
	 * @param idBebida - El identificador de la bebida. Debe existir una bebida con dicho identificador
	 * @param horario - El horario en el que el bar sirve la bebida (DIURNO, NOCTURNO, TODOS)
	 */
	public Reserva (long idReserva, long idInmueble,long idCliente,Timestamp fechaInicio, Timestamp fechaFin, Timestamp fechaGeneracion,Timestamp fechaCancelacion,char cancelado ,int numeroPersonas) 
	{
		this.idReserva = idReserva;
		this.idInmueble = idInmueble;
		this.idCliente=idCliente;
		this.numeroPersonas = numeroPersonas;
		this.fechaInicio=fechaInicio;
		this.fechaFin=fechaFin;
		this.fechaGeneracion=fechaGeneracion;
		this.fechaCancelacion=fechaCancelacion;
		this.cancelado=cancelado;
	}

	@Override
	public String toString() 
	{
		return "Reserva [idBar=" + idReserva + ", idBebida=" + idInmueble + ", fecha inicio=" + fechaInicio + "]";
	}

	public long getIdReserva() {
		// TODO Auto-generated method stub
		return idReserva;
	}
	
	public void setIdReserva(long idReserva)
	{
		this.idReserva=idReserva;
	}
	
	public long getIdInmueble() {
		// TODO Auto-generated method stub
		return idInmueble;
	}
	
	public void setIdInmueble(long idInmueble)
	{
		this.idInmueble=idInmueble;
	}
	
	public Timestamp getFechaInicio() {
		// TODO Auto-generated method stub
		return fechaInicio;
	}

	public void setFechaInicio(Timestamp fechaInicio)
	{
		this.fechaInicio=fechaInicio;
	}
	
	public Timestamp getFechaFin() {
		// TODO Auto-generated method stub
		return fechaFin;
	}
	
	public void setFechaFin(Timestamp fechaFin)
	{
		this.fechaFin=fechaFin;
	}
	
	public Timestamp getFechaGeneracion() {
		// TODO Auto-generated method stub
		return fechaGeneracion;
	}

	public void setFechaGeneracion(Timestamp fechaGeneracion)
	{
		this.fechaGeneracion=fechaGeneracion;
	}
	
	public int getNumeroPersonas() {
		// TODO Auto-generated method stub
		return numeroPersonas;
	}
	
	public void setFechaNumeroPersonas(int numeroPersonas)
	{
		this.numeroPersonas=numeroPersonas;
	}

	public char getCancelado() {
		return cancelado;
	}

	public void setCancelado(char cancelado) {
		this.cancelado = cancelado;
	}

	public long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(long idCliente) {
		this.idCliente = idCliente;
	}

	public Timestamp getFechaCancelacion() {
		return fechaCancelacion;
	}

	public void setFechaCancelacion(Timestamp fechaCancelacion) {
		this.fechaCancelacion = fechaCancelacion;
	}
	
	public static class cmpFechaGeneracion implements Comparator<Reserva>{
		/**
		 * metodo de comparacion de los Viajes de Uber por la zona de destino
		 * @return 0 si dstid = dstid de com, 1 si dstid > dstid de com, -1 si dstid < dstid de com
		 */		
			public int compare(Reserva o1, Reserva o2) {
				if(o1.fechaGeneracion.getTime()-o2.fechaGeneracion.getTime()<0){
					return -1;
				}else if(o1.fechaGeneracion.getTime()-o2.fechaGeneracion.getTime()<0){
					return 1;
				}
				else return 0;
			}
		}

	
	
}
