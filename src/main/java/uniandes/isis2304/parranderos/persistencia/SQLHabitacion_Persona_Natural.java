package uniandes.isis2304.parranderos.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.parranderos.negocio.Habitacion_Hostel;
import uniandes.isis2304.parranderos.negocio.Habitacion_Persona_Natural;
import uniandes.isis2304.parranderos.negocio.Reserva;

public class SQLHabitacion_Persona_Natural {
	
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

    /**
     * Clase padre sql INMUEBLE
     */

    private SQLInmueble inmuebleSQL;


    /* ****************************************************************
     * 			Métodos
     *****************************************************************/
    /**
     * Constructor
     * @param pp - El Manejador de persistencia de la aplicación
     */
    public SQLHabitacion_Persona_Natural (PersistenciaAlohandes pp)
    {
        this.pp = pp;
    }
    
    
    public List<Habitacion_Persona_Natural> darHabitacionesPersonaNatural(PersistenceManager pm)
    {
        Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaHabitacionPersonaNatural());
        q.setResultClass(Reserva.class);
        return (List<Habitacion_Persona_Natural>) q.execute();
    }

    public long adicionarHabitacion_Persona_Natural (PersistenceManager pm, long id,double precioServicios,boolean comidaIncluida,boolean banioPrivado,boolean cocineta,boolean compartida,String nombre, String tipoInmueble,String ubicacion,int capacidad,boolean disponible,String foto,String descripcion,int veceReservada,double costoXNoche,long idOperador)
    {

        String cocinetaB = "";
       

        if(cocineta == true){
        	cocinetaB = "Y";
        }else{
        	cocinetaB = "N";
        }
        
        String compartidaB = "";
       

        if(compartida == true){
        	compartidaB = "Y";
        }else{
        	compartidaB = "N";
        }
        
        String comidaB = "";
       

        if(comidaIncluida == true){
        	comidaB = "Y";
        }else{
        	comidaB = "N";
        }
        String banioB = "";
       

        if(banioPrivado == true){
        	banioB = "Y";
        }else{
        	banioB = "N";
        }
           
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaHabitacionPersonaNatural() + "(ID_INMUEBLE_PR,PRECIO_SERIVICIOS,COMIDA_INCLUIDA,BANIO_PRIVADO,COCINETA,COMPARTIDA ) values (?, ?, ?, ?, ?, ?)");
        q.setParameters(id, precioServicios,comidaIncluida,banioPrivado,cocineta,compartida);
        inmuebleSQL.adicionarInmueble( pm, id, nombre, tipoInmueble, ubicacion,capacidad,disponible,foto,descripcion,veceReservada,costoXNoche,idOperador);
        return (long) q.executeUnique();

    }
}
