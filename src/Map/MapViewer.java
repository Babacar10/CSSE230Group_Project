package Map;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Graph.Graph;
import Graph.Graph.Edge;
import Graph.Graph.Node;



public class MapViewer extends JFrame {

	private HashMap<String, Integer> teamList = new HashMap<String, Integer>();
	JComboBox teamListDropdown1;
	JComboBox teamListDropdown2;
	String filename;
	Graph graph;
	ControlPanel controlPanel;
	JPanel mapPanel;
	ArrayList<Line2D> lines = new ArrayList<Line2D>();
	JLabel timeTotal = new JLabel("Time: 5hr 36min");			
	JLabel distTotal = new JLabel("");
	JLabel seedAvg = new JLabel("");
	
	
	public MapViewer(String filename, Graph graph) throws IOException {
		super("Map!!!");
		this.graph = graph;
		this.filename = filename;
		this.setSize(1000, 750);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container content = getContentPane();
		content.setLayout(new BorderLayout());
		controlPanel = new ControlPanel();
		content.add(controlPanel, BorderLayout.NORTH);
		JPanel plannerPanel = new JPanel();
		JLabel tripPlannerLabel = new JLabel("Trip Planner");
		
		
		
		
//		ArrayList<String> teamArray = new ArrayList<String>();
//		for (String val : teamList.keySet()) {
//			teamArray.add(val);
//		}
//		Collections.sort(teamArray);
//		String[] array = teamArray.toArray(new String[teamArray.size()]);
//		JComboBox tripPlannerDropdown = new JComboBox(array);
//		tripPlannerDropdown.setRenderer(new MyComboBoxRenderer("Starting Location"));
//		tripPlannerDropdown.setSelectedIndex(-1);
//		
//		plannerPanel.add(tripPlannerDropdown);
//		JSplitPane pane = new JSplitPane( JSplitPane.VERTICAL_SPLIT, 
//                plannerPanel, controlPanel );
//		this.add(pane);
		
		
		
		
		
		
		mapPanel = new JPanel();
		ImageIcon img = new ImageIcon("src/Map/mapimg.png");
		Image image = img.getImage();
		Image newimg = image.getScaledInstance(1000,  575,  java.awt.Image.SCALE_SMOOTH);
		img = new ImageIcon(newimg); 
		mapPanel.add(new JLabel(img));
		this.add(mapPanel);
//		this.add(new JLabel("hello"));
//		this.pack();
		mapPanel.setSize(new Dimension(100, 100));
		mapPanel.repaint();
		this.repaint();
		this.setResizable(false);
		this.setVisible(true);
	
		
	
	}
	
	
	
	public void addTeamsAndSeeds() throws IOException {
		FileInputStream teams_seeds = null;
		XSSFWorkbook wb = null;  
		
		ArrayList<Integer> seeds = new ArrayList<Integer>();
		try {
			teams_seeds = new FileInputStream(new File(this.filename));
			wb = new XSSFWorkbook(teams_seeds);   
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}  
		ArrayList<String> teamNames = new ArrayList<String>();
		//creating a Sheet object to retrieve the object  
		XSSFSheet sheet = wb.getSheetAt(0);  
		//evaluating cell type   
		FormulaEvaluator formulaEvaluator=wb.getCreationHelper().createFormulaEvaluator();  
		for(Row row: sheet)     //iteration over row using for each loop  
		{
		teamList.put(row.getCell(0).getStringCellValue(), (int)row.getCell(1).getNumericCellValue());
		}  
		
	}

	public static void main(String[] args) {
		
	}
	
	public void drawConnectingLine(String team1, String team2) {
		try {
			Hashtable<Integer, Node> nodes = graph.getNodes();
			int x1 = nodes.get(teamList.get(team1)).getX();
			int x2 = nodes.get(teamList.get(team2)).getX();
			int y1 = nodes.get(teamList.get(team1)).getY();
			int y2 = nodes.get(teamList.get(team2)).getY();
			Line2D line = new Line2D.Float(x1, y1, x2, y2);
			lines.add(line);
			this.repaint();
		}catch (NullPointerException n){
			System.out.println("SELECT TWO TEAMS");
		}
		
	}
	
	public void drawAllPaths() {
		Hashtable<Integer, Node> nodes = graph.getNodes();
		for (Node node : nodes.values()) {
			ArrayList<Edge> edges = node.edges;
			for (Edge edge : edges) {
				drawConnectingLine(edge.node1.teamName, edge.node2.teamName);
			}
		}
	}
	
	public void paint(Graphics gp) {
		super.paint(gp);
		Graphics2D graphics = (Graphics2D) gp;
//		Line2D line = new Line2D.Float(0, 0, 150, 220);
//	    graphics.draw(line);
	    for (Line2D eachLine : lines) {
	    	graphics.setColor(Color.RED);
	    	float[] dashingPattern = {5f, 5f};
	    	Stroke stroke3 = new BasicStroke(4f, BasicStroke.CAP_ROUND,
	    	        BasicStroke.JOIN_ROUND, 2.0f, null, 0.0f);
	    	graphics.setStroke(stroke3);
	    	
//	    	Draw Arrowed Line
	    	drawArrowLine(gp, (int)eachLine.getX1(), (int)eachLine.getY1(), (int)eachLine.getX2(), (int)eachLine.getY2(), 10, 20);
	    	
	    	
	    	
	    	// Draw Normal line
//	    	graphics.draw(eachLine);
	    }
		
	}
	
	private void drawArrowLine(Graphics g, int x1, int y1, int x2, int y2, int d, int h) {
	    int dx = x2 - x1, dy = y2 - y1;
	    double D = Math.sqrt(dx*dx + dy*dy);
	    double xm = D - d, xn = xm, ym = h, yn = -h, x;
	    double sin = dy / D, cos = dx / D;

	    x = xm*cos - ym*sin + x1;
	    ym = xm*sin + ym*cos + y1;
	    xm = x;

	    x = xn*cos - yn*sin + x1;
	    yn = xn*sin + yn*cos + y1;
	    xn = x;

	    int[] xpoints = {x2, (int) xm, (int) xn};
	    int[] ypoints = {y2, (int) ym, (int) yn};

	    g.drawLine(x1, y1, x2, y2);
	    g.fillPolygon(xpoints, ypoints, 3);
	}
	
	class GoButton extends JButton {
		public GoButton() {
			super("GO");
			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					String sortedBy = "none";
					if (controlPanel.sortGroup.getSelection() != null) {
						sortedBy = controlPanel.sortGroup.getSelection().getActionCommand();
					}	
					int sortVal = 0;
					if (sortedBy != "none") {
						sortVal = Integer.parseInt(sortedBy);
					}
					
					String team1 = String.valueOf(teamListDropdown1.getSelectedItem());
					String team2 = String.valueOf(teamListDropdown2.getSelectedItem());

					if (teamList.containsKey(team1) && teamList.containsKey(team2) && team1 != team2) {
					// FIXME: change from 0 to sortVal 
					ArrayList<Node> shortestPath = graph.shortestPath(0, teamList.get(team1), teamList.get(team2));
					lines = new ArrayList<Line2D>();
					int totalDistance = 0;
					int totalSeed = 0;
					int totalTime = 0;					
					totalSeed += shortestPath.get(0).seed + teamList.get(team2);
					ArrayList<Edge> lastEdges = shortestPath.get(0).edges;
					for (Edge edge : lastEdges) {
						System.out.println("node 1: "+ edge.node1.teamName + " node 2: " + edge.node2.teamName);
						if (edge.node1.seed == shortestPath.get(0).seed && edge.node2.seed == teamList.get(team2) || edge.node1.seed == teamList.get(team2) && edge.node2.seed == shortestPath.get(0).seed ) {
							totalDistance += edge.distanceCost;
							totalTime += edge.timeCost;							
						}
					}
					for (int i  = 0; i < shortestPath.size()-1; i ++) {
						drawConnectingLine(shortestPath.get(shortestPath.size()-i-1).teamName, shortestPath.get(shortestPath.size()-i-2).teamName);
						totalSeed += shortestPath.get(shortestPath.size()-i-1).seed;
						ArrayList<Edge> edges = shortestPath.get(shortestPath.size()-i-1).edges;
						for (Edge edge : edges) {
							if (edge.node1 == shortestPath.get(shortestPath.size()-i-1) && edge.node2 == shortestPath.get(shortestPath.size()-i-2) || edge.node1 == shortestPath.get(shortestPath.size()-i-2) && edge.node2 == shortestPath.get(shortestPath.size()-i-1) ) {
								totalDistance += edge.distanceCost;
								totalTime += edge.timeCost;
							}
						}
						
					}
					
					drawConnectingLine(shortestPath.get(0).teamName, team2);
					
					
					ArrayList<String> showing = new ArrayList<String>();
					
					for (JCheckBox cbox : controlPanel.checkBoxes) {
						if (cbox.isSelected()) {
							showing.add(cbox.getActionCommand());
						}
					}
					if (showing.contains("showTime" )) {
						timeTotal.setText("Time: " + (totalTime) / 60 + "hr "+ (totalTime -  (totalTime / 60)+"min"));
					}else {
						timeTotal.setText(""); 
					}
					if (showing.contains("showDist")) {
						distTotal.setText("Distance: " + totalDistance + " miles");
					}else {
						distTotal.setText("");
					}
					if (showing.contains("showRank")) {
						seedAvg.setText("Avg Rank: "+ totalSeed / (shortestPath.size()+1));
					}else {
						seedAvg.setText("");
					}
					
					System.out.println(team1 + ": "+ teamList.get(team1) + " to " + team2 + ": " + teamList.get(team2) + ", sorted by: " + sortedBy + ", showing: " + showing.toString());
					repaint();
				}else {
					System.out.println("Please pick two teams!");
				}
				}
			});
		}
	}

	
	class MyComboBoxRenderer extends JLabel implements ListCellRenderer {
		  private String _title;

		  public MyComboBoxRenderer(String title) {
		    _title = title;
		  }

		  public Component getListCellRendererComponent(JList list, Object value,
		      int index, boolean isSelected, boolean hasFocus) {
		    if (index == -1 && value == null)
		      setText(_title);
		    else
		      setText(value.toString());
		    return this;
		  }
		}
	
	class ControlPanel extends JPanel {
		
		public ButtonGroup sortGroup;
		public ArrayList<JCheckBox> checkBoxes = new ArrayList<JCheckBox>();
		
		public ControlPanel() throws IOException {
			TitledBorder border = BorderFactory.createTitledBorder(
					BorderFactory.createLoweredBevelBorder(), "NBA Roadmap");
			border.setTitleJustification(TitledBorder.LEFT);
			this.setBorder(border);
			this.setLayout(new FlowLayout());
			addTeamsAndSeeds();
			ArrayList<String> teamArray = new ArrayList<String>();
			for (String val : teamList.keySet()) {
				teamArray.add(val);
			}
			Collections.sort(teamArray);
			String[] array = teamArray.toArray(new String[teamArray.size()]);
			
			
			
			
			
			
			// Add dropdowns
			
			teamListDropdown1 = new JComboBox(array);
			teamListDropdown2 = new JComboBox(array);
			teamListDropdown1.setRenderer(new MyComboBoxRenderer("START"));
			teamListDropdown1.setSelectedIndex(-1);
			teamListDropdown2.setRenderer(new MyComboBoxRenderer("END"));
			teamListDropdown2.setSelectedIndex(-1);
			this.add(teamListDropdown1);
			JLabel toLabel = new JLabel("TO");
			this.add(toLabel);
			this.add(teamListDropdown2);
			// End dropdowns
			
			// Add sort buttons
			JLabel sortLabel = new JLabel("Sort By:");
			sortLabel.setFont(new Font("Serif", Font.PLAIN, 18));
			this.add(sortLabel);			
			JRadioButton sortByTimeButton = new JRadioButton("Time");
			sortByTimeButton.setSelected(true);
			JRadioButton sortByDistanceButton = new JRadioButton("Distance");
			JRadioButton sortByCompetitionButton = new JRadioButton("Competition");
			sortByCompetitionButton.setActionCommand("2");
			sortByDistanceButton.setActionCommand("0");
			sortByTimeButton.setActionCommand("1");
			this.sortGroup = new ButtonGroup();
			Box sortBox = Box.createVerticalBox();
			sortBox.add(sortLabel);
			sortBox.add(sortByTimeButton);
			sortBox.add(sortByDistanceButton);
			sortBox.add(sortByCompetitionButton);			
			sortGroup.add(sortByCompetitionButton);
			sortGroup.add(sortByTimeButton);
			sortGroup.add(sortByDistanceButton);
			this.add(sortBox);
			// End sort buttons
			
			// Add show buttons			
			JLabel showLabel = new JLabel("Show:");
			showLabel.setFont(new Font("Serif", Font.PLAIN, 18));
			this.add(showLabel);			
			JCheckBox showTimeButton = new JCheckBox("Time");			
			JCheckBox showDistanceButton = new JCheckBox("Distance");
			JCheckBox showRankButton = new JCheckBox("Rank");
			this.checkBoxes.add(showTimeButton);
			this.checkBoxes.add(showDistanceButton);
			this.checkBoxes.add(showRankButton);
			showRankButton.setActionCommand("showRank");
			showDistanceButton.setActionCommand("showDist");
			showTimeButton.setActionCommand("showTime");
			Box showBox = Box.createVerticalBox();
			showBox.add(showLabel);
			showBox.add(showTimeButton);
			showBox.add(showDistanceButton);
			showBox.add(showRankButton);			
			this.add(showBox);
			// End show buttons
			
			// Add results panel
			JPanel resultsPanel = new JPanel();
			timeTotal = new JLabel("");	
			timeTotal.setFont(new Font("Serif", Font.PLAIN, 18));			
			Box boxTwo = Box.createVerticalBox();
			boxTwo.add(timeTotal);
			distTotal = new JLabel("");
			distTotal.setFont(new Font("Serif", Font.PLAIN, 18));
			boxTwo.add(distTotal);
			seedAvg = new JLabel("");
			seedAvg.setFont(new Font("Serif", Font.PLAIN, 18));
			boxTwo.add(seedAvg);				
			resultsPanel.add(boxTwo);
			// End results panel
			
			this.add(resultsPanel);
			// Add GO button
			JButton goButton = new GoButton();
			
			this.add(goButton);
			
			
			
			// End GO button
			JButton drawAll = new JButton("Show all Paths");
			drawAll.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					drawAllPaths();
				}
				});
			drawAll.setFocusPainted(false);
			drawAll.setBackground(new Color(59, 89, 182));
			drawAll.setFont(new Font("Tahoma", Font.BOLD, 12));
			drawAll.setForeground(Color.WHITE);
			this.add(drawAll);
			
			

		}
	}
} 