/**
 * 
 */
package com.example.asteroides;

import java.util.Vector;

import android.database.sqlite.SQLiteDatabase;

/**
 * @author El�as
 *
 */
public interface AlmacenPuntuaciones {

public void guardarPuntuacion(int puntos,String nombre,long fecha);

     public Vector<String> listaPuntuaciones(int cantidad);

}