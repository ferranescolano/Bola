package cat.flx.bola;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    GameView gameView;
    SensorManager manager;
    MediaPlayer mediaPlayer;
    SoundPool soundPool;
    int clicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameView = findViewById(R.id.gameView);
        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> deviceSensors = manager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor : deviceSensors) {
            Log.d("flx", sensor.getName() + " " + sensor.getVendor() + " " + sensor.getType());
        }
        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        mediaPlayer.setLooping(true);
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        clicId = soundPool.load(this, R.raw.clic, 1);
        gameView.setMainActivity(this);
        
    }

    public void clic() {
        soundPool.play(clicId, 1, 1, 0, 0, 1);
    }

    @Override
    public void onResume() {
        super.onResume();
        Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
        mediaPlayer.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        manager.unregisterListener(this);
        mediaPlayer.pause();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float aX = sensorEvent.values[0];
        float aY = sensorEvent.values[1];
        gameView.setAccel(-aX, aY);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
