package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.spring.controllers.SubscriptionRestController;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.services.ISubscriptionServices;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class GestionStationSkiApplicationTests {

    private MockMvc mockMvc;

    @Mock
    private ISubscriptionServices subscriptionServices;

    @InjectMocks
    private SubscriptionRestController subscriptionRestController;

    private LocalDate startDate;
    private LocalDate endDate;
    private Subscription subscription1;
    private Subscription subscription2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(subscriptionRestController).build();

        // Initialize the start and end dates
        startDate = LocalDate.of(2024, 1, 1);
        endDate = LocalDate.of(2024, 12, 31);

        // Initialize the subscription objects
        subscription1 = new Subscription(1L, startDate, endDate, 100.0f, TypeSubscription.MONTHLY);
        subscription2 = new Subscription(2L, LocalDate.of(2024, 6, 15), endDate, 150.0f, TypeSubscription.SEMESTRIEL);
    }

    @Test
    void addSubscriptionTest() throws Exception {
        Subscription subscription = new Subscription();
        subscription.setNumSub(1L);  // numSub will be auto-generated
        subscription.setStartDate(LocalDate.of(2024, 10, 1)); // Set start date
        subscription.setEndDate(LocalDate.of(2024, 11, 1));   // Set end date
        subscription.setPrice(100.0f); // Set price
        subscription.setTypeSub(TypeSubscription.MONTHLY); // Assuming 'MONTHLY' is a valid type
    
        when(subscriptionServices.addSubscription(any(Subscription.class))).thenReturn(subscription);
    
        mockMvc.perform(post("/subscription/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"startDate\": \"2024-10-01\", \"endDate\": \"2024-11-01\", \"price\": 100.0, \"typeSub\": \"MONTHLY\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numSub").value(subscription.getNumSub()))
                .andExpect(jsonPath("$.price").value(100.0))
                .andExpect(jsonPath("$.typeSub").value("MONTHLY"))
                .andExpect(jsonPath("$.startDate").value("2024-10-01"))
                .andExpect(jsonPath("$.endDate").value("2024-11-01"));
    
        verify(subscriptionServices, times(1)).addSubscription(any(Subscription.class));
    }

    @Test
    void retrieveSubscriptionByIdTest() throws Exception {
        Subscription subscription = new Subscription(1L, LocalDate.now(), LocalDate.now().plusMonths(1), 100.0f, TypeSubscription.MONTHLY);
        when(subscriptionServices.retrieveSubscriptionById(1L)).thenReturn(subscription);

        mockMvc.perform(get("/subscription/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numSub").value(subscription.getNumSub()))
                .andExpect(jsonPath("$.price").value(subscription.getPrice()))
                .andExpect(jsonPath("$.typeSub").value(subscription.getTypeSub().name()));

        verify(subscriptionServices, times(1)).retrieveSubscriptionById(1L);
    }

    @Test
    void getSubscriptionByTypeTest() throws Exception {
        Subscription subscription = new Subscription(1L, LocalDate.now(), LocalDate.now().plusMonths(1), 100.0f, TypeSubscription.MONTHLY);
        Set<Subscription> subscriptions = Collections.singleton(subscription);
        when(subscriptionServices.getSubscriptionByType(TypeSubscription.MONTHLY)).thenReturn(subscriptions);

        mockMvc.perform(get("/subscription/all/MONTHLY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].numSub").value(subscription.getNumSub()))
                .andExpect(jsonPath("$[0].typeSub").value("MONTHLY"));

        verify(subscriptionServices, times(1)).getSubscriptionByType(TypeSubscription.MONTHLY);
    }

    @Test
    void updateSubscriptionTest() throws Exception {
        Subscription subscription = new Subscription(1L, LocalDate.now(), LocalDate.now().plusMonths(1), 120.0f, TypeSubscription.MONTHLY);
        when(subscriptionServices.updateSubscription(any(Subscription.class))).thenReturn(subscription);

        mockMvc.perform(put("/subscription/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"numSub\": 1, \"startDate\": \"2024-10-01\", \"endDate\": \"2024-11-01\", \"price\": 120.0, \"typeSub\": \"MONTHLY\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numSub").value(subscription.getNumSub()))
                .andExpect(jsonPath("$.price").value(120.0))
                .andExpect(jsonPath("$.typeSub").value("MONTHLY"));

        verify(subscriptionServices, times(1)).updateSubscription(any(Subscription.class));
    }
/*
    @Test
    public void testGetSubscriptionsByDates() throws Exception {
        // Mock the service to return a list of subscriptions between the given dates
        when(subscriptionServices.retrieveSubscriptionsByDates(startDate, endDate)).thenReturn(Arrays.asList(subscription1, subscription2));

        // Perform GET request to the endpoint with date parameters
        mockMvc.perform(get("/subscription/all/{date1}/{date2}", startDate, endDate))
                .andExpect(status().isOk())  // Check if the status is 200 OK
                .andExpect(jsonPath("$.length()").value(2))  // Check if two subscriptions are returned
                .andExpect(jsonPath("$[0].numSub").value(subscription1.getNumSub()))  // Check first subscription
                .andExpect(jsonPath("$[1].numSub").value(subscription2.getNumSub()));  // Check second subscription
    }*/

}
