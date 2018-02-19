package com.example.usuario.boundservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * La actividad requiere un servicio concreto, que ofrece en
 * este caso el servicio BoundService.
 */
public class MainActivity extends AppCompatActivity {

    TextView txvTime;
    Button btnTime, btnStop;

    //Servicio que nos ofrece un contador de tiempo
    private BoundService boundService;
    //Indica si estamos vinculados al servicio
    private boolean isBound = false;

    //Necesitamos un objeto ServiceConnection que controla
    //la conexión con el servidor
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            boundService = ((BoundService.MyBinder) binder).getService();
            isBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txvTime = findViewById(R.id.txvTime);
        btnTime = findViewById(R.id.btnTime);
        btnStop = findViewById(R.id.btnStop);
        btnTime.setEnabled(false);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isBound) {
                    String time = boundService.getTimestamp();
                    txvTime.setText(time);
                    txvTime.setVisibility(View.VISIBLE);
                    stopBoundService();
                }
                btnTime.setEnabled(true);
                btnStop.setEnabled(false);
            }
        });
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBound) {
                    txvTime.setText("");
                    txvTime.setVisibility(View.INVISIBLE);
                    btnStop.setEnabled(true);
                    btnTime.setEnabled(false);
                }
            }
        });
    }

    //Podemos arrancar el servicio al empezar
    @Override
    protected void onStart() {
        super.onStart();
        startBoundService();
    }

    //OBLIGATORIO: Desvincular el servicio de la Activity
    @Override
    protected void onStop() {
        super.onStop();
        if(isBound)
            stopBoundService();
    }

    /**
     * Inicia el servidor.
     * Si se inicia mediante startService, el servicio que se inicia
     * en serviceConnection es nulo. Se inicializa con bindService.
     */
    private void startBoundService() {
        Intent intent = new Intent(this, BoundService.class);
        //startService(intent);
        //IMPORTANTE: Hay que vincular
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }
    /**
     * Para el servidor
     */
    private void stopBoundService() {
        //stopService(intent);
        //IMPORTANTE: Hay que desvincular
        unbindService(serviceConnection);
    }

}
