/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package engine.elements;

import java.awt.Color;


/**
 *
 * @author josja
 */

public class Triangle {
    public Vertex [] points;
    private Color color;
    private Vector normal;
    
    public Triangle(Vertex [] points){
        this.points = points;
    }   
    
     /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }
    
    /**
     * @return the normal
     */
    public Vector getNormal() {
        return normal;
    }

    /**
     * @param normal the normal to set
     */
    public void setNormal(Vector normal) {
        this.normal = normal;
    }
    
    @Override
    public String toString(){
        return "1: "+points[0]+"\n2: "+points[1]+"\n3: "+points[2];
    }
}
