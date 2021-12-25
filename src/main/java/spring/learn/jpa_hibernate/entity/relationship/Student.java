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

    // create table students(
    //     id          int          not null auto_increment,
    //     name        varchar(100) not null,
    //     passport_id int          not null unique,
    //     primary key (id),
    //     foreign key (passport_id) references passports (id)
    // );

    // A TABLE (a) WHICH HAS FOREIGN KEY OF ANOTHER TABLE (b), OWNS THE OTHER TABLE (b) [`a` owns `b`]

    // because `students` table has `passport_id` as foreign key =>
    // `Student` now OWNS `Passport`
    // So, here it is annotated with @OneToOne -> but this is not for the case of `Passport`

    @OneToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Passport passport;

    public Student(String name) {
        this.name = name;
    }
}
