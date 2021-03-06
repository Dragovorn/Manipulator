package com.dragovorn.manipulator.log.gui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;

public class DocumentSizeFilter extends DocumentFilter {

    private int maxCharacters;

    DocumentSizeFilter(int maxChars) {
        maxCharacters = maxChars;
    }

    @Override
    public void insertString(FilterBypass filterBypass, int offset, String string, AttributeSet attributeSet) throws BadLocationException {
        if ((filterBypass.getDocument().getLength() + string.length()) <= maxCharacters) {
            super.insertString(filterBypass, offset, string, attributeSet);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    @Override
    public void replace(DocumentFilter.FilterBypass filterBypass, int offset, int length, String string, AttributeSet attributeSet) throws BadLocationException {
        if ((filterBypass.getDocument().getLength() + string.length() - length) <= maxCharacters) {
            super.replace(filterBypass, offset, length, string, attributeSet);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }
}