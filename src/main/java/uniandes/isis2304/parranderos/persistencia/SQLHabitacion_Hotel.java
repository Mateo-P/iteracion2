package uniandes.isis2304.parranderos.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.parranderos.negocio.Habitacion_Hostel;
import uniandes.isis2304.parranderos.negocio.Habitacion_Hotel;
import uniandes.isis2304.parranderos.negocio.Reserva;

public class SQLHabitacion_Hotel {
	
	
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
    public SQLHabitacion_Hotel (PersistenciaAlohandes pp)
    {
        this.pp = pp;
    }
    
    public List<Habitacion_Hotel> darHabitaciones_hotel(PersistenceManager pm)
    {
        Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaHabitacionHotel());
        q.setResultClass(Reserva.class);
        return (List<Habitacion_Hotel>) q.execute();
    }
    
    public long adicionarHabitacion_Hotel (PersistenceManager pm, long id, String tipoHabitacion,double costoTotal,String nombre, String tipoInmueble,String ubicacion,int capacidad,boolean disponible,String foto,String descripcion,int veceReservada,double costoXNoche,long idOperador)
    {

           
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaHabitacionHotel() + "(ID_INMUEBLE_HH,TIPO_HABITACION,COSTO_TOTAL) values (?, ?, ?)");
        q.setParameters(id, tipoHabitacion,costoTotal);
        inmuebleSQL.adicionarInmueble( pm, id, nombre, tipoInmueble, ubicacion,capacidad,disponible,foto,descripcion,veceReservada,costoXNoche,idOperador);
        return (long) q.executeUnique();

    }
    

}
