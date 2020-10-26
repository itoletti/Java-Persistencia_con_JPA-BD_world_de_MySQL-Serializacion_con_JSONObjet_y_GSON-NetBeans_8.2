
package persistencia;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import logica.City;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import logica.Country;
import persistencia.exceptions.IllegalOrphanException;
import persistencia.exceptions.NonexistentEntityException;
import persistencia.exceptions.PreexistingEntityException;

/**
 *
 * @author Usuario
 */
public class CountryJpaController implements Serializable {

    public CountryJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;
    
    public CountryJpaController() {
        this.emf = Persistence.createEntityManagerFactory("entidadesJPA_PU");
    }
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Country country) throws PreexistingEntityException, Exception {
        if (country.getCityList() == null) {
            country.setCityList(new ArrayList<City>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<City> attachedCityList = new ArrayList<City>();
            for (City cityListCityToAttach : country.getCityList()) {
                cityListCityToAttach = em.getReference(cityListCityToAttach.getClass(), cityListCityToAttach.getId());
                attachedCityList.add(cityListCityToAttach);
            }
            country.setCityList(attachedCityList);
            em.persist(country);
            for (City cityListCity : country.getCityList()) {
                Country oldCountryCodeOfCityListCity = cityListCity.getCountryCode();
                cityListCity.setCountryCode(country);
                cityListCity = em.merge(cityListCity);
                if (oldCountryCodeOfCityListCity != null) {
                    oldCountryCodeOfCityListCity.getCityList().remove(cityListCity);
                    oldCountryCodeOfCityListCity = em.merge(oldCountryCodeOfCityListCity);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCountry(country.getCode()) != null) {
                throw new PreexistingEntityException("Country " + country + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Country country) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Country persistentCountry = em.find(Country.class, country.getCode());
            List<City> cityListOld = persistentCountry.getCityList();
            List<City> cityListNew = country.getCityList();
            List<String> illegalOrphanMessages = null;
            for (City cityListOldCity : cityListOld) {
                if (!cityListNew.contains(cityListOldCity)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain City " + cityListOldCity + " since its countryCode field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<City> attachedCityListNew = new ArrayList<City>();
            for (City cityListNewCityToAttach : cityListNew) {
                cityListNewCityToAttach = em.getReference(cityListNewCityToAttach.getClass(), cityListNewCityToAttach.getId());
                attachedCityListNew.add(cityListNewCityToAttach);
            }
            cityListNew = attachedCityListNew;
            country.setCityList(cityListNew);
            country = em.merge(country);
            for (City cityListNewCity : cityListNew) {
                if (!cityListOld.contains(cityListNewCity)) {
                    Country oldCountryCodeOfCityListNewCity = cityListNewCity.getCountryCode();
                    cityListNewCity.setCountryCode(country);
                    cityListNewCity = em.merge(cityListNewCity);
                    if (oldCountryCodeOfCityListNewCity != null && !oldCountryCodeOfCityListNewCity.equals(country)) {
                        oldCountryCodeOfCityListNewCity.getCityList().remove(cityListNewCity);
                        oldCountryCodeOfCityListNewCity = em.merge(oldCountryCodeOfCityListNewCity);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = country.getCode();
                if (findCountry(id) == null) {
                    throw new NonexistentEntityException("The country with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Country country;
            try {
                country = em.getReference(Country.class, id);
                country.getCode();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The country with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<City> cityListOrphanCheck = country.getCityList();
            for (City cityListOrphanCheckCity : cityListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Country (" + country + ") cannot be destroyed since the City " + cityListOrphanCheckCity + " in its cityList field has a non-nullable countryCode field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(country);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Country> findCountryEntities() {
        return findCountryEntities(true, -1, -1);
    }

    public List<Country> findCountryEntities(int maxResults, int firstResult) {
        return findCountryEntities(false, maxResults, firstResult);
    }

    private List<Country> findCountryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Country.class));
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

    public Country findCountry(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Country.class, id);
        } finally {
            em.close();
        }
    }

    public List<Country> findCountry_x_name (String as_name){
        EntityManager em = getEntityManager();
        TypedQuery<Country> consultaPais = em.createNamedQuery("Country.findByName", Country.class);
        consultaPais.setParameter("name", as_name);
        List<Country> lista = consultaPais.getResultList();
        return lista;
    }
    
    public int getCountryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Country> rt = cq.from(Country.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
