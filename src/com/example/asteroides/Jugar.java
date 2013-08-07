package com.example.asteroides;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Jugar extends Activity {

	private VistaJuego vistaJuego;
	private MediaPlayer mPlayer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.win);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);

		if (!(mPlayer.isPlaying())) {

			mPlayer.setLooping(isFinishing());
			mPlayer.start();
		}
		vistaJuego = (VistaJuego) findViewById(R.id.VistaJuego);
		vistaJuego.setPadre(this);
	}

	@Override
	public void onDestroy() {
		this.mPlayer.stop();
		// vistaJuego.getHiloJuego().interrupt();
		// vistaJuego.getHiloJuego().detener();
		super.onDestroy();

	}

	@Override
	protected void onPause() {
		this.vistaJuego.pausarSensores();
		this.mPlayer.pause();
		vistaJuego.getHiloJuego().pausar();
		super.onPause();
	}

	@Override
	protected void onResume() {
		this.vistaJuego.reanudarSensores();
		this.mPlayer.start();
		vistaJuego.getHiloJuego().reanudar();
		super.onResume();
	}

	public void sePulsa(View view) {
		Toast.makeText(this, "Pulsado", Toast.LENGTH_SHORT).show();
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
