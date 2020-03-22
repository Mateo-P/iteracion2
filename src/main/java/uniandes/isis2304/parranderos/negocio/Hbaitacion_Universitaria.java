package uniandes.isis2304.parranderos.negocio;

public class Hbaitacion_Universitaria extends Inmueble implements  VOHabitacion_Universitaria{

    private boolean compartida;

    private boolean amoblada;

    @Override
    public String toString() {
        return "Hbaitacion_Universitaria{" +
                "compartida=" + compartida +
                ", amoblada=" + amoblada +
                ", idInmueble=" + idInmueble +
                '}';
    }

    private long idInmueble;

    public Hbaitacion_Universitaria(){
        this.compartida = false;
        this.amoblada = false;
        this.idInmueble = 0;
    }

    public Hbaitacion_Universitaria(boolean compartida, boolean amoblada, long idInmueble) {
        this.compartida = compartida;
        this.amoblada = amoblada;
        this.idInmueble = idInmueble;
    }



    public boolean isCompartida() {
        return compartida;
    }

    public void setCompartida(boolean compartida) {
        this.compartida = compartida;
    }

    public boolean isAmoblada() {
        return amoblada;
    }

    public void setAmoblada(boolean amoblada) {
        this.amoblada = amoblada;
    }

    public long getIdInmueble() {
        return idInmueble;
    }

    public void setIdInmueble(long idInmueble) {
        this.idInmueble = idInmueble;
    }



}
