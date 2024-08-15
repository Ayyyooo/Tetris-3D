/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package engine.elements;

import engine.Engine;
import java.awt.Color;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author josja
 */
public class Mesh {

    public LinkedList<Triangle> m;
    private Boolean isTransparent = false;
    private Boolean drawBorder = false;
    public Mesh(){
        this.m = new LinkedList<>();
    }
    
    public Mesh(float [][][] coords){
       this.m = new LinkedList<>();
       for(int i = 0; i<  coords.length; i++){
            if (coords[i].length != 3) {
              throw new IllegalArgumentException("Sub-array " + i + " in coords must have length 3 (representing a triangle)");
            }
            
            this.m.add( 
            new Triangle(new Vertex[]{
            new Vertex(coords[i][0][0],coords[i][0][1],coords[i][0][2],coords[i][0][3],coords[i][0][4]),
            new Vertex(coords[i][1][0],coords[i][1][1],coords[i][1][2],coords[i][1][3],coords[i][1][4]),
            new Vertex(coords[i][2][0],coords[i][2][1],coords[i][2][2],coords[i][2][3],coords[i][2][4])}));
           
       }
    }
    
    public void applyTrans(float [][] matrix){
        HashSet<Vector> transformedVertex = new HashSet<>();
        Iterator<Triangle> iterator = this.m.iterator();
        while(iterator.hasNext()){
            for (Vector point : iterator.next().points) {
                if(!transformedVertex.contains(point)){
                    Engine.matrixMultiInPlace(matrix, point);
                    transformedVertex.add(point);
                }
            }
        }
    }
    
    public void applyProjection(float [][] matrix){
        Iterator<Triangle> iterator = this.m.iterator();
        while(iterator.hasNext()){
            
            for (Vertex point : iterator.next().points) {
                Engine.matrixMultiInPlace(matrix, point);
                
            }
        }
    }
    
    public void copy(Mesh m2){
        Iterator<Triangle> iterator = m2.m.iterator();
        this.setDrawBorder(m2.getDrawBorder());
        this.setIsTransparent(m2.getIsTransparent());
        while(iterator.hasNext()){
            this.m.add(new Triangle( new Vertex[]{
            new Vertex(),new Vertex(),new Vertex()
            }));
            Triangle itNext = iterator.next();
            Triangle tri = this.m.getLast();
            tri.setColor(itNext.getColor());
            for(int j =0;j<itNext.points.length;j++){
                tri.points[j].setX(itNext.points[j].getX());
                tri.points[j].setY(itNext.points[j].getY());
                tri.points[j].setZ(itNext.points[j].getZ());
//                tri.points[j].setU(itNext.points[j].getU());
//                tri.points[j].setV(itNext.points[j].getV());
            }
        }
    }
    
    public void setSolidColor(Color color){
        Iterator<Triangle> iterator = this.m.iterator();
        while(iterator.hasNext()){
            iterator.next().setColor(color);
        }
    }
    
        /**
     * @return the isTransparent
     */
    public Boolean getIsTransparent() {
        return isTransparent;
    }

    /**
     * @param isTransparent the isTransparent to set
     */
    public void setIsTransparent(Boolean isTransparent) {
        this.isTransparent = isTransparent;
    }

    /**
     * @return the drawBorder
     */
    public Boolean getDrawBorder() {
        return drawBorder;
    }

    /**
     * @param drawBorder the drawBorder to set
     */
    public void setDrawBorder(Boolean drawBorder) {
        this.drawBorder = drawBorder;
    }
}
