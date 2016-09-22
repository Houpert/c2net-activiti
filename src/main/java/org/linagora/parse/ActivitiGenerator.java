package org.linagora.parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.linagora.dao.ActivitiBpmn;
import org.linagora.database.Database;
import org.springframework.web.multipart.MultipartFile;

public class ActivitiGenerator {

	private static final String xslPath = "src/main/resources/parse/";
	private static final String xslName = "ActivitiXLS.xml";
	private static final String xslNewFileName = "ToTransform.xml";
	private static final String xslFullName = xslPath+xslName;

	private static final String xslPathTest = "src/main/resources/test/";


	public static void main(String[] args) throws Exception {
		File xslFile = getFileWithPath(xslPath, xslName);
		File xmlFile = getFileWithPath(xslPathTest, xslNewFileName);
		File xmlFileDone = new File(xslPathTest, "Done"+xslNewFileName);

		Transformer transformer = TransformerFactory.newInstance( ).newTransformer(
				new StreamSource(xslFile));
		transformer.transform(new StreamSource(xmlFile),new StreamResult(xmlFileDone));
	}

	public ActivitiBpmn parseXMLToActiviti(MultipartFile file) throws IOException{

		//TODO GET XLS FILE
		File xslFile = getFileWithPath(xslPath, xslName);
		File xmlFile = getFileWithPath(xslPath, xslNewFileName);;

		try {
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer(new StreamSource(xslFile));
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//TODO REMOVE CAMUNDA BALISE

		//TODO REMOVE bpmndi:BPMNDiagram (position bpmn)

		//TODO Parse XML for activiti reader

		return new ActivitiBpmn(file.getOriginalFilename(), null);
	}

	private static File getFileWithPath(String path, String name) throws IOException {
		File fileToLoad = new File(path, name);

		if(fileToLoad.length() == 0)
			throw new IOException("File not found");
		return fileToLoad;
	}

	public void readMultipartFile(MultipartFile file) throws IOException{
		String name = file.getOriginalFilename();

		InputStream inputStream = file.getInputStream();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

		String line;
		while ((line = bufferedReader.readLine()) != null)
		{
			// do my processing      
			System.out.println(line);
		}
	}

	public boolean saveBPMNFile(ActivitiBpmn activitiBpmn) {
		// TODO Create BPMN File with 
		Database database = new Database();
		String name = database.create(activitiBpmn);

		if(name == null)
			return false;

		return true;
	}

}
