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
        Subscription subscription = new Subscription();
        when(subscriptionServices.addSubscription(any(Subscription.class))).thenReturn(subscription);

        mockMvc.perform(post("/subscription/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"startDate\": \"2024-10-01\", \"endDate\": \"2024-11-01\", \"price\": 100.0, \"typeSub\": \"MONTHLY\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numSub").value(subscription.getNumSub()))
                .andExpect(jsonPath("$.price").value(100.0))
                .andExpect(jsonPath("$.typeSub").value("MONTHLY"));

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
    void retrieveSubscriptionsByDatesTest() throws Exception {
        Subscription subscription = new Subscription(1L, LocalDate.now(), LocalDate.now().plusMonths(1), 100.0f, TypeSubscription.MONTHLY);
        List<Subscription> subscriptions = Arrays.asList(subscription);
        when(subscriptionServices.retrieveSubscriptionsByDates(any(LocalDate.class), any(LocalDate.class))).thenReturn(subscriptions);

        mockMvc.perform(get("/subscription/all/2024-01-01/2024-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].numSub").value(subscription.getNumSub()))
                .andExpect(jsonPath("$[0].typeSub").value("MONTHLY"));

        verify(subscriptionServices, times(1)).retrieveSubscriptionsByDates(any(LocalDate.class), any(LocalDate.class));
    }
}
