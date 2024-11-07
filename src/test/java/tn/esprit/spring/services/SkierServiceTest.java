package tn.esprit.spring.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SkierServiceTest {

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private IPisteRepository pisteRepository;

    @Mock
    private ICourseRepository courseRepository;

    @Mock
    private IRegistrationRepository registrationRepository;

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SkierServicesImpl skierServicesImpl;

    private Skier skier;

    //Test retrieveAllSkiers
    @Test
    public void testRetrieveAllSkiers() {
        List<Skier> skierList = new ArrayList<>();
        skierList.add(skier);
        when(skierRepository.findAll()).thenReturn(skierList);

        List<Skier> result = skierServicesImpl.retrieveAllSkiers();

        assertEquals(1, result.size()); // Changed expected size to 1
        verify(skierRepository, times(1)).findAll();
    }

    //Test addSkier
    @Test
    public void testAddSkier() {
        Skier skier = new Skier();
        Subscription subscription = new Subscription();
        subscription.setTypeSub(TypeSubscription.ANNUAL);
        subscription.setStartDate(LocalDate.now());
        skier.setSubscription(subscription);

        when(skierRepository.save(skier)).thenReturn(skier);

        Skier result = skierServicesImpl.addSkier(skier);

        assertNotNull(result.getSubscription().getEndDate());
        assertEquals(LocalDate.now().plusYears(1), result.getSubscription().getEndDate());
        verify(skierRepository, times(1)).save(skier);
    }

    //Test assignSkierToSubscription
    @Test
    public void testAssignSkierToSubscription() {
        Skier skier = new Skier();
        Subscription subscription = new Subscription();

        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(subscriptionRepository.findById(2L)).thenReturn(Optional.of(subscription));
        when(skierRepository.save(any(Skier.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Return the updated skier after save

        Skier result = skierServicesImpl.assignSkierToSubscription(1L, 2L);

        assertNotNull(result, "The skier should not be null after assignment.");
        assertEquals(subscription, result.getSubscription(), "The subscription should be assigned to the skier.");
    }

    //Test addSkierAndAssignToCourse
    @Test
    public void testAddSkierAndAssignToCourse() {
        Skier skier = new Skier();
        Registration registration = new Registration();
        skier.setRegistrations(new HashSet<>(Collections.singletonList(registration)));
        Course course = new Course();

        when(skierRepository.save(skier)).thenReturn(skier);
        when(courseRepository.getById(1L)).thenReturn(course); // Ensure this aligns with method in service

        Skier result = skierServicesImpl.addSkierAndAssignToCourse(skier, 1L);

        assertNotNull(result);
        assertEquals(1, result.getRegistrations().size());
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }

    //Test removeSkier
    @Test
    public void testRemoveSkier() {
        skierServicesImpl.removeSkier(1L);

        verify(skierRepository, times(1)).deleteById(1L);
    }

    //Test retrieveSkier
    @Test
    public void testRetrieveSkier() {
        Skier skier = new Skier();
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));

        Skier result = skierServicesImpl.retrieveSkier(1L);

        assertEquals(skier, result);
        verify(skierRepository, times(1)).findById(1L);
    }

    //Test assignSkierToPiste
    @Test
    public void testAssignSkierToPiste() {
        Skier skier = new Skier();
        skier.setPistes(new HashSet<>());

        Piste piste = new Piste();

        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(pisteRepository.findById(2L)).thenReturn(Optional.of(piste));
        when(skierRepository.save(any(Skier.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Return the updated skier after save

        Skier result = skierServicesImpl.assignSkierToPiste(1L, 2L);

        assertNotNull(result, "The skier should not be null after assignment.");
        assertTrue(result.getPistes().contains(piste), "The piste should be assigned to the skier.");
    }

    //Test retrieveSkiersBySubscriptionType
    @Test
    public void testRetrieveSkiersBySubscriptionType() {
        List<Skier> skierList = new ArrayList<>();
        skierList.add(skier);
        when(skierRepository.findBySubscription_TypeSub(TypeSubscription.MONTHLY)).thenReturn(skierList);

        List<Skier> result = skierServicesImpl.retrieveSkiersBySubscriptionType(TypeSubscription.MONTHLY);

        assertEquals(1, result.size()); // Changed expected size to 1
        verify(skierRepository, times(1)).findBySubscription_TypeSub(TypeSubscription.MONTHLY);
    }


}
