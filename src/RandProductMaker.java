import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.RandomAccessFile;

public class RandProductMaker extends JFrame {
    private JButton addButton;
    private JTextField nameTextField, descriptionTextField, IDtextField, costTextField, countTextField;
    private JLabel nameLabel, descriptionLabel, IDLabel, costLabel, countLabel;
    private boolean canAdd = true;
    private int writeCount = 0;
    private RandomAccessFile productFile;
    private String finalID, finalName, finalDescription;
    private double cost;


    public RandProductMaker() {

        setTitle("RandProductMaker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);


//        File workingDirectory = new File(System.getProperty("user.dir"));
//        Path outfile = Paths.get(workingDirectory.getPath() + "\\src\\data.bin");

        JPanel mainPanel = new JPanel();


        JPanel textareasPanel = new JPanel();
        textareasPanel.setLayout(new GridLayout(2, 4));
        nameLabel = new JLabel("Name");
        descriptionLabel = new JLabel("Description");
        IDLabel = new JLabel("ID");
        costLabel = new JLabel("Cost");
        countLabel = new JLabel("Count");
        nameTextField = new JTextField();

        textareasPanel.add(IDLabel);
        textareasPanel.add(nameLabel);
        textareasPanel.add(descriptionLabel);
        textareasPanel.add(costLabel);


        IDtextField = new JTextField();
        IDtextField.setPreferredSize(new Dimension(150, 150));
        textareasPanel.add(IDtextField);

        nameTextField.setPreferredSize(new Dimension(150, 150));
        textareasPanel.add(nameTextField);


        descriptionTextField = new JTextField();
        descriptionTextField.setPreferredSize(new Dimension(150, 150));
        textareasPanel.add(descriptionTextField);


        costTextField = new JTextField();
        costTextField.setPreferredSize(new Dimension(150, 150));
        textareasPanel.add(costTextField);

        countTextField = new JTextField();
        countTextField.setEditable(false);
        countTextField.setPreferredSize(new Dimension(150, 150));


        mainPanel.add(textareasPanel, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String finalID = fixStrings(IDtextField.getText(), 6); //fields for ID
                String finalName = fixStrings(nameTextField.getText(), 35); //fields for name
                String finalDescription = fixStrings(descriptionTextField.getText(), 75); // fields for description
                double cost = Double.parseDouble(costTextField.getText());

                if (canAdd) {
                    try {
                        if (productFile == null) {
                            productFile = new RandomAccessFile("data.dat", "rw");
                        }


                        productFile.seek(productFile.length());
                        productFile.writeBytes(finalName);
                        productFile.writeBytes(finalDescription);
                        productFile.writeBytes(finalID);
                        productFile.writeDouble(cost);
                        Product product = new Product(finalID, finalName, finalDescription, cost);
                        String formattedRecord = product.getFormattedRandomAccessRecord();
                        System.out.println("Wrote file to disk.");
                        writeCount += 1;
                        countTextField.setText(String.valueOf(writeCount));
                        nameTextField.setText("");
                        descriptionTextField.setText("");
                        IDtextField.setText("");
                        costTextField.setText("");

                    } catch (IOException ae) {
                        System.out.println(ae.getMessage());
                        ae.printStackTrace();
                        System.exit(0);
                    }
                }


            }
        });

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.add(countLabel);
        topPanel.add(countTextField);
        mainPanel.add(topPanel, BorderLayout.NORTH);


        buttonPanel.add(addButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);


        add(mainPanel);


        setVisible(true);


    }

    private String fixStrings(String originalString, int fixedSize) {
        String fixedString = "";

        if (originalString.length() > fixedSize) {
            canAdd = false;
            JOptionPane.showMessageDialog(this, "Too Many Characters In Field");
        } else {
            int spacesNeeded = fixedSize - originalString.length();
            String Spaces = "";
            for (int i = 0; i < spacesNeeded; i++) {
                Spaces += " ";
            }
            fixedString = originalString + Spaces;
        }
        return fixedString;
    }


    private static class Product {
        private String id;
        private String name;
        private String description;
        private double cost;

        public Product(String id, String name, String description, double cost) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.cost = cost;
        }

        public String getFormattedRandomAccessRecord() {
            return String.format("%-20s%-20s%-20s%-20s", id, name, description, cost);
        }
    }
}