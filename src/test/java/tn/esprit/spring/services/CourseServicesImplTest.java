package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.repositories.ICourseRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class CourseServicesImplTest {

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private CourseServicesImpl courseServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRetrieveAllCourses() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course()); // Add dummy course data if necessary
        when(courseRepository.findAll()).thenReturn(courses);

        List<Course> retrievedCourses = courseServices.retrieveAllCourses();
        assertNotNull(retrievedCourses);
        assertEquals(1, retrievedCourses.size()); // Adjust if you add more dummy data
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    public void testAddCourse() {
        Course course = new Course();
        when(courseRepository.save(course)).thenReturn(course);

        Course addedCourse = courseServices.addCourse(course);
        assertNotNull(addedCourse);
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    public void testUpdateCourse() {
        Course course = new Course();
        course.setNumCourse(1L);
        when(courseRepository.save(course)).thenReturn(course);

        Course updatedCourse = courseServices.updateCourse(course);
        assertNotNull(updatedCourse);
        assertEquals(1L, updatedCourse.getNumCourse());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    public void testRetrieveCourse() {
        Long courseId = 1L;
        Course course = new Course();
        course.setNumCourse(courseId);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        Course retrievedCourse = courseServices.retrieveCourse(courseId);
        assertNotNull(retrievedCourse);
        assertEquals(courseId, retrievedCourse.getNumCourse());
        verify(courseRepository, times(1)).findById(courseId);
    }
}
