package com.forgery.GUITools;

import com.forgery.service.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

@Component
public class MainPanel extends JPanel implements ActionListener {

        BufferedImage forgeryPic = ImageIO.read(new URL("https://i.imgur.com/vWbjWqE.jpg"));
        JLabel forgeryImage = new JLabel(new ImageIcon(forgeryPic));
        JLabel clickLabel = new JLabel("Soul Forge Studio @2023");
        JLabel sourceDirectoryLabel = new JLabel("No Loyalty Dir Selected");
        JLabel resultsPathLabel = new JLabel("No Results Dir Selected");
        JLabel generation = new JLabel("");
        String chooserTitle;
        JButton fileDirectoryButton =  new JButton("Select Loyalty Directory");
        JButton csvFileDirectoryButton = new JButton("Select Results Directory");
        JButton forgeButton = new JButton("FORGE!");

        String loyaltyPath;
        String resultsPath;
        @Autowired
        private CSVService csvService;

        public MainPanel() throws IOException {
                fileDirectoryButton.addActionListener(this);
                csvFileDirectoryButton.addActionListener(this);
                forgeButton.addActionListener(this);
                setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                setMinimumSize(new Dimension(500, 500));
                setLayout(new GridLayout(0, 1));
                add(forgeryImage);
                add(fileDirectoryButton);
                add(sourceDirectoryLabel);
                add(csvFileDirectoryButton);
                add(resultsPathLabel);
                add(forgeButton);
                add(clickLabel);
                add(generation);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
                if(e.getSource() == fileDirectoryButton){
                        JFileChooser chooser = new JFileChooser();
                        chooser.setCurrentDirectory(new File("."));
                        chooser.setDialogTitle(chooserTitle);
                        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        chooser.setAcceptAllFileFilterUsed(false);
                        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                                loyaltyPath = chooser.getSelectedFile().getPath();
                                sourceDirectoryLabel.setText("Selected Loyalty Directory: " + loyaltyPath);
                                //String loyaltyFinalized = csvService.generateLoyaltyResults(chooser.getSelectedFile().getPath());
                        }
                        else {
                                System.out.println("No Selection ");
                        }
                }

                if(e.getSource() == csvFileDirectoryButton){
                        JFileChooser chooser = new JFileChooser();
                        chooser.setCurrentDirectory(new File("."));
                        chooser.setDialogTitle(chooserTitle);
                        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        chooser.setAcceptAllFileFilterUsed(false);
                        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                                resultsPath = chooser.getSelectedFile().getPath();
                                resultsPathLabel.setText("Selected Results Directory: " + resultsPath);
                        } else {
                                System.out.println("No Selection ");
                        }
                }
                if(e.getSource() == forgeButton){
                        generation.setText("Forging Results, Please Wait...");
                        new Thread() {
                                @Override
                                public void run() {
                                        try {
                                                this.sleep(3000);
                                        } catch (InterruptedException e) {
                                                e.printStackTrace();
                                        }


                                        String loyaltyFinalized = csvService.generateLoyaltyResults(loyaltyPath, resultsPath);
                                        if(loyaltyFinalized.equals("We got the data!! WOO.")){
                                                generation.setText("Results generated! Check destination folder.");}

                                }
                        }.start();
                }
        }
}
