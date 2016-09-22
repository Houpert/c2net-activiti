package org.linagora.service.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebServiceActiviti {

	@RequestMapping("/activiti")
	public String startProcess(@RequestParam(value="id") String id) {
		
		return id;
	}
}