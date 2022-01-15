package de.neuwirthinformatik.Alexander.TU;

import de.neuwirthinformatik.Alexander.TU.util.Log;

public class TU {
	public static class Settings 
	{
		public boolean DEBUG_LOGGING = false;
		public boolean ASSERT = false;
		public boolean A_H = false;
		public boolean IS_FULL_FEATURED = false;

		public String[] permissions = new String[] {};
	}
	
	public static Settings settings = new Settings();
	public static Log log = new Log();
}
