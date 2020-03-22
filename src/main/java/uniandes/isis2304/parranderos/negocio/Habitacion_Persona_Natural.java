package uniandes.isis2304.parranderos.negocio;

public class Habitacion_Persona_Natural  extends Inmueble implements  VOHabitacion_Persona_Natural{

    private double precioServicios;

    private boolean comidaIncluida;

    private boolean bañoPrivado;

    private boolean cocineta;

    private boolean compartida;

    private long idInmueble;

    public double getPrecioServicios() {
        return precioServicios;
    }

    public void setPrecioServicios(double precioServicios) {
        this.precioServicios = precioServicios;
    }

    public boolean isComidaIncluida() {
        return comidaIncluida;
    }

    public void setComidaIncluida(boolean comidaIncluida) {
        this.comidaIncluida = comidaIncluida;
    }

    public boolean isBañoPrivado() {
        return bañoPrivado;
    }

    public void setBañoPrivado(boolean bañoPrivado) {
        this.bañoPrivado = bañoPrivado;
    }

    public boolean isCocineta() {
        return cocineta;
    }

    public void setCocineta(boolean cocineta) {
        this.cocineta = cocineta;
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

    public Habitacion_Persona_Natural(){
        this.precioServicios = 0;
        this.comidaIncluida = false;
        this.bañoPrivado = false;
        this.cocineta = false;
        this.compartida = false;
        this.idInmueble = 0;
    }


    public Habitacion_Persona_Natural(double precioServicios, boolean comidaIncluida, boolean bañoPrivado, boolean cocineta, boolean compartida, long idInmueble) {
        this.precioServicios = precioServicios;
        this.comidaIncluida = comidaIncluida;
        this.bañoPrivado = bañoPrivado;
        this.cocineta = cocineta;
        this.compartida = compartida;
        this.idInmueble = idInmueble;
    }

    @Override
    public String toString() {
        return "Habitacion_Persona_Natural{" +
                "precioServicios=" + precioServicios +
                ", comidaIncluida=" + comidaIncluida +
                ", bañoPrivado=" + bañoPrivado +
                ", cocineta=" + cocineta +
                ", compartida=" + compartida +
                ", idInmueble=" + idInmueble +
                '}';
    }

}
