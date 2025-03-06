package org.example;

import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

@Component
public class PaymentStateMachinePersist implements StateMachinePersist<PaymentStates, PaymentEvents, Long> {
    private final PaymentStateMachineRepository repository;

    public PaymentStateMachinePersist(PaymentStateMachineRepository repository) {
        this.repository = repository;
    }

    @Override
    public void write(StateMachineContext<PaymentStates, PaymentEvents> context, Long paymentId) throws Exception {
        PaymentStateMachineEntity entity = repository.findById(paymentId)
                .orElse(new PaymentStateMachineEntity());

        entity.setPaymentId(paymentId);
        entity.setState(context.getState());  // Persist the state
        entity.setEvent(context.getEvent() != null ? context.getEvent().name() : null);
        entity.setLastUpdated(java.time.LocalDateTime.now());

        repository.save(entity);
    }

    @Override
    public StateMachineContext<PaymentStates, PaymentEvents> read(Long paymentId) throws Exception {
        return repository.findById(paymentId)
                .map(entity -> new DefaultStateMachineContext<PaymentStates, PaymentEvents>(
                        entity.getState(),
                        null,
                        null,
                        null
                ))
                .orElse(null);
    }
}
