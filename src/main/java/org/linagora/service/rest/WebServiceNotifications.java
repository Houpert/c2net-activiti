package org.linagora.service.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
public class WebServiceNotifications {

	@RequestMapping("/send")
	public String sendNotification(@RequestParam(value="id") String id) {
		
		return id;
	}
}