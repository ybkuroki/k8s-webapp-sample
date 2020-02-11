package managesys.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class BookRepository extends AbstractRepository {

    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager em;

    @Override
    public EntityManager em() {
        return em;
    }

}
