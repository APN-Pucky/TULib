package de.neuwirthinformatik.Alexander.TU.TUO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.neuwirthinformatik.Alexander.GitJarUpdate.Info;
import de.neuwirthinformatik.Alexander.GitJarUpdate.Info.OS;
import de.neuwirthinformatik.Alexander.TU.TU;
import de.neuwirthinformatik.Alexander.TU.Basic.GlobalData;
import de.neuwirthinformatik.Alexander.TU.TUO.TUO.Param.OP;
import de.neuwirthinformatik.Alexander.TU.util.StringUtil;

public class TUO {
	public static int threads = 1;
	public static String tuo = "tuo";
	public static boolean force_full_deck = false;
	public static boolean tuo_out = true;
	public static final String wd = "";

	public static ProcessBuilder pbb(String name, Param p) {
		boolean bge = !(p.BGE.equals("") || p.BGE.equalsIgnoreCase("none"));
		boolean hand = !p.hand.equals("");
		boolean enemy_hand = !p.enemy_hand.equals("");
		boolean yf = !p.yf.equals("");
		boolean ef = !p.ef.equals("");
		boolean ye = !p.ye.equals("");
		boolean ee = !p.ee.equals("");
		// boolean dom = !p.dom.equals("");
		boolean fight_surge = !p.fight_surge.equals("");
		// boolean eg = p.endgame>-1;

		boolean climb = p.op == OP.climb;
		boolean climbex = p.op == OP.climb_ex;
		boolean anneal = p.op == OP.anneal;

		ArrayList<String> param = new ArrayList<String>();

		param.add(wd + tuo);
		param.add("\"" + p.deck + "\"");
		param.add("\"" + p.enemies + "\"");
		param.add(p.mode.toString());
		param.add(p.order.toString());
		if (yf) {
			param.add("yf");
			param.add("\"" + p.yf + "\"");
		}
		if (ef) {
			param.add("ef");
			param.add("\"" + p.ef + "\"");
		}
		if (ye) {
			param.add("ye");
			param.add("\"" + p.ye + "\"");
		}
		if (ee) {
			param.add("ee");
			param.add("\"" + p.ee + "\"");
		}
		param.add(p.dom.toString());
		if (bge) {
			param.add("-e");
			if (Info.detectOS() == OS.WINDOWS)
				param.add("\"" + p.BGE + "\"");
			else
				param.add(p.BGE);
		}
		if (hand) {
			param.add("hand");
			param.add("\"" + p.hand + "\"");
		}
		if (enemy_hand) {
			param.add("enemy:hand");
			param.add("\"" + p.enemy_hand + "\"");
		}
		if (p.enemy_ordered) {
			param.add("enemy:ordered");
		}
		param.add("endgame");
		param.add("" + p.endgame);
		param.add("fund");
		param.add("" + p.fund);
		if (p.freeze > 0) {
			param.add("freeze");
			param.add("" + p.freeze);
		}
		if (p.op != OP.reorder && force_full_deck) {
			param.add("-L");
			param.add("" + 10);
			param.add("" + 10);
		}

		if (climb || climbex || anneal)
			param.add("-o=data/ownedcards_" + name + ".txt");
		param.add(p.op.toString());
		if (climbex) {
			param.add("" + p.iterations[1]);
		}
		param.add("" + p.iterations[0]);
		if (anneal) {
			param.add("" + p.iterations[1]);
			param.add(String.format(Locale.US, "%.9f", p.iterations[2]));
		}
		if (fight_surge) {
			param.add(p.fight_surge);
		}
		param.add("-t");
		param.add("" + threads);

		if (name.startsWith("Mono")) {
			param.add("-m");
			switch (name.substring(4, 6).toUpperCase()) {
			case ("BT"):
				param.add("bloodthirsty");
				break;
			case ("IM"):
				param.add("imperial");
				break;
			case ("RD"):
				param.add("raider");
				break;
			case ("XE"):
				param.add("xeno");
				break;
			case ("RT"):
				param.add("righteous");
				break;
			case ("PR"):
				param.add("progenitor");
				break;
			default:
				param.remove(param.size() - 1);
			}
		}

		// flags
		for (String s : StringUtil.splitByQuotesAndSpaces(p.flags)) {
			param.add(s);
		}

		String para = "";
		for (String s : param) {
			para += s + " ";
		}
		TU.log.m(para, "TUO", name);

		return new ProcessBuilder(param);
	}

	public static String climb(String b, Param param,OP algo) {
		//param.op = TUM.settings.climb_algorithm();
		param.op = algo;

		return climbAny(b, param);
	}

	public static String reorder(String b, Param param) {
		param.op = OP.reorder;

		return climbAny(b, param);
	}

	public static String climbAny(String b, Param param) {

		StringBuilder result = new StringBuilder();

		try {
			ProcessBuilder pb = pbb(b, param);
			Process process = pb.start();

			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			// BufferedReader br = new BufferedReader(new
			// InputStreamReader(process.getErrorStream()));

			String line = null, err_line = null;
			while ((line = br.readLine()) != null) {
				result.append(line);
				result.append("\n");

				if (tuo_out) {
					TU.log.m(line, "TUO", b);
				}
			}
			process.waitFor();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return parseClimbReturn(result.toString(), b);
	}

	public static Result sim(Param param) {
		return sim("", param);
	}

	public static Result sim(String b, Param param) {
		param.op = OP.sim;
		StringBuilder result = new StringBuilder();
		try {
			ProcessBuilder pb = pbb(b, param);

			// pb.directory(new File(wd));
			Process process = pb.start();
			// System.out.println(pb.directory().toString());

			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			// BufferedReader br = new BufferedReader(new
			// InputStreamReader(process.getErrorStream()));
			String line;
			

			while ((line = br.readLine()) != null) {
				result.append(line);
				result.append("\n");
				if (tuo_out)
					TU.log.m(line, "TUO", b);
			}
			process.waitFor();
			// System.out.println("EXIT CODE: " + process.exitValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parseSimReturn(result.toString(), b);

	}

	public static String parseClimbReturn(String s,String name) {
		String[] lines = s.split("\n");

		String[] parts = lines[lines.length - 1].split(":");
		String deck = GlobalData.removeHash(parts[parts.length - 1]);

		return deck;
	}

	public static Result parseSimReturn(String s,String name) {
		System.out.println(s);
		String[] lines = s.split("\n");

		Pattern p = Pattern.compile("\\d+\\.?\\d?\\d?");
		int score_lines = 0;
		double score = 0;
		int ii=0;
		for(ii =0 ; ii < lines.length;ii++) {if(lines[ii].contains("score:"))break;}
		if (ii != lines.length && lines[ii].contains("score:")) {
			Matcher m_SCORE = p.matcher(lines[lines.length - 1]);
			// m_LOSS.find();m_LOSS.group()
			m_SCORE.find();
			score = Double.parseDouble(m_SCORE.group());
			score_lines = 1;
		}

		// Matcher m_SIM = p.matcher(lines[lines.length-3-score_lines]);
		// m_SIM.find();m_SIM.group();m_SIM.find();m_SIM.group();m_SIM.find();
		// int sim = Integer.parseInt(m_SIM.group());
		for(ii =0 ; ii < lines.length;ii++) {if(lines[ii].contains("win%:"))break;}
		Matcher m_WIN = p.matcher(lines[ii]);
		// m_WIN.find();m_WIN.group();
		m_WIN.find();
		double win = Double.parseDouble(m_WIN.group());

		for(ii =0 ; ii < lines.length;ii++) {if(lines[ii].contains("stall%:"))break;}
		Matcher m_STALL = p.matcher(lines[ii]);
		// m_STALL.find();m_STALL.group();
		m_STALL.find();
		double stall = Double.parseDouble(m_STALL.group());

		for(ii =0 ; ii < lines.length;ii++) {if(lines[ii].contains("loss%:"))break;}
		Matcher m_LOSS = p.matcher(lines[ii]);
		// m_LOSS.find();m_LOSS.group()
		m_LOSS.find();
		double loss = Double.parseDouble(m_LOSS.group());
		if(score_lines ==1)TU.log.m(lines[lines.length-4], "TUO","sim", name);
		TU.log.m(lines[lines.length-3], "TUO","sim", name);
		TU.log.m(lines[lines.length-2], "TUO","sim", name);
		TU.log.m(lines[lines.length-1], "TUO","sim", name);
		return new Result(win, stall, loss, score);
	}

	public static class Result {
		// public int SIM;
		public double WINS = 0;
		public double STALLS = 0;
		public double LOSSES = 100;
		public double SCORE = 0;

		public Result(double w, double s, double l, double sc) {
			WINS = w;
			STALLS = s;
			LOSSES = l;
			SCORE = sc;
		}

		public double lost(Param.Mode m) {
			return 100 - won(m);
		}

		public double won(Param.Mode m) {
			return WINS + (m.stall_is_win ? STALLS : 0.);
		}
	}

	public static class Param {
		public enum Mode {
			pvp("pvp"), pvp_defense("pvp-defense", true), brawl("brawl"), brawl_defense("brawl-defense", true),
			cq("cq"), cq_defense("cq-defense", true), gw("gw"), gw_defense("gw-defense", true), raid("raid"),
			campaign("campaign"), surge("surge");

			private String p;
			private boolean stall_is_win = false;

			private Mode(String n) {
				p = n;
			}

			private Mode(String n, boolean stall) {
				p = n;
				stall_is_win = stall;
			}

			public static Mode get(String s) {
				for (Mode m : values())
					if (m.p.equalsIgnoreCase(s))
						return m;
				if ("battle".equalsIgnoreCase(s))
					return pvp;
				return null;
			}

			public String toString() {
				return p;
			}
		}

		public enum Dom {
			dom_owned("dom-owned"), dom_maxed("dom-maxed"), dom_none("dom-none");

			private String p;

			private Dom(String n) {
				p = n;
			}

			public static Dom get(String s) {
				for (Dom m : values())
					if (m.p.equalsIgnoreCase(s))
						return m;
				return null;
			}

			public String toString() {
				return p;
			}
		}

		public enum Order {
			random("random"), ordered("ordered"), exact_ordered("exact-ordered"), flexible("flexible");

			private String p;

			private Order(String n) {
				p = n;
			}

			public static Order get(String s) {
				for (Order m : values())
					if (m.p.equalsIgnoreCase(s))
						return m;
				return null;
			}

			public String toString() {
				return p;
			}
		}

		public enum OP {
			climb("climb"), climb_ex("climbex"), anneal("anneal"), sim("sim"), reorder("reorder"), genetic("genetic"),
			beam("beam");

			private String p;

			private OP(String n) {
				p = n;
			}

			public String toString() {
				return p;
			}

			public static OP get(String s) {
				for (OP m : values())
					if (m.p.equalsIgnoreCase(s))
						return m;
				return null;
			}

			public static OP[] valuesClimb() {
				return Arrays.stream(OP.values()).filter(o -> (o != OP.sim && o != OP.reorder)).toArray(OP[]::new);
			}
		}

		public enum Endgame {
			none(0), maxed(1), fused(2);

			private int p;

			private Endgame(int n) {
				p = n;
			}

			public static Endgame get(String s) {
				for (Endgame m : values())
					if (m.toString().equalsIgnoreCase(s))
						return m;
				return null;
			}

			public static Endgame get(int s) {
				for (Endgame m : values())
					if (m.p == s)
						return m;
				return null;
			}

			public String toString() {
				return "" + p;
			}

			public int toInt() {
				return p;
			}
		}

		public String deck;
		public int[] inv;
		public String enemies;
		public OP op;
		public Order order;
		public Mode mode;
		public String BGE = "";
		public Dom dom;
		public String ye, ee;
		public String yf, ef;
		public String hand = "";
		public String enemy_hand = "";
		public String fight_surge = "";
		public double[] iterations;
		public int fund;
		public Endgame endgame;
		public int freeze;
		public boolean enemy_ordered = false;
		public int min_size = 1;
		public int max_size = 10;
		public String flags;

		public Param(String deck, String enemies, OP o, Order ord, Mode m, Dom dom, String yf, String ef, String ye,
				String ee, String bge, double[] iter, int fund, Endgame eg, String flags) {
			this.deck = deck;
			this.enemies = enemies;
			op = o;
			order = ord;
			mode = m;
			BGE = bge;
			iterations = iter;
			this.fund = fund;
			this.yf = yf;
			this.ef = ef;
			this.ye = ye;
			this.ee = ee;
			endgame = eg;
			this.dom = dom;
			this.flags = flags;
		}

		public Param(String deck, String enemies, OP o, Order ord, Mode m, Dom dom, String yf, String ef, String ye,
				String ee, String bge, int iter, int fund, Endgame eg) {
			this(deck, enemies, o, ord, m, dom, yf, ef, ye, ee, bge, new double[] { iter }, fund, eg, "");
		}

		public Param(String deck, String enemies, OP o, Order ord, Mode m, Dom dom, String bge, int iter, int fund) {
			this(deck, enemies, o, ord, m, dom, "", "", "", "", bge, iter, fund, Endgame.fused);
		}

		public Param(String deck, String enemies, OP o, Order ord, Mode m, Dom dom, String bge, double[] iter,
				int fund) {
			this(deck, enemies, o, ord, m, dom, "", "", "", "", bge, iter, fund, Endgame.fused, "");
		}

		public Param(String deck, String enemies, OP o, Order ord, Mode m, Dom dom, String bge, double... iter) {
			this(deck, enemies, o, ord, m, dom, "", "", "", "", bge, iter, 0, Endgame.fused, "");
		}

		/*
		public Param(String deck, String enemies, double... iter) {
			this(deck, enemies, OP.sim, TUM.settings.order(), TUM.settings.mode(), TUM.settings.dom(),
					TUM.settings.yf(), TUM.settings.ef(), TUM.settings.ye(), TUM.settings.ee(), TUM.settings.bge(),
					iter, 0, TUM.settings.endgame(), TUM.settings.flags());
		}
		*/

		/*
		public Param(String deck, double... iter) {
			this(deck, TUM.settings.enemy_deck(), iter);
		}
		*/
	}

}
