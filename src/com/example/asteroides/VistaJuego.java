package com.example.asteroides;

import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class VistaJuego extends View implements SensorEventListener {

	// //// PUNTUACION //// ///
	 private int puntuacion = 0;
	// //// THREAD Y TIEMPO //////
	// Thread encargado de procesar el juego
	private ThreadJuego hiloJuego = new ThreadJuego();

	// Cada cuanto queremos procesar cambios (ms)
	private static int PERIODO_PROCESO = 50;
	// Cuando se realizó el último proceso
	private long ultimoProceso = 0;
	protected double retardo;
	protected long ahora;
	protected int semaforo = 0;

	// //// NAVE //////
	private Grafico nave;// Gráfico de la nave
	private int giroNave; // Incremento de dirección
	private float aceleracionNave; // aumento de velocidad
	// Incremento estándar de giro y aceleración
	//private static final int PASO_GIRO_NAVE = 3;
	//private static final float PASO_ACELERACION_NAVE = 0.15f;

	// //// ASTEROIDES //////
	private Vector<Grafico> vectorAsteroides; // Vector con los vectorAsteroides
	private int numAsteroides = 5; // Número inicial de asteroides
	private int numFragmentos = 3; // Fragmentos en que se divide
	
	// //// MISIL //////
	private Grafico misil;
	private static int PASO_VELOCIDAD_MISIL = 12;
	private boolean misilActivo = false;
	private int tiempoMisil;
/*
	// //// BOTON //////
	private Grafico BottonArma;
	private Grafico BottonFreno;
	// //// ARMA MASIVA (BOTON) //////
	private int posX;
	private int posY;
*/
	// //// ARMA MASIVA (BOTON) //////
	//private Grafico arma;

	// //// FRENO DE MANO (BOTON) //////
	//private Grafico freno;

	// //// SENSORES // //////
	private SensorManager mSensorManager;
	public Sensor accelerationSensor;
	private List<Sensor> listSensors;
	
	private Activity padre;

	 

	 public void setPadre(Activity padre) {
	       this.padre = padre;
	 }
	 private void salir() {
		    Bundle bundle = new Bundle();
		    bundle.putInt("puntuacion", puntuacion);
		    Intent intent = new Intent();
		    intent.putExtras(bundle);
		    padre.setResult(Activity.RESULT_OK, intent);
		    padre.finish();
		}
	 
	/**
	 * @return the mSensorManager
	 */
	public SensorManager getmSensorManager() {
		return mSensorManager;
	}

	/**
	 * @return the listSensors
	 */
	public List<Sensor> getListSensors() {
		return listSensors;
	}
	
	/**
	 * @return the hiloJuego
	 */
	public ThreadJuego getHiloJuego() {
		return hiloJuego;
	}

	/**
	 * @param hiloJuego
	 *            the hiloJuego to set
	 */
	public void setHiloJuego(ThreadJuego hiloJuego) {
		this.hiloJuego = hiloJuego;
	}

    public void pausarSensores() {
        this.mSensorManager.unregisterListener(this);
    }

    public void reanudarSensores() {
        if (this.accelerationSensor != null) {
            this.mSensorManager.registerListener(this, accelerationSensor,
                    SensorManager.SENSOR_DELAY_GAME);
        }
    }
   
	public VistaJuego(Context context, AttributeSet attrs) {

		super(context, attrs);

		mSensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);

		accelerationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		listSensors = mSensorManager
				.getSensorList(Sensor.TYPE_ACCELEROMETER);

		if (!listSensors.isEmpty()) {
			accelerationSensor = listSensors.get(0);
			mSensorManager.registerListener(this, accelerationSensor,
					SensorManager.SENSOR_DELAY_GAME);
		}
		
		Drawable drawableNave, drawableAsteroide, drawableMisil; //, drawableArma, drawableFreno;

		drawableAsteroide = context.getResources().getDrawable(
				R.drawable.asteroide1);
		drawableNave = context.getResources().getDrawable(R.drawable.nave);
		//drawableArma = context.getResources().getDrawable(
		//		R.drawable.animacion_pulsador);
		//drawableFreno = context.getResources().getDrawable(
		//		R.drawable.animacion_pulsador);
		drawableMisil = context.getResources().getDrawable(R.drawable.misil1);

		vectorAsteroides = new Vector<Grafico>();
		nave = new Grafico(this, drawableNave);
		//arma = new Grafico(this, drawableArma);
		//freno = new Grafico(this, drawableFreno);
		misil = new Grafico(this, drawableMisil);

		for (int i = 0; i < numAsteroides; i++) {

			Grafico asteroide = new Grafico(this, drawableAsteroide);
			asteroide.setIncY(Math.random() * 4 - 2);
			asteroide.setIncX(Math.random() * 4 - 2);
			asteroide.setAngulo((int) (Math.random() * 360));
			asteroide.setRotacion((int) (Math.random() * 8 - 4));
			vectorAsteroides.add(asteroide);

		}
/*
		freno.setAlto(192);
		arma.setAlto(192);
		freno.setAncho(192);
		arma.setAncho(192);
		arma.setPosX(0);
		arma.setPosY(200);
		freno.setPosY(200);
		arma.setPosX(0);
		freno.setPosX(800 - freno.getAncho());
		freno.setAngulo(0);
		arma.setAngulo(0);
*/
		// arma.setOnClickListener(new OnClickListener() {

		// public void onClick(View view) {
		// sePulsa(null);
		//
		// }

		// });


	}

	private float mX = 0, mY = 0;
	private boolean disparo = false;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			disparo = true;
			break;
		case MotionEvent.ACTION_MOVE:
			float dx = Math.abs(x - mX);
			float dy = Math.abs(y - mY);
			if (dy < 20 && dx > 1) {
				giroNave = Math.round((x - mX)*3);
				disparo = false;
			} else if (dx < 20 && dy > 1) {
				aceleracionNave = Math.round((mY - y) / 33);
				disparo = false;
			}
			break;
		case MotionEvent.ACTION_UP:
			giroNave = 0;
			aceleracionNave = (aceleracionNave > 0.10f) ? aceleracionNave-=0.250f : 0.0000000000000f;
			if (disparo) {
				ActivaMisil();
			}
			break;
		}
		mX = x;
		mY = y;
		return true;
	}

	@Override
	protected void onSizeChanged(int ancho, int alto, int ancho_anter,
			int alto_anter) {

		/**
		 * Introduce estas líneas al final del método onSizeChanged():
		 * 
		 * Esto ocasionará que se llame al método run() del hilo de ejecución.
		 * Este método es un bucle infinito que continuamente llama al
		 * actualizaFisica().
		 */

		ultimoProceso = System.currentTimeMillis();
		if (!hiloJuego.isAlive())
			hiloJuego.start();

		super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);

		// Una vez que conocemos nuestro ancho y alto.

		for (Grafico asteroide : vectorAsteroides) {

			do {
				/** vectorAsteroides no coinciden en incio con nave */

				asteroide.setPosX(Math.random()
						* (ancho - asteroide.getAncho()));

				asteroide.setPosY(Math.random() * (alto - asteroide.getAlto()));

			} while (asteroide.distancia(nave) < (ancho + alto) / 5);

			/** Posicion de la nave en el centro de la pantalla */

			nave.setPosX((ancho - nave.getAncho()) / 2);

			nave.setPosY((alto - nave.getAlto()) / 2);
		}
	}

	@Override
	synchronized protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);

		for (Grafico asteroide : vectorAsteroides) {

			asteroide.dibujaGrafico(canvas);

		}
		nave.dibujaGrafico(canvas);
		//freno.dibujaGrafico(canvas);
		//arma.dibujaGrafico(canvas);
		if (misilActivo)
			misil.dibujaGrafico(canvas);

	}

	synchronized protected void actualizaTiempo() {

		ahora = System.currentTimeMillis();

		// No hagas nada si el período de proceso no se ha cumplido.

		if (ultimoProceso + PERIODO_PROCESO > ahora) {
			return;
		}

		// Para una ejecución en tiempo real calculamos retardo
		retardo = (ahora - ultimoProceso) / PERIODO_PROCESO;
		ultimoProceso = ahora; // Para la próxima vez
		semaforo = 1;
		
		for (Grafico asteroide : vectorAsteroides) {
			    if (asteroide.verificaColision(nave)) {
			       salir();
			    }
		}
		
	}

	synchronized private void destruyeAsteroide(int i) {

		double posY = vectorAsteroides.elementAt(i).getPosY();
		double posX = vectorAsteroides.elementAt(i).getPosX();
		Drawable drawableAsteroide = null;
		puntuacion += 100;
		misilActivo = false;
		/*
		if( vectorAsteroides.elementAt(i).getDrawable() == getResources().getDrawable(R.drawable.asteroide1)){
			//vectorAsteroides.remove(i);
			drawableAsteroide = getContext().getResources().getDrawable(R.drawable.asteroide2);
			
			for (int p = 0; p < numFragmentos; p++) {
				Grafico asteroide = new Grafico(this, drawableAsteroide);
				asteroide.setIncY(posY);			
				asteroide.setIncX(posX);
				asteroide.setAngulo((int) (Math.random() * 360));
				asteroide.setRotacion((int) (Math.random() * 8 - 4));
				vectorAsteroides.add(asteroide);

			}

		} else if( vectorAsteroides.elementAt(i).getDrawable() == getResources().getDrawable(R.drawable.asteroide2)){
			//vectorAsteroides.remove(i);
			drawableAsteroide = getContext().getResources().getDrawable(R.drawable.asteroide3);
			
			for (int p = 0; p < numFragmentos; p++) {
				Grafico asteroide = new Grafico(this, drawableAsteroide);
				asteroide.setIncY(posY);			
				asteroide.setIncX(posX);
				asteroide.setAngulo((int) (Math.random() * 360));
				asteroide.setRotacion((int) (Math.random() * 8 - 4));
				vectorAsteroides.add(asteroide);

			} 
			
		} else {
			//vectorAsteroides.remove(i);
			if (vectorAsteroides.isEmpty()) {
				salir();
			}
		}*/
		vectorAsteroides.remove(i);
	}

	synchronized private void ActivaMisil() {

		misil.setPosX(nave.getPosX() + nave.getAncho() / 2 - misil.getAncho()
				/ 2);

		misil.setPosY(nave.getPosY() + nave.getAlto() / 2 - misil.getAlto() / 2);

		misil.setAngulo(nave.getAngulo());

		misil.setIncX(Math.cos(Math.toRadians(misil.getAngulo()))
				* PASO_VELOCIDAD_MISIL);

		misil.setIncY(Math.sin(Math.toRadians(misil.getAngulo()))
				* PASO_VELOCIDAD_MISIL);

		tiempoMisil = (int) Math.min(
				this.getWidth() / Math.abs(misil.getIncX()), this.getHeight()
						/ Math.abs(misil.getIncY())) - 2;

		misilActivo = true;

	}

	public class ThreadJuego extends Thread {

		private ThreadNave hiloNave = new ThreadNave();
		private ThreadMisiles hiloMisiles = new ThreadMisiles();
		private ThreadAsteroides hiloAsteroides = new ThreadAsteroides();

		private boolean pausa, corriendo;

		public synchronized void pausar() {
			pausa = true;
		}

		public synchronized void reanudar() {
			corriendo = true;
			pausa = false;
			notify();
		}

		public void detener() {
			corriendo = false;
			if (pausa)
				reanudar();
		}

		@Override
		public void run() {

			ultimoProceso = System.currentTimeMillis();

			if (!hiloAsteroides.isAlive())
				hiloAsteroides.start();
			if (!hiloNave.isAlive())
				hiloNave.start();
			if (!hiloMisiles.isAlive())
				hiloMisiles.start();
			
			semaforo = 0;

			//while (corriendo) {
				

				
				synchronized (this) {
					while (pausa) {

						try {
							wait();
						} catch (Exception e) {

						}

					}

				//}
			}

		}

		public class ThreadAsteroides extends Thread {

			@Override
			public void run() {
				corriendo = true;

				while (corriendo) {

					actualizaTiempo();
					actualizarAsteroides(retardo);

					synchronized (this) {

						while (pausa) {

							try {

								wait();
							} catch (Exception e) {

							}

						}

					}

				}

			}

			synchronized public void actualizarAsteroides(double retardo) {
				if (semaforo != 1)
					return;
				for (Grafico asteroide : vectorAsteroides) {

					asteroide.incrementaPos(retardo);
					
				    if (asteroide.verificaColision(nave)) {
					    salir();
				    }
						
				}
				semaforo = 2;
			}

		}

		public class ThreadNave extends Thread {

			@Override
			public void run() {
				corriendo = true;

				while (corriendo) {

					actualizaTiempo();
					actualizarNave(retardo);
					synchronized (this) {

						while (pausa) {

							try {
								wait();
							} catch (Exception e) {

							}
						}
					}
				}
			}

			synchronized public void actualizarNave(double retardo) {

				if (semaforo != 2)
					return;
				// Actualizamos velocidad y dirección de la nave a partir de
				// giroNave y aceleracionNave (según la entrada del jugador)
				nave.setAngulo((int) (nave.getAngulo() + giroNave * retardo));

				double nIncX = nave.getIncX() + aceleracionNave
						* Math.cos(Math.toRadians(nave.getAngulo())) * retardo;

				double nIncY = nave.getIncY() + aceleracionNave
						* Math.sin(Math.toRadians(nave.getAngulo())) * retardo;

				// Actualizamos si el módulo de la velocidad no excede el máximo
				if (Math.hypot(nIncX, nIncY) <= Grafico.getMaxVelocidad()) {

					nave.setIncX(nIncX);

					nave.setIncY(nIncY);

				}

				// Actualizamos posiciones X e Y
				nave.incrementaPos(retardo);
				semaforo = 3;
			}

		}

		public class ThreadMisiles extends Thread {

			@Override
			public void run() {
				while (corriendo) {
					actualizaTiempo();
					actualizarDisparoMisil(retardo);
					synchronized (this) {

						while (pausa) {

							try {
								wait();
							} catch (Exception e) {

							}

						}
					}
				}
			}

			synchronized private void actualizarDisparoMisil(double retardo) {
				if (semaforo != 3)
					return;
				if (misilActivo) {
					misil.incrementaPos(retardo);
					tiempoMisil -= retardo;
					if (tiempoMisil < 0) {
						misilActivo = false;
					} else {

						for (int i = 0; i < vectorAsteroides.size(); i++)

							if (misil.verificaColision(vectorAsteroides
									.elementAt(i))) {
								destruyeAsteroide(i);
								break;
							}

					}
				}
				semaforo = 0;
			}

		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	private boolean hayValorInicial = false;
	private float[] valorInicial = new float[3];

	@Override
	/*
	 * public void onSensorChanged(SensorEvent event) {
	 * 
	 * float valor = event.values[1];
	 * 
	 * if (!hayValorInicial) {
	 * 
	 * valorInicial = valor;
	 * 
	 * hayValorInicial = true;
	 * 
	 * }
	 * 
	 * giroNave = (int) (valor - valorInicial) / 3;
	 * 
	 * }
	 */
	synchronized public void onSensorChanged(SensorEvent event) {

		float[] valor = new float[3];
		valor = event.values;

		if (!hayValorInicial) {

			valorInicial = valor;
			hayValorInicial = true;

		}

		// Si está en modo portrait y
		/*
		 * // Si se inclina hacia la derecha if (valorInicial[1] < valor[1]) {
		 * for (int i = (int) nave.getAngulo(); nave.getAngulo() >= 90; i--) {
		 * giroNave = i; } // Si se inclina hacia la izquierda } else if
		 * (valorInicial[1] > valor[1]) { for (int i = (int) nave.getAngulo();
		 * nave.getAngulo() >= 180; i++) { giroNave = i; } // Si se inclina
		 * hacia afuera/atras } else if (valorInicial[0] < valor[0]) { for (int
		 * i = (int) nave.getAngulo(); nave.getAngulo() >= 0; i--) { giroNave =
		 * i; } // Si se inclina hacia la adentro/adelante } else if
		 * (valorInicial[0] > valor[0]) { for (int i = (int) nave.getAngulo();
		 * nave.getAngulo() >= 270; i++) { giroNave = i; } } else { giroNave =
		 * 0; }
		 */
		giroNave = (int) ((valor[1] - valorInicial[1] / 1080));

	}

}
