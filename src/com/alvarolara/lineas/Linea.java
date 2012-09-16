package com.alvarolara.lineas;

import java.sql.SQLException;
import java.util.ArrayList;

import com.alvarolara.Dia;
import com.alvarolara.Hora;
import com.alvarolara.MySQL;
import com.alvarolara.OsmNode;

public abstract class Linea {
	
	/**
	 * Numero de linea.
	 */
	protected String linea;
	
	/**
	 * 
	 * @return
	 */
	public String getLinea() {
		return linea;
	}


	/**
	 * 
	 * @param linea
	 */
	public void setLinea(String linea) {
		this.linea = linea;
	}


	/**
	 * Funcion que para saber si estamos en el ultimo elemento de un ArrayList.
	 * @param hora
	 * @param indice
	 * @return
	 */
	protected boolean esUltimoElemento(ArrayList<Hora> hora, int indice){
		if (((hora.size()-1)-indice)==0){
			return true;
		}
		return false;
	}
	
	/**
	 * Funcion que busca la posicion del nodo inicio, para empezar a crear la ruta.
	 * @param node
	 * @param inicio
	 * @return
	 */
	protected int encuentraInicio(ArrayList<OsmNode> node, OsmNode inicio){
		for(int i=0;i<node.size()-1;i++){
			if(node.get(i).comparaNodo(inicio)){
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Va desde nodo Origen a destino y cuando llega, se detiene y devuelve la hora en la que se ha quedado.
	 * @param sql
	 * @param node
	 * @param linea2
	 * @param dia
	 * @param hora
	 * @param origen
	 * @param destino
	 * @return
	 * @throws SQLException
	 */
	protected Hora llegadaPara(MySQL sql, ArrayList<OsmNode> node, String linea2, Dia dia, Hora hora, OsmNode origen, OsmNode destino) throws SQLException{
		
		//hora.decrementaTiempo();
		hora.incrementaTiempo();
		int i = encuentraInicio(node, origen)+1;
		boolean salir = true;
		
		while(salir){
			if(node.size() == i){
				i=0;
			}
			sql.anadeRutaLinea(linea2, dia, hora.toString(), node.get(i).getLat(), node.get(i).getLon());
			
			if(destino.comparaNodo(node.get(i))){
				salir=false;
				break;
			}
			hora.incrementaTiempo();
			i++;
		}
		return hora;
	}
	
	/**
	 * Va desde nodo Origen a destino y se detiene y cuando llega a hora hasta.
	 * @param sql
	 * @param node
	 * @param linea2
	 * @param dia
	 * @param hora
	 * @param origen
	 * @param destino
	 * @param hasta
	 * @return
	 * @throws SQLException
	 */
	protected Hora llegadaEspera(MySQL sql, ArrayList<OsmNode> node, String linea2, Dia dia, Hora hora, OsmNode origen, OsmNode destino, Hora hasta) throws SQLException{
		
		//hora.decrementaTiempo();
		hora.incrementaTiempo();
		int i = encuentraInicio(node, origen)+1;
		boolean salir = true;
		
		while(salir){
			if(node.size() == i){
				i=0;
			}
			sql.anadeRutaLinea(linea2, dia, hora.toString(), node.get(i).getLat(), node.get(i).getLon());
			
			if(destino.comparaNodo(node.get(i))){
				while(true){
					hora.incrementaTiempo();
					if(hora.comparaHora(hasta)){
						sql.anadeRutaLinea(linea2, dia, hora.toString(), destino.getLat(), destino.getLon());
						return hora;
					}else{
						sql.anadeRutaLinea(linea2, dia, hora.toString(), destino.getLat(), destino.getLon());
					}
				}
			}
			hora.incrementaTiempo();
			i++;
		}
		return hora;
	}
	
	/**
	 * Funcion que inserta los puntos gps cuando hay un ArrayList de ida y otro de vuelta. Sale de Ida y Llega a Vuelta.
	 * @param linea2
	 * @param sql
	 * @param node
	 * @param NodoIda
	 * @param NodoVuelta
	 * @param HoraVuelta
	 * @param HoraIda
	 * @throws SQLException
	 */
	protected Hora idaVuelta(MySQL sql, ArrayList<OsmNode> node, String linea2, Dia dia, OsmNode NodoIda, OsmNode NodoVuelta, ArrayList<Hora> HoraVuelta, ArrayList<Hora> HoraIda) throws SQLException{
		
		int i = encuentraInicio(node, NodoIda);
		
		boolean salir = true;
		int indiceHora = 0;
		OsmNode nodoCompara = new OsmNode(-99999999);
		nodoCompara = NodoVuelta;
		Hora horaCompara = HoraVuelta.get(0);
		Hora hora = HoraIda.get(0).incrementaTiempo();
		
		while(salir){
			//repetir bucle hasta fin de horario de ida o de vuelta.
			
			while(node.size() != i){
				//incrementamos la hora
				hora.incrementaTiempo();
				
				/*if(hora.getHora()==14 && hora.getMinuto()==03 && hora.getSegundo()==11){
					System.out.println("hora");
				}*/
				//Si el nodo actual coincide con el vuelta (ha llegado parada). nodocompara (parada actual), nos paramos hasta que la hora sea la adecuada.
				if(nodoCompara.comparaNodo(node.get(i))){
					
					//si las horas son distintas, mantenemos la posicion de la parada NodoVuelta. Sino, salimos y usamos la siguiente posicion.
					if(horaCompara.comparaHora(hora)){
						
						//Insertamos el nodo actual incrementando el indice.
						sql.anadeRutaLinea(linea2, dia, hora.toString(), node.get(i).getLat(), node.get(i).getLon());
						i++;
						
						//llamada a finaliza
						if(esUltimoElemento(HoraIda,indiceHora)){
							//salimos de la funcion y devolvemos la hora.
							salir = false;
							break;
						}
						
						//Si el nodocompara es igual que el nodovuelta.
						if(nodoCompara.comparaNodo(NodoVuelta)){
							
							//cambiar la asignacion de nodocompara y copiar la hora.
							nodoCompara = NodoIda;
							indiceHora++;
							horaCompara.copiaHora(HoraIda.get(indiceHora));
							
						}else{
							
							//cambiar la asignacion de nodocompara y copiar la hora.
							nodoCompara = NodoVuelta;
							horaCompara.copiaHora(HoraVuelta.get(indiceHora));
						}
					}else{
						//Insertamos el nodo actual.
						sql.anadeRutaLinea(linea2, dia, hora.toString(), node.get(i).getLat(), node.get(i).getLon());	
					}
				}else{
				//El nodo actual no coincide con ninguno de ninguna parada.
				//Insertamos el nodo actual incrementando el indice.
				sql.anadeRutaLinea(linea2, dia, hora.toString(), node.get(i).getLat(), node.get(i).getLon());
				i++;
				}
			}
			//cuando recorra el array de nodos, empieza por el principio.
			i=0;
		}
		return hora;
	}
	
}
