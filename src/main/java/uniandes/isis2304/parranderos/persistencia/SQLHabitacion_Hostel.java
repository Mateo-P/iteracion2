package uniandes.isis2304.parranderos.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.parranderos.negocio.Apartamento;
import uniandes.isis2304.parranderos.negocio.Habitacion_Hostel;
import uniandes.isis2304.parranderos.negocio.Reserva;

public class SQLHabitacion_Hostel {

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
    public SQLHabitacion_Hostel (PersistenciaAlohandes pp)
    {
        this.pp = pp;
    }
    
    public List<Habitacion_Hostel> darHabitaciones_hostel(PersistenceManager pm)
    {
        Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaHabitacionHostel());
        q.setResultClass(Reserva.class);
        return (List<Habitacion_Hostel>) q.execute();
    }
    
    public long adicionarHabitacion_Hostel (PersistenceManager pm, long id, boolean compartida,String nombre, String tipoInmueble,String ubicacion,int capacidad,boolean disponible,String foto,String descripcion,int veceReservada,double costoXNoche,long idOperador)
    {

        String compartidaB = "";
       

        if(compartida == true){
        	compartidaB = "Y";
        }else{
        	compartidaB = "N";
        }
     
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaHabitacionHostel() + "(ID_INMUEBLE_HSH, COMPARTIDA) values (?, ?)");
        q.setParameters(id, compartidaB);
        inmuebleSQL.adicionarInmueble( pm, id, nombre, tipoInmueble, ubicacion,capacidad,disponible,foto,descripcion,veceReservada,costoXNoche,idOperador);
        return (long) q.executeUnique();

    }

    




}
