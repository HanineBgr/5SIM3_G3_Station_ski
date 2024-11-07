package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@AllArgsConstructor
@Service
public class SkierServicesImpl implements ISkierServices {

    private ISkierRepository skierRepository;

    private IPisteRepository pisteRepository;

    private ICourseRepository courseRepository;

    private IRegistrationRepository registrationRepository;

    private ISubscriptionRepository subscriptionRepository;

    private static final Logger logger = LogManager.getLogger(SkierServicesImpl.class);


    @Override
    public List<Skier> retrieveAllSkiers() {
        logger.info("Retrieving all skiers.");
        List<Skier> skiers = skierRepository.findAll();
        logger.info("Retrieved {} skiers.", skiers.size());
        return skiers;
    }

    @Override
    public Skier addSkier(Skier skier) {
        logger.info("Adding skier with details: {}", skier);
        switch (skier.getSubscription().getTypeSub()) {
            case ANNUAL:
                skier.getSubscription().setEndDate(skier.getSubscription().getStartDate().plusYears(1));
                break;
            case SEMESTRIEL:
                skier.getSubscription().setEndDate(skier.getSubscription().getStartDate().plusMonths(6));
                break;
            case MONTHLY:
                skier.getSubscription().setEndDate(skier.getSubscription().getStartDate().plusMonths(1));
                break;
        }
        Skier savedSkier = skierRepository.save(skier);
        logger.info("Skier added with ID: {}", savedSkier.getNumSkier());
        return savedSkier;
    }

    @Override
    public Skier assignSkierToSubscription(Long numSkier, Long numSubscription) {
        logger.info("Assigning skier with ID: {} to subscription with ID: {}", numSkier, numSubscription);
        Skier skier = skierRepository.findById(numSkier).orElse(null);
        Subscription subscription = subscriptionRepository.findById(numSubscription).orElse(null);
        skier.setSubscription(subscription);
        Skier updatedSkier = skierRepository.save(skier);
        logger.info("Assigned subscription with ID: {} to skier with ID: {}", numSubscription, numSkier);
        return updatedSkier;
    }

    @Override
    public Skier addSkierAndAssignToCourse(Skier skier, Long numCourse) {
        logger.info("Adding skier and assigning to course with ID: {}", numCourse);
        Skier savedSkier = skierRepository.save(skier);
        Course course = courseRepository.getById(numCourse);
        Set<Registration> registrations = savedSkier.getRegistrations();
        for (Registration r : registrations) {
            r.setSkier(savedSkier);
            r.setCourse(course);
            registrationRepository.save(r);
        }
        logger.info("Skier added with ID: {} and assigned to course ID: {}", savedSkier.getNumSkier(), numCourse);
        return savedSkier;
    }

    @Override
    public void removeSkier(Long numSkier) {
        logger.info("Removing skier with ID: {}", numSkier);
        skierRepository.deleteById(numSkier);
        logger.info("Removed skier with ID: {}", numSkier);
    }

    @Override
    public Skier retrieveSkier(Long numSkier) {
        logger.info("Retrieving skier with ID: {}", numSkier);
        Skier skier = skierRepository.findById(numSkier).orElse(null);
        logger.info("Retrieved skier: {}", skier);
        return skier;
    }

    @Override
    public Skier assignSkierToPiste(Long numSkieur, Long numPiste) {
        logger.info("Assigning skier with ID: {} to piste with ID: {}", numSkieur, numPiste);
        Skier skier = skierRepository.findById(numSkieur).orElse(null);
        Piste piste = pisteRepository.findById(numPiste).orElse(null);
        try {
            skier.getPistes().add(piste);
        } catch (NullPointerException exception) {
            Set<Piste> pisteList = new HashSet<>();
            pisteList.add(piste);
            skier.setPistes(pisteList);
        }
        Skier updatedSkier = skierRepository.save(skier);
        logger.info("Assigned piste with ID: {} to skier with ID: {}", numPiste, numSkieur);
        return updatedSkier;
    }

    @Override
    public List<Skier> retrieveSkiersBySubscriptionType(TypeSubscription typeSubscription) {
        logger.info("Retrieving skiers by subscription type: {}", typeSubscription);
        List<Skier> skiers = skierRepository.findBySubscription_TypeSub(typeSubscription);
        logger.info("Retrieved {} skiers with subscription type: {}", skiers.size(), typeSubscription);
        return skiers;
    }
}
