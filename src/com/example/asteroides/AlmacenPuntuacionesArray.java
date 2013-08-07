/**
 * 
 */
package com.example.asteroides;

import java.util.Vector;

import android.database.sqlite.SQLiteDatabase;

/**
 * @author Elías
 * 
 */
public class AlmacenPuntuacionesArray implements AlmacenPuntuaciones {

	private Vector<String> puntuaciones;

	public AlmacenPuntuacionesArray() {

		puntuaciones = new Vector<String>();

	}

	@Override
	public void guardarPuntuacion(int puntos, String nombre, long fecha) {

		puntuaciones.add(0, puntos + " " + nombre);

	}

	@Override
	public Vector<String> listaPuntuaciones(int cantidad) {

		return puntuaciones;

	}

}