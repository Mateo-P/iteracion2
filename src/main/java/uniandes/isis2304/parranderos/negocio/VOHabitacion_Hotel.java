package uniandes.isis2304.parranderos.negocio;

public interface VOHabitacion_Hotel{


        public Habitacion_Hotel.TIPO_HABITACION getTipoHabitacion() ;



        public double getCostoTotal();



        public long getIdInmueble();



        @Override
        public String toString() ;
}
