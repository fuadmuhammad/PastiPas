package belajar.belajar;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SpbuDistanceAdapter extends ArrayAdapter<String>{
	private Map<String,List<String>> map;
	
	public SpbuDistanceAdapter(Context context,Map<String,List<String>> map){
		super(context,R.layout.spbuitem, R.id.jarak, map.keySet().toArray(new String[]{}));
		this.map = map;
	}
	
	public View getView(int pos, View convertView, ViewGroup parent){
		View row = super.getView(pos, convertView, parent);
		String key = super.getItem(pos);
		List<String> entry = map.get(key);
		
		TextView nama_view = (TextView)row.findViewById(R.id.nama);
		nama_view.setText(entry.get(0));
		
		TextView alamat_view = (TextView)row.findViewById(R.id.alamat);
		alamat_view.setText(entry.get(1));
		
		TextView jarak_view = (TextView)row.findViewById(R.id.jarak);
		jarak_view.setText("Jarak : "+key+" KM");
		
		return row;
		
	}
}
