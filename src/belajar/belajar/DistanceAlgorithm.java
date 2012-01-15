package belajar.belajar;

public class DistanceAlgorithm {
	
	public static double deg_2_rad(double deg){
		return (deg * Math.PI / 180);
	}
	
	public static double distance(double lon1, double lat1, double lon2, double lat2){
		double R = 6371; //Earth Radius
		double d_lon = deg_2_rad(lon2-lon1);
		double d_lat = deg_2_rad(lat2-lat1);
		double a = Math.sin(d_lat/2) * Math.sin(d_lat/2) + Math.sin(d_lon/2) * Math.sin(d_lon/2) * Math.cos(lat1) * Math.cos(lat2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		return R*c; 
	}
}
