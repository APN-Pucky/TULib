package de.neuwirthinformatik.Alexander.TU.Basic;

import java.util.ArrayList;

import de.neuwirthinformatik.Alexander.TU.TU;
import de.neuwirthinformatik.Alexander.TU.util.StringUtil;
import lombok.Data;

// TODO why no lombok Data?
public class Card {
	public static final Card NULL = new Card(new int[] { 0 }, "NULL", 0, 0, new int[] {}, 0, 0, 0,
			new CardInstance.Info[] { new CardInstance.Info(0, 0, 0, 0, new SkillSpec[] {}) }, "", 0);
	public final int[] ids;
	public final CardInstance.Info[] infos;
	public final int fusion_level;
	public final String name;
	public final int rarity;
	public final String picture;
	public final int asset_bundle;
	public final int[] materials;
	public final int faction;
	public final CardType type;
	public final CardCategory category;
	public final int fort_type;
	public final int set;
	// further stats; skilz...

	public Card(int[] ids, String name, int rarity, int lvl, int[] mats, int fort, int set, int f,
			CardInstance.Info[] infos, String pic, int bundle) {
		this.ids = ids;
		this.name = name;
		this.rarity = rarity;
		this.fusion_level = lvl;
		this.materials = mats;
		this.type = CardType.getByID(ids[0]);
		this.fort_type = fort;
		this.set = set;
		this.category = CardCategory.getByID(ids[0], fort, set);
		this.infos = infos;
		this.faction = f;
		this.picture = pic;
		this.asset_bundle = bundle;
	}

	public int getFortType() {
		return this.fort_type;
	}

	public int getSet() {
		return this.set;
	}

	public int[] getMaterials() {
		return materials;
	}

	public int[] getIDs() {
		return ids;
	}

	public String getName() {
		return name;
	}

	public String getPicture() {
		return picture;
	}

	public int getAssetBundle() {
		return asset_bundle;
	}

	public int getFusionLevel() {
		return fusion_level;
	}

	public int getRarity() {
		return rarity;
	}

	public int getLowestID() {
		return ids[0];
	}

	public int getHighestID() {
		return ids[ids.length - 1];
	}

	public int getPositionID(int id) {
		for (int i = 0; i < ids.length; i++)
			if (ids[i] == id)
				return i;
		return -1;
	}

	public CardType getCardType() {
		return type;
	}

	public int getFaction() {
		return (faction);
	}

	public CardInstance.Info[] getInfos() {
		return infos;
	}

	public boolean equals(Card c) {
		return c != null && ids[0] == c.getIDs()[0];
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getHighestID();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (getHighestID() != other.getHighestID())
			return false;
		return true;
	}

	public String toString() {
		String ret = "";
		for (int i = 0; i < ids.length; i++) {
			ret += ids[i] + ", ";
		}
		;
		return name + "[" + ret + "]" + "{" + rarity + "}";
	}

	public String description() {
		return new CardInstance(ids[ids.length - 1], this, infos[infos.length - 1]).description(); // Max Card
	}

	public String getUnitType() {
		int id = getHighestID();
		if ((1000 <= id && id < 2000) || (25000 <= id && id < 30000)) {
			return "Commander";
		} else if ((2000 <= id && id < 3000) || (8000 <= id && id < 10000) || (17000 <= id && id < 25000)) {
			return "Structure";
		} else if ((50000 <= id && id < 55000)) {
			return "Dominion";
		} else {
			return "Assault";
		}
	}

	public enum Faction {
		imperial(1), raider(2), bloodthirsty(3), xeno(4), righteous(5), progenitor(6), allfaction(0);

		private int p;

		private Faction(int n) {
			p = n;
		}

		public static Faction get(String s) {
			for (Faction m : values())
				if (m.toString().equalsIgnoreCase(s))
					return m;
			return null;
		}

		public static Faction get(int s) {
			for (Faction m : values())
				if (m.p == s)
					return m;
			return null;
		}

		// public String toString(){return ""+p;}
		public int toInt() {
			return p;
		}
	}

	public enum Rarity {
		common(1), rare(2), epic(3), legendary(4), vindicator(5), mythic(6);

		private int p;

		private Rarity(int n) {
			p = n;
		}

		public static Rarity get(String s) {
			for (Rarity m : values())
				if (m.toString().equalsIgnoreCase(s))
					return m;
			return null;
		}

		public static Rarity get(int s) {
			for (Rarity m : values())
				if (m.p == s)
					return m;
			return null;
		}

		// public String toString(){return ""+p;}
		public int toInt() {
			return p;
		}
	}

	public enum Level {
		single(0), dual(1), quad(2);

		private int p;

		private Level(int n) {
			p = n;
		}

		public static Level get(String s) {
			for (Level m : values())
				if (m.toString().equalsIgnoreCase(s))
					return m;
			return null;
		}

		public static Level get(int s) {
			for (Level m : values())
				if (m.p == s)
					return m;
			return null;
		}

		// public String toString(){return ""+p;}
		public int toInt() {
			return p;
		}
	}

	public static enum CardType {
		ASSAULT, COMMANDER, STRUCTURE, DOMINION;// , FORTRESS_DEFENSE, FORTRESS_SIEGE,
		// DOMINION, DOMINION_MATERIAL;

		public static CardType getByID(int id) {
			if (id < 1000)
				return ASSAULT;
			else if (id < 2000)
				return COMMANDER;
			else if (id < 3000)
				return STRUCTURE;
			else if (id < 8000)
				return ASSAULT;
			else if (id < 10000)
				return STRUCTURE;
			else if (id < 17000)
				return ASSAULT;
			else if (id < 25000)
				return STRUCTURE;
			else if (id < 30000)
				return COMMANDER;
			else if (id < 50001)
				return ASSAULT;
			else if (id < 55001)
				return DOMINION;
			else
				return ASSAULT;
		}
	}

	public static enum CardCategory {
		NORMAL, FORTRESS_DEFENSE, FORTRESS_SIEGE, FORTRESS_CONQUEST, DOMINION, DOMINION_MATERIAL;

		public static CardCategory getByID(int id, int fort, int set) {
			if ((id >= 2700) && (id < 2997)) {
				switch (fort) {
				case 1:
					return FORTRESS_DEFENSE;
				case 2:
					return FORTRESS_SIEGE;
				default:
					if (/* TUM.settings.ASSERT && */ (id < 2748) || (id >= 2754))
						// System.out.println("unsupported Fortress Id: " + id + " fort_type: " + fort);
						throw new NullPointerException("unsupported Fortress Id: " + id + " fort_type: " + fort);
					else
						return FORTRESS_SIEGE; // Sky Fortress
				}
			}
			if ((id == 43451) || (id == 43452)) {
				return DOMINION_MATERIAL;
			}
			if (id >= 50001 && id < 55001) {
				return DOMINION;
			}
			if (set == 8000) {
				if (/* TUM.settings.ASSERT && */ CardType.getByID(id) != CardType.STRUCTURE)
					throw new NullPointerException("Set 8000 is fortress, but " + id + " is not a structure");
				if (17359 >= id)
					return FORTRESS_CONQUEST; // END of CQ Cards look section 8
			}
			return NORMAL;
		}
	}

	public static class CardInstance {
		@Data
		public static class Info {
			final int attack, health, cost, level;
			final SkillSpec[] skills;

			/*
			 * public Info(int attack, int health,int cost,int level, SkillSpec[] skills) {
			 * this.attack = attack; this.health = health; this.level = level; this.cost =
			 * cost; this.skills = skills; }
			 */
			public String description() {
				String ret = "";
				ret += attack + "/" + health + "/" + cost + "\n";
				for (SkillSpec s : skills) {
					ret += s + "\n";
				}
				ret += "\n";
				return StringUtil.removeLastCharacter(ret, 2);
			}
		}

		public static final CardInstance NULL = new CardInstance(0, Card.NULL, null);
		private final int id;
		private final Info info;
		private final Card c;

		private CardInstance(int id, Card card, Info info) {
			this.id = id;
			c = card;
			this.info = info;
			if (TU.settings.ASSERT) {
				if (GlobalData.getCount(card.getIDs(), id) == 0)
					throw new IllegalArgumentException("CardInstance id not in Card");
				if (info.level != getLevel())
					throw new IllegalArgumentException("Different Levels");
			}
		}

		private CardInstance(int id, Card card) {
			this(id, card, card.getInfos()[card.getPositionID(id)]);
		}

		private CardInstance(int id) {
			this(id, GlobalData.getCardByID(id));
		}

		public static CardInstance get(int id) {
			if (id == 0) {
				return NULL;
			} else {
				return get(id, GlobalData.getCardByID(id));
			}
		}

		public static CardInstance get(int id, Card c) {
			if (id == 0) {
				return NULL;
			} else {
				Info[] infs = c.getInfos();
				int pos = c.getPositionID(id);
				if (pos >= infs.length || pos < 0) {
					TU.log.e("Error Creating Card " + c.getName(), "CardInstance");
					return NULL;
				}
				return get(id, c, infs[pos]);
			}
		}

		public static CardInstance get(int id, Card c, Info i) {
			if (id == 0 || c.equals(Card.NULL)) {
				return NULL;
			} else {
				return new CardInstance(id, c, i);
			}
		}

		public Info getInfo() {
			return info;
		}

		public int getCost() {
			return info.cost;
		}

		public int getHealth() {
			return info.health;
		}

		public int getAttack() {
			return info.attack;
		}

		public SkillSpec[] getSkills() {
			return info.skills;
		}

		public int getID() {
			return id;
		}

		public Card getCard() {
			return c;
		}

		public int[] getIDs() {
			return c.getIDs();
		}

		public String getName() {
			return c.getName() + "-" + getLevel();
		}

		public String toString() {
			return getName();
		}

		public int getFaction() {
			return c.getFaction();
		}

		public CardType getCardType() {
			return c.getCardType();
		}

		public String description() {
			String ret = getName() + "\n";
			ret += StringUtil.capitalizeOnlyFirstLetters(GlobalData.factionToString(getFaction())) + " ";
			ret += StringUtil.capitalizeOnlyFirstLetters(GlobalData.rarityToString(getRarity())) + " ";
			ret += StringUtil.capitalizeOnlyFirstLetters(getCardType().toString()) + " ";
			ret += StringUtil.capitalizeOnlyFirstLetters(GlobalData.fusionToString(getFusionLevel())) + "\n";
			ret += info.attack + "/" + info.health + "/" + info.cost + "\n";
			for (SkillSpec s : info.skills) {
				ret += s + "\n";
			}
			ret += "\n";
			for (SkillSpec s : info.skills) {
				if (s.card_id > 0)
					ret += get(s.card_id).description() + "\n\n";
			}
			return StringUtil.removeLastCharacter(ret, 2);
		}

		public String getUnitType() {
			return c.getUnitType();
		}

		public int getFusionLevel() {
			return c.getFusionLevel();
		}

		public int getRarity() {
			return c.getRarity();
		}

		public int getLowestID() {
			return c.getLowestID();
		}

		public CardInstance getLowest() {
			return get(c.getLowestID(), c);
		}

		public int getHighestID() {
			return c.getHighestID();
		}

		public CardInstance getHighest() {
			return get(c.getHighestID(), c);
		}

		public int getLevel() {
			// return info.level;
			return c.getPositionID(id) + 1;
		}

		public CardInstance[] getMaterials() {
			return GlobalData.getCardInstancesFromIDs(c.getMaterials());
		}

		public ArrayList<CardInstance> getLowestMaterials() {
			ArrayList<CardInstance> ac = new ArrayList<CardInstance>();
			if (c.materials.length == 0) {
				ac.add(getLowest());
			} else {
				for (CardInstance ci : getMaterials()) {
					ac.addAll(ci.getLowestMaterials());
				}
			}
			return ac;
		}

		public int getCostFromLowestMaterials() {
			int cost = GlobalData.getSPNeededToLevelTo(getLowest(), this);
			if (c.materials.length == 0) {
				return cost;
			}
			for (CardInstance ci : getMaterials()) {
				cost += ci.getCostFromLowestMaterials();
			}
			return cost;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CardInstance other = (CardInstance) obj;
			if (id != other.id)
				return false;
			return true;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + id;
			return result;
		}

		public CardInstance clone() {
			return get(id, c);
		}

	}
}
