package de.neuwirthinformatik.Alexander.TU.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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
		return readFileUsingFiles(path);
	}

	public static String readFileUsingFiles(String path) {
		byte[] encoded;
		try {
			encoded = Files.readAllBytes(Paths.get(path));
			return new String(encoded, Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String readFileUsingFileInputStream(String file) {
		FileInputStream fin = null;
		String ret = "";
		try {
			fin = new FileInputStream(file);
			ret = StreamUtil.convertStreamToString(fin);
		} catch (Exception e) {
			e.printStackTrace();

			// Log.e("Exception", "File read failed: " + e.toString());
		} finally {
			try {
				if (fin != null)
					fin.close();
			} catch (IOException e) {
				e.printStackTrace();
				// Log.e("Exception", "File close failed: " + e.toString());
			}
		}
		return ret;
	}

	public static void copyFile(String src, String dst) {
		try {
			Files.copy(new File(src).toPath(), new File(dst).toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void appendLine(String file, String... lines) {
		appendLines(file, lines);
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

	public static String readFirstLine(String file) {
		FileInputStream fin = null;
		BufferedReader reader = null;
		String ret = "";
		try {
			fin = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(fin));
			ret = reader.readLine();
		} catch (Exception e) {
			e.printStackTrace();

			// Log.e("Exception", "File read failed: " + e.toString());
		} finally {
			try {
				if (reader != null)
					reader.close();
				if (fin != null)
					fin.close();
			} catch (IOException e) {
				e.printStackTrace();
				// Log.e("Exception", "File close failed: " + e.toString());
			}
		}
		return ret;
	}

	public static void writeToFile(String file, String data) {
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(file);
			stream.write(data.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
			// Log.e("Exception", "File write failed: " + e.toString());
		} finally {
			try {
				if (stream != null)
					stream.close();
			} catch (IOException e) {
				e.printStackTrace();
				// Log.e("Exception", "File close failed: " + e.toString());
			}
		}
	}
}
