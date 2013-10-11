<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="servlet.*"%>
<%@page import="java.io.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String player_name = request.getParameter("player_name");
	String hero_name = request.getParameter("hero_name");
	String exp = request.getParameter("exp");
	String level = request.getParameter("level");
	String round = request.getParameter("round");
	String kills = request.getParameter("kills");
	String type = request.getParameter("type");
	String comment = request.getParameter("comment");
	String dead = request.getParameter("dead");
	String start_time = request.getParameter("start_time");
	String end_time = request.getParameter("end_time");
	String reg = request.getParameter("reg");
	String liga = request.getParameter("liga");

	String sepp = "&&&";

	String storeValue = "$$$" + player_name + sepp + hero_name + sepp
			+ exp + sepp + level + sepp + round + sepp + kills + sepp
			+ type + sepp + comment + sepp + dead + sepp + start_time
			+ sepp + end_time + sepp + reg + sepp + liga;
	String result = "hm";
	String path = request.getRealPath("");
	File f = new File(path+"/highscores.dat");
	if (f.exists()) {
		try {
			PrintWriter outWriter = new PrintWriter(new BufferedWriter(
					new FileWriter(f.getAbsoluteFile(), true)));
			outWriter.print(storeValue);
			outWriter.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
		result = "data appended";
	} else {
		result = "file not found";
	}
%><%=result%>s
