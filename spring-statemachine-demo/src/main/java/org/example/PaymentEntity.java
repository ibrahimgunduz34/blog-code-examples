package org.example;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "payments")
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentStates paymentState;

    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal amount;

    public PaymentEntity() {
    }

    public PaymentEntity(Long id, PaymentStates paymentState, BigDecimal amount) {
        this.id = id;
        this.paymentState = paymentState;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public PaymentStates getPaymentState() {
        return paymentState;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
