package uniandes.isis2304.parranderos.persistencia;

import uniandes.isis2304.parranderos.negocio.Inmueble;
import uniandes.isis2304.parranderos.negocio.Reserva;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.sql.Timestamp;

public class SQLInmueble {

    /* ****************************************************************
     * 			Constantes
     *****************************************************************/
    /**
     * Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos
     * Se renombra acá para facilitar la escritura de las sentencias
     */
    private final static String SQL = PersistenciaParranderos.SQL;

    /* ****************************************************************
     * 			Atributos
     *****************************************************************/
    /**
     * El manejador de persistencia general de la aplicación
     */
    private PersistenciaParranderos pp;

    /* ****************************************************************
     * 			Métodos
     *****************************************************************/
    /**
     * Constructor
     * @param pp - El Manejador de persistencia de la aplicación
     */
    public SQLInmueble (PersistenciaParranderos pp)
    {
        this.pp = pp;
    }

    /**
     * Crea y ejecuta la sentencia SQL para adicionar una RESERVA a la base de datos de Alohandes
     * @param pm - El manejador de persistencia

     * @return EL número de tuplas insertadas
     */
    public long adicionarInmueble (PersistenceManager pm, long idReserva, Timestamp fechaInicioReserva, Timestamp fechaFinReserva, Timestamp fechaGeneracionReserva, int numeroPersonas, long idInmueble)
    {
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaReserva () + "(ID_RESERVA,FECHA_INICIO_RESERVA,FECHA_FINAL_RESERVA,FECHA_GENERACION_RESERVA,NUMERO_PERSONAS,ID_INMUEBLE) VALUES (?, ?, ?, ?, ?, ?)");
        q.setParameters(idReserva, fechaInicioReserva, fechaFinReserva,fechaGeneracionReserva,numeroPersonas,idInmueble);
        return (long)q.executeUnique();
    }

    public long eliminarInmueble(PersistenceManager pm,long idInmueble){
        Query q1 = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaReserva () + " WHERE ID_INMUEBLE = ?");
        q1.setResultClass(Reserva.class);
        q1.setParameters(idInmueble);
        Reserva reserva = (Reserva) q1.executeUnique();
        if(reserva != null){
            return 0;
        }else{
            Query q2 = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaReserva () + " WHERE ID_INMUEBLE = ?");
            q2.setResultClass(Inmueble.class);
            q2.setParameters(idInmueble);
            Inmueble inmueble = (Inmueble) q1.executeUnique();
            String tipo_inmueble = inmueble.getTipoInmueble()+"";
            Query q3= pm.newQuery(SQL, "DELETE FROM " + tipo_inmueble + " WHERE  ID_INMUEBLE= ?");
            q3.setParameters(idInmueble);
            long fila1 = (long) q3.executeUnique();
            Query q4= pm.newQuery(SQL, "DELETE FROM " + pp.darTablaReserva() + " WHERE  ID_INMUEBLE= ?");
            q4.setParameters(idInmueble);
            long fila2=  (long) q4.executeUnique();
            return fila1+fila2;
        }

    }

}
