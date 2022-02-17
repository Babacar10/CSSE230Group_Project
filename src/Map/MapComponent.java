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
			teams_seeds = new FileInputStream(new File("src/team-seed-x-y.xlsx"));
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
			graph.addNode((int)row.getCell(1).getNumericCellValue(), row.getCell(0).getStringCellValue(), (int)row.getCell(2).getNumericCellValue(), (int)row.getCell(3).getNumericCellValue());
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
		ArrayList<Graph.Node> cool = graph.tripPlanner(20, 1400);
		System.out.print(cool.size() );
		for(int i =0; i<cool.size();i++) {
			Graph.Node hi = cool.get(i);
			System.out.print(hi.teamName);
		}
		MapViewer mv = new MapViewer("src/team-seed-x-y.xlsx", graph);
//		mv.addTeamsAndSeeds();
		
		
		
		
		  
	}
}
