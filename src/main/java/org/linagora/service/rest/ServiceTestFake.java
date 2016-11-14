package org.linagora.service.rest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class ServiceTestFake{

	@RequestMapping("/hello")
	public String test() {
		System.out.println("Hello World GET");
		return "Hello World GET" ;
	}
	
	@RequestMapping(value = "/hello", method = RequestMethod.POST)
	public String testPost(@RequestBody String postPayload) {
		System.out.println("Hello World POST");
		System.out.println(postPayload);
		return "Hello World POST" ;
	}
}