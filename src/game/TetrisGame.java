/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;
import engine.Engine;
import engine.ObjFileLoader;
import engine.elements.Mesh;
import engine.elements.Triangle;
import engine.elements.Vector;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.swing.Timer;


/**
 *
 * @author josja
 */
public class TetrisGame {
    private Timer timer;
    Piece movingPiece;
    Piece shadowPiece;
    private int delay;
    private int level;
    private int score;
    private Color [][][] stack; 
    private ArrayList<ArrayList<Mesh>> scene;
    
    public TetrisGame(ArrayList<ArrayList<Mesh>> scene){
        this.level = 0;
        stack = new Color[15][6][6]; //y,x,z
        this.scene = scene;
        startGame();
        this.timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                movePieceDown();
            }
        });
        this.timer.start();
    }
    
    private void startGame(){
        calculateDelay();
        for(int i= 0; i<4;i++)this.scene.add(new ArrayList<>());
        generateNewPiece();
        Mesh board = new Mesh();
        ObjFileLoader.loadFile(board, "src/objfiles/board.obj");
        board.setDrawBorder(true);
        board.setIsTransparent(true);
        board.applyTrans(Engine.translationMatrix(-3f, 0,-3f));
        ArrayList<Mesh> boardMesh = new ArrayList<>();
        boardMesh.add(board);
        this.scene.set(2,boardMesh); // index 2 board
        ArrayList<Mesh> stackMeshes = new ArrayList<>();
        this.scene.set(3,stackMeshes); // index 3 Stack
    }
    
    private void generateNewPiece(){
        movingPiece = new Piece(Tetrominoes.values()[(new Random()).nextInt(Tetrominoes.values().length)],false);
        //movingPiece = new Piece(Tetrominoes.SQUARE,false);
        shadowPiece = new Piece(this.movingPiece.type,true);
        moveShadowDown();
        this.scene.set(0,movingPiece.blocks); //index 0 moving piece
        this.scene.set(1,shadowPiece.blocks); //index 1 shadow piece
    }
    
    private void calculateDelay() {
        int baseDelay = 800; // milliseconds for level 1
        int decrement = 80;  // milliseconds decrement per level
        delay = Math.max(baseDelay - (level - 1) * decrement, 80); // Minimum delay of 80ms
    }
    
    //returns false it the piece collides with another piece or with the walls
    private boolean collide(ArrayList<Vector> position){
        for(Vector p: position){
            int x = ((int)Math.floor(p.getX()))+3;
            int y = (int)Math.floor(p.getY());
            int z = ((int)Math.floor(p.getZ()))+3;
            if(y>14) y=14;
            if(x<0 || x>5 || y<0 || z<0 || z>5 )return false;
            if(stack[y][x][z] !=null)return false;
        }
        return true;
    }
    
    private void bottomHit(){
        //updates the stack
        Color color = this.movingPiece.getColor();
        int minY = 14;
        int maxY = 0;
        for(Vector block: this.movingPiece.positions){
            int x = ((int)Math.floor(block.getX()))+3;
            int y = (int)Math.floor(block.getY());
            int z = ((int)Math.floor(block.getZ()))+3;
            minY = Math.min(y, minY);
            maxY = Math.max(y, maxY);
            this.stack[y][x][z] = color;
        }
        checkCompleteLines(minY, maxY);
        updateScene();
        generateNewPiece();
    }
    
    //checks the affected planes and clears if the plane is completed
    private void checkCompleteLines(int min, int max){
        for(int y = min;y<=max;y++){
            if(isComplete(y)){
                clearLines(y);
                Scoring(clearLines(y));                
                break;
            }
        }
    }    
    private void Scoring(int clearedLines){
        switch (clearedLines){
            case 1:
                score += 220;
            case 2:
                score += 550;
            case 3:
                score += 1000;
        }
    }    
    
    private void clearLines(int y){
        int i = y;
        while(y<15 && i<15){
            y++;
            boolean empty = false;
            if(!isComplete(y)){
                empty = true;
                for(int x = 0; x<6;x++){
                    for(int z = 0; z<6;z++){
                        System.out.println("x: "+x +" y: "+y+" z: "+z+" i: "+i);
                        if(this.stack[y][x][z]!= null)empty = false; 
                        this.stack[i][x][z] = this.stack[y][x][z];
                     
                    }   
                }
                i++;
            }
            if(empty)break;
        }
    }
    
    private boolean isComplete(int y){
        for(int x = 0; x<6;x++){
                for(int z = 0; z<6;z++){
                    
                  if(this.stack[y][x][z]==null) return false;   
                }
            }
        return true;
    }
    
    //updates the scene
    private void updateScene(){
        Mesh cube = new Mesh();
        String path = "src/objfiles/cube.obj";
        ObjFileLoader.loadFile(cube, path);
        ArrayList<Mesh> stackMeshes = new ArrayList<>();
        //updates scene
        boolean end = true;
        for(int y = 0; y<15; y++){
            for(int x = 0;x<6; x++){
               for(int z = 0; z<6; z++){
                   
                   if(this.stack[y][x][z] != null){
                       System.out.println("x "+x+" y "+y+" z "+z);
                       Mesh mesh = new Mesh();
                       mesh.copy(cube);
                       int i = 0;
                        for (Iterator<Triangle> iterator = mesh.m.iterator(); iterator.hasNext();) {
                            iterator.next(); 

                            boolean condition = switch (i / 2) {
                                case 0 -> (z > 0 && this.stack[y][x][z - 1] != null); // front face z-
                                case 1 -> (x > 0 && this.stack[y][x - 1][z] != null); // left face x-
                                case 2 -> (y < 14 && this.stack[y + 1][x][z] != null); // top face y+
                                case 3 -> (x < 5 && this.stack[y][x + 1][z] != null); // right face x+
                                case 4 -> true; // bottom face y-
                                case 5 -> (z < 5 && this.stack[y][x][z + 1] != null); // back face z+
                                default -> false;
                            };

                            if (condition) {
                                iterator.remove(); //removes non visible faces
                                System.out.println("face removed");
                                System.out.println(i);
                            }
                            i++;
                        }
                        
                       mesh.setSolidColor(this.stack[y][x][z]);
                       
                       mesh.applyTrans(Engine.translationMatrix(x-3,y,z-3));
                       stackMeshes.add(mesh);
                       end = false;
                   }
               }
            }
            if(end)break;
            end=true;
        }
        this.scene.set(3, stackMeshes);
    }
    
    //moves shadow to the bottom hit
    private void moveShadowDown(){  //wrong  
        while(collide(this.shadowPiece.getMovedPosition(0, -1, 0))){
            this.shadowPiece.moveDown();
        }
    }
    
    //moves shadow back to the position of the moving piece
    private void moveShadowBack(){
         // Calculate the translation vector needed to move the shadow piece to the moving piece's position
        Vector positionDifference = Vector.vectorSub(this.movingPiece.position, this.shadowPiece.position);

        // Translation matrix to move shadow piece to the moving piece's position
        float[][] translationMatrix = Engine.translationMatrix(positionDifference);

        // Apply translation to each block in the shadow piece
        for (int i = 0; i < this.shadowPiece.blocks.size(); i++) {
            // Apply the translation matrix to the block
            this.shadowPiece.blocks.get(i).applyProjection(translationMatrix);

            // Update the shadow piece positions directly
            this.shadowPiece.positions.get(i).setX(this.movingPiece.positions.get(i).getX());
            this.shadowPiece.positions.get(i).setY(this.movingPiece.positions.get(i).getY());
            this.shadowPiece.positions.get(i).setZ(this.movingPiece.positions.get(i).getZ());
        }

        // Update the shadow piece overall position to match the moving piece's position
        this.shadowPiece.position.setX(this.movingPiece.position.getX());
        this.shadowPiece.position.setY(this.movingPiece.position.getY());
        this.shadowPiece.position.setZ(this.movingPiece.position.getZ());
    }
    
    private void movePieceDown(){
        if(collide(this.movingPiece.getMovedPosition(0, -1, 0)))this.movingPiece.moveDown();
        else{
            //update the scene, stack, create new piece.
            bottomHit();
        }
    }
    
    //rotation, moving methods
    
    public void rotateRightY(){
        if(collide(this.movingPiece.getRotatedPosition(0, 90, 0))){
            moveShadowBack();
            this.movingPiece.rotateRightY();
            this.shadowPiece.rotateRightY();
            moveShadowDown();
        }
        else{
            //wallkicks
        }
    }
    
    public void rotateRightZ(){
        if(collide(this.movingPiece.getRotatedPosition(0, 0, 90))){
            moveShadowBack();
            this.movingPiece.rotateRightZ();
            this.shadowPiece.rotateRightZ();
            moveShadowDown();
        }
        else{
            //wallkicks
        }
    }
    
    public void rotateLeftY(){
        if(collide(this.movingPiece.getRotatedPosition(0, -90, 0))){
            moveShadowBack();
            this.movingPiece.rotateLeftY();
            this.shadowPiece.rotateLeftY();
            moveShadowDown();
        }
        else{
            //wallkicks
        }
    }
    
    public void rotateLeftZ(){
        if(collide(this.movingPiece.getRotatedPosition(0, 0, -90))){
            moveShadowBack();
            this.movingPiece.rotateLeftZ();
            this.shadowPiece.rotateLeftZ();
            moveShadowDown();
        }
        else{
            //wallkicks
        }
    }
    
    public void moveLeft(){
        if(collide(this.movingPiece.getMovedPosition(-1, 0, 0))){
            moveShadowBack();
            this.movingPiece.moveLeft();
            this.shadowPiece.moveLeft();
            moveShadowDown();
        }
    }
    
    public void moveRight(){
        if(collide(this.movingPiece.getMovedPosition(1, 0, 0))){
            moveShadowBack();
            this.movingPiece.moveRight();
            this.shadowPiece.moveRight();
            moveShadowDown();
        }
    }
    
    public void moveForward(){
        if(collide(this.movingPiece.getMovedPosition(0, 0, 1))){
            moveShadowBack();
            this.movingPiece.moveForward();
            this.shadowPiece.moveForward();
            moveShadowDown();
        }
    }
      
    public void moveBackward(){
        if(collide(this.movingPiece.getMovedPosition(0, 0, -1))){
            moveShadowBack();
            this.movingPiece.moveBackward();
            this.shadowPiece.moveBackward();
            moveShadowDown();
        }
    }
    
    //translates the moving piece to the shadow piece positin
    public void hardDrop(){
        Vector positionDifference = Vector.vectorSub(this.shadowPiece.position, this.movingPiece.position);

        // Translation matrix to move moving piece to the position shadow piece
        float[][] translationMatrix = Engine.translationMatrix(positionDifference);

        // Apply translation to each block in the moving piece
        for (int i = 0; i < this.movingPiece.blocks.size(); i++) {
            // Apply the translation matrix to the block
            this.movingPiece.blocks.get(i).applyProjection(translationMatrix);

            // Update the movingPiece positions directly
            this.movingPiece.positions.get(i).setX(this.shadowPiece.positions.get(i).getX());
            this.movingPiece.positions.get(i).setY(this.shadowPiece.positions.get(i).getY());
            this.movingPiece.positions.get(i).setZ(this.shadowPiece.positions.get(i).getZ());
        }

        // Update the shadow piece overall position to match the moving piece's position
        this.movingPiece.position.setX(this.shadowPiece.position.getX());
        this.movingPiece.position.setY(this.shadowPiece.position.getY());
        this.movingPiece.position.setZ(this.shadowPiece.position.getZ());
        bottomHit();
    }
    
    //moves the piece down (soft drop)
    public void softDrop(){
        if(collide(this.movingPiece.getMovedPosition(0, -1, 0)))this.movingPiece.moveDown();
    }
    
}
