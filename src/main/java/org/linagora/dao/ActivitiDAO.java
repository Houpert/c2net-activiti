package org.linagora.dao;

import java.io.File;

public class ActivitiDAO {
	
	public String processId;
	public File file;

	public ActivitiDAO(String process, File file) {
		super();
		this.processId = process;
		this.file = file;
	}
	
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String process) {
		this.processId = process;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getName(){
		return file.getName();
	}
}
