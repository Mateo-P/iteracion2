package uniandes.isis2304.parranderos.negocio;

public class Habitacion_Hostel extends Inmueble implements  VOHabitacion_Hostel {



    private boolean compartida;

     private long idInmueble;

    public Habitacion_Hostel(){
        this.compartida = false;
        this.idInmueble = 0;
    }

    public Habitacion_Hostel(boolean compartida, long idInmueble) {
        this.compartida = compartida;
        this.idInmueble = idInmueble;
    }

    public boolean isCompartida() {
        return compartida;
    }

    public void setCompartida(boolean compartida) {
        this.compartida = compartida;
    }

    public long getIdInmueble() {
        return idInmueble;
    }

    public void setIdInmueble(long idInmueble) {
        this.idInmueble = idInmueble;
    }


    @Override
    public String toString() {
        return "Habitacion_Hostel{" +
                "compartida=" + compartida +
                ", idInmueble=" + idInmueble +
                '}';
    }

}
