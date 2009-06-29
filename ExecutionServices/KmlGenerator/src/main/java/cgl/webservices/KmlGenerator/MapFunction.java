package cgl.webservices.KmlGenerator;

public class MapFunction {

	/*
	 * distance of twoo points unit( metres)
	 */

	public static double DistanceOfTwoPoints(double lng1, double lat1,
			double lng2, double lat2) {
		double radLat1 = Rad(lat1);
		double radLat2 = Rad(lat2);
		double a = radLat1 - radLat2;
		double b = Rad(lng1) - Rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * 6378137.0;// The long axis of WGS84 ball
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	private static double Rad(double d) {
		return d * Math.PI / 180.0;
	}

	/*
	 * Gauss projection calculation function Beijing axis from
	 * latitude/longitude to surface coordinate( unit:Metres )
	 */
	// Gauss projection calculation function
	// 
	public static Coordinate GaussProjCal(double longitude, double latitude) {
		int ProjNo = 0;
		int ZoneWide; // //bandwidth
		double X, Y;
		double longitude1, latitude1, longitude0, latitude0, X0, Y0, xval, yval;
		double a, f, e2, ee, NN, T, C, A, M, iPI;
		iPI = 0.0174532925199433; // //3.1415926535898/180.0;
		ZoneWide = 6;
		a = 6378245.0;
		f = 1.0 / 298.3; // Beijing geodetic coordinate system 1954
		ProjNo = (int) (longitude / ZoneWide);
		longitude0 = ProjNo * ZoneWide + ZoneWide / 2;
		longitude0 = longitude0 * iPI;
		latitude0 = 0;
		longitude1 = longitude * iPI; // logitude to rad
		latitude1 = latitude * iPI; // latitude to rad
		e2 = 2 * f - f * f;
		ee = e2 * (1.0 - e2);
		NN = a
				/ Math.sqrt(1.0 - e2 * Math.sin(latitude1)
						* Math.sin(latitude1));
		T = Math.tan(latitude1) * Math.tan(latitude1);
		C = ee * Math.cos(latitude1) * Math.cos(latitude1);
		A = (longitude1 - longitude0) * Math.cos(latitude1);
		M = a
				* ((1 - e2 / 4 - 3 * e2 * e2 / 64 - 5 * e2 * e2 * e2 / 256)
						* latitude1
						- (3 * e2 / 8 + 3 * e2 * e2 / 32 + 45 * e2 * e2 * e2
								/ 1024) * Math.sin(2 * latitude1)
						+ (15 * e2 * e2 / 256 + 45 * e2 * e2 * e2 / 1024)
						* Math.sin(4 * latitude1) - (35 * e2 * e2 * e2 / 3072)
						* Math.sin(6 * latitude1));
		xval = NN
				* (A + (1 - T + C) * A * A * A / 6 + (5 - 18 * T + T * T + 72
						* C - 58 * ee)
						* A * A * A * A * A / 120);
		yval = M
				+ NN
				* Math.tan(latitude1)
				* (A * A / 2 + (5 - T + 9 * C + 4 * C * C) * A * A * A * A / 24 + (61
						- 58 * T + T * T + 600 * C - 330 * ee)
						* A * A * A * A * A * A / 720);
		X0 = 1000000L * (ProjNo + 1) + 500000L;
		Y0 = 0;
		xval = xval + X0;
		yval = yval + Y0;
		X = xval;
		Y = yval;
		Coordinate xy = new Coordinate();
		xy.setX(X);
		xy.setY(Y);
		return xy;

	}

	/*
	 * Gauss projection invert function surface coordinate(unit:metres) to
	 * latitude and longitude.
	 */
	public static Coordinate GaussProjInvCal(double X, double Y) {
		double longitude, latitude;
		int ProjNo;
		int ZoneWide; // bandwidth
		double longitude1, latitude1, longitude0, latitude0, X0, Y0, xval, yval;
		double e1, e2, f, a, ee, NN, T, C, M, D, R, u, fai, iPI;
		iPI = 0.0174532925199433; // //3.1415926535898/180.0;
		a = 6378245.0;
		f = 1.0 / 298.3; // Beijing geodetic coodinate system in 1954
		ZoneWide = 6; // //6 bandwidth
		ProjNo = (int) (X / 1000000L); // get
		longitude0 = (ProjNo - 1) * ZoneWide + ZoneWide / 2;
		longitude0 = longitude0 * iPI;
		X0 = ProjNo * 1000000L + 500000L;
		Y0 = 0;
		xval = X - X0;
		yval = Y - Y0; // surface coordinate
		e2 = 2 * f - f * f;
		e1 = (1.0 - Math.sqrt(1 - e2)) / (1.0 + Math.sqrt(1 - e2));
		ee = e2 / (1 - e2);
		M = yval;
		u = M / (a * (1 - e2 / 4 - 3 * e2 * e2 / 64 - 5 * e2 * e2 * e2 / 256));
		fai = u + (3 * e1 / 2 - 27 * e1 * e1 * e1 / 32) * Math.sin(2 * u)
				+ (21 * e1 * e1 / 16 - 55 * e1 * e1 * e1 * e1 / 32)
				* Math.sin(4 * u) + (151 * e1 * e1 * e1 / 96) * Math.sin(6 * u)
				+ (1097 * e1 * e1 * e1 * e1 / 512) * Math.sin(8 * u);
		C = ee * Math.cos(fai) * Math.cos(fai);
		T = Math.tan(fai) * Math.tan(fai);
		NN = a / Math.sqrt(1.0 - e2 * Math.sin(fai) * Math.sin(fai));
		R = a
				* (1 - e2)
				/ Math.sqrt((1 - e2 * Math.sin(fai) * Math.sin(fai))
						* (1 - e2 * Math.sin(fai) * Math.sin(fai))
						* (1 - e2 * Math.sin(fai) * Math.sin(fai)));
		D = xval / NN;
		// calculate (Longitude) and (Latitude)
		longitude1 = longitude0
				+ (D - (1 + 2 * T + C) * D * D * D / 6 + (5 - 2 * C + 28 * T
						- 3 * C * C + 8 * ee + 24 * T * T)
						* D * D * D * D * D / 120) / Math.cos(fai);
		latitude1 = fai
				- (NN * Math.tan(fai) / R)
				* (D * D / 2 - (5 + 3 * T + 10 * C - 4 * C * C - 9 * ee) * D
						* D * D * D / 24 + (61 + 90 * T + 298 * C + 45 * T * T
						- 256 * ee - 3 * C * C)
						* D * D * D * D * D * D / 720);
		longitude = longitude1 / iPI;
		latitude = latitude1 / iPI;
		Coordinate lonlat = new Coordinate();
		lonlat.setX(longitude);
		lonlat.setY(latitude);
		return lonlat;
	}

	public static Coordinate MercatorProject(double lon, double lat) {
		// do magic stuff
		double p[] = new double[2];

		double rx = (Math.PI / 180d) * lon;
		double ry = (Math.PI / 180d) * lat;
		p[0] = rx;
		p[1] = Math.log(Math.tan(ry) + (1 / Math.cos(ry)));
		p[0] = p[0] * 180d / Math.PI;
		p[1] = p[1] * 180d / Math.PI;
		// System.out.println(lon+","+lat+" "+p[0]+","+p[1]);
		Coordinate xy = new Coordinate();
		xy.setX(p[0]);
		xy.setY(p[1]);
		return xy;
	}

	public static Coordinate MercatorUnproject(double x, double y) {
		double p[] = new double[2];

		double rx = (Math.PI / 180d) * x;
		double ry = (Math.PI / 180d) * y;
		p[0] = rx;
		p[1] = 2 * Math.atan(Math.pow(Math.E, ry)) - (Math.PI / 2d);

		p[0] = p[0] * 180d / Math.PI;
		p[1] = p[1] * 180d / Math.PI;
		// System.out.println(lon+","+lat+" "+p[0]+","+p[1]);
		Coordinate lonlat = new Coordinate();
		lonlat.setX(p[0]);
		lonlat.setY(p[1]);
		return lonlat;
	}

	/*
	 * dlat2xy Calling details: takes @ref = ($lon, $lat) and arrays @lons,
	 * @lats produces arrays @xs, @ys by reference, eg. my (@lons, @lats,@xs,
	 * @ys); my @ref = ($lonref,$latref)= split ' ',<>; ($xsref, $ysref) =
	 * Lxy::dlat2xy(\@ref,\@lons,\@lats); @xs = @$xsref; @ys = @$ysref;
	 */
	public static Coordinate dlat2xy(double lon, double lat, double lonref,
			double latref) {
		double sinlon, xfactor, x, y, len, str;
		double eqrad = 6378.139*1000;
		double flattening = 1.0 / 298.247;
		double yfactor = 111.32*1000;

		sinlon = Math.sin(Math.toRadians(lonref));
		xfactor = Math.toRadians(1.0) * Math.cos(Math.toRadians(latref))
				* eqrad * (1.0 - sinlon * sinlon * flattening);
		x = (lon - lonref) * xfactor;
		y = (lat - latref) * yfactor;
		len = Math.sqrt(x * x + y * y);
		str = Math.atan2(x, y);
		str = Math.toDegrees(str);
		Coordinate xy = new Coordinate();
		xy.setX(x);
		xy.setY(y);
		return xy;
	}

	/*
	 * dxy2lat Calling details: takes @ref = ($lon, $lat) and arrays @xs, @ys
	 * gives lon, lat list from a set of x, y points, where x, y are distances
	 * in km from ref point. Calling sequence just as dlat2xy above, reversing
	 * x, y with lon, lat @ref is ($lon, $lat) exactly as above: my @ref =
	 * ($lonref,$latref)= split ' ',<>; ($lonsref, $latsref) =
	 * Lxy::dxy2lat(\@ref,\@xs,\@ys)
	 */
	public static Coordinate dxy2lat(double x, double y, double lonref,
			double latref) {
		double sinlon, xfactor, len, str;
		double eqrad = 6378.139*1000;
		double flattening = 1.0 / 298.247;
		double yfactor = 111.32*1000;
		sinlon = Math.sin(Math.toRadians(lonref));
		xfactor = Math.toRadians(1.0) * Math.cos(Math.toRadians(latref))
				* eqrad * (1.0 - sinlon * sinlon * flattening);
		double lon = x / xfactor + lonref;
		double lat = y / yfactor + latref;
		len = Math.sqrt(x * x + y * y);
		str = Math.atan2(x, y);
		str = Math.toDegrees(str);
		Coordinate lonlat = new Coordinate();
		lonlat.setX(lon);
		lonlat.setY(lat);
		return lonlat;

	}

}
