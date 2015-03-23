/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package monokuro;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sinri
 */
public class Monokuro {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            CommonImage oriImage=new CommonImage(new File("testImage.jpg"));
            for(byte grayBits=1;grayBits<=4;grayBits++){
                MNCImage mncImage=new MNCImage(oriImage,grayBits);
                mncImage.saveMNCImageToFile(new File("guhehe"+grayBits+".mnc"));
                
                MNCImage readMNCImage=new MNCImage(new File("guhehe"+grayBits+".mnc"));
                CommonImage convertedImage=readMNCImage.convertToCommonImage();
                convertedImage.saveToFileWithFormat(new File("ohoho"+grayBits+".jpg"), "jpg");
            }
        } catch (IOException ex) {
            Logger.getLogger(Monokuro.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
}
