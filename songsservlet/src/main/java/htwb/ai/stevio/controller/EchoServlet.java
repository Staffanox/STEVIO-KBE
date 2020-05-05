package htwb.ai.stevio.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

public class EchoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private String uriToDB = null;

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
	    // Beispiel: Laden eines Initparameters aus der web.xml
		this.uriToDB = servletConfig.getInitParameter("uriToDB");
	}

	@Override
	public void doGet(HttpServletRequest request, 
	        HttpServletResponse response) throws IOException {
		// alle Parameternamen (keys)
		Enumeration<String> paramNames = request.getParameterNames();

		String responseStr = "";
		String param = "";
		while (paramNames.hasMoreElements()) {
			param = paramNames.nextElement();
			responseStr = responseStr + param + "=" 
			+ request.getParameter(param) + "\n";
		}
		response.setContentType("text/plain");
		try (PrintWriter out = response.getWriter()) {
			responseStr += "\n Your request will be sent to " + uriToDB;
			out.println(responseStr);
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain");
		ServletInputStream inputStream = request.getInputStream();
		byte[] inBytes = IOUtils.toByteArray(inputStream);
		try (PrintWriter out = response.getWriter()) {
			out.println(new String(inBytes));
		}
	}
	
	protected String getUriToDB () {
		return this.uriToDB;
	}
}
