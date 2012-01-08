package belajar.belajar;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME="db_pastipas";

	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, 2);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {		
		db.execSQL("CREATE TABLE pastipas (_id INTEGER PRIMARY KEY AUTOINCREMENT, no_spbu TEXT, ref_gedung TEXT, propinsi TEXT, alamat TEXT, kota TEXT, latitude REAL, longitude REAL);");
		
		HttpClient httpclient = new DefaultHttpClient();  
        HttpGet request = new HttpGet("https://api.scraperwiki.com/api/1.0/datastore/sqlite?format=jsondict&name=pastipas_1&query=select%20*%20from%20%60swdata%60%20order%20by%20no_spbu");  
        ResponseHandler<String> handler = new BasicResponseHandler();
        try {  
            String result = httpclient.execute(request, handler);
            JSONArray json_arr = new JSONArray(result);
            for(int i =0;i<json_arr.length();i++){
            	JSONObject jsonObject = json_arr.getJSONObject(i);
            	ContentValues cv = new ContentValues();
            	cv.put("no_spbu", jsonObject.getString("no_spbu"));
            	cv.put("ref_gedung", jsonObject.getString("ref_gedung"));
            	cv.put("propinsi",jsonObject.getString("propinsi"));
            	cv.put("alamat", jsonObject.getString("alamat"));
            	cv.put("kota", jsonObject.getString("kota"));
            	cv.put("latitude",jsonObject.getDouble("latitude"));
            	cv.put("longitude", jsonObject.getDouble("longitude"));
            	db.insert("pastipas","ref_gedung", cv);
            }            
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  catch (JSONException e) {  
            e.printStackTrace();
        }
        httpclient.getConnectionManager().shutdown();

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS pastipas");
        onCreate(db);
	}

}
