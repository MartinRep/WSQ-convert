package wsq_convert;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.*;

public class GUIrunner extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8473377041942261003L;
	JButton folderButton;
	JFileChooser chooser;
	JButton convertButton;
	WsqConvert wsqConvert;
	File folderSelected;
	JLabel terminalOutput;

	public GUIrunner() {
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.anchor = GridBagConstraints.WEST;
		folderButton = new JButton("Choose folder with WSQ files");
		folderButton.addActionListener((ActionEvent e) -> {
			chooser = new JFileChooser();
			chooser.setCurrentDirectory(new java.io.File("."));
			chooser.setDialogTitle("Choose WSQ files folder");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);
			if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				folderSelected = chooser.getSelectedFile();
				ArrayList<String> wsqFiles = new ArrayList<String>();
				WsqConvert.traverseDirectory(".*\\.wsq", chooser.getSelectedFile(), wsqFiles);
				printToTerminal(String.format("Number of files counted: %d", wsqFiles.size()));
				convertButton.setVisible(true);
			} else {
				System.out.println("No Selection ");
			}

		});
		add(folderButton, constraints);

		convertButton = new JButton("CONVERT to PNG");
		convertButton.setVisible(false);
		convertButton.addActionListener((ActionEvent e) -> {
			try {
				convertButton.setText("Processing....");
				ArrayList<String> filesProcessed = (ArrayList<String>) WsqConvert.convertImages(folderSelected);
				printToTerminal(String.format("Files processed: %d", filesProcessed.size()));
				convertButton.setVisible(false);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		constraints.gridy = 2;
		add(convertButton, constraints);
		
		terminalOutput = new JLabel();
		terminalOutput.setForeground(Color.BLACK);
		printToTerminal("Choose a folder containing WSQ files.");
		printToTerminal("Please note:");
		printToTerminal("All files in sub-directories will be included");
		constraints.gridx = 0;
		constraints.gridy = 1;
		add(terminalOutput, constraints);
	

	}
	
	private void printToTerminal(String message) {
		String previousText = terminalOutput.getText();
		if(previousText.compareTo("") == 0) {
			terminalOutput.setText(String.format("<html><i>%s</i><html>", message));
		} else {
			previousText = previousText.substring(0, previousText.lastIndexOf("</i>"));
			terminalOutput.setText(String.format("%s<br>%s</i><html>", previousText, message));
		}
	}

	public static void main(String s[]) {
		JFrame frame = new JFrame("WSQ CONVERT");
		GUIrunner panel = new GUIrunner();
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.getContentPane().add(panel, "North");
		frame.setSize(new Dimension(300, 300));
		frame.setVisible(true);
	}
}
