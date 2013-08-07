package com.example.asteroides;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AlmacenPuntuacionesFicheroInterno implements AlmacenPuntuaciones {

	private static String FICHERO = "puntuaciones.txt";

	private Context context;

	public AlmacenPuntuacionesFicheroInterno(Context context) {

		this.context = context;

	}

	public void guardarPuntuacion(int puntos, String nombre, long fecha) {

		try {

			FileOutputStream f = context.openFileOutput(FICHERO,
					Context.MODE_APPEND);

			String texto = puntos + " " + nombre + "\n";

			f.write(texto.getBytes());

			f.close();

		} catch (Exception e) {

			Log.e("Asteroides", e.getMessage(), e);

		}

	}

	public Vector<String> listaPuntuaciones(int cantidad) {

		Vector<String> result = new Vector<String>();

		try {

			FileInputStream f = context.openFileInput(FICHERO);

			BufferedReader entrada = new BufferedReader(
					new InputStreamReader(f));

			int n = 0;

			String linea;

			do {

				linea = entrada.readLine();

				if (linea != null) {

					result.add(linea);

					n++;

				}

			} while (n < cantidad && linea != null);

			f.close();

		} catch (Exception e) {

			Log.e("Asteroides", e.getMessage(), e);

		}

		return result;

	}


}