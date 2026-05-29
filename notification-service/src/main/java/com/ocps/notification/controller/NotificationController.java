package com.ocps.notification.controller;

import com.ocps.notification.dto.NotificationRequestDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @PostMapping("/send")
    public String sendNotification(@RequestBody NotificationRequestDto requestDto) {
        System.out.println("Sending notification to: " + requestDto.getRecipient());
        System.out.println("Message: " + requestDto.getMessage());
        return "Notification Sent";
    }
}