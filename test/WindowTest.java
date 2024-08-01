/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author josja
 */

import engine.*;
import engine.elements.*;
import java.awt.Color;
import java.util.Iterator;


public class WindowTest implements Runnable{
    Window window;
    Mesh mesh3d;
    Engine p;
    float xAngle = 1f;
    float yAngle = 0f;
    float zAngle = 0f;
    
    
    public WindowTest(Window window, Mesh mesh3d,Engine p){
        this.window = window;
        this.mesh3d = mesh3d;
        this.p = p;
       
        

        
    }
    @Override
    public void run(){
        
        while(true){
            //yAngle += 0.3f;
            p.changeAspect(window.getWidth(),window.getHeight());
            //System.out.println(p.aspect);
            
            //p.cameraVec.setY(p.cameraVec.getY()+0.01f);
            //p.cameraVec.setX(p.cameraVec.getX()+0.1f);
            //p.cameraVec.setX(p.cameraVec.getX()-0.01f);
            //p.cameraVec.setX(p.cameraVec.getX()+0.1f);
            //p.cameraVec.setY(p.cameraVec.getY()+0.1f);
           
            
            
            Mesh projected = new Mesh();

            float [][] transMatrix = new float[4][4];
            for(float [] row: transMatrix){
                for(float m: row) m=0;
            }

            transMatrix[0][0] = 1;
            transMatrix[1][1] = 1;
            transMatrix[2][2] = 1;
            transMatrix[1][3] = 1;
            transMatrix[2][3] = 5f;
            
            
            //rotation
            mesh3d.applyTrans(Engine.rotationMatrix(xAngle, yAngle, zAngle));
            
            //translation
            projected.copy(mesh3d);
            projected.applyTrans(transMatrix);
            
            //camera matrix
            projected.applyTrans(p.matrixFrom());
            
            //back face culling
            p.cullBackFaces(projected);
            
            //clipping
            p.clipTrianglesAgainstPlane(projected);
            
            
            
            //projection
            projected.applyProjection(p.projMatrix);
            

            
            Light light = new Light(new Vector(0,0,-1), new Color(100,100,100) ,0.5f);
            
            Light.applyLLighting(projected, light);
            
            Renderer render = new Renderer(window.getWidth(),window.getHeight());
            render.renderFrame(projected);
            window.update(render.getFrame());
            try {
                Thread.sleep(10);  // Control animation speed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    

}
