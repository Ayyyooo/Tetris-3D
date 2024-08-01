/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package engine;
import engine.elements.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
/**
 *
 * @author josja
 */
public class ObjFileLoader {
    
    public static void loadFile(Mesh mesh, String path){
        ArrayList <Vertex> vertex = new ArrayList<>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            while((line = br.readLine()) != null){
                if(line.isBlank())continue;
                String[] tokens = line.split("\\s+"); 
            
                switch(tokens[0]){
                    case "v" -> {
                        vertex.add(new Vertex(Float.parseFloat(tokens[1]),Float.parseFloat(tokens[2]),Float.parseFloat(tokens[3]),0,0));
                    }
                    case "f" -> {
                        int [] index = new int[3];
                        for(int i = 1; i<tokens.length;i++){
                            if(tokens[i].contains("//")){
                                String [] ind = tokens[i].split("//");
                                index [i-1] = Integer.parseInt(ind[0])-1;
                            }else if(tokens[i].contains("/")){
                                String [] ind = tokens[i].split("/");
                                index [i-1] = Integer.parseInt(ind[0])-1;
                            }else{
                                index [i-1] = Integer.parseInt(tokens[i])-1;
                            }
                        }
                        Triangle tri = new Triangle(new Vertex[]{
                            vertex.get(index[0]),
                            vertex.get(index[1]),
                            vertex.get(index[2])
                        });
                        mesh.m.add(tri);
                    }
                
                }
                
            }
        }catch(IOException e){
             e.printStackTrace();
        }
        
    }
}
