/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package monokuro;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Sinri
 */
public class CommonImage {
    
    public BufferedImage image=null;
    
    public CommonImage(File file) throws IOException{
        image=ImageIO.read(file);
    }
    
    public CommonImage(int width,int height,int[] pixels){
        image=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        this.setImageWithARGBArray(width, height, pixels);
    }
    
    /**
     * 
     * @param file
     * @param format jpg, png and gif
     * @throws IOException 
     */
    public void saveToFileWithFormat(File file,String format) throws IOException{
        ImageIO.write(image, format, file);
    }
    
    /**
     * 
     * @param pixels null I think also works
     * @return 
     */
    public int[] getARGBArray(int[] pixels){
        int x=0;
        int y=0;
        int width=image.getWidth();
        int height=image.getHeight();
        
        
        int type=image.getType();
        if(type==BufferedImage.TYPE_INT_ARGB || type==BufferedImage.TYPE_INT_RGB){
            return (int [])image.getRaster().getDataElements(x, y, width, height, pixels);
        }else{
            return image.getRGB(x, y, width, height, pixels, 0, width);
        }
    }
    
    public final void setImageWithARGBArray(int width, int height, int[] pixels){
        int x=0;
        int y=0;
        int type=image.getType();
        if(type==BufferedImage.TYPE_INT_ARGB || type==BufferedImage.TYPE_INT_RGB){
            image.getRaster().setDataElements(x, y, width, height, pixels);
        }else{
            image.setRGB(x, y, width, height, pixels, 0, width);
        }
    }
    
    public static int alphaFromARGB(int cARGB){
        return  ((cARGB >> 24) & 0x000000FF);
    }
    public static int redFromARGB(int cARGB){
        return  ((cARGB >> 16) & 0x000000FF);
    }
    public static int greenFromARGB(int cARGB){
        return  ((cARGB >> 8) & 0x000000FF);
    }
    public static int blueFromARGB(int cARGB){
        return  ((cARGB) & 0x000000FF);
    }
    
    public static int colorWithARGB(int alpha,int red, int green, int blue){
        return (alpha<<24)|(red<<16)|(green<<8)|blue;
    }
}
