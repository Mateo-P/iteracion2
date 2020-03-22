package uniandes.isis2304.parranderos.negocio;

public class Inmueble implements VOInmueble{


    private long id;

    private String nombre;

    private Restriccion_Inmueble.TIPO_INMUEBLE tipoInmueble;

    private String ubicacion;

    private int capacidad;

    private boolean disponible;

    private String foto;

    private String descripcion;

    private int vecesReservada;

    private double costoXNoche;

    private long idOperador;

    public Inmueble(){
        this.id = 0;
        this.nombre = "";
        this.tipoInmueble = null;
        this.ubicacion = "";
        this.capacidad = 0;
        this.disponible = false;
        this.foto = "";
        this.descripcion = "";
        this.vecesReservada = 0;
        this.costoXNoche = 0;
        this.idOperador = 0;
    }

    public Inmueble( long id,String nombre,Restriccion_Inmueble.TIPO_INMUEBLE  tipo_inmueble,String ubicacion,int capacidad,boolean disponible,String foto,String descripcion,int vecesReservada,double costoXNoche,long idOperador){
        this.id = id;
        this.nombre = nombre;
        this.tipoInmueble = tipo_inmueble;
        this.ubicacion = ubicacion;
        this.capacidad = capacidad;
        this.disponible = disponible;
        this.foto = foto;
        this.descripcion = descripcion;
        this.vecesReservada = vecesReservada;
        this.costoXNoche = costoXNoche;
        this.idOperador = idOperador;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Restriccion_Inmueble.TIPO_INMUEBLE  getTipoInmueble() {
        return tipoInmueble;
    }

    public void setTipoInmueble(Restriccion_Inmueble.TIPO_INMUEBLE  tipoInmueble) {
        this.tipoInmueble = tipoInmueble;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getVecesReservada() {
        return vecesReservada;
    }

    public void setVecesReservada(int vecesReservada) {
        this.vecesReservada = vecesReservada;
    }

    public double getCostoXNoche() {
        return costoXNoche;
    }

    public void setCostoXNoche(double costoXNoche) {
        this.costoXNoche = costoXNoche;
    }

    public long getIdOperador() {
        return idOperador;
    }

    public void setIdOperador(long idOperador) {
        this.idOperador = idOperador;
    }

    @Override
    public String toString() {
        return "Inmueble{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", tipoInmueble=" + tipoInmueble +
                ", ubicacion='" + ubicacion + '\'' +
                ", capacidad=" + capacidad +
                ", disponible=" + disponible +
                ", foto='" + foto + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", vecesReservada=" + vecesReservada +
                ", costoXNoche=" + costoXNoche +
                ", idOperador=" + idOperador +
                '}';
    }








}
