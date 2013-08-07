
/**
 * 
 */
package com.example.asteroides;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * @author Elías
 * 
 */
public class Preferencias extends PreferenceActivity {
	
	private static int PREF_FRAGS = 3;
	private static String PREF_STOR = "array";

	public SharedPreferences preferencias;		

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferencias);
		//SharedPreferences preferencias = getSharedPreferences( PREF_STOR , MODE_WORLD_READABLE);		
	}
/*
	public void guardarAlmacenamiento () {

		SharedPreferences preferencias = getSharedPreferences(
		PREF_STOR, Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = preferencias.edit();

			for (int n = 9; n >= 1; n--) {

				editor.putString("almacenamiento" + preferencias.getString("puntuacion" + (n - 1), ""));

			}
			editor.putString("puntuaciones", "")
			editor.putString("puntuacion0", puntos + " " + nombre);

			editor.commit();

		}
		
	}
*/
	public void playMusic(MediaPlayer mPlayer) {
		mPlayer.start();
	}

	public void stopMusic(MediaPlayer mPlayer) {
		mPlayer.stop();
	}
}