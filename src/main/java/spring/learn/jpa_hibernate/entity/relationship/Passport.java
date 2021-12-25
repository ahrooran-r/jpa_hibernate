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
@Entity
@Table(name = "passports")
public class Passport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // always specify what type of strategy is used
    private Integer id;

    @Column(nullable = false, unique = true)
    private String number;

    public Passport(String number) {
        this.number = number;
    }
}
