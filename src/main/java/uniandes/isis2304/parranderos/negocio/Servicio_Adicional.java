package uniandes.isis2304.parranderos.negocio;

public class Servicio_Adicional implements VOServicio_Adicional{

    private String nombre;

    private double precio;

    private long id;

    public Servicio_Adicional(){
        this.nombre = "";
        this.precio = 0;
        this.id = 0;
    }

    public Servicio_Adicional(String nombre, double precio, long id) {
        this.nombre = nombre;
        this.precio = precio;
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Servicio_Adicional{" +
                "nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", id=" + id +
                '}';
    }


}