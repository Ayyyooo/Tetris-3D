/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package engine;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
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
            this.jframe.addComponentListener(controls);
        } else {
            removeKeyListener(controls);
            this.jframe.removeComponentListener(controls);
        }
    }
    
    
}
