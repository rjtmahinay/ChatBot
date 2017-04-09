package com.ai;

import javax.swing.JTextArea;

/**
 *
 * @author Aspire
 */
public class Bot {

    void botChat(String string, JTextArea textArea) {

        textArea.append("Bot: " + string + "\n");
    }
}
