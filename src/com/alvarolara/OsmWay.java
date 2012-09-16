package com.alvarolara;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class OsmWay implements OsmObjeto {
	
	/**
	 * id del way.
	 */
	private int  id;
	
	/**
	 * indica si es visible.
	 */
	private String visible;
	
	/**
	 * nodes asociados al way.
	 */
	private ArrayList<OsmNode> listaNodos = new ArrayList<OsmNode>();
	

	public OsmWay(int id, String visible, ArrayList<OsmNode> listaNodos){
		
			System.out.println("--------------------\nway: id=" + id + " visible=" + visible);
			this.id = id;
			this.visible = visible;
			this.listaNodos = listaNodos;
	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}

	public String getVisible() {
		return visible;
	}


	public void setVisible(String visible) {
		this.visible = visible;
	}
	
	public ArrayList<OsmNode> getListaNodos() {
		return listaNodos;
	}


	public void setListaNodos(ArrayList<OsmNode> listaNodos) {
		this.listaNodos = listaNodos;
	}
	
	public String toString(){
		String cadena = "--------------------\nway: id=" + id + " visible=" + visible;;
		for (OsmNode o : listaNodos) {
			cadena += o.toString(); 
		}
		return cadena;
		
	}
}
