
package persistencia;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import logica.Country;

public class CountryService {
    
    CountryJpaController countryJPA = new CountryJpaController();
    
    public void crearCountry(Country pais){
        try {
            countryJPA.create(pais);
        } catch (Exception ex) {
            Logger.getLogger(CountryService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminarCountry(String id){
        try {
            countryJPA.destroy(id);
        } catch (Exception ex) {
            Logger.getLogger(CountryService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void actualizarCountry(Country pais){
        try {
            countryJPA.edit(pais);
        } catch (Exception ex) {
            Logger.getLogger(CountryService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    public Country buscarCountry(String id){
        Country res = null;
        try {
            res = countryJPA.findCountry(id);
        } catch (Exception ex) {
            Logger.getLogger(CountryService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }            
    
    public List<Country> buscarCountry(){
        List<Country> res = null;
        try {
            res = countryJPA.findCountryEntities();
        } catch (Exception ex) {
            Logger.getLogger(CountryService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    } 
    
    public List<Country> buscarCountry_x_name(String name){
        List<Country> res = null;
        try {
            res = countryJPA.findCountry_x_name(name);
        } catch (Exception ex) {
            Logger.getLogger(CountryService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    } 
}
