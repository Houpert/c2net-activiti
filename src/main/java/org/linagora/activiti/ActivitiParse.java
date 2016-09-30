package org.linagora.activiti;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.linagora.dao.ActivitiBpmn;
import org.springframework.web.multipart.MultipartFile;

public class ActivitiParse {

	private static final String xslPath = "src/main/resources/parse/";
	private static final String xslName = "ActivitiXLS.xml";
	private static final String xslNewFileName = "ToTransform.xml";
	private static final String xslFullName = xslPath+xslName;

	private static final String xslPathTest = "src/main/resources/test/";
	private static final String xmlProcessPath = "src/main/resources/processes/";

	private static final String startNameParsing = "Parse_";
	private static final String extensionBPMN = ".bpmn20.xml";


	/*
	 Only for test parsing 
	 public static void main(String[] args) throws Exception {
		File xslFile = getFileWithPath(xslPath, xslName);
		File xmlFile = getFileWithPath(xslPathTest, xslNewFileName);
		File xmlFileDone = new File(xslPathTest, "Done"+xslNewFileName);

		Transformer transformer = TransformerFactory.newInstance( ).newTransformer(
				new StreamSource(xslFile));
		transformer.transform(new StreamSource(xmlFile),new StreamResult(xmlFileDone));
	}*/

	public ActivitiBpmn parseXMLToActiviti(MultipartFile multipart) {
		try {
			File xslFile = getFileWithPath(xslPath, xslName);
			File xmlFile = getFileFromeMultipartFile(multipart);
			File xmlFileDone = new File(xmlProcessPath, startNameParsing+multipart.getName()+extensionBPMN);

			xmlFileDone.deleteOnExit();

			Transformer transformer = TransformerFactory.newInstance( ).newTransformer(new StreamSource(xslFile));
			transformer.transform(new StreamSource(xmlFile),new StreamResult(xmlFileDone));
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ActivitiBpmn(multipart.getOriginalFilename(), null);
	}

	private File getFileFromeMultipartFile(MultipartFile multipart) throws IllegalStateException, IOException {
		File convFile = new File(multipart.getOriginalFilename());
		convFile.createNewFile(); 
		FileOutputStream fos = new FileOutputStream(convFile); 
		fos.write(multipart.getBytes());
		fos.close();
		convFile.deleteOnExit();
		return convFile;
	}

	private static File getFileWithPath(String path, String name) throws IOException {
		File fileToLoad = new File(path, name);

		if(fileToLoad.length() == 0)
			throw new IOException("File not found");
		return fileToLoad;
	}
}
