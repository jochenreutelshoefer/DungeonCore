package graphics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import log.Log;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 11.09.16.
 */
public class ImageConverter {

	private static final String path = "/Users/jochenreutelshofer/Documents/privat/Dungeon resources/ReinerProkein/red knight bitmaps/";

	public static final String TRANS_COLOR1 = "61442B";
	public static final String TRANS_COLOR2 = "6A4C30";

	/*
	to find out the background color use following ImageMagick call
	convert '/Users/jochenreutelshofer/Documents/privat/Dungeon resources/ReinerProkein/T firedragon/firedragon bitmaps flying/firedragon fliegt e0000.bmp' -define histogram:unique-colors=true -format %c histogram:info:-

	 */

	public static void main(String[] args) throws IOException, InterruptedException {
		File root = new File(path);
		if (!root.exists()) {
			Log.severe("Root folder not found: " + root);
		}

		File[] rootContent = root.listFiles();
		for (File file : rootContent) {
			if (file.isDirectory()) {
				convertAllBitmapsToGif(file);
			}
		}
	}

	private static void convertAllBitmapsToGif(File folder) throws IOException, InterruptedException {
		File[] allFiles = folder.listFiles();
		assert allFiles != null;
		for (File file : allFiles) {
			if (file.isDirectory()) {
				convertAllBitmapsToGif(file);
			}
		}

		File[] bitmaps = folder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith("bmp");
			}
		});
		for (File bitmap : bitmaps) {
			String gifName = bitmap.getName().replace(".bmp", ".gif");
			File gifFile = new File(folder, gifName);
			if (!gifFile.exists()) {
				//String command = "convert '"+bitmap.getAbsolutePath()+"' -transparent '#61442B' '"+gifFile.getAbsolutePath()+"'";
				String[] command = { "convert", "" + bitmap.getAbsolutePath() + "", "-transparent", "#" + TRANS_COLOR1, "" + gifFile
						.getAbsolutePath() + "" };
				System.out.println("Converting: " + bitmap.getName());
				ProcessBuilder processBuilder = new ProcessBuilder(command);
				Process process = processBuilder.start();
				process.waitFor();
				int exitValue = process.exitValue();
				if (exitValue != 0) {
					printStream(process.getErrorStream());
					throw new IOException("Command could not successfully be executed: " + command);
				}
			}
		}
	}

	public static void printStream(InputStream str) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(str));
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			System.out.println(inputLine);
		}
		in.close();
	}
}
