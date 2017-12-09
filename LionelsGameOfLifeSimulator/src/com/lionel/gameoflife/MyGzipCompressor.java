package com.lionel.gameoflife;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class MyGzipCompressor {

	public static void StringToFile(String str, String filepath) throws Exception {

		FileOutputStream fos = new FileOutputStream(filepath);
		GZIPOutputStream gzos = new GZIPOutputStream(fos);
		OutputStreamWriter osw = new OutputStreamWriter(gzos, "UTF-8");
		BufferedWriter writer = new BufferedWriter(osw);
		writer.append(str);

		if (writer != null) {
			writer.close();
		}
	}

	public static String FileToString(String filepath) throws Exception {

		FileInputStream fis = new FileInputStream(filepath);
		GZIPInputStream gzis = new GZIPInputStream(fis);
		InputStreamReader isr = new InputStreamReader(gzis, "UTF-8");
		BufferedReader reader = new BufferedReader(isr);

		String str = "";
		String line;
		while ((line = reader.readLine()) != null)
			str += line;

		if (reader != null)
			reader.close();

		return str;

	}

}
