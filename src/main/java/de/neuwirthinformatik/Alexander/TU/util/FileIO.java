package de.neuwirthinformatik.Alexander.TU.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileIO {
	public static void deleteFile(String file) {
		File f = new File(file);
		if (f.exists())
			f.delete();
	}
	public static void createFile(String file) {
		File f = new File(file);
		if (!f.exists())
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	public static String readFile(String path) {
		byte[] encoded;
		try {
			encoded = Files.readAllBytes(Paths.get(path));
			return new String(encoded, Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	public static void copyFile(String src, String dst) {
		try {
			Files.copy(new File(src).toPath(), new File(dst).toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void appendLine(String file, String... lines) {
		appendLines(file,lines);
	}
	public static void appendLine(String file, String line) {
		File f = new File(file);
		if (!f.exists()) {
			if (f.getParentFile() != null)
				f.getParentFile().mkdirs();
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		BufferedWriter output;
		try {
			output = new BufferedWriter(new FileWriter(file, true));
			output.append(line + System.getProperty("line.seperator"));
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void appendLines(String file, String[] lines) {
		File f = new File(file);
		if (!f.exists()) {
			if (f.getParentFile() != null)
				f.getParentFile().mkdirs();
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		BufferedWriter output;
		try {
			output = new BufferedWriter(new FileWriter(file, true));
			for (String line : lines)
				output.append(line + "\n");
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
