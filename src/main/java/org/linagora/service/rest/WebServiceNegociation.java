package org.linagora.service.rest;

import org.linagora.service.ServiceNegociation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/negociation")
public class WebServiceNegociation implements ServiceNegociation{

}