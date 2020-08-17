import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CalculateShortestPath {

	public static class ShortestPath {
		int distance;
		int[] path;
	}
	static ShortestPath shortestPath = null;

	public static void main(String[] args) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode destinations = mapper.readTree(CalculateShortestPath.class.getResourceAsStream("destinations.json"));
		System.out.println(destinations.size());
		
		int[][] distances = new int[destinations.size()][destinations.size()];
		for (int i=0; i<distances.length; ++i) {
			for (int j=i; j<distances.length; ++j) {
				int distance = (int) Haversine.distance(destinations.get(i).get("lat").asDouble(), destinations.get(i).get("lon").asDouble(), destinations.get(j).get("lat").asDouble(), destinations.get(j).get("lon").asDouble());
				String startPlaceName = destinations.get(i).get("place").toString().replace("\"", "");
				String endPlaceName = destinations.get(j).get("place").toString().replace("\"", "");
				if ((startPlaceName.equals("furthest-apart-south") && endPlaceName.equals("most-northern")) ||
						(endPlaceName.equals("furthest-apart-south") && startPlaceName.equals("most-northern"))) {
					distance=0;
				}
				distances[i][j] = distance;
				distances[j][i] = distance;
			}
		}
		
		for (int i=0; i<distances.length; ++i) {
			for (int j=0; j<distances.length; ++j) {
				System.out.print(distances[i][j] + " ");
			}
			System.out.println();
		}
		System.exit(0);
//		System.out.println(distances);
		calculateShortestPath(distances);
		
		System.out.println(distances[0][0]);
		System.out.println(shortestPath.distance);
	}

	private static ShortestPath calculateShortestPath(int[][] distances) {
		boolean[] visited = new boolean[distances.length];
		visited[0] = true;
		int[] currentPath = new int[distances.length];
		currentPath[0]=0;
		CalculateShortestPath.calculateShortestPath(distances, 0, visited, 0, 1, currentPath);
		return shortestPath;
	}

	private static void calculateShortestPath(int[][] distances, int startPosition, boolean[] visited, int distance, int depth, int[] currentPath) {
		if (depth == distances.length) {
			// we have reached the end of a loop construct the shortest path for this end
			ShortestPath p = new ShortestPath();
			p.distance = distance + distances[startPosition][0];
			p.path = currentPath.clone();
			if (shortestPath == null || (p.distance < shortestPath.distance)) {
				shortestPath = p;
				System.out.println(shortestPath.distance);
			}
			return;
		}
		
		// there is still some way to go
		for (int endPosition=0; endPosition < distances.length; ++endPosition) {
			if (!visited[endPosition]) {
				visited[endPosition] = true;
				int newDistance = distance + distances[startPosition][endPosition];
				currentPath[depth] = endPosition;
				CalculateShortestPath.calculateShortestPath(distances, endPosition, visited, newDistance, depth+1, currentPath);
				visited[endPosition] = false;
			}
			if (depth==11) {
				System.out.println();
			} else if (depth == 12) {
				System.out.print(".");
			}
		}
	}

}