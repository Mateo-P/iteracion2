package uniandes.isis2304.parranderos.persistencia;

import uniandes.isis2304.parranderos.negocio.Inmueble;
import uniandes.isis2304.parranderos.negocio.Operador;
import uniandes.isis2304.parranderos.negocio.Reserva;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.sql.Timestamp;
import java.util.List;

public class SQLInmueble {

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
    public SQLInmueble (PersistenciaAlohandes pp)
    {
        this.pp = pp;
    }




    public long adicionarInmueble (PersistenceManager pm, long id,String nombre, String tipoInmueble,String ubicacion,int capacidad,boolean disponible,String foto,String descripcion,int veceReservada,double costoXNoche,long idOperador)
    {
        String disponibleB = "";
        if(disponible== true){
            disponibleB = "Y";
        }else{
            disponibleB = "N";
        }
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaInmueble () + "(ID_INMUEBLE, DISPONIBLE,NOMBRE,TIPO_INMUEBLE,UBICACION,CAPACIDAD,DESCRIPCION,VECES_RESERVADA,COSTO_NOCHE,ID_OPERADOR,FOTO) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)");
        q.setParameters(id,disponibleB, nombre, tipoInmueble, ubicacion,capacidad,descripcion,veceReservada,costoXNoche,idOperador,foto);
        return (long) q.executeUnique();
    }

    /**
     * Crea y ejecuta la sentencia SQL para encontrar la información de UN BAR de la
     * base de datos de Parranderos, por su identificador
     * @param pm - El manejador de persistencia
     * @return El objeto BAR que tiene el identificador dado
     */
    public Inmueble darInmueblePorId (PersistenceManager pm, long idInmueble)
    {
        Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaInmueble () + " WHERE id = ?");
        q.setResultClass(Inmueble.class);
        q.setParameters(idInmueble);
        return (Inmueble) q.executeUnique();
    }
  
    /**
     * Crea y ejecuta la sentencia SQL para eliminar UN INMUEBLE de la base de datos de Parranderos, por su identificador
     * @param pm - El manejador de persistencia
     * @param idInmueble - El identificador del bar
     * @return EL número de tuplas eliminadas
     */
    public long eliminarInmueble(PersistenceManager pm,long idInmueble){
        Query q1 = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaReserva () + " WHERE ID_INMUEBLE = ?");
        q1.setResultClass(Reserva.class);
        q1.setParameters(idInmueble);
        Reserva reserva = (Reserva) q1.executeUnique();
        if(reserva != null){
            return 0;
        }else{
            Query q2 = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaInmueble() + " WHERE ID_INMUEBLE = ?");
            q2.setResultClass(Inmueble.class);
            q2.setParameters(idInmueble);
            Inmueble inmueble = (Inmueble) q1.executeUnique();
            String tipo_inmueble = inmueble.getTipoInmueble()+"";
            Query q3= pm.newQuery(SQL, "DELETE FROM " + tipo_inmueble + " WHERE  ID_INMUEBLE= ?");
            q3.setParameters(idInmueble);
            long fila1 = (long) q3.executeUnique();
            Query q4= pm.newQuery(SQL, "DELETE FROM " + pp.darTablaInmueble() + " WHERE  ID_INMUEBLE= ?");
            q4.setParameters(idInmueble);
            long fila2=  (long) q4.executeUnique();
            return fila1+fila2;
        }

    }
    /**
     * Crea y ejecuta la sentencia SQL para encontrar la información de LOS INMUEBLES de la
     * base de datos de Parranderos
     * @param pm - El manejador de persistencia
     * @return Una lista de objetos BAR
     */

    public List<Inmueble> darInmuebles (PersistenceManager pm)
    {
        Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaInmueble ());
        q.setResultClass(Reserva.class);
        return (List<Inmueble>) q.execute();
    }
    
    public List<Inmueble> darInmueblesPorTipo (PersistenceManager pm, String tipoInmueble)
    {
        Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaInmueble () + " WHERE INMUEBLE.TIPO_INMUEBLE=?");
        q.setResultClass(Inmueble.class);
        q.setParameters(tipoInmueble);
        return (List<Inmueble>) q.execute();
    }

}
