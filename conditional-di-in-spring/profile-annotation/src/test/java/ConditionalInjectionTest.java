import org.example.PaymentProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ConditionalInjectionTest {
    @Test
    public void testWithNoopPaymentProcessorImp() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        context.scan("org.example");
        context.refresh();

        PaymentProcessor paymentProcessorAdapter = context.getBean(PaymentProcessor.class);

        assertThat(paymentProcessorAdapter.getClass().getName()).isEqualTo("org.example.NoopPaymentProcessor");
    }

    @Test
    public void testWithCurrentPaymentProcessorImpl() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        ConfigurableEnvironment environment = context.getEnvironment();
        environment.setActiveProfiles("production");

        context.scan("org.example");
        context.refresh();

        PaymentProcessor paymentProcessorAdapter = context.getBean(PaymentProcessor.class);
        assertThat(paymentProcessorAdapter.getClass().getName()).isEqualTo("org.example.PayuPaymentProcessor");
    }
}

