package org.linagora.parse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.linagora.utility.LoggerManager;
import org.springframework.web.multipart.MultipartFile;

public class ActivitiParse {

	final static Logger logger = Logger.getLogger(ActivitiParse.class);	

	private final String defaultXslPath = "src/main/resources/parse/";
	private final String defaultXslName = "ActivitiXLS.xml";

	private String xslPath = "src/main/resources/parse/";
	private String xslName = "ActivitiXLS.xml";

	private static final String xmlProcessPath = "src/main/resources/processes/";

	private static final String startNameParsing = "Parse_";
	private static final String extensionBPMN = ".bpmn20.xml";

	public ActivitiParse() {
		this.xslPath = defaultXslPath;
		this.xslName = defaultXslName;
	}

	public ActivitiParse(String xslPath, String xslName) {
		this.xslPath = xslPath;
		this.xslName = xslName;
	}

	public File parseXMLToActiviti(MultipartFile multipart) {
		try {
			File xslFile = getFileWithPath(xslPath, xslName);
			File xmlFile = getFileFromeMultipartFile(multipart);
			File xmlFileAfterDone = new File(xmlProcessPath, startNameParsing+multipart.getOriginalFilename()+extensionBPMN);

			xmlFileAfterDone.deleteOnExit();

			xmlFileAfterDone = parseBpmnFile(xslFile, xmlFile, xmlFileAfterDone);

			return xmlFileAfterDone;
		}catch (IOException e) {
			LoggerManager.loggerTrace(e);
		}
		return null;
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

	private File parseBpmnFile(File xslFile, File xmlFile, File xmlFileAfterDone) {
		try{
			Transformer transformer = TransformerFactory.newInstance( ).newTransformer(new StreamSource(xslFile));
			transformer.transform(new StreamSource(xmlFile),new StreamResult(xmlFileAfterDone));
			return xmlFileAfterDone;
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			LoggerManager.loggerTrace(e);
			return null;
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			LoggerManager.loggerTrace(e);
			return null;
		}
	}

	private File getFileWithPath(String path, String name) throws IOException {
		File fileToLoad = new File(path, name);

		if(fileToLoad.length() == 0)
			throw new IOException("File not found");
		return fileToLoad;
	}

	public String generateParsedFileName(String fileName) {
		return startNameParsing+fileName+extensionBPMN;
	}
}
