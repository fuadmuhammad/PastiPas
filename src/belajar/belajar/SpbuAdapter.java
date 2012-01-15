package belajar.belajar;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SpbuAdapter extends ArrayAdapter<String>{
	private Map<String,List<String>> map;
	
	public SpbuAdapter(Context context,Map<String,List<String>> map){
		super(context,R.layout.spbuitem, R.id.nama, map.keySet().toArray(new String[]{}));
		this.map = map;
	}
	
	public View getView(int pos, View convertView, ViewGroup parent){
		View row = super.getView(pos, convertView, parent);
		String key = super.getItem(pos);
		List<String> entry = map.get(key);
		
		TextView alamat_view = (TextView)row.findViewById(R.id.alamat);
		alamat_view.setText(entry.get(0));
		
		TextView jarak_view = (TextView)row.findViewById(R.id.jarak);
		jarak_view.setText("Jarak : "+entry.get(1)+" KM");
		
		return row;
		
	}
}
