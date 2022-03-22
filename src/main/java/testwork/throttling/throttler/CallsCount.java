package testwork.throttling.throttler;

import testwork.throttling.config.ThrottlerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class CallsCount {
    private final Map<String, AtomicLong> callsCount = new ConcurrentHashMap<>();
    private final long maxCallPerSecond;

    @Autowired
    public CallsCount(ThrottlerProperties throttlerProperties) {
        this.maxCallPerSecond = throttlerProperties.getMaxCallPerSecond();
    }

    public void addKeyIfAbsentAndIncrementCount(String keyName) {
        AtomicLong previseValue = addKey(keyName);
        long currentNumberCalls = previseValue == null ? 1 : previseValue.incrementAndGet();
        if (maxCallPerSecond < currentNumberCalls) {
            throw new ThrottlerException(HttpStatus.BAD_GATEWAY);
        }
    }

    private AtomicLong addKey(String keyName) {
        return callsCount.putIfAbsent(keyName, new AtomicLong(1));
    }

    public void reset() {
        callsCount.replaceAll((k, v) -> new AtomicLong(0));
    }
}