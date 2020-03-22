package uniandes.isis2304.parranderos.negocio;

public class Habitacion_Hotel extends Inmueble implements VOHabitacion_Hotel {

    public enum TIPO_HABITACION{
        SUITE,SEMISUITE,ESTANDAR
    }

    private TIPO_HABITACION tipoHabitacion;

    private double costoTotal;

    private long idInmueble;

    public Habitacion_Hotel(){
        this.tipoHabitacion = null;
        this.costoTotal = 0;
        this.idInmueble = 0;
    }

    public Habitacion_Hotel(TIPO_HABITACION tipoHabitacion, double costoTotal, long idInmueble) {
        this.tipoHabitacion = tipoHabitacion;
        this.costoTotal = costoTotal;
        this.idInmueble = idInmueble;
    }

    public TIPO_HABITACION getTipoHabitacion() {
        return tipoHabitacion;
    }

    public void setTipoHabitacion(TIPO_HABITACION tipoHabitacion) {
        this.tipoHabitacion = tipoHabitacion;
    }

    public double getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(double costoTotal) {
        this.costoTotal = costoTotal;
    }

    public long getIdInmueble() {
        return idInmueble;
    }

    public void setIdInmueble(long idInmueble) {
        this.idInmueble = idInmueble;
    }

    @Override
    public String toString() {
        return "Habitacion_Hotel{" +
                "tipoHabitacion=" + tipoHabitacion +
                ", costoTotal=" + costoTotal +
                ", idInmueble=" + idInmueble +
                '}';
    }


}
