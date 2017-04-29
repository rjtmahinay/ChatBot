/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ai;

import java.util.ArrayList;
import javax.swing.JTextArea;

/**
 *
 * @author De Guzman and Aspire
 */
public class BackChain {

    public static String tell;
    public static String ask;
    public static ArrayList<String> agenda;
    public static ArrayList<String> facts;
    public static ArrayList<String> clauses;
    public static ArrayList<String> entailed;

    public BackChain(String a, String t) {
        agenda = new ArrayList<>();
        clauses = new ArrayList<>();
        entailed = new ArrayList<>();
        facts = new ArrayList<>();
        tell = t;
        ask = a;
        init(tell);
    }

    public static void init(String tell) {
        agenda.add(ask);
        String[] rules = tell.split(";");
        for (String rule : rules) {
            if (!rule.contains("=>")) {
                facts.add(rule);
            } else {
                clauses.add(rule);
            }
        }
    }

    public String start(JTextArea textArea) {
        String output = "";
        if (bcentails(textArea)) {
            //output = "YES: ";
            output = "YES";
//            for (int i = entailed.size() - 1; i >= 0; i--) {
//                if (i == 0) {
//                    output += entailed.get(i);
//                } else // no comma at the end
//                {
//                    output += entailed.get(i) + ", ";
//                }
//
//            }
        } else {
            output = "NO";
        }
        return output;
    }

    public boolean bcentails(JTextArea textArea) {
        ArrayList<String> p = new ArrayList<>();
        while (!agenda.isEmpty()) {
            //get conclusion
            String q = agenda.remove(agenda.size() - 1);

            entailed.add(q);

            if (!facts.contains(q)) {
                for (int i = 0; i < clauses.size(); i++) {
                    if (conclusionContains(clauses.get(i), q)) {
                        //get premises
                        ArrayList<String> temp = getPremises(clauses.get(i));
                        for (int j = 0; j < temp.size(); j++) {
                            p.add(temp.get(j));
                        }
                    }
                }

            }

            if (p.isEmpty()) {
                int random = (int) (Math.random() * 3 + 1);
                Bot bot = new Bot();
                System.out.println("learning here...");
                if (random == 1) {
                    bot.botChat("I didn't understand your question", textArea);
                } else if (random >= 2) {
                    bot.botChat("I can't comprehend", textArea);
                }
                return false;
            } else {
                for (int i = 0; i < p.size(); i++) {
                    if (!entailed.contains(p.get(i))) {
                        agenda.add(p.get(i));
                    }
                }
            }
        }
        return true;
    }

    public static ArrayList<String> getPremises(String clause) {
        // get the premise
        String premise = clause.split("=>")[0];
        ArrayList<String> temp = new ArrayList<>();
        String[] conjuncts = premise.split("&");
        for (String conjunct : conjuncts) {
            if (!agenda.contains(conjunct)) {
                temp.add(conjunct);
            }
        }
        return temp;
    }

    public static boolean conclusionContains(String clause, String c) {
        String conclusion = clause.split("=>")[1];
        
        return conclusion.equals(c);
    }

}
