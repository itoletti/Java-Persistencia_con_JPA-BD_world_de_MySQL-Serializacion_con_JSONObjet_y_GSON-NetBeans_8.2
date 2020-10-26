package testJson;

import java.io.Serializable;

public class Raza {
    private String nombre, pais;

    public Raza(String nombre, String pais) {
        this.nombre = nombre;
        this.pais = pais;
    }
    

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
    
}