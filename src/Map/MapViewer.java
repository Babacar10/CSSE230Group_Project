package Map;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class MapViewer extends JFrame {

	private HashMap<String, Integer> teamList = new HashMap<String, Integer>();
	JComboBox teamListDropdown1;
	JComboBox teamListDropdown2;
	String filename;
	ControlPanel controlPanel;
	
	public MapViewer(String filename) throws IOException {
		super("Map!!!");
		this.filename = filename;
		this.setSize(1000, 750);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container content = getContentPane();
		content.setLayout(new BorderLayout());
		controlPanel = new ControlPanel();
		content.add(controlPanel, BorderLayout.NORTH);
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
	
	class GoButton extends JButton {
		public GoButton() {
			super("GO");
			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					String team1 = String.valueOf(teamListDropdown1.getSelectedItem());
					String team2 = String.valueOf(teamListDropdown2.getSelectedItem());
					String sortedBy = "none";
					if (controlPanel.sortGroup.getSelection() != null) {
						sortedBy = controlPanel.sortGroup.getSelection().getActionCommand();
					}		
					
					ArrayList<String> showing = new ArrayList<String>();
					
					for (JCheckBox cbox : controlPanel.checkBoxes) {
						if (cbox.isSelected()) {
							showing.add(cbox.getActionCommand());
						}
					}
					System.out.println(team1 + ": "+ teamList.get(team1) + " to " + team2 + ": " + teamList.get(team2) + ", sorted by: " + sortedBy + ", showing: " + showing.toString());					
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
			// Add dropdowns
			ArrayList<String> teamArray = new ArrayList<String>();
			for (String val : teamList.keySet()) {
				teamArray.add(val);
			}
			Collections.sort(teamArray);
			String[] array = teamArray.toArray(new String[teamArray.size()]);
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
			JRadioButton sortByDistanceButton = new JRadioButton("Distance");
			JRadioButton sortByCompetitionButton = new JRadioButton("Competition");
			sortByCompetitionButton.setActionCommand("comp");
			sortByDistanceButton.setActionCommand("dist");
			sortByTimeButton.setActionCommand("time");
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
			JLabel timeTotal = new JLabel("Time: 5hr 36min");	
			timeTotal.setFont(new Font("Serif", Font.PLAIN, 18));			
			Box boxTwo = Box.createVerticalBox();
			boxTwo.add(timeTotal);
			JLabel distTotal = new JLabel("Distance: 372mi");
			distTotal.setFont(new Font("Serif", Font.PLAIN, 18));
			boxTwo.add(distTotal);
			JLabel seedAvg = new JLabel("Avg. Rank: 13.4");
			seedAvg.setFont(new Font("Serif", Font.PLAIN, 18));
			boxTwo.add(seedAvg);				
			resultsPanel.add(boxTwo);
			// End results panel
			
			this.add(resultsPanel);
			// Add GO button
			JButton goButton = new GoButton();
			
			this.add(goButton);
			
			// End GO button
			
			

		}
	}
} 