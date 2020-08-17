import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CalculateLoneliestPlace {

	public static void main(String[] args) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode searchFile = mapper
				.readTree(CalculateLoneliestPlace.class.getResourceAsStream("places-search-s.json"));
		JsonNode candidatesFile = mapper
				.readTree(CalculateLoneliestPlace.class.getResourceAsStream("places-candidates-s.json"));
		JsonNode search = searchFile.get("elements");
		JsonNode candidates = candidatesFile.get("elements");

		double maxDistance = -1;
		long pId = -1;
		int ctrCandidates = candidates.size();
		System.out.println(ctrCandidates);
		int ctr = 0;
		for (JsonNode place : candidates) {
			double minDistance = 100; // km
			//if (place.get("id").asInt() == 26066670) continue;
			for (JsonNode c : search) {
				if (c.get("id").equals(place.get("id"))) continue;
				
				double distance = Haversine.distance(place.get("lat").asDouble(), place.get("lon").asDouble(),
						c.get("lat").asDouble(), c.get("lon").asDouble());
				if (distance < minDistance) {
					minDistance = distance;
				}
			}
			if (minDistance > maxDistance) {
				maxDistance = minDistance;
				pId = place.get("id").asLong();
				System.out.println(maxDistance);
				System.out.println(pId);
			}
			if (minDistance > 7) {
				System.out.println(">7km");
				System.out.println(maxDistance);
				System.out.println(pId);
			}
			
			++ctr;
			if (ctr % 1000 == 0) {
				System.out.print(".");		
			}
		}
		System.out.println(maxDistance);
		System.out.println(pId);
	}

}
