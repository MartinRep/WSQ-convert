package wsq_convert;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class GUIrunner extends JPanel {

    private static final long serialVersionUID = -8473377041942261003L;
    private JButton folderButton;
    private JFileChooser chooser;
    private JButton convertButton;
    private File folderSelected;
    private JLabel infoLabel;

    public GUIrunner() {
        initializeGUI();
    }

    private void initializeGUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel descLabel = createDescriptionLabel();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(descLabel, constraints);

        folderButton = createFolderButton();
        constraints.gridy = 1;
        add(folderButton, constraints);

        infoLabel = createInfoLabel();
        constraints.gridy = 2;
        add(infoLabel, constraints);

        convertButton = createConvertButton();
        constraints.gridy = 3;
        add(convertButton, constraints);
    }

    private JLabel createDescriptionLabel() {
        JLabel label = new JLabel();
        label.setFont(new Font("Verdana", Font.PLAIN, 12));
        label.setText("<html><i>1. Select WSQ directory.<br/>2. Number of files will be displayed.<br/>3. Press CONVERT.<br/>4. PNG files will be created for each WSQ.</i></html>");
        return label;
    }

    private JButton createFolderButton() {
        JButton button = new JButton("Select WSQ Directory...");
        button.addActionListener(this::folderButtonClicked);
        return button;
    }

    private void folderButtonClicked(ActionEvent e) {
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            folderSelected = chooser.getSelectedFile();
            try {
                List<String> wsqFiles = WsqConvert.fetchFilePathsMatchingPattern(".*\\.wsq", folderSelected);
                infoLabel.setText(String.format("Total files selected: %d", wsqFiles.size()));
                convertButton.setVisible(true);
            } catch (Exception ex) {
                infoLabel.setText("An error occurred.");
            }
        }
    }

    private JLabel createInfoLabel() {
        JLabel label = new JLabel();
        label.setFont(new Font("Verdana", Font.BOLD, 15));
        return label;
    }

    private JButton createConvertButton() {
        JButton button = new JButton("Convert");
        button.setVisible(false);
        button.addActionListener(this::convertButtonClicked);
        return button;
    }

    private void convertButtonClicked(ActionEvent e) {
        try {
            List<String> filesProcessed = WsqConvert.convertImagesToPng(folderSelected);
            infoLabel.setText(String.format("Processed files: %d", filesProcessed.size()));
            convertButton.setVisible(false);
        } catch (IOException e1) {
            infoLabel.setText("An error occurred.");
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("WSQ CONVERT");
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        frame.getContentPane().add(new GUIrunner(), BorderLayout.NORTH);
        frame.setSize(new Dimension(400, 400));
        frame.setVisible(true);
    }
}
