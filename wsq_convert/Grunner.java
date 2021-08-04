package wsq_convert;


import javax.swing.*;
import java.awt.event.*;
import java.awt.*;


public class Grunner extends JPanel
implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8473377041942261003L;

	
	
	JButton go;

	JFileChooser chooser;
	String choosertitle;

	public Grunner() {
		go = new JButton("Vyber adresar s otlackami");
		go.addActionListener(this);
		add(go);
	}

	public void actionPerformed(ActionEvent e) {
		int result;

		chooser = new JFileChooser(); 
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle(choosertitle);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		//
		// disable the "All files" option.
		//
		chooser.setAcceptAllFileFilterUsed(false);
		//    
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
			System.out.println("getCurrentDirectory(): " 
					+  chooser.getCurrentDirectory());
			System.out.println("getSelectedFile() : " 
					+  chooser.getSelectedFile());
		}
		else {
			System.out.println("No Selection ");
		}
	}

	public Dimension getPreferredSize(){
		return new Dimension(400, 400);
	}

	public static void main(String s[]) {
		JFrame frame = new JFrame("WSQ CONVERT");
		Grunner panel = new Grunner();
		frame.addWindowListener(
				new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						System.exit(0);
					}
				}
				);
		frame.getContentPane().add(panel,"Center");
		frame.setSize(panel.getPreferredSize());
		frame.setVisible(true);
	}
}
