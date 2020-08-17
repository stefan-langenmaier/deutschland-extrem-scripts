import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.Route;
import io.jenetics.jpx.WayPoint;

public class GenerateGpx {

	public static void main(String[] args) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		
		JsonNode destinations = mapper.readTree(GenerateGpx.class.getResourceAsStream("destinations.json"));
		System.out.println(destinations.size());
		
		Scanner scanner = new Scanner(GenerateGpx.class.getResourceAsStream("concorde-tsp.sol"));
		
		if (destinations.size() != scanner.nextInt()) {
			System.out.println("destinations and solution don't match");
			System.exit(1);
		}
		
		Route route = Route.builder()
			.name("shortest-extreme-route")
			.build();
		GPX gpx = GPX.builder().addRoute(route).build();
		
		while (scanner.hasNext()) {
			int point = scanner.nextInt();
			String name = destinations.get(point).get("place").asText();
			double lat = destinations.get(point).get("lat").asDouble();
			double lon = destinations.get(point).get("lon").asDouble();
			WayPoint wayPoint = WayPoint.builder().name(name).lat(lat).lon(lon).build();
			route = route.toBuilder().addPoint(wayPoint).build();
			gpx = gpx.toBuilder().addWayPoint(wayPoint).build();
			
		}
		int point = 0;
		String name = destinations.get(point).get("place").asText();
		double lat = destinations.get(point).get("lat").asDouble();
		double lon = destinations.get(point).get("lon").asDouble();
		WayPoint wayPoint = WayPoint.builder().name(name).lat(lat).lon(lon).build();
		route = route.toBuilder().addPoint(wayPoint).build();
		
		gpx = gpx.toBuilder().addRoute(route).build();
		
		FileOutputStream file = new FileOutputStream("route.gpx");
		GPX.write(gpx, file);

	}

}
