package com.michelegarofalo.springbootapi.gui;

import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;

public class GUI {

    public static final String FRAME_TITLE = "Spring Boot API Michele e Antonio";
    public static final short FRAME_WIDTH = 500;
    public static final short FRAME_HEIGHT = 400;

    public static void createAndShowGUI() {

        JFrame frame = new JFrame(FRAME_TITLE);

        JLabel label = new JLabel("Running...", SwingConstants.CENTER);

        JTextArea console = new JTextArea();
        console.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(console);

        System.setOut(new PrintStream(new TextAreaOutputStream(console), true));
        System.setErr(new PrintStream(new TextAreaOutputStream(console), true));

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(event -> System.exit(0));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);

        frame.setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(label, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}