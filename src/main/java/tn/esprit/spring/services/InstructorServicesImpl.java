package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IInstructorRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class InstructorServicesImpl implements IInstructorServices {

    private static final Logger logger = LogManager.getLogger(InstructorServicesImpl.class);

    private IInstructorRepository instructorRepository;
    private ICourseRepository courseRepository;

    @Override
    public Instructor addInstructor(Instructor instructor) {
        logger.info("Ajout de l'instructeur: {}", instructor.getFirstName());
        Instructor savedInstructor = instructorRepository.save(instructor);
        logger.info("Instructeur ajouté avec succès: {}", savedInstructor);
        return savedInstructor;
    }

    @Override
    public List<Instructor> retrieveAllInstructors() {
        logger.info("Récupération de tous les instructeurs");
        List<Instructor> instructors = instructorRepository.findAll();
        logger.info("Nombre d'instructeurs récupérés: {}", instructors.size());
        return instructors;
    }

    @Override
    public Instructor updateInstructor(Instructor instructor) {
        logger.info("Mise à jour de l'instructeur avec ID: {}", instructor.getNumInstructor());
        Instructor updatedInstructor = instructorRepository.save(instructor);
        logger.info("Instructeur mis à jour avec succès: {}", updatedInstructor);
        return updatedInstructor;
    }

    @Override
    public Instructor retrieveInstructor(Long numInstructor) {
        logger.info("Récupération de l'instructeur avec ID: {}", numInstructor);
        Optional<Instructor> instructorOpt = instructorRepository.findById(numInstructor);
        if (instructorOpt.isPresent()) {
            logger.info("Instructeur trouvé: {}", instructorOpt.get());
            return instructorOpt.get();
        } else {
            logger.warn("Aucun instructeur trouvé avec ID: {}", numInstructor);
            return null;
        }
    }

    @Override
    public Instructor addInstructorAndAssignToCourse(Instructor instructor, Long numCourse) {
        logger.info("Ajout de l'instructeur: {} et attribution au cours ID: {}", instructor.getFirstName(), numCourse);
        Course course = courseRepository.findById(numCourse).orElse(null);
        if (course == null) {
            logger.error("Aucun cours trouvé avec ID: {}", numCourse);
            return null;
        }
        Set<Course> courseSet = new HashSet<>();
        courseSet.add(course);
        instructor.setCourses(courseSet);
        Instructor savedInstructor = instructorRepository.save(instructor);
        logger.info("Instructeur ajouté et assigné avec succès: {}", savedInstructor);
        return savedInstructor;
    }
}
