package com.alvarolara;

public class OsmNode implements OsmObjeto {
	
	/**
	 * Id identificador del objeto.
	 */
	private int id;
	
	/**
	 * Action modify.
	 */
	private String action;
	
	/**
	 * Visible true o false;
	 */
	private String visible;
	
	/**
	 * Latitud del nodo.
	 */
	private double lat;
	
	/**
	 * Longitud del nodo.
	 */
	private double lon;

	/**
	 * Constructor para el tipo Node.
	 * @param id
	 * @param action
	 * @param visible
	 * @param lat
	 * @param lon
	 */
	public OsmNode(int id, String action, String visible, double lat, double lon){

		//System.out.println("--------------------\nnode: id=" + id + ", lat=" + lat + ", lon=" + lon);
			
		this.id = id;
		this.action = action;
		this.visible = visible;
		this.lat = lat;
		this.lon = lon;
	}
	
	/**
	 * Contructor que copia un nodo.
	 * @param origen
	 */
	public OsmNode(OsmNode origen){	
		this.lat = origen.getLat();
		this.lon = origen.getLon();
	}
	
	/**
	 * Contructor con latitud y longitud.
	 * @param lat
	 * @param lon
	 */
	public OsmNode(double lat, double lon){
		this.lat = lat;
		this.lon = lon;
	}
	
	/**
	 * Constructor para el tipo Nd en vez de node.
	 * @param id
	 */
	public OsmNode(int id){

		//System.out.println("--------------------\nnd: ref=" + id);
			
		this.id = id;
	}
	
	/**
	 * Compara un nodo devolviendo true o false.
	 * @param nodo
	 * @return
	 */
	public boolean comparaNodo(OsmNode nodo){
		if(this.lat == nodo.getLat() && this.lon == nodo.getLon()){
			return true;
		}
		return false;
	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getAction() {
		return action;
	}


	public void setAction(String action) {
		this.action = action;
	}


	public String getVisible() {
		return visible;
	}


	public void setVisible(String visible) {
		this.visible = visible;
	}


	public double getLat() {
		return lat;
	}


	public void setLat(double lat) {
		this.lat = lat;
	}


	public double getLon() {
		return lon;
	}


	public void setLon(double lon) {
		this.lon = lon;
	}
	
	public String toString(){
		if(visible == null){
			return "\n\tnd: id=" + id;
		}else{
			return "--------------------\nnode: id=" + id + ", lat=" + lat + ", lon=" + lon;
		}
	}
}
