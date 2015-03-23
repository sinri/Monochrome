/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package monokuro;

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
public class MNCImageTest {
    
    public MNCImageTest() {
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
     * Test of buildMNC method, of class MNCImage.
     */
    @Test
    public void testBuildMNC() {
        System.out.println("buildMNC");
        CommonImage image = null;
        byte grayBits = 2;
        MNCImage.buildMNC(image, grayBits);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of grayFromARGB method, of class MNCImage.
     */
    @Test
    public void testGrayFromARGB() {
        System.out.println("grayFromARGB");
        int a = 0;
        int r = 0xED;
        int g =  0xEE;
        int b =  0xEF;
        byte grayBits = 2;
        int expResult =  0xc0;
        int result = MNCImage.grayFromARGB(a, r, g, b, grayBits);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of argbValueFromGray method, of class MNCImage.
     */
    @Test
    public void testArgbValueFromGray() {
        System.out.println("argbValueFromGray");
        int gray = 192;
        byte grayBits = 2;
        int expResult = 255;
        int result = MNCImage.argbValueFromGray(gray, grayBits);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
    
}
