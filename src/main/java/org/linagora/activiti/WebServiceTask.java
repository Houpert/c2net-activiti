package org.linagora.activiti;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("startProcessInstanceDelegate")
public class WebServiceTask implements JavaDelegate {

	public void execute(DelegateExecution execution) throws Exception {
		RuntimeService runtimeService = execution.getEngineServices().getRuntimeService();

		runtimeService.startProcessInstanceByKey("ServiceTask");
	}

}