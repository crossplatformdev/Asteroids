/**
 * 
 */
package com.example.asteroides;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * @author Elías
 * 
 */
public class Grafico {
	private Drawable drawable; // Imagen que dibujaremos

	private double posX, posY; // Posición

	private double incX, incY; // Velocidad desplazamiento

	private int angulo, rotacion;// Ángulo y velocidad rotación

	private int ancho, alto; // Dimensiones de la imagen

	private int radioColision; // Para determinar colisión

	// Donde dibujamos el gráfico (usada en view.ivalidate)

	private View view;

	// Para determinar el espacio a borrar (view.ivalidate)

	public static final int MAX_VELOCIDAD = 20;

	public Grafico(View view, Drawable drawable) {

		this.view = view;

		this.drawable = drawable;

		ancho = drawable.getIntrinsicWidth();

		alto = drawable.getIntrinsicHeight();

		radioColision = (alto + ancho) / 4;

	}

	public void dibujaGrafico(Canvas canvas) {

		canvas.save();

		int x = (int) (posX + ancho / 2);

		int y = (int) (posY + alto / 2);

		canvas.rotate((float) angulo, (float) x, (float) y);

		drawable.setBounds((int) posX, (int) posY, (int) posX + ancho,
				(int) posY + alto);

		drawable.draw(canvas);

		canvas.restore();

		int rInval = (int) Math.hypot(ancho, alto) / 2 + MAX_VELOCIDAD;

		view.invalidate(x - rInval, y - rInval, x + rInval, y + rInval);

	}

	public void incrementaPos(double factor) {

		posX += incX * factor;

		// Si salimos de la pantalla, corregimos posición

		if (posX < -ancho / 2) {
			posX = view.getWidth() - ancho / 2;
		}

		if (posX > view.getWidth() - ancho / 2) {
			posX = -ancho / 2;
		}

		posY += incY * factor;

		if (posY < -alto / 2) {
			posY = view.getHeight() - alto / 2;
		}

		if (posY > view.getHeight() - alto / 2) {
			posY = -alto / 2;
		}

		angulo += rotacion * factor; // Actualizamos ángulo

	}

	public double distancia(Grafico g) {

		return Math.hypot(posX - g.posX, posY - g.posY);

	}

	public boolean verificaColision(Grafico g) {

		return (distancia(g) < (radioColision + g.radioColision));

	}

	/**
	 * @return the drawable
	 */
	public Drawable getDrawable() {
		return drawable;
	}

	/**
	 * @param drawable the drawable to set
	 */
	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}

	/**
	 * @return the posX
	 */
	public double getPosX() {
		return posX;
	}

	/**
	 * @param posX the posX to set
	 */
	public void setPosX(double posX) {
		this.posX = posX;
	}

	/**
	 * @return the posY
	 */
	public double getPosY() {
		return posY;
	}

	/**
	 * @param posY the posY to set
	 */
	public void setPosY(double posY) {
		this.posY = posY;
	}

	/**
	 * @return the incX
	 */
	public double getIncX() {
		return incX;
	}

	/**
	 * @param incX the incX to set
	 */
	public void setIncX(double incX) {
		this.incX = incX;
	}

	/**
	 * @return the incY
	 */
	public double getIncY() {
		return incY;
	}

	/**
	 * @param incY the incY to set
	 */
	public void setIncY(double incY) {
		this.incY = incY;
	}

	/**
	 * @return the angulo
	 */
	public int getAngulo() {
		return angulo;
	}

	/**
	 * @param angulo the angulo to set
	 */
	public void setAngulo(int angulo) {
		this.angulo = angulo;
	}

	/**
	 * @return the rotacion
	 */
	public int getRotacion() {
		return rotacion;
	}

	/**
	 * @param rotacion the rotacion to set
	 */
	public void setRotacion(int rotacion) {
		this.rotacion = rotacion;
	}

	/**
	 * @return the ancho
	 */
	public int getAncho() {
		return ancho;
	}

	/**
	 * @param ancho the ancho to set
	 */
	public void setAncho(int ancho) {
		this.ancho = ancho;
	}

	/**
	 * @return the alto
	 */
	public int getAlto() {
		return alto;
	}

	/**
	 * @param alto the alto to set
	 */
	public void setAlto(int alto) {
		this.alto = alto;
	}

	/**
	 * @return the radioColision
	 */
	public int getRadioColision() {
		return radioColision;
	}

	/**
	 * @param radioColision the radioColision to set
	 */
	public void setRadioColision(int radioColision) {
		this.radioColision = radioColision;
	}

	/**
	 * @return the view
	 */
	public View getView() {
		return view;
	}

	/**
	 * @param view the view to set
	 */
	public void setView(View view) {
		this.view = view;
	}

	/**
	 * @return the maxVelocidad
	 */
	public static int getMaxVelocidad() {
		return MAX_VELOCIDAD;
	}
	
}
