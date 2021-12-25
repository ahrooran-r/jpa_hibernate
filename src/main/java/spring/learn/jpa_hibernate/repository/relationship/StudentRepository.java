package spring.learn.jpa_hibernate.repository.relationship;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import spring.learn.jpa_hibernate.entity.relationship.Passport;
import spring.learn.jpa_hibernate.entity.relationship.Student;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Slf4j
@Repository
@Transactional
public class StudentRepository {

    @PersistenceContext
    EntityManager em;

    public Student findById(int id) {
        Student student = em.find(Student.class, id);
        return student;
    }

    public Student save(Student student) {
        if (student.getId() == null) em.persist(student);
        else em.merge(student);
        return student;
    }

    public Student saveWithPassport() {

        // As you see in Student.class, it owns the passport.
        // So in order to save a student in database, there should already be a passport saved in `passports` table

        // 1. So a newly created passport must be first persisted onto database
        Passport passport = new Passport("Z23456");
        em.persist(passport);

        // 2. Then associate passport with student
        Student student = new Student("Jumbo");
        student.setPassport(passport);

        // 3. Now persist the student
        em.persist(student);

        return student;
    }
}
