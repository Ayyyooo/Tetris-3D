/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game.windows;

import engine.Window;
import game.TetrisGame;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author josja
 */
public class Menu {

    private JButton start;
    private JButton scoreBoard;
    private JButton exit;

    public Menu(Window window, TetrisGame tg) {
        window.setLayout(null);
        ImageIcon startIcon = new ImageIcon("src\\game\\icons\\startButton.png");
        start = new JButton(startIcon);
        start.setBorder(BorderFactory.createEmptyBorder());
        start.setContentAreaFilled(false);
        start.setFocusPainted(false);
        
        ImageIcon scoreboardIcon = new ImageIcon("src\\game\\icons\\scoreboardButton.png");
        scoreBoard = new JButton(scoreboardIcon);
        scoreBoard.setBorder(BorderFactory.createEmptyBorder());
        scoreBoard.setContentAreaFilled(false);
        scoreBoard.setFocusPainted(false);

        ImageIcon exitIcon = new ImageIcon("src\\game\\icons\\exitButton.png");
        exit = new JButton(exitIcon);
        exit.setBorder(BorderFactory.createEmptyBorder());
        exit.setContentAreaFilled(false);
        exit.setFocusPainted(false);

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
        int buttonWidth = windowWidth; // Button width is 1/4 of window width
        int buttonHeight = windowHeight / 3; // Button height is 1/12 of window height

        int x = (windowWidth - buttonWidth) / 2;
        int yStart = windowHeight / 2 - buttonHeight;

        start.setBounds(x, yStart, buttonWidth, buttonHeight);
        scoreBoard.setBounds(x, yStart + buttonHeight -50 , buttonWidth, buttonHeight);
        exit.setBounds(x, yStart + (buttonHeight - 50) * 2, buttonWidth, buttonHeight);
    }
}
