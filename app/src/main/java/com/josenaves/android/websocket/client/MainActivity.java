package com.josenaves.android.websocket.client;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.josenaves.android.websocket.client.data.ImagesDataSource;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ImagesDataSource dataSource;

    private final AsyncHttpClient connection = AsyncHttpClient.getDefaultInstance();
    private WebSocket webSocket;

    private CoordinatorLayout coordinatorLayout;
    private FloatingActionButton fab;
    private FloatingActionButton fabBatch;

    private TextView textId;
    private TextView textName;
    private TextView textDate;
    private TextView textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinator_layout);

        textId = (TextView)findViewById(R.id.text_id);
        textName = (TextView)findViewById(R.id.text_name);
        textDate = (TextView)findViewById(R.id.text_date);
        textSize = (TextView)findViewById(R.id.text_size);

        // prepare database to use
        dataSource = new ImagesDataSource(this);

        // connect with server
        Log.d(TAG, "Open connection with server...");
        start();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webSocket != null) {
                    webSocket.send(new byte[]{66, 77, 111, 34, 66, 11, 2, 4, 5, 66, 99, 121});
                    Log.d(TAG, "Sending random data to server");
                }
            }
        });
        fab.setEnabled(false);

        fabBatch = (FloatingActionButton) findViewById(R.id.fabSaveBatch);
        fabBatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webSocket != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < 10; i++) {
                                webSocket.send(new byte[]{66});
                                Log.d(TAG, "Sending random data to server");
                            }
                        }
                    }).start();
                }
            }
        });
        fabBatch.setEnabled(false);
    }

    private void start() {
        final String wsuri = "ws://192.168.0.16:9090";
        connection.websocket(wsuri, null, new AsyncHttpClient.WebSocketConnectCallback() {
            @Override
            public void onCompleted(Exception ex, WebSocket ws) {
                if (ex != null) {
                    if (ex instanceof TimeoutException) {
                        Log.e(TAG, "Timeout - server must be down :(");

                        Snackbar.make(MainActivity.this.coordinatorLayout, "Error connection to server", Snackbar.LENGTH_SHORT)
                                .setAction("WebSocket", null).show();
                    }
                    return;
                }

                Log.d(TAG, "Connected.");
                fab.setEnabled(true);
                fabBatch.setEnabled(true);

                ws.setDataCallback(new DataCallback() {
                    @Override
                    public void onDataAvailable(DataEmitter emitter, ByteBufferList bb) {
                        Log.d(TAG, "Got some bytes!");
                        decode(bb.getAllByteArray());
                        bb.recycle();
                    }
                });
                webSocket = ws;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Decode bytes and save the data into database and image in storage
     * @param buffer
     */
    private void decode(byte[] buffer) {
        try {
            final Image image = Image.ADAPTER.decode(buffer);
            Log.d(TAG, image.toString());

            // persist the image
            dataSource.open();
            dataSource.createImage(image);
            //Log.d(TAG, "Records on database: " + dataSource.getAllImages().size());
            dataSource.close();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textId.setText("ID: " + image.id);
                    textName.setText("Name: " + image.name);
                    textDate.setText("Date: " + image.date);
                    textSize.setText("Size: " + image.image_data.size() + " bytes");
                }
            });
        }
        catch (IOException io) {
            Log.e(TAG, "Error decoding message");
        }
    }

}
