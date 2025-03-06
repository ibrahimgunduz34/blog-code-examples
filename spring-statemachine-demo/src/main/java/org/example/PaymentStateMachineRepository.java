package org.example;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentStateMachineRepository extends JpaRepository<PaymentStateMachineEntity, Long> {
}
