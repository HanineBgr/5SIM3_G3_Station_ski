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
import java.util.List;
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

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(subscriptionRestController).build();
    }

    @Test
    void addSubscriptionTest() throws Exception {
        // Create a subscription object with appropriate data
        Subscription subscription = new Subscription();
        subscription.setNumSub(1L);  // numSub will be auto-generated
        subscription.setStartDate(LocalDate.of(2024, 10, 1)); // Set start date
        subscription.setEndDate(LocalDate.of(2024, 11, 1));   // Set end date
        subscription.setPrice(100.0f); // Set price
        subscription.setTypeSub(TypeSubscription.MONTHLY); // Assuming 'MONTHLY' is a valid type
    
        // Mock the service call to return the subscription object
        when(subscriptionServices.addSubscription(any(Subscription.class))).thenReturn(subscription);
    
        // Perform the POST request and validate the response
        mockMvc.perform(post("/subscription/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"startDate\": \"2024-10-01\", \"endDate\": \"2024-11-01\", \"price\": 100.0, \"typeSub\": \"MONTHLY\"}"))
                .andExpect(status().isOk())  // Check for successful status code
                .andExpect(jsonPath("$.numSub").value(subscription.getNumSub()))  // Verify numSub is returned
                .andExpect(jsonPath("$.price").value(100.0))  // Verify price is returned correctly
                .andExpect(jsonPath("$.typeSub").value("MONTHLY"))  // Verify subscription type
                .andExpect(jsonPath("$.startDate").value("2024-10-01"))  // Verify correct start date format
                .andExpect(jsonPath("$.endDate").value("2024-11-01"));  // Verify correct end date format
    
        // Verify that the addSubscription method was called once
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

    @Test
    public void retrieveSubscriptionsByDatesTest() throws Exception {
        // Correct date format: yyyy-MM-dd
        String startDate = "2024-10-01";
        String endDate = "2024-10-31";
        
        mockMvc.perform(get("/subscription/all/{date1}/{date2}", startDate, endDate))
               .andExpect(status().isOk()) // Expect 200 OK
               .andExpect(jsonPath("$[0].startDate").value("2024-10-01")) // Check the startDate of the first subscription
               .andExpect(jsonPath("$[0].endDate").value("2024-10-31"))   // Check the endDate of the first subscription
               .andExpect(jsonPath("$[0].price").value(100.0))             // Adjust the price based on your mock data
               .andExpect(jsonPath("$[0].typeSub").value("MONTHLY"));      // Adjust based on your mock data
    }

}
