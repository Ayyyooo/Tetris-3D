/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import engine.Engine;
import engine.elements.Mesh;
import engine.elements.Triangle;
import engine.elements.Vector;
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

    public ScreenGame(ArrayList<ArrayList<Mesh>> screen) {
        this.screen = screen;
        next = new LinkedList<>();
        offset = new Vector(-0.375f, -0.125f, -0.125f);
    }

    public void setTetrisGame(TetrisGame tgame) {
        this.tgame = tgame;

    }

    public void createPieces() {
        screen.add(null); //adds the held piece
        for (Tetrominoes t : this.tgame.next) {
            next.add(new Piece(t, false)); //adds the next pieces
            screen.add(next.getLast().blocks);
        }
        resize();
        addNext();
    }

    //updates the screen array list
    public void updateScreen() {
        if (heldPiece != null) {
            heldPiece.rotate(1,1,0,offset);
            screen.set(0, heldPiece.blocks);
        }
        int i = 1;
        for (Piece nxt : next) {
            nxt.rotate(1, 1, 0, offset);
            screen.set(i, nxt.blocks);
            i++;
        }

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

}
