package de.neuwirthinformatik.Alexander.TU.TUO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import de.neuwirthinformatik.Alexander.TU.Basic.Card.CardInstance;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.CardType;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.Faction;
import de.neuwirthinformatik.Alexander.TU.Basic.Card.Rarity;
import de.neuwirthinformatik.Alexander.TU.Basic.Gen;
import de.neuwirthinformatik.Alexander.TU.Basic.GlobalData;

public class FindCard {

	public static String name = "DR_F3LL";
	public static String stati = "";

	public static void main(String[] args) throws FileNotFoundException, IOException {
		GlobalData.init(false);

		boolean stop = false;
		CardInstance a = null, c = null, d = null;
		while (!stop) {
			stati = UUID.randomUUID().toString().replaceAll("-", "");
			a = Gen.genCardInstance(name + stati, (name + stati).hashCode(), CardType.ASSAULT);
			c = Gen.genCardInstance(name + stati + "_COM", (name + stati).hashCode(), true);
			d = Gen.genCardInstance(name + stati + "_DOM", (name + stati).hashCode(), CardType.DOMINION);
			if (
					/*Faction.get(a.getFaction()) == Faction.progenitor && Faction.get(c.getFaction()) == Faction.progenitor
					&& Faction.get(d.getFaction()) == Faction.progenitor
					&& */
					Rarity.get(a.getRarity()) == Rarity.mythic
					&& Rarity.get(c.getRarity()) == Rarity.mythic
					&& Rarity.get(d.getRarity()) == Rarity.mythic
					)
				stop = true;
		}
		System.out.println(stati);
		System.out.println(a.getInfo());
		System.out.println(c.getInfo());
		System.out.println(d.getInfo());

	}

}
