package io.github.sinri.Monochrome;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Monokuro {
    public static void showUsage() {
//        System.out.println("Usage:\n"
//                + "Single file process: java -jar Monokuro.jar -f[GRAY_BYTES][!] InputImage [OutputImage]\n"
//                + "Folder process: java -jar Monokuro.jar -d[GRAY_BYTES][!] InputFolder [OutputFolder]\n" +
//                "GRAY_BYTES is an integer amongst [1,7]\n" +
//                "Add `!` means to generate MNC file, otherwise Image File (JPG/PNG).");
        System.out.println("Usage:\n" +
                "java -jar Monokuro.jar [mnc] GRAY_BYTES [file|dir] INPUT_IMAGE_PATH OUTPUT_MNC_PATH\n" +
                "java -jar Monokuro.jar [jpg|png|gif] [file|dir] INPUT_MNC_PATH OUTPUT_IMAGE_PATH\n" +
                "GRAY_BYTES is an integer amongst [1,7]");
    }

    public static void main(String[] args) {
        try {
            if (args.length < 4) {
                Monokuro.showUsage();
                throw new RuntimeException("ARGS LACK");
            }

            String action = args[0];

            if (action.equalsIgnoreCase("mnc")) {
                if (args.length < 5) {
                    Monokuro.showUsage();
                    throw new RuntimeException("ARGS LACK");
                }

                byte gray_bytes = Byte.parseByte(args[1]);
                if (gray_bytes < 1 || gray_bytes > 7) {
                    throw new RuntimeException("gray_bytes should be amongst [1,7]!");
                }

                String fs_type = args[2];
                String input_path = args[3];
                String output_path = args[4];
                if (fs_type.equalsIgnoreCase("file")) {
                    encodeCommonImageToMncFile(gray_bytes, input_path, output_path);
                } else if (fs_type.equalsIgnoreCase("dir")) {
                    encodeCommonImagesInDirToMncFiles(gray_bytes, input_path, output_path);
                } else {
                    throw new RuntimeException("fs_type should be `file` or `dir`!");
                }
            } else if (
                    action.equalsIgnoreCase("jpg")
                            || action.equalsIgnoreCase("png")
                            || action.equalsIgnoreCase("gif")
            ) {
                String fs_type = args[1];
                String input_path = args[2];
                String output_path = args[3];
                if (fs_type.equalsIgnoreCase("file")) {
                    decodeMncFileToCommonImage(input_path, output_path, action);
                } else if (fs_type.equalsIgnoreCase("dir")) {
                    decodeMncFilesInDirToCommonImages(input_path, output_path, action);
                } else {
                    throw new RuntimeException("fs_type should be `file` or `dir`!");
                }
            } else {
                throw new RuntimeException("action should be `encode` or `decode`!");
            }
        } catch (Exception e) {
            System.err.print(e.getMessage());
            showUsage();
        }
    }

    protected static void encodeCommonImageToMncFile(byte gray_bytes, String commonImageFilePath, String mncFilePath) throws IOException {
        CommonImage oriImage = new CommonImage(new File(commonImageFilePath));
        MNCImage mncImage = new MNCImage(oriImage, gray_bytes);
        mncImage.saveMNCImageToFile(new File(mncFilePath));
    }

    protected static void decodeMncFileToCommonImage(String mncFilePath, String commonImageFilePath, String format) throws IOException {
        MNCImage mncImage = new MNCImage(new File(mncFilePath));
        CommonImage commonImage = mncImage.convertToCommonImage();
        commonImage.saveToFileWithFormat(new File(commonImageFilePath), format);
    }

    protected static void encodeCommonImagesInDirToMncFiles(byte gray_bytes, String commonImageDirPath, String mncDirPath) throws IOException {
        File inputFolder = new File(commonImageDirPath);
        File outputFolder = new File(mncDirPath);

        if (!inputFolder.isDirectory()) {
            throw new RuntimeException("input dir is not there!");
        }

        File[] subFiles = inputFolder.listFiles();
        if (subFiles == null || subFiles.length <= 0) {
            throw new RuntimeException("input dir empty!");
        }
        if (outputFolder.isDirectory() || outputFolder.mkdirs()) {
            for (File file : subFiles) {
                if (file.isFile() && (file.getName().endsWith(".png") || file.getName().endsWith(".jpg"))) {
                    System.out.println("Processing " + file.getAbsolutePath() + "...");

                    encodeCommonImageToMncFile(
                            gray_bytes,
                            file.getAbsolutePath(),
                            outputFolder.getAbsolutePath() + File.separator + file.getName() + ".mnc"
                    );
                }
            }
        } else {
            throw new RuntimeException("output dir is not there!");
        }
    }

    protected static void decodeMncFilesInDirToCommonImages(String mncDirPath, String commonImageDirPath, String format) throws IOException {
        File inputFolder = new File(mncDirPath);
        File outputFolder = new File(commonImageDirPath);

        if (!inputFolder.isDirectory()) {
            throw new RuntimeException("input dir is not there!");
        }

        File[] subFiles = inputFolder.listFiles();
        if (subFiles == null || subFiles.length <= 0) {
            throw new RuntimeException("input dir empty!");
        }
        if (outputFolder.isDirectory() || outputFolder.mkdirs()) {
            for (File file : subFiles) {
                if (file.isFile() && (file.getName().endsWith(".png") || file.getName().endsWith(".jpg"))) {
                    System.out.println("Processing " + file.getAbsolutePath() + "...");

                    decodeMncFileToCommonImage(
                            file.getAbsolutePath(),
                            outputFolder.getAbsolutePath() + File.separator + file.getName() + "." + format,
                            format
                    );
                }
            }
        } else {
            throw new RuntimeException("output dir is not there!");
        }
    }

    /**
     * @param args the command line arguments
     * @deprecated
     */
    public static void mainOld(String[] args) {
        try {
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

            byte gray_bytes = 2;

            if (option.startsWith("-f")) {
                if (option.length() > 2) {
                    String gray_bytes_string = option.substring(2, 3);
                    if (!gray_bytes_string.equals("!")) {
                        gray_bytes = (byte) Integer.parseInt(gray_bytes_string, 10);
                        if (gray_bytes < 1 || gray_bytes > 7) {
                            throw new IllegalArgumentException("gray bytes is not correct, should be an integer in [1,7]");
                        }
                    }
                }
                //File
                CommonImage oriImage = new CommonImage(new File(input));

                MNCImage mncImage = new MNCImage(oriImage, gray_bytes);// confirm a best [1,7], default 2
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
                if (option.length() > 2) {
                    String gray_bytes_string = option.substring(2, 3);
                    if (!gray_bytes_string.equals("!")) {
                        gray_bytes = (byte) Integer.parseInt(gray_bytes_string, 10);
                        if (gray_bytes < 1 || gray_bytes > 7) {
                            throw new IllegalArgumentException("gray bytes is not correct, should be an integer in [1,7]");
                        }
                    }
                }
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
                                String file_name = Monokuro.fileNameWithoutExtension(file.getName());
                                try {
                                    CommonImage oriImage = new CommonImage(file);

                                    MNCImage mncImage = new MNCImage(oriImage, gray_bytes);
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

    public static String fileNameWithoutExtension(String fn) {
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
