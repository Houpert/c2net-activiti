package org.linagora.parse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.linagora.dao.ActivitiDAO;
import org.linagora.utility.LoggerManager;
import org.springframework.web.multipart.MultipartFile;

public class ActivitiParse {

	final static Logger logger = Logger.getLogger(ActivitiParse.class);	

	private static final String DEFAULT_NAME_PARSING = "Parse_";
	private static final String EXTENSION_BPMN = ".bpmn20.xml";
	
	private static final String DEFAULT_XML_PROCESS_PATH = "src/main/resources/processes/";
	private static final String DEFAULT_XSL_PATH = "src/main/resources/parse/";
	private static final String DEFAULT_XSL_NAME = "ActivitiXLS.xml";
	
	private String xslPath;
	private String xslName;

	public ActivitiParse() {
		this.xslPath = DEFAULT_XSL_PATH;
		this.xslName = DEFAULT_XSL_NAME;
	}

	public ActivitiParse(String xslPath, String xslName) {
		if(xslPath == null)
			this.xslPath = DEFAULT_XSL_PATH;
		else
			this.xslPath = xslPath;

		if(xslName == null)
			this.xslName = DEFAULT_XSL_NAME;
		else
			this.xslName = xslPath;
	}

	public ActivitiDAO parseXMLToActivitiExecutable(MultipartFile multipart) {
		try {
			File xslFile = getFileWithPath(xslPath, xslName);
			File xmlFile = getFileFromeMultipartFile(multipart);
			File xmlFileAfterDone = new File(DEFAULT_XML_PROCESS_PATH, DEFAULT_NAME_PARSING+multipart.getOriginalFilename()+EXTENSION_BPMN);

			xmlFileAfterDone = parseBpmnFile(xslFile, xmlFile, xmlFileAfterDone);
			xmlFile.delete();

			if(xmlFileAfterDone != null)
				return new ActivitiDAO(multipart.getOriginalFilename(), xmlFileAfterDone);

		}catch (IOException | TransformerException e) {
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

	private File parseBpmnFile(File xslFile, File xmlFile, File xmlFileAfterDone) throws TransformerException {
		Transformer transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(xslFile));
		transformer.transform(new StreamSource(xmlFile),new StreamResult(xmlFileAfterDone));
		return xmlFileAfterDone;
	}

	private File getFileWithPath(String path, String name) throws IOException {
		File fileToLoad = new File(path, name);

		if(fileToLoad.length() == 0)
			throw new IOException("File not found");
		return fileToLoad;
	}

	public String generateParsedFileName(String fileName) {
		return DEFAULT_NAME_PARSING+fileName+EXTENSION_BPMN;
	}
}
