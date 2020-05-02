package uniandes.isis2304.parranderos.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.parranderos.negocio.Ofrecen;
import uniandes.isis2304.parranderos.negocio.Reserva;
import uniandes.isis2304.parranderos.negocio.Servicio_Adicional;

public class SQLOfrecen {
	
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
   public SQLOfrecen (PersistenciaAlohandes pp)
   {
       this.pp = pp;
   }
   
   public List<Ofrecen> darViviendasCedidas(PersistenceManager pm)
   {
       Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaOfrecen());
       q.setResultClass(Ofrecen.class);
       return (List<Ofrecen>) q.execute();
   }
   
   
   public Servicio_Adicional darOfrecenPorIdServicio (PersistenceManager pm, long idServicio)
   {
       Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaServiciosAdicional() + " WHERE ID_SERVICIO = ?");
       q.setResultClass(Servicio_Adicional.class);
       q.setParameters(idServicio);
       return (Servicio_Adicional) q.executeUnique();
   }
   
   public Servicio_Adicional darOfrecenPorIdInmueble (PersistenceManager pm, long idServicio)
   {
       Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaServiciosAdicional() + " WHERE ID_INMUEBLE = ?");
       q.setResultClass(Servicio_Adicional.class);
       q.setParameters(idServicio);
       return (Servicio_Adicional) q.executeUnique();
   }
}


