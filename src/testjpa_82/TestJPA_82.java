
package testjpa_82;

import java.math.BigDecimal;
import java.util.List;
import logica.City;
import logica.Country;
import persistencia.CityService;
import persistencia.CountryService;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class TestJPA_82 {

    public static void main(String[] args) {
        
        Country pais, pais_1;
        City ciudad;
        List<City> ciudades;
        List<Country> paises;
        int nacimiento = 1980;
        pais = new Country("ALC","Alarconlandia","North America","Central America",BigDecimal.valueOf(15.00),(short)1980,1,BigDecimal.valueOf(99.00),BigDecimal.valueOf(3.00),BigDecimal.valueOf(3.00),"Natilandia", "Monarquia","Nataluña",100,"AN");
        JSONObject jsonObject = new JSONObject(pais);
        String codificado = jsonObject.toString();
        System.out.println("Codificado: " + codificado);
//        Gson gson = new GsonBuilder()
//            .excludeFieldsWithoutExposeAnnotation()
//            .create();
//        String codificado = gson.toJson(pais);        
//        System.out.println("Codificado: " + codificado);
        CountryService paisService = new CountryService();
        paisService.crearCountry(pais);
        
        pais = paisService.buscarCountry("TLT");
        System.out.println(pais.toString());

        jsonObject = new JSONObject(pais);
        codificado = jsonObject.toString();
        System.out.println("Codificado: " + codificado); 
        
        pais = paisService.buscarCountry("ALC");
        System.out.println(pais.toString());
        
        paisService.eliminarCountry("ALC");
        
        paises = paisService.buscarCountry_x_name("Canada");
        
//        for (Country pais_e : paises){
//             System.out.println(pais_e.toString());
//        }
        
        //o lo que seria lo mismo
        paises.forEach((Country _item) -> {
                JSONObject jsonObject_1 = new JSONObject(_item);             
                String codificado_1 = jsonObject_1.toString();
                System.out.println("Codificado: " + codificado_1);
                System.out.println(_item.toString());
            });         
        
        
        CityService ciudadService = new CityService();
        String ls_ciudad = "Tandil";
        ciudades = ciudadService.buscarCity_x_nombre(ls_ciudad);
        
        if (ciudades.isEmpty())
            System.out.println("la ciudad " + ls_ciudad + " no existe");
        else {
            JSONObject respuesta = new JSONObject();
            JSONObject resultadoCiudades[];
            int i = 0;
            resultadoCiudades = new JSONObject[ciudades.size()];
            for( City unaCiudad : ciudades) {
                    respuesta = new JSONObject(unaCiudad);
                    resultadoCiudades[i++] = jsonObject;
                    System.out.println(unaCiudad.toString());
            };
            System.out.println(respuesta.put("ciudades", resultadoCiudades));
        }
        
   
        
    }

}
