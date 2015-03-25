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
            test_convertOnce(new File("testImage.jpg"));
            test_convertOnce(new File("testImage.jpg__2.png"));
        } catch (IOException ex) {
            Logger.getLogger(Monokuro.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void test_convertOnce(File file) throws IOException {
        CommonImage oriImage = new CommonImage(file);
        for (byte grayBits = 1; grayBits <= 4; grayBits++) {
            MNCImage mncImage = new MNCImage(oriImage, grayBits);
            mncImage.saveMNCImageToFile(new File(file.getName()+"_" + grayBits + ".mnc"));

            MNCImage readMNCImage = new MNCImage(new File(file.getName()+"_" + grayBits + ".mnc"));
            CommonImage convertedImage = readMNCImage.convertToCommonImage();
            convertedImage.saveToFileWithFormat(new File(file.getName()+"__" + grayBits + ".jpg"), "jpg");
            convertedImage.saveToFileWithFormat(new File(file.getName()+"__" + grayBits + ".png"), "png");
        }
    }
}
