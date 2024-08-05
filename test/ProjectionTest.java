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
        TetrisGame tgame = new TetrisGame(scene);
        
        //Stress test
        
        
//        for(int i = 0; i<10; i++){
//            for(int j = 0;j<10; j++){
//                for(int k = 0; k< 20;k++){
//                    Mesh border = new Mesh();
//                    Mesh cube = new Mesh();
//                    border.copy(cube1);
//                    cube.copy(cube2);
//                    //border.applyTrans(Engine.translationMatrix(i,k,j));
//                    cube.applyTrans(Engine.translationMatrix(i,k,j));
//                    world.add(border);
//                    world.add(cube);
//                }
//            }
//        }
        

       
       Window window = new Window(tgame);
        
       p.cameraVec = Engine.matrixMulti(Engine.translationMatrix(0, 36, -38), p.cameraVec);
       p.lookVec = Engine.matrixMulti(Engine.rotationMatrix(34.5f, 0, 0), p.lookVec);
        
        
        UpdateWindow update = new UpdateWindow(p,window,scene,screen);
        Thread updateThread = new Thread(update);
        updateThread.start();

    }
    
}
