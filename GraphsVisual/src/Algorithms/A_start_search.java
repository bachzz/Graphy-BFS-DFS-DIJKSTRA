package Algorithms;

import java.awt.Point;
import java.util.*;

import Algorithms.City;

public class A_start_search {

	private PriorityQueue<City> que = new PriorityQueue<City>();
	private ArrayList<City>[] graph;
	private int start, num_city, dest;
	private double[] dis;
	private ArrayList<Double> euler_dis;//distance from that CITY -> DESTINATION
	private int[] pre;
	private HashSet<Integer> visitedQ = new HashSet<Integer>();

	class Sortbydest implements Comparator<City>
	{
	    // Used for sorting in ascending order of
	    // dest
	    public int compare(City a, City b)
	    {
	        return (euler_dis.get(a.city)>euler_dis.get(b.city)?1:euler_dis.get(a.city)<euler_dis.get(b.city)?-1:0);
	    }
	}

	public void init(ArrayList<City>[] graph,ArrayList<Double>euler_dis, int start,int dest, int num_city) {
		this.graph = graph;
		this.start = start;
		this.dest = dest;
		this.num_city = num_city;
		this.euler_dis = euler_dis;
		/*sorting following distance to destination */
		for (int i = 0; i < num_city; i++) {
			if(graph[i]==null)continue;
			Collections.sort(graph[i],new Sortbydest());
		}
		dis = new double[num_city];
		pre = new int[num_city];
		for (int i = 0; i < num_city; i++) {
			dis[i] = Double.MAX_VALUE;
			pre[i] = -1;
		}
		dis[start] = 0;
		pre[start] = -2;
	}
	//fix this
	public void run() {
		que.add(new City(start, 0L));
		while (que.size() > 0) {
			City temp = que.poll();
			for (City itr : graph[temp.city]) {
				if (dis[itr.city] > euler_dis.get(itr.city) + temp.distance + itr.distance) {
					dis[itr.city] = itr.distance + temp.distance;
					pre[itr.city] = temp.city;
					que.add(new City(itr.city, dis[itr.city]));
				}
			}
		}
	}

	public String run(int step,ArrayList<Point> visited) {
		int stop=0;
		que.clear();
		double best = Double.MAX_VALUE;;
		for (int i = 0; i < num_city; i++) {
			dis[i] = Double.MAX_VALUE;
			pre[i] = -1;
		}
		dis[start] = euler_dis.get(start);
		pre[start] = -2;

		visitedQ.add(start);
		que.add(new City(start, euler_dis.get(start)));//bug
		while (que.size() > 0) {
			City temp = que.peek();
			for (City itr : graph[temp.city]) {
				//if (dis[itr.city] == Double.MAX_VALUE || (dis[itr.city] > temp.distance + itr.distance + euler_dis.get(itr.city))) {
				if (dis[itr.city] == Double.MAX_VALUE || (dis[itr.city] > temp.distance + itr.distance)) {

					for(Point itr1:visited)if(itr1.y==itr.city) {visited.remove(itr1);break;}
					Point temp1=new Point(temp.city,itr.city);
					visited.add(temp1);
					visitedQ.add(itr.city);

					dis[itr.city] = (pre[itr.city]>-1?itr.distance + temp.distance + euler_dis.get(itr.city) - euler_dis.get(pre[itr.city]):itr.distance + temp.distance + euler_dis.get(itr.city)-euler_dis.get(temp.city));
					pre[itr.city] = temp.city;
					que.add(new City(itr.city, dis[itr.city] ));

					if(stop>=step) {
						int c=10;
						String str=new String("Queue = [ ");
						for(City itr1:que){
							c+=(itr1.city+" ").length();
							if(c>=25) {c=0;str+="\n";}
							str+=itr1.city+" ";
						}
						str+="]";
						c=10;
						str+="\nVisited = [";
						for(Integer itr1:visitedQ) {
							c+=(itr1.toString()+" ").length();
							if(c>=25) {c=0;str+="\n";}
							str+=itr1.toString()+" ";
						}
						str+="]";
						System.out.println(str);
						return str;
					}
					if(dis[que.peek().city]>best)return "end";//optimal
					if(itr.city==dest)best=(best>dis[dest]?dis[dest]:best);
					stop++;
				}
			}
			que.poll();
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
			while (temp != start) {
				System.out.print("<-" + temp);
				temp = pre[temp];
			}
			System.out.println(" <- "+start);
			System.out.println();
		}
	}
	public boolean path(int start,int dest,City length_path,List<Integer>_path_) {
		if(dis[dest]==Double.MAX_VALUE) {
			System.out.println("traverse A* from " + this.start + " to "+this.dest+" is fail");
			return false;
		}
		length_path.setDistance(dis[dest]);//this is dijkstra
		for(;dest!=start;dest=pre[dest])_path_.add(dest);
		_path_.add(dest);
		return true;
	}
}
