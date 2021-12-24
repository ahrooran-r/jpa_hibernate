package spring.learn.jpa_hibernate.repository;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import spring.learn.jpa_hibernate.JpaHibernateApplication;
import spring.learn.jpa_hibernate.entity.Course;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = JpaHibernateApplication.class)
class CourseRepositoryTests {

    @Autowired
    CourseJpaRepository courseRepository;

    @Test
    void contextLoads() {
        Course course = courseRepository.findById(1);
        assertEquals("Arts", course.getName());
    }

    /**
     * @ DirtiesContext -> resets the system to previous original state
     */
    @Test
    @DirtiesContext
    void deleteById_basic() {
        long id = 1L;
        courseRepository.deleteById(id);
        assertNull(courseRepository.findById(id));
    }
}
