package spring.learn.jpa_hibernate.entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "course")
@NamedQuery(name = "Course.deleteById", query = "delete from Course where id=:id")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Course(String name) {
        this.name = name;
    }
}
