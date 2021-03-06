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

package uniandes.isis2304.parranderos.interfazApp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDODataStoreException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import uniandes.isis2304.parranderos.negocio.Cliente;
import uniandes.isis2304.parranderos.negocio.Inmueble;
import uniandes.isis2304.parranderos.negocio.Parranderos;
import uniandes.isis2304.parranderos.negocio.Reserva;
import uniandes.isis2304.parranderos.negocio.VOApartamento;
import uniandes.isis2304.parranderos.negocio.VOCliente;
import uniandes.isis2304.parranderos.negocio.VOInmueble;
import uniandes.isis2304.parranderos.negocio.VOReserva;


/**
 * Clase principal de la interfaz
 * @author Germán Bravo
 */
@SuppressWarnings("serial")

public class InterfazAlohandesApp extends JFrame implements ActionListener
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(InterfazAlohandesApp.class.getName());
	
	/**
	 * Ruta al archivo de configuración de la interfaz
	 */
	private static final String CONFIG_INTERFAZ = "./src/main/resources/config/interfaceConfigApp.json"; 
	
	/**
	 * Ruta al archivo de configuración de los nombres de tablas de la base de datos
	 */
	private static final String CONFIG_TABLAS = "./src/main/resources/config/TablasBD_A.json"; 
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
    /**
     * Objeto JSON con los nombres de las tablas de la base de datos que se quieren utilizar
     */
    private JsonObject tableConfig;
    
    /**
     * Asociación a la clase principal del negocio.
     */
    private Parranderos parranderos;
    
	/* ****************************************************************
	 * 			Atributos de interfaz
	 *****************************************************************/
    /**
     * Objeto JSON con la configuración de interfaz de la app.
     */
    private JsonObject guiConfig;
    
    /**
     * Panel de despliegue de interacción para los requerimientos
     */
    private PanelDatos panelDatos;
    
    /**
     * Menú de la aplicación
     */
    private JMenuBar menuBar;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
    /**
     * Construye la ventana principal de la aplicación. <br>
     * <b>post:</b> Todos los componentes de la interfaz fueron inicializados.
     */
    public InterfazAlohandesApp( )
    {
        // Carga la configuración de la interfaz desde un archivo JSON
        guiConfig = openConfig ("Interfaz", CONFIG_INTERFAZ);
        
        // Configura la apariencia del frame que contiene la interfaz gráfica
        configurarFrame ( );
        if (guiConfig != null) 	   
        {
     	   crearMenu( guiConfig.getAsJsonArray("menuBar") );
        }
        
        tableConfig = openConfig ("Tablas BD", CONFIG_TABLAS);
        parranderos = new Parranderos (tableConfig);
        
    	String path = guiConfig.get("bannerPath").getAsString();
        panelDatos = new PanelDatos ( );

        setLayout (new BorderLayout());
        add (new JLabel (new ImageIcon (path)), BorderLayout.NORTH );          
        add( panelDatos, BorderLayout.CENTER );        
    }
    
	/* ****************************************************************
	 * 			Métodos de configuración de la interfaz
	 *****************************************************************/
    /**
     * Lee datos de configuración para la aplicació, a partir de un archivo JSON o con valores por defecto si hay errores.
     * @param tipo - El tipo de configuración deseada
     * @param archConfig - Archivo Json que contiene la configuración
     * @return Un objeto JSON con la configuración del tipo especificado
     * 			NULL si hay un error en el archivo.
     */
    private JsonObject openConfig (String tipo, String archConfig)
    {
    	JsonObject config = null;
		try 
		{
			Gson gson = new Gson( );
			FileReader file = new FileReader (archConfig);
			JsonReader reader = new JsonReader ( file );
			config = gson.fromJson(reader, JsonObject.class);
			log.info ("Se encontró un archivo de configuración válido: " + tipo);
		} 
		catch (Exception e)
		{
//			e.printStackTrace ();
			log.info ("NO se encontró un archivo de configuración válido");			
			JOptionPane.showMessageDialog(null, "No se encontró un archivo de configuración de interfaz válido: " + tipo, "Parranderos App", JOptionPane.ERROR_MESSAGE);
		}	
        return config;
    }
    
    /**
     * Método para configurar el frame principal de la aplicación
     */
    private void configurarFrame(  )
    {
    	int alto = 0;
    	int ancho = 0;
    	String titulo = "";	
    	
    	if ( guiConfig == null )
    	{
    		log.info ( "Se aplica configuración por defecto" );			
			titulo = "Parranderos APP Default";
			alto = 300;
			ancho = 500;
    	}
    	else
    	{
			log.info ( "Se aplica configuración indicada en el archivo de configuración" );
    		titulo = guiConfig.get("title").getAsString();
			alto= guiConfig.get("frameH").getAsInt();
			ancho = guiConfig.get("frameW").getAsInt();
    	}
    	
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setLocation (50,50);
        setResizable( true );
        setBackground( Color.WHITE );

        setTitle( titulo );
		setSize ( ancho, alto);        
    }

    /**
     * Método para crear el menú de la aplicación con base em el objeto JSON leído
     * Genera una barra de menú y los menús con sus respectivas opciones
     * @param jsonMenu - Arreglo Json con los menùs deseados
     */
    private void crearMenu(  JsonArray jsonMenu )
    {    	
    	// Creación de la barra de menús
        menuBar = new JMenuBar();       
        for (JsonElement men : jsonMenu)
        {
        	// Creación de cada uno de los menús
        	JsonObject jom = men.getAsJsonObject(); 

        	String menuTitle = jom.get("menuTitle").getAsString();        	
        	JsonArray opciones = jom.getAsJsonArray("options");
        	
        	JMenu menu = new JMenu( menuTitle);
        	
        	for (JsonElement op : opciones)
        	{       	
        		// Creación de cada una de las opciones del menú
        		JsonObject jo = op.getAsJsonObject(); 
        		String lb =   jo.get("label").getAsString();
        		String event = jo.get("event").getAsString();
        		
        		JMenuItem mItem = new JMenuItem( lb );
        		mItem.addActionListener( this );
        		mItem.setActionCommand(event);
        		
        		menu.add(mItem);
        	}       
        	menuBar.add( menu );
        }        
        setJMenuBar ( menuBar );	
    }
    
	/* ****************************************************************
	 * 			CRUD de Reserva
	 *****************************************************************/
    /**
     * Adiciona un tipo de bebida con la información dada por el usuario
     * Se crea una nueva tupla de tipoBebida en la base de datos, si un tipo de bebida con ese nombre no existía
     */
    public void crearReserva( )
    {
    	try 
    	{
    		String info = JOptionPane.showInputDialog (this, "Ingrese los datos de la reserva?", "Crear Reserva", JOptionPane.QUESTION_MESSAGE);
    		if (info != null)
    		{
    			 System.out.println("entra a reserva");
    			Timestamp fechaGeneracion = new Timestamp(System.currentTimeMillis());
    			Timestamp fechaInicio = new Timestamp(System.currentTimeMillis()+10000000);
    			Timestamp fechaFin = new Timestamp(System.currentTimeMillis()+10000000+2000000);
        		VOReserva Reserva = parranderos.adicionarReserva(1, 1007863890, fechaInicio, fechaFin, fechaGeneracion, null, 'N', 4);
        		if (Reserva == null)
        		{
        			throw new Exception ("No se pudo crear la Reserva ");
        		}
        		String resultado = "En crearReserva\n\n";
        		resultado += " reserva creada exitosamente: " + Reserva;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    public void crearReservaMasiva( )
    {
    	try 
    	{
    		String info = JOptionPane.showInputDialog (this, "Ingrese los datos de la reserva 1ro el tipo de alojamiento, 2do la cantidad demandada \n Ej: APARTAMENTO, 50 ?", "Crear Reserva Masiva", JOptionPane.QUESTION_MESSAGE);
    		if (info != null)
    		{
    			 System.out.println("entra a reserva Masiva");
    			Timestamp fechaGeneracion = new Timestamp(System.currentTimeMillis());
    			Timestamp fechaInicio = new Timestamp(System.currentTimeMillis()+10000000);
    			Timestamp fechaFin = new Timestamp(System.currentTimeMillis()+10000000+2000000);
        		List<Reserva> Reserva = parranderos.adicionarReservaMasivas(info.split(",")[0], 1007863890, fechaInicio, fechaFin, fechaGeneracion, null,'N',Integer.parseInt(info.split(",")[1]));
        		if (Reserva.size() == 0)
        		{
        			throw new Exception ("No se pudo crear la Reserva Masiva ");
        		}
        		String resultado = "En crearReserva\n\n";
        		resultado += " reserva Masiva creada exitosamente: " + Reserva;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    public void eliminarReservaMasiva( )
    {
    	try 
    	{
    		String info = JOptionPane.showInputDialog (this, "Ingrese los siguientes datos de la reserva Masiva\n"
    																						+ "Fecha inicio,\n"
    																						+ "Fecha fin,\n"
    																						+ "Fecha de generacion,\n"
    																						+ "cedula"
    																						+ " Ej: DATE'2015-12-17',\n"
    																						+ " DATE'2016-1-17',\n"
    																						+ "DATE'2019-12-17',\n"
    																						+ "1007863890 ", JOptionPane.QUESTION_MESSAGE);
    		if (info != null)
    		{
    			 System.out.println("entra a reserva Masiva");
    			Timestamp fechaGeneracion = new Timestamp(System.currentTimeMillis());
    			Timestamp fechaInicio = new Timestamp(System.currentTimeMillis()+10000000);
    			Timestamp fechaFin = new Timestamp(System.currentTimeMillis()+10000000+2000000);
        		long Reserva = parranderos.eliminarReservaMasiva(1007863890, fechaInicio, fechaFin, fechaGeneracion);
        		if (Reserva == 0)
        		{
        			throw new Exception ("No se pudo eliminar la Reserva Masiva ");
        		}
        		String resultado = "En eliminarReserva\n\n";
        		resultado += " reserva Masiva Eliminada exitosamente: " + Reserva;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    public void adicionarOperador()
    {
    	try{
    		parranderos.adicionarOperador();
    		
    	}
    	catch(Exception e)
    	{
    		String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
    	}
    }
    
	public void deshabilitarAlojamiento(){
    	try{
    		
    		String idInmueble = JOptionPane.showInputDialog (this, "Id del Inmueble ?", "Deshabilitar Inmueble por Id", JOptionPane.QUESTION_MESSAGE);
    		long id = Integer.parseInt(idInmueble);
    		long resp = parranderos.deshabilitarAlojamiento(id);
    		if(resp !=0){
    		String resultado = "En deshabilitar Alojamiento\n\n";
    		resultado += " reservas reubicadas exitosamente: " + resp;
			resultado += "\n Operación terminada";
			panelDatos.actualizarInterfaz(resultado);
    		}
    		else{
    			panelDatos.actualizarInterfaz("Hubo algun erro en la operación");
    		}
    		
    	}
    	catch(Exception e)
    	{
    		String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
    	}
    }
    
    public void habilitarInmueble(){
    	try{
    		String idInmueble = JOptionPane.showInputDialog (this, "Id del Inmueble ?", "Deshabilitar Inmueble por Id", JOptionPane.QUESTION_MESSAGE);
    		long id = Integer.parseInt(idInmueble);
    		long resp = parranderos.habilitarAlojamiento(id);
    		if(resp ==1){
    		String resultado = "En habilitar Alojamiento\n\n";
    		resultado += " Alojamiento habilitado: " + resp;
			resultado += "\n Operación terminada";
			panelDatos.actualizarInterfaz(resultado);
			
    		}else{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
    	}
    	catch(Exception e)
    	{
    		String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
    	}
	}
    
    
    
    /**
     * Consulta en la base de datos los tipos de bebida existentes y los muestra en el panel de datos de la aplicación
     */
    public void listarReservas( )
    {
    	try 
    	{
			List <VOReserva> lista = parranderos.darVOReserva();

			String resultado = "En listarTipoBebida";
			resultado +=  "\n" + listarReservas (lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    public void listarMejorYPeorInmueble( )
    {
    	try 
    	{
			List<Inmueble> lista = (List<Inmueble>) parranderos.darMejorYPeorInmueble();

			String resultado = "En listarInbmuelbes";
			resultado +=  "\n" + listarMejorYPeorInmueble(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    public void listarTopClientes( )
    {
    	try 
    	{
			List<Cliente> lista = (List<Cliente>) parranderos.darClientesTop1();
			List<Cliente> lista2 = (List<Cliente>) parranderos.darClientesTop2();
			List<Cliente> lista3= (List<Cliente>) parranderos.darClientesTop3();
			
			String resultado = "En listarClientes";
			resultado +=  "\n" + listarClientes(lista,1);
			resultado +=  "\n" + listarClientes(lista2,2);
			resultado +=  "\n" + listarClientes(lista3,3);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    public void verConsumoAlohAndes(){
    	try{
    		
    		String nombre = JOptionPane.showInputDialog (this, "Ingrese el id del inmuble y el rango de fechas separados por coma", "Crear un apartamento", JOptionPane.QUESTION_MESSAGE);
    		String[] respuestas = nombre.split(",");
    		Long idInmueble = (long) Integer.parseInt(respuestas[0]);
    		String inDate = respuestas[1];
    		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		Timestamp fecha1 = new Timestamp(((java.util.Date)df.parse(inDate)).getTime());
    		String inDate2 = respuestas[2];
    		Timestamp fecha2 = new Timestamp(((java.util.Date)df.parse(inDate2)).getTime());
    		
    		List<Cliente> resp = parranderos.consultarConsumo(idInmueble, fecha1, fecha2);
    		String resultado = "Consultar Consumo Aloh Andes\n\n";
    		for(int i=0;i>resp.size();i++){
    			resultado +=  "\n"+resp.get(i).toString();
    		}
    		
    		panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
    		
    		
    		
    	}catch (Exception e){
    		
    		String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
    		
    	}
    }
    
    public void verConsumoAlohAndesV2(){
    	try{
    		
    		String nombre = JOptionPane.showInputDialog (this, "Ingrese el id del inmuble y el rango de fechas separados por coma", "Crear un apartamento", JOptionPane.QUESTION_MESSAGE);
    		String[] respuestas = nombre.split(",");
    		Long idInmueble = (long) Integer.parseInt(respuestas[0]);
    		String inDate = respuestas[1];
    		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		Timestamp fecha1 = new Timestamp(((java.util.Date)df.parse(inDate)).getTime());
    		String inDate2 = respuestas[2];
    		Timestamp fecha2 = new Timestamp(((java.util.Date)df.parse(inDate2)).getTime());
    		
    		List<Cliente> resp = parranderos.consultarConsumoV2(idInmueble, fecha1, fecha2);
    		String resultado = "Consultar Consumo Aloh Andes\n\n";
    		for(int i=0;i>resp.size();i++){
    			resultado +=  "\n"+resp.get(i).toString();
    		}
    		
    		panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
    		
    		
    		
    	}catch (Exception e){
    		
    		String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
    		
    	}
    }
    	
    	    
    
	/* ****************************************************************
	 * 			CRUD de Apartamento
	 *****************************************************************/
	/**
	 * Adiciona un Apartamento con la información dada por el usuario
	 * Se crea una nueva tupla de Apartamento en la base de datos, si un tipo de bebida con ese nombre no existía
	 */
	public void adicionarApartamento( )
	{
		try
		{
			String nombre = JOptionPane.showInputDialog (this, "Ingrese los datos del apartamento?", "Crear un apartamento", JOptionPane.QUESTION_MESSAGE);
			if (nombre != null)
			{

				VOApartamento tb = parranderos.adicionarApartamento (2,true,true,nombre,"APARTAMENTO","CRA 56# 34-12",3,true,"https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcR9Gt48xG4GqeNHLoYyt3iquEw6rI3wTFuJMUQa6E115-190EzG","2 alcobas dos bañños",0,200,0);
				if (tb == null)
				{
					throw new Exception ("No se pudo crear un tipo de bebida con nombre: " + nombre);
				}
				String resultado = "En adicionarApartamento\n\n";
				resultado += "Apartamento adicionado exitosamente: " + tb;
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	/**
	 * Consulta en la base de datos los tipos de bebida existentes y los muestra en el panel de datos de la aplicación
	 */
	public void listarInmuebles( )
	{
		try
		{
			List <VOInmueble> lista = parranderos.darVOInmueble();

			String resultado = "En listarInmuebles";
			resultado +=  "\n" + listarInmuebles(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	
	public void listarClientes( )
	{
		try
		{
			List <VOCliente> lista = parranderos.darVOCliente();

			String resultado = "En listarInmuebles";
			resultado +=  "\n" + listarClientes(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
/**
 * RFC3
 */
	public void listarIndices( )
	{
		try
		{
			List <VOInmueble> lista = parranderos.darVOInmueble();

			String resultado = "En listarIndices";
			ArrayList<Double> indices= (ArrayList<Double>) parranderos.darIndicesDeOcupacionInmuebles();
			resultado +=  "\n" + "[";
			for(int i=0;i<indices.size();i++)
			{
				resultado+="El inmueble: "+lista.get(i).getNombre()+" tiene un indice de ocupacion de: "+ indices.get(i);
			}
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
    /**
     * Borra de la base de datos el tipo de bebida con el identificador dado po el usuario
     * Cuando dicho tipo de bebida no existe, se indica que se borraron 0 registros de la base de datos
     */
    public void eliminarReservaPorId( )
    {
    	try 
    	{
    		String idTipoStr = JOptionPane.showInputDialog (this, "Id de la reserva?", "Borrar Reserva por Id", JOptionPane.QUESTION_MESSAGE);
    		if (idTipoStr != null)
    		{
    			long idTipo = Long.valueOf (idTipoStr);
    			long tbEliminados = parranderos.eliminarReserva(idTipo);

    			String resultado = "En eliminar reserva\n\n";
    			resultado += tbEliminados + " Reservas eliminadas\n";
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }



	/**
     * Busca la reserva con el id indicado por el usuario y lo muestra en el panel de datos
     */
    public void buscarReservaPorId( )
    {
    	try 
    	{
    		String idReserva = JOptionPane.showInputDialog (this, "Id de la reserva?", "Buscar Reserva por Id", JOptionPane.QUESTION_MESSAGE);
    		if (idReserva != null)
    		{
    			long idParseado = Long.valueOf (idReserva);
    			VOReserva reserva = parranderos.darReservaPorId(idParseado);
    			String resultado = "En buscar Reserva por Id\n\n";
    			if (reserva != null)
    			{
        			resultado += "La reserva es del id: " + reserva.getIdReserva();
    			}
    			else
    			{
        			resultado += "Una reserva con id: " + idParseado + " NO EXISTE\n";    				
    			}
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    /**
     *RCF8 consultar informacion de clientes mas frecuentes
     */
    public void BuscarClientesMasFrecuentesPorIdInmueble( )
    {
    	try 
    	{
    		String idInmueble = JOptionPane.showInputDialog (this, "Id del Inmueble?", "Buscar Clientes mas frecuentes por Id de un Inmueble", JOptionPane.QUESTION_MESSAGE);
    		if (idInmueble != null)
    		{
    			long idParseado = Long.valueOf (idInmueble);
    			List<Cliente> cliente = parranderos.darInformacionCLientesMasFrecuentes(idParseado);
    			String resultado = "En listar Cliente por mas frecunetes \n\n";
    			if (cliente.size()!=0)
    			{
        			resultado += "La Clientes mas frecuentes del in mueble"+ idInmueble+ " son ";
        			for (int i = 0; i < cliente.size(); i++) {
						resultado+=cliente.get(i).toString()+"\n";
					}
    			}
    			else
    			{
        			resultado += "Una inmuelbe con id: " + idParseado + " NO EXISTE\n";    				
    			}
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    /**
			* Borra de la base de datos el inmueble con el identificador dado po el usuario
	 * Cuando dicho tipo de bebida no existe, se indica que se borraron 0 registros de la base de datos
	 */
	public void eliminarInmueblePorId( )
	{
		try
		{
			String idTipoStr = JOptionPane.showInputDialog (this, "Id del Inmueble ?", "Borrar Inmueble por Id", JOptionPane.QUESTION_MESSAGE);
			if (idTipoStr != null)
			{
				long idTipo = Long.valueOf (idTipoStr);
				long tbEliminados = parranderos.eliminarInmueble(idTipo);

				String resultado = "En eliminar inmueble\n\n";
				resultado += tbEliminados + " inmuebles eliminados\n";
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	/**
	 * Busca el inmubele con el id indicado por el usuario y lo muestra en el panel de datos
	 */
	public void buscarInmueblePorId( )
	{
		try
		{
			String idInmueble= JOptionPane.showInputDialog (this, "Id del inmueble?", "Buscar Inmueble por Id", JOptionPane.QUESTION_MESSAGE);
			if (idInmueble != null)
			{
				long idParseado = Long.valueOf (idInmueble);
				VOInmueble inmueble = parranderos.darInmueblePorId(idParseado);
				String resultado = "En buscar Reserva por Id\n\n";
				if (inmueble != null)
				{
					resultado += "El inmueble del id: " + inmueble.getId();
				}
				else
				{
					resultado += "Un inmueble  con id: " + idParseado + " NO EXISTE\n";
				}
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}




	/* ****************************************************************
	 * 			Métodos administrativos
	 *****************************************************************/
	/**
	 * Muestra el log de Parranderos
	 */
	public void mostrarLogParranderos ()
	{
		mostrarArchivo ("parranderos.log");
	}
	
	/**
	 * Muestra el log de datanucleus
	 */
	public void mostrarLogDatanuecleus ()
	{
		mostrarArchivo ("datanucleus.log");
	}
	
	/**
	 * Limpia el contenido del log de parranderos
	 * Muestra en el panel de datos la traza de la ejecución
	 */
	public void limpiarLogParranderos ()
	{
		// Ejecución de la operación y recolección de los resultados
		boolean resp = limpiarArchivo ("parranderos.log");

		// Generación de la cadena de caracteres con la traza de la ejecución de la demo
		String resultado = "\n\n************ Limpiando el log de parranderos ************ \n";
		resultado += "Archivo " + (resp ? "limpiado exitosamente" : "NO PUDO ser limpiado !!");
		resultado += "\nLimpieza terminada";

		panelDatos.actualizarInterfaz(resultado);
	}
	
	/**
	 * Limpia el contenido del log de datanucleus
	 * Muestra en el panel de datos la traza de la ejecución
	 */
	public void limpiarLogDatanucleus ()
	{
		// Ejecución de la operación y recolección de los resultados
		boolean resp = limpiarArchivo ("datanucleus.log");

		// Generación de la cadena de caracteres con la traza de la ejecución de la demo
		String resultado = "\n\n************ Limpiando el log de datanucleus ************ \n";
		resultado += "Archivo " + (resp ? "limpiado exitosamente" : "NO PUDO ser limpiado !!");
		resultado += "\nLimpieza terminada";

		panelDatos.actualizarInterfaz(resultado);
	}
	
	/**
	 * Limpia todas las tuplas de todas las tablas de la base de datos de parranderos
	 * Muestra en el panel de datos el número de tuplas eliminadas de cada tabla
	 */
	public void limpiarBD ()
	{
		try 
		{
    		// Ejecución de la demo y recolección de los resultados
			long eliminados [] = parranderos.limpiarParranderos();
			
			// Generación de la cadena de caracteres con la traza de la ejecución de la demo
			String resultado = "\n\n************ Limpiando la base de datos ************ \n";
			resultado += eliminados [0] + " Gustan eliminados\n";
			resultado += eliminados [1] + " Sirven eliminados\n";
			resultado += eliminados [2] + " Visitan eliminados\n";
			resultado += eliminados [3] + " Bebidas eliminadas\n";
			resultado += eliminados [4] + " Tipos de bebida eliminados\n";
			resultado += eliminados [5] + " Bebedores eliminados\n";
			resultado += eliminados [6] + " Bares eliminados\n";
			resultado += "\nLimpieza terminada";
   
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	
	/**
	 * Muestra la presentación general del proyecto
	 */
	public void mostrarPresentacionGeneral ()
	{
		mostrarArchivo ("data/00-ST-ParranderosJDO.pdf");
	}
	
	/**
	 * Muestra el modelo conceptual de Parranderos
	 */
	public void mostrarModeloConceptual ()
	{
		mostrarArchivo ("data/Modelo Conceptual Parranderos.pdf");
	}
	
	/**
	 * Muestra el esquema de la base de datos de Parranderos
	 */
	public void mostrarEsquemaBD ()
	{
		mostrarArchivo ("data/Esquema BD Parranderos.pdf");
	}
	
	/**
	 * Muestra el script de creación de la base de datos
	 */
	public void mostrarScriptBD ()
	{
		mostrarArchivo ("data/EsquemaParranderos.sql");
	}
	
	/**
	 * Muestra la arquitectura de referencia para Parranderos
	 */
	public void mostrarArqRef ()
	{
		mostrarArchivo ("data/ArquitecturaReferencia.pdf");
	}
	
	/**
	 * Muestra la documentación Javadoc del proyectp
	 */
	public void mostrarJavadoc ()
	{
		mostrarArchivo ("doc/index.html");
	}
	
	/**
     * Muestra la información acerca del desarrollo de esta apicación
     */
    public void acercaDe ()
    {
		String resultado = "\n\n ************************************\n\n";
		resultado += " * Universidad	de	los	Andes	(Bogotá	- Colombia)\n";
		resultado += " * Departamento	de	Ingeniería	de	Sistemas	y	Computación\n";
		resultado += " * Licenciado	bajo	el	esquema	Academic Free License versión 2.1\n";
		resultado += " * \n";		
		resultado += " * Curso: isis2304 - Sistemas Transaccionales\n";
		resultado += " * Proyecto: Alohandes Uniandes\n";
		resultado += " * @version 1.0\n";
		resultado += " * \n";
		resultado += "\n ************************************\n\n";

		panelDatos.actualizarInterfaz(resultado);		
    }
    

	/* ****************************************************************
	 * 			Métodos privados para la presentación de resultados y otras operaciones
	 *****************************************************************/
    /**
     * Genera una cadena de caracteres con la lista de los tipos de bebida recibida: una línea por cada tipo de bebida
     * @param lista - La lista con los tipos de bebida
     * @return La cadena con una líea para cada tipo de bebida recibido
     */
    private String listarReservas(List<VOReserva> lista) 
    {
    	String resp = "Las Reservas existentes son:\n";
    	int i = 1;
        for (VOReserva tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}

	/**
	 * Genera una cadena de caracteres con la lista de los tipos de bebida recibida: una línea por cada tipo de bebida
	 * @param lista - La lista con los tipos de bebida
	 * @return La cadena con una líea para cada tipo de bebida recibido
	 */
	private String listarInmuebles(List<VOInmueble> lista)
	{
		String resp = "Los inmuebles existentes son:\n";
		int i = 1;
		for (VOInmueble tb : lista)
		{
			resp += i++ + ". " + tb.toString() + "\n";
		}
		return resp;
	}
	private String listarMejorYPeorInmueble(List<Inmueble> lista)
	{
		String resp = "";
		
			resp +="El mejor inmuble es " + lista.get(0).toString() + "\n";
			resp +="El peor inmuble es " + lista.get(0).toString() + "\n";
		return resp;
	}
	private String listarClientes(List<Cliente> lista,int top)
	{
		String resp = "\n";
		if(top==1)
		{
			resp+="Clientes que reservaron todas las semanas\n";
		}
		if(top==2)
		{
			resp+="Clientes que pagan habitaciones caras\n";
		}
		if(top==3)
		{
			resp+="Clientes que reservan SUITES\n";
		}
		int i = 1;
		for (Cliente tb : lista)
		{
			resp += i++ + ". " + tb.toString() + "\n";
		}
		return resp;
	}
	
	private String listarClientes(List<VOCliente> lista)
	{
		String resp = "\n";
	
		int i = 1;
		for (VOCliente tb : lista)
		{
			resp += i++ + ". " + tb.toString() + "\n";
		}
		return resp;
	}
    /**
     * Genera una cadena de caracteres con la descripción de la excepcion e, haciendo énfasis en las excepcionsde JDO
     * @param e - La excepción recibida
     * @return La descripción de la excepción, cuando es javax.jdo.JDODataStoreException, "" de lo contrario
     */
	private String darDetalleException(Exception e) 
	{
		String resp = "";
		if (e.getClass().getName().equals("javax.jdo.JDODataStoreException"))
		{
			JDODataStoreException je = (javax.jdo.JDODataStoreException) e;
			return je.getNestedExceptions() [0].getMessage();
		}
		return resp;
	}

	/**
	 * Genera una cadena para indicar al usuario que hubo un error en la aplicación
	 * @param e - La excepción generada
	 * @return La cadena con la información de la excepción y detalles adicionales
	 */
	private String generarMensajeError(Exception e) 
	{
		String resultado = "************ Error en la ejecución\n";
		resultado += e.getLocalizedMessage() + ", " + darDetalleException(e);
		resultado += "\n\nRevise datanucleus.log y parranderos.log para más detalles";
		return resultado;
	}

	/**
	 * Limpia el contenido de un archivo dado su nombre
	 * @param nombreArchivo - El nombre del archivo que se quiere borrar
	 * @return true si se pudo limpiar
	 */
	private boolean limpiarArchivo(String nombreArchivo) 
	{
		BufferedWriter bw;
		try 
		{
			bw = new BufferedWriter(new FileWriter(new File (nombreArchivo)));
			bw.write ("");
			bw.close ();
			return true;
		} 
		catch (IOException e) 
		{
//			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Abre el archivo dado como parámetro con la aplicación por defecto del sistema
	 * @param nombreArchivo - El nombre del archivo que se quiere mostrar
	 */
	private void mostrarArchivo (String nombreArchivo)
	{
		try
		{
			Desktop.getDesktop().open(new File(nombreArchivo));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* ****************************************************************
	 * 			Métodos de la Interacción
	 *****************************************************************/
    /**
     * Método para la ejecución de los eventos que enlazan el menú con los métodos de negocio
     * Invoca al método correspondiente según el evento recibido
     * @param pEvento - El evento del usuario
     */
    @Override
	public void actionPerformed(ActionEvent pEvento)
	{
		String evento = pEvento.getActionCommand( );		
        try 
        {
			Method req = InterfazAlohandesApp.class.getMethod ( evento );			
			req.invoke ( this );
		} 
        catch (Exception e) 
        {
			e.printStackTrace();
		} 
	}
    
	/* ****************************************************************
	 * 			Programa principal
	 *****************************************************************/
    /**
     * Este método ejecuta la aplicación, creando una nueva interfaz
     * @param args Arreglo de argumentos que se recibe por línea de comandos
     */
    public static void main( String[] args )
    {
        try
        {
        	
            // Unifica la interfaz para Mac y para Windows.
            UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName( ) );
            InterfazAlohandesApp interfaz = new InterfazAlohandesApp( );
            interfaz.setVisible( true );
        }
        catch( Exception e )
        {
            e.printStackTrace( );
        }
    }
}
