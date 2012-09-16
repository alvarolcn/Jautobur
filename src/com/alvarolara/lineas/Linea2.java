package com.alvarolara.lineas;

import java.sql.SQLException;
import java.util.ArrayList;

import com.alvarolara.Dia;
import com.alvarolara.Hora;
import com.alvarolara.MySQL;
import com.alvarolara.OsmNode;
import com.alvarolara.OsmXmlWriter;
import com.alvarolara.Parseador;

public class Linea2 extends Linea{
	
	/**
	 * Nodos de la linea.
	 */
	private ArrayList<OsmNode> node;
	
	/**
	 * contructor Linea1.
	 */
	public Linea2(){
		linea = "1";
	}
	
	/**
	 * Devuelve la lista de nodes.
	 * @return
	 */
	public ArrayList<OsmNode> daveuelveOSMNode(){
		return node;
	}
	
	public Linea2(String archivoDestino, MySQL sql, String nombre, double velocidadMedia, boolean activo) throws Exception{
		
		try{
			
			//asignar la linea que es.
			this.linea = "2";
			
			
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
				
				
				/*****************************************************************/
				/***************************PARADAS*******************************/
				/*****************************************************************/
				
				OsmNode carreteraArcos = new OsmNode(-99999999, null, "true", 42.32763487581272, -3.7120727660866515);
				OsmNode polideportivoTalamillo = new OsmNode(-99999999, null, "true", 42.36017944911222, -3.6841857393109567);
				
				/*****************************************************************/
				/************************FIN PARADAS******************************/
				/*****************************************************************/
				
				
				
				/*****************************************************************/
				/*********************PROCESAMOS LAS IDA**************************/
				/*****************************************************************/
				
				System.out.println("1/2 PROCESAMOS LAS IDA");
					
				//ArrayList Hora Ida.
				HoraIda= new ArrayList<Hora>();
				
				HoraIda.add(new Hora(7, 30, 00));
				HoraIda.add(new Hora(8, 30, 00));
				HoraIda.add(new Hora(9, 30, 00));
				HoraIda.add(new Hora(10, 30, 00));
				HoraIda.add(new Hora(11, 30, 00));
				HoraIda.add(new Hora(12, 30, 00));
				HoraIda.add(new Hora(13, 30, 00));
				HoraIda.add(new Hora(14, 30, 00));
				HoraIda.add(new Hora(15, 30, 00));
				HoraIda.add(new Hora(16, 30, 00));
				HoraIda.add(new Hora(17, 30, 00));
				HoraIda.add(new Hora(18, 30, 00));
				HoraIda.add(new Hora(19, 30, 00));
				HoraIda.add(new Hora(20, 30, 00));
				HoraIda.add(new Hora(21, 30, 00));
				HoraIda.add(new Hora(22, 30, 00));
				
				//ArrayList Hora Vuelta.
				HoraVuelta= new ArrayList<Hora>();
				
				HoraVuelta.add(new Hora(8, 00, 00));
				HoraVuelta.add(new Hora(9, 00, 00));
				HoraVuelta.add(new Hora(10, 00, 00));
				HoraVuelta.add(new Hora(11, 00, 00));
				HoraVuelta.add(new Hora(12, 00, 00));
				HoraVuelta.add(new Hora(13, 00, 00));
				HoraVuelta.add(new Hora(14, 00, 00));
				HoraVuelta.add(new Hora(15, 00, 00));
				HoraVuelta.add(new Hora(16, 00, 00));
				HoraVuelta.add(new Hora(17, 00, 00));
				HoraVuelta.add(new Hora(18, 00, 00));
				HoraVuelta.add(new Hora(19, 00, 00));
				HoraVuelta.add(new Hora(20, 00, 00));
				HoraVuelta.add(new Hora(21, 00, 00));
				HoraVuelta.add(new Hora(22, 00, 00));
				
				
				//Nodos de la parada de las posiciones de ida y de vuelta (paradas).
				//Carretera de Arcos.
				NodoIda = carreteraArcos;
				//Barriada Illera.
				NodoVuelta = polideportivoTalamillo;
				
				auxiliar = idaVuelta(sql, node, linea, Dia.L, NodoIda, NodoVuelta, HoraVuelta, HoraIda);
				
				System.out.println("****************LLEGAPARADA******************");
				//continuamos hasta la proxima parada.
				llegadaPara(sql, node, linea, Dia.L, auxiliar, polideportivoTalamillo, carreteraArcos);
				
				/*****************************************************************/
				/*******************FIN PROCESAMOS LAS IDA************************/
				/*****************************************************************/
				
				
				
				/*****************************************************************/
				/*******************PROCESAMOS LAS VUELTAS************************/
				/*****************************************************************/
				
				System.out.println("2/2 PROCESAMOS LAS VUELTA");
		
				//ArrayList Hora Ida.
				HoraIda= new ArrayList<Hora>();
				
				HoraIda.add(new Hora(7, 30, 00));
				HoraIda.add(new Hora(8, 30, 00));
				HoraIda.add(new Hora(9, 30, 00));
				HoraIda.add(new Hora(10, 30, 00));
				HoraIda.add(new Hora(11, 30, 00));
				HoraIda.add(new Hora(12, 30, 00));
				HoraIda.add(new Hora(13, 30, 00));
				HoraIda.add(new Hora(14, 30, 00));
				HoraIda.add(new Hora(15, 30, 00));
				HoraIda.add(new Hora(16, 30, 00));
				HoraIda.add(new Hora(17, 30, 00));
				HoraIda.add(new Hora(18, 30, 00));
				HoraIda.add(new Hora(19, 30, 00));
				HoraIda.add(new Hora(20, 30, 00));
				HoraIda.add(new Hora(21, 30, 00));
				HoraIda.add(new Hora(22, 30, 00));
				
				//ArrayList Hora Vuelta.
				HoraVuelta= new ArrayList<Hora>();
				
				HoraVuelta.add(new Hora(8, 00, 00));
				HoraVuelta.add(new Hora(9, 00, 00));
				HoraVuelta.add(new Hora(10, 00, 00));
				HoraVuelta.add(new Hora(11, 00, 00));
				HoraVuelta.add(new Hora(12, 00, 00));
				HoraVuelta.add(new Hora(13, 00, 00));
				HoraVuelta.add(new Hora(14, 00, 00));
				HoraVuelta.add(new Hora(15, 00, 00));
				HoraVuelta.add(new Hora(16, 00, 00));
				HoraVuelta.add(new Hora(17, 00, 00));
				HoraVuelta.add(new Hora(18, 00, 00));
				HoraVuelta.add(new Hora(19, 00, 00));
				HoraVuelta.add(new Hora(20, 00, 00));
				HoraVuelta.add(new Hora(21, 00, 00));
				HoraVuelta.add(new Hora(22, 00, 00));
				
				
				//Nodos de la parada de las posiciones de ida y de vuelta (paradas).
				//Barriada Illera.
				NodoIda = polideportivoTalamillo;
				//Carretera de Arcos.
				NodoVuelta = carreteraArcos;
				
				auxiliar = idaVuelta(sql, node, linea, Dia.L, NodoIda, NodoVuelta, HoraVuelta, HoraIda);
				
				//continuamos hasta la proxima parada.
				llegadaPara(sql, node, linea, Dia.L, auxiliar, carreteraArcos, polideportivoTalamillo);
				
				
				
				/*****************************************************************/
				/*****************FIN PROCESAMOS LAS VUELTAS**********************/
				/*****************************************************************/
			}
		
		}catch (SQLException e) {
			// TODO: handle exception
		}
	}
	
	
}
