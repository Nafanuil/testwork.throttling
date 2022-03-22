package testwork.throttling.controller;

import testwork.throttling.throttler.CallsCount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    private final CallsCount callsCount;

    @Autowired
    public MainController(CallsCount callsCount) {
        this.callsCount = callsCount;
    }

    @GetMapping("/testUrl")
    public ResponseEntity<String> testUrl(@RequestParam("ip") String ip) {
        callsCount.addKeyIfAbsentAndIncrementCount(ip);
        log.info("ip: " + ip);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
