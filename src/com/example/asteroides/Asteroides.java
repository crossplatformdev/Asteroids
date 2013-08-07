package com.example.asteroides;

import java.io.ObjectInputStream.GetField;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Asteroides extends Activity {

	public static AlmacenPuntuaciones almacen = new AlmacenPuntuacionesArray();
	//public static AlmacenPuntuacionesPreferencias almacen = new AlmacenPuntuacionesPreferencias();
	//public AlmacenPuntuacionesFicheroInterno almacen = new AlmacenPuntuacionesFicheroInterno(this);
	
	private Button bAcercaDe;
	private Button bJugar;
	private Button bConfigurar;
	private Button bSalir;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.intro);
		super.onCreate(savedInstanceState);

		almacen = new AlmacenPuntuacionesFicheroInterno(this);
		setContentView(R.layout.activity_main);
		Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
		if (!(mPlayer.isPlaying())){
			mPlayer.setLooping(isFinishing());
			mPlayer.start();
		}
		
		
		bAcercaDe = (Button) findViewById(R.id.about);
		bAcercaDe.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {

				lanzarAcercaDe(null);

			}

		});

		bJugar = (Button) findViewById(R.id.play);
		bJugar.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {

				lanzarJugar(null);

			}

		});

		bConfigurar = (Button) findViewById(R.id.configure);
		bConfigurar.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {

				lanzarPreferencias(null);

			}

		});

		bSalir = (Button) findViewById(R.id.exit);
		bSalir.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {

				lanzarSalir(null);

			}

		});

	}

	private MediaPlayer mPlayer;

	/* Antiguo método onCreate(); */
	/*
	 * @Override public void onCreate(Bundle savedInstanceState) { this.mPlayer
	 * = MediaPlayer.create(getApplicationContext(), R.raw.intro);
	 * 
	 * super.onCreate(savedInstanceState);
	 * setContentView(R.layout.activity_main);
	 * 
	 * if (!(mPlayer.isPlaying())) mPlayer.start();
	 * 
	 * }
	 */

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.activity_localizacion, menu); return
	 * true; }
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		super.onCreateOptionsMenu(menu);

		MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.localizacion_menu, menu);

		return true;
		/** true -> el menú ya está visible */

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.menu_about:

			lanzarAcercaDe(null);
			break;
		case R.id.menu_exit:

			lanzarSalir(null);
			break;
		case R.id.menu_settings:

			lanzarPreferencias(null);
			break;
		default:
			break;

		}

		return true;
		/** true -> consumimos el item, no se propaga */

	}

	public void lanzarAcercaDe(View view) {

		Intent i = new Intent(this, AcercaDe.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);

	}

	public void lanzarPreferencias(View view) {

		Intent i = new Intent(this, Preferencias.class);

		startActivity(i);

	}

	public void lanzarPuntuaciones(View view) {

		Intent i = new Intent(this, Puntuaciones.class);

		startActivity(i);

	}

	public void lanzarJugar(View view) {

		Intent i = new Intent(this, Jugar.class);
		this.mPlayer.stop();
		startActivityForResult(i, 1234);

	}

	public void lanzarSalir(View view) {
		this.mPlayer.stop();
		finish();
	}

	 @Override 
	 protected void onActivityResult (int requestCode, int resultCode, Intent data){
	     
		 super.onActivityResult(requestCode, resultCode, data);
	     
	     if (requestCode==1234 & resultCode==RESULT_OK & data!=null) {
	        int puntuacion = data.getExtras().getInt("puntuacion");
	        String nombre = "Yo";
	        // Mejor leerlo desde un Dialog o una nueva actividad 
	        //AlertDialog.Builder
	        almacen.guardarPuntuacion(puntuacion, nombre,
	                        System.currentTimeMillis());
	        lanzarPuntuaciones(null);
	     }

	  }
	
	@Override
	public void onDestroy() {
		this.mPlayer.stop();
		Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
		super.onDestroy();

	}
	 @Override protected void onStart() {

		   super.onStart();
			this.mPlayer.start();
		   Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();

		}

		 

		@Override protected void onResume() {

		   super.onResume();
			this.mPlayer.start();
		   Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();

		}

		 

		@Override protected void onPause() {

		   Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
			this.mPlayer.pause();
		   super.onPause();

		}

		 

		@Override protected void onStop() {

		   Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
			this.mPlayer.stop();
		   super.onStop();

		}

		 

		@Override protected void onRestart() {

		   super.onRestart();
			this.mPlayer.start();
		   Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();

		}
		
		@Override
		protected void onSaveInstanceState(Bundle estadoGuardado) {

			super.onSaveInstanceState(estadoGuardado);

			if (mPlayer != null) {

				int pos = mPlayer.getCurrentPosition();

				estadoGuardado.putInt("posicion", pos);

			}

		}

		@Override
		protected void onRestoreInstanceState(Bundle estadoGuardado) {

			super.onRestoreInstanceState(estadoGuardado);

			if (estadoGuardado != null && mPlayer != null) {

				int pos = estadoGuardado.getInt("posicion");

				mPlayer.seekTo(pos);

			}

		}

}
