package spring.learn.jpa_hibernate.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * @ Entity name is used to refer your entity throughout the application, notably in HQL queries
 * <p>
 * @ Table is the actual DB table name
 * <p>
 * <a href=https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-reference-type-conversions.html>Look here</a> for data conversions
 */
@Entity(name = "Person")
@Table(name = "person")
@NamedQuery(name = "find_all", query = "select p from Person p")
// here `Person` refers to `name` given in @Entity annotation -> @Entity(name = "Person")
// note that JPQL is slightly different from MySQL
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // This works when `auto_increment` in mysql is enabled: https://stackoverflow.com/q/20603638/10582056
    @Column(name = "id")
    private int id;

    // here both name in class and actual column name match
    // so no need to manually specify
    // but in case where table names are different, then we can give some custom name in class
    // and use @Column to map the attribute to the table
    // for example
    // @Column(name = "name")
    // private String personName;
    // This allows attribute `personName` to map with table column `name`
    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "birth_date")
    private Date birthDate;

    /**
     * Every entity should have a no-args constructor
     */
    public Person() {
    }

    public Person(String name, String location, Date birthDate) {
        this.name = name;
        this.location = location;
        this.birthDate = birthDate;
    }

    public Person(int id, String name, String location, Date birthDate) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.birthDate = birthDate;
    }

    public String toString() {
        return "Person(id=" + this.id + ", name=" + this.name + ", location=" + this.location + ", birthDate=" + this.birthDate + ")";
    }
}
