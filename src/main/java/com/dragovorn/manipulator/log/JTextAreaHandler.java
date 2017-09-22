package com.dragovorn.manipulator.log;

import javax.swing.*;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

public class JTextAreaHandler extends StreamHandler {

    JTextArea textArea = null;

    JTextAreaHandler(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void publish(LogRecord record) {
        super.publish(record);
        flush();

        if (textArea != null) {
            textArea.append(getFormatter().format(record));
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }
    }
}