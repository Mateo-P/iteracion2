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

/**
 * Clase para modelar el concepto OPERADOR del negocio de Alohandes
 *
 * @author Mateo Perez
 */
public class Operador implements VOOperador
{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El identificador ÚNICO de los bares
	 */
	private long id;
	
	/**
	 * El nombre del operador
	 */
	private String nombre;

	/**
	 * La horarios del operador
	 */
	private String horaApertura, horaCierre;
	
	/**
	 * El vinculo del operador (ESTUDIANTE, PROFESOR, PROFESOR_INVITADO,EGRESADO,FAMILIA_ESTUDIANTE,REGISTRADO_EVENTO,EMPRESA )
	 */
	private String tipoVinculo;
	
	/**
	 * El dinero recuadado por el operador
	 */
	private double dineroRecaudado;
	
	/**
	 * El correo del operador
	 */
	private String correo;
	
	/**
	 * El telefono del operador
	 */
	private int telefono;
	
	/**
	 * El número de sedes del bar en la ciudad
	 */
	private String tipoOperador;

	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
    /**
     * Constructor por defecto
     */
	public Operador() 
    {
    	this.id = 0;
		this.nombre = "";
		this.setTipoVinculo("");
		this.horaApertura="";
		this.horaCierre="";
		this.setDineroRecaudado(0);
		this.setCorreo("");
		this.setTelefono(0);
		this.setTipoOperador("");
	}

	/**
	 * Constructor con valores
	 * @param id - El id del operador
	 * @param nombre - El nombre del operador
	 * @param ciudad - La ciudad del operador
	 * @param presupuesto - El presupuesto del bar (ALTO, MEDIO, BAJO)
	 * @param cantSedes - Las sedes del bar (Mayor que 0)
	 */
    public Operador(long id, String nombre,String tipoVinculo, String horaApertura, String horaCierre, double dinero, String correo, int telefono,String tipoOperador) 
    {
    	this.id = id;
		this.nombre = nombre;
		this.setTipoVinculo(tipoVinculo);
		this.horaApertura = horaApertura;
		this.horaCierre = horaCierre;
		this.setDineroRecaudado(dinero);
		this.setCorreo(correo);
		this.setTelefono(telefono);
		this.setTipoOperador(tipoOperador);
	}

    /**
	 * @return El id del operador
	 */
	public long getId() 
	{
		return id;
	}
	
	/**
	 * @param id - El nuevo id del bar
	 */
	public void setId(long id) 
	{
		this.id = id;
	}
	
	/**
	 * @return el nombre del bar
	 */
	public String getNombre() 
	{
		return nombre;
	}
	
	/**
	 * @param nombre El nuevo nombre del bar
	 */
	public void setNombre(String nombre) 
	{
		this.nombre = nombre;
	}
	
	/**
	 * @return la ciudad del bar
	 */
	public String getHoraApertura() 
	{
		return horaApertura;
	}
	
	/**
	 * @param ciudad - La nueva ciudad del bar
	 */
	public void setHoraApertura(String horaApertura) 
	{
		this.horaApertura = horaApertura;
	}
	
	/**
	 * @return la ciudad del bar
	 */
	public String getHoraCierre() 
	{
		return horaCierre;
	}
	
	/**
	 * @param ciudad - La nueva ciudad del bar
	 */
	public void setHoraCierre(String horaCierre) 
	{
		this.horaCierre = horaCierre;
	}
	
	
	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del bar
	 */
	public String toString() 
	{
		return "Operador [id=" + id + ", nombre=" + nombre + ", telefono=" + telefono + ", correo=" + correo
				+ ", tipo Operador=" + tipoOperador + "]";
	}

	

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public int getTelefono() {
		return telefono;
	}

	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}

	public String getTipoVinculo() {
		return tipoVinculo;
	}

	public void setTipoVinculo(String tipoVinculo) {
		this.tipoVinculo = tipoVinculo;
	}

	public double getDineroRecaudado() {
		return dineroRecaudado;
	}

	public void setDineroRecaudado(double dineroRecaudado) {
		this.dineroRecaudado = dineroRecaudado;
	}

	public String getTipoOperador() {
		return tipoOperador;
	}

	public void setTipoOperador(String tipoOperador) {
		this.tipoOperador = tipoOperador;
	}
	

}
