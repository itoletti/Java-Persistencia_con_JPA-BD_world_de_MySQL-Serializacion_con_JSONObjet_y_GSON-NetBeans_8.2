package testJson;

import org.json.JSONObject;

public class TestJson {
    
    public static void main(String[] args) {
        String[] amigos = {"Guayaba", "Snowball", "Cuco"};
        Raza raza = new Raza("Caniche", "Francia");
        Mascota mascota = new Mascota("Maggie", (short)3000, amigos, raza);        
        JSONObject jsonObject = new JSONObject(mascota);
        String codificado = jsonObject.toString();
        System.out.println("Codificado: " + codificado); 
    }    
}
