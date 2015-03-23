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
//            MNCImage.buildMNC(oriImage,(byte)2);
            MNCImage mncImage=new MNCImage(oriImage,(byte)3);
            mncImage.saveMNCImageToFile(new File("guhehe.mnc"));
        } catch (IOException ex) {
            Logger.getLogger(Monokuro.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
}
