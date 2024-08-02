/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;
import engine.ObjFileLoader;
import engine.Engine;
import java.util.ArrayList;
import engine.elements.Mesh;
import engine.elements.Vector;
import java.awt.Color;
/**
 *
 * @author josja
 */
public class Piece {

    public ArrayList<Mesh> blocks;
    public final Tetrominoes type;
    private Color color;
    private int rotationStateY;
    private int rotationStateZ;
    public Vector position;
    
    public Piece(Tetrominoes type){
        this.type = type;
        blocks = new ArrayList<>();
        constructPiece();
        setColor();
        position = new Vector();
        if(type != Tetrominoes.SQUARE){
            backToPosition();
        }
    }
    
    //constructs the piece based on the type, asigns a color and populates the blocks array
    private void constructPiece(){
        //creates a cube
        Mesh cube = new Mesh();
        String path = "src/objfiles/cube.obj";
        ObjFileLoader.loadFile(cube, path);
        switch(this.type){
            case STRAIGHT -> { //creates a straight "l"
                //translate offset (-1.5,-0.5,-0.5)
                this.color = new Color(255,0,0);
                for(int i = 0;i<4;i++){ 
                    Mesh mesh = new Mesh();
                    mesh.copy(cube);
                    mesh.applyTrans(Engine.translationMatrix(i-1.5f,-0.5f,-0.5f)); //translates each cube to its postition
                    this.blocks.add(mesh);
                }
            }
            case J -> { //creates a J
                //translate offset (-1.5,-0.5,-0.5)
                this.color = new Color(255,0,0);
                for(int i = 0;i<3;i++){ 
                    Mesh mesh = new Mesh();
                    mesh.copy(cube);
                    mesh.applyTrans(Engine.translationMatrix(i-1.5f,-0.5f,-0.5f)); //translates each cube to its postition
                    this.blocks.add(mesh);
                }
                Mesh mesh = new Mesh();
                mesh.copy(cube);
                mesh.applyTrans(Engine.translationMatrix(-1.5f,0.5f,-0.5f)); //translates each cube to its postition
                this.blocks.add(mesh);
                Mesh mesh1 = new Mesh();
                mesh1.copy(cube);
                mesh1.applyTrans(Engine.translationMatrix(-1.5f,-0.5f,-1.5f)); //translates each cube to its postition
                this.blocks.add(mesh1);
            }
            case SQUARE -> { // creates a square "o"
                //translate offset (0,0,0)
                this.color = new Color(255,0,0);
                for(int i =0; i<2;i++){
                    for(int j =0; j<2;j++){
                        for(int k=0; k<2;k++){
                            Mesh mesh = new Mesh();
                            mesh.copy(cube);
                            mesh.applyTrans(Engine.translationMatrix(i,j,k)); //translates each cube to its postition
                            this.blocks.add(mesh);
                        }
                    }
                }
            }
            case SKEW -> { // creates a skew "z"
                //translate offset (-1.5,-0.5,-0.5)
                this.color = new Color(255,0,0);
                for(int i =0; i<4;i++){
                    Mesh mesh = new Mesh();
                    mesh.copy(cube);
                    this.blocks.add(mesh);
                }
                this.blocks.get(0).applyTrans(Engine.translationMatrix(-1.5f,0.5f,-0.5f)); 
                this.blocks.get(1).applyTrans(Engine.translationMatrix(-0.5f,0.5f,-0.5f));
                this.blocks.get(2).applyTrans(Engine.translationMatrix(-0.5f,-0.5f,-0.5f));
                this.blocks.get(3).applyTrans(Engine.translationMatrix(0.5f,-0.5f,-0.5f));
            }
            case T -> { // creates a T
                //translate offset (-1.5,-0.5,-0.5)
                this.color = new Color(255,0,0);
                for(int i =0; i<6;i++){
                    Mesh mesh = new Mesh();
                    mesh.copy(cube);
                    this.blocks.add(mesh);
                }
                this.blocks.get(0).applyTrans(Engine.translationMatrix(-1.5f,-0.5f,-0.5f));
                this.blocks.get(1).applyTrans(Engine.translationMatrix(-0.5f,-0.5f,-0.5f));
                this.blocks.get(2).applyTrans(Engine.translationMatrix(0.5f,-0.5f,-0.5f));
                this.blocks.get(3).applyTrans(Engine.translationMatrix(-0.5f,0.5f,-0.5f));
                this.blocks.get(4).applyTrans(Engine.translationMatrix(-0.5f,-0.5f,0.5f));
                this.blocks.get(5).applyTrans(Engine.translationMatrix(-0.5f,-0.5f,-1.5f));
            }
        }
    }
    
    private void setColor(){
        for(Mesh block: this.blocks){
            block.setSolidColor(this.color);
        }
    }
    
        /**
     * @return the rotationStateY
     */
    public int getRotationStateY() {
        return rotationStateY;
    }

    /**
     * @return the rotationStateZ
     */
    public int getRotationStateZ() {
        return rotationStateZ;
    }
    
    /*
     Rotation Methods
    */
    // to rotate a piece it needs to be moved to the origin, this is achived by ading the
    //negatives of the offset and the postion
    private void moveToOrigin(){
        for(Mesh block: this.blocks){
            block.applyTrans(Engine.translationMatrix(Vector.vectorSub(new Vector(-1.5f,-0.5f,-0.5f),this.position)));
        }
    }
    //moves the piece back to its possition this is achived by adding the postition and the offset
    private void backToPosition(){
        for(Mesh block: this.blocks){
            block.applyTrans(Engine.translationMatrix(Vector.vectorAdd(this.position,new Vector(1.5f,0.5f,0.5f))));
        }
    }
    public void rotateRightY(){
        if(this.type == Tetrominoes.SQUARE)return;
        moveToOrigin();
        for(Mesh block: this.blocks){
            block.applyProjection(Engine.rotationMatrix(90, 0, 0));

        }
        this.rotationStateY = (this.rotationStateY==270)? 0 :this.rotationStateY+90;
        backToPosition();
    }
    
    public void rotateRightZ(){
        if(this.type == Tetrominoes.SQUARE)return;
        moveToOrigin();
        for(Mesh block: this.blocks){
            block.applyProjection(Engine.rotationMatrix(0, 0, 90));
        }
        this.rotationStateZ = (this.rotationStateZ==270)? 0 :this.rotationStateZ+90;
        backToPosition();
    }
    
    public void rotateLeftY(){
        if(this.type == Tetrominoes.SQUARE)return;
        moveToOrigin();
        for(Mesh block: this.blocks){
            block.applyProjection(Engine.rotationMatrix(-90, 0, 0));
        }
        this.rotationStateY = (this.rotationStateY==0)? 270 :this.rotationStateY-90;
        backToPosition();
    }
    
    public void rotateLeftZ(){
        if(this.type == Tetrominoes.SQUARE)return;
        moveToOrigin();
        for(Mesh block: this.blocks){
            block.applyProjection(Engine.rotationMatrix(0, 0, -90));
        }
        this.rotationStateZ = (this.rotationStateZ==0)? 270 :this.rotationStateZ-90;
        backToPosition();
    }
    
    
    //applies movement to blocks
    private void move(float x, float y, float z){
        for(Mesh block: this.blocks){
            block.applyTrans(Engine.translationMatrix(x,y,z));
        }
    }
    
    //Moves positive Z axis 
    public void moveForward(){
        this.position.setZ(this.position.getZ()+1);
        move(0,0,1);
    }
    //Moves negative Z axis
    public void moveBackward(){
        this.position.setZ(this.position.getZ()-1);
        move(0,0,-1);
    }
    //Moves positive X axis
    public void moveRight(){
        this.position.setX(this.position.getX()+1);
        move(1,0,0);
    }
    //Moves negative Z axis
    public void moveLeft(){
        this.position.setX(this.position.getX()-1);
        move(-1,0,0);
    }
}
