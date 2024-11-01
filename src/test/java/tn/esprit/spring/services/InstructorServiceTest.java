package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.repositories.IInstructorRepository;
import tn.esprit.spring.repositories.ICourseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InstructorServiceTest {

    @Mock
    private IInstructorRepository instructorRepository;

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private InstructorServicesImpl instructorService;

    // Sample instructor data for testing
    private Instructor instructor;

    @BeforeEach
    void setUp() {
        instructor = new Instructor();
        instructor.setNumInstructor(1L);
        instructor.setFirstName("Hanine");
        instructor.setLastName("Bouguerra");
        // Set other fields as necessary
    }

    @Test
    void testAddInstructor() {
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        Instructor savedInstructor = instructorService.addInstructor(instructor);

        assertNotNull(savedInstructor);
        assertEquals("Hanine", savedInstructor.getFirstName());
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    void testRetrieveAllInstructors() {
        List<Instructor> instructorList = new ArrayList<>();
        instructorList.add(instructor);

        when(instructorRepository.findAll()).thenReturn(instructorList);

        List<Instructor> retrievedInstructors = instructorService.retrieveAllInstructors();

        assertNotNull(retrievedInstructors);
        assertEquals(1, retrievedInstructors.size());
        verify(instructorRepository, times(1)).findAll();
    }

    @Test
    void testUpdateInstructor() {
        when(instructorRepository.save(instructor)).thenReturn(instructor);

        Instructor updatedInstructor = instructorService.updateInstructor(instructor);

        assertNotNull(updatedInstructor);
        assertEquals("Hanine", updatedInstructor.getFirstName());
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    void testRetrieveInstructor() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));

        Instructor foundInstructor = instructorService.retrieveInstructor(1L);

        assertNotNull(foundInstructor);
        assertEquals("Hanine", foundInstructor.getFirstName());
        verify(instructorRepository, times(1)).findById(1L);
    }

    @Test
    void testRetrieveInstructorNotFound() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.empty());

        Instructor foundInstructor = instructorService.retrieveInstructor(1L);

        assertNull(foundInstructor);
        verify(instructorRepository, times(1)).findById(1L);
    }

    @Test
    void testAddInstructorAndAssignToCourse() {
        Long courseId = 1L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(new Course())); // Mocking a course
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        Instructor savedInstructor = instructorService.addInstructorAndAssignToCourse(instructor, courseId);

        assertNotNull(savedInstructor);
        verify(courseRepository, times(1)).findById(courseId);
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    void testAddInstructorAndAssignToCourseCourseNotFound() {
        Long courseId = 1L; // Example course ID
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        Instructor savedInstructor = instructorService.addInstructorAndAssignToCourse(instructor, courseId);

        assertNull(savedInstructor);
        verify(courseRepository, times(1)).findById(courseId);
        verify(instructorRepository, never()).save(any(Instructor.class)); // Ensure save is never called
    }
}
