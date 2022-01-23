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

	public static int size = 20;
	public static String name = "DR_F3LL";
	public static String[] stati = new String[size];
	public static double[] scores = new double[size];
	public static String[] good =new String [] {
					"c88a1b3f385e42f6a9d6f939b30056fb",

					"a4dcb785345b4dc09a238b0b8e2c4c80",

					"cda050ffa6ec42b6b1596ddba65ed96f",

					"a36f0240c46f4d4696c0f5c784eaa2b6",

					"9ddebbab1f9742afb55bab50cea2dc77",

					"1b5171d618e8498fbdc7da16cdf573e4",

					"29e5e66556c74c9093074f0cd02c3f15",

					"0f4e7a2ed3f84be0972dcdc61e3932f9",

					"c09c181e769b45489d77191164b0d211",

					"0435792faceb4144b16a9f857cbad09a",

					"74a01dd15dca4242aebba6f90b026ca1",

					"f7136615a5a74af182abb039426e53db",

					"5eb9384288974921895a9024920cf59f",

					"f811740fe485461f8ad2d597bec975fa",

					"72ec641ecd2f407886f38d70e6800f41",
					/*
			"c88a1b3f385e42f6a9d6f939b30056fb",
			"d85b829b173344ee9cdd9c4f149a72ff",
			"98c876af12ca42fc99ced86241b5fae5",
			"12d98fd1f24d4708a1773d661416f76f",
			"b336d349eb6e47b784779f1c5b34a915",
			"56497d1a85314b108fe834bdd36515ce",
			"9c2ffc6aa4ed492f9cc6e0804747a216",
			"2c748951f6ab46bcb768f48a35a62134",
			"8a57f02ca3f743ae81c084b10460099b",
			"d6910b0285fb43cf9cc28b43728960af",
			"a586a85de1ff4b47889fbedd8a2e6434",
			"935ac4d301f2487bbcbc326e339eff0e",
			"fd7b882bc9e74de6b6b7d48d74ca6820",
			"0886179bb0fa451f916b0c04e3b7a434",
			"48ddd71daf4940649a3d15db7e5eab58",
			"7414ab268ad040adab38e8a5bae4f09a",
			"9a47d38fd2b34896aa3c555cc3834e35",
			"ad43b35b743342a49bab4c8116a97e47",
			"138e4be447ae4af7b5de39c8bfbed2b8",
			"b53bfbec66d74e81899293f5ad7d0a4a",
			"f7136615a5a74af182abb039426e53db",
			"d6797b970f744bda9ba1552d70de670d",
			"c88a1b3f385e42f6a9d6f939b30056fb",
			"d85b829b173344ee9cdd9c4f149a72ff",
			"98c876af12ca42fc99ced86241b5fae5",
			"b336d349eb6e47b784779f1c5b34a915",
			"dafb8314cbf34d109d8354d47e246215",
			"56497d1a85314b108fe834bdd36515ce",
			"441f433696e848efa9eb51c996a14a50",
			"44015c818857426f9614564a4b832053",
			"9c2ffc6aa4ed492f9cc6e0804747a216",
			"ced85010852f41d795f99ba8e87839ed",
			"12d98fd1f24d4708a1773d661416f76f",
			"2c748951f6ab46bcb768f48a35a62134",
			"8a57f02ca3f743ae81c084b10460099b",
			"935ac4d301f2487bbcbc326e339eff0e",
			"9a47d38fd2b34896aa3c555cc3834e35",
			"500a758cd60c431bb0123146adc949d2",
			"416d61306a994e399f078127c2f2ebf1",
			"591a7f79acfa4c02a18ce9d1f13dc782",
			*/

	};

	public static void main(String[] args) throws FileNotFoundException, IOException {
		IOUtils.write("<?xml version='1.0' encoding='UTF-8'?>\r\n" + "<root></root>",
				new FileOutputStream(new File("data/cards_section_" + (20) + ".xml")), "UTF-8");
		GlobalData.init(false);

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
