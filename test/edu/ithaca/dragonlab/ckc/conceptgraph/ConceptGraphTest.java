package edu.ithaca.dragonlab.ckc.conceptgraph;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ithaca.dragonlab.ckc.io.ConceptGraphRecord;
import edu.ithaca.dragonlab.ckc.io.ConceptRecord;
import edu.ithaca.dragonlab.ckc.io.LearningObjectLinkRecord;
import edu.ithaca.dragonlab.ckc.learningobject.ExampleLearningObjectFactory;
import edu.ithaca.dragonlab.ckc.learningobject.ExampleLearningObjectResponseFactory;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;

import edu.ithaca.dragonlab.ckc.learningobject.LearningObjectResponse;
import edu.ithaca.dragonlab.ckc.util.DataUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;


public class ConceptGraphTest {
	static Logger logger = LogManager.getLogger(ConceptGraphTest.class);


    @Test
    public void SuggestedConceptNodeMapSimpleTest() {
        ConceptGraph orig = ExampleConceptGraphFactory.simpleTestGraphTest();

        //what it is
        HashMap<String, List<learningObjectSuggestion>> objectSuggestionMap = orig.SuggestedConceptNodeMap();

        //what it should be
        List<learningObjectSuggestion> testList3 = new ArrayList<>();
        Assert.assertEquals(1, objectSuggestionMap.size());
        Assert.assertEquals(testList3, objectSuggestionMap.get("C"));

    }


    @Test
    public void SuggestedConceptNodeMapWillOneStudentTest() {
        ConceptGraph orig= ExampleConceptGraphFactory.willExampleConceptGraphTestOneStudent();
        //what it is
        HashMap<String, List<learningObjectSuggestion>> objectSuggestionMap = orig.SuggestedConceptNodeMap();

        Assert.assertEquals(2, objectSuggestionMap.size());
        Assert.assertEquals(objectSuggestionMap.get("If Statement").get(1).getId(), "Q3");
        Assert.assertEquals(objectSuggestionMap.get("If Statement").get(0).getId(), "Q10");
        Assert.assertEquals(objectSuggestionMap.get("While Loop").get(0).getId(), "Q10");

    }


    @Test
    public void buildLearningObjectListSimpleTest(){
        ConceptGraph orig = ExampleConceptGraphFactory.makeSimpleWithData();

        //from A nodes
        HashMap<String, Integer> testCompareA = new HashMap<String, Integer>();
        testCompareA.put("Q1",1);
        testCompareA.put("Q2",1);
        testCompareA.put("Q3",2);
        testCompareA.put("Q4",2);
        testCompareA.put("Q5",2);
        testCompareA.put("Q6",2);

        Assert.assertEquals(testCompareA, orig.buildLearningObjectSummaryList("A"));

        //from B node
        HashMap<String, Integer> testCompareB = new HashMap<String, Integer>();
        testCompareB.put("Q1",1);
        testCompareB.put("Q2",1);
        testCompareB.put("Q3",1);
        testCompareB.put("Q4",1);
        testCompareB.put("Q5",1);
        testCompareB.put("Q6",1);

        Assert.assertEquals(testCompareB, orig.buildLearningObjectSummaryList("B"));


        //from c node
        HashMap<String, Integer> testCompareC = new HashMap<String, Integer>();
        testCompareC.put("Q3",1);
        testCompareC.put("Q4",1);
        testCompareC.put("Q5",1);
        testCompareC.put("Q6",1);

        Assert.assertEquals(testCompareC, orig.buildLearningObjectSummaryList("C"));

        //from a node not in graph
        Assert.assertEquals(null, orig.buildLearningObjectSummaryList("W"));
    }




    @Test
    public void suggestedOrderBuildLearningObjectListTest(){
        List<LearningObjectLinkRecord> myList = ExampleLearningObjectFactory.makeSimpleLOLRecords();
        myList.add(new LearningObjectLinkRecord("Q10", Arrays.asList("A"),1));
        ConceptGraph orig = new ConceptGraph(ExampleConceptGraphRecordFactory.makeSimple(),
                myList, ExampleLearningObjectResponseFactory.makeSimpleResponses());

//        //from A nodes
        HashMap<String, Integer> testCompareA = new HashMap<String, Integer>();
        testCompareA.put("Q3",2);
        testCompareA.put("Q4",2);
        testCompareA.put("Q5",2);
        testCompareA.put("Q6",2);
        testCompareA.put("Q1",1);
        testCompareA.put("Q2",1);
        testCompareA.put("Q10",1);

        HashMap<String, Integer> learningSummaryFromA = orig.buildLearningObjectSummaryList("A");
        //makes sure that buildLearningObjectSummaryList works
        Assert.assertEquals(testCompareA,learningSummaryFromA);

        //build the sugggested learning object list
        List<learningObjectSuggestion> suggestedList = orig.buildLearningObjectSuggestionList(learningSummaryFromA);


        //this is ordered based on "level"
        List<learningObjectSuggestion> suggestListTest = new ArrayList<>();
        suggestListTest.add(new learningObjectSuggestion("Q3", 2,learningObjectSuggestion.Level.RIGHT) );
        suggestListTest.add(new learningObjectSuggestion("Q1", 1,learningObjectSuggestion.Level.RIGHT) );
        suggestListTest.add(new learningObjectSuggestion("Q2", 1,learningObjectSuggestion.Level.RIGHT) );
        suggestListTest.add(new learningObjectSuggestion("Q4", 2,learningObjectSuggestion.Level.WRONG) );
        suggestListTest.add(new learningObjectSuggestion("Q5", 2,learningObjectSuggestion.Level.WRONG) );
        suggestListTest.add(new learningObjectSuggestion("Q6", 2,learningObjectSuggestion.Level.WRONG) );
        suggestListTest.add(new learningObjectSuggestion("Q10", 1,learningObjectSuggestion.Level.INCOMPLETE) );


        //orders the list based off of "level"
        orig.suggestedOrderBuildLearningObjectList(suggestedList);

//        //who should call orderedList

        for (int i =0; i<suggestedList.size(); i++){

            Assert.assertEquals(suggestedList.get(i).getId(),suggestListTest.get(i).getId());
            Assert.assertEquals(suggestedList.get(i).getPathNum(),suggestListTest.get(i).getPathNum());
            Assert.assertEquals(suggestedList.get(i).getLevel(),suggestListTest.get(i).getLevel());

        }

    }


	@Test
    public void copyConstructorTest(){
	    ConceptGraph orig = ExampleConceptGraphFactory.makeSimpleWithData();

	    ConceptGraph copy = new ConceptGraph(orig, "Copy");
	    Assert.assertNotNull(copy.findNodeById("A"));
	    Assert.assertEquals(copy.findNodeById("C"), copy.findNodeById("B").getChildren().get(0));
    }

    @Test
    public void buildGraphFromRecordAndRecordFromGraphTest(){
        ConceptGraphRecord origRecord = ExampleConceptGraphRecordFactory.makeSimple();
        ConceptGraph toTest = new ConceptGraph(origRecord);
        ConceptGraphRecord newRecord = toTest.buildNodesAndLinks();
        Assert.assertEquals(3, newRecord.getConcepts().size());
        Assert.assertEquals(3, newRecord.getLinks().size());
        Assert.assertThat(newRecord.getConcepts(), containsInAnyOrder(origRecord.getConcepts().toArray()));
        Assert.assertThat(newRecord.getLinks(), containsInAnyOrder(origRecord.getLinks().toArray()));

        origRecord = ExampleConceptGraphRecordFactory.makeSuperComplex();
        toTest = new ConceptGraph(origRecord);
        newRecord = toTest.buildNodesAndLinks();
        Assert.assertEquals(6, newRecord.getConcepts().size());
        Assert.assertEquals(11, newRecord.getLinks().size());
        Assert.assertThat(newRecord.getConcepts(), containsInAnyOrder(origRecord.getConcepts().toArray()));
        Assert.assertThat(newRecord.getLinks(), containsInAnyOrder(origRecord.getLinks().toArray()));
    }

    @Test
    public void buildGraphConstructorTest(){
        ConceptGraphRecord structure = ExampleConceptGraphRecordFactory.makeSimple();
        List<LearningObjectLinkRecord> lolinks = ExampleLearningObjectFactory.makeSimpleLOLRecords();
        List<LearningObjectResponse> responses = ExampleLearningObjectResponseFactory.makeSimpleResponses();

        ConceptGraph graph = new ConceptGraph(structure, lolinks, responses);

        Assert.assertEquals(2, graph.findNodeById("A").getChildren().size());
        Assert.assertEquals(1, graph.findNodeById("B").getChildren().size());
        Assert.assertEquals(0, graph.findNodeById("C").getChildren().size());

        Assert.assertEquals(6, graph.getLearningObjectMap().size());
        Assert.assertEquals(0, graph.findNodeById("A").getLearningObjectMap().size());
        Assert.assertEquals(2, graph.findNodeById("B").getLearningObjectMap().size());
        Assert.assertEquals(4, graph.findNodeById("C").getLearningObjectMap().size());

        Assert.assertEquals(3, graph.getLearningObjectMap().get("Q1").getResponses().size());
        Assert.assertEquals(3, graph.getLearningObjectMap().get("Q4").getResponses().size());
        Assert.assertEquals(2, graph.getLearningObjectMap().get("Q6").getResponses().size());
    }

    //TODO: Write a test where a single learning object is connected to multiple ConceptNodes, makes sure its the same learning object by address

    @Test
    public void addLearningObjectsTest(){
        ConceptGraph graph = ExampleConceptGraphFactory.makeSimpleWithData();

        Assert.assertEquals(2, graph.findNodeById("B").getLearningObjectMap().size());
        Assert.assertEquals(4, graph.findNodeById("C").getLearningObjectMap().size());
    }

    @Test
    public void addLearningObjectsAndResponsesTest(){
        ConceptGraph graph = ExampleConceptGraphFactory.makeSimpleWithData();

        Assert.assertEquals(3, graph.findNodeById("B").getLearningObjectMap().get("Q1").getResponses().size());
        Assert.assertEquals(3, graph.findNodeById("B").getLearningObjectMap().get("Q2").getResponses().size());
        Assert.assertEquals(3, graph.findNodeById("C").getLearningObjectMap().get("Q3").getResponses().size());
        Assert.assertEquals(3, graph.findNodeById("C").getLearningObjectMap().get("Q4").getResponses().size());
        Assert.assertEquals(2, graph.findNodeById("C").getLearningObjectMap().get("Q5").getResponses().size());
        Assert.assertEquals(2, graph.findNodeById("C").getLearningObjectMap().get("Q6").getResponses().size());

        Assert.assertEquals(1,     graph.findNodeById("B").getLearningObjectMap().get("Q1").calcKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(1,     graph.findNodeById("B").getLearningObjectMap().get("Q2").calcKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.667, graph.findNodeById("C").getLearningObjectMap().get("Q3").calcKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.333, graph.findNodeById("C").getLearningObjectMap().get("Q4").calcKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.5,   graph.findNodeById("C").getLearningObjectMap().get("Q5").calcKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.5,   graph.findNodeById("C").getLearningObjectMap().get("Q6").calcKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
    }

    @Test
    public void simpleCheckNumEachIDTest(){
        //checks if the tree has the right number of copies per orig node
        int numA = 0;
        int numB = 0;
        int numC = 0;

        ConceptGraphRecord treeLists = ExampleConceptGraphFactory.makeSimpleWithData().graphToTree().buildNodesAndLinks();
        for(ConceptRecord node : treeLists.getConcepts()){
			if(node.getLabel().equals("A")){
                numA++;
            }else if(node.getLabel().equals("B")){
                numB++;
            }else if(node.getLabel().equals("C")){
                numC++;
            }
        }
        Assert.assertEquals(1, numA);
        Assert.assertEquals(1, numB);
        Assert.assertEquals(2, numC);
    }

    @Test
	public void treeConversionTest(){
		checkTreeConversionByNodesAndLinksNumbers(ExampleConceptGraphFactory.makeSimpleWithData(), 3, 3, 4, 3);
		checkTreeConversionByNodesAndLinksNumbers(ExampleConceptGraphFactory.makeMedium(), 4, 5, 7, 6);
		checkTreeConversionByNodesAndLinksNumbers(ExampleConceptGraphFactory.makeComplex(), 5, 8, 13, 12);
		checkTreeConversionByNodesAndLinksNumbers(ExampleConceptGraphFactory.makeSuperComplex(), 6, 11, 24, 23);
	}

	public void checkTreeConversionByNodesAndLinksNumbers(ConceptGraph graphToTest, int expectedGraphNodeCount, int expectedGraphLinkCount, int expectedTreeNodeCount, int expectedTreeLinkCount){
		ConceptGraphRecord graphLists = graphToTest.buildNodesAndLinks();
		Assert.assertEquals(expectedGraphNodeCount, graphLists.getConcepts().size());
		Assert.assertEquals(expectedGraphLinkCount, graphLists.getLinks().size());

		ConceptGraphRecord treeLists = graphToTest.graphToTree().buildNodesAndLinks();

		Assert.assertEquals(expectedTreeNodeCount, treeLists.getConcepts().size());
		Assert.assertEquals(expectedTreeLinkCount, treeLists.getLinks().size());
	}

	//TODO: fis the original creators
    @Test
    public void treeToTreeTest(){
//        treeToTreeCheck(ExampleConceptGraphFactory.makeSimpleInputTree());
//        treeToTreeCheck(ExampleConceptGraphFactory.makeComplexInputTree());
    }

    public void treeToTreeCheck(ConceptGraph treeToTest){
        ConceptGraphRecord initialLists = treeToTest.buildNodesAndLinks();
        ConceptGraph treeFromTree = treeToTest.graphToTree();

        ConceptGraphRecord postLists = treeFromTree.buildNodesAndLinks();
        Assert.assertEquals(postLists.getConcepts().size(), initialLists.getConcepts().size());
        Assert.assertEquals(postLists.getLinks().size(), initialLists.getLinks().size());
    }

    @Test
    public void mediumReTreeTest(){
        ConceptGraph myGraph = ExampleConceptGraphFactory.makeMedium().graphToTree();
        ConceptGraph myTree = myGraph.graphToTree();
        ConceptGraph myTree2 = myTree.graphToTree();

        ConceptGraphRecord lists1 = myGraph.buildNodesAndLinks();
        ConceptGraphRecord lists2 = myTree.buildNodesAndLinks();
        ConceptGraphRecord lists3 = myTree2.buildNodesAndLinks();

        Assert.assertEquals(lists2.getConcepts().size(), lists3.getConcepts().size());

        Assert.assertEquals(lists2.getLinks().size(), lists3.getLinks().size());

        Assert.assertEquals(lists3.getLinks().size() + 1, lists3.getConcepts().size());
    }

    //TODO: Don't know how these tests are still passing, I think they should be broken, so they should be re-written to catch errors, then the format of test files should be fixed
	@Test
	public void makeConceptGraphFromFileTest(){
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//        try {
//			ConceptGraphRecord lists = mapper.readValue(new File("test/testresources/ABCSimple.json"), ConceptGraphRecordOld.class);
//			Assert.assertEquals(11, lists.getNodes().size());
//			Assert.assertEquals(11, lists.getLinks().size());
//
//			ConceptGraph myGraph = new ConceptGraph(lists);
//			ConceptGraph myTree = myGraph.graphToTree();
//
//			ConceptGraphRecordOld listsFromTree = myTree.buildNodesAndLinks();
//
//			Assert.assertEquals(16, listsFromTree.getNodes().size());
//			Assert.assertEquals(15, listsFromTree.getLinks().size());
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
	}

    //TODO: Don't know how these tests are still passing, I think they should be broken, so they should be re-written to catch errors, then the format of test files should be fixed
    @Test
	public void makeJsonFromConceptGraphTreeTest(){

//		ObjectMapper mapper = new ObjectMapper();
//		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//		String outputLocation = "out/test/sampleoutput/CarrieJsonGraph.json";
//
//        try {
//			ConceptGraphRecordOld lists = mapper.readValue(new File("test/testresources/ABCSimple.json"), ConceptGraphRecordOld.class);
//			ConceptGraph tree = new ConceptGraph(lists).graphToTree();
//
//			try{
//			mapper.writeValue(new File(outputLocation), tree.buildNodesAndLinks());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		String jsonString = "";
//		//reads json from file
//		try {
//			for (String line : Files.readAllLines(Paths.get(outputLocation))) {
//			    jsonString += line;
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		int numID = jsonString.split("id").length-1;
//		int numLink = jsonString.split("parent").length-1;
//
//		Assert.assertEquals(16, numID);
//		Assert.assertEquals(15, numLink);
	}
    @Test
    public void linkLearningObjectsTest(){
        ConceptGraph graph = ExampleConceptGraphFactory.makeSimpleWithData();
        //TODO: Did this line matter, because it can't work like that anymore...
        //graph.addLearningObjects(ExampleLearningObjectFactory.makeSimpleLearningObjectDef());

        LearningObject duplicateObject = new LearningObject("Q1");
        LearningObjectResponse duplicateResponse = new LearningObjectResponse("user1","Q1",1);

        // Tries to add Q1 but it already exists so it does not get added and returns false
        duplicateObject.addResponse(duplicateResponse);
        List<String> concepts = new ArrayList<>();
        concepts.add("B");
        concepts.add("C");
        Assert.assertEquals(-1, graph.linkLearningObjects(duplicateObject,concepts));


        // New question to be linked in to the existing graph
        LearningObject myObject = new LearningObject("Q7");
        LearningObjectResponse myResponse = new LearningObjectResponse("user1","Q7",1);

        concepts.add("D");
        myObject.addResponse(myResponse);


        // Tests adding myObject to B C and D and returns 2 because it only added to B and C but D does not exist
        Assert.assertEquals(2, graph.linkLearningObjects(myObject, concepts));
        // all info is correct
        Assert.assertEquals(myObject, graph.findNodeById("B").getLearningObjectMap().get("Q7"));
        Assert.assertEquals(myObject.getResponses().size(),graph.findNodeById("B").getLearningObjectMap().get("Q7").getResponses().size());
        Assert.assertEquals("Q7",graph.findNodeById("B").getLearningObjectMap().get("Q7").getId());
        Assert.assertEquals("user1",graph.findNodeById("B").getLearningObjectMap().get("Q7").getResponses().get(0).getUserId());
        Assert.assertEquals(3,graph.findNodeById("B").getLearningObjectMap().size());
        // If adding learning object to multiple different Concepts, it points to the same learning object
        Assert.assertEquals(true, graph.findNodeById("B").getLearningObjectMap().get("Q7") == graph.findNodeById("C").getLearningObjectMap().get("Q7"));
        Assert.assertEquals("Q7",graph.getLearningObjectMap().get("Q7").getId());
        // Makes sure the new question was only added once to Learning Object map (previously had 6 questions, now 7)
        Assert.assertEquals(7,graph.getLearningObjectMap().size());
    }

    @Test
    public void addLearningObjectsFromLearningObjectLinkRecords(){
        //Creating graph
        ConceptGraph graph = ExampleConceptGraphFactory.makeSimpleWithData();

        List<LearningObjectResponse> responses = new ArrayList<>();

        responses.add( new LearningObjectResponse("user1","Q1",1));
        responses.add( new LearningObjectResponse("user1","Q7",1));

        //Creating learningObjectLinkedRecord list
        List<LearningObjectLinkRecord> learningObjectLinkRecords = new ArrayList<>();

        List<String> concepts = new ArrayList<>();
        concepts.add("B");
        concepts.add("C");
        LearningObjectLinkRecord question7Record = new LearningObjectLinkRecord("Q7",concepts);
        learningObjectLinkRecords.add(question7Record);

        graph.addLearningObjectsFromLearningObjectLinkRecords(learningObjectLinkRecords);
        graph.addLearningObjectResponses(responses);

        // all info is correct
        Assert.assertEquals(1,graph.findNodeById("B").getLearningObjectMap().get("Q7").getResponses().size());
        Assert.assertEquals("Q7",graph.findNodeById("B").getLearningObjectMap().get("Q7").getId());
        Assert.assertEquals("user1",graph.findNodeById("B").getLearningObjectMap().get("Q7").getResponses().get(0).getUserId());
        Assert.assertEquals(3,graph.findNodeById("B").getLearningObjectMap().size());
        // If adding learning object to multiple different Concepts, it points to the same learning object
        Assert.assertEquals(true, graph.findNodeById("B").getLearningObjectMap().get("Q7") == graph.findNodeById("C").getLearningObjectMap().get("Q7"));
        Assert.assertEquals("Q7",graph.getLearningObjectMap().get("Q7").getId());
        // Makes sure the new question was only added once to Learning Object map (previously had 6 questions, now 7)
        Assert.assertEquals(7,graph.getLearningObjectMap().size());


    }

    @Test
    public void calcKnowledgeEstimateMoreComplexTest() {
        ConceptGraph orig = ExampleConceptGraphFactory.willExampleConceptGraphTestOneStudent();

        Assert.assertEquals(0.806, orig.findNodeById("Boolean").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.783090, orig.findNodeById("Boolean Expressions").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.746, orig.findNodeById("If Statement").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);

        Assert.assertEquals(0.722, orig.findNodeById("While Loop").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.85, orig.findNodeById("Counting").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.7666, orig.findNodeById("For Loop").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);

    }


	@Test
	public void calcKnowledgeEstimateTest(){
        ConceptGraph graph = ExampleConceptGraphFactory.makeSimpleWithData();
        graph.calcDataImportance();
        graph.calcKnowledgeEstimates();

        Assert.assertEquals(0.5, graph.findNodeById("C").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.688, graph.findNodeById("B").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.615, graph.findNodeById("A").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
    }

    //TODO: Adapt to new graph creation once data is reinstated
//	@Test
//	public void calcActualCompTest() {
//        //create your selectionGraph and add responses to it
//
//		float delta = (float) .01;
//
//		//Checks children on If Statement to make sure their actualComps are correct
//		//(gotten from visualized graph)
//		//file:///Users/willsuchanek/git/metafora-project/MonitorInterventionMetafora/war/TreeDisplay/index.html
//		Assert.assertEquals(0, selectionListMap.get("Boolean").getKnowledgeEstimate(),delta);
//		Assert.assertEquals(1, selectionListMap.get("test_question6_4_2").getKnowledgeEstimate(),delta);
//		Assert.assertEquals(1, selectionListMap.get("test_question6_4_1").getKnowledgeEstimate(),delta);
//
//
//		//Calculates the actualComp for If statements children
//		float sumOfChildren = 0;
//		int childCounter = 0;
//		for(ConceptNode node :selectionListMap.get("If Statement").getChildren()){
//			sumOfChildren+=node.getKnowledgeEstimate();
//			childCounter++;
//		}
//		//makes sure the calculated number is equal to the number stored in If Statements
//		Assert.assertEquals(sumOfChildren/childCounter, selectionListMap.get("If Statement").getKnowledgeEstimate(),delta);
//
//		//Checks children on Control to make sure their actualComps are correct
//		//(gotten from visualized graph)
//		//file:///Users/willsuchanek/git/metafora-project/MonitorInterventionMetafora/war/TreeDisplay/index.html
//		Assert.assertEquals(.67, selectionListMap.get("If Statement").getKnowledgeEstimate(),delta);
//		Assert.assertEquals(0, selectionListMap.get("Loops").getKnowledgeEstimate(),delta);
//
//		//Calculates the actualComp for Control children
//		sumOfChildren = 0;
//		childCounter = 0;
//		for(ConceptNode node :selectionListMap.get("Control").getChildren()){
//			sumOfChildren+=node.getKnowledgeEstimate();
//			childCounter++;
//		}
//
//		//makes sure the calculated number is equal to the number stored in If Statements
//		Assert.assertEquals(sumOfChildren/childCounter, selectionListMap.get("Control").getKnowledgeEstimate(),delta);
//	}

}

	
