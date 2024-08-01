/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package engine;
import engine.elements.Light;
import engine.elements.Mesh;
import engine.elements.Vector;
import java.util.ArrayList;
/**
 *
 * @author josja
 */
public class UpdateWindow implements Runnable{
    public Engine engine;
    public Window window;
    private final ArrayList<Mesh> scene;
    private final ArrayList<Mesh> screenElements;
    private int w,h; 
    public UpdateWindow(Engine engine, Window window, ArrayList<Mesh> scene, ArrayList<Mesh> screenElements){
        this.engine = engine;
        this.window = window;
        this.w = window.getWidth();
        this.h = window.getHeight();
        this.scene = scene;
        this.screenElements = screenElements;
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
            yAngle += 0.4f; //Temporary
           // System.out.println("Camera pointing direction: "+engine.lookVec);
            System.out.println("Camera position "+engine.cameraVec);
            try {
                Thread.sleep(10);  // Control animation speed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    float yAngle = 0; //Temporary
    private ArrayList<Mesh> projectScene(){
    ArrayList<Mesh> projected = new ArrayList<>();
            for(Mesh element: scene){
                Mesh projectedScene = new Mesh();
                projectedScene.copy(element);
                
                //rotation Temporary
                projectedScene.applyTrans(Engine.rotationMatrix(0, yAngle,0));
                
                //position
                projectedScene.applyTrans(Engine.translationMatrix(projectedScene.getPosition()));
                
                //camera matrix
                projectedScene.applyTrans(engine.matrixFrom());
                
                //if the block is transparent avoid clipping and back face culling
                if(!projectedScene.getIsTransparent()){
                    //back face culling
                    engine.cullBackFaces(projectedScene);

                    //clipps triangles in scene
                    engine.clipTrianglesAgainstPlane(projectedScene);
                    
                    Light light = new Light(new Vector(0,0,-1), 0.6f);
                    Light.applyLLighting(projectedScene, light);
                }
                
                //projects scene
                projectedScene.applyProjection(engine.projMatrix);
                
                projected.add(projectedScene);
            }
            return projected;
    }
    float xAngle =0;
    float zAngle =0;
    private ArrayList<Mesh> projectScreen(){
        ArrayList<Mesh> projected = new ArrayList<>();
            for(Mesh element: screenElements){
                Mesh projectedScreen = new Mesh();
                projectedScreen.copy(element);
                
                //rotation Temporary
                projectedScreen.applyTrans(Engine.rotationMatrix(xAngle, yAngle,zAngle));
                zAngle +=0.5f;
                xAngle +=0.5f;
                //position
                projectedScreen.applyTrans(Engine.translationMatrix(projectedScreen.getPosition()));
                
                
                //if the block is transparent avoid clipping and back face culling
                if(!projectedScreen.getIsTransparent()){
                    //back face culling
                    engine.cullBackFaces(projectedScreen);

                    //clipps triangles in scene
                    engine.clipTrianglesAgainstPlane(projectedScreen);
                    
                    Light light = new Light(engine.cameraVec, 0.3f);
                    Light.applyLLighting(projectedScreen, light);
                }
                
                //projects screen elements
                projectedScreen.applyProjection(engine.projMatrix);
                projected.add(projectedScreen);
            
            }
        return projected;
    }
    
    
}
