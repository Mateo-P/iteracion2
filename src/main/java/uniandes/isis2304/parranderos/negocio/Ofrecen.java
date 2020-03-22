package uniandes.isis2304.parranderos.negocio;

public class Ofrecen implements VOOfrecen{

    private long idServicio;

    private long idInmueble;

    public Ofrecen(){
        this.idServicio = 0;
        this.idInmueble = 0;
    }

    public Ofrecen(long idServicio, long idInmueble) {
        this.idServicio = idServicio;
        this.idInmueble = idInmueble;
    }



    public long getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(long idServicio) {
        this.idServicio = idServicio;
    }

    public long getIdInmueble() {
        return idInmueble;
    }

    public void setIdInmueble(long idInmueble) {
        this.idInmueble = idInmueble;
    }

    @Override
    public String toString() {
        return "Ofrecen{" +
                "idServicio=" + idServicio +
                ", idInmueble=" + idInmueble +
                '}';
    }



}
