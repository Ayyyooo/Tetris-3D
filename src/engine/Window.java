/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package engine;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;
import game.Menu;

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
        jframe.setVisible(true);
        this.controls = controls;
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
    
    public void activateControls(boolean activate){
        if (activate) {
            addKeyListener(controls);
            addMouseMotionListener(controls);
            addMouseListener(controls);
        } else {
            removeKeyListener(controls);
            removeMouseMotionListener(controls);
            removeMouseListener(controls);
        }
    }
    
    
}
