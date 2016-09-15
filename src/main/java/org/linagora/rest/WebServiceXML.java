package org.linagora.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebServiceXML {

	@RequestMapping("/parse")
	public String getXML(@RequestParam(value="xml") String xml) {
		return xml;
	}
}