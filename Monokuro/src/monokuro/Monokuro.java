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

    public static void showUsage() {
        System.out.println("Usage:\n"
                + "Single file process: java -jar Monokuro.jar -f[!] InputImage [OutputImage]\n"
                + "Folder process: java -jar Monokuro.jar -d[!] InputFolder [OutputFolder]");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            //test_convertOnce(new File("testImage.jpg"));
            //test_convertOnce(new File("testImage.jpg__2.png"));
            //System.out.println(args.length);
            if (args.length < 2) {
                Monokuro.showUsage();
                return;
            }
            String option = args[0];
            String input = args[1];
            String output = input + "_mnc";
            if (args.length > 2) {
                output = args[2];
            }


            if (option.startsWith("-f")) {
                //File
                CommonImage oriImage = new CommonImage(new File(input));

                MNCImage mncImage = new MNCImage(oriImage, (byte) 2);
                if (option.endsWith("!")) {
                    mncImage.saveMNCImageToFile(new File(output));
                } else {
                    CommonImage convertedImage = mncImage.convertToCommonImage();
                    if (output.endsWith(".jpg") || output.endsWith(".jpeg")) {
                        convertedImage.saveToFileWithFormat(new File(output), "jpg");
                    } else {
                        convertedImage.saveToFileWithFormat(new File(output), "png");
                    }
                }
            } else if (option.startsWith("-d")) {
                //Folder
                File inputFolder = new File(input);
                File outputFolder = new File(output);
                if (inputFolder.isDirectory()) {
                    File[] subFiles = inputFolder.listFiles();
                    if (subFiles != null && subFiles.length > 0) {
                        outputFolder.mkdirs();
                        for (File file : subFiles) {
                            if (file.isFile() && (file.getName().endsWith(".png") || file.getName().endsWith(".jpg"))) {
                                System.out.println("Processing " + file.getAbsolutePath() + "...");
                                String file_name = Monokuro.fileNameWithoutExtention(file.getName());
                                try {
                                    CommonImage oriImage = new CommonImage(file);

                                    MNCImage mncImage = new MNCImage(oriImage, (byte) 2);
                                    if (option.endsWith("!")) {
                                        mncImage.saveMNCImageToFile(new File(output + "/" + file_name + ".mnc"));
                                    } else {
                                        CommonImage convertedImage = mncImage.convertToCommonImage();
                                        if (output.endsWith(".jpg") || output.endsWith(".jpeg")) {
                                            convertedImage.saveToFileWithFormat(new File(output + "/" + file_name + ".jpg"), "jpg");
                                        } else {
                                            convertedImage.saveToFileWithFormat(new File(output + "/" + file_name + ".png"), "png");
                                        }
                                    }
                                } catch (IOException e) {
                                    System.out.println("Exception! " + e.getMessage());
                                }
                            }
                        }
                    }
                } else {
                    System.out.println("Input Folder is not a folder");
                }
            } else {
                Monokuro.showUsage();
            }

        } catch (Exception ex) {
            Logger.getLogger(Monokuro.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static String fileNameWithoutExtention(String fn) {
        int lastPointIndex = fn.lastIndexOf(".");

        if (lastPointIndex == -1) {
            return fn;
        } else {
            return fn.substring(0, lastPointIndex);
        }
    }

    private static void test_convertOnce(File file) throws IOException {
        CommonImage oriImage = new CommonImage(file);
        for (byte grayBits = 1; grayBits <= 4; grayBits++) {
            MNCImage mncImage = new MNCImage(oriImage, grayBits);
            mncImage.saveMNCImageToFile(new File(file.getName() + "_" + grayBits + ".mnc"));

            MNCImage readMNCImage = new MNCImage(new File(file.getName() + "_" + grayBits + ".mnc"));
            CommonImage convertedImage = readMNCImage.convertToCommonImage();
            convertedImage.saveToFileWithFormat(new File(file.getName() + "__" + grayBits + ".jpg"), "jpg");
            convertedImage.saveToFileWithFormat(new File(file.getName() + "__" + grayBits + ".png"), "png");
        }
    }
}
