/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import engine.Controls;
import engine.Engine;
import engine.UpdateWindow;
import engine.Window;
import engine.elements.Mesh;
import java.util.ArrayList;

/**
 *
 * @author josja
 */
public class GameStart {
    public static void main(String[] args) {
       Engine p = new Engine(800,600,30,0.1f,5000);
       //sets camera perspective
       p.cameraVec = Engine.matrixMulti(Engine.translationMatrix(-10, 28, -25), p.cameraVec);
       p.lookVec = Engine.matrixMulti(Engine.rotationMatrix(37f, 22, 0), p.lookVec);
       
       ArrayList<ArrayList<Mesh>> scene = new ArrayList();
       ArrayList<ArrayList<Mesh>> screen = new ArrayList();
       
       //screen elements
       ScreenGame screenGame = new ScreenGame(screen);
       //game functionalities
       TetrisGame tgame = new TetrisGame(scene, screenGame);
       //controls
       Controls controls = new Controls(tgame,800,600,p.cameraVec,p.lookVec);
       //window
       Window window = new Window(controls);
       //menu
       Menu menu = new Menu(window, tgame);

       //initializes and starts the window update
       UpdateWindow update = new UpdateWindow(p,window,scene,screen,screenGame,tgame);
       Thread updateThread = new Thread(update);
       updateThread.start();
    }
}
