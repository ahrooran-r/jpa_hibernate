package spring.learn.jpa_hibernate.repository.relationship;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import spring.learn.jpa_hibernate.entity.relationship.Review;
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
     * <p> because here Subject is not the owning part and relationship is OneToMany
     */
    public void addReviewsToSubject(int subjectId, List<Review> reviews) {

        Subject subject = findById(subjectId);

        reviews.forEach(review -> {

            subject.addReview(review);
            // simply adding review to subject will not suffice
            // because subject -> review is OneToMany
            // and review is the OWNING part

            // so we need to explicitly set Subject in Review -> setting the relationship
            review.setSubject(subject);

            // now as for the last part -> we need to persist reviews
            // The class is already annotated with @Transactional and @PersistenceContext is used
            // Therefore the `subject` is already tracked
            // But the newly added `reviews` are not yet tracked
            // So we need to manually persist them for the first time

            em.persist(review);
        });
    }
}
