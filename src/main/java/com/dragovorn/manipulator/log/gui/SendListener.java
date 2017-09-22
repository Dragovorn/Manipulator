package com.dragovorn.manipulator.log.gui;

import com.dragovorn.manipulator.Manipulator;
import com.dragovorn.manipulator.command.Command;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SendListener implements ActionListener {

    private boolean executed = false;

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = ConsoleWindow.getInstance().getCommand().getText();

        Manipulator.getInstance().getConsoleCommands().forEach((key, value) -> execute(cmd, key, value));

        if (!this.executed) {
            Manipulator.getInstance().getLogger().info("Unknown console command: " + cmd);
        }

        ConsoleWindow.getInstance().getCommand().setText("");

        this.executed = false;
    }

    private void execute(String cmd, String key, Command value) {
        if (cmd.equalsIgnoreCase(key)) {
            value.handle();
            this.executed = true;
        }
    }
}
