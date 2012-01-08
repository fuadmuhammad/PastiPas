package belajar.belajar;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends Activity {
	
	private int spbu_id;
	private DatabaseHelper db;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailspbu);
		db = new DatabaseHelper(this);
		
		spbu_id = getIntent().getIntExtra("SPBU_ID",0);
	    Cursor cursor = db.getReadableDatabase().rawQuery("SELECT _ID, nama, alamat FROM pastipas WHERE _ID = ?", 
	                                new String[]{""+spbu_id});
	    
	    if (cursor.getCount() == 1){
	    	cursor.moveToFirst();
	    	TextView nama = (TextView)findViewById(R.id.nama);
	    	nama.setText(cursor.getString(cursor.getColumnIndex("nama")));
	    	TextView alamat = (TextView)findViewById(R.id.alamat);
	    	alamat.setText(cursor.getString(cursor.getColumnIndex("alamat")));
	    }
        
	}
}
