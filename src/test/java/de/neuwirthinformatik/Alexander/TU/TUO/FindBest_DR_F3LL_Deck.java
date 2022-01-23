package de.neuwirthinformatik.Alexander.TU.TUO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

import de.neuwirthinformatik.Alexander.GitJarUpdate.Task;
import de.neuwirthinformatik.Alexander.TU.Basic.Card;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.CardInstance;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.CardType;
import de.neuwirthinformatik.Alexander.TU.Basic.Gen;
import de.neuwirthinformatik.Alexander.TU.Basic.GlobalData;
import de.neuwirthinformatik.Alexander.TU.TUO.TUO.Param.Dom;
import de.neuwirthinformatik.Alexander.TU.TUO.TUO.Param.Mode;
import de.neuwirthinformatik.Alexander.TU.TUO.TUO.Param.OP;
import de.neuwirthinformatik.Alexander.TU.TUO.TUO.Param.Order;
import de.neuwirthinformatik.Alexander.TU.TUO.TUO.Result;

public class FindBest_DR_F3LL_Deck {
	static class Pair implements Comparable<Pair> {
		double score;
		String stati;

		Pair(double sco, String st) {
			this.score = sco;
			this.stati = st;
		}

		public int compareTo(Pair other) // making it only compare a2 values
		{
			return (int)(-this.score + other.score);
		}
	}

	public static int size = 10;
	public static String name = "DR_F3LL";
	public static String[] stati = new String[size];
	public static double[] scores = new double[size];
	public static String[] good =new String [] {
			"c88a1b3f385e42f6a9d6f939b30056fb"

	};

	public static void main(String[] args) throws FileNotFoundException, IOException {
		IOUtils.write("<?xml version='1.0' encoding='UTF-8'?>\r\n" + "<root></root>",
				new FileOutputStream(new File("data/cards_section_" + (20) + ".xml")), "UTF-8");
		GlobalData.init();

		TUO.tuo_out = false;
		TUO.threads = 16;
		for (int i = 0; i < stati.length; i++) {
			if( i >= good.length) {
			stati[i] = UUID.randomUUID().toString().replaceAll("-", "");
			}
			else {
				stati[i] = good[i];
			}
			Card a = Gen.genCardInstance(name + stati[i], (name + stati[i]).hashCode(), CardType.ASSAULT).getCard();
			Card c = Gen.genCardInstance(name + stati[i] + "_COM", (name + stati[i]).hashCode(), true).getCard();
			Card d = Gen.genCardInstance(name + stati[i] + "_DOM", (name + stati[i]).hashCode(), CardType.DOMINION).getCard();
			GlobalData.xml.appendToCardSection(20, a);
			GlobalData.xml.appendToCardSection(20, c);
			GlobalData.xml.appendToCardSection(20, d);
		}
		for (int i = 0; i < stati.length; i++) {
			System.out.println((i*100)/size +"%");
			final int ii = i;
			String g = deck(stati[0]);
			for (int j = 1; j < stati.length; j++) {
				g = appendDeck(g, deck(stati[j]));
			}
			scores[ii] =  simit(deck(stati[ii]) , g).WINS;
		}
		Task.sleepForAll();
		Pair[] pairs = new Pair[stati.length];

		for (int i = 0; i < pairs.length; i++) {
			pairs[i] = new Pair(scores[i], stati[i]);
		}
		Arrays.sort(pairs);
		// printing values
		for (int i = 0; i < 20; i++) {
			System.out.println(((double)pairs[i].score)  + "% " + pairs[i].stati);
			CardInstance a = Gen.genCardInstance(name + pairs[i].stati, (name + pairs[i].stati).hashCode(), CardType.ASSAULT);
			CardInstance c = Gen.genCardInstance(name + pairs[i].stati + "_COM", (name + pairs[i].stati).hashCode(), true);
			CardInstance d = Gen.genCardInstance(name + pairs[i].stati + "_DOM", (name + pairs[i].stati).hashCode(), CardType.DOMINION);
			System.out.println(a.getInfo());
			System.out.println(c.getInfo());
			System.out.println(d.getInfo());
		}
		for (int i = 0; i < 20; i++) {
			System.out.println("\"" + pairs[i].stati+ "\",");
		}

	}
	public static String appendDeck(String a1,String b2) {
		return a1+ ";" + b2 ;
	}
	
	public static String deck(String stati1) {
		return name + stati1 + "_COM," + name + stati1 + "_DOM," + name + stati1 + "#10";
	}

	public static Result sim(String stati1, String stati2) throws FileNotFoundException, IOException {

		return simit(name + stati1 + "_COM," + name + stati1 + "_DOM," + name + stati1 + "#10",
				name + stati2 + "_COM," + name + stati2 + "_DOM," + name + stati2 + "#10");
	}

	public static Result simit(String deck1, String deck2) {
		TUO.Param p = new TUO.Param(deck1, deck2, OP.sim, Order.random, Mode.pvp_defense, Dom.dom_maxed, "", 1000, 0);
		//p.flags = "prefix /home/apn/git/TULib/";
		Result r = TUO.sim(p);
		return r;
	}

}
