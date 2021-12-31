package spring.learn.jpa_hibernate.entity.basics;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@ToString
@Getter
@Setter

// following are important annotations
@Cacheable // enables 2nd level cache
// @SQLDelete(sql = "update course set is_deleted=true where id=?") // Hibernate only -> not JPA
// // I can execute specific query when deleting or other such events instead of actually doing said event. This is called Soft Delete
// @Where(clause = "isDeleted=false") // when doing a `select *`, it only retrieves non-marked rows
@Entity
@Table(name = "course")
@NamedQueries(
        value = {
                @NamedQuery(name = "Course.deleteById", query = "delete from Course where id=:id"),
                @NamedQuery(name = "Course.findById", query = "select c from Course c where c.id=:id")
        }
)
// you cannot simply give multiple @NamedQuery one after the other
// hence do this work around
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @CreationTimestamp // Hibernate annotation -> there will be problems when migrating to another orm
    private LocalDateTime createdOn;

    @UpdateTimestamp // Hibernate annotation -> there will be problems when migrating to another orm
    private LocalDateTime lastUpdated;

    private boolean isDeleted;

    // you cannot add default values by this method: https://stackoverflow.com/a/2554796/10582056

    // Workaround to use JPA instead of hibernate: https://stackoverflow.com/a/42367173/10582056

    public Course(String name) {
        this.name = name;
    }

    public Course(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
