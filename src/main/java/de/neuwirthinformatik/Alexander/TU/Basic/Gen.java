package de.neuwirthinformatik.Alexander.TU.Basic;

import java.util.ArrayList;
import java.util.Random;

import de.neuwirthinformatik.Alexander.TU.Basic.Card.CardCategory;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.CardInstance;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.CardInstance.Info;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.CardType;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.Faction;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.Level;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.Rarity;
import de.neuwirthinformatik.Alexander.TU.util.StringUtil;

public class Gen {
	// gen
	private static final int pool_size = 100;
	private static final int generations = 10;
	private static final double mutate_percentage = 0.3;
	private static final double crossover_percentage = 0.3;

	private static final double dom_probabilty = 0.05;
	private static final double com_probabilty = 0.25;
	private static final double struct_probabilty = 0.35;

	private static final double level2_probabilty = 0.10;
	private static final double level1_probabilty = 0.25;

	private static final double mythic_probabilty = 0.01;
	private static final double vindicator_probabilty = 0.05;
	private static final double legendary_probabilty = 0.15;
	private static final double epic_probabilty = 0.45;
	private static final double rare_probabilty = 0.75;
	// card
	private static final double mutate_attack_percent = 0.05;
	private static final double mutate_health_percent = 0.05;
	private static final double mutate_cost_probability = 0.01;

	// skill
	private static final double mutate_x_percent = 0.05;
	private static final double mutate_n_probability = 0.01;
	private static final double mutate_c_probability = 0.01;
	private static final double mutate_all_probability = 0.01;
	private static final double mutate_y_probability = 0.01;
	private static final double mutate_trigger_probability = 0.01;

	private static Random r = new Random();

	public static void main(String[] args) {
		GlobalData.init();
		String name = "DR_F3LL";
		int seed = name.hashCode();
		CardInstance ci = Gen.genCardInstance(name, seed, true);
		System.out.println(ci.getInfo());
		System.out.println(genCardType(ci.getInfo()));
	}

	public static CardInstance.Info getSingleInfo(int seedr) {
		return getSingleInfo(seedr, false);
	}

	public static CardInstance.Info getSingleInfo(int seedr, boolean force_com) {
		r.setSeed(seedr);
		CardInstance.Info[] is = genInfo(seedr, force_com);
		return is[r.nextInt(is.length)];
	}

	public static CardInstance.Info[] genInfo(int seedr) {
		return genInfo(seedr, false);
	}

	public static CardInstance.Info[] genInfo(int seedr, boolean force_com) {
		ArrayList<Card> printed = new ArrayList<Card>();
		int number = pool_size;
		CardInstance.Info[] is = new CardInstance.Info[number];
		int offset = GlobalData.all_cards.length;
		for (int i = 1; i < GlobalData.all_cards.length && number > 0; i++) {
			Card c = GlobalData.all_cards[offset - i];
			if (c != null && c.fusion_level == 2 && !printed.contains(c)
					&& !c.getName().toLowerCase().startsWith("test")
					&& !c.getName().toLowerCase().startsWith("revolt ranger")
					&& !c.getName().toLowerCase().startsWith("cephalodjinn")) {
				printed.add(c);
				number--;
				is[number] = GlobalData.getCardInstanceById(c.getHighestID()).getInfo();
			}
		}

		gen(is, force_com);
		return is;
	}

	public static CardType genCardType(CardInstance.Info t) {
		if (couldBeStruct(t) && r.nextDouble() < dom_probabilty) {
			return CardType.DOMINION;
		}
		if (couldBeCommander(t) && r.nextDouble() < com_probabilty) {
			return CardType.COMMANDER;
		}
		if (couldBeStruct(t) && r.nextDouble() < struct_probabilty) {
			return CardType.STRUCTURE;
		}

		return CardType.ASSAULT;
	}

	public static Level genLevel() {
		double d = r.nextDouble();
		if (d < level2_probabilty) {
			return Level.quad;
		}
		if (d < level1_probabilty) {
			return Level.dual;
		} else {
			return Level.single;
		}
	}

	public static Rarity genRarity() {
		double d = r.nextDouble();
		if (d < mythic_probabilty) {
			return Rarity.mythic;
		}
		if (d < vindicator_probabilty) {
			return Rarity.vindicator;
		}
		if (d < legendary_probabilty) {
			return Rarity.legendary;
		}
		if (d < epic_probabilty) {
			return Rarity.epic;
		}
		if (d < rare_probabilty) {
			return Rarity.rare;
		}
		return Rarity.common;
	}

	public static Faction genFaction(CardInstance.Info t) {
		Faction faction = Faction.get(1 + r.nextInt(6));
		for (SkillSpec ss : t.getSkills()) {
			if (!ss.getY().equals("allfactions")) {
				faction = Faction.get(ss.getY());
			}
		}
		return faction;
	}

	public interface CardInstanceRequirement {
		boolean check(CardInstance param1);
	}

	public static CardInstance genCardInstance(String name, int seed) {
		return genCardInstance(name, seed, (ci) -> true);
	}

	public static CardInstance genCardInstance(String name, int seed, CardInstanceRequirement cir) {
		return genCardInstance(name, seed, cir, false);
	}

	public static CardInstance genCardInstance(String name, int seed, boolean force_com) {
		return genCardInstance(name, seed, (ci) -> true, force_com);
	}

	public static CardInstance genCardInstance(String name, int seed, CardInstanceRequirement cir, boolean force_com) {
		CardInstance.Info i = Gen.getSingleInfo(seed, force_com);
		final CardInstanceRequirement tmp = cir;
		if (force_com) {
			cir = (ccc) -> {
				return tmp.check(ccc) && ccc.getCardType() == CardType.COMMANDER;
			};
		}
		int did_num = 999999;
		int mrank = 6;
		int rank = 6;
		Info[] ia = new Info[mrank];
		int[] ids = new int[mrank];
		for (int j = 0; j < ids.length; j++)
			ids[j] = did_num + j;
		// ids[rank-1] = 2; // TODO Define card id somewhere close to name
		ia[rank - 1] = i;
		Card c = new Card(ids, name, genRarity().toInt(), genLevel().toInt(), new int[] {}, 0, 0, genFaction(i).toInt(),
				ia, "", 0, Gen.genCardType(i), CardCategory.NORMAL);
		CardInstance ci = CardInstance.get(999999 + rank - 1, c, i);
		if (!check(ci) || !cir.check(ci)) {
			return genCardInstance(name, seed + 1, tmp, force_com);
		}
		return ci;
	}

	public static String gen(int seedr) {
		CardInstance.Info[] is = genInfo(seedr);
		String msg = "";
		String faction = "allfaction";
		while (faction.equals("allfaction"))
			faction = GlobalData.factionToString(r.nextInt(7));
		boolean summon;
		int i = 1;
		do {
			summon = false;
			CardInstance.Info t = is[i - 1];
			String desc = t.description();
			String type;
			if (couldBeStruct(t) && r.nextDouble() < struct_probabilty) {
				t = new CardInstance.Info(0, t.getHealth(), t.getCost(), t.getLevel(), t.getSkills());
				desc = t.description();
				type = "Structure";
			} else {
				type = "Assault";
			}
			for (SkillSpec ss : t.getSkills()) {
				if (!ss.getY().equals("allfactions")) {
					faction = ss.getY();
				}
				if (ss.getId().equals("summon")) {
					summon = true;
					desc = desc.replace(GlobalData.getNameAndLevelByID(ss.getCard_id()), "Gen #" + (i + 1));
				}
			}
			msg += "Gen #" + i + "\n";
			msg += StringUtil.capitalizeOnlyFirstLetters(faction) + " ";

			msg += type;
			msg += "\n";
			msg += desc;
			msg += "\n\n";
			i++;
		} while (summon && i < is.length);
		return StringUtil.removeLastCharacter(msg, 2);
	}

	public static String gen() {
		return gen(0);
	}

	private static CardInstance.Info gen(CardInstance.Info[] is) {
		return gen(is, false);
	}

	private static CardInstance.Info gen(CardInstance.Info[] is, boolean force_com) {
		for (int i = 0; i < generations * is.length; i++) {
			int i1 = r.nextInt(is.length);
			int i2 = r.nextInt(is.length);
			int i3 = r.nextInt(is.length);
			is[i1] = mutate(is[i1], force_com);
			CardInstance.Info tmp = crossover(is[i2], is[i3], force_com);
			is[i2] = crossover(is[i2], is[i3], force_com);
			is[i3] = tmp;
			// System.out.println(tmp.getCost());
		}
		return is[r.nextInt(is.length)];
	}

	private static boolean check(CardInstance.Info i) {
		int errr = checkInfo(i);
		// System.out.println(errr);
		if (errr < 0)
			return false;
		return true;
	}

	private static boolean check(CardInstance ci) {
		int errr = checkCardInstance(ci);
		// System.out.println(errr);
		if (errr < 0)
			return false;
		return true;
	}

	private static int checkCardInstance(CardInstance ci) {
		boolean wall = false;
		Info i = ci.getInfo();
		for (int j = 0; j < i.getSkills().length; j++) {
			if (i.getSkills()[j].getId().equals("wall"))
				wall = true;
		}
		if (ci.getCardType() == CardType.DOMINION || ci.getCardType() == CardType.STRUCTURE) {
			for (int j = 0; j < i.getSkills().length; j++) {
				if (i.getSkills()[j].getId().equals("counter") && !wall) {
					return -4;
				}
			}

		}
		if (ci.getCardType() == CardType.DOMINION || ci.getCardType() == CardType.COMMANDER) {
			for (int j = 0; j < i.getSkills().length; j++) {
				if (i.getSkills()[j].getTrigger().equals("play")) {
					return -7;
				}
			}
		}
		if (ci.getCardType() == CardType.STRUCTURE || ci.getCardType() == CardType.DOMINION) {
			if (!couldBeStruct(ci.getInfo())) {
				return -6;
			}
			for (int j = 0; j < i.getSkills().length; j++) {
				if (i.getSkills()[j].getTrigger().equals("attacked") && !wall) {
					return -1;
				}

			}
		}
		if (ci.getCardType() == CardType.COMMANDER) {
			if (!couldBeCommander(ci.getInfo())) {
				return -5;
			}
			if (wall)
				return -2;
			for (int j = 0; j < i.getSkills().length; j++) {
				if (i.getSkills()[j].getTrigger().equals("death")) {
					return -3;
				}
				if (i.getSkills()[j].getId().equals("evade")) {
					return -4;
				}
			}

		}

		return 0;
	}

	private static int checkInfo(CardInstance.Info i) {
		boolean wall = false;
		boolean flurry = false;
		for (int j = 0; j < i.getSkills().length; j++) {
			if (i.getSkills()[j].getX() == 0 && !i.getSkills()[j].getId().equals("wall")
					&& !i.getSkills()[j].getId().equals("jam") && !i.getSkills()[j].getId().equals("summon")
					&& !i.getSkills()[j].getId().equals("flurry") && !i.getSkills()[j].getId().equals("overload"))
				return -1;
			if (i.getSkills()[j].isAll()
					&& (!couldBeTrigger(i.getSkills()[j]) || i.getSkills()[j].getId().equals("mimic")))
				return -2;
			if (!i.getSkills()[j].getTrigger().equals("activate")
					&& (!couldBeTrigger(i.getSkills()[j]) || i.getSkills()[j].getC() > 0))
				return -3;
			if (i.getSkills()[j].getId().equals("summon") && i.getSkills()[j].isAll())
				return -9;
			if (i.getSkills()[j].getId().equals("wall"))
				wall = true;
			if (i.getSkills()[j].getId().equals("flurry"))
				flurry = true;
			for (int k = 0; k < i.getSkills().length; k++) {
				if (k != j && i.getSkills()[j].getId().equals(i.getSkills()[k].getId()))
					return -4;
			}
		}
		if (flurry) {
			for (SkillSpec s : i.getSkills()) {
				if (s.getId().equals("jam"))
					return -6;
			}
		}
		if (wall) {
			if (i.getAttack() != 0)
				return -8;
			if (!couldBeStruct(i))
				return -7;
		}
		return 0;
	}

	private static boolean couldBeTrigger(SkillSpec s) {
		return (s.getId().equals("enfeeble") || s.getId().equals("strike") || s.getId().equals("mortar")
				|| s.getId().equals("siege") || s.getId().equals("sunder") || s.getId().equals("weaken")
				|| s.getId().equals("overload") || s.getId().equals("protect") || s.getId().equals("rally")
				|| s.getId().equals("entrap") || s.getId().equals("jam") || s.getId().equals("mimic")
				|| s.getId().equals("enrage") || s.getId().equals("rush") || s.getId().equals("heal")
				|| s.getId().equals("mend") || s.getId().equals("fortify") || s.getId().equals("summon")
				|| s.getId().equals("enhance") || s.getId().equals("evolve"));
	}

	private static boolean couldBeCommander(CardInstance.Info i) {
		for (SkillSpec s : i.getSkills()) {
			if (s.getId().equals("wall")) {
				return false;
			}
		}
		return couldBeStruct(i) && i.getCost() == 0;
	}

	private static boolean couldBeStruct(CardInstance.Info i) {
		for (SkillSpec s : i.getSkills()) {
			if (s.getId().equals("armor"))
				return false;
			if (s.getId().equals("fortify"))
				return false;
			if (s.getId().equals("mend"))
				return false;
			if (s.getId().equals("avenge"))
				return false;
			if (s.getId().equals("scavenge"))
				return false;
			if (s.getId().equals("payback"))
				return false;
			if (s.getId().equals("revenge"))
				return false;
			if (s.getId().equals("tribute"))
				return false;
			if (s.getId().equals("coalition"))
				return false;
			if (s.getId().equals("legion"))
				return false;
			if (s.getId().equals("pierce"))
				return false;
			if (s.getId().equals("rupture"))
				return false;
			if (s.getId().equals("swipe"))
				return false;
			if (s.getId().equals("drain"))
				return false;
			if (s.getId().equals("venom"))
				return false;
			if (s.getId().equals("hunt"))
				return false;
			if (s.getId().equals("mark"))
				return false;
			if (s.getId().equals("berserk"))
				return false;
			if (s.getId().equals("inhibit"))
				return false;
			if (s.getId().equals("sabotage"))
				return false;
			if (s.getId().equals("leech"))
				return false;
			if (s.getId().equals("poison"))
				return false;
			if (s.getId().equals("allegiance"))
				return false;
			if (s.getId().equals("valor"))
				return false;
			if (s.getId().equals("bravery"))
				return false;
		}
		return true;
	}

	public static CardInstance.Info crossover(CardInstance.Info i1, CardInstance.Info i2) {
		return crossover(i1, i2, false);
	}

	public static CardInstance.Info crossover(CardInstance.Info i1, CardInstance.Info i2, boolean force_com) {
		int attack = cross(i1.getAttack(), i2.getAttack());
		int health = cross(i1.getHealth(), i2.getHealth());
		int cost = force_com ? 0 : cross(i1.getCost(), i2.getCost());
		int level = cross(i1.getLevel(), i2.getLevel());

		SkillSpec[] skills = new SkillSpec[Math.max(i1.getSkills().length, i2.getSkills().length)];
		int gid = 0;
		for (SkillSpec s1 : i1.getSkills()) {
			for (SkillSpec s2 : i2.getSkills()) {
				if (s1.getId().equals(s2.getId())) {
					skills[gid] = cross(s1, s2);
					gid++;
				}
			}
		}

		while (gid < skills.length) {
			SkillSpec test = cross(i1.getSkills()[r.nextInt(i1.getSkills().length)],
					i2.getSkills()[r.nextInt(i2.getSkills().length)]);
			boolean con = false;
			for (int i = gid - 1; i >= 0; i--) {
				if (test.getId().equals(skills[i].getId())) {
					con = true;
				}
			}
			if (!con) {
				skills[gid] = test;
				gid++;
			}
		}
		CardInstance.Info tmp = new CardInstance.Info(attack, health, cost, level, skills);
		// System.out.println("Check:" + tmp + " = " + i1 + " + " + i2);
		if (!check(tmp)) {
			// System.out.println("check");
			return crossover(i1, i2, force_com);
		}
		return tmp;
	}

	private static SkillSpec cross(SkillSpec a, SkillSpec b) {
		if (a.getId().equals(b.getId())) {
			int x = cross(a.getX(), b.getX());
			int n = cross(a.getN(), b.getN());
			int c = cross(a.getC(), b.getC());
			String y = cross(a.getY(), b.getY());
			boolean all = cross(a.isAll(), b.isAll());
			String trigger = cross(a.getTrigger(), b.getTrigger());
			return new SkillSpec(a.getId(), x, y, n, c, a.getS(), a.getS2(), all, a.getCard_id(), trigger);
		} else {
			return new SkillSpec[] { a, b }[r.nextInt(2)];
		}
	}

	private static String cross(String a, String b) {
		return new String[] { a, b }[r.nextInt(2)];
	}

	private static boolean cross(boolean a, boolean b) {
		return new boolean[] { a, b }[r.nextInt(2)];
	}

	private static int cross(int a, int b) {
		return new int[] { a, b, (a + b) / 2, (a + b + 1) / 2 }[r.nextInt(4)];
	}

	public static CardInstance.Info mutate(CardInstance.Info i) {
		return mutate(i, false);
	}

	public static CardInstance.Info mutate(CardInstance.Info i, boolean force_com) {
		int attack = (int) (i.getAttack() + r.nextInt(2 * (1 + (int) (i.getAttack() * mutate_attack_percent)))
				- i.getAttack() * mutate_attack_percent);
		int health = (int) (i.getHealth() + r.nextInt(2 * (1 + (int) (i.getHealth() * mutate_health_percent)))
				- i.getHealth() * mutate_health_percent);
		int cost = force_com ? 0
				: (r.nextDouble() < mutate_cost_probability ? i.getCost() + r.nextInt(3) - 1 : i.getCost());
		int level = i.getLevel();
		if (cost < 0)
			cost = 0;
		if (cost >= 5)
			cost = 4;
		if (health <= 0)
			health = 1;
		if (attack < 0)
			attack = 0;
		SkillSpec[] skills = new SkillSpec[i.getSkills().length];
		for (int j = 0; j < skills.length; j++) {
			skills[j] = mutate(i.getSkills()[j]);
		}
		CardInstance.Info t = new CardInstance.Info(attack, health, cost, level, skills);
		// System.out.println("Check:" + t + " = " + i);
		if (!check(t))
			return mutate(i, force_com);
		return t;
	}

	public static SkillSpec mutate(SkillSpec s) {
		int x = varby(s.getX(), mutate_x_percent);
		int n = varby1(s.getN(), mutate_n_probability);
		int c = varby1(s.getC(), mutate_c_probability);
		boolean all = varbool(s.isAll(), mutate_all_probability);
		String y = varfaction(s.getY(), mutate_y_probability);
		String trigger = vartrigger(s.getTrigger(), mutate_trigger_probability);
		int card_id = 0;
		if (s.getCard_id() > 0) {
			Card cc = null;
			while (cc == null)
				cc = GlobalData.distinct_cards[r.nextInt(GlobalData.distinct_cards.length)];
			card_id = cc.getHighestID();
		}

		return new SkillSpec(s.getId(), x, y, n, c, s.getS(), s.getS2(), all, card_id, trigger);
	}

	private static int varby(int value, double mutate_percent) {
		if (value == 0)
			return 0;
		return (int) (value + r.nextInt(2 * (1 + (int) (value * mutate_percent))) - value * mutate_percent);
	}

	private static int varby1(int value, double mutate_probability) {
		if (r.nextDouble() > mutate_probability)
			return value;
		if (value == 0)
			return 0;
		int posi = value - 1 + r.nextInt(3);
		if (value == 0)
			posi = 1;// don't make values Null
		return posi;
	}

	private static boolean varbool(boolean value, double mutate_probability) {
		if (r.nextDouble() > mutate_probability)
			return value;
		return !value;
	}

	private static String varfaction(String value, double mutate_probability) {
		if (value.equals("allfactions"))
			return value;
		if (r.nextDouble() > mutate_probability)
			return value;
		return GlobalData.factionToString(r.nextInt(7));
	}

	private static String vartrigger(String value, double mutate_probability) {
		if (r.nextDouble() > mutate_probability)
			return value;
		return new String[] { "activate", "play", "death", "attacked" }[r.nextInt(4)];
	}
}
