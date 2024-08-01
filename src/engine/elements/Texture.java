/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package engine.elements;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
/**
 *
 * @author josja
 */
public class Texture {
    private BufferedImage image;
    public Texture(String src){
        try{
            image = ImageIO.read(new File(src));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public int getWidth() {
        return image.getWidth()-1;
    }

    public int getHeight() {
        return image.getHeight()-1;
    }

    public int getPixel(int x, int y) {
        try{
        return image.getRGB(x, y);
        }catch(Exception e){
            System.out.println("x "+x);
            System.out.println("y "+y);
            return image.getRGB(x, y);
        }
    }
}
