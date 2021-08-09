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
	JLabel descLabel;
	JLabel infoLabel;

	public GUIrunner() {
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		
		descLabel = new JLabel();
		descLabel.setForeground(Color.BLACK);
		descLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
		labelPrintf("<b>1.</b> Vyber adresár s WSQ súbormi.", descLabel, true);
		labelPrintf("<b>2.</b> Program vypíše koľko súborov bude spracovaných.", descLabel, false);
		labelPrintf("<b>3.</b> Stlač CONVERT", descLabel, false);
		labelPrintf("<b>4.</b> Ku každému WSQ súboru sa vytvorý PNG súbor", descLabel, false);
		labelPrintf("<br/>Poznámka:", descLabel, false);
		labelPrintf("Z vybraného adresára sa spracujú súbory aj z podadresárov<br/>", descLabel, false);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.anchor = GridBagConstraints.NORTH;
		add(descLabel, constraints);
		
		folderButton = new JButton("Vyber adresar s WSQ súbormi...");
		folderButton.addActionListener((ActionEvent e) -> {
			chooser = new JFileChooser();
			chooser.setCurrentDirectory(new java.io.File("."));
			chooser.setDialogTitle("Vyber adresar s WSQ súbormi...");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);
			if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				folderSelected = chooser.getSelectedFile();
				ArrayList<String> wsqFiles = new ArrayList<String>();
				WsqConvert.traverseDirectory(".*\\.wsq", chooser.getSelectedFile(), wsqFiles);
				labelPrintf(String.format("Počet vybraných súborov: %d", wsqFiles.size()), infoLabel, true);
				convertButton.setVisible(true);
			}
		});
		constraints.gridx = 0;
		constraints.gridy = 1;
		add(folderButton, constraints);
		
		infoLabel = new JLabel();
		infoLabel.setForeground(Color.BLACK);
		infoLabel.setFont(new Font("Verdana", Font.BOLD, 15) );
		constraints.gridx = 0;
		constraints.gridy = 2;
		add(infoLabel, constraints);
		
		convertButton = new JButton("Konvertovať");
		convertButton.setVisible(false);
		convertButton.addActionListener((ActionEvent e) -> {
			try {
				ArrayList<String> filesProcessed = (ArrayList<String>) WsqConvert.convertImages(folderSelected);
				labelPrintf(String.format("Spracovaných súborov: %d", filesProcessed.size()), infoLabel, false);
				convertButton.setVisible(false);
			} catch (IOException e1) {
				infoLabel.setForeground(Color.RED);
				infoLabel.setFont(new Font("Verdana", Font.BOLD, 8));
				infoLabel.setText(e1.getMessage());
			}
		});
		constraints.gridx = 0;
		constraints.gridy = 3;
		add(convertButton, constraints);

	}
	
	private void labelPrintf(String message, JLabel label, boolean newLine) {
		String previousText = label.getText();
		if(newLine == true) {
			label.setText(String.format("<html><i>%s</i><html>", message));
		} else {
			previousText = previousText.substring(0, previousText.lastIndexOf("</i>"));
			label.setText(String.format("%s<br>%s</i><html>", previousText, message));
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
		frame.setSize(new Dimension(400, 400));
		frame.setVisible(true);
	}
}
