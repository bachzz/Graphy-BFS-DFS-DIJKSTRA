package Algorithms;

import java.awt.Point;
import java.util.*;

import Algorithms.City;

public class Dijkstra {

	private PriorityQueue<City> que = new PriorityQueue<City>();
	private ArrayList<City>[] graph;
	private int start, num_city, dest;
	private double[] dis;
	private int[] pre;
	private LinkedList<Integer> visitedQ = new LinkedList<Integer>();
	
	public void init(ArrayList<City>[] graph, int start,int dest, int num_city) {
		this.graph = graph;
		this.start = start;
		this.dest = dest;
		this.num_city = num_city;
		dis = new double[num_city];
		pre = new int[num_city];
		for (int i = 0; i < num_city; i++) {
			dis[i] = Double.MAX_VALUE;
			pre[i] = -1;
		}
		dis[start] = 0;
		pre[start] = -2;
	}

	public void run() {
		que.add(new City(start, 0L));
		while (que.size() > 0) {
			City temp = que.poll();
			for (City itr : graph[temp.city]) {
				if (dis[itr.city] > itr.distance + temp.distance) {
					dis[itr.city] = itr.distance + temp.distance;
					pre[itr.city] = temp.city;
					que.add(new City(itr.city, itr.distance + temp.distance));
				}
			}
		}
	}

	public String run(int step,ArrayList<Point> visited) {
		int stop=0;
		que.clear();
		visitedQ.clear();
		
		double best = Double.MAX_VALUE;;
		for (int i = 0; i < num_city; i++) {
			dis[i] = Double.MAX_VALUE;
			pre[i] = -1;
		}
		dis[start] = 0;
		pre[start] = -2;

		que.add(new City(start, 0L));
		visitedQ.add(start);
		
		while (que.size() > 0) {
			City temp = que.poll();
			for (City itr : graph[temp.city]) {
				if (dis[itr.city] > itr.distance + temp.distance) {

					for(Point itr1:visited)if(itr1.y==itr.city) {visited.remove(itr1);break;}
					Point temp1=new Point(temp.city,itr.city);
					visited.add(temp1);

					dis[itr.city] = itr.distance + temp.distance;
					pre[itr.city] = temp.city;
					que.add(new City(itr.city, itr.distance + temp.distance));
					visitedQ.add(itr.city);
					
					if(stop==step) {
						String str=new String("\n\nQueue = [ ");for(City itr1:que)str+=itr1.city+" ";str+="]";
						System.out.println(str);
						str+="\n\nVisited = [";
						for(Integer itr1:visitedQ)str+=itr1.toString()+" ";
						str+="]";
						System.out.println(str);
						return str;
					}
					if(dis[que.peek().city]>best)return "end";//optimal
					if(itr.city==dest)best=(best>dis[dest]?dis[dest]:best);

					stop++;
				}
			}
		}
		return "end";
	}

	public void print() {
		int temp;
		System.out.println("traverse dijkstra from " + start + " to:");
		for (int i = 0; i < num_city; i++) {
			if (dis[i] == Double.MAX_VALUE) {
				System.out.print("city " + i + "is error");
				continue;
			}
			System.out.print("city " + i + " with dis: " + dis[i] + "\n" + i);
			temp = pre[i];
			while (temp != -2) {
				System.out.print("<-" + temp);
				temp = pre[temp];
			}
			System.out.println();
		}
	}
	public boolean path(int start,int dest,City length_path,List<Integer>_path_) {
		if(dis[dest]==Double.MAX_VALUE) {
			System.out.println("traverse dijkstra from " + this.start + " to "+this.dest+" is fail");
			return false;
		}
		length_path.setDistance(dis[dest]);//this is dijkstra
		for(;dest!=start;dest=pre[dest])_path_.add(dest);
		_path_.add(dest);
		return true;
	}
}