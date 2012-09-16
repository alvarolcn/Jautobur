package com.alvarolara;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class Parseador {
	
	/**
	 * XMLStramReader para parsear el fichero XML.
	 */
	XMLStreamReader reader;
	
	/**
	 * Arraylist para almacenar en memoria el archivo OSM para postprocesarlo.
	 */
	ArrayList<OsmObjeto> listaObjetos = new ArrayList<OsmObjeto>();
	
	/**
	 * Almacenar en memoria el archivo OSM solo con el way e internemente nodos con lat y lon.
	 */
	OsmWay listaWays = null;
	
	/**
	 * Almacenar en memoria el archivo OSM de ruta procesado.
	 */
	OsmWay listaWaysProcesados = null;
	
	/**
	 * Numero total de nodos encontrados.
	 */
	int totalNodes=0;
	
	/**
	 * numero total del ways encontrados.
	 */
	int totalWays = 0;
	
	/**
	 * numero total del Nd encontrados.
	 */
	int totalNd=0;
	
	

	public Parseador(String inputFilename){
		try{
			File file = new File(inputFilename);
			FileInputStream inputStream = new FileInputStream(file);
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			reader = inputFactory.createXMLStreamReader(inputStream);
		} catch (FileNotFoundException e){
			System.out.println("El archivo OSM no se ha encontrado: " + inputFilename);
			e.printStackTrace();
		} catch (XMLStreamException e){
			System.out.println("Error al parsear el archivo OSM. Comprueba si está bien creado el archivo OSM.");
			e.printStackTrace();
		}
	}
	
	/**
	 * FUncion que escribe los nodos y el way ya procesados.
	 */
	public void escribeRuta(OsmXmlWriter writer){
		//recorrer el arraylist.
		for(int i =0; i<listaWaysProcesados.getListaNodos().size(); i++){
			writer.writeNode(listaWaysProcesados.getListaNodos().get(i));
		}
		writer.writeWay(listaWaysProcesados);
		writer.closeDoc();
	}
	
	
	/**
	 * Parsear todo el archivo: nodes, ways y nd.
	 * 
	 */
	public void parseaTodo(){
		int contador = 0;
		
		int nodosanadidos = 0;
		
		int id=0;
		String visible = "";
		ArrayList<OsmNode> listaNodos = new ArrayList<OsmNode>();
		try {
			while (reader.hasNext()){
				int type = reader.next();
				switch (type){				
				case XMLStreamConstants.START_ELEMENT:
					String inicioTag = reader.getLocalName();
					if (inicioTag == "node"){
						
						//Añadir al arraylist los nodos, para despues procesar los ways.
						listaObjetos.add(new OsmNode(Integer.parseInt(reader.getAttributeValue(null,"id")), 
								reader.getAttributeValue(null,"action"), reader.getAttributeValue(null,"visible"), 
								Double.parseDouble(reader.getAttributeValue(null,"lat")), 
								Double.parseDouble(reader.getAttributeValue(null,"lon"))));
						totalNodes++;
					} else if (inicioTag == "way"){
						//si contador vale mas de 0, almacenar en arraylis
						if(contador>0){
							listaObjetos.add(new OsmWay(id, visible, listaNodos));
							System.out.println("nodos añadidos: " + nodosanadidos);
							listaNodos = new ArrayList<OsmNode>();
						}
						
						//atributos del way
						id = Integer.parseInt(reader.getAttributeValue(null,"id"));
						
						visible = reader.getAttributeValue(null,"visible");
						
						System.out.println("Way= "+id);
						
						totalWays++;
						contador++;

					}else if (inicioTag == "nd"){
						
						int ref = Integer.parseInt(reader.getAttributeValue(null,"ref"));
						
						System.out.println("encontrado nd=" + ref + " en way=" + id);
						
						listaNodos.add(new OsmNode(ref));
						
						totalNd++;
						nodosanadidos++;
					}
					break;
				case XMLStreamConstants.END_DOCUMENT:
					// todos los nodos, ways y nd han sido parseados.
					System.out.println("##### Fin del documento");
					break;
				
				default:
					break;
				}
			}
			listaObjetos.add(new OsmWay(id, visible, listaNodos));
			estadisticas();
					
		} catch (XMLStreamException e) {
			System.err.println("Error al parsear los nodo. ¿Esta el documento bien formateado?");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Cerramos el XMLStreamReader y mostramos las estadisticas
	 * de nodes, way y nd encontrados.
	 */
	private void estadisticas(){
		// mostrar estadisticas del parseo
		System.out.println("______________________________\n");
		System.out.println("Nodes encontrados: "+ totalNodes + "\n");
		System.out.println("Ways encontrados: "+ totalWays + "\n");
		System.out.println("Nd encontrados: "+ totalNd + "\n");
		System.out.println("Hay "+ (totalNd-totalNodes) + " nodos comunes\n");
		System.out.println("______________________________\n");
	}
	
	/**
	 * Prueba para imprimir el arraylist que esta en memoria.
	 */
	public void imprimeListaObjetos(){
		System.out.println("imprimiendo la lista de objetos almacenados en memoria...");
		for (OsmObjeto o : listaObjetos) {
			System.out.println(o.toString());
		}
	}
	
	/**
	 * Funcion que se le pasa un idnodo y devuelve el objeto nodo completo.
	 * @param idNodo
	 * @return
	 */
	public OsmNode buscaNodo(int idNodo){
		ArrayList<OsmNode> auxiliar = new ArrayList<OsmNode>();
		
		for(int i =0; i<listaObjetos.size(); i++){
			if(listaObjetos.get(i) instanceof OsmNode){
				auxiliar.add((OsmNode)listaObjetos.get(i));
			}
		}
		
		for(int i =0; i<listaObjetos.size(); i++){
			if(auxiliar.get(i).getId() == idNodo){
				return auxiliar.get(i);
			}
		}
		return auxiliar.get(0);
	}
	
	
	/**
	 * A partir del arraylist de nodos y ways, relacionamos en un arraylist auxiliar
	 * cada nd con su lat y lon del nd que apunta. Despues se vuelve a volcar al
	 * arraylist original y solo quedara un osmway en el.
	 */
	public void procesaRutaArraylist(){
		
		for(int i = 0; i<listaObjetos.size(); i++){
			//sacar el way y ponerlo en el auxiliar.
			if(listaObjetos.get(i) instanceof OsmWay){
				//copiar el way objeto al nuevo array.
				listaWays = (OsmWay)listaObjetos.get(i);
			}
		}
		
		//poner el node id = nd ref en arraylist
		for(int i = 0; i<listaWays.getListaNodos().size(); i++){
			//asignar la las propiedades de nodo a nd
			
			listaWays.getListaNodos().set(i, buscaNodo(listaWays.getListaNodos().get(i).getId()));
			
		}
	}
	
	
	/**
	 * Pasados dos nodos, devuelve uno nuevo como punto nuevo entre ambos dos.
	 * @param nodo1
	 * @param nodo2
	 * @return
	 */
	public OsmNode puntoMedio(OsmNode nodo1, OsmNode nodo2){
		
		//latantes
		int latantes = ((int) nodo1.getLat()+(int) nodo2.getLat())/2;
		
		//latdespues
		String lat1 = Double.toString(nodo1.getLat());
		lat1 = lat1.substring(lat1.indexOf(".")+1, lat1.length());
		int lat1despues = Integer.parseInt(lat1);
		
		String lat2 = Double.toString(nodo2.getLat());
		lat2 = lat2.substring(lat2.indexOf(".")+1, lat2.length());
		int lat2despues = Integer.parseInt(lat2);
		
		int latdespues = (lat1despues+lat2despues)/2;
		
		//lonantes
		int lonantes = ((int) nodo1.getLon()+(int) nodo2.getLon())/2;
		
		//londespues
		String lon1 = Double.toString(nodo1.getLon());
		lon1 = lon1.substring(lon1.indexOf(".")+1, lon1.length());
		int lon1despues = Integer.parseInt(lon1);
		
		String lon2 = Double.toString(nodo2.getLon());
		lon2 = lon2.substring(lon2.indexOf(".")+1, lon2.length());
		int lon2despues = Integer.parseInt(lon2);
		
		int londespues = (lon1despues+lon2despues)/2;
		
		System.out.println("el punto medio de " + nodo1.getLat() + "," + nodo1.getLon() + " y de " + nodo2.getLat() + "," + nodo2.getLon() +" es: Lat: " + latantes + "." + latdespues + ", Long: " + lonantes + '.' + londespues);
		
		//añadir un id no 
		return new OsmNode(-(int)(Math.random()*1000),null,"true",new Double(latantes + "." + latdespues),new Double(lonantes + "." + londespues));
	}
	
	
	/**
	 * funcion que redondea el double a los decimales que pasemos.
	 * @param numero
	 * @param decimales
	 * @return
	 */
	public static double redondearDouble(double numero, int decimales){
		numero = numero*(java.lang.Math.pow(10, decimales));
		numero = java.lang.Math.round(numero);
		numero = numero/Math.pow(10, decimales);

		return numero;
	}
	
	
	/**
	 * calcula la distancia entre dos nodos por el metodo Haversine.
	 * @param nodo1
	 * @param nodo2
	 * @param tipo
	 * @return
	 */
	public static double distanciaHaversine(OsmNode nodo1,OsmNode nodo2, String tipo){
		double distancia = 0;
		distancia = (Math.sin(Math.toRadians(nodo1.getLat())) * Math.sin(Math.toRadians(nodo2.getLat()))) + (Math.cos(Math.toRadians(nodo1.getLat())) * Math.cos(Math.toRadians(nodo2.getLat())) * Math.cos(Math.toRadians(nodo1.getLon() - nodo2.getLon()))); 
		distancia = Math.acos(distancia); 
		distancia = Math.toDegrees(distancia); 
		distancia = distancia * 60 * 1.1515 * 1.609344;
		
		if(tipo == "km"){
			return redondearDouble(distancia, 6);
		}else if(tipo == "m"){
			return redondearDouble(distancia * 1000, 6);
		}
		if(distancia < 0)
			return 0;
		return 0; 
	}
	
	/**
	 * Calcula la distancia del way en km o m
	 * segun parametro.
	 */
	public double calculaDistanciaRuta(String tipo){
		double distancia = 0;
		
		//procesar el arraylist del way y medir distancia nodo a nodo.
		for(int i=0; i<listaWays.getListaNodos().size()-1; i++){
			distancia += distanciaHaversine(listaWays.getListaNodos().get(i), listaWays.getListaNodos().get(i+1), tipo);
		}
		
		if(tipo == "km"){
			return redondearDouble(distancia,2);
		}else{
			return distancia;
		}
	}
	
	
	/**
	 * Cada x metros se crea una nueva coordenada gps siguiendo
	 * el trayecto seguido por el way.
	 */
	public void calculaMasCoordenadas(double velocidadMedia){
		int i = 0;
		int contadorNodos = 1;
		OsmNode nodo1;
		OsmNode nodo2;
		OsmNode auxiliar;
		double distancia = 0;
		boolean fin = false;
		
		//convertir la velocidad de km/h a metros/seg
		double metrosPorSegundo = (velocidadMedia/3.6);
		//convertirlo a km, para la funcion calculapunto
		metrosPorSegundo = metrosPorSegundo/1000;
		
		listaWaysProcesados = new OsmWay(-1, "true", new ArrayList<OsmNode>());
		//asignar el primer nodo.
		listaWaysProcesados.getListaNodos().add(0,new OsmNode(contadorNodos, "modify", "true", listaWays.getListaNodos().get(0).getLat(),listaWays.getListaNodos().get(0).getLon()));
		
		int tam = listaWays.getListaNodos().size();
		while(tam > i+1){
			nodo1 = listaWays.getListaNodos().get(i);
			nodo2 = listaWays.getListaNodos().get(i+1);
			//scar el bearing
			Bearing b = Bearing.calculateBearing(nodo1, nodo2, null);
			b.calculateBearing(nodo1, nodo2, null);
		
			while(fin=true){
				double distanciahaversine = (distanciaHaversine(nodo1, nodo2, "m")/1000)-distancia;
				if(distanciahaversine > metrosPorSegundo){
					contadorNodos++;
					distancia = distancia + metrosPorSegundo;
					//Anadir el nuevo nodo.
					auxiliar = b.findPoint(nodo1, b.getAngle(), distancia, new OsmNode(contadorNodos));
					listaWaysProcesados.getListaNodos().add(contadorNodos-1,auxiliar);
				}else{
					//resetear distancia para los siguientes pares de nodos.
					
					distancia = 0;
					break;
				}
			}
			i++;
			contadorNodos++;
			listaWaysProcesados.getListaNodos().add(contadorNodos-1,new OsmNode(contadorNodos, "modify", "true", nodo2.getLat(),nodo2.getLon()));
			
		}
		System.out.println("Creados " + contadorNodos + " nodos adicionales con velocidad: " + velocidadMedia + "km/h.");
	}

	public XMLStreamReader getReader() {
		return reader;
	}

	public void setReader(XMLStreamReader reader) {
		this.reader = reader;
	}

	public ArrayList<OsmObjeto> getListaObjetos() {
		return listaObjetos;
	}

	public void setListaObjetos(ArrayList<OsmObjeto> listaObjetos) {
		this.listaObjetos = listaObjetos;
	}

	public OsmWay getListaWays() {
		return listaWays;
	}

	public void setListaWays(OsmWay listaWays) {
		this.listaWays = listaWays;
	}

	public OsmWay getListaWaysProcesados() {
		return listaWaysProcesados;
	}

	public void setListaWaysProcesados(OsmWay listaWaysProcesados) {
		this.listaWaysProcesados = listaWaysProcesados;
	}

	public int getTotalNodes() {
		return totalNodes;
	}

	public void setTotalNodes(int totalNodes) {
		this.totalNodes = totalNodes;
	}

	public int getTotalWays() {
		return totalWays;
	}

	public void setTotalWays(int totalWays) {
		this.totalWays = totalWays;
	}

	public int getTotalNd() {
		return totalNd;
	}

	public void setTotalNd(int totalNd) {
		this.totalNd = totalNd;
	}
}