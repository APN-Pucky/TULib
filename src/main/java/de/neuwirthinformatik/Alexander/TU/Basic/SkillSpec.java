package de.neuwirthinformatik.Alexander.TU.Basic;

import de.neuwirthinformatik.Alexander.TU.util.StringUtil;
import lombok.Data;

@Data
public class SkillSpec {
	final String id;
	final int x;
	final String y;
	final int n;
	final int c;
	final String s;
	final String s2;
	final boolean all;
	final int card_id;
	final String trigger;

	public SkillSpec(String id, int x, String y, int n, int c, String s, String s2, boolean all,int card_id, String trigger) {
		this.x = x;
		this.y = y;
		this.c = c;
		this.n = n;
		this.all = all;
		this.id = id;
		this.s = s;
		this.s2 = s2;
		this.card_id = card_id;
		this.trigger = trigger;
	}
	
	public String toString()
	{
		//return text();
		/*return (!trigger.equals("activate")?"On "+StringUtil.capitalizeOnlyFirstLetters(trigger.toString())+ ": ":"")
				+ StringUtil.capitalizeOnlyFirstLetters(id.toString())
				+ (all?" All":"") + " "
				+ (n>0?n+ " ":"")
				+ (y.equals("allfactions")?"":StringUtil.capitalizeOnlyFirstLetters(y.toString())+ " ")
				+ (x>0?x+ " ":"")
				+ (c>0?"every "+c+ " ":"")
				+ ((!s.equals("no_skill")&&!s2.equals("no_skill"))?StringUtil.capitalizeOnlyFirstLetters(s.toString())+ " to " + StringUtil.capitalizeOnlyFirstLetters(s2.toString()):"")
				+ ((card_id>0)?(" " + Data.getNameAndLevelByID(card_id)):"")
				;*/
		String result = StringUtil.capitalizeOnlyFirstLetters(id.toString());
		if(!trigger.equals("activate")) result = "On " + StringUtil.capitalizeOnlyFirstLetters(trigger.toString()) + ": " + result;
		if(all)result += " All";
		if(n>0)result += " " + this.n;
		if(!y.equals("allfactions"))result += " " + StringUtil.capitalizeOnlyFirstLetters(y.toString());
		else if(n>0 && x>0)result += " Assault";
		if(!s.equals("no_skill"))result += " " + StringUtil.capitalizeOnlyFirstLetters(s.toString());
		if(!s2.equals("no_skill"))result += " to " + StringUtil.capitalizeOnlyFirstLetters(s2.toString());
		if(x>0)result += " " + this.x;
		if(card_id>0)
		{
			result += " " + GlobalData.getNameAndLevelByID(card_id);
		}
		if(c>0)result += " every " + this.c;
		return result;
	}
	
	public String text()
	{
		String result = StringUtil.capitalizeOnlyFirstLetters(id.toString());
		if(!trigger.equals("activate")) result = "On " + StringUtil.capitalizeOnlyFirstLetters(trigger.toString()) + ": " + result;
		if(all)result += " All";
		if(n>0)result += " " + this.n;
		if(!y.equals("allfactions"))result += " " + StringUtil.capitalizeOnlyFirstLetters(y.toString());
		else if(n>0 && x>0)result += " Assault";
		if(!s.equals("no_skill"))result += " " + StringUtil.capitalizeOnlyFirstLetters(s.toString());
		if(!s2.equals("no_skill"))result += " to " + StringUtil.capitalizeOnlyFirstLetters(s2.toString());
		if(x>0)result += " " + this.x;
		if(card_id>0)
		{
			result += " " + GlobalData.getNameByID(card_id);
		}
		if(c>0)result += " every " + this.c;
		return result;
	}

	
/*
	public static enum Name {
		// Placeholder for no-skill:
		no_skill,

		// Activation (harmful):
		enfeeble, jam, mortar, siege, strike, sunder, weaken,

		// Activation (helpful):
		enhance, evolve, heal, mend, overload, protect, rally, fortify, enrage, entrap, rush,

		// Activation (unclassified/polymorphic):
		mimic,

		// Defensive:
		armor, avenge, scavenge, corrosive, counter, evade, subdue, absorb, flying, payback, revenge, tribute, refresh, wall, barrier,

		// Combat-Modifier:
		coalition, legion, pierce, rupture, swipe, drain, venom, hunt,

		// Damage-Dependent:
		berserk, inhibit, sabotage, leech, poison,

		// Triggered:
		allegiance, flurry, valor, stasis, summon, bravery;
		
		public static 
	};

	public static enum Trigger {
		activate, play, attacked, death,
	};*/
}
