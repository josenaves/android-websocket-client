package com.josenaves.android.pb.restful.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.josenaves.android.pb.restful.Image;
import com.josenaves.android.pb.restful.R;
import com.josenaves.android.pb.restful.api.ImageAPI;
import com.josenaves.android.pb.restful.data.ImagesDataSource;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ImagesDataSource dataSource;

    private CoordinatorLayout coordinatorLayout;
    private FloatingActionButton fab;
    private FloatingActionButton fabBatch;

    private TextView textId;
    private TextView textName;
    private TextView textDate;
    private TextView textSize;

    private ImageAPI imageAPI;

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

        // make api client
        imageAPI = new ImageAPI(this);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Sending random data to server");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //byte[] imageBuffer = imageAPI.getRandomImage();
                        //decode(imageBuffer);
                        save(imageAPI.getRandomImage());
                    }
                }).start();
            }
        });


        fabBatch = (FloatingActionButton) findViewById(R.id.fabSaveBatch);
        fabBatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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


        if (id == R.id.action_settings) {
            Intent prefsIntent = new Intent(this, SettingsActivity.class);
            startActivity(prefsIntent);
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
            Log.e(TAG, "Error decoding message - " + io.getMessage());
        }
    }


    private void save(final Image image) {
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

}
