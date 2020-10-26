
package persistencia;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import logica.City;
import logica.Country;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author Usuario
 */
public class CityJpaController implements Serializable {

    public CityJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public CityJpaController() {
        this.emf = Persistence.createEntityManagerFactory("entidadesJPA_PU");
    }      
    
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(City city) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Country countryCode = city.getCountryCode();
            if (countryCode != null) {
                countryCode = em.getReference(countryCode.getClass(), countryCode.getCode());
                city.setCountryCode(countryCode);
            }
            em.persist(city);
            if (countryCode != null) {
                countryCode.getCityList().add(city);
                countryCode = em.merge(countryCode);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(City city) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            City persistentCity = em.find(City.class, city.getId());
            Country countryCodeOld = persistentCity.getCountryCode();
            Country countryCodeNew = city.getCountryCode();
            if (countryCodeNew != null) {
                countryCodeNew = em.getReference(countryCodeNew.getClass(), countryCodeNew.getCode());
                city.setCountryCode(countryCodeNew);
            }
            city = em.merge(city);
            if (countryCodeOld != null && !countryCodeOld.equals(countryCodeNew)) {
                countryCodeOld.getCityList().remove(city);
                countryCodeOld = em.merge(countryCodeOld);
            }
            if (countryCodeNew != null && !countryCodeNew.equals(countryCodeOld)) {
                countryCodeNew.getCityList().add(city);
                countryCodeNew = em.merge(countryCodeNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = city.getId();
                if (findCity(id) == null) {
                    throw new NonexistentEntityException("The city with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            City city;
            try {
                city = em.getReference(City.class, id);
                city.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The city with id " + id + " no longer exists.", enfe);
            }
            Country countryCode = city.getCountryCode();
            if (countryCode != null) {
                countryCode.getCityList().remove(city);
                countryCode = em.merge(countryCode);
            }
            em.remove(city);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<City> findCityEntities() {
        return findCityEntities(true, -1, -1);
    }

    public List<City> findCityEntities(int maxResults, int firstResult) {
        return findCityEntities(false, maxResults, firstResult);
    }

    private List<City> findCityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(City.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public City findCity(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(City.class, id);
        } finally {
            em.close();
        }
    }

    public List<City> findCity_x_name(String as_name) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<City> consultaCiudad = em.createNamedQuery("City.findByName", City.class);
            consultaCiudad.setParameter("name", as_name);
            List<City> lista = consultaCiudad.getResultList();
            return lista;     
        } finally {
            em.close();
        }
    }

    
    public int getCityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<City> rt = cq.from(City.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
