package com.alvarolara.lineas;

import java.sql.SQLException;
import java.util.ArrayList;

import com.alvarolara.Dia;
import com.alvarolara.Hora;
import com.alvarolara.MySQL;
import com.alvarolara.OsmNode;
import com.alvarolara.OsmWay;
import com.alvarolara.OsmXmlWriter;
import com.alvarolara.Parseador;

public class Linea1 extends Linea{
	
	/**
	 * Nodos de la linea.
	 */
	private ArrayList<OsmNode> node;
	
	/**
	 * contructor Linea1.
	 */
	public Linea1(){
		linea = "1";
	}
	
	/**
	 * Devuelve la lista de nodes.
	 * @return
	 */
	public ArrayList<OsmNode> daveuelveOSMNode(){
		return node;
	}
	
	public Linea1(String archivoDestino, MySQL sql, String nombre, double velocidadMedia, boolean activo) throws Exception{
		
		try{
			//asignar la linea que es.
			this.linea = "1";
			
			
			String inputFilename = "lineas/"+archivoDestino+".osm";
			String outputFilename = "lineas/"+inputFilename.substring(inputFilename.indexOf("/")+1, inputFilename.indexOf(".")) + "-procesado.osm";
			Hora auxiliar;
			double distancia;
			ArrayList<Hora> HoraIda;
			ArrayList<Hora> HoraVuelta;
			OsmNode NodoIda;
			OsmNode NodoVuelta;
			
			Parseador parser = new Parseador(inputFilename);
			parser.parseaTodo();
			
			//comprobar que lo que esta en el arraylist esta bien
			parser.imprimeListaObjetos();
			
			//para un unico way con los nodos ya enlazados.
			parser.procesaRutaArraylist();
			
			//sacar la distancia de la linea.
			distancia = parser.calculaDistanciaRuta("km");
			
			System.out.println("Distancia de la linea " + linea + ": " + distancia + "km");
			
			//calcular mas coordenadas intermedias
			parser.calculaMasCoordenadas(velocidadMedia);
			
			
			//llamar al escritor xmlwiter.
			OsmXmlWriter writer = new OsmXmlWriter(outputFilename);
			parser.escribeRuta(writer);
			
			//cerrar el fichero creado.
			writer.closeDoc();
			
			node = parser.getListaWaysProcesados().getListaNodos();
			
			//borrar
			sql.resultadoLinea(linea);
			
			
			if(activo){
				//elimar los datos anteriores de la linea actual.
				sql.vaciaRutaLinea(linea);
				
				//insertar linea en la base de datos.
				sql.vaciaLinea(linea);
				sql.insertaLinea(linea, nombre, distancia, velocidadMedia, "SI");
				sql.vaciaNodosLinea(linea);
				sql.insertaNodosLinea(linea, node);
				
				
				
				System.out.println("**********PROCESANDO LINEA " + linea + "***********");
				
							
				/*****************************************************************/
				/***************************PARADAS*******************************/
				/*****************************************************************/
				
				OsmNode centro = new OsmNode(-99999999, null, "true", 42.34069813600197, -3.6986491146877025);
				OsmNode gamonal = new OsmNode(-99999999, null, "true", 42.35483582177026, -3.663541461280968);
				
				/*****************************************************************/
				/************************FIN PARADAS******************************/
				/*****************************************************************/
				
				
				
				/*****************************************************************/
				/***************PROCESAMOS LOS NOCTURNOS LECTIVOS*****************/
				/*****************************************************************/
				
				System.out.println("1/12 PROCESAMOS LOS NOCTURNOS LECTIVOS");
				
				//ArrayList Hora Ida.
				HoraIda= new ArrayList<Hora>();
				
				HoraIda.add(new Hora(23, 30, 00));
				HoraIda.add(new Hora(00, 00, 00));
				HoraIda.add(new Hora(00, 30, 00));
				
				//ArrayList Hora Vuelta.
				HoraVuelta= new ArrayList<Hora>();
				
				HoraVuelta.add(new Hora(23, 45, 00));
				HoraVuelta.add(new Hora(00, 15, 00));
				
				//Nodos de la parada de las posiciones de ida y de vuelta (paradas).
				//Gamonal.
				NodoIda = centro;
				//Centro.
				NodoVuelta = gamonal;
				
				//Procesamos las HoraIda y HoraVuelta.
				auxiliar = idaVuelta(sql, node, linea, Dia.L, NodoIda, NodoVuelta, HoraVuelta, HoraIda);
				
				//Parte a la proxima parada y cuando llegue, acaba.(no espera).
				llegadaPara(sql, node, linea, Dia.L, auxiliar, centro, gamonal);
				
				//Insertamos la linea.
				sql.insertaRutaLinea(linea);
				
				/*****************************************************************/
				/************FIN PROCESAMOS LOS NOCTURNOS LECTIVOS****************/
				/*****************************************************************/
				
				/*****************************************************************/
				/***************PROCESAMOS LOS NOCTURNOS FESTIVOS*****************/
				/*****************************************************************/
				
				System.out.println("2/12 PROCESAMOS LOS NOCTURNOS FESTIVOS");
				
				//ArrayList Hora Ida.
				HoraIda= new ArrayList<Hora>();
				
				HoraIda.add(new Hora(23, 30, 00));
				HoraIda.add(new Hora(00, 00, 00));
				HoraIda.add(new Hora(00, 30, 00));
				
				//ArrayList Hora Vuelta.
				HoraVuelta= new ArrayList<Hora>();
				
				HoraVuelta.add(new Hora(23, 45, 00));
				HoraVuelta.add(new Hora(00, 15, 00));
				
				//Nodos de la parada de las posiciones de ida y de vuelta (paradas).
				//Gamonal.
				NodoIda = centro;
				//Centro.
				NodoVuelta = gamonal;
				
				//Procesamos las HoraIda y HoraVuelta.
				auxiliar = idaVuelta(sql, node, linea, Dia.F, NodoIda, NodoVuelta, HoraVuelta, HoraIda);
				
				//Parte a la proxima parada y cuando llegue, acaba.(no espera).
				llegadaPara(sql, node, linea, Dia.F, auxiliar, centro, gamonal);
				
				//Insertamos la linea.
				sql.insertaRutaLinea(linea);
				
				/*****************************************************************/
				/************FIN PROCESAMOS LOS NOCTURNOS FESTIVOS****************/
				/*****************************************************************/
				
				
				
				
				/*****************************************************************/
				/*************PROCESAMOS LOS LECTIVOS AUTOBUS 6.10****************/
				/*****************************************************************/
				
				System.out.println("3/12 PROCESAMOS LOS LECTIVOS AUTOBUS 6.10");
				
				//ArrayList Hora Ida.
				HoraIda= new ArrayList<Hora>();
				
				HoraIda.add(new Hora(6, 10, 00));
				HoraIda.add(new Hora(6, 45, 00));
				
				//ArrayList Hora Vuelta.
				HoraVuelta= new ArrayList<Hora>();
				
				HoraVuelta.add(new Hora(6, 30, 00));
				
				//Nodos de la parada de las posiciones de ida y de vuelta (paradas).
				//Gamonal.
				NodoIda = centro;
				//Centro.
				NodoVuelta = gamonal;
				
				//Procesamos las HoraIda y HoraVuelta.
				auxiliar = idaVuelta(sql, node, linea, Dia.L, NodoIda, NodoVuelta, HoraVuelta, HoraIda);
				
				//mantenga posicion hasta las 7.00.
				llegadaEspera(sql, node, linea, Dia.L, auxiliar, centro, gamonal, new Hora(7,00,00));
				
				//Insertamos la linea.
				sql.insertaRutaLinea(linea);
				
				/*****************************************************************/
				/************FIN PROCESAMOS LOS LECTIVOS AUTOBUS 6.10*************/
				/*****************************************************************/
				
				/*****************************************************************/
				/***************PROCESAMOS LOS LECTIVOS AUTOBUS1******************/
				/*****************************************************************/
				
				System.out.println("4/12 PROCESAMOS LOS LECTIVOS AUTOBUS1");
				
				//ArrayList Hora Ida.
				HoraIda= new ArrayList<Hora>();
				
				HoraIda.add(new Hora(7, 00, 00));
				HoraIda.add(new Hora(7, 30, 00));
				HoraIda.add(new Hora(8, 00, 00));
				HoraIda.add(new Hora(8, 30, 00));
				HoraIda.add(new Hora(9, 00, 00));
				HoraIda.add(new Hora(9, 30, 00));
				HoraIda.add(new Hora(10, 00, 00));
				HoraIda.add(new Hora(10, 30, 00));
				HoraIda.add(new Hora(11, 00, 00));
				HoraIda.add(new Hora(11, 30, 00));
				HoraIda.add(new Hora(12, 00, 00));
				HoraIda.add(new Hora(12, 30, 00));
				HoraIda.add(new Hora(13, 00, 00));
				HoraIda.add(new Hora(13, 30, 00));
				HoraIda.add(new Hora(14, 00, 00));
				HoraIda.add(new Hora(14, 30, 00));
				HoraIda.add(new Hora(15, 00, 00));
				HoraIda.add(new Hora(15, 30, 00));
				HoraIda.add(new Hora(16, 00, 00));
				HoraIda.add(new Hora(16, 30, 00));
				HoraIda.add(new Hora(17, 00, 00));
				HoraIda.add(new Hora(17, 30, 00));
				HoraIda.add(new Hora(18, 00, 00));
				HoraIda.add(new Hora(18, 30, 00));
				HoraIda.add(new Hora(19, 00, 00));
				HoraIda.add(new Hora(19, 30, 00));
				HoraIda.add(new Hora(20, 00, 00));
				HoraIda.add(new Hora(20, 30, 00));
				HoraIda.add(new Hora(21, 00, 00));
				HoraIda.add(new Hora(21, 30, 00));
				HoraIda.add(new Hora(22, 00, 00));
				HoraIda.add(new Hora(22, 30, 00));
				HoraIda.add(new Hora(23, 00, 00));
				
				//ArrayList Hora Vuelta.
				HoraVuelta= new ArrayList<Hora>();
				
				HoraVuelta.add(new Hora(7, 15, 00));
				HoraVuelta.add(new Hora(7, 45, 00));
				HoraVuelta.add(new Hora(8, 15, 00));
				HoraVuelta.add(new Hora(8, 45, 00));
				HoraVuelta.add(new Hora(9, 15, 00));
				HoraVuelta.add(new Hora(9, 45, 00));
				HoraVuelta.add(new Hora(10, 15, 00));
				HoraVuelta.add(new Hora(10, 45, 00));
				HoraVuelta.add(new Hora(11, 15, 00));
				HoraVuelta.add(new Hora(11, 45, 00));
				HoraVuelta.add(new Hora(12, 15, 00));
				HoraVuelta.add(new Hora(12, 45, 00));
				HoraVuelta.add(new Hora(13, 15, 00));
				HoraVuelta.add(new Hora(13, 45, 00));
				HoraVuelta.add(new Hora(14, 15, 00));
				HoraVuelta.add(new Hora(14, 45, 00));
				HoraVuelta.add(new Hora(15, 15, 00));
				HoraVuelta.add(new Hora(15, 45, 00));
				HoraVuelta.add(new Hora(16, 15, 00));
				HoraVuelta.add(new Hora(16, 45, 00));
				HoraVuelta.add(new Hora(17, 15, 00));
				HoraVuelta.add(new Hora(17, 45, 00));
				HoraVuelta.add(new Hora(18, 15, 00));
				HoraVuelta.add(new Hora(18, 45, 00));
				HoraVuelta.add(new Hora(19, 15, 00));
				HoraVuelta.add(new Hora(19, 45, 00));
				HoraVuelta.add(new Hora(20, 15, 00));
				HoraVuelta.add(new Hora(20, 45, 00));
				HoraVuelta.add(new Hora(21, 15, 00));
				HoraVuelta.add(new Hora(21, 45, 00));
				HoraVuelta.add(new Hora(22, 15, 00));
				HoraVuelta.add(new Hora(22, 45, 00));
				//La ultima salida es 23.15 pero hay que pasarselo a traves de llegadaEspera.
				//HoraVuelta.add(new Hora(23, 15, 00));
				
				//Nodos de la parada de las posiciones de ida y de vuelta (paradas).
				//Gamonal.
				NodoIda = gamonal;
				//Centro.
				NodoVuelta = centro;
				
				//Procesamos las HoraIda y HoraVuelta.
				auxiliar = idaVuelta(sql, node, linea, Dia.L, NodoIda, NodoVuelta, HoraVuelta, HoraIda);
				
				//Continuamos hasta la proxima parada, esperando hasta las 23:15.
				auxiliar = llegadaEspera(sql, node, linea, Dia.L, auxiliar, centro, gamonal, new Hora(23, 15, 00));
				
				//Parte a la proxima parada y cuando llegue, acaba.(no espera).
				llegadaPara(sql, node, linea, Dia.L, auxiliar, centro, gamonal);
				
				//Insertamos la linea.
				sql.insertaRutaLinea(linea);
				
				/*****************************************************************/
				/************FIN PROCESAMOS LOS LECTIVOS AUTOBUS1*****************/
				/*****************************************************************/
				
				/*****************************************************************/
				/**************PROCESAMOS LOS LECTIVOS AUTOBUS2*******************/
				/*****************************************************************/
				
				System.out.println("5/12 PROCESAMOS LOS LECTIVOS AUTOBUS2");
		
				//ArrayList Hora Ida.
				HoraIda= new ArrayList<Hora>();
				
				HoraIda.add(new Hora(7, 15, 00));
				HoraIda.add(new Hora(7, 45, 00));
				HoraIda.add(new Hora(8, 15, 00));
				HoraIda.add(new Hora(8, 45, 00));
				HoraIda.add(new Hora(9, 15, 00));
				HoraIda.add(new Hora(9, 45, 00));
				HoraIda.add(new Hora(10, 15, 00));
				HoraIda.add(new Hora(10, 45, 00));
				HoraIda.add(new Hora(11, 15, 00));
				HoraIda.add(new Hora(11, 45, 00));
				HoraIda.add(new Hora(12, 15, 00));
				HoraIda.add(new Hora(12, 45, 00));
				HoraIda.add(new Hora(13, 15, 00));
				HoraIda.add(new Hora(13, 45, 00));
				HoraIda.add(new Hora(14, 15, 00));
				HoraIda.add(new Hora(14, 45, 00));
				HoraIda.add(new Hora(15, 15, 00));
				HoraIda.add(new Hora(15, 45, 00));
				HoraIda.add(new Hora(16, 15, 00));
				HoraIda.add(new Hora(16, 45, 00));
				HoraIda.add(new Hora(17, 15, 00));
				HoraIda.add(new Hora(17, 45, 00));
				HoraIda.add(new Hora(18, 15, 00));
				HoraIda.add(new Hora(18, 45, 00));
				HoraIda.add(new Hora(19, 15, 00));
				HoraIda.add(new Hora(19, 45, 00));
				HoraIda.add(new Hora(20, 15, 00));
				HoraIda.add(new Hora(20, 45, 00));
				HoraIda.add(new Hora(21, 15, 00));
				HoraIda.add(new Hora(21, 45, 00));
				HoraIda.add(new Hora(22, 15, 00));
				HoraIda.add(new Hora(22, 45, 00));
				HoraIda.add(new Hora(23, 15, 00));
				
				//ArrayList Hora Vuelta.
				HoraVuelta= new ArrayList<Hora>();
				
				HoraVuelta.add(new Hora(7, 30, 00));
				HoraVuelta.add(new Hora(8, 00, 00));
				HoraVuelta.add(new Hora(8, 30, 00));
				HoraVuelta.add(new Hora(9, 00, 00));
				HoraVuelta.add(new Hora(9, 30, 00));
				HoraVuelta.add(new Hora(10, 00, 00));
				HoraVuelta.add(new Hora(10, 30, 00));
				HoraVuelta.add(new Hora(11, 00, 00));
				HoraVuelta.add(new Hora(11, 30, 00));
				HoraVuelta.add(new Hora(12, 00, 00));
				HoraVuelta.add(new Hora(12, 30, 00));
				HoraVuelta.add(new Hora(13, 00, 00));
				HoraVuelta.add(new Hora(13, 30, 00));
				HoraVuelta.add(new Hora(14, 00, 00));
				HoraVuelta.add(new Hora(14, 30, 00));
				HoraVuelta.add(new Hora(15, 00, 00));
				HoraVuelta.add(new Hora(15, 30, 00));
				HoraVuelta.add(new Hora(16, 00, 00));
				HoraVuelta.add(new Hora(16, 30, 00));
				HoraVuelta.add(new Hora(17, 00, 00));
				HoraVuelta.add(new Hora(17, 30, 00));
				HoraVuelta.add(new Hora(18, 00, 00));
				HoraVuelta.add(new Hora(18, 30, 00));
				HoraVuelta.add(new Hora(19, 00, 00));
				HoraVuelta.add(new Hora(19, 30, 00));
				HoraVuelta.add(new Hora(20, 00, 00));
				HoraVuelta.add(new Hora(20, 30, 00));
				HoraVuelta.add(new Hora(21, 00, 00));
				HoraVuelta.add(new Hora(21, 30, 00));
				HoraVuelta.add(new Hora(22, 00, 00));
				HoraVuelta.add(new Hora(22, 30, 00));
				HoraVuelta.add(new Hora(23, 00, 00));
				
				//Nodos de la parada de las posiciones de ida y de vuelta (paradas).
				//Gamonal.
				NodoIda = gamonal;
				//Centro.
				NodoVuelta = centro;
				
				//Procesamos las HoraIda y HoraVuelta.
				auxiliar = idaVuelta(sql, node, linea, Dia.L, NodoIda, NodoVuelta, HoraVuelta, HoraIda);
				
				//Parte a la proxima parada y cuando llegue, acaba.(no espera).
				llegadaPara(sql, node, linea, Dia.L, auxiliar, gamonal, centro);
				
				//Insertamos la linea.
				sql.insertaRutaLinea(linea);
				
				/*****************************************************************/
				/*************FIN PROCESAMOS LOS LECTIVOS AUTOBUS2****************/
				/*****************************************************************/
				
				/*****************************************************************/
				/***************PROCESAMOS LOS LECTIVOS AUTOBUS3******************/
				/*****************************************************************/
				
				System.out.println("6/12 PROCESAMOS LOS LECTIVOS AUTOBUS3");
				
				//ArrayList Hora Ida.
				HoraIda= new ArrayList<Hora>();
				
				HoraIda.add(new Hora(7, 22, 30));
				HoraIda.add(new Hora(7, 52, 30));
				HoraIda.add(new Hora(8, 22, 30));
				HoraIda.add(new Hora(8, 52, 30));
				HoraIda.add(new Hora(9, 22, 30));
				HoraIda.add(new Hora(9, 52, 30));
				HoraIda.add(new Hora(10, 22, 30));
				HoraIda.add(new Hora(10, 52, 30));
				HoraIda.add(new Hora(11, 22, 30));
				HoraIda.add(new Hora(11, 52, 30));
				HoraIda.add(new Hora(12, 22, 30));
				HoraIda.add(new Hora(12, 52, 30));
				HoraIda.add(new Hora(13, 22, 30));
				HoraIda.add(new Hora(13, 52, 30));
				HoraIda.add(new Hora(14, 22, 30));
				HoraIda.add(new Hora(14, 52, 30));
				HoraIda.add(new Hora(15, 22, 30));
				HoraIda.add(new Hora(15, 52, 30));
				HoraIda.add(new Hora(16, 22, 30));
				HoraIda.add(new Hora(16, 52, 30));
				HoraIda.add(new Hora(17, 22, 30));
				HoraIda.add(new Hora(17, 52, 30));
				HoraIda.add(new Hora(18, 22, 30));
				HoraIda.add(new Hora(18, 52, 30));
				HoraIda.add(new Hora(19, 22, 30));
				HoraIda.add(new Hora(19, 52, 30));
				HoraIda.add(new Hora(20, 22, 30));
				HoraIda.add(new Hora(20, 52, 30));
				HoraIda.add(new Hora(21, 22, 30));
				HoraIda.add(new Hora(21, 52, 30));
				HoraIda.add(new Hora(22, 22, 30));
				HoraIda.add(new Hora(22, 52, 30));
				
				//ArrayList Hora Vuelta.
				HoraVuelta= new ArrayList<Hora>();
				
				HoraVuelta.add(new Hora(7, 37, 30));
				HoraVuelta.add(new Hora(8, 07, 30));
				HoraVuelta.add(new Hora(8, 37, 30));
				HoraVuelta.add(new Hora(9, 07, 30));
				HoraVuelta.add(new Hora(9, 37, 30));
				HoraVuelta.add(new Hora(10, 07, 30));
				HoraVuelta.add(new Hora(10, 37, 30));
				HoraVuelta.add(new Hora(11, 07, 30));
				HoraVuelta.add(new Hora(11, 37, 30));
				HoraVuelta.add(new Hora(12, 07, 30));
				HoraVuelta.add(new Hora(12, 37, 30));
				HoraVuelta.add(new Hora(13, 07, 30));
				HoraVuelta.add(new Hora(13, 37, 30));
				HoraVuelta.add(new Hora(14, 07, 30));
				HoraVuelta.add(new Hora(14, 37, 30));
				HoraVuelta.add(new Hora(15, 07, 30));
				HoraVuelta.add(new Hora(15, 37, 30));
				HoraVuelta.add(new Hora(16, 07, 30));
				HoraVuelta.add(new Hora(16, 37, 30));
				HoraVuelta.add(new Hora(17, 07, 30));
				HoraVuelta.add(new Hora(17, 37, 30));
				HoraVuelta.add(new Hora(18, 07, 30));
				HoraVuelta.add(new Hora(18, 37, 30));
				HoraVuelta.add(new Hora(19, 07, 30));
				HoraVuelta.add(new Hora(19, 37, 30));
				HoraVuelta.add(new Hora(20, 07, 30));
				HoraVuelta.add(new Hora(20, 37, 30));
				HoraVuelta.add(new Hora(21, 07, 30));
				HoraVuelta.add(new Hora(21, 37, 30));
				HoraVuelta.add(new Hora(22, 07, 30));
				HoraVuelta.add(new Hora(22, 37, 30));
				//La ultima salida es 23.07.30 pero hay que pasarselo a traves de llegadaEspera.
				//HoraVuelta.add(new Hora(23, 07, 30));
					
				//Nodos de la parada de las posiciones de ida y de vuelta (paradas).
				//Gamonal.
				NodoIda = gamonal;
				//Centro.
				NodoVuelta = centro;
				
				//Procesamos las HoraIda y HoraVuelta.
				auxiliar = idaVuelta(sql, node, linea, Dia.L, NodoIda, NodoVuelta, HoraVuelta, HoraIda);
				
				//Continuamos hasta la proxima parada, esperando hasta las 23:07.
				auxiliar = llegadaEspera(sql, node, linea, Dia.L, auxiliar, gamonal, centro, new Hora(23, 07, 30));
				
				//Parte a la proxima parada y cuando llegue, acaba.(no espera).
				llegadaPara(sql, node, linea, Dia.L, auxiliar, centro, gamonal);
				
				//Insertamos la linea.
				sql.insertaRutaLinea(linea);
				
				/*****************************************************************/
				/************FIN PROCESAMOS LOS LECTIVOS AUTOBUS3*****************/
				/*****************************************************************/
				
				/*****************************************************************/
				/**************PROCESAMOS LOS LECTIVOS AUTOBUS4*******************/
				/*****************************************************************/
				
				System.out.println("7/12 PROCESAMOS LOS LECTIVOS AUTOBUS4");
				
				//ArrayList Hora Ida.
				HoraIda= new ArrayList<Hora>();
				
				HoraIda.add(new Hora(7, 22, 30));
				HoraIda.add(new Hora(7, 52, 30));
				HoraIda.add(new Hora(8, 22, 30));
				HoraIda.add(new Hora(8, 52, 30));
				HoraIda.add(new Hora(9, 22, 30));
				HoraIda.add(new Hora(9, 52, 30));
				HoraIda.add(new Hora(10, 22, 30));
				HoraIda.add(new Hora(10, 52, 30));
				HoraIda.add(new Hora(11, 22, 30));
				HoraIda.add(new Hora(11, 52, 30));
				HoraIda.add(new Hora(12, 22, 30));
				HoraIda.add(new Hora(12, 52, 30));
				HoraIda.add(new Hora(13, 22, 30));
				HoraIda.add(new Hora(13, 52, 30));
				HoraIda.add(new Hora(14, 22, 30));
				HoraIda.add(new Hora(14, 52, 30));
				HoraIda.add(new Hora(15, 22, 30));
				HoraIda.add(new Hora(15, 52, 30));
				HoraIda.add(new Hora(16, 22, 30));
				HoraIda.add(new Hora(16, 52, 30));
				HoraIda.add(new Hora(17, 22, 30));
				HoraIda.add(new Hora(17, 52, 30));
				HoraIda.add(new Hora(18, 22, 30));
				HoraIda.add(new Hora(18, 52, 30));
				HoraIda.add(new Hora(19, 22, 30));
				HoraIda.add(new Hora(19, 52, 30));
				HoraIda.add(new Hora(20, 22, 30));
				HoraIda.add(new Hora(20, 52, 30));
				HoraIda.add(new Hora(21, 22, 30));
				HoraIda.add(new Hora(21, 52, 30));
				HoraIda.add(new Hora(22, 22, 30));
				HoraIda.add(new Hora(22, 52, 30));
				HoraIda.add(new Hora(23, 22, 30));
				
				//ArrayList Hora Vuelta.
				HoraVuelta= new ArrayList<Hora>();
				
				HoraVuelta.add(new Hora(7, 37, 30));
				HoraVuelta.add(new Hora(8, 07, 30));
				HoraVuelta.add(new Hora(8, 37, 30));
				HoraVuelta.add(new Hora(9, 07, 30));
				HoraVuelta.add(new Hora(9, 37, 30));
				HoraVuelta.add(new Hora(10, 07, 30));
				HoraVuelta.add(new Hora(10, 37, 30));
				HoraVuelta.add(new Hora(11, 07, 30));
				HoraVuelta.add(new Hora(11, 37, 30));
				HoraVuelta.add(new Hora(12, 07, 30));
				HoraVuelta.add(new Hora(12, 37, 30));
				HoraVuelta.add(new Hora(13, 07, 30));
				HoraVuelta.add(new Hora(13, 37, 30));
				HoraVuelta.add(new Hora(14, 07, 30));
				HoraVuelta.add(new Hora(14, 37, 30));
				HoraVuelta.add(new Hora(15, 07, 30));
				HoraVuelta.add(new Hora(15, 37, 30));
				HoraVuelta.add(new Hora(16, 07, 30));
				HoraVuelta.add(new Hora(16, 37, 30));
				HoraVuelta.add(new Hora(17, 07, 30));
				HoraVuelta.add(new Hora(17, 37, 30));
				HoraVuelta.add(new Hora(18, 07, 30));
				HoraVuelta.add(new Hora(18, 37, 30));
				HoraVuelta.add(new Hora(19, 07, 30));
				HoraVuelta.add(new Hora(19, 37, 30));
				HoraVuelta.add(new Hora(20, 07, 30));
				HoraVuelta.add(new Hora(20, 37, 30));
				HoraVuelta.add(new Hora(21, 07, 30));
				HoraVuelta.add(new Hora(21, 37, 30));
				HoraVuelta.add(new Hora(22, 07, 30));
				HoraVuelta.add(new Hora(22, 37, 30));
				HoraVuelta.add(new Hora(23, 07, 30));
				
				//Nodos de la parada de las posiciones de ida y de vuelta (paradas).
				//Centro.
				NodoIda = centro;
				//Gamonal.
				NodoVuelta = gamonal;
				
				//Procesamos las HoraIda y HoraVuelta.
				auxiliar = idaVuelta(sql, node, linea, Dia.L, NodoIda, NodoVuelta, HoraVuelta, HoraIda);
				
				//Parte a la proxima parada y cuando llegue, acaba.(no espera).
				auxiliar = llegadaPara(sql, node, linea, Dia.L, auxiliar, centro, gamonal);
				
				//Insertamos la linea.
				sql.insertaRutaLinea(linea);
				
				/*****************************************************************/
				/*************FIN PROCESAMOS LOS LECTIVOS AUTOBUS4*****************/
				/*****************************************************************/
				
				
				
				
				/*****************************************************************/
				/*****************PROCESAMOS LOS FESTIVOS 7.30********************/
				/*****************************************************************/
				
				System.out.println("8/12 PROCESAMOS LOS FESTIVOS 7.30");
				
				//ArrayList Hora Ida.
				HoraIda= new ArrayList<Hora>();
				
				HoraIda.add(new Hora(7, 30, 00));
				HoraIda.add(new Hora(8, 00, 00));
				
				//ArrayList Hora Vuelta.
				HoraVuelta= new ArrayList<Hora>();
				
				HoraVuelta.add(new Hora(7, 44, 00));
				//La ultima salida es 8.14 pero hay que pasarselo a traves de llegadaEspera.
				//HoraVuelta.add(new Hora(8, 14, 00));
				
				//Gamonal.
				NodoIda = gamonal;
				//Centro.
				NodoVuelta = centro;
				
				
				//Procesamos las HoraIda y HoraVuelta.
				auxiliar = idaVuelta(sql, node, linea, Dia.F, NodoIda, NodoVuelta, HoraVuelta, HoraIda);
				
				//Continuamos hasta la proxima parada, esperando hasta las 8:14.
				llegadaEspera(sql, node, linea, Dia.F, auxiliar, gamonal, centro, new Hora(8, 14, 00));
				
				//Insertamos la linea.
				sql.insertaRutaLinea(linea);
				
				/*****************************************************************/
				/****************FIN PROCESAMOS LOS FESTIVOS 7.30******************/
				/*****************************************************************/
				
				/*****************************************************************/
				/****************PROCESAMOS LOS FESTIVOS AUTOBUS1*****************/
				/*****************************************************************/
				
				System.out.println("9/12 PROCESAMOS LOS FESTIVOS AUTOBUS1");
				//ArrayList Hora Ida.
				HoraIda= new ArrayList<Hora>();
				
				HoraIda.add(new Hora(8, 14, 00));
				HoraIda.add(new Hora(8, 42, 00));
				HoraIda.add(new Hora(9, 10, 00));
				HoraIda.add(new Hora(9, 38, 00));
				HoraIda.add(new Hora(10, 06, 00));
				HoraIda.add(new Hora(10, 34, 00));
				HoraIda.add(new Hora(11, 02, 00));
				HoraIda.add(new Hora(11, 30, 00));
				HoraIda.add(new Hora(11, 58, 00));
				HoraIda.add(new Hora(12, 26, 00));
				HoraIda.add(new Hora(12, 54, 00));
				HoraIda.add(new Hora(13, 22, 00));
				HoraIda.add(new Hora(13, 50, 00));
				HoraIda.add(new Hora(14, 18, 00));
				HoraIda.add(new Hora(14, 46, 00));
				HoraIda.add(new Hora(15, 14, 00));
				HoraIda.add(new Hora(15, 42, 00));
				HoraIda.add(new Hora(16, 10, 00));
				HoraIda.add(new Hora(16, 38, 00));
				HoraIda.add(new Hora(17, 06, 00));
				HoraIda.add(new Hora(17, 34, 00));
				HoraIda.add(new Hora(18, 02, 00));
				HoraIda.add(new Hora(18, 30, 00));
				HoraIda.add(new Hora(18, 58, 00));
				HoraIda.add(new Hora(19, 26, 00));
				HoraIda.add(new Hora(19, 54, 00));
				HoraIda.add(new Hora(20, 22, 00));
				HoraIda.add(new Hora(20, 50, 00));
				HoraIda.add(new Hora(21, 18, 00));
				HoraIda.add(new Hora(21, 46, 00));
				HoraIda.add(new Hora(22, 14, 00));
				HoraIda.add(new Hora(22, 42, 00));
				HoraIda.add(new Hora(23, 10, 00));
				
				//ArrayList Hora Vuelta.
				HoraVuelta= new ArrayList<Hora>();
				
				HoraVuelta.add(new Hora(8, 28, 00));
				HoraVuelta.add(new Hora(8, 56, 00));
				HoraVuelta.add(new Hora(9, 24, 00));
				HoraVuelta.add(new Hora(9, 52, 00));
				HoraVuelta.add(new Hora(10, 20, 00));
				HoraVuelta.add(new Hora(10, 48, 00));
				HoraVuelta.add(new Hora(11, 16, 00));
				HoraVuelta.add(new Hora(11, 44, 00));
				HoraVuelta.add(new Hora(12, 12, 00));
				HoraVuelta.add(new Hora(12, 40, 00));
				HoraVuelta.add(new Hora(13, 8, 00));
				HoraVuelta.add(new Hora(13, 36, 00));
				HoraVuelta.add(new Hora(14, 04, 00));
				HoraVuelta.add(new Hora(14, 32, 00));
				HoraVuelta.add(new Hora(15, 00, 00));
				HoraVuelta.add(new Hora(15, 28, 00));
				HoraVuelta.add(new Hora(15, 56, 00));
				HoraVuelta.add(new Hora(16, 24, 00));
				HoraVuelta.add(new Hora(16, 52, 00));
				HoraVuelta.add(new Hora(17, 20, 00));
				HoraVuelta.add(new Hora(17, 48, 00));
				HoraVuelta.add(new Hora(18, 16, 00));
				HoraVuelta.add(new Hora(18, 44, 00));
				HoraVuelta.add(new Hora(19, 12, 00));
				HoraVuelta.add(new Hora(19, 40, 00));
				HoraVuelta.add(new Hora(20, 8, 00));
				HoraVuelta.add(new Hora(20, 36, 00));
				HoraVuelta.add(new Hora(21, 04, 00));
				HoraVuelta.add(new Hora(21, 32, 00));
				HoraVuelta.add(new Hora(22, 00, 00));
				HoraVuelta.add(new Hora(22, 28, 00));
				HoraVuelta.add(new Hora(22, 56, 00));
				//La ultima salida es 23.24 pero hay que pasarselo a traves de llegadaEspera.
				//HoraVuelta.add(new Hora(23, 24, 00));
				
				//Nodos de la parada de las posiciones de ida y de vuelta (paradas).
				//Centro.
				NodoIda = centro;
				//Gamonal.
				NodoVuelta = gamonal;
				
				//Procesamos las HoraIda y HoraVuelta.
				auxiliar = idaVuelta(sql, node, linea, Dia.F, NodoIda, NodoVuelta, HoraVuelta, HoraIda);
				
				//Continuamos hasta la proxima parada, esperando hasta las 23:24.
				auxiliar = llegadaEspera(sql, node, linea, Dia.F, auxiliar, gamonal, centro, new Hora(23, 24, 00));
				//System.out.println("*******************LLEGADA 2*********************");
				
				//Parte a la proxima parada y cuando llegue, acaba.(no espera).
				llegadaPara(sql, node, linea, Dia.F, auxiliar, gamonal, centro);
				
				//Insertamos la linea.
				sql.insertaRutaLinea(linea);
				
				/*****************************************************************/
				/**************FIN PROCESAMOS LOS FESTIVOS AUTOBUS1***************/
				/*****************************************************************/
				
				/*****************************************************************/
				/****************PROCESAMOS LOS FESTIVOS AUTOBUS2*****************/
				/*****************************************************************/
				
				System.out.println("10/12 PROCESAMOS LOS FESTIVOS AUTOBUS2");
				
				//ArrayList Hora Ida.
				HoraIda= new ArrayList<Hora>();
				
				HoraIda.add(new Hora(8, 14, 00));
				HoraIda.add(new Hora(8, 42, 00));
				HoraIda.add(new Hora(9, 10, 00));
				HoraIda.add(new Hora(9, 38, 00));
				HoraIda.add(new Hora(10, 06, 00));
				HoraIda.add(new Hora(10, 34, 00));
				HoraIda.add(new Hora(11, 02, 00));
				HoraIda.add(new Hora(11, 30, 00));
				HoraIda.add(new Hora(11, 58, 00));
				HoraIda.add(new Hora(12, 26, 00));
				HoraIda.add(new Hora(12, 54, 00));
				HoraIda.add(new Hora(13, 22, 00));
				HoraIda.add(new Hora(13, 50, 00));
				HoraIda.add(new Hora(14, 18, 00));
				HoraIda.add(new Hora(14, 46, 00));
				HoraIda.add(new Hora(15, 14, 00));
				HoraIda.add(new Hora(15, 42, 00));
				HoraIda.add(new Hora(16, 10, 00));
				HoraIda.add(new Hora(16, 38, 00));
				HoraIda.add(new Hora(17, 06, 00));
				HoraIda.add(new Hora(17, 34, 00));
				HoraIda.add(new Hora(18, 02, 00));
				HoraIda.add(new Hora(18, 30, 00));
				HoraIda.add(new Hora(18, 58, 00));
				HoraIda.add(new Hora(19, 26, 00));
				HoraIda.add(new Hora(19, 54, 00));
				HoraIda.add(new Hora(20, 22, 00));
				HoraIda.add(new Hora(20, 50, 00));
				HoraIda.add(new Hora(21, 18, 00));
				HoraIda.add(new Hora(21, 46, 00));
				HoraIda.add(new Hora(22, 14, 00));
				HoraIda.add(new Hora(22, 42, 00));
				HoraIda.add(new Hora(23, 10, 00));
				
				//ArrayList Hora Vuelta.
				HoraVuelta= new ArrayList<Hora>();
				
				HoraVuelta.add(new Hora(8, 28, 00));
				HoraVuelta.add(new Hora(8, 56, 00));
				HoraVuelta.add(new Hora(9, 24, 00));
				HoraVuelta.add(new Hora(9, 52, 00));
				HoraVuelta.add(new Hora(10, 20, 00));
				HoraVuelta.add(new Hora(10, 48, 00));
				HoraVuelta.add(new Hora(11, 16, 00));
				HoraVuelta.add(new Hora(11, 44, 00));
				HoraVuelta.add(new Hora(12, 12, 00));
				HoraVuelta.add(new Hora(12, 40, 00));
				HoraVuelta.add(new Hora(13, 8, 00));
				HoraVuelta.add(new Hora(13, 36, 00));
				HoraVuelta.add(new Hora(14, 04, 00));
				HoraVuelta.add(new Hora(14, 32, 00));
				HoraVuelta.add(new Hora(15, 00, 00));
				HoraVuelta.add(new Hora(15, 28, 00));
				HoraVuelta.add(new Hora(15, 56, 00));
				HoraVuelta.add(new Hora(16, 24, 00));
				HoraVuelta.add(new Hora(16, 52, 00));
				HoraVuelta.add(new Hora(17, 20, 00));
				HoraVuelta.add(new Hora(17, 48, 00));
				HoraVuelta.add(new Hora(18, 16, 00));
				HoraVuelta.add(new Hora(18, 44, 00));
				HoraVuelta.add(new Hora(19, 12, 00));
				HoraVuelta.add(new Hora(19, 40, 00));
				HoraVuelta.add(new Hora(20, 8, 00));
				HoraVuelta.add(new Hora(20, 36, 00));
				HoraVuelta.add(new Hora(21, 04, 00));
				HoraVuelta.add(new Hora(21, 32, 00));
				HoraVuelta.add(new Hora(22, 00, 00));
				HoraVuelta.add(new Hora(22, 28, 00));
				HoraVuelta.add(new Hora(22, 56, 00));
				//La ultima salida es 23.24 pero hay que pasarselo a traves de llegadaEspera.
				//HoraVuelta.add(new Hora(23, 24, 00));
				
				//Nodos de la parada de las posiciones de ida y de vuelta (paradas).
				//Gamonal.
				NodoIda = gamonal;
				//Centro.
				NodoVuelta = centro;
				
				//Procesamos las HoraIda y HoraVuelta.
				auxiliar = idaVuelta(sql, node, linea, Dia.F, NodoIda, NodoVuelta, HoraVuelta, HoraIda);
				
				//Continuamos hasta la proxima parada, esperando hasta las 23:24.
				auxiliar = llegadaEspera(sql, node, linea, Dia.F, auxiliar, centro, gamonal, new Hora(23, 24, 00));
				//System.out.println("*******************LLEGADA 2*********************");
				
				//Parte a la proxima parada y cuando llegue, acaba.(no espera).
				llegadaPara(sql, node, linea, Dia.F, auxiliar, centro, gamonal);
				
				//Insertamos la linea.
				sql.insertaRutaLinea(linea);
				
				/*****************************************************************/
				/**************FIN PROCESAMOS LOS FESTIVOS AUTOBUS2***************/
				/*****************************************************************/
				
				/*****************************************************************/
				/************PROCESAMOS LOS FESTIVOS AUTOBUS3 11.00***************/
				/*****************************************************************/
				
				//Empieza a partir de las 11:09:30, 7.5 minutos despues de la ultima salida del 1 y 2.
				System.out.println("11/12 PROCESAMOS LOS FESTIVOS AUTOBUS3 11.00");
				
				//ArrayList Hora Ida.
				HoraIda= new ArrayList<Hora>();
				
				HoraIda.add(new Hora(11, 9, 30));
				HoraIda.add(new Hora(11, 37, 30));
				HoraIda.add(new Hora(12, 05, 30));
				HoraIda.add(new Hora(12, 33, 30));
				HoraIda.add(new Hora(13, 01, 30));
				HoraIda.add(new Hora(13, 29, 30));
				HoraIda.add(new Hora(13, 57, 30));
				HoraIda.add(new Hora(14, 25, 30));
				HoraIda.add(new Hora(14, 53, 30));
				HoraIda.add(new Hora(15, 21, 30));
				HoraIda.add(new Hora(15, 49, 30));
				HoraIda.add(new Hora(16, 17, 30));
				HoraIda.add(new Hora(16, 45, 30));
				HoraIda.add(new Hora(17, 13, 30));
				HoraIda.add(new Hora(17, 41, 30));
				HoraIda.add(new Hora(18, 9, 30));
				HoraIda.add(new Hora(18, 37, 30));
				HoraIda.add(new Hora(19, 05, 30));
				HoraIda.add(new Hora(19, 33, 30));
				HoraIda.add(new Hora(20, 01, 30));
				HoraIda.add(new Hora(20, 29, 30));
				HoraIda.add(new Hora(20, 57, 30));
				HoraIda.add(new Hora(21, 25, 30));
				HoraIda.add(new Hora(21, 53, 30));
				HoraIda.add(new Hora(22, 21, 30));
				HoraIda.add(new Hora(22, 49, 30));
				HoraIda.add(new Hora(23, 17, 30));
				
				
				//ArrayList Hora Vuelta.
				HoraVuelta= new ArrayList<Hora>();
				
				HoraVuelta.add(new Hora(11, 23, 30));
				HoraVuelta.add(new Hora(11, 51, 30));
				HoraVuelta.add(new Hora(12, 19, 30));
				HoraVuelta.add(new Hora(12, 47, 30));
				HoraVuelta.add(new Hora(13, 15, 30));
				HoraVuelta.add(new Hora(13, 43, 30));
				HoraVuelta.add(new Hora(14, 11, 30));
				HoraVuelta.add(new Hora(14, 39, 30));
				HoraVuelta.add(new Hora(15, 07, 30));
				HoraVuelta.add(new Hora(15, 35, 30));
				HoraVuelta.add(new Hora(16, 03, 30));
				HoraVuelta.add(new Hora(16, 31, 30));
				HoraVuelta.add(new Hora(16, 59, 30));
				HoraVuelta.add(new Hora(17, 27, 30));
				HoraVuelta.add(new Hora(17, 55, 30));
				HoraVuelta.add(new Hora(18, 23, 30));
				HoraVuelta.add(new Hora(18, 51, 30));
				HoraVuelta.add(new Hora(19, 19, 30));
				HoraVuelta.add(new Hora(19, 47, 30));
				HoraVuelta.add(new Hora(20, 15, 30));
				HoraVuelta.add(new Hora(20, 43, 30));
				HoraVuelta.add(new Hora(21, 11, 30));
				HoraVuelta.add(new Hora(21, 39, 30));
				HoraVuelta.add(new Hora(22, 07, 30));
				HoraVuelta.add(new Hora(22, 35, 30));
				HoraVuelta.add(new Hora(23, 03, 30));
				//La ultima salida es 23.00.
				
				//Nodos de la parada de las posiciones de ida y de vuelta (paradas).
				//Gamonal.
				NodoIda = gamonal;
				//Centro.
				NodoVuelta = centro;
				
				//Procesamos las HoraIda y HoraVuelta.
				auxiliar = idaVuelta(sql, node, linea, Dia.F, NodoIda, NodoVuelta, HoraVuelta, HoraIda);
				
				//Parte a la proxima parada y cuando llegue, acaba.(no espera).
				llegadaPara(sql, node, linea, Dia.F, auxiliar, centro, gamonal);
				
				//Insertamos la linea.
				sql.insertaRutaLinea(linea);
						
				/*****************************************************************/
				/***********FIN PROCESAMOS LOS FESTIVOS AUTOBUS3 11.00************/
				/*****************************************************************/
				
				/*****************************************************************/
				/*************PROCESAMOS LOS FESTIVOS AUTOBUS4 11.00**************/
				/*****************************************************************/
				
				//Empieza a partir de las 11:09:30, 7.5 minutos despues de la ultima salida del 1 y 2.
				System.out.println("12/12 PROCESAMOS LOS FESTIVOS AUTOBUS4 11.00");
				
				//ArrayList Hora Ida.
				HoraIda= new ArrayList<Hora>();
				
				HoraIda.add(new Hora(11, 9, 30));
				HoraIda.add(new Hora(11, 37, 30));
				HoraIda.add(new Hora(12, 05, 30));
				HoraIda.add(new Hora(12, 33, 30));
				HoraIda.add(new Hora(13, 01, 30));
				HoraIda.add(new Hora(13, 29, 30));
				HoraIda.add(new Hora(13, 57, 30));
				HoraIda.add(new Hora(14, 25, 30));
				HoraIda.add(new Hora(14, 53, 30));
				HoraIda.add(new Hora(15, 21, 30));
				HoraIda.add(new Hora(15, 49, 30));
				HoraIda.add(new Hora(16, 17, 30));
				HoraIda.add(new Hora(16, 45, 30));
				HoraIda.add(new Hora(17, 13, 30));
				HoraIda.add(new Hora(17, 41, 30));
				HoraIda.add(new Hora(18, 9, 30));
				HoraIda.add(new Hora(18, 37, 30));
				HoraIda.add(new Hora(19, 05, 30));
				HoraIda.add(new Hora(19, 33, 30));
				HoraIda.add(new Hora(20, 01, 30));
				HoraIda.add(new Hora(20, 29, 30));
				HoraIda.add(new Hora(20, 57, 30));
				HoraIda.add(new Hora(21, 25, 30));
				HoraIda.add(new Hora(21, 53, 30));
				HoraIda.add(new Hora(22, 21, 30));
				HoraIda.add(new Hora(22, 49, 30));
				HoraIda.add(new Hora(23, 17, 30));
				
				
				//ArrayList Hora Vuelta.
				HoraVuelta= new ArrayList<Hora>();
				
				HoraVuelta.add(new Hora(11, 23, 30));
				HoraVuelta.add(new Hora(11, 51, 30));
				HoraVuelta.add(new Hora(12, 19, 30));
				HoraVuelta.add(new Hora(12, 47, 30));
				HoraVuelta.add(new Hora(13, 15, 30));
				HoraVuelta.add(new Hora(13, 43, 30));
				HoraVuelta.add(new Hora(14, 11, 30));
				HoraVuelta.add(new Hora(14, 39, 30));
				HoraVuelta.add(new Hora(15, 07, 30));
				HoraVuelta.add(new Hora(15, 35, 30));
				HoraVuelta.add(new Hora(16, 03, 30));
				HoraVuelta.add(new Hora(16, 31, 30));
				HoraVuelta.add(new Hora(16, 59, 30));
				HoraVuelta.add(new Hora(17, 27, 30));
				HoraVuelta.add(new Hora(17, 55, 30));
				HoraVuelta.add(new Hora(18, 23, 30));
				HoraVuelta.add(new Hora(18, 51, 30));
				HoraVuelta.add(new Hora(19, 19, 30));
				HoraVuelta.add(new Hora(19, 47, 30));
				HoraVuelta.add(new Hora(20, 15, 30));
				HoraVuelta.add(new Hora(20, 43, 30));
				HoraVuelta.add(new Hora(21, 11, 30));
				HoraVuelta.add(new Hora(21, 39, 30));
				HoraVuelta.add(new Hora(22, 07, 30));
				HoraVuelta.add(new Hora(22, 35, 30));
				HoraVuelta.add(new Hora(23, 03, 30));
				//La ultima salida es 23.00.
				
				//Nodos de la parada de las posiciones de ida y de vuelta (paradas).
				//Centro.
				NodoIda = centro;
				//Gamonal.
				NodoVuelta = gamonal;
				
				//Procesamos las HoraIda y HoraVuelta.
				auxiliar = idaVuelta(sql, node, linea, Dia.F, NodoIda, NodoVuelta, HoraVuelta, HoraIda);
				
				//Parte a la proxima parada y cuando llegue, acaba.(no espera).
				llegadaPara(sql, node, linea, Dia.F, auxiliar, gamonal, centro);
				
				//Insertamos la linea.
				sql.insertaRutaLinea(linea);
				
				/*****************************************************************/
				/***********FIN PROCESAMOS LOS FESTIVOS AUTOBUS4 11.00************/
				/*****************************************************************/
				
				
				System.out.println("**********PROCESADA LINEA " + linea + "***********");
			
			}
			
			}catch (SQLException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
}
