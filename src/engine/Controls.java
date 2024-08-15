package engine;
import engine.elements.Vector;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import game.TetrisGame;
import game.windows.Menu;
import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

public class Controls implements KeyListener, ComponentListener{
    TetrisGame game;
    Vector cameraVec;
    Vector lookVec;
    Menu menu;
    
    public Controls(TetrisGame tg, int width,int height,Vector cameraVec, Vector lookVec){
        game = tg;
        this.cameraVec = cameraVec;
        this.lookVec = lookVec;
        
    }
    
    public void setMenu(Menu menu){
        this.menu = menu;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_W -> //(W) clockwise rotatation in the z axis
                game.rotateRightZ();
            case KeyEvent.VK_A -> //(A) clockwise rotatation in the y axis
                game.rotateRightY();
            case KeyEvent.VK_S -> //(S) counterclockwise rotation in the z axis
                game.rotateLeftZ();
            case KeyEvent.VK_D -> //(D) counterclockwise rotatation in the y axis
                game.rotateLeftY();
            case KeyEvent.VK_UP -> //(ARROW UP) moves forward z+
                game.moveForward();
            case KeyEvent.VK_LEFT -> //(ARROW LEFT) moves left x-
                game.moveLeft();
            case KeyEvent.VK_DOWN -> //(ARROW DOWN) moves backward z-
                game.moveBackward();
            case KeyEvent.VK_RIGHT -> //(ARROW RIGHT) moves right x+
                game.moveRight();
            case KeyEvent.VK_SPACE -> //(SPACE) hard drop
                game.hardDrop();
            case KeyEvent.VK_V -> //(V) soft drop
                game.softDrop();
            case KeyEvent.VK_SHIFT -> //(Shift) hold piece
                game.holdPiece();
            case KeyEvent.VK_Q -> //(Q) clockwise rotation view 90 degrees 
                game.rotateBoard(-90,false);
            case KeyEvent.VK_E -> //(E) counterclockwise rotation view 90 degrees 
                game.rotateBoard(90,false);
            case KeyEvent.VK_R ->{ //(R) top view
                game.rotateBoard(0,true);
                if(game.boardAngleX== -90){
                     cameraVec.setX(0);
                     cameraVec.setY(0);
                     cameraVec.setZ(-30);
                     lookVec.setX(0);
                     lookVec.setY(0);
                     lookVec.setZ(1);
                     Engine.matrixMultiInPlace(Engine.translationMatrix(2, 2, 0), cameraVec);
                     Engine.matrixMultiInPlace(Engine.rotationMatrix(6, -6, 0), lookVec);
                }else{
                    cameraVec.setX(0);
                     cameraVec.setY(0);
                     cameraVec.setZ(0);
                     lookVec.setX(0);
                     lookVec.setY(0);
                     lookVec.setZ(1);
                     Engine.matrixMultiInPlace(Engine.translationMatrix(-10, 28, -25), cameraVec);
                     Engine.matrixMultiInPlace(Engine.rotationMatrix(37f, 22, 0), lookVec);
                }
            }
           case KeyEvent.VK_ESCAPE ->{ //game pause
               menu.displayMenu();
           }
        }
                
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void componentResized(ComponentEvent e) {
        menu.displayMenu();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        menu.displayMenu();
    }

    @Override
    public void componentShown(ComponentEvent e) {
        
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        menu.displayMenu();
    }
}
