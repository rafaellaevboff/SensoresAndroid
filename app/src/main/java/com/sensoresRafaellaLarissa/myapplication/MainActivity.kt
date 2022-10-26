package com.sensoresRafaellaLarissa.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.sensoresRafaellaLarissa.myapplication.databinding.ActivityMainBinding
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    private lateinit var acelerometro : Sensor
    private lateinit var pressaoAr : Sensor
    private lateinit var sensorManager : SensorManager
    private lateinit var listener : SensorEventListener
    private var valoresGravidade = FloatArray(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Lista todos os sensores disponíveis no dispositivo
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensores : List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)
        sensores.forEach{ sensor ->
            Log.i("SENSORES", sensor.toString())
        }

        //sensor específico
        acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        pressaoAr = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)

        //criar SensorEventListener
        listener = object : SensorEventListener{
            @SuppressLint("SetTextI18n")
            override fun onSensorChanged(event: SensorEvent?) {
                when(event?.sensor?.type){
                    Sensor.TYPE_PRESSURE-> {
                        val pressao = (event.values[0])/10
                        Log.i("SENSORES", "Pressão = $pressao hPa")
                        binding.valorPressao.text = "$pressao hpa"
                    }
                    Sensor.TYPE_ACCELEROMETER-> {
                        valoresGravidade = event.values.clone()
                        val x = event.values[0]
                        val y = event.values[1]
                        val z = event.values[2]
                        val gravidade = String.format("%.2f", y)
                        Log.i("SENSORES", "X = $x m/s^2\nY = $y m/s^2\nZ = $z m/s^2")
                        binding.valorGravidade.text = "$gravidade m/s^2"
                    }
                }
            }
            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
        }

        binding.valorUmidade.text = ""
    }

    override fun onResume(){
        super.onResume()
        sensorManager.registerListener(listener, acelerometro, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(listener, pressaoAr, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause(){
        super.onPause()
        sensorManager.unregisterListener(listener)
    }
}
