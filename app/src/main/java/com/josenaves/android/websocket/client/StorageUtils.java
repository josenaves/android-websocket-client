package com.josenaves.android.websocket.client;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;

public final class StorageUtils {

    private static final String TAG = StorageUtils.class.getSimpleName();

    private static final String PREFIX = "Android/data";

    /**
     * Create a storage path folders based on datetime on the device
     * @return if success, the complete path for the saved file; otherwise, null
     */
    public static String saveFileInExternalStorage(Context context, String filename, byte[] data) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = 1 + cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        int milisecond = cal.get(Calendar.MILLISECOND);

        String absolutePath = String.format("%s/%s/%s/%s/%s/%s/%s/%s/%s/%s/",
                Environment.getExternalStorageDirectory().getAbsolutePath(),
                PREFIX,
                context.getApplicationContext().getPackageName(),
                year, month, day, hour, minute, second, milisecond);

        Log.d(TAG, absolutePath);

        // create the directory structure
        File folders = new File(absolutePath);
        if (folders.mkdirs()){
            Log.d(TAG, "Folders created at " + absolutePath);
        } else {
            Log.e(TAG, "Error creatingn dirs");
        }

        // get a reference for the file to be created
        File fileToBeSaved = new File(folders, filename);

        boolean saved = false;

        FileOutputStream fos = null;
        try {
            //fos = context.openFileOutput(testFile.getAbsolutePath(), Context.MODE_WORLD_READABLE);
            fos = new FileOutputStream(fileToBeSaved);
            fos.write(data);
            fos.close();
            return fileToBeSaved.getAbsolutePath();
        }
        catch(FileNotFoundException fnf) {
            Log.e(TAG, "Could not find path: " +  fnf.getMessage());
        }
        catch(IOException io){
            Log.e(TAG, io.getMessage());
        }
        finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException io){
                    Log.e(TAG, "Error closing file: " + io.getMessage());
                }
            }
        }

        return null;
    }

    public static void localFile(Context context) {

        try {
            // Use Activity method to create a file in the writeable directory
            FileOutputStream fos = context.openFileOutput("filename", Context.MODE_WORLD_WRITEABLE);

            // Create buffered writer
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
            writer.write("Hi, I'm writing stuff");
            writer.close();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

    }

}
