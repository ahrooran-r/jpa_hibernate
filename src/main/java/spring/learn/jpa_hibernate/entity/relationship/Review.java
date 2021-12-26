package spring.learn.jpa_hibernate.entity.relationship;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
// ---------------------------
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private int rating;

    @Column(length = 250)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    // by default fetch type is EAGER
    @ToString.Exclude
    private Subject subject;

    // Many Reviews map to one course
    // If you take a look at SQL for Review -> relationship.sql
    // you can see `reviews` table has `foreign key` attribute => hence it is the owning part in this relationship
    // so `mapped by` should be added to Subject


    public Review(int rating, String description) {
        this.rating = rating;
        this.description = description;
    }
}
