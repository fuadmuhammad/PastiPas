package belajar.belajar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class BelajarandroidActivity extends Activity{
	
	private ListView list_spbu;
	private DatabaseHelper db;
	private EditText search_text;
	//private ListAdapter adapter;
	//private Activity that;
	private LocationManager locationManager;
	private double curr_lat = 0 ;
	private double curr_long = 0;
	private TreeMap<String,List<String>> displayed_data;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.test);
        db = new DatabaseHelper(this);
        list_spbu = (ListView)findViewById(R.id.daftar);
        search_text = (EditText)findViewById(R.id.editText1);
        
        Button button_search = (Button)findViewById(R.id.button1);
        button_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pastipas_search();
			}
		});
        
        Button terdekat_search = (Button)findViewById(R.id.button2);
        terdekat_search.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		terdekat_search();
        	}
        });
        
        //this.that = this;
        
        /*list_spbu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(that,DetailActivity.class);
		        Cursor cursor = (Cursor) adapter.getItem(arg2);
		        intent.putExtra("SPBU_ID", cursor.getInt(cursor.getColumnIndex("_id")));
				startActivity(intent);
			}
        	
		});*/
        
        
      locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

      LocationListener locationListener = new LocationListener() {
    	 public void onLocationChanged(Location location) {
           // Called when a new location is found by the network location provider.
        	//search_text.setText(""+location.getLatitude());
        	curr_lat = location.getLatitude();
        	curr_long = location.getLongitude();
         }

         public void onStatusChanged(String provider, int status, Bundle extras) {}

         public void onProviderEnabled(String provider) {}

         public void onProviderDisabled(String provider) {}
      };

      Criteria criteria = new Criteria();
      //criteria.setAccuracy(Criteria.ACCURACY_FINE);
      String provider_name = locationManager.getBestProvider(criteria, true);
      Location lastKnownLocation = locationManager.getLastKnownLocation(provider_name);
      //Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
      //search_text.setText(""+lastKnownLocation.getLatitude());
      this.curr_lat = lastKnownLocation.getLatitude();
      this.curr_long = lastKnownLocation.getLongitude();
      locationManager.requestLocationUpdates(provider_name, 0, 0, locationListener);
      
      displayed_data = (TreeMap<String,List<String>>)getLastNonConfigurationInstance();
      
      if(displayed_data == null){
    	  terdekat_search();  
      }else{
    	  list_spbu.setAdapter(new SpbuDistanceAdapter(this,displayed_data));
      }
    }
    
    private void pastipas_search(){
    	String q = search_text.getText().toString();
    	if(q.trim().length()==0){
    		return;
    	}
    	Cursor cursor = db.getReadableDatabase().rawQuery("select _id, no_spbu , alamat||' '||ref_gedung||' '||kota||' '||propinsi as alamat, latitude, longitude"+
    											" FROM pastipas WHERE no_spbu LIKE ? OR alamat LIKE ? OR kota LIKE ? OR ref_gedung LIKE ? OR propinsi LIKE ? LIMIT 10 ", 
                new String[]{"%"+q+"%","%" + q + "%","%" + q + "%","%" + q + "%","%" + q + "%"});
    	
    	
    	
    	DecimalFormat df = new DecimalFormat("0.00");
    	displayed_data = new TreeMap<String,List<String>>();
    	while(cursor.moveToNext()){
    		double latitude = cursor.getDouble(3);
    		double longitude = cursor.getDouble(4);
    		Double jarak_result = DistanceAlgorithm.distance(this.curr_long, this.curr_lat, longitude, latitude);
    		
    		if(jarak_result.isNaN()){
    			continue;
    		}
    		List<String> l = new ArrayList<String>();
    		l.add(cursor.getString(1));
    		l.add(cursor.getString(2));
    		displayed_data.put(df.format(jarak_result), l);
    	}
    	
    	list_spbu.setAdapter(new SpbuDistanceAdapter(this,displayed_data));
    }
    
    private void terdekat_search(){
    	if(this.curr_lat ==0 || this.curr_long ==0){
    		return;
    	}
    	Cursor cursor = db.getReadableDatabase().rawQuery("select _id, no_spbu , alamat||' '||ref_gedung||' '||kota||' '||propinsi as alamat, latitude, longitude FROM pastipas",new String[]{});
    	    	
    	TreeMap<Double,List<String>> m= new TreeMap<Double,List<String>>();
    	DecimalFormat df = new DecimalFormat("0.00");
    	while(cursor.moveToNext()){
    		double latitude = cursor.getDouble(3);
    		double longitude = cursor.getDouble(4);
    		Double jarak_result = DistanceAlgorithm.distance(this.curr_long, this.curr_lat, longitude, latitude);
    		
    		if(jarak_result.isNaN()){
    			continue;
    		}
    		
    		List<String> l = new ArrayList<String>();
    		l.add(cursor.getString(1));
    		l.add(cursor.getString(2));
    		m.put(jarak_result, l);
    	}
    	
    	
    	int i =1;
    	displayed_data = new TreeMap<String,List<String>>();
    	for(Double key:m.keySet()){
    		List<String> entry = m.get(key);
    		
    		List<String> l = new ArrayList<String>();
    		l.add(entry.get(0));
    		l.add(entry.get(1));
    		displayed_data.put(df.format(key), l);
    		i++;
    		if(i>10){
    			break;
    		}
    	}
    	
    	
    	list_spbu.setAdapter(new SpbuDistanceAdapter(this,displayed_data));
    	
    }
    
    public Object onRetainNonConfigurationInstance() {
        return displayed_data;
    }

}