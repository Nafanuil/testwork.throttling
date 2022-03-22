package testwork.throttling.throttler;

import java.util.Timer;
import java.util.TimerTask;

public class ThrottleTimerImpl implements Throttler {

    private final long throttlePeriod;
    private final CallsCount callsCount;

    public ThrottleTimerImpl(long intervalInMinute, CallsCount callsCount) {
        this.throttlePeriod = intervalInMinute * 60 * 1000;
        this.callsCount = callsCount;
    }

    @Override
    public void start() {
        new Timer(true).schedule(new TimerTask() {
            @Override
            public void run() {
                callsCount.reset();
            }
        }, 0, throttlePeriod);
    }
}