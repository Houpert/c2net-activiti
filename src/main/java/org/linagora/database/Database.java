package org.linagora.database;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.linagora.dao.ActivitiBpmn;

public class Database {

	public static String create(ActivitiBpmn activitiBpmn){
		String idCreate = activitiBpmn.getBpmnName();
		try{
			File file = new File(activitiBpmn.getBpmnName());
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(activitiBpmn.getData());
			fileWriter.flush();
			fileWriter.close();

			//TODO Don't forget to remove this line
			file.deleteOnExit();
		}catch(IOException e){
			return null;
		}

		return idCreate;
	}

	public static String edit(ActivitiBpmn bpmn){
		String idEdit = bpmn.getBpmnName();


		return idEdit;
	}

	public static String delete(String id){
		String idDelete = id;


		return idDelete;
	}

}
