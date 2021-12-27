package spring.learn.jpa_hibernate.repository.relationship;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import spring.learn.jpa_hibernate.entity.relationship.Passport;
import spring.learn.jpa_hibernate.entity.relationship.Review;
import spring.learn.jpa_hibernate.entity.relationship.Student;
import spring.learn.jpa_hibernate.entity.relationship.Subject;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Repository
@Transactional
public class SubjectRepository {

    @PersistenceContext
    EntityManager em;

    public Subject findById(int id) {
        Subject subject = em.find(Subject.class, id);
        return subject;
    }

    /**
     * This is a different approach than usual setting !!!
     * <p> see {@link StudentRepository#saveWithPassport(Student, Passport)} for similar OneToOne approach
     */
    public void addReviewsToSubject(int subjectId, List<Review> reviews) {

        // `Review` owns `Subject`
        // So to persist a new review -> there should already be a subject in database

        Subject subject = findById(subjectId);
        // Luckily we have a subject, and we first retrieve it
        // If there is no subject -> First persist the subject, then do the following

        reviews.forEach(review -> {

            subject.addReview(review);
            // first associate review with subject
            // because subject -> review is OneToMany, and review is the OWNING part,
            // we have to first add review and then set the relationship on `review` as well -> because its the owner

            // setting the relationship on Review -> because it's the owner
            // in database terms: `reviews` table has foreign key `subject_id`
            // so foreign key must be set
            review.setSubject(subject);

            // now as for the last part -> we need to persist reviews
            // The class is already annotated with @Transactional and @PersistenceContext is used
            // and subject is already persisted beforehand and is now retrieved from the database
            // Therefore the `subject` is already tracked
            // But the newly added `reviews` are not yet tracked
            // So we need to manually persist them for the first time

            em.persist(review);

            // NOTE: We can even persist review first and later add it to subject and set the relationship
            // the order doesn't matter
            // because once persisted, review will be tracked as well -> hence further changes will be automatically persisted
            // in-fact we can do these 3 steps in ANY ORDER and result will be the same
        });
    }

    /**
     * @see StudentRepository#addSubjectsToStudents(int, List)
     */
    public void addStudentsToSubject(int subjectId, List<Integer> studentIds) {

        // 1. retrieve subject
        Subject subject = findById(subjectId);

        // 2. add students
        studentIds.forEach(id -> {

            // retrieve student
            Student student = em.find(Student.class, id);

            // 2.2 add student to subject
            subject.addStudent(student);

            // 2.3 add subject to student
            // must do both 2.1 and 2.2 because ManyToMany relationship
            student.addSubject(subject);

            // 2.1 persist student
            em.persist(student);
        });

        em.persist(subject);
    }
}