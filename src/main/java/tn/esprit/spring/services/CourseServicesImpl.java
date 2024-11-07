package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.repositories.ICourseRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class CourseServicesImpl implements ICourseServices {

    private static final Logger logger = LogManager.getLogger(CourseServicesImpl.class);
    private ICourseRepository courseRepository;

    @Override
    public List<Course> retrieveAllCourses() {
        logger.info("Retrieving all courses");
        return courseRepository.findAll();
    }

    @Override
    public Course addCourse(Course course) {
        logger.debug("Adding course: {}", course);
        Course savedCourse = courseRepository.save(course);
        logger.info("Course added successfully with ID: {}", savedCourse.getNumCourse());
        return savedCourse;
    }

    @Override
    public Course updateCourse(Course course) {
        logger.debug("Updating course with ID: {}", course.getNumCourse());
        Course updatedCourse = courseRepository.save(course);
        logger.info("Course updated successfully with ID: {}", updatedCourse.getNumCourse());
        return updatedCourse;
    }

    @Override
    public Course retrieveCourse(Long numCourse) {
        logger.debug("Retrieving course with ID: {}", numCourse);
        return courseRepository.findById(numCourse).orElse(null);
    }
}
