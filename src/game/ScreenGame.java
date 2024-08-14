/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import engine.Engine;
import engine.elements.Mesh;
import engine.ObjFileLoader;
import engine.elements.Vector;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 *
 * @author josja
 */
public class ScreenGame {

    private final ArrayList<ArrayList<Mesh>> screen;
    private TetrisGame tgame;
    private Piece heldPiece;
    private final LinkedList<Piece> next;
    private final Vector offset;
    private int angleScore;
    private int angleHold;
    private int angleNext;

    public ScreenGame(ArrayList<ArrayList<Mesh>> screen) {
        this.screen = screen;
        next = new LinkedList<>();
        offset = new Vector(-0.375f, -0.125f, -0.125f);
        this.angleScore = 360;
        this.angleHold = 360;
        this.angleNext = 360; 
    }

    public void setTetrisGame(TetrisGame tgame) {
        this.tgame = tgame;

    }

    public void createPieces() {
        screen.add(null); //adds the held piece
        addNextPiecesToScreen(); //adds the next pieces
        createTextMeshes(); //adds the text objects
        resize();
        addNext();
    }
    
    private void addNextPiecesToScreen() {
        for (Tetrominoes t : this.tgame.next) {
            next.add(new Piece(t, false));
            screen.add(next.getLast().blocks);
        }
    }
    
    private void createTextMeshes() {
        ArrayList<Mesh> textMesh = new ArrayList<>();
        float[][] resizeMatrix = Engine.scalingMatrix(0.25f, 0.25f, 0.25f);

        textMesh.add(createTextMesh("src/objfiles/score.obj", Color.white, resizeMatrix, Engine.translationMatrix(-4.2f, 3, 15))); //score
        textMesh.add(createTextMesh("src/objfiles/hold.obj", Color.white, resizeMatrix, Engine.translationMatrix(-4.2f, 1, 15))); //hold
        textMesh.add(createTextMesh("src/objfiles/next.obj", Color.white, resizeMatrix, Engine.translationMatrix(4.2f, 2.5f, 15))); //next

        this.screen.add(textMesh);
    }
    
    private Mesh createTextMesh(String filePath, Color color, float[][] resizeMatrix, float[][] translationMatrix) {
        Mesh mesh = new Mesh();
        ObjFileLoader.loadFile(mesh, filePath);
        mesh.setSolidColor(color);
        mesh.applyTrans(resizeMatrix);
        mesh.applyTrans(translationMatrix);
        return mesh;
    }

    //updates the screen array list
    public void updateScreen() {
        updateHeldPiece();
        updateNextPieces();
        rotateTextMeshes();
    }
    
    private void updateHeldPiece() {
        if (heldPiece != null) {
            heldPiece.rotate(1, 1, 0, offset);
            screen.set(0, heldPiece.blocks);
        }
    }
    
    private void updateNextPieces() {
        int i = 1;
        for (Piece nxt : next) {
            nxt.rotate(1, 1, 0, offset);
            screen.set(i, nxt.blocks);
            i++;
        }
    }
    
    private void rotateTextMeshes() {
        float [][] rotationMatrix = Engine.rotationMatrix(0, 15, 0);
        if(angleScore<360){
            rotateMesh(screen.get(5).get(0),  Engine.translationMatrix(4.2f, -3, -15), rotationMatrix, Engine.translationMatrix(-4.2f, 3, 15));
            angleScore+=15;
        }
        if(angleHold<360){
            rotateMesh(screen.get(5).get(1), Engine.translationMatrix(4.2f, -1, -15), rotationMatrix, Engine.translationMatrix(-4.2f, 1, 15));
            angleHold+=15;
        }
        if(angleNext<360){
            rotateMesh(screen.get(5).get(2), Engine.translationMatrix(-4.2f, -2.5f, -15), rotationMatrix, Engine.translationMatrix(4.2f, 2.5f, 15));
            angleNext+=15;
        }
    }

    private void rotateMesh(Mesh mesh, float[][] translationMatrix1, float [][] rotationMatrix, float[][] translationMatrix2) {
            mesh.applyTrans(translationMatrix1);
            mesh.applyTrans(rotationMatrix);
            mesh.applyTrans(translationMatrix2);
    }

    //updates the pieces
    public void updatePieces() {
        if (tgame.saved != null) {
            heldPiece = new Piece(tgame.saved, false);
        }
        
        Iterator<Tetrominoes> itGame = this.tgame.next.iterator();
        ListIterator<Piece> itNext = next.listIterator();
        while (itGame.hasNext() && itNext.hasNext()) {
            itNext.next();
            itNext.set(new Piece(itGame.next(), false));
        }
        resize();
        if (heldPiece != null) {
            for (Mesh block : heldPiece.blocks) {
                block.applyTrans(Engine.translationMatrix(Vector.vectorAdd(offset, new Vector(-4.2f, -3.75f, 15))));//translates the piece to the y center ands add i
            }
            heldPiece.position = new Vector(-4.2f,0,15);
            screen.set(0,heldPiece.blocks);
        }
        addNext();
    }

    //resize the piece
    private void resize() {
        float[][] resize = Engine.scalingMatrix(0.25f, 0.25f, 0.25f);
        if (heldPiece != null) {
            for (Mesh block : heldPiece.blocks) {
                block.applyTrans(resize);
            }
        }
        for (Piece nxt : next) {
            for (Mesh block : nxt.blocks) {
                block.applyTrans(resize);
            }
        }
    }

    //locates and adds the piece to screen
    private void addNext() {
        int i = 1;
        float j = 1.5f;
        for (Piece nxt : next) {
            for (Mesh block : nxt.blocks) {
                block.applyTrans(Engine.translationMatrix(Vector.vectorAdd(offset, new Vector(4.2f, -3.75f + j, 15))));//translates the piece to the y center ands adds j
            }
            nxt.position = new Vector(4.2f, 0 + j, 15); //updates the possition for each piece
            screen.set(i, nxt.blocks);
            i++;
            j--;
        }
    }
    
    public void rotatingScore(){
        rotateMesh(screen.get(5).get(0), Engine.translationMatrix(4.2f, -3, -15), Engine.rotationMatrix(0, -this.angleScore, 0), Engine.translationMatrix(-4.2f, 3, 15));
        this.angleScore = 0;
    }
    public void rotatingHold(){
        rotateMesh(screen.get(5).get(1), Engine.translationMatrix(4.2f, -1, -15), Engine.rotationMatrix(0, -this.angleHold, 0), Engine.translationMatrix(-4.2f, 1, 15));
        this.angleHold = 0;
    }
    public void rotatingNext(){
        if(this.screen.size()<6)return;
        rotateMesh(screen.get(5).get(2), Engine.translationMatrix(-4.2f, -2.5f, -15), Engine.rotationMatrix(0, -this.angleNext, 0), Engine.translationMatrix(4.2f, 2.5f, 15));
        this.angleNext = 0;
    }

}
