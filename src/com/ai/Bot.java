package com.ai;

import java.awt.Color;
import javax.swing.JTextArea;

/**
 *
 * @author Aspire
 */
public class Bot {

    void botChat(String string, JTextArea textArea) {
        textArea.setForeground(Color.blue);
        textArea.append("Bot: " + string + ".\n");
    }
}
