package belajar.belajar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class BelajarandroidActivity extends Activity{
	
	private ListView list_spbu;
	private DatabaseHelper db;
	private EditText search_text;
	private ListAdapter adapter;
	private Activity that;
	private LocationManager locationManager;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.test);
        db = new DatabaseHelper(this);
        list_spbu = (ListView)findViewById(R.id.listView1);
        search_text = (EditText)findViewById(R.id.editText1);
        
        pastipas_search();
        
        Button button_search = (Button)findViewById(R.id.button1);
        button_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pastipas_search();
			}
		});
        
        this.that = this;
        
        list_spbu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(that,DetailActivity.class);
		        Cursor cursor = (Cursor) adapter.getItem(arg2);
		        intent.putExtra("SPBU_ID", cursor.getInt(cursor.getColumnIndex("_id")));
				startActivity(intent);
			}
        	
		});
        
        
      locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

      LocationListener locationListener = new LocationListener() {
    	 public void onLocationChanged(Location location) {
           // Called when a new location is found by the network location provider.
        	// search_text.setText(""+location.getLatitude());
         }

         public void onStatusChanged(String provider, int status, Bundle extras) {}

         public void onProviderEnabled(String provider) {}

         public void onProviderDisabled(String provider) {}
      };

      Criteria criteria = new Criteria();
      criteria.setAccuracy(Criteria.ACCURACY_FINE);
      String provider_name = locationManager.getBestProvider(criteria, true);
      //Location lastKnownLocation = locationManager.getLastKnownLocation(provider_name);
      Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
      //search_text.setText(""+lastKnownLocation.getLatitude());
      locationManager.requestLocationUpdates(provider_name, 0, 0, locationListener);
    }
    
    private void pastipas_search(){
    	String q = search_text.getText().toString();
    	Cursor cursor = db.getReadableDatabase().rawQuery("select _id, no_spbu , alamat||' '||ref_gedung||' '||kota||' '||propinsi as alamat"+
    											" FROM pastipas WHERE no_spbu LIKE ? OR alamat LIKE ? OR kota LIKE ? OR ref_gedung LIKE ? OR propinsi LIKE ? LIMIT 10", 
                new String[]{"%"+q+"%","%" + q + "%","%" + q + "%","%" + q + "%","%" + q + "%"});
    	
		adapter = new SimpleCursorAdapter(this,R.layout.spbuitem, cursor, 
   					new String[] {"no_spbu","alamat"}, new int[]{R.id.nama,R.id.alamat} );
		list_spbu.setAdapter(adapter);
		//cursor.close();
    }

}