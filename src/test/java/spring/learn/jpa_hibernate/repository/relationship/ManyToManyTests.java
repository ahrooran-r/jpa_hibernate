package spring.learn.jpa_hibernate.repository.relationship;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import spring.learn.jpa_hibernate.JpaHibernateApplication;
import spring.learn.jpa_hibernate.entity.relationship.Review;
import spring.learn.jpa_hibernate.entity.relationship.Student;
import spring.learn.jpa_hibernate.entity.relationship.Subject;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = JpaHibernateApplication.class)
public class ManyToManyTests {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void test_addSubjectsToStudents() {

        int studentId = 20_001;

        Subject s1 = new Subject("Cooking");
        Subject s2 = new Subject("Music");
        List<Subject> subjects = Arrays.asList(s1, s2);

        studentRepository.addSubjectsToStudents(studentId, subjects);
    }

    @Test
    @Transactional
    public void getStudentAndSubjects() {

        // adding subjects to owning side student

        int id = 20_001;
        Student student = studentRepository.findById(id);

        log.info("Student = {}", student);

        List<Subject> subjects = student.getSubjects();
        log.info("Student = {}, taken subjects = {}", student, subjects);

        // NOTE: A practical problem I've encountered:
        // if you do not see your result after adding something, then better check whether you used @Transactional in excessive places
        // If in doubt: do a quick em.flush() and check whether if it persists.
        // If so, then you have added some additional unnecessary @Transactional
    }

    @Test
    public void test_addStudentsToSubject() {

        // adding students to non-owning side subject

        int subjectId = 10_002;

        List<Integer> studentIds = Arrays.asList(20_002, 20_003);

        subjectRepository.addStudentsToSubject(subjectId, studentIds);

        // NOTE: You can see this is a bit different.
        // Rather than creating student objects and feed them into subject class,
        // I am sending student ids
        // this is because for subject to add students -> all students must be
        // already associated with a passport (remember the constraints)
        // This gives us NO CHOICE but to
        //      1. Retrieve student from database
        //      2. Do operations on it
        //      3. Then persisting it again
        // This is impossible to do in 2 separate sessions -> can only be done in a single method

        // So instead of doing following approach:
        /*
            List<Student> students = Arrays.asList(em.find(Student.class, 20_002), em.find(Student.class, 20_003));
            subjectRepository.addStudentsToSubject(subjectId, students);
        */
        // Because, inner method has its own transaction, hence the retrieved list cannot be extended to inner method
        // So instead, I am directly passing student ids within the inner method
        // Now it works

        // So remember: whenever there is a need to feed a retrieved entity to another inner method,
        // the inner method should not have @Transactional of its own
        // Otherwise just send their ids only
        // https://stackoverflow.com/a/6222624/10582056
    }

    @Test
    @Transactional
    public void getSubjectsAndStudent() {

        int id = 10_002;
        Subject subject = subjectRepository.findById(id);

        log.info("Subject = {}", subject);

        List<Student> students = subject.getStudents();
        log.info("Subject = {}, taken students = {}", subject, students);
    }


    @Test
    // @DirtiesContext -> I'm not doing this because I want the changes made by this test to reflect on the next test below
    public void test_addReviewsToSubject() {

        int subjectId = 10_002;

        Review r1 = new Review(5, "Muah!!!");
        Review r2 = new Review(3, "Good enough!!!");
        List<Review> reviews = Arrays.asList(r1, r2);

        subjectRepository.addReviewsToSubject(subjectId, reviews);

    }

    @Test
    @Transactional
    public void getSubject_andReviews() {

        int id = 10_002;
        Subject subject = em.find(Subject.class, id);
        log.info("Subject = {}", subject);
        // I'm directly using entity manager rather than separate SubjectRepository

        // we need @Transactional for this -> because of LAZY fetch
        List<Review> reviews = subject.getReviews();
        log.info("Reviews = {}", reviews);
    }
}
