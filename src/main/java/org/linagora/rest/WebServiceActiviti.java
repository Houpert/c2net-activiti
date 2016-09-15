package org.linagora.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebServiceActiviti {

	@RequestMapping("/actinivi")
	public String startProcess(@RequestParam(value="id") String id) {
		
		return id;
	}
}