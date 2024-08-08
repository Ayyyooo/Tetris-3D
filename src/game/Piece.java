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

    public final ArrayList<Mesh> blocks;
    public final ArrayList<Vector> positions;
    public final Tetrominoes type;
    private Color color;
    private int rotationStateY;
    private int rotationStateZ;
    public Vector position;
    private final boolean isTransparent;
    
    public Piece(Tetrominoes type, boolean isTransparent){
        this.type = type;
        blocks = new ArrayList<>();
        positions = new ArrayList<>();
        constructPiece();
        if(isTransparent) setTransparent();
        else setColor();
        position = new Vector(0,15,0);
        this.isTransparent = isTransparent;
    }
    
    //constructs the piece based on the type, asigns a color and populates the blocks array
    private void constructPiece(){
        //creates a cube
        Mesh cube = new Mesh();
        Vector center = new Vector(0.5f,0.5f,0.5f);
        String path = "src/objfiles/cube.obj";
        ObjFileLoader.loadFile(cube, path);
        switch(this.type){
            case TEST-> {
                  Mesh mesh = new Mesh();
                  mesh.copy(cube);
                mesh.applyTrans(Engine.translationMatrix(0, 15, 0));
                this.blocks.add(mesh);
                this.positions.add(Vector.vectorAdd(center, new Vector(0,15,0)));
                if(!isTransparent) this.color = new Color(120, 120, 120);
            }
            case STRAIGHT -> { //creates a straight "l"
                //translate offset (-1.5,-0.5,-0.5)
                if(!isTransparent) this.color = new Color(1, 255, 244);
                for(int i = 0;i<3;i++){ 
                    Mesh mesh = new Mesh();
                    mesh.copy(cube);
                    mesh.applyTrans(Engine.translationMatrix(i,15,0)); //translates each cube to its postition
                    this.blocks.add(mesh);
                    this.positions.add(Vector.vectorAdd(center, new Vector(i,15,0)));
                }
            }
            case J -> { //creates a J
                if(!isTransparent) this.color = new Color(124, 255, 1);
                for(int i = 0;i<3;i++){ 
                    Mesh mesh = new Mesh();
                    mesh.copy(cube);
                    mesh.applyTrans(Engine.translationMatrix(i,15,0)); //translates each cube to its postition
                    this.blocks.add(mesh);
                    this.positions.add(Vector.vectorAdd(center, new Vector(i,15,0)));
                }
                Mesh mesh = new Mesh();
                mesh.copy(cube);
                mesh.applyTrans(Engine.translationMatrix(0,16,0)); //translates each cube to its postition
                this.blocks.add(mesh);
                this.positions.add(Vector.vectorAdd(center, new Vector(0,16,0)));
//                Mesh mesh1 = new Mesh();
//                mesh1.copy(cube);
//                mesh1.applyTrans(Engine.translationMatrix(0,15,-1)); //translates each cube to its postition
//                this.blocks.add(mesh1);
//                this.positions.add(Vector.vectorAdd(center, new Vector(0,15,-1)));
            }
            case SQUARE -> { // creates a square "o"
                //translate offset (0,0,0)
                if(!isTransparent) this.color = new Color(255, 242, 5);
                for(int i =0; i<2;i++){
                    for(int j =0; j<2;j++){
                            Mesh mesh = new Mesh();
                            mesh.copy(cube);
                            mesh.applyTrans(Engine.translationMatrix(0,i+15,j)); //translates each cube to its postition
                            this.positions.add(Vector.vectorAdd(center, new Vector(0,i+15,j)));
                            this.blocks.add(mesh);
                    }
                }
            }
            case SKEW -> { // creates a skew "z"
                //translate offset (-1.5,-0.5,-0.5)
                if(!isTransparent) this.color = new Color(254, 0, 0);
                for(int i =0; i<4;i++){
                    Mesh mesh = new Mesh();
                    mesh.copy(cube);
                    this.blocks.add(mesh);
                }
                this.blocks.get(0).applyTrans(Engine.translationMatrix(0,16,0)); 
                this.positions.add(Vector.vectorAdd(center, new Vector(0,16,0)));
                this.blocks.get(1).applyTrans(Engine.translationMatrix(1,16,0));
                this.positions.add(Vector.vectorAdd(center, new Vector(1,16,0)));
                this.blocks.get(2).applyTrans(Engine.translationMatrix(1,15,0));
                this.positions.add(Vector.vectorAdd(center, new Vector(1,15,0)));
                this.blocks.get(3).applyTrans(Engine.translationMatrix(2,15,0));
                this.positions.add(Vector.vectorAdd(center, new Vector(2,15,0)));
            }
            case T -> { // creates a T
                //translate offset (-1.5,-0.5,-0.5)
                if(!isTransparent) this.color = new Color(255, 17, 123);
                for(int i =0; i<4;i++){
                    Mesh mesh = new Mesh();
                    mesh.copy(cube);
                    this.blocks.add(mesh);
                }
                this.blocks.get(0).applyTrans(Engine.translationMatrix(0,15,0));
                this.positions.add(Vector.vectorAdd(center, new Vector(0,15,0)));
                this.blocks.get(1).applyTrans(Engine.translationMatrix(1,15,0));
                this.positions.add(Vector.vectorAdd(center, new Vector(1,15,0)));
                this.blocks.get(2).applyTrans(Engine.translationMatrix(2,15,0));
                this.positions.add(Vector.vectorAdd(center, new Vector(2,15,0)));
                this.blocks.get(3).applyTrans(Engine.translationMatrix(1,16,0));
                this.positions.add(Vector.vectorAdd(center, new Vector(1,16,0)));
            }

        }
    }
    
    private void setColor(){
        for(Mesh block: this.blocks){
            block.setSolidColor(this.color);
        }
    }
    
    private void setTransparent(){
        for(Mesh block: this.blocks){
            block.setIsTransparent(true);
            block.setDrawBorder(true);
        }
    }
    
    public Color getColor(){
        return this.color;
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
        //translate offset (-1.5,-0.5,-0.5) to ceneter the rotating point
        Vector originOffset = Vector.vectorSub(new Vector(-1.5f,-0.5f,-0.5f),this.position);
        for(Mesh block: this.blocks){
            block.applyTrans(Engine.translationMatrix(originOffset));
        }
        updatePosition(originOffset,this.positions);
    }
    
    private void moveToOrigin(Vector offset){
        //translate offset (-1.5,-0.5,-0.5) to ceneter the rotating point
        Vector originOffset = Vector.vectorSub(offset,this.position);
        for(Mesh block: this.blocks){
            block.applyTrans(Engine.translationMatrix(originOffset));
        }
        updatePosition(originOffset,this.positions);
    }
    //moves the piece back to its possition this is achived by adding the postition and the offset
    
    private void backToPosition(){
        Vector offset = Vector.vectorAdd(this.position,new Vector(1.5f,0.5f,0.5f));
        for(Mesh block: this.blocks){
            block.applyTrans(Engine.translationMatrix(offset));
        }
        updatePosition(offset, this.positions);
    }
    
    private void backToPosition(Vector offset){
        Vector backOffset = Vector.vectorAdd(offset,this.position);
        for(Mesh block: this.blocks){
            block.applyTrans(Engine.translationMatrix(backOffset));
        }
        updatePosition(backOffset, this.positions);
    }
    
    //updates the positions 
    private void updatePosition(Vector offset, ArrayList<Vector> pos){
        for (int i = 0; i < pos.size(); i++) {
            pos.set(i, Vector.vectorAdd(pos.get(i), offset));
        }
    }
    
    private void rotate(float xAngle, float yAngle, float zAngle){
        float [][] rotMatrix = Engine.rotationMatrix(xAngle, yAngle, zAngle);
        moveToOrigin();
        for(int i =0; i<this.blocks.size();i++){
            this.blocks.get(i).applyTrans(rotMatrix);
            Engine.matrixMultiInPlace(rotMatrix, this.positions.get(i));
        }
        backToPosition();
    }
    
    public void rotate(float xAngle, float yAngle, float zAngle, Vector offset){
        float [][] rotMatrix = Engine.rotationMatrix(xAngle, yAngle, zAngle);
        moveToOrigin(offset);
        for(int i =0; i<this.blocks.size();i++){
            this.blocks.get(i).applyTrans(rotMatrix);
            Engine.matrixMultiInPlace(rotMatrix, this.positions.get(i));
        }
        offset = Vector.vectorSub(new Vector(), offset);
        backToPosition(offset);
    }
    
    
    public void rotateRightY(){
        rotate(0,90,0);
        this.rotationStateY = (this.rotationStateY==270)? 0 :this.rotationStateY+90;
        
    }
    
    public void rotateRightZ(){
        rotate(0,0,90);
        this.rotationStateZ = (this.rotationStateZ==270)? 0 :this.rotationStateZ+90;
    }
    
    public void rotateLeftY(){
        rotate(0,-90,0);
        this.rotationStateY = (this.rotationStateY==0)? 270 :this.rotationStateY-90;
        
    }
    
    public void rotateLeftZ(){
        rotate(0,0,-90);
        this.rotationStateZ = (this.rotationStateZ==0)? 270 :this.rotationStateZ-90;
    }
    
    //applies movement to blocks
    private void move(float x, float y, float z){
        this.position.setZ(this.position.getZ()+z);
        this.position.setY(this.position.getY()+y);
        this.position.setX(this.position.getX()+x);
        float [][] transMatrix = Engine.translationMatrix(x,y,z);
         for(int i =0; i<this.blocks.size();i++){
            this.blocks.get(i).applyProjection(transMatrix);
            this.positions.get(i).setX(x+this.positions.get(i).getX());
            this.positions.get(i).setY(y+this.positions.get(i).getY());
            this.positions.get(i).setZ(z+this.positions.get(i).getZ());  
        }
    }
    
    public void move(Vector vect){
        this.position.setZ(this.position.getZ()+vect.getZ());
        this.position.setY(this.position.getY()+vect.getY());
        this.position.setX(this.position.getX()+vect.getX());
        float [][] transMatrix = Engine.translationMatrix(vect.getX(),vect.getY(),vect.getZ());
         for(int i =0; i<this.blocks.size();i++){
            this.blocks.get(i).applyProjection(transMatrix);
            this.positions.get(i).setX(vect.getX()+this.positions.get(i).getX());
            this.positions.get(i).setY(vect.getY()+this.positions.get(i).getY());
            this.positions.get(i).setZ(vect.getZ()+this.positions.get(i).getZ());  
        }
    }
    
    //gets a copy of position but rotated in one axis
    public ArrayList<Vector> getRotatedPosition(float AngleX, float AngleY, float AngleZ){
        ArrayList<Vector> copyPosition = new ArrayList<>(); 
        for(Vector v: this.positions){
            copyPosition.add(new Vector(v.getX(),v.getY(), v.getZ()));
        }
        //moves to origin
        updatePosition(Vector.vectorSub(new Vector(-1.5f,-0.5f,-0.5f),this.position), copyPosition);
        
        //rotation matrix
        float [][] rotMatrix = Engine.rotationMatrix(AngleX, AngleY, AngleZ);
        
        //roataes each vector
        for(Vector v: copyPosition){
            Engine.matrixMultiInPlace(rotMatrix, v);
        }
        
        //moves back to position
        updatePosition(Vector.vectorAdd(this.position,new Vector(1.5f,0.5f,0.5f)), copyPosition);
        return copyPosition;
    }
    
    public ArrayList<Vector> getMovedPosition(Vector vector){
        ArrayList<Vector> copyPosition = new ArrayList<>(); 
        for(Vector v: this.positions){
            copyPosition.add(new Vector(v.getX(),v.getY(), v.getZ()));
        }
        
        for(Vector v: copyPosition){
            v.setX(vector.getX()+v.getX());
            v.setY(vector.getY()+v.getY());
            v.setZ(vector.getZ()+v.getZ());  
        }
        
        return copyPosition;
    }
}
