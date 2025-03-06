package org.example;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Component
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final StateMachinePersister<PaymentStates, PaymentEvents, Long> smPersister;
    private final StateMachine<PaymentStates, PaymentEvents> stateMachine;

    public PaymentService(PaymentRepository paymentRepository,
                          StateMachinePersister<PaymentStates, PaymentEvents, Long> smPersister,
                          StateMachine<PaymentStates, PaymentEvents> stateMachine) {
        this.paymentRepository = paymentRepository;
        this.smPersister = smPersister;
        this.stateMachine = stateMachine;
    }

    public PaymentEntity create(BigDecimal amount) {
        PaymentEntity entity = new PaymentEntity(null, PaymentStates.INITIAL, amount);
        return paymentRepository.save(entity);
    }

    public PaymentEntity authorize(Long paymentId) throws Exception {
        PaymentEntity entity = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("No payment found with id " + paymentId));

        try {
            smPersister.restore(stateMachine, paymentId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to restore state machine", e);
        }

        stateMachine.sendEvent(Mono.just(
                        MessageBuilder.withPayload(PaymentEvents.AUTHORIZE)
                                .build())
                )
                .subscribe();

        PaymentStates newState = stateMachine.getState().getId();

        PaymentEntity updatedEntity = new PaymentEntity(entity.getId(), newState, entity.getAmount());
        paymentRepository.save(updatedEntity);

        return updatedEntity;
    }

    public PaymentEntity getPayment(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("No payment found with id " + paymentId));
    }
}