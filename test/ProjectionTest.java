/*
    * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *%%%
 * @author josja
 */
import engine.elements.*;
import engine.*;
import java.util.ArrayList;
import game.*;
public class ProjectionTest {
    public static void main(String[] args) {
        Engine p = new Engine(800,600,30,0.1f,5000);
       
        
        //game test 
       ArrayList<ArrayList<Mesh>> scene = new ArrayList();
       ArrayList<ArrayList<Mesh>> screen = new ArrayList();
       ScreenGame screenGame = new ScreenGame(screen);
       TetrisGame tgame = new TetrisGame(scene, screenGame);
       
       Controls controls = new Controls(tgame);
       Window window = new Window(controls);
        
       p.cameraVec = Engine.matrixMulti(Engine.translationMatrix(0, 27, -27), p.cameraVec);
       p.lookVec = Engine.matrixMulti(Engine.rotationMatrix(35f, 0, 0), p.lookVec);
        
        
        UpdateWindow update = new UpdateWindow(p,window,scene,screen,screenGame);
        Thread updateThread = new Thread(update);
        updateThread.start();

    }
    
}
