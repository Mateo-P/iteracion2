package uniandes.isis2304.parranderos.persistencia;

import uniandes.isis2304.parranderos.negocio.Apartamento;
import uniandes.isis2304.parranderos.negocio.Inmueble;
import uniandes.isis2304.parranderos.negocio.Reserva;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.List;

public class SQLApartamento {

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
    public SQLApartamento (PersistenciaAlohandes pp)
    {
        this.pp = pp;
    }

    public List<Apartamento> darApartamentos(PersistenceManager pm)
    {
        Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaApartamento());
        q.setResultClass(Reserva.class);
        return (List<Apartamento>) q.execute();
    }


    public long adicionarApartamento (PersistenceManager pm, long id, int numeroHabitaciones, boolean amoblado,boolean serviciosIncluidos ,String nombre, String tipoInmueble,String ubicacion,int capacidad,boolean disponible,String foto,String descripcion,int veceReservada,double costoXNoche,long idOperador)
    {

        String amobladoB = "";
        String servicios = "";

        if(amoblado == true){
            amobladoB = "Y";
        }else{
            amobladoB = "N";
        }
        if(serviciosIncluidos == true){
            servicios = "Y";
        }else{
            servicios = "N";
        }
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaApartamento () + "(ID_INMUEBLE_AP, AMOBLADO, NUMERO_HABITACIONES,SERVICIOS_INCLUIDOS) values (?, ?, ?, ?)");
        q.setParameters(id, amobladoB, numeroHabitaciones, servicios);
        inmuebleSQL.adicionarInmueble( pm, id, nombre, tipoInmueble, ubicacion,capacidad,disponible,foto,descripcion,veceReservada,costoXNoche,idOperador);
        return (long) q.executeUnique();

    }

}
