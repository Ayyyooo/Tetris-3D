/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package engine;
import engine.elements.Light;
import engine.elements.Mesh;
import engine.elements.Vector;
import game.ScreenGame;
import game.TetrisGame;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JLabel;
/**
 *
 * @author josja
 */
public class UpdateWindow implements Runnable{
    public Engine engine;
    public Window window;
    private final ArrayList<ArrayList<Mesh>> scene; // multidimensional array of mesh each array of mesh represent a game object 
    private final ArrayList<ArrayList<Mesh>> screenElements; // multidimensional array of mesh each array of mesh represent a screen object
    private int w,h; 
    private final ScreenGame screenG;
    private final TetrisGame tetrisG;
    JLabel score;
    public UpdateWindow(Engine engine, Window window, ArrayList<ArrayList<Mesh>> scene, ArrayList<ArrayList<Mesh>> screenElements, ScreenGame screenG, TetrisGame tetrisG){
        this.engine = engine;
        this.window = window;
        this.w = window.getWidth();
        this.h = window.getHeight();
        this.scene = scene;
        this.screenElements = screenElements;
        this.screenG = screenG;
        this.tetrisG = tetrisG;
        this.setScore();
    }
    @Override
    public void run(){
        while(true){
            //updates the height and width if resized
            if(this.window.getWidth() != this.w || this.window.getHeight() != this.h ){
                engine.changeAspect(window.getWidth(),window.getHeight());
                this.w = window.getWidth();
                this.h = window.getHeight();
            }
            ArrayList<Mesh> projectedScene = projectScene();
            ArrayList<Mesh> projectedScreen = projectScreen();
            
            Renderer render = new Renderer(window.getWidth(),window.getHeight());
            //render scene
            render.renderFrame(projectedScene);
            //render screen
            render.renderFrame(projectedScreen);
            this.window.update(render.getFrame());
            updateScore();
            try {
                Thread.sleep(16);  // Control animation speed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    private ArrayList<Mesh> projectScene(){
    ArrayList<Mesh> projected = new ArrayList<>();
    Light light = new Light( Vector.vectorSub(new Vector(), engine.lookVec), 0.3f);
        for(ArrayList<Mesh> gameObj: scene){
            for(Mesh element: gameObj){
                Mesh projectedScene = new Mesh();
                projectedScene.copy(element);
                
                if(this.tetrisG.lastBoardAngleY<this.tetrisG.boardAngleY){
                    //rotation 
                    projectedScene.applyTrans(Engine.rotationMatrix(0, this.tetrisG.lastBoardAngleY,0));
                    this.tetrisG.lastBoardAngleY +=1.5f;
                }
                else if(this.tetrisG.lastBoardAngleY>this.tetrisG.boardAngleY){
                    //rotation 
                    projectedScene.applyTrans(Engine.rotationMatrix(0, this.tetrisG.lastBoardAngleY,0));
                    this.tetrisG.lastBoardAngleY -=1.5f;
                }else{
                    projectedScene.applyTrans(Engine.rotationMatrix(0, this.tetrisG.boardAngleY,0));
                }
                if(this.tetrisG.lastBoardAngleX<this.tetrisG.boardAngleX){
                    //rotation 
                    projectedScene.applyTrans(Engine.rotationMatrix(this.tetrisG.lastBoardAngleX,0,0));
                    this.tetrisG.lastBoardAngleX +=1.5f;
                }
                else if(this.tetrisG.lastBoardAngleX>this.tetrisG.boardAngleX){
                    //rotation 
                    projectedScene.applyTrans(Engine.rotationMatrix( this.tetrisG.lastBoardAngleX,0,0));
                    this.tetrisG.lastBoardAngleX -=1.5f;
                }else{
                    projectedScene.applyTrans(Engine.rotationMatrix( this.tetrisG.boardAngleX,0,0));
                }
                
                //camera matrix
                projectedScene.applyTrans(engine.matrixFrom());
                
                //if the block is transparent avoid clipping and back face culling
                if(!projectedScene.getIsTransparent()){
                    //back face culling
                    engine.cullBackFaces(projectedScene);

                    //clipps triangles in scene
                    //engine.clipTrianglesAgainstPlane(projectedScene);
                    Light.applyLLighting(projectedScene, light);
                }
                
                //projects scene
                projectedScene.applyProjection(engine.projMatrix);
                
                projected.add(projectedScene);
            }
        }
            return projected;
    }
    
    private ArrayList<Mesh> projectScreen(){
        screenG.updateScreen();
        Light light = new Light(new Vector(0,0,-1), 0.3f);
        ArrayList<Mesh> projected = new ArrayList<>();
            for(ArrayList<Mesh> screenObj: screenElements){
                if(screenObj == null) continue;
                for(Mesh element: screenObj){
                    Mesh projectedScreen = new Mesh();
                    projectedScreen.copy(element);

                    //if the block is transparent avoid clipping and back face culling
                    if(!projectedScreen.getIsTransparent()){
                        //back face culling
                        engine.cullBackFaces(projectedScreen);

                        //clipps triangles in scene
                        //engine.clipTrianglesAgainstPlane(projectedScreen);

                        
                        Light.applyLLighting(projectedScreen, light);
                    }

                    //projects screen elements
                    projectedScreen.applyProjection(engine.projMatrix);
                    projected.add(projectedScreen);

                }
        }
        return projected;
    }
    
    private void setScore(){
        score = new JLabel(String.valueOf(this.tetrisG.score));
        score.setForeground(Color.WHITE);
        score.setFont(new Font("Arial", Font.BOLD, 30));
        this.window.setLayout(null);
        this.window.add(score);
    }
    private void updateScore(){
        Vector position = new Vector(-4.2f, 2.5f, 15);
        Engine.matrixMultiInPlace(this.engine.projMatrix, position);

        // Set the text for the score label
        score.setText(String.valueOf(this.tetrisG.score));

        // Get the preferred size of the label based on its content
        Dimension labelSize = score.getPreferredSize();

        // Calculate screen position
        int screenX = (int)((position.getX() * (0.5f * (float)this.w)) + (this.w / 2));
        int screenY = (int)((1 - position.getY()) * (0.5f * (float)this.h));

        // Center the label on the calculated position
        int centeredX = screenX - (labelSize.width / 2);
        int centeredY = screenY - (labelSize.height / 2);

        // Set the bounds of the label based on its content size
        score.setBounds(centeredX, centeredY, labelSize.width, labelSize.height);
    } 
    
    
}
