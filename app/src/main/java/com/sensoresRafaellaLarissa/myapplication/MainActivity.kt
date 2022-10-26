package com.sensoresRafaellaLarissa.myapplication

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    private lateinit var acelerometro : Sensor
    private lateinit var sensorManager : SensorManager
    private lateinit var listener : SensorEventListener
    private var valoresGravidade = FloatArray(3)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensores : List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)
        sensores.forEach{ sensor ->
            Log.i("SENSORES", sensor.toString())
        }

        //sensor específico
        acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        //criar SensorEventListener
        listener = object : SensorEventListener{
            override fun onSensorChanged(event: SensorEvent?) {
                if(event?.sensor?.type == Sensor.TYPE_ACCELEROMETER){
                        valoresGravidade = event.values.clone()
                        val x = event.values[0]
                        val y = event.values[1]
                        val z = event.values[2]
                        Log.i("SENSORES","X = $x\nY = $y\nZ = $z")
                }
            }
            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
        }
    }

    override fun onResume(){
        super.onResume()
        sensorManager.registerListener(listener, acelerometro, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause(){
        super.onPause()
        sensorManager.unregisterListener(listener)
    }
}