package engine;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import game.TetrisGame;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

public class Controls implements KeyListener, MouseListener, MouseMotionListener{
    TetrisGame game;
    public Controls(TetrisGame tg){
        game = tg;
    }
        @Override
    public void mouseClicked(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        switch (e.getKeyCode()){
            case KeyEvent.VK_W: //(W) clockwise rotatation in the z axis
                game.rotateRightZ(); 
                break;
            case KeyEvent.VK_A: //(A) clockwise rotatation in the y axis
                game.rotateRightY();
                break;
            case KeyEvent.VK_S: //(S) counterclockwise rotation in the z axis
                game.rotateLeftZ(); 
                break;
            case KeyEvent.VK_D: //(D) counterclockwise rotatation in the y axis
                game.rotateLeftY();
                break;
            case KeyEvent.VK_UP: //(ARROW UP) moves forward z+
                game.moveForward();
                break;
            case KeyEvent.VK_LEFT: //(ARROW LEFT) moves left x-
                game.moveLeft();
                break;
            case KeyEvent.VK_DOWN: //(ARROW DOWN) moves backward z-
                game.moveBackward();
                break;
            case KeyEvent.VK_RIGHT: //(ARROW RIGHT) moves right x+
                game.moveRight();
                break;
            case KeyEvent.VK_SPACE: //(SPACE) hard drop
                game.hardDrop();
                break;    
            case KeyEvent.VK_V: //(V) soft drop
                game.softDrop();
                break;
            case KeyEvent.VK_SHIFT: //(V) soft drop
                game.holdPiece();
                break;
            case KeyEvent.VK_Q: //(Q) clockwise rotation view 90 degrees 
                game.rotateBoard(90);
                break;
            case KeyEvent.VK_E: //(E) counterclockwise rotation view 90 degrees 
                game.rotateBoard(-90);
                break;
            
        }
                
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseMoved(MouseEvent e) {
       //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    

}
