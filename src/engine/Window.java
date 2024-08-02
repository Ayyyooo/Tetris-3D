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
import game.Piece;

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
    private final Piece piece;
    
    public Window(Piece piece){
        setFocusable(true);
        requestFocusInWindow();
        jframe = new JFrame("");
        jframe.setSize(800, 600);
        jframe.add(this);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setVisible(true);
        this.piece = piece;
        
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
           piece.rotateLeftY();
        }
        if(pressedKeys.contains(KeyEvent.VK_S)){
           piece.rotateRightY(); 
        }
        if(pressedKeys.contains(KeyEvent.VK_A)){
           piece.rotateLeftZ(); 
        }
        if(pressedKeys.contains(KeyEvent.VK_D)){
           piece.rotateRightZ(); 
        }
//        if(pressedKeys.contains(KeyEvent.VK_SPACE)){
//            eng.cameraVec = engine.elements.Vector.vectorSub(eng.cameraVec,eng.upVec);
//        }
//        if(pressedKeys.contains(KeyEvent.VK_SHIFT)){
//            //eng.cameraVec = engine.elements.Vector.vectorAdd(eng.cameraVec,eng.upVec);
//        }
        if(pressedKeys.contains(KeyEvent.VK_LEFT)){
            piece.moveLeft();
            System.out.println(piece.position);
        }
        if(pressedKeys.contains(KeyEvent.VK_RIGHT)){
            piece.moveRight();
            System.out.println(piece.position);
        }
        if(pressedKeys.contains(KeyEvent.VK_UP)){
            piece.moveForward();
            System.out.println(piece.position);
        }
        if(pressedKeys.contains(KeyEvent.VK_DOWN)){
            piece.moveBackward();
            System.out.println(piece.position);
        }
    }
    
}
