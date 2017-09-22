package com.dragovorn.manipulator.command;

import com.dragovorn.manipulator.Manipulator;

import java.util.Scanner;

public class ConsoleThread extends Thread {

    private boolean executed = false;

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (Manipulator.isRunning()) {
            String cmd = scanner.nextLine();

            Manipulator.getInstance().getConsoleCommands().forEach((key, value) -> execute(cmd, key, value));

            if (!this.executed) {
                Manipulator.getInstance().getLogger().info("Unknown console command: " + cmd);
            }

            this.executed = false;
        }
    }

    private void execute(String cmd, String key, Command value) {
        if (cmd.equalsIgnoreCase(key)) {
            value.handle();
            this.executed = true;
        }
    }
}