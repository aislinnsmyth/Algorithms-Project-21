//Aspects of of code adapted from Algorithms 4th edition, authors being Robert Sedgewick and Kevin Wayne.


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class DijkstraShortestPath {

	private static double distTo[];							//distance from source vertex to destination vertex
	private static DirectedEdge[] edgeTo;					//edgeTo[v] = last edge on shortest s->w path
	private static IndexMinPQ<Double> pq;					//priority queue class
	static ArrayList<Stops> stop = new ArrayList<>();		//Array list
	ArrayList<Stops> s = new ArrayList<>();
	static ArrayList<Trips> list = new ArrayList<>();		//Array list to loop through the stop times
	static EdgeWeightedDiagraph graph;
	private static String busStops = "C:\\Users\\aisli\\OneDrive\\Documents\\CSYear2\\Algorithm\\stops.txt";
	private static String stopTimes = "C:\\Users\\aisli\\OneDrive\\Documents\\CSYear2\\Algorithm\\stop_times.txt";
	private static String transfers = "C:\\Users\\aisli\\OneDrive\\Documents\\CSYear2\\Algorithm\\transfers.txt";


	public DijkstraShortestPath() {

	}

	private static void ShortestPath(EdgeWeightedDiagraph G, int s) {
		distTo = new double[G.V()];
		edgeTo= new DirectedEdge[G.V()];

		for(int v=0; v< G.V(); v++) {
			distTo[v] = Double.POSITIVE_INFINITY;
			distTo[s] = 0.0;
		}
		// relax vertices in order of distance from s
		pq = new IndexMinPQ<Double>(G.V());
		pq.insert(s, distTo[s]);
		while (!pq.isEmpty()) {
			int v = pq.delMin();
			for (DirectedEdge e : G.adj(v))
				relax(e);
		}
	}
	// relax edge e and update pq if changed
	private static void relax(DirectedEdge e) {
		int v = e.from(), w = e.to();
		if (distTo[w] > distTo[v] + e.weight()) {
			distTo[w] = distTo[v] + e.weight();
			edgeTo[w] = e;
			if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
			else                pq.insert(w, distTo[w]);
		}


	}

	public static Iterable<DirectedEdge> pathTo(int v) {
		// graph.validateVertex(v);
		// if (!hasPathTo(v)) return null;
		Stack<DirectedEdge> path = new Stack<DirectedEdge>();
		for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
			path.push(e);
		}
		return path;
	}

	public static ArrayList<Integer> getEnrouteStops(int v){
		ArrayList<Integer> res = new ArrayList<>();
		Iterable<DirectedEdge> path = pathTo(v);
		res.add(v);
		for(DirectedEdge e : path) {
			res.add(e.from());
		}
		Collections.reverse(res);
		return res;
	}

	public static double distTo(int v) {
		return distTo[v];
	}

	public static int findMaxStopID() {
		int max=0;
		for(Stops s: stop) {
			if(max <= s.stop_id)
				max=s.stop_id +1;
		}
		return max;
	}


	public static void initialising() {

		try {
			readStops(busStops);
			graph = new EdgeWeightedDiagraph(findMaxStopID());
			for(Stops s : stop){
				DirectedEdge d = new DirectedEdge(s.stop_id,s.stop_id,0);
				graph.addEdge(d);
			}
			System.out.println("Reading stops: " + stop.size());
			readTransfers(transfers);
			ReadStopTimes(stopTimes);
			int size = list.size();
			for(int i=0; i<size-1; i++) {
				Trips trip1 = list.get(i);
				Trips trip2 = list.get(i+1);

				if(trip1.trip_id == trip2.trip_id) {
					DirectedEdge newEdge = new DirectedEdge(trip1.stop_id, trip2.stop_id, 1);
					graph.addEdge(newEdge);
				}
			}
			System.out.println("Graph setup complete");
		} catch (IOException e) {
		}
	}


	private static void readStops(String busStops) throws IOException {
		BufferedReader buffer = new BufferedReader(new FileReader(busStops)); 
		String st;

		while((st = buffer.readLine()) != null) { 
			String[] line = st.split(",");
			if (!line[1].equals("stop_code")) {


				int stop_id = Integer.parseInt(line[0]);
				int stop_code = (line[1].equals(" ")) ? -1 : Integer.parseInt(String.valueOf(line[1]));
				String stop_name = line[2];
				String stop_desc = line[3];
				double stop_lat = Double.parseDouble(line[4]);
				double stop_lon = Double.parseDouble(line[5]);
				String zone_id = line[6];
				String stop_url = line[7];
				int location_type = Integer.parseInt(line[8]);
				String parent_station = (line.length == 9) ? "" : line[9];
				Stops s = new Stops(stop_id, stop_code, stop_name, stop_desc, stop_lat,
						stop_lon,zone_id,stop_url,location_type, parent_station);
				stop.add(s);
			}
		} 
		buffer.close();

	}

	private static void readTransfers(String transfers) throws IOException {
		BufferedReader buffer = new BufferedReader(new FileReader(transfers)); 
		String sr;
		int count =0;
		while((sr = buffer.readLine()) != null) { 
			String[] line1 = sr.split(",");
			if(!line1[1].equals("to_stop_id")) {
				int from_stop_id = Integer.parseInt(line1[0]);
				int to_stop_id = Integer.parseInt(line1[1]);
				int transfer_type = Integer.parseInt(line1[2]);
				double min_transfer_time = (line1.length == 3) ? -100 : Double.parseDouble(line1[3]);
				double cost = 0;
				if(transfer_type == 2)  {
					cost = min_transfer_time /100;
				}
				else if(transfer_type == 0) {
					cost = 2;
				}

				DirectedEdge edge = new DirectedEdge(from_stop_id, to_stop_id, cost);
				graph.addEdge(edge);
				count++;
			}
		}
		System.out.println("Reading transfers: " + count);
		buffer.close();

	}

	private static void ReadStopTimes(String stopTimes) throws IOException {
		BufferedReader buffer = new BufferedReader(new FileReader(stopTimes)); 
		String sl;
		int count = 0;
		while((sl = buffer.readLine()) != null) { 
			String[] line2 = sl.split(",");
			if(!line2[1].equals("arrival_time")) {
				int trip_id = Integer.parseInt(line2[0]);
				String arrival_time = line2[1];
				String departure_time = line2[2];
				if(timeReading(arrival_time) && timeReading(departure_time)) {
					int stop_id = Integer.parseInt(line2[3]);
					int stop_sequence = Integer.parseInt(line2[4]);
					int stop_headsign = (line2[5].equals("")) ? -100 : Integer.parseInt(line2[5]);
					int pickup_type = Integer.parseInt(line2[6]);
					int drop_off_type = Integer.parseInt(line2[7]);
					double shape_dist_traveled = (line2.length == 8) ? -100 : Double.parseDouble(line2[8]);
					Trips t = new Trips(trip_id,arrival_time,departure_time,stop_id,stop_sequence,
							stop_headsign, pickup_type, drop_off_type, shape_dist_traveled);
					list.add(t);
				}

			}
			count++;
		}
		System.out.println("Reading stop times: " + count);
		buffer.close();
	}

	public static boolean timeReading(String time) {
		String[] parts = time.strip().split(":");
		int hour = Integer.parseInt(parts[0]);

		return hour < 24;


	}

	public static double userInput(int from, int to) {
		double distance = 0;

		ShortestPath(graph, from);
		distance = distTo(to);
		pathTo(to);

		return distance;
	}



	public static void main(String[] args) {

		boolean bool = true;
		while(bool = true) {
			initialising();
			Scanner newScanner = new Scanner(System.in);
			try {
				System.out.println("Please enter you starting Bus Stop:");
				int from = newScanner.nextInt();
				//int from = 55;
				System.out.println("Please enter the destination Bus Stop:");
				int to = newScanner.nextInt();
				//	int to = 643;

				System.out.println("From - "+from+" To - "+to+"  Cost - "+userInput(from,to));

				Iterable<DirectedEdge> path_itr = pathTo(to);

				for(DirectedEdge p: path_itr) {
					System.out.println("From - "+ p.from() + " to - "+ p.to() + " cost - "+p.weight());
				}

				ArrayList<Integer> path = getEnrouteStops(to);

				for (int p : path) {
					System.out.print(p +" -> ");
				}

				bool = false;

			} catch(java.lang.ArrayIndexOutOfBoundsException ex) {
				System.out.println("Error, this is NOT a Stop. Try again!");

			}
		}
	}
}


