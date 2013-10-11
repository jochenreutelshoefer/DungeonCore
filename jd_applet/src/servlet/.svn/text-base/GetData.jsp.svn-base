<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="servlet.*"%>
<%@page import="java.io.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	
	//String content = DataReader.readData(request); 

int bufferSize = 8192;
String path = request.getRealPath("");
File f = new File(path + "/highscores.dat");
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
		result = "file " + f.toString() + " not found!";
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

	response.setContentType("text/plain;");
	response.setCharacterEncoding("UTF-8");
	//response.getOutputStream().write(result.getBytes());
	
%><%=result %>
