package com.example.asteroides;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlSerializer;

import android.util.Log;
import android.util.Xml;

class ListaPuntuaciones {

	private class Puntuacion {

		int puntos;

		String nombre;

		long fecha;

	}

	private List<Puntuacion> listaPuntuaciones;
	private boolean cargadaLista;

	public ListaPuntuaciones() {

		listaPuntuaciones = new ArrayList<Puntuacion>();

	}

	public void nuevo(int puntos, String nombre, long fecha) {

		Puntuacion puntuacion = new Puntuacion();

		puntuacion.puntos = puntos;

		puntuacion.nombre = nombre;

		puntuacion.fecha = fecha;

		listaPuntuaciones.add(puntuacion);

	}

	public Vector<String> aVectorString() {

		Vector<String> result = new Vector<String>();

		for (Puntuacion puntuacion : listaPuntuaciones) {

			result.add(puntuacion.nombre + " " + puntuacion.puntos);

		}

		return result;

	}

	public void leerXML(InputStream entrada) throws Exception {

		SAXParserFactory fabrica = SAXParserFactory.newInstance();

		SAXParser parser = fabrica.newSAXParser();

		XMLReader lector = parser.getXMLReader();

		ManejadorXML manejadorXML = new ManejadorXML();

		lector.setContentHandler(manejadorXML);

		lector.parse(new InputSource(entrada));

		cargadaLista = true;

	}

	public void escribirXML(OutputStream salida) {

		XmlSerializer serializador = Xml.newSerializer();

		try {

			serializador.setOutput(salida, "UTF-8");

			serializador.startDocument("UTF-8", true);

			serializador.startTag("", "lista_puntuaciones");

			for (Puntuacion puntuacion : listaPuntuaciones) {

				serializador.startTag("", "puntuacion");

				serializador.attribute("", "fecha",

				String.valueOf(puntuacion.fecha));

				serializador.startTag("", "nombre");

				serializador.text(puntuacion.nombre);

				serializador.endTag("", "nombre");

				serializador.startTag("", "puntos");

				serializador.text(String.valueOf(puntuacion.puntos));

				serializador.endTag("", "puntos");

				serializador.endTag("", "puntuacion");

			}

			serializador.endTag("", "lista_puntuaciones");

			serializador.endDocument();

		} catch (Exception e) {

			Log.e("Asteroides", e.getMessage(), e);

		}

	}

	class ManejadorXML extends DefaultHandler {

		private StringBuilder cadena;

		private Puntuacion puntuacion;

		@Override
		public void startDocument() throws SAXException {

			listaPuntuaciones = new ArrayList<Puntuacion>();

			cadena = new StringBuilder();

		}

		@Override
		public void startElement(String uri, String nombreLocal,
				String nombreCualif, Attributes atr) throws SAXException {

			cadena.setLength(0);

			if (nombreLocal.equals("puntuacion")) {

				puntuacion = new Puntuacion();

				puntuacion.fecha = Long.parseLong(atr.getValue("fecha"));

			}

		}

		@Override
		public void characters(char ch[], int comienzo, int lon) {

			cadena.append(ch, comienzo, lon);

		}

		@Override
		public void endElement(String uri, String nombreLocal,

		String nombreCualif) throws SAXException {

			if (nombreLocal.equals("puntos")) {

				puntuacion.puntos = Integer.parseInt(cadena.toString());

			} else if (nombreLocal.equals("nombre")) {

				puntuacion.nombre = cadena.toString();

			} else if (nombreLocal.equals("puntuacion")) {

				listaPuntuaciones.add(puntuacion);

			}

		}

		@Override
		public void endDocument() throws SAXException {

		}

	}
}