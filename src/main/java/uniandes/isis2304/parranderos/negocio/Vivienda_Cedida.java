package uniandes.isis2304.parranderos.negocio;

public class Vivienda_Cedida extends Inmueble {

    private String seguro;

    private int diasUsadoAño;

    private long idInmueble;

    public Vivienda_Cedida(){
        this.seguro = "";
        this.diasUsadoAño = 0;
        this.idInmueble = 0;
    }


    public Vivienda_Cedida(String seguro, int diasUsadoAño, long idInmueble, long idInmueble1) {
        this.seguro = seguro;
        this.diasUsadoAño = diasUsadoAño;
        this.idInmueble = idInmueble;
    }

    public String getSeguro() {
        return seguro;
    }

    public void setSeguro(String seguro) {
        this.seguro = seguro;
    }

    public int getDiasUsadoAño() {
        return diasUsadoAño;
    }

    public void setDiasUsadoAño(int diasUsadoAño) {
        this.diasUsadoAño = diasUsadoAño;
    }

    public long getIdInmueble() {
        return idInmueble;
    }

    public void setIdInmueble(long idInmueble) {
        this.idInmueble = idInmueble;
    }

    @Override
    public String toString() {
        return "Vivienda_Cedida{" +
                "seguro='" + seguro + '\'' +
                ", diasUsadoAño=" + diasUsadoAño +
                ", idInmueble=" + idInmueble +
                '}';
    }


}
