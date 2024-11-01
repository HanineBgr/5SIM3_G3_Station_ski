package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.services.ISubscriptionServices;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Tag(name = "\uD83D\uDC65 Subscription Management")
@RestController
@RequestMapping("/subscription")
@RequiredArgsConstructor
public class SubscriptionRestController {
    private static final Logger logger = LogManager.getLogger(SubscriptionRestController.class);
    private final ISubscriptionServices subscriptionServices;

    @Operation(description = "Add Subscription ")
    @PostMapping("/add")
    public Subscription addSubscription(@RequestBody Subscription subscription){
        logger.info("Adding subscription: {}", subscription);
        Subscription addedSubscription = subscriptionServices.addSubscription(subscription);
        logger.info("Subscription added: {}", addedSubscription);
        return addedSubscription;
    }

    @Operation(description = "Retrieve Subscription by Id")
    @GetMapping("/get/{id-subscription}")
    public Subscription getById(@PathVariable("id-subscription") Long numSubscription){
        logger.info("Retrieving subscription with ID: {}", numSubscription);
        Subscription subscription = subscriptionServices.retrieveSubscriptionById(numSubscription);
        logger.info("Retrieved subscription: {}", subscription);
        return subscription;
    }
    
    @Operation(description = "Retrieve Subscriptions by Type")
    @GetMapping("/all/{typeSub}")
    public Set<Subscription> getSubscriptionsByType(@PathVariable("typeSub")TypeSubscription typeSubscription){
        logger.info("Retrieving subscriptions of type: {}", typeSubscription);
        Set<Subscription> subscriptions = subscriptionServices.getSubscriptionByType(typeSubscription);
        logger.info("Retrieved {} subscriptions of type: {}", subscriptions.size(), typeSubscription);
        return subscriptions;
    }

    @Operation(description = "Update Subscription ")
    @PutMapping("/update")
    public Subscription updateSubscription(@RequestBody Subscription subscription){
        logger.info("Updating subscription: {}", subscription);
        Subscription updatedSubscription = subscriptionServices.updateSubscription(subscription);
        logger.info("Updated subscription: {}", updatedSubscription);
        return updatedSubscription;
    }

    @Operation(description = "Retrieve Subscriptions created between two dates")
    @GetMapping("/all/{date1}/{date2}")
    public List<Subscription> getSubscriptionsByDates(@PathVariable("date1") LocalDate startDate,
                                                      @PathVariable("date2") LocalDate endDate){
        logger.info("Retrieving subscriptions created between {} and {}", startDate, endDate);
        List<Subscription> subscriptions = subscriptionServices.retrieveSubscriptionsByDates(startDate, endDate);
        logger.info("Retrieved {} subscriptions between {} and {}", subscriptions.size(), startDate, endDate);
        return subscriptions;
    }

}
