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

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = JpaHibernateApplication.class)
public class StudentRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    StudentRepository studentDao;

    @Test
    public void lazyFetchExample() {
        int id = 20_004;
        Student student = studentDao.findById(id);
        log.info("student with id: {} -> {}", id, student);
        log.info("passport -> {}", student.getPassport());

        // As you can see, any ONE-TO-ONE relationship is `eager fetch`
        // both student details and passport details are retrieved when findById() is fired
    }
}
