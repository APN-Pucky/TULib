package de.neuwirthinformatik.Alexander.TU.Basic;

import static org.junit.Assert.*;

import org.junit.Test;

public abstract class GlobalDataTest {

	@Test
	public void distinctCards() {
		for (Card c : GlobalData.distinct_cards) {
			assertTrue("data size " + GlobalData.distinct_cards.length, c != null);
		}
	}

	@Test
	public void distinctCardsSize() {
		assertTrue(GlobalData.distinct_cards.length < 100000);
	}

	@Test
	public void getCardByName() {
		String n = "Krellus";
		assertTrue(GlobalData.getCardByName(n).getName().equals(n));
	}
}
