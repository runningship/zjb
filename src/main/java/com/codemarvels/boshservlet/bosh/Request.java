package com.codemarvels.boshservlet.bosh;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
public class Request {
	private Document doc;
    private Node rootNode;
    private NamedNodeMap attributes;
    private boolean isSessCreationRequest;
    private transient HttpServletRequest webRequest;
    
    public Request(Document doc, HttpServletRequest webRequest)
    {
            this.doc = doc;
            rootNode = doc.getDocumentElement();
            attributes = rootNode.getAttributes();
            this.webRequest = webRequest; 
    }
    public Document getDoc() {
            return doc;
    }
    public void setDoc(Document doc) {
            this.doc = doc;
    }
    public Node getRootNode() {
            return rootNode;
    }
    public void setRootNode(Node rootNode) {
            this.rootNode = rootNode;
    }
    public NamedNodeMap getAttributes() {
            return attributes;
    }
    public void setAttributes(NamedNodeMap attributes) {
            this.attributes = attributes;
    }
    public boolean isSessCreationRequest() {
            return isSessCreationRequest;
    }
    public void setSessCreationRequest(boolean isSessCreationRequest) {
            this.isSessCreationRequest = isSessCreationRequest;
    }
    public HttpServletRequest getWebRequest() {
            return webRequest;
    }
    public void setWebRequest(HttpServletRequest webRequest) {
            this.webRequest = webRequest;
    }
}
