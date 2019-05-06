package Algorithms;

public class City implements Comparable<City> {
	int city;
	double distance;

	public City(int city, double distance) {
		this.city = city;
		this.distance = distance;
	}

	public int compareTo(City x) {
		return Double.compare(this.distance, x.distance);
	}

	public Double getDistance() {
		Double temp = new Double(this.distance);
		return temp;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
}