package Map;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
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
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
	
	public MapViewer(String filename) throws IOException {
		super("Map!!!");
		this.filename = filename;
		this.setSize(800, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container content = getContentPane();
		content.setLayout(new BorderLayout());
		content.add(new ControlPanel(), BorderLayout.NORTH);
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
					System.out.println(team1 + ": "+ teamList.get(team1) + " to " + team2 + ": " + teamList.get(team2));					
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
		public ControlPanel() throws IOException {
			TitledBorder border = BorderFactory.createTitledBorder(
					BorderFactory.createLoweredBevelBorder(), "Control Panel");
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
			this.add(teamListDropdown2);
			// End dropdowns
			
			// Add buttons
			JButton goButton = new GoButton();
			this.add(goButton);
			JRadioButton sortByTimeButton = new JRadioButton("Time");
			JRadioButton sortByDistanceButton = new JRadioButton("Distance");
			JRadioButton sortByCompetitionButton = new JRadioButton("Competition");
			ButtonGroup sortGroup = new ButtonGroup();
			sortGroup.add(sortByCompetitionButton);
			sortGroup.add(sortByTimeButton);
			sortGroup.add(sortByDistanceButton);
			this.add(sortByCompetitionButton);
			this.add(sortByTimeButton);
			this.add(sortByDistanceButton);
//		    group.add(birdButton);
//		    group.add(catButton);
//		    group.add(dogButton);
//		    group.add(rabbitButton);
//		    group.add(pigButton);

		}
	}
} 