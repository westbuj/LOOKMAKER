package com.jnn.util;


/*
 * @(#)SimpleElement.java
 */

//import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

/**
 * <code>SimpleElement</code> is the only node type for
 * simplified DOM model.
 */
public class SimpleElement {
	private String tagName;
	private String text;
	private Hashtable attributes;
	private Vector childElements;

	public SimpleElement(String tagName) {
		this.tagName = tagName;
		attributes = new Hashtable();
		childElements = new Vector();
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAttribute(String name) {
		return (String)attributes.get(name);
	}

	public void setAttribute(String name, String value) {
		attributes.put(name, value);
	}

	public void addChildElement(SimpleElement element) {
		childElements.addElement(element);
	}

	public Object[] getChildElements() {
		Object o[]=new Object[childElements.size() ];
		childElements.copyInto(o); 

		return o;
	}
}
