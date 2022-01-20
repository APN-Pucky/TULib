package de.neuwirthinformatik.Alexander.TU.Basic;

import org.junit.BeforeClass;

public class GlobalDataServerTest extends GlobalDataTest {

	@BeforeClass
	public static void setUpClass() {
		GlobalData.init(false);
	}
}
