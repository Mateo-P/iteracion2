package uniandes.isis2304.parranderos.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.parranderos.negocio.Reserva;
import uniandes.isis2304.parranderos.negocio.Vivienda_Cedida;

public class SQLVivienda_Cedida {
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
    public SQLVivienda_Cedida (PersistenciaAlohandes pp)
    {
        this.pp = pp;
    }
    
    
    public List<Vivienda_Cedida> darViviendasCedidas(PersistenceManager pm)
    {
        Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaViviendaCedida());
        q.setResultClass(Reserva.class);
        return (List<Vivienda_Cedida>) q.execute();
    }
    
    public long adicionarVivienda_Cedida (PersistenceManager pm, long id, int diasUsadoAnio,String infoSeguro,String nombre, String tipoInmueble,String ubicacion,int capacidad,boolean disponible,String foto,String descripcion,int veceReservada,double costoXNoche,long idOperador)
    {

           
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaViviendaCedida() + "(ID_INMUEBLE_VC,DIAS_USADO_ANIO,SEGURO ) values (?, ?, ?)");
        q.setParameters(id, diasUsadoAnio,infoSeguro);
        inmuebleSQL.adicionarInmueble( pm, id, nombre, tipoInmueble, ubicacion,capacidad,disponible,foto,descripcion,veceReservada,costoXNoche,idOperador);
        return (long) q.executeUnique();

    }

}
