package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.repository.SubscriptionRepository;
import tn.esprit.spring.services.SubscriptionServiceImpl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GestionStationSkiApplicationTests {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;

    private Subscription subscription;
    private List<Subscription> subscriptionList;

    @BeforeEach
    void setUp() {
        // Initialize a single Subscription object
        subscription = new Subscription();
        subscription.setNumSub(1L);
        subscription.setStartDate(LocalDate.parse("2024-10-01"));
        subscription.setEndDate(LocalDate.parse("2024-11-01"));
        subscription.setPrice(100.0f);
        subscription.setTypeSub(TypeSubscription.MONTHLY);

        // Initialize list of Subscription objects
        Subscription subscription1 = new Subscription(2L, LocalDate.parse("2024-05-01"), LocalDate.parse("2024-06-01"), 150.0f, TypeSubscription.MONTHLY);
        Subscription subscription2 = new Subscription(3L, LocalDate.parse("2024-07-01"), LocalDate.parse("2024-08-01"), 200.0f, TypeSubscription.YEARLY);

        subscriptionList = Arrays.asList(subscription1, subscription2);
    }

    @Test
    void testRetrieveAllSubscriptions() {
        // Arrange
        when(subscriptionRepository.findAll()).thenReturn(subscriptionList);

        // Act
        List<Subscription> result = subscriptionService.retrieveAllSubscriptions();

        // Assert
        assertEquals(2, result.size());
        assertEquals(150.0f, result.get(0).getPrice());
        verify(subscriptionRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveSubscriptionById() {
        // Arrange
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));

        // Act
        Subscription result = subscriptionService.retrieveSubscriptionById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(TypeSubscription.MONTHLY, result.getTypeSub());
        verify(subscriptionRepository, times(1)).findById(1L);
    }

    @Test
    void testAddSubscription() {
        // Arrange
        when(subscriptionRepository.save(subscription)).thenReturn(subscription);

        // Act
        Subscription result = subscriptionService.addSubscription(subscription);

        // Assert
        assertNotNull(result);
        assertEquals(100.0f, result.getPrice());
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    void testUpdateSubscription() {
        // Arrange
        subscription.setPrice(120.0f);
        when(subscriptionRepository.save(subscription)).thenReturn(subscription);

        // Act
        Subscription result = subscriptionService.updateSubscription(subscription);

        // Assert
        assertNotNull(result);
        assertEquals(120.0f, result.getPrice());
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    void testRemoveSubscription() {
        // Arrange
        Long subscriptionId = 1L;
        doNothing().when(subscriptionRepository).deleteById(subscriptionId);

        // Act
        subscriptionService.removeSubscription(subscriptionId);

        // Assert
        verify(subscriptionRepository, times(1)).deleteById(subscriptionId);
    }

    @Test
    void testGetSubscriptionsByType() {
        // Arrange
        when(subscriptionRepository.findSubscriptionsByType(TypeSubscription.MONTHLY)).thenReturn(subscriptionList);

        // Act
        List<Subscription> result = subscriptionService.getSubscriptionsByType(TypeSubscription.MONTHLY);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(TypeSubscription.MONTHLY, result.get(0).getTypeSub());
        verify(subscriptionRepository, times(1)).findSubscriptionsByType(TypeSubscription.MONTHLY);
    }

    @Test
    void testRetrieveSubscriptionsByDates() {
        // Arrange
        when(subscriptionRepository.findSubscriptionsBetweenDates(any(LocalDate.class), any(LocalDate.class))).thenReturn(subscriptionList);

        // Act
        List<Subscription> result = subscriptionService.retrieveSubscriptionsByDates(LocalDate.parse("2024-01-01"), LocalDate.parse("2024-12-31"));

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(subscriptionRepository, times(1)).findSubscriptionsBetweenDates(any(LocalDate.class), any(LocalDate.class));
    }
}
