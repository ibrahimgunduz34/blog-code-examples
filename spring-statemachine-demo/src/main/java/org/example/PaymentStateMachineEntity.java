package org.example;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_state_machine")
public class PaymentStateMachineEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long paymentId;

    @Enumerated(EnumType.STRING)
    private PaymentStates state;

    @Column(nullable = false, length = 20)
    private String event;

    private LocalDateTime lastUpdated;

    public PaymentStateMachineEntity() {
        this.lastUpdated = LocalDateTime.now();
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public PaymentStates getState() {
        return state;
    }

    public String getEvent() {
        return event;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public void setState(PaymentStates state) {
        this.state = state;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}