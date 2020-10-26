package persistencia;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import logica.City;

public class CityService {
    CityJpaController cityJPA = new CityJpaController();
    public void crearCity(City ciudad){
        try {
            cityJPA.create(ciudad);
        } catch (Exception ex) {
            Logger.getLogger(CountryService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminarCity(int id){
        try {
            cityJPA.destroy(id);
        } catch (Exception ex) {
            Logger.getLogger(CountryService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void actualizarCity(City ciudad){
        try {
            cityJPA.edit(ciudad);
        } catch (Exception ex) {
            Logger.getLogger(CountryService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    public City buscarCity(int id){
        City res = null;
        try {
            res = cityJPA.findCity(id);
        } catch (Exception ex) {
            Logger.getLogger(CountryService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }            

    public List<City> buscarCity_x_nombre(String as_name){
        List<City> res = null;
        try {
            res = cityJPA.findCity_x_name(as_name);
        } catch (Exception ex) {
            Logger.getLogger(CountryService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    } 
    
    public List<City> buscarCity(){
        List<City> res = null;
        try {
            res = cityJPA.findCityEntities();
        } catch (Exception ex) {
            Logger.getLogger(CountryService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    } 
}    

