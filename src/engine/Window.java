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
    private TetrisGame game;
    private final Set<Integer> pressedKeys = new HashSet<>();
    
    public Window(TetrisGame game){
        setFocusable(true);
        requestFocusInWindow();
        jframe = new JFrame("");
        jframe.setSize(800, 600);
        jframe.add(this);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setVisible(true);
        this.game = game;
        addKeyListener(new KeyAdapter() {
            
            @Override
            public void keyPressed(KeyEvent e) {
                pressedKeys.add(e.getKeyCode());
                handleKeys();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(e.getKeyCode());
                handleKeys();
           }});
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
    
    //temporary
    private void handleKeys() {
        // 
        if (pressedKeys.contains(KeyEvent.VK_W)) {
           game.rotateLeftY();
        }
        if(pressedKeys.contains(KeyEvent.VK_S)){
           game.rotateRightY(); 
        }
        if(pressedKeys.contains(KeyEvent.VK_A)){
           game.rotateLeftZ(); 
        }
        if(pressedKeys.contains(KeyEvent.VK_D)){
           game.rotateRightZ(); 
        }
        if(pressedKeys.contains(KeyEvent.VK_SPACE)){
            game.hardDrop();
        }
////        if(pressedKeys.contains(KeyEvent.VK_SHIFT)){
////            //eng.cameraVec = engine.elements.Vector.vectorAdd(eng.cameraVec,eng.upVec);
////        }
        if(pressedKeys.contains(KeyEvent.VK_LEFT)){
            game.moveLeft();
        }
        if(pressedKeys.contains(KeyEvent.VK_RIGHT)){
            game.moveRight();
        }
        if(pressedKeys.contains(KeyEvent.VK_UP)){
            game.moveForward();
        }
        if(pressedKeys.contains(KeyEvent.VK_DOWN)){
            game.moveBackward();
        }
    }
    
}
