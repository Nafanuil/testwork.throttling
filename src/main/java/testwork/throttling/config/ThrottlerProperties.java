package testwork.throttling.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("throttler")
public class ThrottlerProperties {

    private Long maxCallPerSecond;
    private Long intervalInMinute;

    public long getMaxCallPerSecond() {
        return maxCallPerSecond;
    }

    public void setMaxCallPerSecond(long maxCallPerSecond) {
        if (maxCallPerSecond < 1) {
            throw new RuntimeException("invalid param 'maxCallPerSecond': " + maxCallPerSecond);
        }
        this.maxCallPerSecond = maxCallPerSecond;
    }

    public long getIntervalInMinute() {
        return intervalInMinute;
    }

    public void setIntervalInMinute(long intervalInMinute) {
        if (intervalInMinute < 1) {
            throw new RuntimeException("invalid param 'intervalInMinute': " + intervalInMinute);
        }
        this.intervalInMinute = intervalInMinute;
    }
}
