/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package engine.elements;

/**
 *
 * @author josja
 */
public class Vector {
    private float x;
    private float y;
    private float z;
    private float w;
    public Vector(){
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.w = 1;
    }
    public Vector(float x,float y,float z){
        this.x =x;
        this.y = y;
        this.z = z;
        this.w = 1;
    }
    /**
     * @return the x
     */
    public float getX() {
        return x;
    }

    /**
     * @return the y
     */
    public float getY() {
        return y;
    }

    /**
     * @return the z
     */
    public float getZ() {
        return z;
    }
    
        /**
     * @return the z
     */
    public float getW() {
        return w;
    }

    /**
     * @param x the x to set
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * @param y the y to set
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * @param z the z to set
     */
    public void setZ(float z) {
        this.z = z;
    }
    
    /**
     * @param w the w to set
     */
    public void setW(float w) {
        this.w = w;
    }
    
    
    public static Vector vectorAdd(Vector p1,Vector p2){
        return new Vector(p1.getX()+p2.getX(),p1.getY()+p2.getY(),p1.getZ()+p2.getZ());
    }
    
    public static Vector vectorSub(Vector p1,Vector p2){
        return new Vector(p1.getX()-p2.getX(),p1.getY()-p2.getY(),p1.getZ()-p2.getZ());
    }
    
    public static Vector vectorMult(Vector p, float m){
        return new Vector(p.getX()*m,p.getY()*m,p.getZ()*m);
    }
    
    public static Vector crossProduct(Vector p1, Vector p2){
        return new Vector(
                p1.getY()*p2.getZ()-p2.getY()*p1.getZ(),
                p1.getZ()*p2.getX()-p1.getX()*p2.getZ(),
                p1.getX()*p2.getY()-p1.getY()*p2.getX()
        );
    }
    
    public static float vectorDotP(Vector p1,Vector p2){
        return p1.getX()*p2.getX()+p1.getY()*p2.getY()+p1.getZ()*p2.getZ();
    }
    
    public static float vectorLength(Vector p){
        return (float) Math.sqrt(vectorDotP(p,p));
    }
    
    public static Vector vectorUnit(Vector p){
        float length = vectorLength(p);
        return new Vector(p.getX()/length,p.getY()/length,p.getZ()/length);
    }
    @Override
    public String toString(){
        return "X: "+this.getX()+"Y: "+this.getY()+"Z: "+this.getZ();
    }
}
