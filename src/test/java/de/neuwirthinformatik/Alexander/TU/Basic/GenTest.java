package de.neuwirthinformatik.Alexander.TU.Basic;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;

import de.neuwirthinformatik.Alexander.TU.Basic.Card.CardInstance;

public class GenTest {
	@BeforeClass
	public static void setUpClass() {
		GlobalData.init(true);
	}
	@Test
	public void genCardInstance() {
		CardInstance ci = Gen.genCardInstance("test", (int) System.currentTimeMillis());
		assertNotNull("",ci);
	}
}
