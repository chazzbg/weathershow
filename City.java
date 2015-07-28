package weathershow;

public class City {

	private String city;

	public City() {
	}

	public City(String City) {
		this.city = City;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String City) {
		this.city = City;
	}

	@Override
	public String toString() {
		return "City{city=" + this.city + '}';
	}
}