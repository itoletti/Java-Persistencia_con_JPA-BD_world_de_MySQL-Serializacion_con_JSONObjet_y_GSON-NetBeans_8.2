package testJson;

import java.io.Serializable;

public class Mascota {

    private String nombre;
    private short edad;
    private String[] amigos;
    private Raza raza;

    public Mascota(String nombre, short edad, String[] amigos, Raza raza) {
        this.nombre = nombre;
        this.edad = edad;
        this.amigos = amigos;
        this.raza = raza;
    }

    public Raza getRaza() {
        return raza;
    }

    public void setRaza(Raza raza) {
        this.raza = raza;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public short getEdad() {
        return edad;
    }

    public void setEdad(short edad) {
        this.edad = edad;
    }

    public String[] getAmigos() {
        return amigos;
    }

    public void setAmigos(String[] amigos) {
        this.amigos = amigos;
    }

}