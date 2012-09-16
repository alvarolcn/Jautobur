package com.alvarolara;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class OsmXmlWriter {
	
	XMLStreamWriter writer;
	
	
	
	/**
	 * Constructs the XMLStreamWriter pointing onto the given file.
	 * Furthermore it adds the xml tag and osm tag.
	 * @param filename
	 */
	public OsmXmlWriter(String filename){
		OutputStream out;
		try {
			out = new FileOutputStream(filename);
			XMLOutputFactory factory = XMLOutputFactory.newInstance();
			writer = factory.createXMLStreamWriter(out, "UTF-8");
			writer.writeStartDocument("UTF-8","1.0");
			writer.writeStartElement("osm");
			writer.writeAttribute("version", "0.6");
			writer.writeAttribute("generator", "OSMAutobur");
		} catch (FileNotFoundException e) {
			System.err.println("No se ha podido escribir en el archivo/carpeta especificado: " + filename);
			e.printStackTrace();
		} catch (XMLStreamException e){
			System.out.println("Error al escribir el XML.");
			e.printStackTrace();
		}		
	}
	
	/**
	 * Adds a node tag to osm output file and writes all attributes, tags and licenseStatus.
	 * @param node
	 */
	public void writeNode(OsmNode node){
		try {
			writer.writeEmptyElement("node");
			//atributos del nodo.
			writer.writeAttribute("id", "-"+Integer.toString(node.getId()));
			writer.writeAttribute("visible", "true");
			writer.writeAttribute("lat", Double.toString(node.getLat()));
			writer.writeAttribute("lon", Double.toString(node.getLon()));
			//fin del elemento nodo
		} catch (XMLStreamException e) {
			System.err.println("Error al escribir los nodos.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a way tag to osm output file and writes all attributes, tags, node references and licenseStatus.
	 * @param way
	 */
	public void writeWay(OsmWay way){
		try {
			writer.writeStartElement("way");
			//atributos de way
			writer.writeAttribute("id", Integer.toString(way.getId()));
			writer.writeAttribute("action", "modify");
			writer.writeAttribute("visible", "true");
			// referencias a nodos.
			for (int i = 0; i < way.getListaNodos().size(); i++){
				writer.writeEmptyElement("nd");
				writer.writeAttribute("ref", "-"+Integer.toString(way.getListaNodos().get(i).getId()));
			}
			//cerrar el elemento way.
			writer.writeEndElement();
		} catch (XMLStreamException e) {
			System.err.println("Error al escribir el way.");
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Escribe el tag cierre para osm y cierra el stream.
	 */
	public void closeDoc(){
		try {
			writer.writeEndDocument();
			writer.close();
		} catch (XMLStreamException e) {
			System.err.println("Error al acabar y cerrar el archivo de salida.");
			e.printStackTrace();
		}
	}
}
