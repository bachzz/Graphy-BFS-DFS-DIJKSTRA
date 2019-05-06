package GraphWindow;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

public class AboutWindow {
	JFrame frame = new JFrame("About");
	JSplitPane splitPane = new JSplitPane();

	JPanel leftPnl = new JPanel();
	JPanel rightPnl = new JPanel();

	JButton bfsBtn = new JButton("BFS");
	JButton dfsBtn = new JButton("DFS");
	JButton dijkBtn = new JButton("Dijkstra");
	JTextArea detail = new JTextArea();
	JScrollPane scroll = new JScrollPane();

	public AboutWindow() {
		// TODO Auto-generated constructor stub
		bfsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnOnClick(1);
            }
		});

		dfsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOnClick(2);
            }
		});

		dijkBtn.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOnClick(3);
            }
		});


		bfsBtn.setForeground(Color.WHITE);
		dfsBtn.setForeground(Color.WHITE);
		dijkBtn.setForeground(Color.WHITE);
		bfsBtn.setBackground(new Color(0,1,0));
		dfsBtn.setBackground(new Color(0,1,0));
		dijkBtn.setBackground(new Color(0,1,0));
		bfsBtn.setFocusPainted(false);
		dfsBtn.setFocusPainted(false);
		dijkBtn.setFocusPainted(false);
		bfsBtn.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
		dfsBtn.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
		dijkBtn.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));

		leftPnl.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		leftPnl.add(bfsBtn, gbc);
		leftPnl.add(dfsBtn, gbc);
		leftPnl.add(dijkBtn, gbc);

		//rightPnl.setLayout(new FlowLayout(FlowLayout.LEFT));
		rightPnl.setLayout(new BoxLayout(rightPnl, BoxLayout.Y_AXIS));
		rightPnl.add(scroll);
		scroll.setViewportView(detail);

		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT); // split the window horizontally
		splitPane.setDividerLocation(200); // initial position of the divider is 200
		splitPane.setLeftComponent(leftPnl);
		splitPane.setRightComponent(rightPnl);

		frame.add(splitPane);
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}

	void btnOnClick(int type) {
		detail.setPreferredSize(new Dimension(rightPnl.getSize().width, rightPnl.getSize().height+50));
		detail.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
		detail.setTabSize(4);
		detail.setEditable(false);


		if (type == 1)
			detail.setText(""
					+ "- Overview:\n"
					+ "	BFS is a traversing algorithm where you should start\n"
					+ "	traversing from a selected node (source or starting node)\n"
					+ "	and traverse the graph layerwise thus exploring the neighbour\n"
					+ "	nodes (nodes which are directly connected to source node).\n"
					+ "	You must then move towards the next-level neighbour nodes.\n"
					+ "\n- Psuedoode:\n"
					+ "	BFS(G, s):\n"
					+ "		let Q be queue.\n"
					+ "		Q.enqueue( s )\n"
					+ "		mark s as visited.\n"
					+ "		while ( Q is not empty)\n"
					+ "			v  =  Q.dequeue( )\n"
					+ "			for all neighbours w of v in Graph G\n"
					+ "				if w is not visited\n"
					+ "					Q.enqueue( w )\n"
					+ "					mark w as visited.\n"
					+ "\n- Complexity:\n"
					+ "	The time complexity of BFS is O(V + E), where V is the number of nodes\n"
					+ "	and E is the number of edges.\n");
		if (type == 2)
			detail.setText(""
					+ "- Overview:\n"
					+ "	The DFS algorithm is a recursive algorithm that uses the idea\n"
					+ "	of backtracking. It involves exhaustive searches of all the nodes\n"
					+ "	by going ahead, if possible, else by backtracking.\n"
					+ "\n- Psuedocode:\n"
					+ "	DFS-iterative (G, s):\n"
					+ "		let S be stack\n"
					+ "		S.push( s )\n"
					+ "		mark s as visited.\n"
					+ "		while ( S is not empty):\n"
					+ "			v  =  S.top( )\n"
					+ "			S.pop( )\n"
					+ "			for all neighbours w of v in Graph G:\n"
					+ "				if w is not visited :\n"
					+ "					S.push( w )\n"
					+ "					mark w as visited\n"
					+ "\n- Complexity:\n"
					+ "	The time complexity of DFS is O(V + E), where V is the number of nodes\n"
					+ "	and E is the number of edges.\n");
		if (type == 3)
			detail.setText(""
					+ "- Overview:\n"
					+ "	Using the Dijkstra algorithm, it is possible to determine the\n"
					+ "	shortest distance (or the least effort / lowest cost) between \n"
					+ "	a start node and any other node in a graph. The idea of the algorithm\n"
					+ "	is to continiously calculate the shortest distance beginning from\n"
					+ "	a starting point, and to exclude longer distances when making an update.\n"
					+ "\n- Psuedocode:\n"
					+ "	Dijkstra(Graph, source):\n"
					+ "		for each vertex v in Graph:\n"
					+ "			dist[v] := infinity\n"
					+ "			previous[v] := undefined\n"
					+ "		dist[source] := 0\n"
					+ "		Q := the set of all nodes in Graph\n"
					+ "		while Q is not empty: \n"
					+ "			u := node in Q with smallest dist[ ]\n"
					+ "			remove u from Q\n"
					+ "			for each neighbor v of u:\n"
					+ "				alt := dist[u] + dist_between(u, v)\n"
					+ "				if alt < dist[v]\n"
					+ "					dist[v] := alt\n"
					+ "					previous[v] := u\n"
					+ "		return previous[ ]\n"
					+ "\n- Complexity:\n"
					+ "	O(V^2 + E log V) with matrix and priority queue\n"
					+ "	E: Total edges, V: total vertices");
	}
}
