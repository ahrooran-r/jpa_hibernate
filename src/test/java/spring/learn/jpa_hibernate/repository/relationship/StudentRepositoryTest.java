package spring.learn.jpa_hibernate.repository.relationship;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import spring.learn.jpa_hibernate.JpaHibernateApplication;
import spring.learn.jpa_hibernate.entity.relationship.Student;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = JpaHibernateApplication.class)
public class StudentRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    StudentRepository studentDao;

    @Test
    public void eagerFetchExample() {

        // make sure
        // @OneToOne(fetch = FetchType.EAGER)
        // private Passport passport;
        // EAGER mode is enabled on `Student` class: `passport`

        int id = 20_004;
        Student student = studentDao.findById(id);
        log.info("student with id: {} -> {}", id, student);
        log.info("passport -> {}", student.getPassport());

        // Any ONE-TO-ONE relationship is `eager fetch` by default
        // both student details and passport details are retrieved when findById() is fired
        // This may have performance issues if you don't want the additional passport object

        // to do lazy retrieval -> do @OneToOne(fetch = FetchType.LAZY) on `passport` attribute
        // if we run this method again: LazyInitializationException will be thrown when trying to get `student.getPassport()`
    }

    @Test
    public void lazyFetchExample_throwsException() {

        // make sure
        // @OneToOne(fetch = FetchType.LAZY)
        // @ToString.Exclude
        // private Passport passport;
        // LAZY mode is enabled on `Student` class: `passport`

        int id = 20_004;
        Student student = studentDao.findById(id);
        log.info("student with id: {} -> {}", id, student);
        log.info("passport -> {}", student.getPassport());

        // if we run this method: LazyInitializationException will be thrown when trying to get `student.getPassport()`
        // error:  `could not initialize proxy - no Session`

        // what happens is session opened before this line -> `studentDao.findById(id);`
        // and closed as soon as `student` object is retrieved

        // so when we execute `student.getPassport()` -> no db connection is made because no session is there

        // see on next method ...
    }

    @Transactional
    @Test
    public void lazyFetchExampleWorking() {

        int id = 20_004;
        Student student = studentDao.findById(id);
        log.info("student with id: {} -> {}", id, student);
        log.info("passport -> {}", student.getPassport());

        // to remove this problem: we have to do `@Transactional` on the method when do `LAZY FETCH`
        // `@Transactional` open the session when method starts and closes session only when method ends
        // so any LAZY calls within that method (aka while session is on) will be executed by JPA
    }

}
