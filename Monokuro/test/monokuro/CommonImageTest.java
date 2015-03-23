/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package monokuro;

import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Sinri
 */
public class CommonImageTest {
    
    public CommonImageTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of saveToFileWithFormat method, of class CommonImage.
     */
    @Test
    public void testSaveToFileWithFormat() throws Exception {
        System.out.println("saveToFileWithFormat");
        File file = null;
        String format = "";
        CommonImage instance = null;
        instance.saveToFileWithFormat(file, format);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getARGBArray method, of class CommonImage.
     */
    @Test
    public void testGetARGBArray() {
        System.out.println("getARGBArray");
        int[] pixels = null;
        CommonImage instance = null;
        int[] expResult = null;
        int[] result = instance.getARGBArray(pixels);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setImageWithARGBArray method, of class CommonImage.
     */
    @Test
    public void testSetImageWithARGBArray() {
        System.out.println("setImageWithARGBArray");
        int width = 0;
        int height = 0;
        int[] pixels = null;
        CommonImage instance = null;
        instance.setImageWithARGBArray(width, height, pixels);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of alphaFromARGB method, of class CommonImage.
     */
    @Test
    public void testAlphaFromARGB() {
        System.out.println("alphaFromARGB");
        int cARGB = 0xF0ABCDEF;
        int expResult = (byte) 0xF0;
        int result = CommonImage.alphaFromARGB(cARGB);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of redFromARGB method, of class CommonImage.
     */
    @Test
    public void testRedFromARGB() {
        System.out.println("redFromARGB");
        int cARGB = 0xF0ABCDEF;
        int expResult = (byte) 0xAB;
        int result = CommonImage.redFromARGB(cARGB);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of greenFromARGB method, of class CommonImage.
     */
    @Test
    public void testGreenFromARGB() {
        System.out.println("greenFromARGB");
        int cARGB = 0xF0ABCDEF;
        int expResult = (byte) 0xCD;
        int result = CommonImage.greenFromARGB(cARGB);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of blueFromARGB method, of class CommonImage.
     */
    @Test
    public void testBlueFromARGB() {
        System.out.println("blueFromARGB");
        int cARGB = 0xF0ABCDEF;
        int expResult = (byte) 0xEF;
        int result = CommonImage.blueFromARGB(cARGB);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of colorWithARGB method, of class CommonImage.
     */
    @Test
    public void testColorWithARGB() {
        System.out.println("colorWithARGB");
        byte alpha = (byte) 0xF0;
        byte red = (byte) 0xAB;
        byte green = (byte) 0xCD;
        byte blue = (byte) 0xEF;
        int expResult = 0xF0ABCDEF;
        int result = CommonImage.colorWithARGB(alpha, red, green, blue);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
    
}
