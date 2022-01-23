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

	public static int size = 250;
	public static String name = "DR_F3LL";
	public static String[] stati = new String[size];
	public static double[] scores = new double[size];
	public static String[] good =new String [] {
			"cda050ffa6ec42b6b1596ddba65ed96f",
			"a3da65ce2c004506aa78dc5c7e77b0ec",
			"3fa751bd85bb41d0a8bf123b47e2035f",
			"7ee29d691a714f1f95205c0fa17d01ee",
			"1b5171d618e8498fbdc7da16cdf573e4",
			"4010118e0b2a456f9867d90734cc9f5c",
			"ca797b485cec432c87f8f64af2f0ff14",
			"a4dcb785345b4dc09a238b0b8e2c4c80",
			"76e8895750514996b2a0a31da9b2a156",
			"c88a1b3f385e42f6a9d6f939b30056fb",
			"66676590bcc7457389b8a67bc360baaa",
			"cf069550949840fc83773d02dc3d774a",
			"98909c5d453e4d268abe297623a55ba8",
			"9ddebbab1f9742afb55bab50cea2dc77",
			"a36f0240c46f4d4696c0f5c784eaa2b6",
			"cda050ffa6ec42b6b1596ddba65ed96f",
			"1b5171d618e8498fbdc7da16cdf573e4",
			"9ddebbab1f9742afb55bab50cea2dc77",
			"98909c5d453e4d268abe297623a55ba8",
			"7e496faa436641c99707ad0125fcd418",
			"a3da65ce2c004506aa78dc5c7e77b0ec",
			"3fa751bd85bb41d0a8bf123b47e2035f",
			"ca797b485cec432c87f8f64af2f0ff14",
			"eb422f45d1da44d7b51ba33cd61c736c",
			"7ee29d691a714f1f95205c0fa17d01ee",
			"4010118e0b2a456f9867d90734cc9f5c",
			"66676590bcc7457389b8a67bc360baaa",
			"cf069550949840fc83773d02dc3d774a",
			"5296d6df77a040f8900874e028e60bd9",
			"a36f0240c46f4d4696c0f5c784eaa2b6",
			"84b6c0d94bc24238bedc261d9f049522",
			"a4dcb785345b4dc09a238b0b8e2c4c80",
			"48d8fc1516a9472e9f39447c9a75ed06",
			"f69fdb54229d40cba47fa8b83c6437b6",
			"76e8895750514996b2a0a31da9b2a156",

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
