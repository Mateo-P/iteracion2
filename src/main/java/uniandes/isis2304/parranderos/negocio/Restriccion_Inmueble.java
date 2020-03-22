package uniandes.isis2304.parranderos.negocio;

public class Restriccion_Inmueble implements VORestriccion_Inmueble{

    public enum TIPO_INMUEBLE{
        APARTAMENTO,
        HABITACION_HOTEL,
        HABITACION_HOSTEL,
        HABITACION_UNIVERSITARIA,
        VIVIENDA_CEDIDA,
        HABITACION_PERSONA_NATURAL
    }

    private TIPO_INMUEBLE tipoInmueble;

    public TIPO_INMUEBLE getTipoInmueble() {
        return tipoInmueble;
    }

    public void setTipoInmueble(TIPO_INMUEBLE tipoInmueble) {
        this.tipoInmueble = tipoInmueble;
    }

    public int getDuracionMinima() {
        return duracionMinima;
    }

    public Restriccion_Inmueble(){
        this.tipoInmueble = null;
        this.duracionMinima = 0;
    }

    public Restriccion_Inmueble(TIPO_INMUEBLE tipoInmueble, int duracionMinima) {
        this.tipoInmueble = tipoInmueble;
        this.duracionMinima = duracionMinima;
    }

    public void setDuracionMinima(int duracionMinima) {
        this.duracionMinima = duracionMinima;
    }

    @Override
    public String toString() {
        return "Restriccion_Inmueble{" +
                "tipoInmueble=" + tipoInmueble +
                ", duracionMinima=" + duracionMinima +
                '}';
    }

    private int duracionMinima;


}
