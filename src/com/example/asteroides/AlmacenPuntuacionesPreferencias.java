package com.example.asteroides;

import java.util.Vector;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.widget.Toast;

public class AlmacenPuntuacionesPreferencias implements AlmacenPuntuaciones {

	private static String PREFERENCIAS = "puntuaciones";

	private Context context;

	public AlmacenPuntuacionesPreferencias(Context context) {

		this.context = context;

	}

	public void guardarPuntuacion(int puntos, String nombre, long fecha) {

		SharedPreferences preferencias = context.getSharedPreferences(

		PREFERENCIAS, Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = preferencias.edit();

		for (int n = 9; n >= 1; n--) {

			editor.putString("puntuacion" + n,

			preferencias.getString("puntuacion" + (n - 1), ""));

		}

		editor.putString("puntuacion0", puntos + " " + nombre);

		editor.commit();
		String stadoSD = Environment.getExternalStorageState();

		if (!stadoSD.equals(Environment.MEDIA_MOUNTED)) {

			Toast.makeText(context, "No puedo escribir en la memoria externa",
					Toast.LENGTH_LONG).show();

			return;

		}
	}

	public Vector<String> listaPuntuaciones(int cantidad) {

		Vector<String> result = new Vector<String>();

		SharedPreferences preferencias = context.getSharedPreferences(
				PREFERENCIAS, Context.MODE_PRIVATE);

		for (int n = 0; n <= 9; n++) {

			String s = preferencias.getString("puntuacion" + n, "");

			if (s != "") {

				result.add(s);

			}

		}
		String stadoSD = Environment.getExternalStorageState();

		if (!stadoSD.equals(Environment.MEDIA_MOUNTED)
				&& !stadoSD.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {

			Toast.makeText(context, "No puedo leer en la memoria externa",
					Toast.LENGTH_LONG).show();

			return result;

		}
		
		return result;

	}

}