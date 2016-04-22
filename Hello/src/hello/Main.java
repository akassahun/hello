package hello;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSplitPane;

public class Main {
	public static void main(String[] argv) throws Exception {
		JFrame frame = new JFrame("Main");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton topComponent = new JButton("top");
		JButton bottomComponent = new JButton("bottom");

		JSplitPane vpane = 
				new JSplitPane(JSplitPane.VERTICAL_SPLIT, topComponent, bottomComponent);

		boolean b = vpane.isContinuousLayout(); // false by default

		// Set the split pane to continuously resize the child components which
		// the divider is dragged
		vpane.setContinuousLayout(true);
		
		frame.getContentPane().add(vpane, BorderLayout.CENTER);
		
		frame.pack();

		frame.setVisible(true);
	}
}