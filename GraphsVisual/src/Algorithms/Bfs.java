package Algorithms;

import java.awt.Point;
import java.util.*;

import Algorithms.City;

public class Bfs {
	private ArrayList<City>[] graph;
	private LinkedList<Integer> queue = new LinkedList<Integer>();
	private int start, num_city, dest;
	private int[] pre;
	//private int[] visited_arr;
	private LinkedList<Integer> visitedQ = new LinkedList<Integer>();
	private int k=0;
	
	public void init(ArrayList<City>[] graph, int start,int dest, int num_city) {
		this.graph = graph;
		this.start = start;
		this.dest = dest; //fix
		this.num_city = num_city;
		pre = new int[num_city];
		//visited_arr = new int[num_city];
		for (int i = 0; i < num_city; i++) {
			pre[i] = -1;
			//visited_arr[i] = -1;
		}
		pre[start] = -2;
	}

	public void run() {
		int temp;
		queue.add(start);
		
		while (!queue.isEmpty()) {
			temp = queue.poll();
			for (City itr : graph[temp]) {
				if (pre[itr.city] == -1) {
					pre[itr.city] = temp;
					queue.add(itr.city);
					
				}
			}
		}
	}

	public String run(int step,ArrayList<Point> visited) {
		int temp;
		int stop=0;

		queue.clear();
		visitedQ.clear();
		for (int i = 0; i < num_city; i++) {
			pre[i] = -1;
			//visited_arr[i] = -1;
		}
		pre[start] = -2;

		queue.add(start);
		//visited_arr[k++] = start;
		visitedQ.add(start);
		
		while (!queue.isEmpty()) {
			temp = queue.poll();
			for (City itr : graph[temp]) {
				if (pre[itr.city] == -1) {

					Point temp1=new Point(itr.city,temp);
					visited.add(temp1);

					pre[itr.city] = temp;
					
					queue.add(itr.city);
					//visited_arr[k++] = itr.city;
					visitedQ.add(itr.city);
					
					if(stop>=step) {
						String str=new String("\n\nQueue = [ ");
						for(Integer itr1:queue)str+=itr1.toString()+" ";
						str+="]";
						str+="\n\nVisited = [";
						for(Integer itr1:visitedQ)str+=itr1.toString()+" ";
						str+="]";
						System.out.println(str);
						return str;
					}
					if(itr.city==dest)return "end";
					stop++;
				}
			}
		}
		return "end";
	}

	public void print() {
		int temp;
		System.out.println("traverse BFS from " + start + " to:");
		for (int i = 0; i < num_city; i++) {
			if (pre[i] == -1) {
				System.out.print("city " + i + "is error");
				continue;
			}
			System.out.print("city " + i + " : " + i);
			temp = pre[i];
			while (temp != -2) {
				System.out.print("<-" + temp);
				temp = pre[temp];
			}
			System.out.println();
		}
	}
	public boolean path(int start,int dest,List<Integer>_path_) {
		if(pre[dest]==-1) {
			System.out.println("traverse BFS from " + this.start + " to "+this.dest+" is fail");
			return false;
		}
		for(;dest!=start;dest=pre[dest])_path_.add(dest);
		_path_.add(dest);
		return true;
	}
}