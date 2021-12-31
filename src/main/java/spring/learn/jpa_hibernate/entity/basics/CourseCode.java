package spring.learn.jpa_hibernate.entity.basics;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@AllArgsConstructor
@Embeddable
@NoArgsConstructor
public class CourseCode {
    private String prefix;
    private String number;

    @Override
    public String toString() {
        return prefix + number;
    }
}
