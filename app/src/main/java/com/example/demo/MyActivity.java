package com.example.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.radiusnetworks.ibeacon.IBeacon;
import com.radiusnetworks.ibeacon.IBeaconConsumer;
import com.radiusnetworks.ibeacon.IBeaconManager;
import com.radiusnetworks.ibeacon.MonitorNotifier;
import com.radiusnetworks.ibeacon.RangeNotifier;
import com.radiusnetworks.ibeacon.Region;

import java.util.Collection;
import java.util.Iterator;


public class MyActivity extends Activity implements IBeaconConsumer {
    ImageView imageView;
    IBeaconManager iBeaconManager;
    IBeacon oldBeacon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        imageView = (ImageView) findViewById(R.id.appView);
        iBeaconManager = IBeaconManager.getInstanceForApplication(this);
        iBeaconManager.bind(this);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        iBeaconManager.unBind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private IBeacon returnClosest(Collection<IBeacon>iBeacons){
        IBeacon tempBeacon = null;
        Iterator<IBeacon>iBeaconIterator = iBeacons.iterator();
        double closest = 100000d;

        while(iBeaconIterator.hasNext()){
            IBeacon iBeacon = iBeaconIterator.next();
            Log.e("Distance Measurement Outer",iBeacon.getMinor()+" - "+iBeacon.getAccuracy());
            if(iBeacon.getAccuracy() < 5) {
                if (iBeacon.getAccuracy() < closest) {
                    closest = iBeacon.getAccuracy();
                    tempBeacon = iBeacon;
                    oldBeacon = tempBeacon;
                }
            }

        }
        if(tempBeacon == null){
            if(oldBeacon!=null) {
                tempBeacon = oldBeacon;
            }else{
                tempBeacon = new IBeacon("UUUDS",100,1);
            }
        }
        return tempBeacon;
    }

    @Override
    public void onIBeaconServiceConnect() {
        iBeaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<IBeacon> iBeacons, Region region) {
                    IBeacon temp = returnClosest(iBeacons);
                    if(temp.getMinor() == 3) {
                        Log.e("Distance Measurement", temp.getMinor() + "is closest");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Detected beacon", Toast.LENGTH_LONG).show();
                                imageView.setImageResource(R.drawable.pic1);
                            }
                        });


                    }
                    if(temp.getMinor() == 4){
                        Log.e("Distance Measurement",temp.getMinor()+"is closest");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"Detected beacon",Toast.LENGTH_LONG).show();
                                imageView.setImageResource(R.drawable.pic2);
                            }
                        });

                    }

                    if(temp.getMinor() == 5){
                        Log.e("Distance Measurement",temp.getMinor()+"is closest");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"Detected beacon",Toast.LENGTH_LONG).show();
                                imageView.setImageResource(R.drawable.pic3);
                            }
                        });

                    }
                }

        });



        try{
            iBeaconManager.startRangingBeaconsInRegion(new Region("myUniqueId",null,null,null));
        }catch(RemoteException e){
            e.printStackTrace();
        }
    }
}
