/*
 * Created on Jun 9, 2003
 * by bnicolle
 */
package org.eclipse.cdt.core.model.tests;

import org.eclipse.cdt.core.model.CModelException;
import org.eclipse.cdt.core.model.ICElement;
import org.eclipse.cdt.core.model.ITranslationUnit;
import org.eclipse.cdt.internal.core.model.CElement;

import junit.framework.*;

import java.util.Stack;


/**
 * @author bnicolle
 *
 */
public class MacroTests extends IntegratedCModelTest {
	/**
	 * @param name
	 */
	public MacroTests(String name) {
		super(name);
	}
	
	/**
	 * @see org.eclipse.cdt.internal.core.model.IntegratedCModelTest
	 */
	public String getSourcefileSubdir() {
		return "resources/cmodel/";
	}

	/**
	 * @see org.eclipse.cdt.internal.core.model.IntegratedCModelTest
	 */
	public String getSourcefileResource() {
		return "MacroTests.c";
	}
	
	/**
	 * @returns a test suite named after this class
	 *          containing all its public members named "test*"
	 */
	public static Test suite() {
		TestSuite suite= new TestSuite(MacroTests.class);
		return suite;
	}


    /* This is a list of elements in the test .c file. It will be used 
     * in a number of places in the tests
     */
    String[] expectedStringList= {"Z", "X", "Y", 
        "SomeName", "", "A::BCD", "DEFA", "DB", "B::SomeName", 
        "PINT", "myPINT", "foobar"};
    int[]  expectedOffsets={  8,26,39,55,75,89,114,130,152,187,212,227};
    int[]  expectedLengths={  1, 1, 1, 1, 1, 8,  4,  2, 18,  4,  6,  6};
    /* This is a list of that the types of the above list of elements is 
     * expected to be.
     */
    int[] expectedTypes= { ICElement.C_MACRO, ICElement.C_MACRO, 
        ICElement.C_MACRO, ICElement.C_STRUCT, 
        ICElement.C_STRUCT, ICElement.C_VARIABLE, ICElement.C_MACRO, 
        ICElement.C_MACRO, ICElement.C_VARIABLE, ICElement.C_MACRO,
        ICElement.C_VARIABLE, ICElement.C_FUNCTION_DECLARATION};


    public void testMacro_0001() throws CModelException {
        ITranslationUnit myTranslationUnit = getTU();
        ICElement myElement;
        Stack missing=new Stack();
        int x;
        
        for (x=0;x<expectedStringList.length;x++) {
            myElement=myTranslationUnit.getElement(expectedStringList[x]);
            if (myElement==null)
                missing.push(expectedStringList[x]);
            else {
                assertTrue("Expected:" + expectedStringList[x] + " Got:" + myElement.getElementName(),
                    expectedStringList[x].equals(myElement.getElementName()));
                    
                assertTrue("Expected type for '" + expectedStringList[x] + "':" + expectedTypes[x] + " Got:" + myElement.getElementType(),
                                    expectedTypes[x] == myElement.getElementType());
                                    
                int offset = -1;
                int length = -1;
                
                if (myElement instanceof CElement) {
                    CElement elem = (CElement)myElement;
                    offset = elem.getIdStartPos();
                    length = elem.getIdLength();
                }
                                    
                assertTrue("Expected offset for '" + expectedStringList[x] + "':" + expectedOffsets[x] + " Got:" + offset,
                                                    expectedOffsets[x] == offset);
                                                    
                assertTrue("Expected length for '" + expectedStringList[x] + "':" + expectedLengths[x] + " Got:" + length,
                                                    expectedLengths[x] == length);
            }
            
        }
        if (!missing.empty()) {
            String output=new String("Could not get elements: ");
            while (!missing.empty())
                output+=missing.pop() + " ";
            assertTrue(output, false);
        }

    }
}
