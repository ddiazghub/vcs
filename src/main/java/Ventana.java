import svcs.Vcs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class Ventana extends JFrame {

    Vcs vcs = new Vcs("");
    String root;
    String user="VCS User";

    JTextArea textArea = new JTextArea();
    JScrollPane scrollPane = new JScrollPane(textArea);

    // Buttons
    JButton vcsButton = new JButton("Create vcs");
    JButton createButton = new JButton("Create File");
    JButton openButton = new JButton("Open File");
    JButton addButton = new JButton("Add");
    JButton commitButton = new JButton("Commit");
    JButton checkoutButton = new JButton("Checkout");
    JButton configButton = new JButton("Config");

    JLabel filenameJLabel = new JLabel("");

    //Panel for log/help function
    JPanel rightPanel = new JPanel(new BorderLayout());
    JTextArea rightTextArea = new JTextArea();
    JButton logButton = new JButton("Log");
    JButton helpButton = new JButton("Help");


    public Ventana(){
        setTitle("Control de Versiones");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        textArea.setEditable(false);
        rightTextArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        rightTextArea.setLineWrap(true);
        rightTextArea.setWrapStyleWord(true);
        vcs.config(user);

        styleFilenameLabel();
        add(scrollPane, BorderLayout.CENTER);
        add(filenameJLabel, BorderLayout.NORTH);
        addButtons(vcsButton,createButton,openButton,addButton,commitButton,logButton,checkoutButton,configButton,helpButton);
        addRightPanel();
        addActionListener();
    }
    public void styleFilenameLabel() {
        filenameJLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centrar texto
        filenameJLabel.setFont(new Font("Verdana", Font.BOLD, 16)); // Cambiar fuente y tamaño
        filenameJLabel.setForeground(new Color(60, 63, 65)); // Cambiar color del texto
        filenameJLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Añadir un poco de espacio alrededor del texto
    }
    public void addRightPanel() {
        rightPanel.setPreferredSize(new Dimension(200, getHeight()));
        rightPanel.add(new JScrollPane(rightTextArea), BorderLayout.CENTER);

        // Panel for buttons to the right of the text area
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5)); // Espaciado entre botones


        rightPanel.add(buttonPanel, BorderLayout.EAST);
        rightPanel.setBackground(new Color(240, 240, 240));
        add(rightPanel, BorderLayout.EAST);
    }

    public void addButtons(JButton... buttons){
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        for (JButton button : buttons) {
            styleButton(button);
            buttonPanel.add(button);
            buttonPanel.add(Box.createRigidArea(new Dimension(5, 0))); // Espacio fijo entre botones
        }
        buttonPanel.add(Box.createHorizontalGlue());

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(200, 200, 200)); // Color del fondo
        button.setForeground(new Color(20, 20, 20)); // Color del texto
        button.setPreferredSize(new Dimension(100, 40)); // Tamaño de botón
        button.setFont(new Font("Arial", Font.BOLD, 12)); // Fuente y tamaño de letra
    }
    public void addActionListener(){
        vcsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // Solo permitir selección de directorios
                int result = fileChooser.showOpenDialog(Ventana.this);

                if (result == JFileChooser.APPROVE_OPTION) {
                    root = fileChooser.getSelectedFile().getAbsolutePath();
                    vcs = new Vcs(root); // Reinicializar Vcs con la nueva ruta root
                    JOptionPane.showMessageDialog(Ventana.this, "Directorio seleccionado: " + root);
                }
            }
        });
        configButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                user = JOptionPane.showInputDialog("Enter username");
                vcs.config(user);
            }
        });
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setEditable(true);
                openFileAndDisplay();
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vcs.add(filenameJLabel.getText());
                JOptionPane.showMessageDialog(null, filenameJLabel.getText() + " ha sido agregado");

            }
        });
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setEditable(true);
                String testFile = JOptionPane.showInputDialog("Ingresa el nombre del archivo");
                filenameJLabel.setText(testFile);
                Path testPath = Path.of(testFile);
                try {
                    Files.writeString(testPath, "");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        commitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Path testPath = Path.of(filenameJLabel.getText());
                String content = textArea.getText();
                try {
                    Files.writeString(testPath, content);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                String message = "Commit " + vcs.getCommits().size();
                vcs.commit(message);
                JOptionPane.showMessageDialog(null,"Commit realizado");
            }
        });
        logButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rightTextArea.setText(vcs.getLog());

            }
        });
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rightTextArea.setText("config: Get and set a username.\nadd: Add a file to the index.\nlog: Show commit logs.\ncommit: Save changes.\ncheckout: Restore a file."
                );
            }
        });
    }

    private void openFileAndDisplay() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try{
                String textfile = fileChooser.getSelectedFile().getName().toString();
                filenameJLabel.setText(textfile);
                FileReader filereader = new FileReader(fileChooser.getSelectedFile());
                BufferedReader reader = new BufferedReader(filereader);
                StringBuilder content = new StringBuilder();

                String line;
                //Escribir contenido en JTextArea
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }

                textArea.setText(content.toString());
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error reading the file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
