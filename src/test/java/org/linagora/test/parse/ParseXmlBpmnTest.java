package org.linagora.test.parse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.linagora.dao.ActivitiDAO;
import org.linagora.parse.ActivitiParse;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@TestComponent
public class ParseXmlBpmnTest {

	private final static String xmlPathInput = "src/test/resources/inputXml/";
	private final static String xmlPathOutput = "src/test/resources/outputXml/";

	private final static String xmlValideFileToParse = "test_bpmn_valid";
	private final static String xmlBpmnFileParse = "Parse_test_bpmn_valid.bpmn20.xml";

	private final static String xmlErrorFileToParse = "test_bpmn_error";
	private final static String noXmlErrorFile = "test_no_xml_file";

	
	private ActivitiParse aParse;

	@Before
	public void setUp() {
		aParse = new ActivitiParse();

	}

	@After
	public void tearDown() {

	}

	private MultipartFile getMockCommonsMultipartFile(File file) throws IOException {
		FileInputStream inputFile = new FileInputStream(file.getAbsolutePath());
		MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "multipart/form-data",
				inputFile);
		return multipartFile;
	}

	@Test
	public void testValideParse() throws Exception {
		File fileToMultipart = new File(xmlPathInput + xmlValideFileToParse);
		
		MultipartFile myMultipartFile = getMockCommonsMultipartFile(fileToMultipart);
		ActivitiDAO myBpmn = aParse.parseXMLToActivitiExecutable(myMultipartFile);

		myBpmn.getFile().deleteOnExit();
		Assert.assertNotNull(myBpmn);
		Assert.assertEquals(myBpmn.getName(), aParse.generateParsedFileName(xmlValideFileToParse));
		
		File fileValide = new File(xmlPathOutput+xmlBpmnFileParse);
		
		Assert.assertEquals(fileValide.length(), myBpmn.getFile().length());
		Assert.assertEquals(fileValide.getName(), myBpmn.getFile().getName());
	}

	@Test
	public void testWrongParsing() throws Exception {
		try {
			File fileToMultipart = new File(xmlPathInput + xmlErrorFileToParse);

			MultipartFile myMultipartFile = getMockCommonsMultipartFile(fileToMultipart);
			aParse.parseXMLToActivitiExecutable(myMultipartFile);

			Assert.fail();
		} catch (Exception e) {
			Assert.assertTrue(e.toString().contains("javax.xml.transform.TransformerException"));
		}
	}

	@Test
	public void testNoXmlFileParsing() throws Exception {
		try {
			File fileToMultipart = new File(xmlPathInput + noXmlErrorFile);

			MultipartFile myMultipartFile = getMockCommonsMultipartFile(fileToMultipart);
			aParse.parseXMLToActivitiExecutable(myMultipartFile);
			Assert.fail();
		} catch (Exception e) {
			Assert.assertTrue(e.toString().contains("javax.xml.transform.TransformerException"));
		}
	}

	@Test
	public void testNullFileParsing() throws Exception {
		try {
			aParse.parseXMLToActivitiExecutable(null);
			Assert.fail();
		} catch (Exception e) {
			Assert.assertTrue(e.getMessage().contains("The parsed file can't be null"));
		}
	}
}
