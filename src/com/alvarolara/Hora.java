package com.alvarolara;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Hora {
	
	/**
	 * Hora.
	 */
	private int hora;
	
	
	/**
	 * Minuto.
	 */
	private int minuto;
	
	/**
	 * Segundo.
	 */
	private int segundo;
	
	
	/**
	 * Constructor, asigna los valores.
	 * @param hora
	 * @param minuto
	 * @param segundo
	 */
	public Hora(int hora, int minuto, int segundo){
		this.hora=hora;
		this.minuto=minuto;
		this.segundo=segundo;
	}
	
	/**
	 * Copia la hora pasada por parametro.
	 * @param hora
	 */
	public void copiaHora(Hora hora){
		this.hora = hora.hora;
		this.minuto = hora.minuto;
		this.segundo = hora.segundo;
	}
	
	/**
	 * Funcion que incrementa horas, minutos y segundos.
	 * @return
	 */
	public Hora incrementaTiempo(){
		if(this.getSegundo()==59){
			this.setSegundo(00);
			if(this.getMinuto()==59){
				this.setMinuto(00);
				if(this.getHora()==23){
					this.setHora(00);
				}else{
					this.setHora(this.getHora()+1);
				}
			}else{
				this.setMinuto(this.getMinuto()+1);
			}
		}else{
			this.setSegundo(this.getSegundo()+1);
		}
		return this;
	}
	
	/**
	 * Funcion que incrementa horas, minutos y segundos.
	 * @return
	 */
	public Hora decrementaTiempo(){
		if(this.getSegundo()==00){
			this.setSegundo(59);
			if(this.getMinuto()==00){
				this.setMinuto(59);
				if(this.getHora()==00){
					this.setHora(23);
				}else{
					this.setHora(this.getHora()-1);
				}
			}else{
				this.setMinuto(this.getMinuto()-1);
			}
		}else{
			this.setSegundo(this.getSegundo()-1);
		}
		return this;
	}
	
	/**
	 * Funcion que compara si dos horas son iguales.
	 * @param hora1
	 * @param hora2
	 * @return
	 */
	private boolean comparaHoras(Hora hora){
		if(this.getHora()== hora.getHora()){
			if(this.getMinuto()==hora.getMinuto()){
				if(this.getSegundo()==hora.getSegundo()){
					return true;
				}
			}
		}else{
			return false;
		}
		return false;
	}
	
	/**
	 * Compara dos horas. Devuelve 0 si son iguales y -1 si son distintas.
	 * @param hora1
	 * @param hora2
	 * @return
	 */
	public boolean comparaHora(Hora hora1){
		if(hora==hora1.getHora()){
			if(minuto==hora1.getMinuto()){
				if(segundo==hora1.getSegundo()){
					//las horas son iguales.
					return true;
				}
			}
		}
		return false;
	}
	
	
	/**
	 * Devuelve un String con dos ceros si el valor pasado es un 0,
	 * sino, devuelve el entero convertido a string.
	 * @param segundos
	 * @return
	 */
	private String ponCeroSegundos(int segundos){
		if(segundos<10){
			return "0" + Integer.toString(segundos);
		}else{
			return Integer.toString(segundos);
		}
	}
	
	
	/**
	 * Devuelve un String con dos ceros si el valor pasado es un 0,
	 * sino, devuelve el entero convertido a string.
	 * @param minutos
	 * @return
	 */
	public String ponCeroMinutos(int minutos){
		if(minutos<10){
			return "0" + Integer.toString(minutos);
		}else{
			return Integer.toString(minutos);
		}
	}
	
	
	/**
	 * Devuelve un String con dos ceros si el valor pasado es un 0,
	 * sino, devuelve el entero convertido a string.
	 * @param horas
	 * @return
	 */
	public String ponCeroHoras(int horas){
		if(horas<10){
			return "0" + Integer.toString(horas);
		}else{
			return Integer.toString(horas);
		}
	}

	
	
	
	public int getHora() {
		return hora;
	}


	public void setHora(int hora) {
		this.hora = hora;
	}


	public int getMinuto() {
		return minuto;
	}


	public void setMinuto(int minuto) {
		this.minuto = minuto;
	}


	public int getSegundo() {
		return segundo;
	}


	public void setSegundo(int segundo) {
		this.segundo = segundo;
	}


	/**
	 * Devuelve la hora en forma de cadena.(para la BBDD).
	 */
	public String toString(){
		return ponCeroHoras(hora) + ":" + ponCeroMinutos(minuto) + ":" + ponCeroSegundos(segundo);
	}
}
