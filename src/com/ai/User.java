/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ai;

import java.awt.Color;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Aspire
 */
public class User extends javax.swing.JFrame {

    String lastEntered = "";

    /**
     * Creates new form User
     */
    public User() {
        initComponents();
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        textField = new javax.swing.JTextField();
        retryBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Java AI");
        setResizable(false);

        textArea.setEditable(false);
        textArea.setColumns(20);
        textArea.setRows(5);
        scrollPane.setViewportView(textArea);

        textField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textFieldActionPerformed(evt);
            }
        });

        retryBtn.setText("Chat again");
        retryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                retryBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane)
            .addGroup(panelLayout.createSequentialGroup()
                .addComponent(textField, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(retryBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textField)
                    .addComponent(retryBtn)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void textFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textFieldActionPerformed
        Bot bot = new Bot();
        String text = textField.getText();

        int random = (int) (Math.random() * 3 + 1);

        textArea.setForeground(Color.red);
        textArea.append("You: " + text + "\n");
        //put last entered in the textfield
        lastEntered = text;
        textField.setText("");

        ArrayList<String> premises = new ArrayList();
        ArrayList<String> conclu = new ArrayList();

        //load the knowledge
        String data = "";
        File myFile = new File("data.txt");
        try {
            Scanner s = new Scanner(new FileReader(myFile.getPath()));
            while (s.hasNext()) {
                data += s.nextLine();
            }
        } catch (IOException ioe) {
        }

        System.out.println(data);

        if (text.equalsIgnoreCase("goodbye") || text.contains("goodbye")
                || lastEntered.equalsIgnoreCase("good bye") || lastEntered.contains("good bye")
                || lastEntered.equalsIgnoreCase("bye") || lastEntered.contains("bye")) {
            if (random == 1) {
                bot.botChat("See you later", textArea);
            } else if (random >= 2) {
                bot.botChat("Goodbye", textArea);
            }
            textField.disable();
        } else if (text.equalsIgnoreCase("hi") || text.contains("hi")
                || text.equalsIgnoreCase("hello") || text.contains("hello")) {
            BackChain bc = new BackChain("Ellen", data);
            String prove = bc.start(textArea);
            if (prove.toLowerCase().equals("yes")) {
                bot.botChat("My name is Ellen", textArea);
            } else {
                bot.botChat("I don't know who am I", textArea);
            }
        } else {
            //read rules
            String[] rules = data.split(";");
            for (int i = 0; i < rules.length; i++) {
                String[] r = rules[i].split("=>");
                premises.add(r[0]);
                conclu.add(r[1]);
            }

            //get keyword in the sentence
            ArrayList<String> keyword = new ArrayList();
            String KeyQuestion = "";
            for (int i = 0; i < premises.size(); i++) {
                if (text.toLowerCase().contains(premises.get(i).toLowerCase())) {
                    keyword.add(conclu.get(i));
                }
            }
            //check using backward chaining
            for (int i = 0; i < keyword.size(); i++) {
                if (i == 0) {
                    BackChain bc = new BackChain(keyword.get(i), data);
                    String prove = bc.start(textArea);
                    if (prove.equalsIgnoreCase("yes")) {
                        bot.botChat(keyword.get(i), textArea);
                    } else {
                        bot.botChat("Sorry I don't have brain", textArea);
                    }
                } else {
                    for (int j = 0; j < keyword.size(); j++) {
                        if (!(j == keyword.size() - 1)) {
                            KeyQuestion += keyword.get(j);
                        } else {
                            KeyQuestion += keyword.get(j) + "&";
                        }
                    }
                    BackChain bc = new BackChain(KeyQuestion, data);
                    String prove = bc.start(textArea);
                    if (prove.equalsIgnoreCase("yes")) {
                        System.out.println(conclu.get(i));
                        bot.botChat(keyword.get(i), textArea);
                    }
                }
            }
            //if answerable by yes or no
            String[] input = text.split(" ");
            if (input[0].toLowerCase().equals("is") 
                || input[0].toLowerCase().equals("are")) {  
                bot.botChat("For future works", textArea);
            }

        }
    }//GEN-LAST:event_textFieldActionPerformed

    private void retryBtnActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_retryBtnActionPerformed
    {//GEN-HEADEREND:event_retryBtnActionPerformed
        if (lastEntered.equalsIgnoreCase("goodbye") || lastEntered.contains("goodbye")
            || lastEntered.equalsIgnoreCase("good bye") || lastEntered.contains("good bye")
            || lastEntered.equalsIgnoreCase("bye") || lastEntered.contains("bye")) {
            textField.enable();
            textArea.setText("");
        }
    }//GEN-LAST:event_retryBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new User().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel panel;
    private javax.swing.JButton retryBtn;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTextArea textArea;
    private javax.swing.JTextField textField;
    // End of variables declaration//GEN-END:variables
}
