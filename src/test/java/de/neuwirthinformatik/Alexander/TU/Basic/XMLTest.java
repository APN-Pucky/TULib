package de.neuwirthinformatik.Alexander.TU.Basic;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import de.neuwirthinformatik.Alexander.TU.Basic.Card.CardInstance;

public class XMLTest {
	@BeforeClass
	public static void setUpClass() {
		GlobalData.init(true);
	}
	
	@Test
	public void getCardXML() {
		CardInstance ci = Gen.genCardInstance("test", (int) System.currentTimeMillis());
		String s = GlobalData.xml.getCardXML(ci.getCard());
		assertNotNull(s,ci);
		System.out.println(s);
	}
	
	@Test 
	public void writeCardXML() throws FileNotFoundException, IOException {
		GlobalData.xml.appendToCardSection(1, Gen.genCardInstance("test", (int) System.currentTimeMillis()).getCard());
	}
}
