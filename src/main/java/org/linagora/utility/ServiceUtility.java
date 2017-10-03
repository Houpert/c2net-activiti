package org.linagora.utility;

import java.util.Map;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.delegate.DelegateExecution;

public class ServiceUtility {
	
	public static String getVariableMessage(DelegateExecution execution) {
		Map<String, Object> globalVariableMap = ProcessEngines.getDefaultProcessEngine().getRuntimeService()
				.getVariables(execution.getProcessInstanceId());

		String message = "";
		boolean first = true;
		System.out.println("HELLO");
		if (globalVariableMap != null && !globalVariableMap.isEmpty()) {
			System.out.println("In the boucle");
			for (String key : globalVariableMap.keySet()) {
				System.out.println("gona add message");
				String keyVarName = "${" + key + "}";
				String keyVarValue = globalVariableMap.get(key).toString();
				if (first) {
					message += keyVarName + " = " + keyVarValue;
					first = false;
				} else {
					message += ", " + keyVarName + " = " + keyVarValue;
				}
			}
		}
		return message;
	}
}
