package com.alvarolara;

import java.sql.SQLException;
import java.util.ArrayList;

import com.alvarolara.lineas.Linea;

public class Parada {
	
	/**
	 * Posición de la parada.
	 */
	private OsmNode nodo;
	
	/**
	 * Nombre de la parada.
	 */
	private String nombre;
	
	/**
	 * Lineas a la que pertenece.
	 */
	private ArrayList<String> lineas;
	
	
	/**
	 * Constructor de parada.
	 * @param nombre
	 * @param nodo
	 * @throws SQLException 
	 */
	public Parada(MySQL sql, ArrayList<OsmNode> node, OsmNode nodo, String nombre, ArrayList<String> linea) throws SQLException{
		this.nodo = nodo;
		this.nombre = nombre;
		this.lineas = linea;
		
		int i =0;
		
		//Buscamos el nodo parada dentro de la linea.
		OsmNode parada = encuentraParada(node);
		
		//Insertamos la Parada.
		int id_parada = sql.insertaParada(nombre, nodo.getLat(), nodo.getLon());
		
		//Inseramos las paradas en la BBDD.
		while(linea.size() != i){
			sql.insertaParadaLinea(id_parada, linea.get(i), parada.getLat(), parada.getLon());
			i++;
		}
	}
	
	/**
	 * Funcion que a partir del nodo parada, encuentra el menos distante a la linea.
	 * Dicho nodo sera guardado en la base de datos y usado para calcular el tiempo restante.
	 * @param node
	 * @return
	 */
	private OsmNode encuentraParada(ArrayList<OsmNode> node){
		//recorrer el arraylist de nodos mirando la distancia con node, 
		//el que tenga la distancia minima, sera el nodoparada y almacenado en la BBDD.
		
		double distanciaAcumulada = 999999999;
		double distanciaObtenida = 0;
		OsmNode parada = new OsmNode(-99999);
		
		for(int i=0;i<node.size()-1;i++){
			
			distanciaObtenida = Parseador.distanciaHaversine(this.nodo, node.get(i),"m");
			
			if(distanciaObtenida < distanciaAcumulada){
				distanciaAcumulada = distanciaObtenida;
				parada = new OsmNode(node.get(i));
			}
		}
		return parada;
	}
}
