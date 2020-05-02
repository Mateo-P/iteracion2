package uniandes.isis2304.parranderos.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.parranderos.negocio.Inmueble;
import uniandes.isis2304.parranderos.negocio.Reserva;
import uniandes.isis2304.parranderos.negocio.Servicio_Adicional;
import uniandes.isis2304.parranderos.negocio.Vivienda_Cedida;

public class SQLServicioAdicional {
	
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
    public SQLServicioAdicional (PersistenciaAlohandes pp)
    {
        this.pp = pp;
    }
    
    public List<Servicio_Adicional> darViviendasCedidas(PersistenceManager pm)
    {
        Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaServiciosAdicional());
        q.setResultClass(Reserva.class);
        return (List<Servicio_Adicional>) q.execute();
    }
    
    
    public Servicio_Adicional darServicioPorId (PersistenceManager pm, long idServicio)
    {
        Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaServiciosAdicional() + " WHERE id = ?");
        q.setResultClass(Servicio_Adicional.class);
        q.setParameters(idServicio);
        return (Servicio_Adicional) q.executeUnique();
    }
}
