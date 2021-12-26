package spring.learn.jpa_hibernate.repository.relationship;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import spring.learn.jpa_hibernate.entity.relationship.Review;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Slf4j
@Repository
@Transactional
public class ReviewRepository {

    @PersistenceContext
    private EntityManager em;

    public Review findById(int id) {
        Review review = em.find(Review.class, id);
        return review;
    }
}
