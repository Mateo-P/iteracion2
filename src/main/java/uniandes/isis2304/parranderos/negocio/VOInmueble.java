package uniandes.isis2304.parranderos.negocio;

public interface VOInmueble {

    public long getId();



    public String getNombre();


    public Restriccion_Inmueble.TIPO_INMUEBLE  getTipoInmueble() ;



    public String getUbicacion();



    public int getCapacidad();



    public boolean isDisponible();



    public String getFoto();



    public String getDescripcion();



    public int getVecesReservada();



    public double getCostoXNoche();



    public long getIdOperador();



    @Override
    public String toString();

}
