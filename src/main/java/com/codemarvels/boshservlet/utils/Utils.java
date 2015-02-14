package com.codemarvels.boshservlet.utils;

import java.io.StringWriter;
import java.util.Random;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.NodeList;
public class Utils {

	private static TransformerFactory tff = TransformerFactory.newInstance();
    public static String serialize(NodeList nl)
    {
            String out = "";
            StreamResult strResult = new StreamResult();
            strResult.setWriter(new StringWriter());
            try 
            {
                    Transformer tf = tff.newTransformer();
                    tf.setOutputProperty("omit-xml-declaration", "yes");
                    for (int i = 0; i < nl.getLength(); i++) 
                    {
                            tf.transform(new DOMSource(nl.item(i)), strResult);
                            String tStr = strResult.getWriter().toString();
                            out += tStr;
                            strResult.getWriter().flush();
                    }
            } catch (Exception e) 
            {
                    AppLogger.info("XML.toString(Document): ");
            }
            return out;
    }
    public static String createRandomID(int length)
    {
            String charlist = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_";
            Random rand = new Random();
            String str = new String();
            for (int i = 0; i < length; i++)
            {
                    str += charlist.charAt(rand.nextInt(charlist.length()));
            }
            return str;

    }
}
