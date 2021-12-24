package spring.learn.jpa_hibernate.repository;

import org.springframework.stereotype.Repository;
import spring.learn.jpa_hibernate.entity.Person;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class PersonJpaRepository {

    // connects to database -> EntityManager is the interface to PersistenceContext.
    // PersistenceContext tracks each call and automatically changes any operations made on the object
    // https://www.baeldung.com/jpa-hibernate-persistence-context
    // https://stackoverflow.com/a/58891587/10582056
    @PersistenceContext
    EntityManager entityManager;

    public Person findById(int id) {
        return entityManager.find(Person.class, id);
    }

    public void deleteById(int id) {
        // first find the person
        Person person = findById(id);

        // then delete
        entityManager.remove(person);

        // one issue with this approach is that it uses 2 DB calls to do single deletion
        // But this is simple and easy to understand
        // Better approach would be to use named queries -> single call fo deletion
    }

    /**
     * it knows the id of person from object provided and if the id already exist, then do update.
     * otherwise, do an insert.
     * so for both `update` and `insert`, same method `merge`.
     */
    public Person update(Person person) {
        return entityManager.merge(person);
    }

    public Person insert(Person person) {
        return entityManager.merge(person);
    }

    /**
     * A different approach -> using JPQL.
     * The named query can be found on the entity to which this query is mapped to
     *
     * @see Person
     */
    public List<Person> findAll() {

        TypedQuery<Person> namedQuery = entityManager.createNamedQuery("find_all", Person.class);
        List<Person> persons = namedQuery.getResultList();

        return persons;
    }
}
