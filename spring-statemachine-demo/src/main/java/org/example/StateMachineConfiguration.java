package org.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;

import java.util.EnumSet;

@Configuration
@EnableStateMachine
public class StateMachineConfiguration extends StateMachineConfigurerAdapter<PaymentStates, PaymentEvents> {
    @Override
    public void configure(StateMachineStateConfigurer<PaymentStates, PaymentEvents> states) throws Exception {
        states
                .withStates()
                .initial(PaymentStates.INITIAL)
                .end(PaymentStates.VOIDED)
                .states(EnumSet.allOf(PaymentStates.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<PaymentStates, PaymentEvents> transitions) throws Exception {
        transitions
                .withExternal()
                .source(PaymentStates.INITIAL)
                .target(PaymentStates.THREE_DS_AUTHENTICATION_PENDING)
                .event(PaymentEvents.AUTHORIZE)
                .and()
                .withExternal()
                .source(PaymentStates.THREE_DS_AUTHENTICATION_PENDING)
                .target(PaymentStates.AUTHORIZED)
                .event(PaymentEvents.AUTHENTICATE)
                .and()
                .withExternal()
                .source(PaymentStates.AUTHORIZED)
                .target(PaymentStates.CAPTURED)
                .event(PaymentEvents.CAPTURE)
                .and()
                .withExternal()
                .source(PaymentStates.AUTHORIZED)
                .target(PaymentStates.VOIDED)
                .event(PaymentEvents.VOID)
                .and()
                .withExternal()
                .source(PaymentStates.CAPTURED)
                .target(PaymentStates.VOIDED)
                .event(PaymentEvents.VOID);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<PaymentStates, PaymentEvents> config) throws Exception {
        config.withConfiguration()
                .autoStartup(true);
    }

    @Bean
    public StateMachinePersister<PaymentStates, PaymentEvents, Long> stateMachinePersister(PaymentStateMachinePersist persist) {
        return new DefaultStateMachinePersister<>(persist);
    }
}
