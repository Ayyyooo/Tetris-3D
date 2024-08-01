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
    
    //basic input for debuging
    Engine eng;
    private final Set<Integer> pressedKeys = new HashSet<>();
    float xAngle = 0;
    float yAngle = 0;
    float zAngle = 0;
    
    public Window(Engine eng){
        setFocusable(true);
        requestFocusInWindow();
        jframe = new JFrame("");
        jframe.setSize(800, 600);
        jframe.add(this);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setVisible(true);
        
        //events Temporary
        this.eng = eng;
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
        //Temporary
         //System.out.println("Camera pointing direction: x: "+this.xAngle +" y: "+this.yAngle+" z: "+this.zAngle);
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
           eng.cameraVec = engine.elements.Vector.vectorAdd(eng.cameraVec,eng.lookVec);
        }
        if(pressedKeys.contains(KeyEvent.VK_S)){
            eng.cameraVec = engine.elements.Vector.vectorSub(eng.cameraVec,eng.lookVec);
        }
        if(pressedKeys.contains(KeyEvent.VK_A)){
            eng.cameraVec = engine.elements.Vector.vectorSub(eng.cameraVec,engine.elements.Vector.crossProduct(eng.upVec,eng.lookVec));
        }
        if(pressedKeys.contains(KeyEvent.VK_D)){
            eng.cameraVec = engine.elements.Vector.vectorAdd(eng.cameraVec,engine.elements.Vector.crossProduct(eng.upVec,eng.lookVec));
        }
        if(pressedKeys.contains(KeyEvent.VK_SPACE)){
            eng.cameraVec = engine.elements.Vector.vectorSub(eng.cameraVec,eng.upVec);
        }
        if(pressedKeys.contains(KeyEvent.VK_SHIFT)){
            //eng.cameraVec = engine.elements.Vector.vectorAdd(eng.cameraVec,eng.upVec);
        }
        if(pressedKeys.contains(KeyEvent.VK_LEFT)){
            eng.lookVec = Engine.matrixMulti(Engine.rotationMatrix(0,-1, 0),eng.lookVec);
            this.yAngle -=1;
        }
        if(pressedKeys.contains(KeyEvent.VK_RIGHT)){
            eng.lookVec = Engine.matrixMulti(Engine.rotationMatrix(0, 1, 0),eng.lookVec);
            this.yAngle +=1;
        }
        if(pressedKeys.contains(KeyEvent.VK_UP)){
            eng.lookVec = Engine.matrixMulti(Engine.rotationMatrix(1, 0, 0),eng.lookVec);
            this.xAngle +=1;
        }
        if(pressedKeys.contains(KeyEvent.VK_DOWN)){
            eng.lookVec = Engine.matrixMulti(Engine.rotationMatrix(-1, 0, 0),eng.lookVec);
            this.xAngle -=1;
        }
    }
    
}
