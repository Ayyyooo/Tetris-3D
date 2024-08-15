/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package engine.elements;

import java.awt.Color;
import java.util.Iterator;

/**
 *
 * @author josja
 */
public class Light {
    public Vector position;
    public float intensity;

    public Light(Vector position, float intensity) {
        this.position = Vector.vectorUnit(position);
        this.intensity = intensity;
    }
    
    public static void applyLLighting(Mesh mesh, Light light){
        Iterator <Triangle> it = mesh.m.iterator();
        while(it.hasNext()){
            Triangle tri = it.next();
            tri.setColor(calculateLighting(tri.getNormal(),light, tri.getColor()));
        }
    }
    
    public static Color calculateLighting(Vector normal, Light light, Color color) {
        
        float diff = Vector.vectorDotP(Vector.vectorUnit(normal), light.position);
        
        Color diffuse = multiplyColor(color, -diff, light.intensity);
        
        return diffuse;
    }
    private static Color multiplyColor(Color color, float factor, float intensity) {
        factor = Math.max(factor,0);
        factor = ((1-intensity)*factor)+intensity;
        
        return new Color(
            (int)Math.min(color.getRed() * Math.abs(factor),255),
            (int)Math.min(color.getGreen() * Math.abs(factor),255),
            (int)Math.min(color.getBlue() * Math.abs(factor),255)
        );
    }
}
