package com.ocps.auth.client;

import com.ocps.auth.dto.NotificationRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "NOTIFICATION-SERVICE")
public interface NotificationClient {

    @PostMapping("/notification/send")
    String sendNotification(@RequestBody NotificationRequestDto requestDto);
}
