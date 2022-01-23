
package de.neuwirthinformatik.Alexander.TU.TUO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

import de.neuwirthinformatik.Alexander.TU.Basic.Card;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.CardType;
import de.neuwirthinformatik.Alexander.TU.Basic.Gen;
import de.neuwirthinformatik.Alexander.TU.Basic.GlobalData;
import de.neuwirthinformatik.Alexander.TU.TUO.TUO.Param.Dom;
import de.neuwirthinformatik.Alexander.TU.TUO.TUO.Param.Mode;
import de.neuwirthinformatik.Alexander.TU.TUO.TUO.Param.OP;
import de.neuwirthinformatik.Alexander.TU.TUO.TUO.Param.Order;
import de.neuwirthinformatik.Alexander.TU.TUO.TUO.Result;

public class FindBestOld {
	static class Pair implements Comparable<Pair>
    {
		int score;
		String stati;
		Pair(int sco, String st) {
			this.score = sco;
			this.stati = st;
		}
		public int compareTo(Pair other) //making it only compare a2 values
        {
            return this.score - other.score;
        }
    }
	public static int size = 10;
	public static String name = "DR_F3LL";
	public static String[] stati = new String[size];
	public static int[] scores = new int[size];

	public static void main(String[] args) throws FileNotFoundException, IOException {
		GlobalData.init(false);
		stati[0]= "c88a1b3f385e42f6a9d6f939b30056fb";
		for (int i = 1; i < stati.length; i++)
			stati[i] = UUID.randomUUID().toString().replaceAll("-", "");
		for (int i = 0; i < stati.length; i++) {
			System.out.print(i);
			for (int j = 0; j < stati.length; j++)
				scores[i] += sim(stati[i],stati[j]).WINS;
		}
		Pair[] pairs = new Pair[stati.length];

		for (int i = 0; i < pairs.length; i++)
        {
            pairs[i] = new Pair(scores[i], stati[i]);
        }
        Arrays.sort(pairs);
        //printing values 
        for (int i = 0; i < scores.length; i++)
        {
            System.out.print(pairs[i].score + " " + pairs[i].stati);
        }

	}

	public static Result sim(String stati1, String stati2) throws FileNotFoundException, IOException {
		IOUtils.write("<?xml version='1.0' encoding='UTF-8'?>\r\n" + "<root></root>",
				new FileOutputStream(new File("data/cards_section_" + (20) + ".xml")), "UTF-8");
		Card a = Gen.genCardInstance(name + stati1, (name + stati1).hashCode(), CardType.ASSAULT).getCard();
		Card c = Gen.genCardInstance(name + stati1 + "_COM", (name + stati1).hashCode(), true).getCard();
		Card d = Gen.genCardInstance(name + stati1 + "_DOM", (name + stati1).hashCode(), CardType.DOMINION).getCard();
		GlobalData.xml.appendToCardSection(20, a);
		GlobalData.xml.appendToCardSection(20, c);
		GlobalData.xml.appendToCardSection(20, d);

		a = Gen.genCardInstance(name + stati2, (name + stati2).hashCode(), CardType.ASSAULT).getCard();
		c = Gen.genCardInstance(name + stati2 + "_COM", (name + stati2).hashCode(), true).getCard();
		d = Gen.genCardInstance(name + stati2 + "_DOM", (name + stati2).hashCode(), CardType.DOMINION).getCard();
		GlobalData.xml.appendToCardSection(20, a);
		GlobalData.xml.appendToCardSection(20, c);
		GlobalData.xml.appendToCardSection(20, d);
		Gen.max_com_id = 0;
		Gen.max_dom_id = 0;
		return simit(name + stati1 + "_COM," + name + stati1 + "_DOM," + name + stati1 + "_ASS#10",
				name + stati2 + "_COM," + name + stati2 + "_DOM," + name + stati2 + "_ASS#10");
	}

	public static Result simit(String deck1, String deck2) {
		TUO.Param p = new TUO.Param(deck1, deck2, OP.sim, Order.random, Mode.pvp_defense, Dom.dom_maxed, "", 100, 0);
		Result r = TUO.sim(p);
		return r;
	}

}
