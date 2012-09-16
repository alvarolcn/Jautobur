package com.alvarolara;

import java.util.ArrayList;

import com.alvarolara.lineas.Linea;
import com.alvarolara.lineas.Linea1;
import com.alvarolara.lineas.Linea2;

public class OSMAutobur {
	
	/**
	 * Main para iniciar el parseado del OSM.
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception{
		
		//Nunca puede llegar a la parada despues de que parta el bus.
		//Velocidades en KM/H !!!.
		//Linea 1 probada a 16,6km/h.
		double velocidadMediaLinea1 = 16.6;
		//Linea 2 probada para el viejo recorrido a: 12km/h.
		double velocidadMediaLinea2 = 15;
		double velocidadMediaLinea3 = 13;
		
		//Procesamiento y subida de los nodos a la BBDD.
		MySQL mysql = new MySQL();
		
		//borrar el contenido de las tablas completamente.
		//mysql.vaciaTablas();
		
		//Eliminamos todas las paradas anteriores.
		mysql.vaciaParadas();
		mysql.vaciaParadaLineas();
		
		
		
		
		Linea1 linea1 = new Linea1("l1", mysql, "Avda. Arlanz&oacute;n - Gamonal", velocidadMediaLinea1, false);
		ArrayList<OsmNode> nosmNodeLinea1 = linea1.daveuelveOSMNode();
		
		
		
		/******************************************************************/
		/***********************PARADAS LINEA 1****************************/
		/******************************************************************/
		
				
		ArrayList<String> lineas= new ArrayList<String>();
		lineas.add("1");
		
		Parada parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3406652, -3.6986077), "Avda. Arlanzon", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("1");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3416071, -3.6936404), "Avda. Arlanzon 19", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("1");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3427865, -3.6875625), "Avda. Arlanzon 38", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("1");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3449393, -3.6840724), "Vitoria-Cruz Roja", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("1");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3470654, -3.6801821), "Vitoria-Bda. Militar", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("1");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3491782, -3.6763754), "Vitoria 140", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("1");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3511559, -3.6727390), "Vitoria 166", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("1");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3526942, -3.6700064), "Vitoria 194", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("1");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3542678, -3.6658629), "Vitoria 228", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("1");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3547937, -3.6636411), "Vitoria 252", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("1");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3548576, -3.6644387), "Vitoria 255", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("1");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3541616, -3.6669558), "Vitoria-Real y Antigua", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("1");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3528656, -3.6700611), "Vitoria 175", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("1");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3519242, -3.6717853), "Vitoria 157", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("1");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3507914, -3.6739162), "Vitoria-Juan XXIII", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("1");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3492861, -3.6765873), "Vitoria-A. Ingenieros", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("1");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3467144, -3.6812560), "Vitoria 109", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("1");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3450584, -3.6842121), "Vitoria 73-75", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("1");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3433887, -3.6890444), "Vitoria-Hacienda", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("1");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3419462, -3.6957788), "Vitoria 29", lineas);
		
		/******************************************************************/
		/**********************FIN PARADAS LINEA 1*************************/
		/******************************************************************/
		
		
		Linea2 linea2 = new Linea2("l2", mysql, "Polideportivo Talamillo - Carretera de Arcos", velocidadMediaLinea2, true);
		ArrayList<OsmNode> nosmNodeLinea2 = linea2.daveuelveOSMNode();

		
		/******************************************************************/
		/***********************PARADAS LINEA 2****************************/
		/******************************************************************/
		
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3275569, -3.7120570), "Ctra. de Arcos", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3292385, -3.7109727), "Ctra. de Arcos 41", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3311024, -3.7086948), "S.Pedro y S.Felices 55", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3322948, -3.7060989), "S.Pedro y S.Felices 33", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3328583, -3.7046711), "S.Pedro y S.Felices 19", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3330376, -3.7007998), "S.Agustin 9", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3344093, -3.6994950), "Madrid 27", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3373782, -3.6992170), "S.Pablo 25", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3415976, -3.6998473), "Soportales de Anton", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3433859, -3.699004), "Avda. del Cid 4", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3465737, -3.6974513), "Avda. del Cid 36", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3484392, -3.6966039), "Avda. del Cid 62", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3498785, -3.6959451), "Avda. del Cid 80", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3511269, -3.6953732), "Avda. del Cid 94", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3522736, -3.6943684), "Antiguo Hospital", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3539028, -3.6921017), "Avda. del Cid 110", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3548705, -3.6908941), "Pozanos 2", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3561573, -3.689546), "Pozanos 30", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3573803, -3.6879588), "Pozanos 50", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3589075, -3.6861498), "Pozanos 96", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3586871, -3.6822894), "Islas Canarias", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3602222, -3.684137), "Polideportivo Talamillo", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3598881, -3.6919375), "Hospital Provincial", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3576154, -3.6835817), "Condesa Mencia 93", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3565769, -3.6860282), "Condesa Mencia - Iglesia", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.355576, -3.6883912), "Condesa Mencia 33", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.354799, -3.6899871), "Condesa Mencia 11", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3539273, -3.6924036), "Avda. del Cid 89", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3520501, -3.695069), "Hospital G. Yague", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3510565, -3.6956591), "Avda. del Cid 83", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3498776, -3.6962071), "Avda. del Cid 67", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3479959, -3.6970659), "Avda. del Cid 39", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3459687, -3.6980321), "Avda. del Cid 23", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3437718, -3.6976755), "Plaza Espa&ntilde;a", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3412646, -3.698501), "Vitoria 7", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3379228, -3.7017632), "Madrid 4", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3343928, -3.6996576), "Madrid 22", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3331677, -3.7007759), "Parque San Agustin", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3330581, -3.7040602), "S. Pedro y S. Felices 2", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3321877, -3.706463), "S. Pedro y S. Felices 26", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3311944, -3.7087665), "S. Pedro y S. Felices 52", lineas);
		
		lineas= new ArrayList<String>();
		lineas.add("2");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.3293124, -3.7110564), "Ctra. de Arcos 40", lineas);
		
		
		/******************************************************************/
		/**********************FIN PARADAS LINEA 2*************************/
		/******************************************************************/
		
		
		/*
		lineas= new ArrayList<String>();
		lineas.add("3");
		
		parada = new Parada(mysql, nosmNodeLinea1, new OsmNode(42.0000, -3.0000), "aaaa", lineas);
		*/
		
		mysql.cerrar();
		System.out.println("FIN");
	}
}
