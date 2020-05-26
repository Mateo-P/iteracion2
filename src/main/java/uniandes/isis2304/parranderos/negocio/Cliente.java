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
public class Cliente implements VOCliente
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
	 * La cedula del operador
	 */
	private int cedula;
	
	/**
	 * El vinculo del operador (ESTUDIANTE, PROFESOR, PROFESOR_INVITADO,EGRESADO,FAMILIA_ESTUDIANTE,REGISTRADO_EVENTO,EMPRESA )
	 */
	private int habilitado;
	
	/**
	 * El dinero recuadado por el operador
	 */
	private String tipoVinculo;
	
	/**
	 * El correo del operador
	 */
	private String correo;
	
	/**
	 * El telefono del operador
	 */
	private int telefono;


	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
    /**
     * Constructor por defecto
     */
	public Cliente() 
    {
    	this.id = 0;
		this.nombre = "";
		this.setTipoVinculo("");
		this.setHabilitado(0);
		this.setCedula(0);
		this.setCorreo("");
		this.setTelefono(0);
		
	}

	/**
	 * Constructor con valores
	 * @param id - El id del operaedor
	 * @param nombre - El nombre del operador
	 * @param ciudad - La ciudad del operador
	 * @param presupuesto - El presupuesto del bar (ALTO, MEDIO, BAJO)
	 * @param cantSedes - Las sedes del bar (Mayor que 0)
	 */
    public Cliente(long id, String nombre,String tipoVinculo, int habilitado, String correo, int telefono,int cedula) 
    {
    	this.id = id;
		this.nombre = nombre;
		this.cedula=cedula;
		this.habilitado=habilitado;
		this.setTelefono(telefono);
		this.setCorreo(correo);
		this.setTipoVinculo(tipoVinculo);
	
	
	

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
	
	
	
	
	
	
	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del bar
	 */
	public String toString() 
	{
		return "Cliente [id=" + id + ", nombre=" + nombre + ", telefono=" + telefono + ", correo=" + correo
				+ ", tipo vinculo=" + tipoVinculo + "]";
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

	public int getCedula() {
		return cedula;
	}

	public void setCedula(int cedula) {
		this.cedula = cedula;
	}

	public int getHabilitado() {
		return habilitado;
	}

	public void setHabilitado(int habilitado) {
		this.habilitado = habilitado;
	}
	

}
