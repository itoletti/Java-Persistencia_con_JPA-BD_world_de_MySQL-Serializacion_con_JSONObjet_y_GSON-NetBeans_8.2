/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import java.math.BigDecimal;
import org.json.JSONObject;


/**
 *
 * @author Usuario
 */
public class TestEntidad {
    public static void main(String[] args) {
        Country pais;
        pais = new Country("ALC","Alarconlandia","North America","Central America",BigDecimal.valueOf(15.00),(short)1980,1,BigDecimal.valueOf(99.00),BigDecimal.valueOf(3.00),BigDecimal.valueOf(3.00),"Natilandia", "Monarquia","Nataluña",100,"AN");
        JSONObject jsonObject = new JSONObject(pais);
        String codificado = jsonObject.toString();
        System.out.println("Codificado: " + codificado); 
    }     
}
