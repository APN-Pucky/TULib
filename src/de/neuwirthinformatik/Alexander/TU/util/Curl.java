package de.neuwirthinformatik.Alexander.TU.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import de.neuwirthinformatik.Alexander.TU.Basic.Deck;

public class Curl 
{
	String cookie;
	
	
	public Curl(String cookie)
	{
		this.cookie = cookie;
	}
	
	public void init() throws Exception
	{
		curl_pull("init","");
	}
	
	public void getUserAccount() throws Exception
	{
		curl_pull("getUserAccount", "");
	}
	public Object[][] getFactionInvites() throws Exception
	{
		String json = curl_pull("updateFaction","");

		JSONObject obj = new JSONObject(json);
		JSONObject m_obj = obj.getJSONObject("faction_invites");
		
		Object[][] ret = new Object[m_obj.keySet().size()][6];
		int i =0;
		for(Iterator<String> iterator = m_obj.keySet().iterator(); iterator.hasNext();i++)
		{
			String cur = iterator.next();
			//System.out.println(cur);
			JSONObject target = m_obj.getJSONObject(cur);
			//System.out.println(target.toString());
			//System.out.println(target.getString("name") + "-" + target.getString("guild_name") + ": " + target.getInt("user_id"));
			ret[i][0]=target.getString("name");
			ret[i][1]=target.getString("leader_name");
			ret[i][2]=target.getString("inviter_name");
			ret[i][3]=target.getInt("faction_id");
			ret[i][4]=target.getInt("num_members");
			ret[i][5]=target.getInt("total_rating");
		}
		
		return ret;
	}
	
	public void leaveFaction() throws Exception
	{
		curl_pull("leaveFaction","");
	}
	
	public void kickFactionMember(int member_id) throws Exception
	{
		curl_pull("kickFactionMember","&target_user_id=" + member_id);
	}
	
	public void acceptFactionInvite(int faction_id) throws Exception
	{
		curl_pull("acceptFactionInvite","&faction_id="+ faction_id);
	}
	
	public String sendFactionMessage(String msg) throws Exception
	{
		return curl_pull("sendFactionMessage","&chat="+ msg);
	}
	
	public void sendFactionInvite(int user_id) throws Exception
	{
		curl_pull("sendFactionInvite","&target_user_id="+ user_id);
	}
	
	
	
	public boolean isGameActive() throws Exception
	{
		String resp = curl_pull("getBattleResults", "");
		JSONObject obj = new JSONObject(resp);
		JSONObject b_data = obj.getJSONObject("battle_data");
		
		if(b_data.has("winner"))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public int[][] getMissionData() throws Exception
	{
		String json = curl_pull("init","");

		JSONObject obj = new JSONObject(json);
		JSONObject m_obj = obj.getJSONObject("mission_completions");
		
		int[][] ret = new int[m_obj.keySet().size()][5];
		int i =0;
		for(Iterator<String> iterator = m_obj.keySet().iterator(); iterator.hasNext();i++)
		{
			String cur = iterator.next();
			//System.out.println(cur);
			JSONObject target = m_obj.getJSONObject(cur);
			//System.out.println(target.toString());
			//System.out.println(target.getString("name") + "-" + target.getString("guild_name") + ": " + target.getInt("user_id"));
			ret[i][0]=Integer.parseInt(cur);
			ret[i][1]=target.getInt("number");
			ret[i][2]=target.getInt("complete");
			ret[i][3]=target.getInt("star2");
			ret[i][4]=target.getInt("star3");
		}
		
		return ret;
		
	}
	
	public Object[][] getRaidLeaderboard(int user_id, int raid_id) throws Exception
	{
		String json = curl_pull("getRaidLeaderboard&user_id="+user_id,"&raid_id=" + raid_id );

		JSONObject obj = new JSONObject(json);
		JSONArray m_obj = obj.getJSONArray("raid_leaderboard");
		
		Object[][] ret = new Object[m_obj.length()][5];
		int i =0;
		for(Iterator<Object> iterator = m_obj.iterator(); iterator.hasNext();i++)
		{
			JSONObject target = (JSONObject) iterator.next();
			//System.out.println(target.toString());
			//System.out.println(target.getString("name") + "-" + target.getString("guild_name") + ": " + target.getInt("user_id"));
			
			ret[i][0]=target.getString("name");
			ret[i][1]=target.getInt("rank");
			ret[i][2]=target.getInt("stat");
			ret[i][3]=target.getInt("raid_level");
			ret[i][4]=target.getInt("raid_level_rank");
		}
		
		return ret;
	}
	
	public Object[][] getRaidMembersLeaderBoard() throws Exception
	{
		String json = curl_pull("getRaidInfo",""); //opt user id
		JSONObject obj = new JSONObject(json);
		
		JSONObject d_obj = obj.optJSONObject("raid_info");
		if(d_obj != null)
		{
			String key = d_obj.keySet().iterator().next();
			JSONObject raid_info = d_obj.getJSONObject(key);
			
			if(raid_info.has("members"))
			{
				JSONObject memb = raid_info.getJSONObject("members");
				Object[][] ret = new Object[memb.keySet().size()][2];
				int i = 0;
				for(Iterator<String> iterator = memb.keySet().iterator(); iterator.hasNext();i++)
				{
					String cur = iterator.next();
					JSONObject m = memb.getJSONObject(cur);
					ret[i][0] = m.getString("member_name");
					ret[i][1] = m.getInt("damage");
				}

				return ret;
			}
			
		}
		return null;
	}
	
	public int[] getRaidData(int user_id) throws Exception
	{
		String json = curl_pull("getRaidInfo","");//opt user id
		JSONObject obj = new JSONObject(json);
		
		JSONObject d_obj = obj.optJSONObject("raid_info");
		int[] ret = new int[8];
		if(d_obj != null)
		{
			String key = d_obj.keySet().iterator().next();
			JSONObject raid_info = d_obj.getJSONObject(key);
			ret[0] = raid_info.getJSONObject("energy").getInt("battle_energy");
			ret[2] = raid_info.getInt("raid_id");
			ret[3] = raid_info.getInt("raid_level");
			ret[1] = 0;
			if(raid_info.has("members"))
			{
				if(raid_info.getJSONObject("members").has(""+user_id))
				{
					JSONObject m = raid_info.getJSONObject("members").getJSONObject(""+user_id);
					ret[1] = m.getInt("damage");
				}
			}
			ret[4] = raid_info.getInt("health");
			ret[5] = raid_info.getInt("max_health");
			if(raid_info.has("raid_level_end"))ret[6] = (int)(raid_info.getLong("raid_level_end")-obj.getLong("time"))/60; // -> mins
			ret[7] = raid_info.optInt("claimed_rewards",0);
		}
		return ret;
	}
	
	
	public Object[][] getConquestMembersLeaderboard(int user_id) throws Exception
	{
		String json = curl_pull("getGuildInfluenceLeaderboard&user_id="+user_id,"");

		JSONObject obj = new JSONObject(json);
		JSONArray m_obj = obj.getJSONObject("conquest_influence_leaderboard").getJSONArray("data");
		
		Object[][] ret = new Object[m_obj.length()-1][3];
		int i =0;
		for(Iterator<Object> iterator = m_obj.iterator(); iterator.hasNext();i++)
		{
			if(i==0)iterator.next();
			JSONObject target = (JSONObject) iterator.next();
			//System.out.println(target.toString());
			//System.out.println(target.getString("name") + "-" + target.getString("guild_name") + ": " + target.getInt("user_id"));
			
			ret[i][0]=target.getString("name");
			ret[i][1]=target.optInt("rank",-1);
			ret[i][2]=target.getInt("stat");
		}
		
		return ret;
	}
	
	public Object[][] getConquestMembersLeaderboardZone(int user_id, int zone_id) throws Exception
	{
		String json = curl_pull("getGuildZoneInfluenceLeaderboard&user_id="+user_id,"&zone_id=" + zone_id );

		JSONObject obj = new JSONObject(json);
		JSONArray m_obj = obj.getJSONObject("conquest_zone_influence_leaderboard").getJSONArray("data");
		
		Object[][] ret = new Object[m_obj.length()-1][3];
		int i =0;
		for(Iterator<Object> iterator = m_obj.iterator(); iterator.hasNext();i++)
		{
			if(i==0)iterator.next();
			JSONObject target = (JSONObject) iterator.next();
			//System.out.println(target.toString());
			//System.out.println(target.getString("name") + "-" + target.getString("guild_name") + ": " + target.getInt("user_id"));
			
			ret[i][0]=target.getString("name");
			ret[i][1]=target.optInt("rank",-1);
			ret[i][2]=target.getInt("stat");
		}
		
		return ret;
	}
	
	
	public Object[][] getConquestLeaderboard(int user_id) throws Exception
	{
		String json = curl_pull("getConquestLeaderboard&user_id="+user_id,"");

		JSONObject obj = new JSONObject(json);
		JSONArray m_obj = obj.getJSONObject("conquest_leaderboard").getJSONArray("data");
		
		Object[][] ret = new Object[m_obj.length()-1][3];
		int i =0;
		for(Iterator<Object> iterator = m_obj.iterator(); iterator.hasNext();i++)
		{
			if(i==0)iterator.next();
			JSONObject target = (JSONObject) iterator.next();
			//System.out.println(target.toString());
			//System.out.println(target.getString("name") + "-" + target.getString("guild_name") + ": " + target.getInt("user_id"));
			
			ret[i][0]=target.getString("name");
			ret[i][1]=target.optInt("rank",-1);
			ret[i][2]=target.getInt("stat");
		}
		
		return ret;
	}
	
	public Object[][] getConquestLeaderboardZone(int user_id, int zone_id) throws Exception
	{
		String json = curl_pull("getConquestZoneRankings&user_id="+user_id,"&zone_id=" + zone_id );

		JSONObject obj = new JSONObject(json);
		JSONArray m_obj = obj.getJSONObject("conquest_zone_data").getJSONObject("rankings").getJSONArray("data");
		
		Object[][] ret = new Object[m_obj.length()-1][3];
		int i =0;
		for(Iterator<Object> iterator = m_obj.iterator(); iterator.hasNext();i++)
		{
			if(i==0)iterator.next();
			JSONObject target = (JSONObject) iterator.next();
			//System.out.println(target.toString());
			//System.out.println(target.getString("name") + "-" + target.getString("guild_name") + ": " + target.getInt("user_id"));
			
			ret[i][0]=target.getString("name");
			ret[i][1]=target.optInt("rank",-1);
			ret[i][2]=target.getInt("stat");
		}
		
		return ret;
	}
	
	public Object[][] getConquestTopLeaderboard(int user_id) throws Exception
	{
		String json = curl_pull("getConquestTopLeaderboard&user_id="+user_id,"");

		JSONObject obj = new JSONObject(json);
		JSONArray m_obj = obj.getJSONObject("conquest_top_leaderboard").getJSONArray("data");
		
		Object[][] ret = new Object[m_obj.length()-1][3];
		int i =0;
		for(Iterator<Object> iterator = m_obj.iterator(); iterator.hasNext();i++)
		{
			if(i==0)iterator.next();
			JSONObject target = (JSONObject) iterator.next();
			//System.out.println(target.toString());
			//System.out.println(target.getString("name") + "-" + target.getString("guild_name") + ": " + target.getInt("user_id"));
			
			ret[i][0]=target.getString("name");
			ret[i][1]=target.optInt("rank",-1);
			ret[i][2]=target.getInt("stat");
		}
		
		return ret;
	}
	
	public Object[][] getConquestTopLeaderboardZone(int user_id, int zone_id) throws Exception
	{
		String json = curl_pull("getConquestZoneTopLeaderboard&user_id="+user_id,"&zone_id=" + zone_id );

		JSONObject obj = new JSONObject(json);
		JSONArray m_obj = obj.getJSONObject("conquest_zone_top_leaderboard").getJSONArray("data");
		
		Object[][] ret = new Object[m_obj.length()-1][3];
		int i =0;
		for(Iterator<Object> iterator = m_obj.iterator(); iterator.hasNext();i++)
		{
			if(i==0)iterator.next();
			JSONObject target = (JSONObject) iterator.next();
			//System.out.println(target.toString());
			//System.out.println(target.getString("name") + "-" + target.getString("guild_name") + ": " + target.getInt("user_id"));
			
			ret[i][0]=target.getString("name");
			ret[i][1]=target.optInt("rank",-1);
			ret[i][2]=target.getInt("stat");
		}
		
		return ret;
	}
	
	public int[] getCQData() throws Exception
	{
		String json = curl_pull("getConquestUpdate","");//opt user id
		JSONObject obj = new JSONObject(json);
		
		JSONObject d_obj = obj.optJSONObject("conquest_data");
		int[] ret = new int[6];
		if(d_obj != null)
		{
			ret[0] = d_obj.getInt("id");
			ret[2] = d_obj.getInt("conquest_points");
			ret[3] = d_obj.getInt("conquest_rank");
			JSONObject d_user = d_obj.getJSONObject("user_conquest_data");
			ret[4] = d_user.getInt("influence");
			ret[1] = d_user.getJSONObject("energy").getInt("battle_energy");
			

			ret[5] = d_user.optInt("claimed_reward",0);
		}
		return ret;
	}
	

	public Object[][] getConquestLiveZone(int user_id, int zone_id) throws Exception
	{
		String json = curl_pull("getConquestUpdate","");//opt user id
		JSONObject obj = new JSONObject(json);
		
		JSONObject d_obj = obj.optJSONObject("conquest_data");
		JSONObject z_obj = d_obj.getJSONObject("zone_data");
		int j = 0;
			if(!z_obj.has(""+zone_id))return new Object[][]{};
			JSONObject zone = z_obj.getJSONObject( ""+zone_id);
			JSONArray data = zone.getJSONObject("rankings").getJSONObject("rankings").getJSONArray("data");
			Object[][] ret = new Object[data.length()-1][3];
			int my_guild = zone.getInt("my_guild_influence");
			int i =0;
			for(Iterator<Object> iterator = data.iterator(); iterator.hasNext();i++)
			{
				if(i==0)iterator.next();
				JSONObject target = (JSONObject) iterator.next();
				//System.out.println(target.toString());
				//System.out.println(target.getString("name") + "-" + target.getString("guild_name") + ": " + target.getInt("user_id"));
				
				ret[i][0]=target.getString("name");
				ret[i][1]=target.optInt("rank",-1);
				//ret[i][2]=target.getInt("stat");
				
				ret[i][2]=target.optInt("influence",my_guild);
			}
		return ret;
	}
	
	public int[] getBattleData() throws Exception
	{
		String json = curl_pull("getHuntingTargets","");
		
		JSONObject obj = new JSONObject(json);
		JSONObject d_obj = obj.getJSONObject("user_data");
		int stamina = d_obj.getInt("stamina");
		int max_stamina = obj.getInt("max_stamina");
		int br = d_obj.getInt("hunting_elo");
		int wins = d_obj.getInt("hunting_wins");
		int lp = d_obj.getInt("league_points");
		int level = d_obj.getInt("level");
		return new int[]{stamina,max_stamina,br,wins,level,lp};
	}
	
	public Object[][] getWarLeaderboard( int user_id) throws Exception
	{
		String json = curl_pull("getFactionWarLeaderboard&user_id="+user_id,"" );

		JSONObject obj = new JSONObject(json);
		JSONArray m_obj = obj.getJSONArray("faction_war_leaderboard");
		
		Object[][] ret = new Object[m_obj.length()][4];
		int i =0;
		for(Iterator<Object> iterator = m_obj.iterator(); iterator.hasNext();i++)
		{
			JSONObject target = (JSONObject) iterator.next();
			//System.out.println(target.toString());
			//System.out.println(target.getString("name") + "-" + target.getString("guild_name") + ": " + target.getInt("user_id"));
			
			ret[i][0]=target.getString("faction_name");
			ret[i][1]=target.getInt("rank");
			ret[i][2]=target.getInt("faction_war_points");
			ret[i][3]=target.getInt("mmr");
			//ret[i][4]=target.getInt("wins");
			//ret[i][5]=target.getInt("losses");
		}
		
		return ret;
	}
	
	
	private Object[][] getWarMembersLeaderboard(JSONArray data)
	{
		Object[][] ret = new Object[data.length()][8];
		int i =0;
		for(Iterator<Object> iterator = data.iterator(); iterator.hasNext();i++)
		{
			JSONObject member = (JSONObject) iterator.next();
			String name = member.getString("member_name");
			int energy = member.optInt("battle_energy",0);
			int wins = member.optInt("wins",0);
			int losses = member.optInt("losses",0);
			int d_wins = member.optInt("defense_wins",0);
			int d_losses = member.optInt("defense_losses",0);
			int faction_war_points = member.optInt("faction_war_points",0);
			int current_war_points = member.optInt("current_war_points",0);
			//int claimed_rewards = member.optInt("claimed_rewards",0);
			ret[i] = new Object[]{name,energy,wins,losses,d_wins,d_losses,faction_war_points,current_war_points};
		}
		return ret;
	}
	
	public Object[][] getWarEnemyLeaderboard() throws Exception
	{
		String json = curl_pull("updateFactionWar","");
		JSONObject obj = new JSONObject(json);
		JSONArray data = obj.getJSONObject("faction_war").getJSONArray("attacker_faction_members");
		
		return getWarMembersLeaderboard(data);
	}
	
		
	public Object[][] getWarOwnMembersLeaderboard() throws Exception
	{
		String json = curl_pull("updateFactionWar","");
		JSONObject obj = new JSONObject(json);
		JSONArray data = obj.getJSONObject("faction_war").getJSONArray("defender_faction_members");
		
		return getWarMembersLeaderboard(data);
	}
	
	public int[] getWarData() throws Exception
	{
		String json = curl_pull("updateFactionWar","");
		JSONObject obj = new JSONObject(json);
		JSONObject data = obj.getJSONObject("faction_war_event_info").getJSONObject("user_info");
		int energy = data.optInt("battle_energy",0);
		int wins = data.optInt("wins",0);
		int losses = data.optInt("losses",0);
		int d_wins = data.optInt("defense_wins",0);
		int d_losses = data.optInt("defense_losses",0);
		int faction_war_points = data.optInt("faction_war_points",0);
		int current_war_points = data.optInt("current_war_points",0);
		int claimed_rewards = data.optInt("claimed_rewards",0);
		
		
		return new int[]{energy,wins,losses,d_wins,d_losses, faction_war_points, current_war_points, claimed_rewards};
	}
	public int[] getBrawlData() throws Exception
	{
		String json = curl_pull("getHuntingTargets","");
		
		JSONObject obj = new JSONObject(json);
		JSONObject brawl = obj.getJSONObject("player_brawl_data");
		int stam =0;
		int rank = brawl.getInt("current_rank");
		int points = Integer.parseInt((brawl.optString("points","0")));
		int wins = Integer.parseInt((brawl.optString("wins","0")));
		int losses = Integer.parseInt((brawl.optString("losses","0")));
		int claimed_rewards = Integer.parseInt((brawl.optString("claimed_rewards","0")));
		
		if(brawl.has("energy"))
		{
			JSONObject energy = brawl.getJSONObject("energy");
			try{
				if(energy.has("battle_energy"))stam = Integer.parseInt(energy.getString("battle_energy"));
			}catch(Exception e1) {
				try{
					stam = energy.getInt("battle_energy");
				}
				catch(Exception e2){stam=0;}
			}
		}
		
		
		return new int[]{stam,rank,points,wins,losses,claimed_rewards};
	}
	
	public Object[][] getBrawlLeaderboard(int user_id) throws Exception
	{
		String json = curl_pull("getBrawlLeaderboard&user_id="+user_id,"");

		JSONObject obj = new JSONObject(json);
		JSONArray m_obj = obj.getJSONArray("brawl_leaderboard");
		
		Object[][] ret = new Object[m_obj.length()][3];
		int i =0;
		for(Iterator<Object> iterator = m_obj.iterator(); iterator.hasNext();i++)
		{
			JSONObject target = (JSONObject) iterator.next();
			//System.out.println(target.toString());
			//System.out.println(target.getString("name") + "-" + target.getString("guild_name") + ": " + target.getInt("user_id"));
			
			ret[i][0]=target.getString("name");
			ret[i][1]=target.getInt("rank");
			ret[i][2]=target.getInt("points");
		}
		
		return ret;
	}
	
	public Object[][] getBrawlMembersLeaderBoard(int user_id) throws Exception
	{
		String json = curl_pull("getBrawlMemberLeaderboard&user_id="+user_id,"");

		JSONObject obj = new JSONObject(json);
		JSONArray m_obj = obj.getJSONArray("brawl_leaderboard");
		
		Object[][] ret = new Object[m_obj.length()][3];
		int i =0;
		for(Iterator<Object> iterator = m_obj.iterator(); iterator.hasNext();i++)
		{
			JSONObject target = (JSONObject) iterator.next();
			//System.out.println(target.toString());
			//System.out.println(target.getString("name") + "-" + target.getString("guild_name") + ": " + target.getInt("user_id"));
			
			ret[i][0]=target.getString("name");
			ret[i][1]=target.getInt("rank");
			ret[i][2]=target.getInt("points");
		}
		
		return ret;
	}
	
	public void claimRaidReward(int raid_id) throws Exception
	{
		curl_pull("claimRaidReward","&raid_id=" + raid_id );
	}
	
	public void claimConquestReward() throws Exception
	{
		curl_pull("claimConquestReward","");
	}
	
	public void claimBrawlRewards() throws Exception
	{
		curl_pull ("claimBrawlRewards","");
	}
	
	public void fightConquestBattle(int zone_id) throws Exception
	{
		curl_pull ("fightConquestBattle","&zone_id="+zone_id);
	}
	
	public void fightRaidBattle(int raid_id, int raid_level) throws Exception
	{
		curl_pull("fightRaidBattle","&raid_level="+raid_level+"&raid_id="+raid_id);
	}
	
	public void fightWarBattle(int slot) throws Exception
	{
		curl_pull ( "startFactionWarBattle","&slot_id="+slot);
	}
	
	public void fightBrawlBattle() throws Exception
	{
		curl_pull ("fightBrawlBattle","");
	}
	
	public void fightFactionQuest(int quest) throws Exception
	{
		curl_pull ("fightFactionQuest","&quest_id="+quest );
	}
	
	public int[][] getGuildQuestData() throws Exception
	{
		String json = curl_pull("updateFaction","");
		
		JSONObject obj = new JSONObject(json);
		JSONObject achievements = obj.getJSONObject("user_achievements");
		
		
		if(achievements.has("-1") &&achievements.has("-2") && achievements.has("-3"))
		{
			JSONObject first = achievements.getJSONObject("-1");
			JSONObject second = achievements.getJSONObject("-2");
			JSONObject third = achievements.getJSONObject("-3");
			int[][] ret = new int[][]{{first.getInt("quest_id"),first.getInt("progress"),first.getInt("max_progress")},
					{second.getInt("quest_id"),second.getInt("progress"),second.getInt("max_progress")},
					{third.getInt("quest_id"),third.getInt("progress"),third.getInt("max_progress")}};
			return ret;
		}
		return null;
	}
	
	public void startMission(int mission_id) throws Exception
	{
		curl_pull("startMission","&mission_id=" +mission_id);
	}
	
	public void useDailyBonus() throws Exception
	{
		curl_pull("useDailyBonus", "");
	}
	
	public void startHuntingBattle(int target_id) throws Exception
	{
		curl_pull("startHuntingBattle", "&target_user_id="+target_id);
		//System.out.println(js);
	}
	
	public Object[][] getHuntingTargets() throws Exception
	{
		String json = curl_pull("getHuntingTargets","");
		
		JSONObject obj = new JSONObject(json);
		JSONObject targets = obj.getJSONObject("hunting_targets");
		//System.out.println(json);
		//System.out.println("Card Count: " +cards.keySet().size());
		Object[][] ret = new Object[targets.keySet().size()][3];
		int i =0;
		for(Iterator<String> iterator = targets.keySet().iterator(); iterator.hasNext();i++)
		{
			String cur =  iterator.next();
			//System.out.println(cur);
			JSONObject target = targets.getJSONObject(cur);
			//System.out.println(target.toString());
			//System.out.println(target.getString("name") + "-" + target.getString("guild_name") + ": " + target.getInt("user_id"));
			ret[i][0]=target.getInt("user_id");
			ret[i][1]=target.getString("name");
			if(target.has("guild_name") && !target.isNull("guild_name"))ret[i][2]=target.getString("guild_name"); else ret[i][2] ="";
		}
		return ret;
	}
	
	public void claimFactionWarRewards() throws Exception
	{
		curl_pull("claimFactionWarRewards","");
	}
	
	public String autoSkipFight() throws Exception
	{
		return curl_pull("playCard", "&autopilot=1&skip=1");
	}
	
	public String playCard(int card_uid) throws Exception
	{
		return curl_pull("playCard", "&card_uid="+ card_uid);
	}
	
	public void setUserFlag(String flag, int value) throws Exception
	{
		curl_pull("setUserFlag","&value="+value + "&flag="+flag);
	}
	
	public int[] getCampaignData() throws Exception {return getCampaignData(curl_pull("init",""));};
	public int[] getCampaignData(String json) {return getCampaignData(new JSONObject(json));}
	public int[] getCampaignData(JSONObject obj)
	{
		JSONObject camp = obj.optJSONObject("current_campaigns");
		if(camp == null || camp.keySet().size()==0)return new int[]{-1,-1,-1,-1,-1,-1,-1,-1,-1};
		
		camp = camp.getJSONObject(camp.keySet().iterator().next());
		int id = camp.optInt("id",-1);
		int cost = camp.optInt("energy",125);
		int[] amount = new int[3],collected = new int[3];
		JSONObject rewards = camp.getJSONObject("rewards");
		for(int i = 1;i  <=3;i++ ) //3 level hardcocded TODO flexible
		{
			JSONObject level = rewards.getJSONObject(""+ i);
			for(Iterator<String> reward_cat = level.keySet().iterator(); reward_cat.hasNext();)
			{
				JSONObject rew_cat = level.optJSONObject(reward_cat.next());
				if(rew_cat==null)continue;//TODO TEST
				for(Iterator<String> ids = rew_cat.keySet().iterator(); ids.hasNext();)
				{
					JSONObject reward_id =rew_cat.getJSONObject(ids.next());
					amount[i-1] += reward_id.getInt("amount"); 
					collected[i-1] += reward_id.getInt("collected");
				}
			}
		}

		JSONObject data = obj.getJSONObject("user_data");
		return new int[]{id,data.getInt("energy"), cost, collected[0], amount[0],collected[1], amount[1],collected[2], amount[2]};
	}
	
	public void startCampaign(int[] deck,int[] res, int id, int diff) throws Exception
	{
		String add = getCardsAndCommanderString(deck);
		String reserves = idsToJSON(res);
		add+= "&campaign_id="+id+"&difficulty=" + diff ;
		if(res.length != 0)add += "&reserves=" + reserves;
		String resp = curl_pull("startCampaign", add);
		//System.out.println(add);
		//System.out.println(resp);
	}
	
	public void fightCampaignBattle(int id) throws Exception
	{
		curl_pull("fightCampaignBattle","&campaign_id="+id);
	}
	
	public void fightPracticeBattle(int target_user_id, boolean is_surge, boolean fight_attack_deck) throws Exception
	{
		curl_pull("fightPracticeBattle","&target_user_id="+target_user_id+"&is_surge="+(is_surge?1:0)+"&fight_attack_deck="+(fight_attack_deck?1:0));
	}
	
	public int[] getGuildMemberDeck(int target_user_id, boolean defense) throws Exception
	{
		String json = curl_pull("getProfileData","&target_user_id="+target_user_id);
		JSONObject m = new JSONObject(json);
		JSONObject player_info = m.getJSONObject("player_info");
		JSONObject deck = player_info.getJSONObject(defense?"defense_deck":"deck");
		return getDeckJSONArray(deck);
	}
	public String getProfileData(int target_user_id) throws Exception
	{
		return curl_pull("getProfileData","&target_user_id="+target_user_id);
	}
	
	/*public void setDeckCards(int[] deck, int slot) throws Exception
	{
		setDecksCards(deck,slot);	
	}*/
	
	public void setDominion(int id, int slot) throws Exception
	{
		curl_pull("setDeckDominion","&dominion_id="+id+"&deck_id="+slot);
	}
	
	public void setDefenseDeck(int slot) throws Exception
	{
		curl_pull("setDefenseDeck","&deck_id="+slot);
	}
	
	public void setActiveDeck(int slot) throws Exception
	{
		curl_pull("setActiveDeck","&deck_id="+slot);
	}
	
	
	public void setDecksCards(Deck off_deck,Deck def_deck, int off_id, int def_id) throws Exception
	{
		//System.out.println("LOGLGO OFF: "+ off_deck.toString());
		int[] off = off_deck.getOffenseDeck();
		int[] off_wo_dom = off_deck.getDefenseDeck();
		int[] def = def_deck.getDefenseDeck();
		setDeckCardsActive(off,off_id); //In-Active Hack		
		setDeckCardsActive(def,def_id);
		off_wo_dom[1] = 0;
		setDeckCardsActive(off_wo_dom,off_id);
		setActiveDeck(off_id);
		setDefenseDeck(def_id);
	}
	
	private String idsToJSON(int[] deck)
	{
		String add = "{";
		boolean[] included =new boolean[deck.length];
		for(int i = 0; i < deck.length;i++)
		{
			int num = 0;
			for(int j = 0; j < deck.length;j++)
			{
				if(deck[i] !=0 && !included[j] && deck[j]==deck[i])
				{
					num++;
					included[j] = true;
				}
			}
			if(num>0)
			{
				/*add += "\"%\"22" +deck[i] + "\"%\"22" + "\"%\"3a" + "\"%\"22" + num + "\"%\"22";
				if(i!= deck.length-1)add += "\"%\"2c";*/
				add += "\"" +deck[i] + "\"" + ":" + "\"" + num + "\"";
				add += ",";
			}
					
		}
		add = add.substring(0, add.length()-1);
		add += "}";
		return add;
	}
	private String getCardsAndCommanderString(int[] deck)
	{
		int commander_id = deck[0];
		String add = "";
		add += "&cards={";//String add = "&dominion_id=" + dominion_id +"&deck_id=" + slot + "&cards=\"%\"7b";
		
		
		boolean[] included =new boolean[deck.length];
		for(int i = 2; i < deck.length;i++)
		{
			int num = 0;
			for(int j = 0; j < deck.length;j++)
			{
				if(deck[i] !=0 && !included[j] && deck[j]==deck[i])
				{
					num++;
					included[j] = true;
				}
			}
			if(num>0)
			{
				/*add += "\"%\"22" +deck[i] + "\"%\"22" + "\"%\"3a" + "\"%\"22" + num + "\"%\"22";
				if(i!= deck.length-1)add += "\"%\"2c";*/
				add += "\"" +deck[i] + "\"" + ":" + "\"" + num + "\"";
				add += ",";
			}
					
		}
		add = add.substring(0, add.length()-1);
		add += "}";//add += "\"%\"7d";
		add += "&commander_id=" + commander_id;
		return add;
	}
	
	public void setDeckCardsActive(int[] deck, int slot) throws Exception
	{
		//int commander_id = deck[0];
		int dominion_id = deck[1];
		String add = "";
		add += "&dominion_id=" + dominion_id +"&deck_id=" + slot + getCardsAndCommanderString(deck);
		add += "&activeYN=1";
		//System.out.println(add);
		curl_pull("setDeckCards",add);
		//System.out.println(resp);
	}
	
	public Object[] getGuild() throws Exception {return getGuild(curl_pull("updateFaction",""));};
	public Object[] getGuild(String json) {return getGuild(new JSONObject(json));}
	public Object[] getGuild(JSONObject obj)
	{
		//String json = curl_pull("updateFaction","");
		//JSONObject obj = new JSONObject(json);
			
		JSONObject data = obj.getJSONObject("user_data");
		
		int user_id = data.getInt("user_id");
		
		data = obj.getJSONObject("faction");
		int faction_id = data.getInt("faction_id");
		int leader_id = data.optInt("leader_id",0);
		JSONObject mem = data.optJSONObject("members");
		int guild_role = 1;
		
		int[] ids = null;
		String[] names=null;
		int i =0;
		if(mem != null)
		{
			guild_role = mem.getJSONObject(""+user_id).getInt("member_role");
			
			int size = mem.keySet().size();
			ids = new int[size];
			names = new String[size];
			for(Iterator<String> iterator = mem.keySet().iterator(); iterator.hasNext();)
			{
				String cur = iterator.next();
				JSONObject person = mem.getJSONObject(cur);
				ids[i]= person.getInt("user_id");
				names[i]= person.getString("name");
				i++;
			}
		}
		//todo Member object
		
		return new Object[]{data.optString("name",""),faction_id,leader_id, guild_role,ids,names};
	}
	public int[] getData() throws Exception {return getData(curl_pull("getHuntingTargets",""));};
	public int[] getData(String json) {return getData(new JSONObject(json));}
	public int[] getData(JSONObject obj)
	{
		JSONObject data = obj.getJSONObject("user_data");
		
		int money = data.getInt("money");
		int salvage = data.getInt("salvage");
		int tokens = data.getInt("tokens");
		int energy = data.getInt("energy");
		int stamina = data.getInt("stamina");
		int off_deck = data.getInt("active_deck");
		int def_deck = data.getInt("defense_deck");
		int num_cards = data.getInt("num_cards");
		int max_salvage = data.getJSONObject("caps").getInt("max_salvage");
		int max_cards = data.getJSONObject("caps").getInt("max_cards");
		int daily_bonus = obj.optBoolean("daily_bonus",false)?1:0;
		/*int daily_bonus = 0;
		if(obj.has("daily_bonus") && obj.getBoolean("daily_bonus"))daily_bonus = 1;*/
		return new int[]{money,salvage,energy,stamina,daily_bonus, off_deck,def_deck,num_cards,max_salvage,max_cards,tokens};
	}
	
	public void consumeItem(int item_id) throws Exception
	{
		curl_pull("consumeItem","&item_id="+item_id);
	}
	
	public void consumeAll() throws Exception
	{
		String json ="";
		while(!json.contains("You need"))json= curl_pull("consumeItem","&item_id="+2000);
		json ="";
		while(!json.contains("You need"))json= curl_pull("consumeItem","&item_id="+2001);
	}
	
	public void buy20Pack() throws Exception
	{
		curl_pull("buyStorePromoGold","&data_usage=0&expected_cost=2000&item_id=48&item_type=3");
	}
	
	public void buy() throws Exception
	{
		String json = curl_pull("getHuntingTargets","");
		
		JSONObject obj = new JSONObject(json);
		JSONObject data = obj.getJSONObject("user_data");
		int old_money = -1;
		int money = data.getInt("money");
		int salvage = data.getInt("salvage");
		//int num_cards = data.getInt("num_cards");
		int max_salvage = data.getJSONObject("caps").getInt("max_salvage");
		//int max_cards = data.getJSONObject("caps").getInt("max_cards");
		
		int count = 0;
		while(salvage < max_salvage-75 && money > 2000 && old_money != money)
		{
			count++;
			json = curl_pull("buyStorePromoGold","&data_usage=0&expected_cost=2000&item_id=48&item_type=3");
			
			if(salvage>max_salvage-150)
			{
				json = curl_pull("salvageL1CommonCards","");
				json = curl_pull("salvageL1RareCards","");
			}
			else if(salvage > max_salvage-300)
			{
				if(count % 2 ==0)
				{
					json = curl_pull("salvageL1CommonCards","");
					json = curl_pull("salvageL1RareCards","");
				}
			}
			else
			{
				if(count % 10 == 0)
				{
					json = curl_pull("salvageL1CommonCards","");
					json = curl_pull("salvageL1RareCards","");			
				}
			}

			old_money = money;
			obj = new JSONObject(json);
			data = obj.getJSONObject("user_data");
			money = data.getInt("money");
			salvage = data.getInt("salvage");
			//num_cards = data.getInt("num_cards");
		}
		System.out.println("While: " + ((salvage < max_salvage-75)?"t":"f") + " " + (money > 2000?"t":"f") + " " + (old_money != money?"t":"f"));
	}
	
	public void salvageCommonRare() throws Exception
	{
		curl_pull("salvageL1CommonCards","");
		curl_pull("salvageL1RareCards","");
	}
	
	public String salvageCard(int id) throws Exception
	{
		return curl_pull("salvageCard","&card_id="+id);
		//System.out.println(resp);
	}
	
	public void setCardLock(int id, boolean lock) throws Exception
	{
		curl_pull("setCardLock","&locked=" +(lock?1:0) + "&card_id=" + id);
	}
	
	public void upgradeCard(int id) throws Exception
	{
		curl_pull("upgradeCard","&in_deck=0&card_id="+id);
	}
	
	public void fuseCard(int id) throws Exception
	{
		curl_pull("fuseCard","&card_id="+id);
	}
	
	/*public void displayInv() throws Exception
	{
		String json = curl_pull("salvageL1CommonCards","");
		
		
		JSONObject obj = new JSONObject(json);
		JSONObject cards = obj.getJSONObject("user_cards");
		
		System.out.println("Card Count: " +cards.keySet().size());
		for(Iterator<String> iterator = cards.keySet().iterator(); iterator.hasNext();)
		{
			String cur =  iterator.next();
			JSONObject card = cards.getJSONObject(cur);
			System.out.println(cur + ": " + card.getInt("num_owned"));
		}
	}*/
	
	public int[] getInventory() throws Exception {return getInventory(curl_pull("salvageL1CommonCards",""));};
	public int[] getInventory(String json) {return getInventory(new JSONObject(json));}
	public int[] getInventory(JSONObject obj) //TODO more speed
	{
		JSONObject cards = obj.getJSONObject("user_cards");
		int count = 0;
		for(Iterator<String> iterator = cards.keySet().iterator(); iterator.hasNext();)
		{
			String cur = iterator.next();
			JSONObject card = cards.getJSONObject(cur);
			count += card.getInt("num_owned");
		}
		int[] ret = new int[count];
		int i = 0;
		for(Iterator<String> iterator = cards.keySet().iterator(); iterator.hasNext();)
		{
			String cur = iterator.next();
			JSONObject card = cards.getJSONObject(cur);
			for(int j =0;j< card.getInt("num_owned");j++)
		    {
				ret[i] = Integer.parseInt(cur);
				i++;
		    }
		}
		return ret;
	}
	
	public int[] getRestore() throws Exception {return getRestore(curl_pull("init",""));};
	public int[] getRestore(String json) {return getRestore(new JSONObject(json));}
	public int[] getRestore(JSONObject obj)
	{
		JSONObject cards = obj.optJSONObject("buyback_data");
		if(cards == null)return new int[]{};
		int count = 0;
		for(Iterator<String> iterator = cards.keySet().iterator(); iterator.hasNext();)
		{
			String cur = (String) iterator.next();
			JSONObject card = cards.getJSONObject(cur);
			count += card.getInt("number");
		}
		int[] restore = new int[count];
		int i = 0;
		for(Iterator<String> iterator = cards.keySet().iterator(); iterator.hasNext();)
		{
			String cur = iterator.next();
			JSONObject card = cards.getJSONObject(cur);
			for(int j =0;j< card.getInt("number");j++)
		    {
				restore[i] = Integer.parseInt(cur);
				i++;
		    }
		}		
		return restore;
	}
	
	public void restore(int id) throws Exception
	{
		curl_pull("buybackCard","&number=1&card_id=" + id);
	}
	
	public void displayDecks() throws Exception
	{
		String json = curl_pull("salvageL1CommonCards","");
		JSONObject obj = new JSONObject(json);
		JSONObject decks = obj.getJSONObject("user_decks");
		JSONObject deck = decks.getJSONObject("1");
		System.out.println("Commander: " + deck.getInt("commander_id"));
		System.out.println("Dominion: " + deck.getInt("dominion_id"));
		JSONObject cards = deck.getJSONObject("cards");
		
		for(Iterator<String> iterator = cards.keySet().iterator(); iterator.hasNext();) {
		    String key = (String) iterator.next();
		    System.out.println(key + " #" + cards.getInt(key));
		}
	}
	
	
	public int[] getDeck(int id) throws Exception {return getDeck(id,curl_pull("salvageL1CommonCards",""));}
	public int[] getDeck(int id, String json) {return getDeck(id,new JSONObject(json));}
	public int[] getDeck(int id, JSONObject obj)
	{
		//int cur=2;
		//int[] ret = new int[12];
		
		JSONObject decks = obj.getJSONObject("user_decks");
		JSONObject deck = decks.getJSONObject(""+id);
		return getDeckJSONObject(deck);
		/*ret[0] = deck.getInt("commander_id");
		ret[1] = deck.getInt("dominion_id");
		JSONObject cards = deck.getJSONObject("cards");
		for(Iterator<String> iterator = cards.keySet().iterator(); iterator.hasNext();) {
		    String key =  iterator.next();
		    for(int i =0;i< cards.getInt(key);i++)
		    {
		    	ret[cur] = Integer.parseInt(key);
		    	cur++;
		    }
		}
		return ret;*/
	}
	
	private int[] getDeckJSONObject(JSONObject deck)
	{
		int cur=2;
		int[] ret = new int[12];
		ret[0] = deck.getInt("commander_id");
		ret[1] = deck.getInt("dominion_id");
		JSONObject cards = deck.getJSONObject("cards");
		for(Iterator<String> iterator = cards.keySet().iterator(); iterator.hasNext();) {
		    String key =  iterator.next();
		    for(int i =0;i< cards.getInt(key);i++)
		    {
		    	ret[cur] = Integer.parseInt(key);
		    	cur++;
		    }
		}
		return ret;
	}
	
	private int[] getDeckJSONArray(JSONObject deck)
	{
		int cur=2;
		int[] ret = new int[12];
		ret[0] = deck.getInt("commander_id");
		ret[1] = deck.getInt("dominion_id");
		JSONArray cards = deck.getJSONArray("cards");
		for(Iterator<Object> iterator = cards.iterator(); iterator.hasNext();cur++)
		{
			String card = (String) iterator.next();
			ret[cur] = Integer.parseInt(card);
		}
		return ret;
	}
	
	
	public int[][] getRestoreAndInventory() throws Exception
	{
		JSONObject obj = new JSONObject(curl_pull("init",""));
		int[] restore = getRestore(obj);
		int[] inventory = getInventory(obj);
		return new int[][]{restore,inventory};
	}
	public int[][] getRestoreInventoryData() throws Exception
	{
		JSONObject obj = new JSONObject(curl_pull("init",""));
		int[] restore = getRestore(obj);
		int[] inventory = getInventory(obj);
		int[] data = getData(obj);
		return new int[][]{restore,inventory,data};
	}
	public Object[] getDataGuildRestoreInventoryOffDef() throws Exception
	{
		JSONObject obj = new JSONObject(curl_pull("init",""));
		int[] restore = getRestore(obj);
		int[] inventory = getInventory(obj);
		int[] data = getData(obj);
		int[] off_deck = getDeck(data[5],obj);
		int[] def_deck = getDeck(data[6],obj);
		Object[] guild = getGuild(obj);
		return new Object[]{data,guild,restore,inventory,off_deck,def_deck};
	}
	
	public String[] getVersionAndDate() throws Exception
	{
		JSONObject obj = new JSONObject(curl_pull("init",""));
		return new String[] {obj.getString("version"),""+obj.getInt("time")};
	}
	
	public int[][] salvageCommonRareInvData() throws Exception
	{
		curl_pull("salvageL1CommonCards","");
		String json = curl_pull("salvageL1RareCards","");
		return new int[][]{getInventory(json),getData(json)};
	}
	
	public String curl_pull(String request, String add_cookie) throws Exception
	{
		
		HttpURLConnection httpcon = (HttpURLConnection) ((new URL("https://mobile.tyrantonline.com/api.php?message=" + request).openConnection()));
		httpcon.setDoOutput(true);
		//HEADERS
		httpcon.setRequestProperty("Host", "mobile.tyrantonline.com");
		httpcon.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:55.0) Gecko/20100101 Firefox/55.0");
		httpcon.setRequestProperty("Accept","*/*");
		httpcon.setRequestProperty("Accept-Language","de,en-US;q=0.7,en;q=0.3");
		httpcon.setRequestProperty("Referer","http://game208033.konggames.com/gamez/0020/8033/live/index.html?kongregate_game_version=1502729120&kongregate_host=www.kongregate.com");
		httpcon.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		httpcon.setRequestProperty("Origin","http://game208033.konggames.com");
		httpcon.setRequestProperty("DNT","1");
		httpcon.setRequestProperty("Connection","keep-alive");
		//
		httpcon.setRequestMethod("POST");
		httpcon.connect();

		byte[] outputBytes = (cookie + add_cookie).replaceAll(";", "&").getBytes("UTF-8");
		
		OutputStream os = httpcon.getOutputStream();
		os.write(outputBytes);
		os.close();
		
		/*StringBuilder builder = new StringBuilder();
		builder.append(httpcon.getResponseCode())
		       .append(" ")
		       .append(httpcon.getResponseMessage())
		       .append("\n");
		Map<String, List<String>> map = httpcon.getHeaderFields();
		for (Map.Entry<String, List<String>> entry : map.entrySet())
		{
		    if (entry.getKey() == null) 
		        continue;
		    builder.append( entry.getKey())
		           .append(": ");

		    List<String> headerValues = entry.getValue();
		    Iterator<String> it = headerValues.iterator();
		    if (it.hasNext()) {
		        builder.append(it.next());

		        while (it.hasNext()) {
		            builder.append(", ")
		                   .append(it.next());
		        }
		    }

		    builder.append("\n");
		}

		System.out.println(builder);*/
		
		String json = StreamUtil.readFullyAsString(httpcon.getInputStream(), "UTF-8");
		return json;
	}
	/*
	public String readFullyAsString(InputStream inputStream, String encoding) throws IOException {
        return readFully(inputStream).toString(encoding);
    }

    private ByteArrayOutputStream readFully(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos;
    }*/
}
