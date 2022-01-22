package de.neuwirthinformatik.Alexander.TU.TUO;

import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import de.neuwirthinformatik.Alexander.TU.Basic.Card;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.CardType;
import de.neuwirthinformatik.Alexander.TU.Basic.Gen;
import de.neuwirthinformatik.Alexander.TU.Basic.GlobalData;
import de.neuwirthinformatik.Alexander.TU.TUO.TUO.Param.Dom;
import de.neuwirthinformatik.Alexander.TU.TUO.TUO.Param.Mode;
import de.neuwirthinformatik.Alexander.TU.TUO.TUO.Param.OP;
import de.neuwirthinformatik.Alexander.TU.TUO.TUO.Param.Order;
import de.neuwirthinformatik.Alexander.TU.TUO.TUO.Result;

public class TUOTest {
	@BeforeClass
	public static void setUpClass() {
		GlobalData.init(true);
	}

	public void sim(String deck1,String deck2) {
		TUO.Param p = new TUO.Param(deck1,deck2,OP.sim,Order.random,Mode.pvp_defense,Dom.dom_maxed,"",100,0);
		Result r = TUO.sim(p);
		assertNotNull("",r);
		System.out.println(r.WINS + " " + r.LOSSES);
	}

	@Test
	public void sim() {
		sim("Cyrus,Obsidian","Cyrus, Obsidian");
	}
	@Test
	public void simGen() throws FileNotFoundException, IOException {
		GlobalData.xml.appendToCardSection(1, Gen.genCardInstance("simgentest", (int) System.currentTimeMillis()).getCard());
		sim("Cyrus,simgentest","Cyrus,Obsidian");
		GlobalData.xml.appendToCardSection(1, Gen.genCardInstance("simgentest2", (int) System.currentTimeMillis()).getCard());
		sim("Cyrus,simgentest","Cyrus,Obsidian");
	}
	@Test 
	public void simGenTest() throws FileNotFoundException, IOException {
		Card a = Gen.genCardInstance("simgentest_ASS", (int) System.currentTimeMillis(),CardType.ASSAULT).getCard();
		Card c = Gen.genCardInstance("simgentest_COM", (int) System.currentTimeMillis(),true).getCard();
		Card d = Gen.genCardInstance("simgentest_DOM", (int) System.currentTimeMillis(),CardType.DOMINION).getCard();
		GlobalData.xml.appendToCardSection(1, a);
		GlobalData.xml.appendToCardSection(1, c);
		GlobalData.xml.appendToCardSection(1, d);
		a = Gen.genCardInstance("simgentest2_ASS", (int) System.currentTimeMillis(),CardType.ASSAULT).getCard();
		c = Gen.genCardInstance("simgentest2_COM", (int) System.currentTimeMillis(),true).getCard();
		d = Gen.genCardInstance("simgentest2_DOM", (int) System.currentTimeMillis(),CardType.DOMINION).getCard();
		GlobalData.xml.appendToCardSection(1, a);
		GlobalData.xml.appendToCardSection(1, c);
		GlobalData.xml.appendToCardSection(1, d);
		sim("simgentest_COM,simgentest_DOM,simgentest_ASS#10","simgentest2_COM,simgentest2_DOM,simgentest2_ASS#10");
	}
}
