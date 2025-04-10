package com.example.COSC2626_A1.service;

import com.example.COSC2626_A1.model.Subscription;
import com.example.COSC2626_A1.repository.SubscriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SubscriptionService {
    final private SubscriptionRepository subscriptionRepository;

    public List<Subscription> getAllSubscriptions(){
        return subscriptionRepository.getAllSubscriptions();
    }

    public Subscription getSubscription(String email){
        return subscriptionRepository.getSubscription(email);
    }

    public void addSubscription(Subscription subscription){
        subscriptionRepository.addSubscription(subscription);
    }

    public Subscription updateSubscription(String email, Subscription subscription){
        return subscriptionRepository.updateSubscription(email, subscription);
    }

}
