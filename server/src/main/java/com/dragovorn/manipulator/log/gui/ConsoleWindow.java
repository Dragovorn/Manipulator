package com.dragovorn.manipulator.log.gui;

import com.dragovorn.manipulator.Manipulator;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ConsoleWindow {

    private JPanel panel;

    private JFrame jFrame;

    private JTextArea console;

    private JTextField command;

    private JButton button;

    private static ConsoleWindow instance;

    public ConsoleWindow() {
        instance = this;

        Dimension size = new Dimension(800, 300);

        this.panel = new JPanel();

        this.panel.setLayout(new FlowLayout());
        this.panel.setPreferredSize(size);
        this.panel.setMinimumSize(size);
        this.panel.setMaximumSize(size);
        this.panel.setSize(size);

        this.console = new JTextArea(16, 65);
        this.console.setEditable(false);
        this.console.setLineWrap(true);

        this.command = new JTextField(58);
        this.command.addActionListener(new SendListener());

        DefaultStyledDocument document = new DefaultStyledDocument();
        document.setDocumentFilter(new DocumentSizeFilter(102));

        this.command.setDocument(document);

        this.button = new JButton("Send");
        this.button.setSize(5, 5);
        this.button.addActionListener(new SendListener());

        this.panel.add(new JScrollPane(this.console));
        this.panel.add(this.command);
        this.panel.add(this.button);

        this.jFrame = new JFrame("Manipulator v" + Manipulator.getInstance().getVersion());

        this.jFrame.setResizable(false);
        this.jFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.jFrame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent event) {
                Manipulator.getInstance().shutdown();
            }
        });
        this.jFrame.add(panel);
        this.jFrame.pack();
        this.jFrame.setVisible(true);
    }

    public JTextArea getConsole() {
        return this.console;
    }

    public JTextField getCommand() {
        return this.command;
    }

    public JPanel getPanel() {
        return this.panel;
    }

    public JFrame getFrame() {
        return this.jFrame;
    }

    public static ConsoleWindow getInstance() {
        return instance;
    }
}