/*
 * Click nbfs://nbhost/FileSystem/Templates/Licenses/license-default.txt to change this license
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.Timer;


/**
 *
 * @author josja
 */
public class TetrisGame {
    private final Timer timer;
    public Piece movingPiece;
    public Piece shadowPiece;
    private int delay;
    private int level;
    private final Color [][][] stack; 
    private final ArrayList<ArrayList<Mesh>> scene;
    public final LinkedList<Tetrominoes> next;
    public Tetrominoes saved;
    private final ScreenGame sgame;
    private int score;
    public float boardAngleY;
    public float lastBoardAngleY;
    public float boardAngleX;
    public float lastBoardAngleX;
    private final HashMap<Float,Vector[]> movements;
    private final Random random;
    private final Vector[] wallkicks;
    private boolean held;
    
    
    public TetrisGame(ArrayList<ArrayList<Mesh>> scene, ScreenGame sgame){
        this.level = 0;
        this.stack = new Color[15][6][6]; //y,x,z
        this.scene = scene;
        this.next = new LinkedList<>();
        this.saved = null;
        this.sgame = sgame;
        this.boardAngleY =0;
        this.lastBoardAngleY =0;
        this.boardAngleX =0;
        this.lastBoardAngleX =0;
        this.random = new Random();
        this.movements = new HashMap<>();
        this.movements.put(0f, new Vector[]{new Vector(0,0,1),new Vector(0,0,-1),new Vector(-1,0,0),new Vector(1,0,0)});//forward, backward,left,right
        this.movements.put(90f, new Vector[]{new Vector(-1,0,0),new Vector(1,0,0),new Vector(0,0,-1),new Vector(0,0,1)});
        this.movements.put(180f, new Vector[]{new Vector(0,0,-1),new Vector(0,0,1),new Vector(1,0,0),new Vector(-1,0,0)});
        this.movements.put(270f, new Vector[]{new Vector(1,0,0),new Vector(-1,0,0),new Vector(0,0,1),new Vector(0,0,-1)});
        this.wallkicks = new Vector []{new Vector(1, 0, 0), new Vector(-1, 0, 0), new Vector(0, 0, 1), new Vector(0, 0, -1), new Vector(1, 0, 1), new Vector(-1, 0, -1), new Vector(1, -1, 0), new Vector(-1, -1, 0), new Vector(0, -1, 1), new Vector(0, -1, -1), new Vector(1, -1, 1), new Vector(-1, -1, -1), new Vector(0, 2, 0), new Vector(1, 2, 0), new Vector(-1, 2, 0), new Vector(0, 2, 1), new Vector(0, 2, -1), new Vector(1, 2, 1), new Vector(-1, 2, -1)};
        this.held= false;
        sgame.setTetrisGame(this);
        startGame();
        this.timer = new Timer(delay, (ActionEvent e) -> {
            movePieceDown();
        });
        
    }
    
    private void startGame(){
        calculateDelay(); //calculates delay
        for(int i= 0; i<4;i++)this.scene.add(new ArrayList<>());// adds an array list to scene 
        for (int i = 0; i < 4; i++) {
            int randomIndex = random.nextInt(Tetrominoes.values().length); // Generate random index
            this.next.add(Tetrominoes.values()[randomIndex]); // Add corresponding Tetrominoes value
        }
        generateNewPiece(); //generates a new piece
        Mesh board = new Mesh(); //creates the board
        ObjFileLoader.loadFile(board, "src/objfiles/board.obj");
        board.setDrawBorder(true);
        board.setIsTransparent(true);
        board.applyTrans(Engine.translationMatrix(-3f, 0,-3f)); //translates board to origin 
        ArrayList<Mesh> boardMesh = new ArrayList<>(); 
        boardMesh.add(board);
        this.scene.set(2,boardMesh); // index 2 board
        ArrayList<Mesh> stackMeshes = new ArrayList<>();
        this.scene.set(3,stackMeshes); // index 3 Stack
        this.sgame.createPieces();
    }
    
    public void startTimer(){
        this.timer.start();
    }
    
    private void endGame(){
        System.out.println("game ended, score: "+score);
    }
    
    private void generateNewPiece(){
        movingPiece = new Piece(this.next.pop(),false);
        shadowPiece = new Piece(this.movingPiece.type,true);
        moveShadowDown();
        this.next.add(Tetrominoes.values()[random.nextInt(Tetrominoes.values().length)]); //adds new piece to the stack
        this.scene.set(0,movingPiece.blocks); //index 0 moving piece
        this.scene.set(1,shadowPiece.blocks); //index 1 shadow piece
        this.sgame.updatePieces();
        this.sgame.rotatingNext();
    }
    
    private void generateNewPiece(Tetrominoes type){
        movingPiece = new Piece(type,false);
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
            if(y>14){
            endGame();
            return;
            }
            minY = Math.min(y, minY);
            maxY = Math.max(y, maxY);
            this.stack[y][x][z] = color;
        }
        checkCompleteLines(minY, maxY);
        updateScene();
        generateNewPiece();
        this.held = false;
    }
    
    //checks the affected planes and clears if the plane is completed
    private void checkCompleteLines(int min, int max){
        for(int y = min;y<=max;y++){
            if(isComplete(y)){
                Scoring(clearLines(y)); 
                break;
            }
        }
    }    
    private void Scoring(int clearedLines){
        this.sgame.rotatingScore();
        switch (clearedLines){
            case 1 -> score += 220;
            case 2 -> score += 550;
            case 3 -> score += 1000;
        }
        System.out.println("this.score = " + this.score);
    }    
    
    private int clearLines(int y){
        int clearedLines = 1;
        int i = y;
        y++;
        while(y<15 && i<15){
            
            if(!isComplete(y)){
                for(int x = 0; x<6;x++){
                    for(int z = 0; z<6;z++){
                        this.stack[i][x][z] = this.stack[y][x][z];
                    }   
                }
                i++;
            }else{
                clearedLines++;}
            y++;
        }
        return clearedLines;
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
    private void moveShadowDown(){
        while(collide(this.shadowPiece.getMovedPosition(new Vector(0,-1,0)))){
            this.shadowPiece.move(new Vector(0,-1,0));
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
        if(collide(this.movingPiece.getMovedPosition(new Vector(0,-1,0))))this.movingPiece.move(new Vector(0,-1,0));
        else{
            //update the scene, stack, create new piece.
            bottomHit();
        }
    }
    
    //rotates the board
    public void rotateBoard(float yAngle, boolean xRotation){
        this.boardAngleY +=yAngle;
        if(this.boardAngleY >270){
            this.boardAngleY = 0;
            this.lastBoardAngleY = -90;
        }
        
        if(this.boardAngleY <0){ 
            this.boardAngleY = 270;
            this.lastBoardAngleY = 360;
        }
        if(xRotation){
            if(this.boardAngleX == -90){
                this.boardAngleX = 0;
                this.lastBoardAngleX = -90;
            }else{
                this.boardAngleX = -90;
                this.lastBoardAngleX = 0;
            }
        }
    }
    
    /*
    rotation, 
    moving 
    methods
    */
    
    //test translations if piece can't rotate
    private Vector wallkicks(ArrayList<Vector> position){
        for(Vector testPosition: this.wallkicks){
            if(collide(Piece.getMovedPosition(testPosition, position)))return testPosition;
        }
        return null;
    }
    
    public void rotateRightY(){
        ArrayList<Vector> rotated = this.movingPiece.getRotatedPosition(0, 90, 0);
        if(collide(rotated)){
            moveShadowBack();
            this.movingPiece.rotateRightY();
            this.shadowPiece.rotateRightY();
            moveShadowDown();
        }
        else{
            Vector translation = wallkicks(rotated);
            System.out.println("translation = " + translation);
            if(translation != null){
                moveShadowBack();
                    this.movingPiece.rotateRightY();
                    this.movingPiece.move(translation);
                    this.shadowPiece.rotateRightY();
                    this.shadowPiece.move(translation);
                moveShadowDown();
            }
        }
    }
    
    public void rotateRightZ(){
        ArrayList<Vector> rotated = this.movingPiece.getRotatedPosition(0, 0, 90);
        if(collide(rotated)){
            moveShadowBack();
            this.movingPiece.rotateRightZ();
            this.shadowPiece.rotateRightZ();
            moveShadowDown();
        }
        else{
            Vector translation = wallkicks(rotated);
            System.out.println("translation = " + translation);
            if(translation != null){
                moveShadowBack();
                    this.movingPiece.rotateRightZ();
                    this.movingPiece.move(translation);
                    this.shadowPiece.rotateRightZ();
                    this.shadowPiece.move(translation);
                moveShadowDown();
            }
        }
    }
    
    public void rotateLeftY(){
        ArrayList<Vector> rotated = this.movingPiece.getRotatedPosition(0, -90, 0);
        if(collide(rotated)){
            moveShadowBack();
            this.movingPiece.rotateLeftY();
            this.shadowPiece.rotateLeftY();
            moveShadowDown();
        }
        else{
            Vector translation = wallkicks(rotated);
            System.out.println("translation = " + translation);
            if(translation != null){
                moveShadowBack();
                    this.movingPiece.rotateLeftY();
                    this.movingPiece.move(translation);
                    this.shadowPiece.rotateLeftY();
                    this.shadowPiece.move(translation);
                moveShadowDown();
            }
        }
    }
    
    public void rotateLeftZ(){
        ArrayList<Vector> rotated = this.movingPiece.getRotatedPosition(0, 0, -90);
        if(collide(rotated)){
            moveShadowBack();
            this.movingPiece.rotateLeftZ();
            this.shadowPiece.rotateLeftZ();
            moveShadowDown();
        }
        else{
            Vector translation = wallkicks(rotated);
            System.out.println("translation = " + translation);
            if(translation != null){
                moveShadowBack();
                    this.movingPiece.rotateLeftZ();
                    this.movingPiece.move(translation);
                    this.shadowPiece.rotateLeftZ();
                    this.shadowPiece.move(translation);
                moveShadowDown();
            }
        }
    }
    
    public void moveLeft(){
        if(collide(this.movingPiece.getMovedPosition(this.movements.get(this.boardAngleY)[2]))){
            moveShadowBack();
            this.movingPiece.move(this.movements.get(this.boardAngleY)[2]);
            this.shadowPiece.move(this.movements.get(this.boardAngleY)[2]);
            moveShadowDown();
        }
    }
    
    public void moveRight(){
        if(collide(this.movingPiece.getMovedPosition(this.movements.get(this.boardAngleY)[3]))){
            moveShadowBack();
            this.movingPiece.move(this.movements.get(this.boardAngleY)[3]);
            this.shadowPiece.move(this.movements.get(this.boardAngleY)[3]);
            moveShadowDown();
        }
    }
    
    public void moveForward(){
        if(collide(this.movingPiece.getMovedPosition(this.movements.get(this.boardAngleY)[0]))){
            moveShadowBack();
            this.movingPiece.move(this.movements.get(this.boardAngleY)[0]);
            this.shadowPiece.move(this.movements.get(this.boardAngleY)[0]);
            moveShadowDown();
        }
    }
      
    public void moveBackward(){
        if(collide(this.movingPiece.getMovedPosition(this.movements.get(this.boardAngleY)[1]))){
            moveShadowBack();
            this.movingPiece.move(this.movements.get(this.boardAngleY)[1]);
            this.shadowPiece.move(this.movements.get(this.boardAngleY)[1]);
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
        if(collide(this.movingPiece.getMovedPosition(new Vector(0,-1,0))))this.movingPiece.move(new Vector(0,-1,0));
    }
    
    //hold piece
    public void holdPiece(){
        if(this.held)return;
        if(this.saved == null){
            this.saved = this.movingPiece.type;
            generateNewPiece();
        }else{
            Tetrominoes temp = this.movingPiece.type;
            generateNewPiece(this.saved);
            this.saved = temp;
        }
        this.sgame.rotatingHold();
        this.sgame.updatePieces();
        this.held = true;
    }
    
    
}
