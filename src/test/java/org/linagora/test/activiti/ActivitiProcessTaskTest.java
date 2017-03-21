package org.linagora.test.activiti;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.util.json.JSONArray;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.linagora.activiti.ActivitiProcess;
import org.linagora.dao.ActivitiDAO;
import org.linagora.dao.openpaas.form.Form;
import org.linagora.exception.ExceptionGeneratorActiviti;
import org.linagora.service.ApplicationWorkflowReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

@TestComponent
public class ActivitiProcessTaskTest {

	private static ConfigurableApplicationContext application;

	private final static String xmlPathInput = "src/test/resources/inputXml/";

	private final static String form_string_bpmn = "form_string";
	private final static String form_userMail_bpmn = "form_userMail";
	private final static String form_number_bpmn = "form_number";


	private final static boolean isExecuted = true;

	private final static ActivitiProcess aProcess = new ActivitiProcess();

	private final static String testUser = "test@test.com";
	private final static String fakeUser = "fake@test.com";

	private final static String taskIdMapKey = "taskId";

	private MultipartFile getMockCommonsMultipartFile(File file) throws IOException {
		FileInputStream inputFile = new FileInputStream(file.getAbsolutePath());
		MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "multipart/form-data",
				inputFile);
		return multipartFile;
	}

	public static void changeSystemOutputTest() throws FileNotFoundException {
		OutputStream output = new FileOutputStream("/dev/null");
		PrintStream nullOut = new PrintStream(output);
		System.setErr(nullOut);
	}

	@BeforeClass
	public static void setUp() throws FileNotFoundException {
		application = SpringApplication.run(ApplicationWorkflowReader.class);
	}

	@AfterClass
	public static void tearDown() {
		application.stop();
	}

	public ActivitiDAO checkActivitiDao(String jsonActiviti, String fileName, boolean isExecute) {
		ActivitiDAO activitiData = new Gson().fromJson(jsonActiviti, ActivitiDAO.class);

		Assert.assertEquals(fileName, activitiData.getProcessId());
		Assert.assertTrue(activitiData.getFile().getAbsolutePath().contains(fileName));
		if (isExecute)
			Assert.assertNotNull(activitiData.getIdNumber());
		else
			Assert.assertNull(activitiData.getIdNumber());

		return activitiData;
	}

	@Test
	public void completeUserTask_executeFormString_TaskFindAndComplete()
			throws ExceptionGeneratorActiviti, IOException {
		try {
			File file = new File(xmlPathInput + form_string_bpmn);
			MultipartFile multipartFile = getMockCommonsMultipartFile(file);

			aProcess.initBpmnIoToActiviti(multipartFile, isExecuted);
			String jsonListTask = aProcess.listTask(fakeUser);

			Assert.assertNotNull(jsonListTask);

			JSONArray jsonarray = new JSONArray(jsonListTask);

			Assert.assertNotNull(jsonarray.getJSONObject(0));
			Assert.assertNotNull(jsonarray.getJSONObject(0).get(taskIdMapKey));
			Assert.assertEquals(1, jsonarray.length());

			Map<String, Object> mapAttribute = new HashMap<String, Object>();
			mapAttribute.put(taskIdMapKey, jsonarray.getJSONObject(0).get(taskIdMapKey));
			mapAttribute.put("string", "testString");

			aProcess.completeUserTask(mapAttribute);
			jsonListTask = aProcess.listTask(fakeUser);

			Assert.assertEquals(0, new JSONArray(jsonListTask).length());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void completeUserTask_executeFormNumber_TaskFindAndComplete() throws ExceptionGeneratorActiviti, IOException {
		try {
			File file = new File(xmlPathInput + form_number_bpmn);
			MultipartFile multipartFile = getMockCommonsMultipartFile(file);
			aProcess.initBpmnIoToActiviti(multipartFile, isExecuted);

			String jsonListTask = aProcess.listTask(testUser);

			Assert.assertNotNull(jsonListTask);

			JSONArray jsonarray = new JSONArray(jsonListTask);

			Assert.assertEquals(1, jsonarray.length());
			Assert.assertNotNull(jsonarray.getJSONObject(0).get(taskIdMapKey));

			Map<String, Object> mapAttribute = new HashMap<String, Object>();
			mapAttribute.put(taskIdMapKey, jsonarray.getJSONObject(0).get(taskIdMapKey));
			mapAttribute.put("integer", 50);
			aProcess.completeUserTask(mapAttribute);

			jsonListTask = aProcess.listTask(testUser);
			jsonarray = new JSONArray(jsonListTask);
			Assert.assertEquals(0, jsonarray.length());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void listTask_checkMailList_FilterOkAndExecute() throws ExceptionGeneratorActiviti, IOException {
		try {
			
			//TODO check user list
			File file = new File(xmlPathInput + form_userMail_bpmn);
			MultipartFile multipartFile = getMockCommonsMultipartFile(file);
			aProcess.initBpmnIoToActiviti(multipartFile, isExecuted);
			
			String jsonListTask = aProcess.listTask(testUser);
			Assert.assertNotNull(jsonListTask);
			
			String jsonListTaskFake = aProcess.listTask(fakeUser);
			Assert.assertNotNull(jsonListTaskFake);

			JSONArray jsonarray = new JSONArray(jsonListTask);

			Assert.assertEquals(1, jsonarray.length());
			Assert.assertNotNull(jsonarray.getJSONObject(0).get(taskIdMapKey));
			
			JSONArray jsonarrayFake = new JSONArray(jsonListTaskFake);

			Assert.assertEquals(0, jsonarrayFake.length());
			
			Map<String, Object> mapAttribute = new HashMap<String, Object>();
			mapAttribute.put(taskIdMapKey, jsonarray.getJSONObject(0).get(taskIdMapKey));
			mapAttribute.put("string", "string");
			aProcess.completeUserTask(mapAttribute);

			jsonListTask = aProcess.listTask(testUser);
			jsonarray = new JSONArray(jsonListTask);
			Assert.assertEquals(0, jsonarray.length());

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
