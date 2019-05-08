package GraphWindow;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;
import javax.swing.Timer;
import javax.swing.*;
import Algorithms.*;

public class GraphWindow extends JPanel {
	private boolean check_run=false;
	private boolean check_Dijkstra=false;

	private JFrame window;
	private JSplitPane splitPane; // split the window in top and bottom
	private JPanel leftPanel; // container panel for the top
	private JPanel rightPanel; // container panel for the bottom

	private JPanel graphPanel;
	private JScrollPane scrollPane; // makes the text scrollable
	private JTextArea txtConsole; // the text console

	private JPanel inputPanel; // under the text a container for all the input elements
	private JPanel btnPanel;
	private JLabel text;
	private JButton resetBtn; // reset RE_DRAW
	private JButton clearBtn; // clear console
	private JButton startBtn; // start simulation
	private String[] algo = { "BFS", "DFS", "Dijkstra","A* Search"};;
	private JComboBox<Object> algoCmb;

	//the thing that can draw everything
	static Graphics2D graphicHandle;
	static BufferedImage  bufferImage;

	private int _selectedNode = -1;
	int _SIZE_OF_NODE = 20;
	private int id = 0;
	private int _source = -1;
	private int _dest = -1;
	double dotOffset = 0.0;

	private HashMap<Integer, HashSet<Integer>> nodes = new HashMap<Integer, HashSet<Integer>>();// 'set of path' of each node
	private HashMap<Integer, Point> locations = new HashMap<Integer, Point>();                  //  list of location of each node
	private HashMap<Integer, Integer> connMem = new HashMap<>();                        //  check the path not RE-DRAW twice
	private List<Integer>_path_ =new ArrayList<Integer>();                                      //  path of solution
	private int count_line=0;

	City path_length=new City(0,0);//I want to pass by reference

	// when running
	private boolean cur_run=false;
	private ArrayList<Point> visited=new ArrayList<Point>();
	private String data_contain="fuck";


	public void init() {
		window = new JFrame("Graphy");
		splitPane = new JSplitPane();
		leftPanel = new JPanel();
		rightPanel = new JPanel();

		graphPanel = new JPanel();
		scrollPane = new JScrollPane();
		txtConsole = new JTextArea();

		inputPanel = new JPanel();
		btnPanel = new JPanel();
		text = new JLabel("   SOURCE: [unknown]    DEST: [unknown]");

		resetBtn = new JButton("Reset");
		clearBtn = new JButton("Clear console");
		startBtn = new JButton("Start");
		algoCmb = new JComboBox<Object>(algo);


		resetBtn.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
		});

		startBtn.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
		});

		clearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
		});

		/* SplitPane
		 * - left: console log
		 * - right: graphPanel + inputPanel
		 */
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT); // split the window horizontally
		splitPane.setDividerLocation(200); // initial position of the divider is 200
		splitPane.setLeftComponent(leftPanel);
		splitPane.setRightComponent(rightPanel);

		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.add(scrollPane);
		scrollPane.setViewportView(txtConsole);
		txtConsole.setEditable(false);

		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS)); // BoxLayout.Y_AXIS will arrange the content vertically
		rightPanel.add(graphPanel);
		rightPanel.add(inputPanel); // then we add the inputPanel to the bottomPanel, so it under the scrollPane textArea

		// graphPanel
		graphPanel.setBackground(new Color(0, 0, 0));
		graphPanel.setBorder(BorderFactory.createEtchedBorder());
		graphPanel.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent evt) {
				graphMouseDragged(evt);
			}
		});
		graphPanel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				graphMouseClicked(evt);
			}

			public void mousePressed(MouseEvent evt) {
				graphMousePressed(evt);
			}

			public void mouseReleased(MouseEvent evt) {
				graphMouseReleased(evt);
			}
		});

		inputPanel.setLayout(new GridLayout(2, 1));
		inputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100)); // set the max height to 75 and the max width to (almost) unlimited
		// inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS)); // X_Axis
		// will arrange the content horizontally
		inputPanel.setBackground(new Color(0, 0, 40));
		inputPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 100));
		inputPanel.setBorder(BorderFactory.createEtchedBorder());
		inputPanel.add(text);

		text.setForeground(Color.WHITE);

		btnPanel.add(resetBtn);
		btnPanel.add(clearBtn);
		btnPanel.add(algoCmb);
		btnPanel.add(startBtn);
		btnPanel.setBackground(new Color(0, 0, 40));
		inputPanel.add(btnPanel);

		// pack(); // calling pack() at the end, will ensure that every layout and size we just defined gets applied before the stuff becomes visible
		window.add(splitPane);
		window.setSize(800, 600);
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setVisible(true);
	}
	                                        /* Re-draw SMT */
	public void RE_DRAW() {
		//delay redraw - useless function
//		try {
//			Thread.sleep(30);
//		} catch (InterruptedException e) {
//
//			e.printStackTrace();
//		}

		//retype
		txtConsole.setText("");

		//re_draw background
		graphicHandle.setColor(Color.black);
		graphicHandle.fillRect(0, 0, graphPanel.getWidth(), graphPanel.getHeight());
        connMem.clear();

        /* Create nodes - colors & labels */
        if(!cur_run)if(locations.size()!=0)consoleLog("   ----Node----");
     	for (int i = 0; i < locations.size(); i++) {
     		Point thePoint = (Point) locations.values().toArray()[i];
     		//retype node & location
     		int vertex=(int)locations.keySet().toArray()[i];
     		if(!cur_run) {
     			consoleLog("N"+vertex+" at ("+locations.get(vertex).x+","+locations.get(vertex).y+")");//fix
     		}

     		if (locations.keySet().toArray()[i].equals((Integer) _source)) {
     			graphicHandle.setColor(Color.GREEN);
    		} else if (locations.keySet().toArray()[i].equals((Integer) _dest)) {
     			graphicHandle.setColor(Color.BLUE);
     		} else if (locations.keySet().toArray()[i].equals((Integer) _selectedNode)) {
     			graphicHandle.setColor(Color.ORANGE);
     		} else {
     			graphicHandle.setColor(Color.RED);
     		}

     		graphicHandle.fillOval(thePoint.x - _SIZE_OF_NODE / 2, thePoint.y - _SIZE_OF_NODE / 2, _SIZE_OF_NODE,
     					_SIZE_OF_NODE);
     	}

     	if(!cur_run)consoleLog("");

     	// Node labels.
     	graphicHandle.setColor(Color.WHITE);
     	for (int i = 0; i < locations.size(); i++) {
     		Point thePoint = (Point) locations.values().toArray()[i];
     		graphicHandle.drawString("N " + locations.keySet().toArray()[i], thePoint.x + _SIZE_OF_NODE ,
     					thePoint.y + _SIZE_OF_NODE);
     	}

		//draw some line
		graphicHandle.setStroke(new BasicStroke(2));//draw thin line
		if(!cur_run)if(count_line!=0)consoleLog("   ----Line----");
		graphicHandle.setColor(Color.cyan);
		for (int i = 0; i < locations.size(); i++) {
			Integer sourceKey = (Integer) nodes.keySet().toArray()[i];
			Point thePoint = (Point) locations.values().toArray()[i];// get location of node_index_i
			for (Integer destKey : (HashSet<Integer>) nodes.values().toArray()[i]) {
				if (!(connMem.containsKey(sourceKey) && connMem.get(sourceKey) == destKey || connMem.containsKey(destKey) && connMem.get(destKey) == sourceKey)) {
					Point destPoint  = locations.get(destKey);
					graphicHandle.drawLine(thePoint.x, thePoint.y, destPoint .x, destPoint .y);
					connMem.put(sourceKey, destKey);
					if(!cur_run) {
						//cal distance
						Double dis=Math.sqrt(sqr(locations.get(sourceKey).x-locations.get(destKey).x)+sqr(locations.get(sourceKey).y-locations.get(destKey).y));
						//retype path
						consoleLog("N"+(sourceKey>99?sourceKey:sourceKey>9?sourceKey+" ":sourceKey+"  ")+" -> N"+(destKey>99?destKey:destKey>9?destKey+" ":destKey+"  ")+" dis:"+Double.parseDouble(String.format("%.3f", dis)));
					}
				}
			}
		}
		if(!cur_run)consoleLog("");

		//                                      when running
		if(cur_run) {
			consoleLog("----process----");
			//draw some point && some line  with same color
			graphicHandle.setColor(Color.YELLOW);
			for(Point itr:visited) {
				graphicHandle.fillOval(locations.get(itr.x).x - _SIZE_OF_NODE / 2, locations.get(itr.x).y - _SIZE_OF_NODE / 2, _SIZE_OF_NODE, _SIZE_OF_NODE);
				graphicHandle.fillOval(locations.get(itr.y).x - _SIZE_OF_NODE / 2, locations.get(itr.y).y - _SIZE_OF_NODE / 2, _SIZE_OF_NODE, _SIZE_OF_NODE);
				graphicHandle.drawLine(locations.get(itr.x).x, locations.get(itr.x).y, locations.get(itr.y).x, locations.get(itr.y).y);
			}
			consoleLog(data_contain);// this is fixed
			txtConsole.update(txtConsole.getGraphics());
 		}




		// Glowing connections
		graphicHandle.setColor(new Color(230,226,9));//(10, 230, 40));
		graphicHandle.setStroke(new BasicStroke(8));
		// use when after click start
		if (!resetBtn.isSelected()) {
            String temp="";
            int c=0;
            if(check_run) {
            	temp+="N"+_source;
            	c+=("N"+_source).length();
            	for(int i=_path_.size()-1;i!=0;) {
                	// type some success path
                	if(c>25) {c=0;temp+="\n";continue;}// new line when string too long
                	temp+=new String(" -> "+"N"+_path_.get(i-1));
                    c+=(" -> "+"N"+_path_.get(i-1)).length();
                    drawPath(graphicHandle, locations.get(_path_.get(i)), locations.get(_path_.get(i-1)), dotOffset);
                    i--;
                }
                consoleLog("   --solution--\n"+temp);
            }
		}

		graphPanel.getGraphics().drawImage(bufferImage, 0, 0, this);
		// update source and destination
		if(check_Dijkstra)text.setText("   SOURCE: "+(_source==-1?"[unknown]":_source)+"    DEST: "+(_dest==-1?"[unknown]":_dest)+"    PATH_LENGTH: "+Double.parseDouble(String.format("%.3f", path_length.getDistance())));
		else text.setText("   SOURCE: "+(_source==-1?"[unknown]":_source)+"    DEST: "+(_dest==-1?"[unknown]":_dest));//fix
	}

	// run after click button start; draw the dot line
	private void drawPath(Graphics graphicHandle2, Point p1, Point p2, double offset) {
		long total = (p2.x - p1.x) * (p2.x - p1.x) + (p2.y - p1.y) * (p2.y - p1.y);
		total = (long) Math.sqrt(total);
		for (long i = (long) (offset * 20); i <= total; i += 20) {
			int x1 = (int) (p1.x + (p2.x - p1.x) * i / total);
			int y1 = (int) (p1.y + (p2.y - p1.y) * i / total);
			int x2 = (int) (p1.x + (p2.x - p1.x) * Math.min(i + 5, total) / total);
			int y2 = (int) (p1.y + (p2.y - p1.y) * Math.min(i + 5, total) / total);
			graphicHandle2.drawLine(x1, y1, x2, y2);
		}
	}
							/* END Re-draw SMT */

	private int nodeSelected(int x, int y) {
		for (int i = 0; i < locations.size(); i++) {
			Point thePoint = (Point) locations.values().toArray()[i];
			int deltaX = x - (thePoint.x - _SIZE_OF_NODE / 2);
			int deltaY = y - (thePoint.y - _SIZE_OF_NODE / 2);
			if (Math.sqrt(deltaX * deltaX + deltaY * deltaY) <= _SIZE_OF_NODE + 6) {
				return (int) locations.keySet().toArray()[i];
			}
		}
		return -1;
	}

							/* Mouse Action SMT */
	private void graphMouseClicked(MouseEvent evt) {
		//set SOURCE && DESTINATION
		_selectedNode = nodeSelected(evt.getX(), evt.getY());
        if (evt.getClickCount() == 2) {
            if (_source == -1 && _dest == -1 || _source != -1 && _dest != -1) {
                _path_.clear();
                _source = _selectedNode;
                _dest = -1;
                check_run=false;
        		check_Dijkstra=false;
            } else if (_source != _selectedNode) {
            	_path_.clear();
                _dest = _selectedNode;
                check_run=false;
        		check_Dijkstra=false;
            }
            RE_DRAW();
        }
	}

	private void graphMousePressed(MouseEvent evt) {
		_selectedNode = nodeSelected(evt.getX(), evt.getY());
		if (_selectedNode < 0 && SwingUtilities.isLeftMouseButton(evt)) {
			//make node
			nodes.put(id, new HashSet<Integer>());
			locations.put(id++, new Point(evt.getX(), evt.getY()));
		}else if (SwingUtilities.isRightMouseButton(evt)) {
			//delete node & stop running
			_path_.clear();
			_source = -1;
			_dest = -1;
			check_run=false;
			check_Dijkstra=false;
			nodes.remove(_selectedNode);
			locations.remove(_selectedNode);

			//delete line
			for (HashSet<Integer> connections : nodes.values()) {
				for (int j = 0; j < connections.size(); j++) {
					Integer connection = (Integer) connections.toArray()[j];
					if (connection == _selectedNode) {
						connections.remove(connection);
						j--;
						count_line--;
					}
				}
			}
			if (_selectedNode == _dest) {
				_dest = -1;
				_path_.clear();
			}
			if (_selectedNode == _source) {
				_source = -1;
				_dest = -1;
				_path_.clear();
			}
			_selectedNode = -1;
		}
		RE_DRAW();
	}

	private void graphMouseDragged(MouseEvent evt) {
		if (_selectedNode >= 0) {
            if (SwingUtilities.isLeftMouseButton(evt)) {
            	//draw the line
            	RE_DRAW();//avoid the duplicate
        		Point source = locations.get(_selectedNode);
                graphicHandle.setStroke(new BasicStroke(1));//draw thin line.
                //graphicHandle.drawImage(bufferImage, 0, 0, this);
                graphicHandle.setColor(Color.GREEN);
                graphicHandle.drawLine(source.x, source.y,evt.getX(), evt.getY());
                graphPanel.getGraphics().drawImage(bufferImage, 0, 0, this);
            } else if (SwingUtilities.isMiddleMouseButton(evt)) {
            	//move the node
                locations.get(_selectedNode).x = evt.getX();
                locations.get(_selectedNode).y = evt.getY();
                RE_DRAW();
            }
        }
	}

	private void graphMouseReleased(MouseEvent evt) {
		// End of DRAGING LINE
		if (_selectedNode >= 0) {
            int destination = nodeSelected(evt.getX(), evt.getY());
            if (destination >= 0 && destination != _selectedNode) {
                nodes.get(_selectedNode).add(destination);
                nodes.get(destination).add(_selectedNode);
                _selectedNode = -1;
                count_line++;
            }
        }
		RE_DRAW();
	}
						/* END Mouse Action SMT */


							/*Button Action*/
	void btnResetActionPerformed(ActionEvent evt) {
		check_run=false;
		check_Dijkstra=false;
		count_line=0;

		nodes = new HashMap<Integer, HashSet<Integer>>();
        locations = new HashMap<Integer, Point>();
        id = 0;
        _path_.clear();
        _source = -1;
        _dest = -1;
        RE_DRAW();
	}

	public void consoleLog(String string) {
        txtConsole.append(string + "\n");
    }

	void btnClearActionPerformed(ActionEvent evt) {
		txtConsole.setText("");
	}

	void btnStartActionPerformed(ActionEvent evt) {
		//clear path & smt after click start
		check_run=false;
    	check_Dijkstra=false;
		_path_.clear();
		visited.clear();

		String x = String.valueOf(algoCmb.getSelectedItem());
        txtConsole.setText("");

        //int num_city=locations.size();
        double dis;
        ArrayList<City>[] graph = new ArrayList[id+1];

        for (int i = 0; i < id+1; i++) {
			graph[i] = new ArrayList<City>();
		}
        //create the graph
        for(Integer i = 0 ; i <id+1 ; i++) {
        	if(nodes.get(i)==null)continue;
        	for(Integer itr:nodes.get(i)) {
        		dis=Math.sqrt(sqr(locations.get(i).x-locations.get(itr).x)+sqr(locations.get(i).y-locations.get(itr).y));
        	    graph[i].add(new City(itr,dis));
        	}
        }

        if (x == "DFS") {
            txtConsole.setText("");
            if (_source == -1 || _dest == -1) {
                if (_source == -1) {
                    consoleLog("Please choose a source by double clicking a node");
                } else {
                    consoleLog("Please choose a destination by double clicking a node");
                }
                return;
            }

            Dfs solution=new Dfs();
            solution.init(graph, _source, _dest, id+1);
            cur_run=true;
            for(int step=0;true;step++){

            	data_contain=new String(solution.run(step,visited));
            	System.out.println(data_contain);
            	RE_DRAW();

            	//delay function
            	for(long i=0;i<1000000000;i++);
            	for(long i=0;i<1000000000;i++);
            	for(long i=0;i<1000000000;i++);
            	for(long i=0;i<1000000000;i++);

//            	TODO input smt to GO_NEXT or GO_BACK
//            	if() {
//            		step++;
//            		continue;
//            	}
//            	else if() {
//            		step--;
//            		continue;
//            	}
            	if(data_contain.equals("end"))break;
            }

            cur_run=false;

            if(solution.path(_source,_dest,_path_)) {
            	check_run=true;
            	check_Dijkstra=false;
            	RE_DRAW();
            }
            else {/* print something when error occurs */
            	consoleLog("fail run DFS from "+_source+" to "+_dest);
            }
        } else if (x == "BFS") {
            txtConsole.setText("");
            if (_source == -1 || _dest == -1) {
                if (_source == -1) {
                    consoleLog("Please choose a source by double clicking a node");
                } else {
                    consoleLog("Please choose a destination by double clicking a node");
                }
                return;
            }
            Bfs solution=new Bfs();
            solution.init(graph, _source, _dest, id+1);
            //solution.run();

            cur_run=true;
            for(int step=0;true;step++){
            	data_contain=new String(solution.run(step,visited));
            	System.out.println(data_contain);
            	RE_DRAW();
            	//delay function
            	for(long i=0;i<1000000000;i++);
            	for(long i=0;i<1000000000;i++);
            	for(long i=0;i<1000000000;i++);
            	for(long i=0;i<1000000000;i++);

//            	TODO input smt to GO_NEXT or GO_BACK
//            	if() {
//            		step++;
//            		continue;
//            	}
//            	else if() {
//            		step--;
//            		continue;
//            	}
            	if(data_contain.equals("end"))break;
            }
            cur_run=false;

            if(solution.path(_source,_dest,_path_)) {
            	check_run=true;
            	check_Dijkstra=false;
            	RE_DRAW();
            }
            else {/* print something when error occurs */
            	consoleLog("fail run BFS from "+_source+" to "+_dest);
            }
        }
        else if (x == "Dijkstra") {
            txtConsole.setText("");
            if (_source == -1 || _dest == -1) {
                if (_source == -1) {
                    consoleLog("Please choose a source by double clicking a node");
                } else {
                    consoleLog("Please choose a destination by double clicking a node");
                }
                return;
            }
            Dijkstra solution=new Dijkstra();
            solution.init(graph, _source, _dest, id+1);
            //solution.run();
            cur_run=true;
            for(int step=0;true;step++){
            	data_contain=new String(solution.run(step,visited));
            	System.out.println(data_contain);
            	RE_DRAW();
            	//delay function
            	for(long i=0;i<1000000000;i++);
            	for(long i=0;i<1000000000;i++);
            	for(long i=0;i<1000000000;i++);
            	for(long i=0;i<1000000000;i++);

//            	TODO input smt to GO_NEXT or GO_BACK
//            	if() {
//            		step++;
//            		continue;
//            	}
//            	else if() {
//            		step--;
//            		continue;
//            	}
            	if(data_contain.equals("end"))break;
            }
            cur_run=false;


            if(solution.path(_source,_dest,path_length,_path_)) {
            	check_run=true;
            	check_Dijkstra=true;
            	RE_DRAW();
            }
            else {/* print something when error occurs */
            	consoleLog("fail run Dijkstra from "+_source+" to "+_dest);
            }
        }
        else if (x == "A* Search") {
            txtConsole.setText("");
            if (_source == -1 || _dest == -1) {
                if (_source == -1) {
                    consoleLog("Please choose a source by double clicking a node");
                } else {
                    consoleLog("Please choose a destination by double clicking a node");
                }
                return;
            }
            A_start_search solution=new A_start_search();
            // special for A*: heuristic to destianation
            ArrayList<Double>euler_destination=new ArrayList<Double>();
            for(Integer i = 0 ; i <id+1 ; i++) {
            	euler_destination.add(1e6);
            	if(nodes.get(i)==null)continue;
            	dis=Math.sqrt(sqr(locations.get(i).x-locations.get(_dest).x)+sqr(locations.get(i).y-locations.get(_dest).y));
            	euler_destination.set(i, dis);
            }

            solution.init(graph, euler_destination, _source, _dest, id+1);
            //solution.run();
            cur_run=true;
            for(int step=0;true;step++){
            	data_contain=new String(solution.run(step,visited));
            	System.out.println(data_contain);
            	RE_DRAW();
            	//delay function
            	for(long i=0;i<1000000000;i++);
            	for(long i=0;i<1000000000;i++);
            	for(long i=0;i<1000000000;i++);
            	for(long i=0;i<1000000000;i++);

//            	TODO input smt to GO_NEXT or GO_BACK
//            	if() {
//            		step++;
//            		continue;
//            	}
//            	else if() {
//            		step--;
//            		continue;
//            	}
            	if(data_contain.equals("end"))break;
            }
            cur_run=false;


            if(solution.path(_source,_dest,path_length,_path_)) {
            	check_run=true;
            	check_Dijkstra=true;
            	RE_DRAW();
            }
            else {/* print something when error occurs */
            	consoleLog("fail run Dijkstra from "+_source+" to "+_dest);
            }
        }
        RE_DRAW();
	}


	public GraphWindow() {
		init();

		bufferImage = new BufferedImage(graphPanel.getWidth(),graphPanel.getHeight(),BufferedImage.TYPE_INT_RGB);
		assert(bufferImage!=null);
		graphicHandle = (Graphics2D) bufferImage.getGraphics();//(Graphics2D) graphPanel.getGraphics();

		/* ANIMATION */
		Timer animationTimer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (_path_.size() > 0) {
                    dotOffset = (dotOffset + .1) % 1;
                    RE_DRAW();
                }
            }
        });
		animationTimer.start();
	}

	// Calculations
	private static int sqr(int x) {
		return x * x;
	}
}
