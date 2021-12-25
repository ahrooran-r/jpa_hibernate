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
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    // check relationship.sql
    // because `students` table has passport id, it now owns `passports`
    @OneToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Passport passport;

    public Student(String name) {
        this.name = name;
    }
}
