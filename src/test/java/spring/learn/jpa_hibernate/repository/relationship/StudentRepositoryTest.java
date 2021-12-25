package spring.learn.jpa_hibernate.repository.relationship;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import spring.learn.jpa_hibernate.JpaHibernateApplication;
import spring.learn.jpa_hibernate.entity.relationship.Passport;
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

    @Transactional
    @Test
    public void transactionalInDepth() {

        // Transactions work like this -> either do all or do nothing
        // so if in middle, something fails or some exception is thrown -> then everything including
        // all previous success operations will `roll back`

        // The moment I call `@Transactional` -> another thing `@PersistenceContext` will also be created
        // The PersistenceContext has all the entities which are being executed upon by the method

        // we access PersistenceContext through EntityManager
        // -> this is why it is annotated with `@PersistenceContext EntityManager em;`

        // If there is no @Transactional is added to the method ->
        // then all 1, 2, 3, 4 parts in this method will act as separate transactions
        // In that case 2nd part will throw exception because now `student` and `passport` are separated.
        // So `student` will have separate PersistenceContext and `passport` will have separate PersistenceContext
        // Then there will not be any communication between student and passport and hence exception thrown

        // NOTE 1: in hibernate terminology: `session` = PersistenceContext
        // so if error says there is `no session` -> then probably `@Transactional` is missing for the method,
        // because as I previously said, @Transactional is the one holding all entities inside the method
        // within one single PersistenceContext

        // NOTE 2: @Transactional can go within inner methods too. So, consider below example
        // @Transactional
        // public void outerMethod() {
        //     Object x = someInnerMethod();
        // }
        // We don't need to annotate `someInnerMethod()` with `@Transactional`
        // because it is already applied to `outerMethod()`
        // This is true for classes as well. If a class is annotated with @Transactional
        // -> all methods within class are good to go!


        int id = 20_004;

        // 1. Retrieve student - using em
        Student student = em.find(Student.class, id);
        // now PersistenceContext = {student}

        // 2. Retrieve passport
        Passport passport = student.getPassport();
        // now PersistenceContext = {student, passport}
        // if we add any other entity, then that will also be added to `PersistenceContext`

        // 3. update passport
        passport.setNumber("I95123");
        // now PersistenceContext = {student, passport_modified}

        // 4. update student
        String newName = String.format("%s - UPDATED", student.getName());
        student.setName(newName);
        // now PersistenceContext = {student_modified, passport_modified}

        // Only after this `method` aka `transaction` is completed, the changes are sent to the database aka `committed`
        // until then they all reside within `PersistenceContext`
    }

    // @Transactional
    @Test
    public void transactionalOnClassSupport() {

        // Remember NOTE 2 from before
        // I have commented out @Transactional annotation from this method

        int id = 20_004;

        Student student = studentDao.findById(id);

        String newName = String.format("%s - DOUBLE-UPDATED", student.getName());
        student.setName(newName);

        log.info("STUDENT -> {}", student);

        // You will see that even though there is no @Transactional -> student is tracked and log prints updated value
        // This is because the transaction support comes from `StudentRepository`
        // if you go to the class `StudentRepository` -> you'll see that the class itself is annotated with @Transactional
        // Hence `student` object will be tracked cause `studentDao.findById(id)` is from `StudentRepository`

        // But as soon as we try to access passport, with `student.getPassport()` -> it will fail
        // because this method is from `Student` class and that is not annotated with @Transactional
        // Hence following calls will throw exception

        Passport passport = student.getPassport();
        passport.setNumber("I95123");
    }

    /**
     * Testing relationship from passport -> student
     */
    @Transactional
    @Test
    public void callingStudentFromPassport() {
        int id = 30_002;

        Passport passport = em.find(Passport.class, id);
        log.info("Passport = {}", passport);

        // call student from passport
        Student student = passport.getStudent();
        log.info("Calling Student from Passport -> {}", student);

    }
}
