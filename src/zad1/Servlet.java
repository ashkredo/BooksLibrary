/**
 *
 *  @author Shkred Artur S15444
 *
 */

package zad1;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class mySevlet
 */
@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String url = "jdbc:derby://localhost/ksidb";
	private Connection con;
	private PrintWriter printWriter;
	private boolean poNazwieKsiazki;
	private String HTML2;

	/**
	 * @see HttpServlet#HttpServlet()
	 */

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String HTML1 = "<title>TPO4_SA_S15444</title> <body bgcolor=\"#C0C0C0\"> <center> <h1>TPO4_SA_S15444</h1> <form  text-align: right;"
					+ " action = \"Servlet\">\r\n <input type=\"text\" name =\"name\">\r\n <select name = \"po\">\r\n"
					+ "<option>ZNAJDZ PO NAZWIE KSIAZKI</option>\r\n <option>ZNAJDZ KSIAZKE WEDLUG AUTORA"
					+ "</option>\r\n </select>\r\n <input type=\"submit\" value=\"ZNAJDZ\">\r\n \r\n </form> </body> </center>";
			
			HTML2 = "<html> <head> <style> table { font-family: arial, sans-serif; border-collapse: collapse;"
						+ "width: 100%; } td, th { border: 1px solid #dddddd; text-align: left; padding: 8px; } tr:nth-child(even) {"
						+ "background-color: #dddddd; } </style> </head> <body> <table> <tr> <th>Autor</th>"
						+ "<th>Nazwa</th> <th>Cena</th> </tr>";
					
			printWriter = response.getWriter();
			printWriter.println(HTML1);

			String[][] ksiazki;
			
			if (!request.getParameter("name").isEmpty()) {
				if (request.getParameter("po").equalsIgnoreCase("ZNAJDZ KSIAZKE WEDLUG AUTORA")) {
					poNazwieKsiazki = false;
					printWriter = response.getWriter();
					ksiazki = getKsiazki(request.getParameter("name"));
					write(ksiazki);
				} else {
					poNazwieKsiazki = true;
					printWriter = response.getWriter();
					ksiazki = getKsiazki(request.getParameter("name"));
					write(ksiazki);
				}
			} else {
				printWriter = response.getWriter();
				ksiazki = getKsiazki("wzystkieKsiazki");
				write(ksiazki);
			}
			printWriter.close();
		} catch (Exception ex){}

	}

	private  String[][] getKsiazki(String parameter) {
		int k = 0;
		try {
			Class.forName("org.apache.derby.jdbc.ClientDriver");
			con = DriverManager.getConnection(url);
			con.getMetaData();
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = stmt.executeQuery(
					"SELECT AUTOR.NAME, POZYCJE.TYTUL, POZYCJE.CENA FROM AUTOR INNER JOIN POZYCJE ON POZYCJE.AUTID = AUTOR.AUTID");
			rs.afterLast();
			while (rs.previous()) {
				k++;
			}
			if (con != null)
				con.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		 String[][] books = new String[k][3];
		if (parameter.equals("wzystkieKsiazki")) {
				books = getBook("SELECT AUTOR.NAME, POZYCJE.TYTUL, POZYCJE.CENA  FROM autor, pozycje where POZYCJE.AUTID = AUTOR.AUTID ORDER BY AUTOR.NAME DESC", books);
		} else if (poNazwieKsiazki && !parameter.equals("wzystkieKsiazki")) {
				books = getBook("SELECT AUTOR.NAME, POZYCJE.TYTUL, POZYCJE.CENA  FROM autor, pozycje where POZYCJE.AUTID = AUTOR.AUTID AND POZYCJE.TYTUL LIKE \'"
								+ parameter + "%\' ORDER BY AUTOR.NAME DESC", books);
		} else if (!poNazwieKsiazki && !parameter.equals("wzystkieKsiazki")) {
				books = getBook("SELECT AUTOR.NAME, POZYCJE.TYTUL, POZYCJE.CENA  FROM autor, pozycje where POZYCJE.AUTID = AUTOR.AUTID AND AUTOR.NAME LIKE \'"
								+ parameter + "%\'", books);

		}
			
		return books;
	}
	
	private String[][] getBook (String str, String[][] books){
		try {
			Class.forName("org.apache.derby.jdbc.ClientDriver");
			con = DriverManager.getConnection(url);
			con.getMetaData();
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = stmt.executeQuery(str);
			rs.afterLast();
			int count = 0;
			while (rs.previous()) {
				 books[count][0] = rs.getString(1);
				 books[count][1] = rs.getString(2);
				 books[count][2] = rs.getString(3);
				 count++;
			}
			if (con != null)
				con.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return books;
	}
	
	private void write(String[][] ksiazki){
		for(int i = 0; i < ksiazki.length; i++){
			HTML2 += "<tr>";
			for(int y = 0; y < ksiazki[i].length; y++){
				if(ksiazki[i][y] != null) {
					HTML2 += "<td>" + ksiazki[i][y] + "</td>";
				}
			} HTML2 += "</tr>";
		} HTML2 += "</table> </body>";
		printWriter.println(HTML2); 
	}

}