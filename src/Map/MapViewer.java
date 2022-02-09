package Map;
import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;


public class MapViewer extends JFrame {

	public MapViewer() {
		super("Map!!!");
		this.setSize(800, 500);// this joke never exists because of the size change
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container content = getContentPane();
		content.setLayout(new BorderLayout());
		content.add(new ControlPanel(), BorderLayout.NORTH);
		this.setVisible(true);

	}

	public static void main(String[] args) {
		
	}

	
	class ControlPanel extends JPanel {
		
	}
}