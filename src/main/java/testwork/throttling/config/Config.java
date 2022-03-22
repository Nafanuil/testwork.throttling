package testwork.throttling.config;

import testwork.throttling.throttler.CallsCount;
import testwork.throttling.throttler.ThrottleTimerImpl;
import testwork.throttling.throttler.Throttler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    Throttler throttler(ThrottlerProperties throttlerProperties, CallsCount callsCount) {
        Throttler timer = new ThrottleTimerImpl(throttlerProperties.getIntervalInMinute(), callsCount);
        timer.start();
        return timer;
    }
}
