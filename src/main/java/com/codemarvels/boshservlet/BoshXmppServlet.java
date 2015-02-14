package com.codemarvels.boshservlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.codemarvels.boshservlet.bosh.ConnectionManager;
import com.google.common.io.CharStreams;

public class BoshXmppServlet extends HttpServlet{

	private static final long serialVersionUID = -6582356799296606455L;
    private ConnectionManager connectionManager = new ConnectionManager();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)  throws ServletException, IOException
    {
            PrintWriter out = new PrintWriter(resp.getOutputStream());
            out.println("BOSHServlet1.0");
            out.println("An implementation of BOSH protocol with built-in XMPP over BOSH module (http://xmpp.org/extensions/xep-0124.html, " +
                            "http://xmpp.org/extensions/xep-0206.html )");
            out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
            try {
                    DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = fac.newDocumentBuilder();
            InputSource inStream = new InputSource();
            String requestXML = CharStreams.toString(request.getReader());
            inStream.setCharacterStream(new StringReader(requestXML));
            Document doc = db.parse(inStream);
                    connectionManager.handleRequest(doc, response, request);
            } catch (SAXException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            } catch (Exception e) {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,e.getMessage());
            }
    }

    @Override
    public void init() throws ServletException
    {
    }
}
