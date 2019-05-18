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
	// private JButton startBtn; // start simulation
	private String[] algo = { "BFS", "DFS", "Dijkstra","A* Search"};;
	private JComboBox<Object> algoCmb;

	//the thing that can draw everything
	Graphics2D graphicHandle;
	BufferedImage  bufferImage;

	//this new
	private JPanel playPanel;
	private JButton backBackBtn;
	private JButton backBtn;
	private JButton playBtn; // start simulation
	private JButton pauseBtn; // pause simulation
	private JButton nextBtn;
	private JButton nextNextBtn;
	private JPanel algoPanel;
	private JSplitPane splitPaneChild;
	private JScrollPane scrollAlgo;

	private int _selectedNode = -1;
	int _SIZE_OF_NODE = 20;
	private int id = 0;
	private int _source = -1;
	private int _dest = -1;
	double dotOffset = 0.0;

	private HashMap<Integer, HashSet<Integer>> nodes = new HashMap<Integer, HashSet<Integer>>();// 'set of path' of each node
	private HashMap<Integer, Point> locations = new HashMap<Integer, Point>();                  //  list of location of each node
	private HashMap<Integer, Integer> connMem = new HashMap<>();                        //  check the path not RE-DRAW twice
	private List<Integer>_path_ =new ArrayList<Integer>();                             //  path of solution
	private int count_line=0;
	JTextArea detail = new JTextArea();

	City path_length=new City(0,0);//I want to pass by reference

	// when running
	private boolean cur_run=false;
	private ArrayList<Point> visited=new ArrayList<Point>();
	private String data_contain="";
	private Thread global_thread;
	private int step=0;
	private boolean LOOP_MOD=true;

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
		playPanel = new JPanel();
		splitPaneChild = new JSplitPane();
		algoPanel = new JPanel();
		scrollAlgo = new JScrollPane();
		algoCmb = new JComboBox<Object>(algo);

		resetBtn = new JButton("Reset");//
		clearBtn = new JButton("Clear console");//
		backBackBtn = new JButton("<<");//
		backBtn = new JButton("<");//
		playBtn = new JButton("Play");//
		pauseBtn = new JButton("Pause");//
		nextBtn = new JButton(">");
		nextNextBtn = new JButton(">>");

		nextNextBtn.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextNextActionPerformed(evt);
            }
		});

		nextBtn.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
		});

		backBtn.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
		});

		backBackBtn.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackBackActionPerformed(evt);
            }
		});

		pauseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPauseActionPerformed(evt);
            }
		});

		resetBtn.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
		});

		playBtn.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
		});

		clearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
		});

		/* Algo panel */

		algoCmb.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		        algoCmbOnChange();
		    }
		});

		algoPanel.setLayout(new BoxLayout(algoPanel, BoxLayout.Y_AXIS));
		algoPanel.add(scrollAlgo);
		algoPanel.setBackground(Color.red);

		scrollAlgo.setViewportView(detail);

		/* SplitPane
		 * - left: console log
		 * - right: graphPanel + inputPanel
		 */
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT); // split the window horizontally
		splitPane.setDividerLocation(200); // initial position of the divider is 200
		splitPane.setLeftComponent(leftPanel);
		//splitPane.setRightComponent(rightPanel);
		splitPane.setRightComponent(splitPaneChild);

		splitPaneChild.setOrientation(JSplitPane.HORIZONTAL_SPLIT); // split the window horizontally
		splitPaneChild.setDividerLocation(600); // initial position of the divider is 200
		splitPaneChild.setLeftComponent(rightPanel);
		splitPaneChild.setRightComponent(algoPanel);

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
		inputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150)); // set the max height to 75 and the max width to (almost) unlimited
		// inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS)); // X_Axis
		// will arrange the content horizontally
		inputPanel.setBackground(new Color(0, 0, 40));
		inputPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 150));
		inputPanel.setBorder(BorderFactory.createEtchedBorder());
		inputPanel.add(text);

		text.setForeground(Color.WHITE);

		btnPanel.add(resetBtn);
		btnPanel.add(clearBtn);
		btnPanel.add(algoCmb);
//		btnPanel.add(startBtn);
		playPanel.add(backBackBtn);
//		btnPanel.add(startBtn);
		playPanel.add(backBtn);
		playPanel.add(playBtn);
		playPanel.add(pauseBtn);
		playPanel.add(nextBtn);
		playPanel.add(nextNextBtn);
		playPanel.setBackground(new Color(0, 0, 40));
		btnPanel.setBackground(new Color(0, 0, 40));
		btnPanel.add(playPanel);
		inputPanel.add(btnPanel);

		// pack(); // calling pack() at the end, will ensure that every layout and size we just defined gets applied before the stuff becomes visible
		window.add(splitPane);
		window.setSize(1200, 700);
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setVisible(true);
		algoCmbOnChange();
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
        if(locations.size()!=0)consoleLog("   ----Node----");
     	for (int i = 0; i < locations.size(); i++) {
     		Point thePoint = (Point) locations.values().toArray()[i];
     		//retype node & location
     		int vertex=(int)locations.keySet().toArray()[i];
     		consoleLog("N"+vertex+" at ("+locations.get(vertex).x+","+locations.get(vertex).y+")");//fix

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
     	consoleLog("");

     	// Node labels.
     	graphicHandle.setColor(Color.WHITE);
     	for (int i = 0; i < locations.size(); i++) {
     		Point thePoint = (Point) locations.values().toArray()[i];
     		graphicHandle.drawString("N " + locations.keySet().toArray()[i], thePoint.x + _SIZE_OF_NODE ,
     					thePoint.y + _SIZE_OF_NODE);
     	}

		//draw some line
		graphicHandle.setStroke(new BasicStroke(2));//draw thin line
		if(count_line!=0)consoleLog("   ----Line----");
		graphicHandle.setColor(Color.cyan);
		for (int i = 0; i < locations.size(); i++) {
			Integer sourceKey = (Integer) nodes.keySet().toArray()[i];
			Point thePoint = (Point) locations.values().toArray()[i];// get location of node_index_i
			for (Integer destKey : (HashSet<Integer>) nodes.values().toArray()[i]) {
				if (!(connMem.containsKey(sourceKey) && connMem.get(sourceKey) == destKey || connMem.containsKey(destKey) && connMem.get(destKey) == sourceKey)) {
					Point destPoint  = locations.get(destKey);
					graphicHandle.drawLine(thePoint.x, thePoint.y, destPoint .x, destPoint .y);
					connMem.put(sourceKey, destKey);
					//cal distance
					Double dis=Math.sqrt(sqr(locations.get(sourceKey).x-locations.get(destKey).x)+sqr(locations.get(sourceKey).y-locations.get(destKey).y));
					//retype path
					consoleLog("N"+(sourceKey>99?sourceKey:sourceKey>9?sourceKey+" ":sourceKey+"  ")+" -> N"+(destKey>99?destKey:destKey>9?destKey+" ":destKey+"  ")+" dis:"+Double.parseDouble(String.format("%.3f", dis)));
				}
			}
		}
		consoleLog("");

		//                                      when running
		if(cur_run) {
			//consoleLog("----process----");
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
		graphicHandle.setColor(Color.YELLOW);//
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

	@SuppressWarnings("deprecation")
	private void graphMousePressed(MouseEvent evt) {
		_selectedNode = nodeSelected(evt.getX(), evt.getY());
		if (_selectedNode < 0 && SwingUtilities.isLeftMouseButton(evt)) {
			//make node
			nodes.put(id, new HashSet<Integer>());
			locations.put(id++, new Point(evt.getX(), evt.getY()));
		}else if (SwingUtilities.isRightMouseButton(evt)) {
			//delete node & stop running
			try {
				global_thread.stop();
			}catch (Exception e) {
				e.printStackTrace();
			}
			_path_.clear();
			_source = -1;
			_dest = -1;
			cur_run=false;
			check_run=false;
			check_Dijkstra=false;
			LOOP_MOD=true;
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
		cur_run=false;
		check_run=false;
		check_Dijkstra=false;
		LOOP_MOD=true;
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

	void btnPauseActionPerformed(ActionEvent evt) {
		System.out.println("PAUSE!!!");
		if(LOOP_MOD) {LOOP_MOD=false;pauseBtn.setText("Continue");}
		else {LOOP_MOD=true;pauseBtn.setText("Pause");}

	}

	void btnBackActionPerformed(ActionEvent evt) {
		System.out.println("BACK");
		step--;
	}

	void btnBackBackActionPerformed(ActionEvent evt) {
		System.out.println("BACKBACK");
		step=0;
	}

	void btnNextActionPerformed(ActionEvent evt) {
		System.out.println("NEXT");
		step++;
	}

	void btnNextNextActionPerformed(ActionEvent evt) {
		System.out.println("NEXTNEXT");
		step=999999;
	}

	@SuppressWarnings("deprecation")
	void btnStartActionPerformed(ActionEvent evt) {
		//clear path & smt after click start
		try {
			global_thread.stop();
		}catch (Exception e) {
			e.printStackTrace();
		}
		check_run=false;
    	check_Dijkstra=false;
		_path_.clear();
		visited.clear();
		step=0;

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
            // TODO this is new
            Runnable runner =
            	    new Runnable(){
            	        public void run(){
            	        	cur_run=true;
            	            System.out.println("LOOP MODE");
            	            for(;true;){
            	            	visited.clear();
            	            	data_contain=new String(solution.run(step,visited));
            	            	System.out.println(data_contain);
            	            	RE_DRAW();
            	            	//delay function
            	            	try {
            						Thread.sleep(1000);
            					} catch (InterruptedException e) {
            						e.printStackTrace();
            					}
            	            	if(data_contain.equals("end"))break;
            	            	if(LOOP_MOD)step++;
            	            }
            	            cur_run=false;
            	            if(solution.path(_source,_dest,_path_)) {
            	            	check_run=true;
            	            	check_Dijkstra=false;
            	            	RE_DRAW();
            	            }
            	            else {/* print something when error occurs */
            	            	RE_DRAW();
            	            	consoleLog("fail run DFS from "+_source+" to "+_dest);
            	            }
            	        }
            	    };
            global_thread=new Thread(runner);
            global_thread.start();
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
            // TODO this is new
            Runnable runner =
            	    new Runnable(){
            	        public void run(){
            	        	cur_run=true;
            	            System.out.println("LOOP MODE");
            	            for(;true;){
            	            	visited.clear();
            	            	data_contain=new String(solution.run(step,visited));
            	            	System.out.println(data_contain);
            	            	RE_DRAW();
            	            	//delay function
            	            	try {
            						Thread.sleep(1000);
            					} catch (InterruptedException e) {
            						e.printStackTrace();
            					}
            	            	if(data_contain.equals("end"))break;
            	            	if(LOOP_MOD)step++;
            	            }
            	            cur_run=false;
            	            if(solution.path(_source,_dest,_path_)) {
            	            	check_run=true;
            	            	check_Dijkstra=false;
            	            	RE_DRAW();
            	            }
            	            else {/* print something when error occurs */
            	            	RE_DRAW();
            	            	consoleLog("fail run BFS from "+_source+" to "+_dest);
            	            }
            	        }
            	    };
            global_thread=new Thread(runner);
            global_thread.start();
        }else if (x == "Dijkstra") {
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
            // TODO this is new
            Runnable runner =
            	    new Runnable(){
            	        public void run(){
            	        	cur_run=true;
            	            System.out.println("LOOP MODE");
            	            for(;true;){
            	            	visited.clear();
            	            	data_contain=new String(solution.run(step,visited));
            	            	System.out.println(data_contain);
            	            	RE_DRAW();
            	            	//delay function
            	            	try {
            						Thread.sleep(1000);
            					} catch (InterruptedException e) {
            						e.printStackTrace();
            					}
            	            	if(data_contain.equals("end"))break;
            	            	if(LOOP_MOD)step++;
            	            }
            	            cur_run=false;
            	            if(solution.path(_source,_dest,path_length,_path_)) {
            	            	check_run=true;
            	            	check_Dijkstra=true;
            	            	RE_DRAW();
            	            }
            	            else {/* print something when error occurs */
            	            	RE_DRAW();
            	            	consoleLog("fail run Dijkstra from "+_source+" to "+_dest);
            	            }
            	        }
            	    };
            global_thread=new Thread(runner);
            global_thread.start();
        }else if (x == "A* Search") {
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
            // TODO this is new
            Runnable runner =
            	    new Runnable(){
            	        public void run(){
            	        	cur_run=true;
            	            System.out.println("LOOP MODE");
            	            for(;true;){
            	            	visited.clear();
            	            	data_contain=new String(solution.run(step,visited));
            	            	System.out.println(data_contain);
            	            	RE_DRAW();
            	            	//delay function
            	            	try {
            						Thread.sleep(1000);
            					} catch (InterruptedException e) {
            						e.printStackTrace();
            					}
            	            	if(data_contain.equals("end"))break;
            	            	if(LOOP_MOD)step++;
            	            }
            	            cur_run=false;
            	            if(solution.path(_source,_dest,path_length,_path_)) {
            	            	check_run=true;
            	            	check_Dijkstra=true;
            	            	RE_DRAW();
            	            }
            	            else {/* print something when error occurs */
            	            	RE_DRAW();
            	            	consoleLog("fail run A* from "+_source+" to "+_dest);
            	            }
            	        }
            	    };
            global_thread=new Thread(runner);
            global_thread.start();
        }
        //RE_DRAW();
	}

	void algoCmbOnChange() {
		//System.out.println("HAHAHA");
		detail.setPreferredSize(new Dimension(algoPanel.getSize().width+200, algoPanel.getSize().height+50));
		detail.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
		detail.setTabSize(4);
		detail.setEditable(false);

		String type = String.valueOf(algoCmb.getSelectedItem());

		if (type == "BFS")
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
		if (type == "DFS")
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
		if (type == "Dijkstra")
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
