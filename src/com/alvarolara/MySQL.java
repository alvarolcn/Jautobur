package com.alvarolara;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MySQL{
	private Connection conexion = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	private String cadenaInsertaLinea = "";
	private String cadenaInsertaNodosLinea = "";
	
	
	public MySQL() throws Exception{
		try {
			//Llamada al driver MySQL
			Class.forName("com.mysql.jdbc.Driver");
			
			// Conexión con el servidor remoto.
			conexion = DriverManager.getConnection("jdbc:mysql://1.1.1.1:3306/osmautobur", "root", "root");
			
			//Statement para trabajar.
			statement = conexion.createStatement();
		
		} catch (Exception e) {
			throw e;
		} 
	}
	
	
	/**
	 * Elimina el contenido de las tablas creadas e inicializa los indices.
	 * @throws SQLException
	 */
	public void vaciaTablas() throws SQLException{
		preparedStatement = conexion.prepareStatement("DELETE FROM linea;");
		preparedStatement.executeUpdate();
		preparedStatement = conexion.prepareStatement("DELETE FROM ruta_linea;");
		preparedStatement.executeUpdate();
		preparedStatement = conexion.prepareStatement("ALTER TABLE ruta_linea AUTO_INCREMENT=0;");
		preparedStatement.executeUpdate();
		preparedStatement = conexion.prepareStatement("DELETE FROM nodos_linea;");
		preparedStatement.executeUpdate();
		preparedStatement = conexion.prepareStatement("DELETE FROM parada;");
		preparedStatement.executeUpdate();
		preparedStatement = conexion.prepareStatement("ALTER TABLE parada AUTO_INCREMENT=0;");
		preparedStatement.executeUpdate();
		preparedStatement = conexion.prepareStatement("DELETE FROM parada_linea;");
		preparedStatement.executeUpdate();
	}
	
	
	/**
	 * 
	 * @param linea
	 * @throws SQLException
	 */
	public void vaciaNodosLinea(String linea) throws SQLException{
		System.out.println("Borra nodos_linea linea: "+linea);
		preparedStatement = conexion.prepareStatement("DELETE FROM nodos_linea WHERE id_linea = '" + linea + "';");
		preparedStatement.executeUpdate();
		
	}
	
	
	/**
	 * Muestra por pantalla el resultado de la tabla linea.
	 * @param resultSet
	 * @throws SQLException
	 */
	public void resultadoLinea(String linea) throws SQLException {
		System.out.println("Listado de lineas:");
		resultSet = statement.executeQuery("SELECT * FROM linea WHERE id_linea = " + linea);
		while (resultSet.next()) {
			Integer id_linea = resultSet.getInt("id_linea");
			String nombre = resultSet.getString("nombre");
			Double distancia = resultSet.getDouble("distancia");
			String activa = resultSet.getString("activa");
			System.out.println("id_linea: " + id_linea + "  nombre: " + nombre + "  distancia: " + distancia +"km" + "  activa: " + activa);
			System.out.println("------------------------------------");
		}
	}
	
	
	/**
	 * Muestra por pantalla el resultado de la tabla ruta_linea.
	 * @param linea
	 * @throws SQLException
	 */
	public void resultadoRutaLinea(String linea) throws SQLException {
		System.out.println("Listado de Ruta para la Linea" + linea + ":");
		resultSet = statement.executeQuery("SELECT * FROM ruta_linea WHERE id_linea = "+linea);
		while (resultSet.next()) {
			Integer id_linea = resultSet.getInt("id_linea");
			String dia = resultSet.getString("dia");
			String hora = resultSet.getString("hora");
			Long latitud = resultSet.getLong("latitud");
			Long longitud = resultSet.getLong("longitud");
			System.out.println("id_linea: " + id_linea + "  dia: " + dia + "  hora: " + hora +"km" + "  latitud: " + latitud + " longitud: " + longitud);
			System.out.println("------------------------------------");
		}
	}
	
	
	/**
	 * Funcion que inserta la Parada y sus latitudes y longitudes fuera.
	 * @param nombre
	 * @param latitudfuera
	 * @param longitudfuera
	 * @throws SQLException
	 */
	public int insertaParada(String nombre, double latitudfuera, double longitudfuera) throws SQLException{
		System.out.println("Insertada parada.");
		System.out.println("INSERT INTO  parada (nombre, latitudfuera, longitudfuera) VALUES ('" + nombre + "','" + latitudfuera + "','" + longitudfuera + "');");
		preparedStatement = conexion.prepareStatement("INSERT INTO  parada (nombre, latitudfuera, longitudfuera) VALUES ('" + nombre + "','" + latitudfuera + "','" + longitudfuera + "');");
		preparedStatement.executeUpdate();
		
		//Devolvemos el id_parada asignado.
		resultSet = statement.executeQuery("SELECT * FROM parada WHERE id_parada = (SELECT MAX(id_parada) FROM parada);");
		
		//Siguiente registro.
		resultSet.next();
		
		Integer id_parada = resultSet.getInt("id_parada");
		
		System.out.println("id_parada: " + id_parada );
		return id_parada;
	}
	
	
	/**
	 * Elimina todas las paradas
	 * @param linea
	 * @throws SQLException
	 */
	public void vaciaParadas() throws SQLException{
		System.out.println("Borra todas las paradas.");
		preparedStatement = conexion.prepareStatement("DELETE FROM parada;");
		preparedStatement.executeUpdate();
		preparedStatement = conexion.prepareStatement("ALTER TABLE parada AUTO_INCREMENT=0;");
		preparedStatement.executeUpdate();
	}
	
	
	/**
	 * Elimina una parada determianda por su id_parada.
	 * @param id_parada
	 * @throws SQLException
	 */
	public void vaciaParada(String id_parada) throws SQLException{
		System.out.println("Borra parada " + id_parada);
		preparedStatement = conexion.prepareStatement("DELETE FROM parada WHERE id_parada='" + id_parada + "';");
		preparedStatement.executeUpdate();
		
	}
	
	
	public void insertaParadaLinea(int id_parada, String id_linea, double latitud, double longitud) throws SQLException{
		System.out.println("Insertada parada_linea.");
		System.out.println("INSERT INTO  parada_linea (id_parada, id_linea, latitud, longitud) VALUES ('" + id_parada + "','" + id_linea + "','" + latitud + "','" + longitud + "');");
		preparedStatement = conexion.prepareStatement("INSERT INTO  parada_linea (id_parada, id_linea, latitud, longitud) VALUES ('" + id_parada + "','" + id_linea + "','" + latitud + "','" + longitud + "');");
		preparedStatement.executeUpdate();
	}
	
	/**
	 * Elimina todo de parada_linea.
	 * @throws SQLException
	 */
	public void vaciaParadaLineas() throws SQLException{
		System.out.println("Borra todas las parada_linea.");
		preparedStatement = conexion.prepareStatement("DELETE FROM parada_linea;");
		preparedStatement.executeUpdate();
	}
	
	/**
	 * Funcion que inserta la linea y su longitud.
	 * @param linea
	 * @param nombre
	 * @param distancia
	 * @param activa
	 * @throws SQLException
	 */
	public void insertaLinea(String linea, String nombre, double distancia, double velocidadmedia, String activa) throws SQLException{
		System.out.println("Insertada linea: " + linea);
		preparedStatement = conexion.prepareStatement("INSERT INTO  linea (id_linea,nombre,distancia,velocidadmedia,activa) VALUES('" + linea + "','" + nombre + "','" + distancia + "','" + velocidadmedia + "','" + activa + "');");
		preparedStatement.executeUpdate();
	}
	
	
	/**
	 * 
	 * @param linea
	 * @throws SQLException
	 */
	public void vaciaLinea(String linea) throws SQLException{
		preparedStatement = conexion.prepareStatement("DELETE FROM linea WHERE id_linea = '" + linea + "';");
		preparedStatement.executeUpdate();
	}
	
	
	/**
	 * Inserta una nueva tupla de ruta_linea segun los parametros pasados.
	 * @param linea2
	 * @param dia
	 * @param hora
	 * @param latitud
	 * @param longitud
	 * @throws SQLException
	 */
	public void insertaRutaLinea(String id_linea) throws SQLException{
	
		//Eliminamos el ultimo caracter ',' de la cadena.
		cadenaInsertaLinea = cadenaInsertaLinea.substring(0, cadenaInsertaLinea.length()-1);
		
		//System.out.println("INSERT INTO  ruta_linea (id_linea,dia,hora,latitud,longitud) VALUES " + cadenaInsertaLinea + ";");
		preparedStatement = conexion.prepareStatement("INSERT INTO  ruta_linea (id_linea,dia,hora,latitud,longitud) VALUES " + cadenaInsertaLinea + ";");
		preparedStatement.executeUpdate();
		
		//Vaciamos la cadena.
		cadenaInsertaLinea = "";
	}
	
	
	/**
	 * Inicializa el String para la inserción de líneas.
	 * @param linea
	 */
	public void anadeRutaLinea(String id_linea, Dia dia, String hora, double latitud, double longitud){
		System.out.println("anade l2: " + hora + " latitud: " + latitud + " longitud: " + longitud);
		cadenaInsertaLinea += "('" + id_linea + "','" + dia + "','" + hora + "','" + latitud + "','" + longitud + "'),";
	}
	
	
	/**
	 * 
	 * @param linea
	 * @throws SQLException
	 */
	public void vaciaRutaLinea(String linea) throws SQLException{
		preparedStatement = conexion.prepareStatement("DELETE FROM ruta_linea WHERE id_linea = '" + linea + "';");
		preparedStatement.executeUpdate();
		
	}
	
	
	/**
	 * Inserta el trazado de la linea de principio a fin.
	 * @param id_linea
	 * @param node
	 * @throws SQLException
	 */
	public void insertaNodosLinea(String id_linea, ArrayList<OsmNode> node) throws SQLException{
		int i = 0;
		System.out.println("Guardando Linea " + id_linea);
		while(node.size()-1 != i){
			cadenaInsertaNodosLinea += "('" + id_linea + "','" + node.get(i).getId() + "','" + node.get(i).getLat() + "','" + node.get(i).getLon() + "'),";
			i++;
		}
		
		//Eliminamos el ultimo caracter ',' de la cadena.
		cadenaInsertaNodosLinea = cadenaInsertaNodosLinea.substring(0, cadenaInsertaNodosLinea.length()-1);
		
		/***********DEBUG***********/
		/**GUARDARLO EN UN ARCHIVO*/
		/*
		String sFichero = "fichero.txt";
		File fichero = new File(sFichero);

		if (fichero.exists())
  	          System.out.println("El fichero " + sFichero 
			+ " ya existe");
		else {
			try{
			  BufferedWriter bw = new BufferedWriter(new FileWriter(sFichero));
		   	  
			 bw.write(cadenaInsertaNodosLinea);
			  
			  bw.close();
			} catch (IOException ioe){
				ioe.printStackTrace();
			}
		}*/
		
		preparedStatement = conexion.prepareStatement("INSERT INTO  nodos_linea (id_linea,id_nodo,latitud,longitud) VALUES " + cadenaInsertaNodosLinea + ";");
		preparedStatement.executeUpdate();
		
		//Vaciamos la variable.
		cadenaInsertaNodosLinea = "";
		System.out.println("Linea " + id_linea + " guardada correctamente.");
	}
	
	
	/**
	 * Método que cierra los resulset y las conexiones activas.
	 */
	public void cerrar() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (conexion != null) {
				conexion.close();
			}
		} catch (Exception e) {

		}
	}

}
