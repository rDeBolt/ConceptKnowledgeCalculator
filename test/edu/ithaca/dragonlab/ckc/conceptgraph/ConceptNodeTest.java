package edu.ithaca.dragonlab.ckc.conceptgraph;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class ConceptNodeTest {

//    @Test
//    public void isAncestorTest() {
//        ConceptGraph orig = PracticalConceptGraphTest.simpleTestGraphTest();
//
//        ConceptNode node = orig.findNodeById("C");
//        boolean num = orig.findNodeById("A").isAncestorOf(node);
//        Assert.assertEquals(true,num);
//
//        ConceptNode node2 = orig.findNodeById("A");
//        boolean num2 = orig.findNodeById("C").isAncestorOf(node2);
//        Assert.assertEquals(false,num2);
//
//
//        ConceptNode node3 = orig.findNodeById("B");
//        boolean num3 = orig.findNodeById("A").isAncestorOf(node3);
//        Assert.assertEquals(true,num3);
//
//    }

    @Test
    public void isAncestorTest() {
        ConceptGraph orig = ExampleConceptGraphFactory.mediumTestGraphTest();

        ConceptNode node = orig.findNodeById("C");
        boolean num = orig.findNodeById("A").isAncestorOf(node);
        Assert.assertEquals(true,num);

        ConceptNode node2 = orig.findNodeById("F");
        boolean num2 = orig.findNodeById("A").isAncestorOf(node2);
        Assert.assertEquals(true,num2);


        ConceptNode node3 = orig.findNodeById("D");
        boolean num3 = orig.findNodeById("B").isAncestorOf(node3);
        Assert.assertEquals(true,num3);


        ConceptNode node4 = orig.findNodeById("D");
        boolean num4 = orig.findNodeById("G").isAncestorOf(node4);
        Assert.assertEquals(false,num4);

        ConceptNode node5 = orig.findNodeById("F");
        boolean num5 = orig.findNodeById("A").isAncestorOf(node5);
        Assert.assertEquals(true,num5);

        ConceptNode node6 = orig.findNodeById("D");
        boolean num6 = orig.findNodeById("A").isAncestorOf(node6);
        Assert.assertEquals(true,num6);
    }



    @Test
    public void buildLearningObjectSummaryOccurrenceSimpleTest(){
		//test the creation of graph
    	//test findNodeBy first
		//then test the nodes

        //tests the size of the buildLearningObjectList


        ConceptGraph graph = ExampleConceptGraphFactory.makeSimpleCompleteWithData();

        HashMap<String, Integer> testA = new HashMap<String, Integer>();
        graph.findNodeById("A").buildLearningObjectSummaryList(testA);
        Assert.assertEquals(6, testA.size());

        HashMap<String, Integer> testB = new HashMap<String, Integer>();
        graph.findNodeById("B").buildLearningObjectSummaryList(testB);
        Assert.assertEquals(6,testB.size());

        HashMap<String, Integer> testC = new HashMap<String, Integer>();
        graph.findNodeById("C").buildLearningObjectSummaryList(testC);
        Assert.assertEquals(4, testC.size());
//


    }









	
}
