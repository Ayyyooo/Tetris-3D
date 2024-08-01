/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package engine;

import java.awt.image.BufferedImage;
import engine.elements.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
        
import java.util.Iterator;
import java.util.ArrayList;
/**
 *
 * @author josja
 */
public class Renderer {
    private final BufferedImage frame;
    private final int w;
    private final int h;
    private final float resizeFactorX;
    private final float resizeFactorY; //set the y positive up
    private final Graphics2D g;
    private final AffineTransform at;
    private final float[][] depthBuffer;
    
    public Renderer(int width, int height){
        this.frame = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.w = width;
        this.h = height;
        this.resizeFactorX = 0.5f*(float)width;
        this.resizeFactorY = 0.5f*(float)height;
        this.at = new AffineTransform();
        // Translate to center the origin
        at.translate(width / 2, height / 2);
        // Flip the y-axis
        at.scale(1, -1);
        this.g = this.frame.createGraphics();
        this.g.setTransform(at);
        this.depthBuffer = new float[width+1][height+1];
        createDepthBuffer();
    }
    private void createDepthBuffer(){
         for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                depthBuffer[i][j] = Float.POSITIVE_INFINITY;
            }
        }
    }
    public void renderFrame( ArrayList<Mesh> scene){
        for(Mesh model: scene){
            if(!model.getIsTransparent())rasterize(model);
            if(model.getDrawBorder()){ 
                drawBorder(model);
                
            }
        }
    }
    
    public void rasterize(Mesh mesh){
        Iterator<Triangle> it = mesh.m.iterator();
        
        while(it.hasNext()){
            Triangle tri = it.next();
            renderTriangle(tri, tri.getColor());
        }
    }



    private void renderTriangle(Triangle tri, Color color){
        int Ax= (int)(tri.points[0].getX()*this.resizeFactorX);
        int Bx= (int)(tri.points[1].getX()*this.resizeFactorX);
        int Cx= (int)(tri.points[2].getX()*this.resizeFactorX);
        
        int Ay= (int)(tri.points[0].getY()*this.resizeFactorY);
        int By= (int)(tri.points[1].getY()*this.resizeFactorY);
        int Cy= (int)(tri.points[2].getY()*this.resizeFactorY);
    
        int minX = Math.max(-w/2,Math.min(Ax, Math.min(Bx, Cx)));
        int maxX = Math.min(w/2, Math.max(Ax, Math.max(Bx, Cx)));
        int minY = Math.max(-h/2, Math.min(Ay, Math.min(By, Cy)));
        int maxY = Math.min(h/2, Math.max(Ay, Math.max(By, Cy)));
        
        for (int y = minY; y <= maxY; y++) {
            for(int x = minX; x<=maxX; x++){
                float [] barycentric = barycentricCoords(Ax,Ay,Bx,By,Cx,Cy,x,y);
                if(barycentric[0]>=0 && barycentric[1]>=0 && barycentric[2]>=0){
                    float z = tri.points[0].getZ()*barycentric[0]+tri.points[1].getZ()*barycentric[1]+tri.points[2].getZ()*barycentric[2];
                    if (z < depthBuffer[(x + w / 2)][(y + h / 2)] ) {
                        g.setColor(color);
                        g.drawLine(x, y, x, y);
                        depthBuffer[x + w / 2][y + h / 2] = z;
                    }
                }
            }
        }
    }
    
    private float[] barycentricCoords( int Ax,int Ay,int Bx,int By, int Cx, int Cy,int x, int y){
        float [] barycentric = new float[3];
        float ABC = edgeFunction(Ax,Ay,Bx,By,Cx,Cy);
        barycentric[0]= edgeFunction(Ax,Ay,Bx,By,x,y)/ ABC;
        barycentric[1] = edgeFunction(Bx,By,Cx,Cy,x,y)/ ABC;
        barycentric[2] = edgeFunction(Cx,Cy,Ax,Ay,x,y)/ ABC;
        return barycentric;
    }
    private float edgeFunction(int Ax,int Ay,int Bx,int By, int Cx, int Cy){
        return ((Bx-Ax)*(Cy-Ay)-(By-Ay)*(Cx-Ax));
    }
    
    private void drawBorder(Mesh mesh){
        Iterator<Triangle> it = mesh.m.iterator();
        
        while(it.hasNext()){
            Triangle tri = it.next();
            drawLine(tri.points[0],tri.points[2]);
            drawLine(tri.points[2],tri.points[1]);
            if(it.hasNext()){
            Triangle tri2 = it.next();
            drawLine(tri2.points[0],tri2.points[1]);
            drawLine(tri2.points[1],tri2.points[2]);
            }
        }
    }
    
    private void drawLine(Vertex A, Vertex B){
        int Ax= (int)(A.getX()*this.resizeFactorX);
        int Bx= (int)(B.getX()*this.resizeFactorX);
        
        int Ay= (int)(A.getY()*this.resizeFactorY);
        int By= (int)(B.getY()*this.resizeFactorY);
    
        int dx = Math.abs(Bx - Ax);
        int dy = Math.abs(By - Ay);
        int sx = Ax < Bx ? 1 : -1;
        int sy = Ay < By ? 1 : -1;
        int err = dx - dy;

        while (true) {
            //necessary to check bounds, because no clipping
            if(Ax + w / 2 >0 && Ax + w / 2 < w  && (Ay + h / 2) >0 && (Ay + h / 2)< h ){
                //liinear interpolation
                float z = B.getZ()+(Ay-By)*((A.getZ()-B.getZ())/((A.getY()*this.resizeFactorY)-By));
                if((z < depthBuffer[(Ax + w / 2)][(Ay + h / 2)] )) {
                    g.setColor(Color.WHITE);
                    g.drawLine(Ax, Ay, Ax, Ay); // Draw pixel
                    depthBuffer[Ax + w / 2][Ay + h / 2] = z;
                }
            }
            if (Ax == Bx && Ay == By) break;
            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                Ax += sx;
            }
            if (e2 < dx) {
                err += dx;
                Ay += sy;
            }
        }
        
    }
    
    public BufferedImage getFrame(){
        return this.frame;
    }
 

}

