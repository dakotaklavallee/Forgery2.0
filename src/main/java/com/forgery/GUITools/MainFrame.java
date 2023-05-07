package com.forgery.GUITools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
@ConditionalOnBean(MainPanel.class)
public class MainFrame extends JFrame {
    @Autowired
    private MainPanel mainPanel;

    public void setUp(){
        add(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("FORGERY 2.0");
        pack();
        setVisible(true);
    }
}
