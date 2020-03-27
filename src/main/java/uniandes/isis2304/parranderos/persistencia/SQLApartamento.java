package uniandes.isis2304.parranderos.persistencia;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

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


    public long adicionarApartamento (PersistenceManager pm, long id, int numeroHabitaciones, boolean amoblado,boolean serviciosIncluidos ,String nombre, String tipoInmueble,String ubicacion,int capacidad,boolean disponible,String foto,String descripcion,int veceReservada,double costoXNoche,long idOperador)
    {

        String amobladoB = "";
        String servicios = "";
        String disponibleB = "";

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
        if(disponible== true){
            disponibleB = "Y";
        }else{
            disponibleB = "N";
        }

        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaApartamento () + "(ID_INMUEBLE_AP, AMOBLADO, NUMERO_HABITACIONES,SERVICIOS_INCLUIDOS) values (?, ?, ?, ?, ?)");
        q.setParameters(id, amobladoB, numeroHabitaciones, servicios);
        return (long) q.executeUnique();
    }

}
