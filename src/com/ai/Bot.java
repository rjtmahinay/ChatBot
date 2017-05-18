package com.ai;

import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Aspire
 */
public class Bot {

    void botChat(String string, JTextPane textPane) {

        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setAlignment(attr, StyleConstants.ALIGN_LEFT);
        StyleConstants.setForeground(attr, Color.green);
        try {
            int length = doc.getLength();
            doc.insertString(doc.getLength(), "Bot: " + string + "\n", null);
            doc.setParagraphAttributes(length + 1, 1, attr, false);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
