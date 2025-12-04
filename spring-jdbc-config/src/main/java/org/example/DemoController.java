package org.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    private final String notificationServiceAccountId;

    public DemoController(@Value("${notification.service.accountId}") String notificationServiceAccountId) {
        this.notificationServiceAccountId = notificationServiceAccountId;
    }

    @GetMapping("/config")
    public String getNotificationServiceAccountId() {
        return notificationServiceAccountId;
    }
}
