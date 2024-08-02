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
import java.awt.Color;
import java.util.ArrayList;
import game.*;
public class ProjectionTest {
    public static void main(String[] args) {
        Engine p = new Engine(800,600,30,0.1f,5000);

        Mesh cube1 = new Mesh();
        String path = "src/objfiles/cube.obj";
        ObjFileLoader.loadFile(cube1, path);
        cube1.setDrawBorder(true);
        cube1.setIsTransparent(true);
        
        Mesh cube2 = new Mesh();
        ObjFileLoader.loadFile(cube2, path);
        cube2.setSolidColor(Color.red);
        
        Mesh cube3Screen = new Mesh();
        cube3Screen.copy(cube2);
        
        Mesh boxScreen = new Mesh();
        boxScreen.copy(cube2);
        boxScreen.setDrawBorder(true);
        boxScreen.setIsTransparent(true);
        boxScreen.applyTrans(Engine.scalingMatrix(1, 2, 1));
        
        Mesh board = new Mesh();
        ObjFileLoader.loadFile(board, "src/objfiles/board.obj");
        board.setDrawBorder(true);
        board.setIsTransparent(true);
        
        //center at the origin
        board.applyTrans(Engine.translationMatrix(-5, 0,-5));
        cube2.applyTrans(Engine.translationMatrix(-5, 0,-5));
        cube1.applyTrans(Engine.translationMatrix(-5,0,-5));
        cube1.applyTrans(Engine.scalingMatrix(1.01f,1.01f, 1.01f));
        cube1.applyTrans(Engine.translationMatrix(0.01f,0.01f,0.01f));
        //Stress test
        
        ArrayList<Mesh> world = new ArrayList();
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
        
        Piece j = new Piece(Tetrominoes.J);
        j.rotateLeftZ();
        for(Mesh block: j.blocks){
            world.add(block);
        }
      
       
//        world.add(cube2);
       world.add(board);
        
       Window window = new Window(j);
        
       p.cameraVec = Engine.matrixMulti(Engine.translationMatrix(0, 36, -38), p.cameraVec);
       p.lookVec = Engine.matrixMulti(Engine.rotationMatrix(34.5f, 0, 0), p.lookVec);
       
       //center at the origin
        cube3Screen.applyTrans(Engine.translationMatrix(-0.5f,0,-0.5f));
       
        cube3Screen.applyTrans(Engine.scalingMatrix(0.15f, 0.15f, 0.15f));
        boxScreen.applyTrans(Engine.rotationMatrix(10, 0, 0));
        cube3Screen.setPosition(new Vector(-3.05f, 0, 10.35f));
        boxScreen.applyTrans(Engine.rotationMatrix(10, 0, 0));
        boxScreen.applyTrans(Engine.translationMatrix(0, 0, 10));
        ArrayList<Mesh> screen = new ArrayList();
        screen.add(cube3Screen);
        //screen.add(boxScreen);
        
        
        UpdateWindow update = new UpdateWindow(p,window,world,screen);
        update.run();

    }
    
}
