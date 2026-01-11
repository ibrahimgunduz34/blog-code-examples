import com.example.AuthorizationResult;
import com.example.PaymentGateway;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class CircuitBreakerTest {
    Logger logger = LoggerFactory.getLogger(CircuitBreakerTest.class);

    @Test
    void failFromBeginning() {
        PaymentGateway paymentGateway = mock(PaymentGateway.class);
        when(paymentGateway.getStatus(anyString())).thenThrow(new RuntimeException("Failed"));

        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .slidingWindowSize(10)
                .minimumNumberOfCalls(5)
                .failureRateThreshold(50)
                .build();

        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(config);
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("circuitBreaker");

        for (int i = 1; i <= 10; i++) {
            try {
                String result = circuitBreaker.executeSupplier(() -> paymentGateway.getStatus("TRID00001"));
            } catch (RuntimeException exception) {
                System.out.printf("%d) %s:%s%n", i, exception.getClass().getSimpleName(), exception.getMessage());
            }
        }

        verify(paymentGateway, times(5)).getStatus(anyString());
        assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);
    }

    @Test
    void failedCallsExceedsFailureRateThresholdAndServiceRecoversLater() throws InterruptedException {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .slidingWindowSize(10)
                .minimumNumberOfCalls(5)
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofMillis(500))
                .permittedNumberOfCallsInHalfOpenState(2)
                .build();

        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(config);
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("circuitBreaker");

        PaymentGateway paymentGateway = mock(PaymentGateway.class);

        when(paymentGateway.getStatus(anyString()))
                .thenReturn("SUCCESS", "SUCCESS", "SUCCESS", "SUCCESS", "SUCCESS")
                .thenThrow(
                        new RuntimeException("Downstream failure"),
                        new RuntimeException("Downstream failure"),
                        new RuntimeException("Downstream failure"),
                        new RuntimeException("Downstream failure"),
                        new RuntimeException("Downstream failure")
                )
                .thenReturn("SUCCESS", "SUCCESS");

        // 1) First 5 calls succeed
        for (int i = 1; i <= 5; i++) {
            final String txId = "TX-" + i;
            circuitBreaker.executeSupplier(() -> paymentGateway.getStatus(txId));
        }
        assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.CLOSED);

        // 2) Next 5 calls fail -> OPEN
        for (int i = 6; i <= 10; i++) {
            final String txId = "TX-" + i;
            try {
                circuitBreaker.executeSupplier(() -> paymentGateway.getStatus(txId));
            } catch (Exception ignored) {
            }
        }

        assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);

        // 3) Wait duration expires
        Thread.sleep(600);

        // Trigger HALF_OPEN (first permitted call)
        circuitBreaker.executeSupplier(() -> paymentGateway.getStatus("TX-11"));
        assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.HALF_OPEN);

        // 4) Second permitted call -> CLOSED
        circuitBreaker.executeSupplier(() -> paymentGateway.getStatus("TX-12"));
        assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.CLOSED);

        verify(paymentGateway, times(12)).getStatus(anyString());
    }

    @Test
    void decorator() {
        PaymentGateway paymentGateway = mock(PaymentGateway.class);
        when(paymentGateway.getStatus(anyString())).thenReturn("SUCCESS");

        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .slidingWindowSize(10)
                .minimumNumberOfCalls(5)
                .failureRateThreshold(50)
                .build();

        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(config);
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("circuitBreaker");

        Function<String, String> decoratedGetStatus =
                CircuitBreaker.decorateFunction(
                        circuitBreaker,
                        paymentGateway::getStatus
                );

        for (int i = 1; i <= 5; i++) {
            decoratedGetStatus.apply("TRID00001");
        }

        assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.CLOSED);
        verify(paymentGateway, times(5)).getStatus(anyString());
    }

    @Test
    void decoratorWithMultipleArguments() {
        PaymentGateway paymentGateway = mock(PaymentGateway.class);
        when(paymentGateway.authorize(anyString(), anyInt())).thenReturn(new AuthorizationResult("TRID00001", "SUCCESS"));

        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .slidingWindowSize(10)
                .minimumNumberOfCalls(5)
                .failureRateThreshold(50)
                .build();

        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(config);
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("circuitBreaker");

        BiFunction<String, Integer, AuthorizationResult> decoratedAuthorize = (token, amount) -> CircuitBreaker.decorateSupplier(
                        circuitBreaker,
                        () -> paymentGateway.authorize(token, amount)
                )
                .get();

        for (int i = 1; i <= 5; i++) {
            decoratedAuthorize.apply("7cf267eb-21d8-4802-9703-d4309bd3eddc", 100);
        }

        assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.CLOSED);
        verify(paymentGateway, times(5)).authorize(anyString(), anyInt());
    }

    @Test
    void fallbackWithPlainJava() {
        PaymentGateway paymentGateway = mock(PaymentGateway.class);
        when(paymentGateway.getStatus(anyString())).thenThrow(new RuntimeException("Failed"));

        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .slidingWindowSize(10)
                .minimumNumberOfCalls(5)
                .failureRateThreshold(50)
                .build();

        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(config);
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("circuitBreaker");

        Function<String, String> decoratedGetStatus =
                CircuitBreaker.decorateFunction(
                        circuitBreaker,
                        paymentGateway::getStatus
                );

        Function<String, String> safeDecoratedGetStatus = transactionId -> {
            try {
                return decoratedGetStatus.apply(transactionId); }
            catch (Exception exception) {
                return  "UNKNOWN";
            }
        };

        for (int i = 1; i <= 5; i++) {
            String result = safeDecoratedGetStatus.apply("TRID00001");
            assertThat(result).isEqualTo("UNKNOWN");
        }

        verify(paymentGateway, times(5)).getStatus(anyString());
        assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);
    }

    @Test
    void fallbackWithVavr() {
        PaymentGateway paymentGateway = mock(PaymentGateway.class);
        when(paymentGateway.getStatus(anyString())).thenThrow(new RuntimeException("Failed"));

        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .slidingWindowSize(10)
                .minimumNumberOfCalls(5)
                .failureRateThreshold(50)
                .build();

        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(config);
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("circuitBreaker");

        Function<String, String> decoratedGetStatus =
                CircuitBreaker.decorateFunction(
                        circuitBreaker,
                        paymentGateway::getStatus
                );

        Function<String, String> safeDecoratedGetStatus = transactionId ->
                Try.of(() -> decoratedGetStatus.apply(transactionId))
                        .recover(throwable -> "UNKNOWN").get();

        for (int i = 1; i <= 5; i++) {
            String result = safeDecoratedGetStatus.apply("TRID00001");
            assertThat(result).isEqualTo("UNKNOWN");
        }

        verify(paymentGateway, times(5)).getStatus(anyString());
        assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);
    }
}
