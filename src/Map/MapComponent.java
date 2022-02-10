package Map;

import Graph.Graph;
import java.io.File;  
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;  
import org.apache.poi.ss.usermodel.FormulaEvaluator;  
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;  

public class MapComponent {
	
	
	
	public static void addAllNodes(Graph graph) throws IOException{
		FileInputStream teams_seeds = null;
		XSSFWorkbook wb = null;  
		
		ArrayList<Integer> seeds = new ArrayList<Integer>();
		try {
			teams_seeds = new FileInputStream(new File("src/Teams-Seeds.xlsx"));
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
		for(Cell cell: row)    //iteration over cell using for each loop  
		{  
		switch(formulaEvaluator.evaluateInCell(cell).getCellType())  
		{  
		case Cell.CELL_TYPE_NUMERIC: // if the cell's contents are a numeric
			seeds.add((int)cell.getNumericCellValue());
		break;  
		case Cell.CELL_TYPE_STRING: // if the cell's contents are a string
			teamNames.add(cell.getStringCellValue());
		break;  
		}  
		}  
		}  
		// Adding all nodes
		for (int i = 0; i < seeds.size(); i++) {
			graph.addNode(seeds.get(i), teamNames.get(i));
		}
	}
	
	public static void addAllEdges(Graph graph) throws IOException{
		FileInputStream teams_seeds = null;
		XSSFWorkbook wb = null;  
		ArrayList<String> teamNames = new ArrayList<String>();
		ArrayList<Integer> seeds = new ArrayList<Integer>();
		try {
			teams_seeds = new FileInputStream(new File("src/seed1-seed2-tcost-dcost.xlsx"));
			wb = new XSSFWorkbook(teams_seeds);   
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}  
		
		//creating a Sheet object to retrieve the object  
		XSSFSheet sheet = wb.getSheetAt(0);    
		for(Row row: sheet)     //iteration over row using for each loop  
		{  
			ArrayList<Integer> inputArgs = new ArrayList<Integer>();
		for(Cell cell: row)    //iteration over cell using for each loop  
		{  
		  inputArgs.add((int)cell.getNumericCellValue());
		}  
			graph.addEdge(inputArgs.get(0), inputArgs.get(1), inputArgs.get(2), inputArgs.get(3));
		}  
		
	}
	
	public static void main(String[] args) throws IOException {
		
		Graph graph = new Graph(); 
		addAllNodes(graph);
		addAllEdges(graph);
		MapViewer mv = new MapViewer("src/Teams-Seeds.xlsx");
//		mv.addTeamsAndSeeds();
		
		
		
		
		  
	}
}
