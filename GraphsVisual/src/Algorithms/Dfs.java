package Algorithms;

import java.awt.Point;
import java.util.*;

import Algorithms.City;

public class Dfs {
	private ArrayList<City>[] graph;
	private int start, num_city,dest;
	private int[] pre;
	private LinkedList<Integer> stack = new LinkedList<Integer>();
	private LinkedList<Integer> visitedQ = new LinkedList<Integer>();


	public void init(ArrayList<City>[] graph, int start,int dest, int num_city) {
		this.graph = graph;
		this.start = start;
		this.dest = dest;
		this.num_city = num_city;
		pre = new int[num_city];
		for (int i = 0; i < num_city; i++) {
			pre[i] = -1;
		}
		pre[start] = -2;
	}

	public void run() {
		int temp;
		stack.addFirst(start);
		loop: while (!stack.isEmpty()) {
			temp = stack.peekFirst();
			for (City itr : graph[temp]) {
				if (pre[itr.city] == -1) {
					stack.addFirst(itr.city);
					pre[itr.city] = temp;
					continue loop;
				}
			}
			stack.pollFirst();
		}
	}
	// run stop when hit the step
	public String run(int step,ArrayList<Point> visited) {
		int stop=0;
		int temp;

		stack.clear();
		visitedQ.clear();

		for (int i = 0; i < num_city; i++) {
			pre[i] = -1;
		}
		pre[start] = -2;

		stack.addFirst(start);
		visitedQ.add(start);

		loop: while (!stack.isEmpty()) {
			temp = stack.peekFirst();
			for (City itr : graph[temp]) {

				if(stop>=step) {
					int c=10;
					String str=new String("Queue = [ ");
					for(Integer itr1:stack){
						c+=(itr1.toString()+" ").length();
						if(c>=25) {c=0;str+="\n";}
						str+=itr1.toString()+" ";
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

				if (pre[itr.city] == -1) {

					Point temp1=new Point(itr.city,temp);
					visited.add(temp1);

					stack.addFirst(itr.city);
					pre[itr.city] = temp;
					visitedQ.add(itr.city);

					if(itr.city==dest)return "end";
					stop++;
					continue loop;
				}
			}
			stack.pollFirst();
		}
		return "end";
	}


	public void print() {
		int temp;
		System.out.println("traverse DFS from " + start + " to:");
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
			System.out.println("traverse DFS from " + this.start + " to "+this.dest+" is fail");
			return false;
		}
		for(;dest!=start;dest=pre[dest])_path_.add(dest);
		_path_.add(dest);
		return true;
	}
}