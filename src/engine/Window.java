/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package engine;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;
import game.TetrisGame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author josja
 */

/**
 *
 * @author josja
 */
public class Window extends JPanel{
    public JFrame jframe;
    private BufferedImage frame;
    private final Set<Integer> pressedKeys = new HashSet<>();
    Controls controls;
    
    public Window(Controls controls){
        setFocusable(true);
        requestFocusInWindow();
        jframe = new JFrame("");
        jframe.setSize(800, 600);
        jframe.add(this);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addButton();
        jframe.setVisible(true);
        this.controls = controls;
        addKeyListener(controls);
        addMouseMotionListener(controls);
        addMouseListener(controls);
    }
    
    private void addButton(){    
        setLayout(null);
        
        JButton start = new JButton("START");
        start.setFont(new Font ("Courier New", Font.BOLD, 25) );
        start.setBackground(Color.CYAN);
        start.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        start.setBounds(300, 200, 200, 50);
        
        JButton scoreBoard = new JButton("SCOREBOARD");
        scoreBoard.setFont(new Font ("Courier New", Font.BOLD, 25) );
        scoreBoard.setBackground(Color.CYAN);
        scoreBoard.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        scoreBoard.setBounds(300, 265, 200, 50);
        
        JButton exit = new JButton("EXIT");
        exit.setFont(new Font ("Courier New", Font.BOLD, 25) );
        exit.setBackground(Color.CYAN);
        exit.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        exit.setBounds(300, 329, 200, 50);
        
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
        
        
        add(start);
        add(scoreBoard);
        add(exit);
       

        
    
        
    }
    
    public void update(BufferedImage frame){
        this.frame = frame;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(frame != null){
            g.drawImage(frame,0,0,null);
        }
    }
    
    
}
