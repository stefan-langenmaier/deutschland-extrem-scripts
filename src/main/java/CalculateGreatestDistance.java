import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CalculateGreatestDistance {

	public static void main(String[] args) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode counties = mapper.readTree(CalculateGreatestDistance.class.getResourceAsStream("counties.json"));
		JsonNode countiesList = counties.get("elements");
		
		double maxDistance=-1;
		int startId=-1;
		int endId=-1;
		for (JsonNode startCounty : countiesList) {
			for (JsonNode endCounty : countiesList) {
				double distance = Haversine.distance(startCounty.get("center").get("lat").asDouble(), startCounty.get("center").get("lon").asDouble(), endCounty.get("center").get("lat").asDouble(), endCounty.get("center").get("lon").asDouble());
				if (distance > maxDistance) {
					System.out.println(distance);
					maxDistance = distance;
					startId = startCounty.get("id").asInt();
					endId = endCounty.get("id").asInt();
				}
			}
			System.out.println(".");
		}
		System.out.println(maxDistance);
		System.out.println(startId);
		System.out.println(endId);
	}

}
