package org.linagora.test.parse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.linagora.parse.ActivitiParse;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@TestComponent
public class ParseXmlBpmnTest {

	private final String xslPathResources = "src/test/resources/parse/";
	private final String xmlPathInput = "src/test/resources/inputXml/";
	private final String xmlPathOutput = "src/test/resources/outputXml/";
	
	
	private final String xslName = "ActivitiXLS.xml";
	private ActivitiParse aParse;

	@Before
	public void setUp() {
		aParse = new ActivitiParse(xslPathResources, xslName);
	}

	@After
	public void tearDown() {

	}

	private MultipartFile getMockCommonsMultipartFile(File file) throws IOException {
		FileInputStream inputFile = new FileInputStream(file.getAbsolutePath());  
		MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "multipart/form-data", inputFile);
        return multipartFile;
    }

	@Test
	public void testValidParsing() throws Exception {
		String fileName = "Valid_TestBpmnToParse.xml";
		File fileToMultipart = new File(xmlPathInput+fileName);
		
		MultipartFile myMultipartFile = getMockCommonsMultipartFile(fileToMultipart);
		File myParsedFile = aParse.parseXMLToActiviti(myMultipartFile);
		
		Assert.assertNotNull(myParsedFile);
		Assert.assertEquals(myParsedFile.getName(), aParse.generateParsedFileName(fileName));
	}
	
	@Test
	public void testWrongParsing() throws Exception {
		String fileName = "Wrong_TestBpmnToParse.xml";
		File fileToMultipart = new File(xmlPathInput+fileName);
		
		MultipartFile myMultipartFile = getMockCommonsMultipartFile(fileToMultipart);
		File myParsedFile = aParse.parseXMLToActiviti(myMultipartFile);
		
		Assert.assertNull(myParsedFile);
	}

}
