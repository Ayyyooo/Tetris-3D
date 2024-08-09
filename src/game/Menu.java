/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import engine.Window;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 *
 * @author josja
 */
public class Menu{
    private JButton start;
    private JButton scoreBoard;
    private JButton exit;
    public Menu(Window window, TetrisGame tg){
        window.setLayout(null);
        
        start = new JButton("START");
        start.setFont(new Font ("Courier New", Font.BOLD, 25) );
        start.setBackground(Color.CYAN);
        start.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        start.setBounds(300, 200, 200, 50);
        
        scoreBoard = new JButton("SCOREBOARD");
        scoreBoard.setFont(new Font ("Courier New", Font.BOLD, 25) );
        scoreBoard.setBackground(Color.CYAN);
        scoreBoard.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        scoreBoard.setBounds(300, 265, 200, 50);
        
        exit = new JButton("EXIT");
        exit.setFont(new Font ("Courier New", Font.BOLD, 25) );
        exit.setBackground(Color.CYAN);
        exit.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        exit.setBounds(300, 329, 200, 50);
        
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tg.startTimer();
                window.activateControls(true);
                start.setVisible(false);
                scoreBoard.setVisible(false);
                exit.setVisible(false);
            }
        });
        
        scoreBoard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Board");
            }
        });
        
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        
        window.add(start);
        window.add(scoreBoard);
        window.add(exit);
        
        window.addComponentListener(new ComponentAdapter() {
             @Override
             public void componentResized(ComponentEvent e) {
                 positionButtons(window.getWidth(), window.getHeight());
             }
         });

        positionButtons(window.getWidth(), window.getHeight());
    
    }
    
    private void positionButtons(int windowWidth, int windowHeight) {
        int buttonWidth = windowWidth / 4; // Button width is 1/4 of window width
        int buttonHeight = windowHeight / 12; // Button height is 1/12 of window height

        int x = (windowWidth - buttonWidth) / 2;
        int yStart = windowHeight / 2 - buttonHeight * 2;

        start.setBounds(x, yStart, buttonWidth, buttonHeight);
        scoreBoard.setBounds(x, yStart + buttonHeight + 15, buttonWidth, buttonHeight);
        exit.setBounds(x, yStart + (buttonHeight + 15) * 2, buttonWidth, buttonHeight);
    }
}
