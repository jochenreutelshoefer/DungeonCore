package servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import javax.servlet.*;

public class DataReader {

	public static String readData(ServletRequest request) {
		int bufferSize = 8192;
		String path = request.getRealPath("");
		File f = new File(path + "highscores.dat");
		String result = "problem reading file " + f.toString();
		if (f.exists()) {
			try {
				InputStream in = new FileInputStream(f);
				byte[] buffer = new byte[0];
				byte[] cache = new byte[bufferSize];
				while (true) {
					int len = in.read(cache);
					if (len <= 0) {
						break;
					}
					byte[] newBuffer = new byte[buffer.length + len];
					System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
					System.arraycopy(cache, 0, newBuffer, buffer.length, len);
					buffer = newBuffer;
				}
				in.close();
				result = new String(buffer);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return "file " + f.toString() + " not found!";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
}
