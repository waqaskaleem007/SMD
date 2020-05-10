package com.example.assignment2;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class importExportSyncService extends Service {

    String data;
    public static ArrayList<Student> students;
    public StudentDataClass studentDataClass;
    DataBaseHelper myDb;

    Messenger messenger = new Messenger(new IncomingHandler());
    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        assert messenger != null;
        return messenger.getBinder();
    }
    @SuppressLint("HandlerLeak")
    public class IncomingHandler extends Handler {
        public void handleMessage(Message msg){
            switch(msg.what){
                case 1:
                    processMessage(msg,msg.replyTo);
                case 2:
                    processMessageExport(msg, msg.replyTo);
                default:
                    super.handleMessage(msg);
            }
        }
    }


    public void processMessage(final Message msg, Messenger replyTo){

        Thread thread = new Thread(new Runnable() {
            Bundle bundle = msg.getData();
            String line;
            @Override
            public void run() {
                line = load(bundle.getString("url"));
                parse(line);
            }
        });
        thread.start();
    }

    private String load(String site){
        String line = "";
        try{
            Log.i("Load", "into the load");
            URL url = new URL(" https://sites.google.com/site/farooqahmedrana/Home/students.xml?attredirects=0&d=1");
            //URL url = new URL(site);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);

            connection.connect();

            Log.i("Load", "into the load after connection");

            StringBuilder content = new StringBuilder();
            BufferedReader reader = new BufferedReader( new InputStreamReader( connection.getInputStream() ) );


            while( (line = reader.readLine()) != null ){
                content.append(line);
                Log.d("into while of load", line); //correctly working
            }

            line = content.toString();

        } catch(Exception ex) {
            Log.e("into Exception", "into the exception in main activity",ex);
            line = ex.getMessage();
            ex.printStackTrace();
        }
        return line;
    }

    private void parse(String xml){

        Log.d("Parse", "into the parse");
        Message reply = Message.obtain(null,1);
        String category = "";
        myDb = new DataBaseHelper(getApplicationContext());
        students = new ArrayList<Student>();
        try{

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(xml));

            int event = parser.getEventType();
            while(event != XmlPullParser.END_DOCUMENT){
                //Log.d("into while of name",parser.getName());

                if(event == XmlPullParser.START_TAG &&
                        parser.getName().equals("course") ) {

                    category = parser.getAttributeValue(null,"title");
                    Log.d("into category","category" + category);
                }

                if(event == XmlPullParser.START_TAG &&
                        parser.getName().equals("student") ){

                    String name = parser.getAttributeValue(null,"name");
                    String rollNo = parser.getAttributeValue(null,"rollno");
                    students.add(new Student(name,rollNo,false));
                    myDb.insertData(name,rollNo);
                    Log.d("into student Data",name);
                }

                event = parser.next();
            }
        } catch(Exception e){
            Log.e("into Exception of parse", "this is exception", e);
        }
        Intent dialogIntent = new Intent(this, MainActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dialogIntent);
    }

    private void processMessageExport(final Message msg, Messenger replyTo){

        Log.d("into the export", "this is Export");

        Thread thread = new Thread(new Runnable(){
            Bundle bundle = msg.getData();
            public void run(){
                post(bundle.getString("url"));
            }
        });

        thread.start();
    }

    public String post(String site){

        StringBuilder content = new StringBuilder();

        try{
            Log.d("into export","into the export");
            URL url = new URL("https://drive.google.com/drive/u/0/folders/1VaFl90Z-DiLnN1VKTs6bpX3z0KRD5Hjk");
            //URL url = new URL(site);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type","text/xml");
            connection.setDoOutput(true);
            connection.connect();

            Log.d("into export","into the export after connection");
            Log.d("into export","into data uploaded");
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            writer.write(getStudentContent());
            writer.flush();

            BufferedReader reader = new BufferedReader( new InputStreamReader( connection.getInputStream() ) );

            String line = "";
            while( (line = reader.readLine()) != null ){
                content.append(line);
            }

            connection.disconnect();

        } catch(Exception ex) {
            Log.e("into post Exception", "into this post exception",ex);
            ex.printStackTrace();
        }

        return content.toString();
    }

    public String getStudentContent(){

        String uri = "content://com.example.assignment2/Student";
        @SuppressLint("Recycle")
        Cursor cursor = getContentResolver().query(Uri.parse(uri),null,null,null,null);

        StringBuilder result = new StringBuilder();
        while (cursor != null && cursor.moveToNext()){
            String rollno = cursor.getString(cursor.getColumnIndex("ROLLNO"));
            String name = cursor.getString(cursor.getColumnIndex("NAME"));
            boolean present = cursor.getInt(3) > 0;
            result.append ("<student rollno='" + rollno + "' name='" + name + "' attendance='" + present + "' />");
        }

        return "<course    code='CS440' title='SMD'  date='10-05-2020' >" + result.toString() + "</student>";
    }



    public interface StudentDataClass{
        public void StudentData(ArrayList<Student> students);
    }


}
