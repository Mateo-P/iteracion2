package uniandes.isis2304.parranderos.negocio;

public class Apartamento extends Inmueble implements VOApartamento{

    private int numeroHabitaciones;

    private boolean amoblado;

    private boolean serviciosIncluidos;

    public Apartamento(){
        this.numeroHabitaciones = 0;
        this.amoblado = false;
        this.serviciosIncluidos = false;
        this.idInmueble = 0;
    }

    public Apartamento(int numeroHabitaciones, boolean amoblado, boolean serviciosIncluidos, long idInmueble) {
        this.numeroHabitaciones = numeroHabitaciones;
        this.amoblado = amoblado;
        this.serviciosIncluidos = serviciosIncluidos;
        this.idInmueble = idInmueble;
    }

    private long idInmueble;

    public int getNumeroHabitaciones() {
        return numeroHabitaciones;
    }

    public void setNumeroHabitaciones(int numeroHabitaciones) {
        this.numeroHabitaciones = numeroHabitaciones;
    }

    public boolean isAmoblado() {
        return amoblado;
    }

    public void setAmoblado(boolean amoblado) {
        this.amoblado = amoblado;
    }

    public boolean isServiciosIncluidos() {
        return serviciosIncluidos;
    }

    public void setServiciosIncluidos(boolean serviciosIncluidos) {
        this.serviciosIncluidos = serviciosIncluidos;
    }

    public long getIdInmueble() {
        return idInmueble;
    }

    public void setIdInmueble(long idInmueble) {
        this.idInmueble = idInmueble;
    }

    @Override
    public String toString() {
        return "Apartamento{" +
                "numeroHabitaciones=" + numeroHabitaciones +
                ", amoblado=" + amoblado +
                ", serviciosIncluidos=" + serviciosIncluidos +
                ", idInmueble=" + idInmueble +
                '}';
    }



}
