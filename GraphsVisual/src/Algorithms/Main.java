package Algorithms;

import java.util.*;

public class Main {
	static Scanner cin = new Scanner(System.in);
	static int num_city, num_road, start, dest;
	static int city1, city2;
	static long distance;
	static ArrayList<City>[] graph;
	/*
	 * input 5 6 1 1
	 *
	 * 1 2 2
	 * 2 0 5
	 * 2 3 4
	 * 1 4 1
	 * 4 3 3
	 * 3 0 1
	 */

	public static void main(String[] args) {
		// khởi tạo graph, đơn đồ thị
		// số City và số Road
		System.out.println("hello");
		num_city = cin.nextInt();
		num_road = cin.nextInt();
		start = cin.nextInt();
		dest = cin.nextInt();

		graph = new ArrayList[num_city];
		for (int i = 0; i < num_city; i++) {
			graph[i] = new ArrayList<City>();
		}

		for (int i = 0; i < num_road; i++) {
			city1 = cin.nextInt();
			city2 = cin.nextInt();
			distance = cin.nextLong();
			graph[city1].add(new City(city2, distance));
			graph[city2].add(new City(city1, distance));
		}

		// kết thúc việc khởi tạo

		Dijkstra graph1 = new Dijkstra();
		Dfs graph2 = new Dfs();
		Bfs graph3 = new Bfs();
		// dijkstra
		graph1.init(graph, start, dest, num_city);
		graph1.run();
		graph1.print();
		// dfs
		graph2.init(graph, start, dest, num_city);
		graph2.run();
		graph2.print();
		// Bfs
		graph3.init(graph, start, dest, num_city);
		graph3.run();
		graph3.print();
	}
}