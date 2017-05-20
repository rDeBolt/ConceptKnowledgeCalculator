package edu.ithaca.dragonlab.ckc.conceptgraph;

import edu.ithaca.dragonlab.ckc.io.ConceptGraphRecord;
import edu.ithaca.dragonlab.ckc.learningobject.ExampleLearningObjectLinkRecordFactory;
import edu.ithaca.dragonlab.ckc.learningobject.ExampleLearningObjectResponseFactory;
import edu.ithaca.dragonlab.ckc.util.DataUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class CohortConceptGraphsTest {

	static Logger logger = LogManager.getLogger(ConceptGraphTest.class);

	@Test
	public void userCountTest(){
		ConceptGraph graph = ExampleConceptGraphFactory.makeSimpleStructure();
		CohortConceptGraphs group = new CohortConceptGraphs(graph, ExampleLearningObjectResponseFactory.makeSimpleResponses());
		Assert.assertEquals(3,group.getUserCount());
	}

	@Test
	public void addSummariesTest(){
        ConceptGraph graph = new ConceptGraph(ExampleConceptGraphRecordFactory.makeSimple(), ExampleLearningObjectLinkRecordFactory.makeSimpleLOLRecords());
        CohortConceptGraphs group = new CohortConceptGraphs(graph, ExampleLearningObjectResponseFactory.makeSimpleResponses());

        Assert.assertEquals(6,group.getAvgGraph().getLearningObjectMap().size());
		Assert.assertEquals(6,group.getUserGraph("student1").getLearningObjectMap().size());
        Assert.assertEquals(6,group.getUserGraph("student2").getLearningObjectMap().size());
        Assert.assertEquals(6,group.getUserGraph("student3").getLearningObjectMap().size());

        Assert.assertEquals(3,group.getAvgGraph().getLearningObjectMap().get("Q1").getResponses().size());
        Assert.assertEquals(2,group.getAvgGraph().getLearningObjectMap().get("Q5").getResponses().size());

        Assert.assertEquals(1,group.getUserGraph("student1").getLearningObjectMap().get("Q1").getResponses().size());
        Assert.assertEquals(0,group.getUserGraph("student3").getLearningObjectMap().get("Q5").getResponses().size());
    }

    @Test
    public void calcKnowledgeEstimateTest() {
        ConceptGraph graph = ExampleConceptGraphFactory.makeSimpleCompleteWithData();
        CohortConceptGraphs group = new CohortConceptGraphs(graph, ExampleLearningObjectResponseFactory.makeSimpleResponses());

        Assert.assertEquals(0.5, group.getAvgGraph().findNodeById("C").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.6875, group.getAvgGraph().findNodeById("B").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.6158, group.getAvgGraph().findNodeById("A").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);

        Assert.assertEquals(1, group.getUserGraph("student1").findNodeById("A").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(1, group.getUserGraph("student1").findNodeById("B").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(1, group.getUserGraph("student1").findNodeById("C").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);

        Assert.assertEquals(0.4, group.getUserGraph("student2").findNodeById("A").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.5, group.getUserGraph("student2").findNodeById("B").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.25, group.getUserGraph("student2").findNodeById("C").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);

        Assert.assertEquals(0.333, group.getUserGraph("student3").findNodeById("A").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.5, group.getUserGraph("student3").findNodeById("B").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0, group.getUserGraph("student3").findNodeById("C").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
    }

	@Test
	public void calcDistFromAvgTest(){
        ConceptGraph graph = ExampleConceptGraphFactory.makeSimpleCompleteWithData();
        CohortConceptGraphs group = new CohortConceptGraphs(graph, ExampleLearningObjectResponseFactory.makeSimpleResponses());

		ConceptGraph user = group.getUserGraph("student1");
		Assert.assertEquals(0.3842, user.findNodeById("A").getKnowledgeDistanceFromAvg(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.3125, user.findNodeById("B").getKnowledgeDistanceFromAvg(),DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.5, user.findNodeById("C").getKnowledgeDistanceFromAvg(),DataUtil.OK_FLOAT_MARGIN);

        user = group.getUserGraph("student2");
        Assert.assertEquals(-0.2158, user.findNodeById("A").getKnowledgeDistanceFromAvg(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(-0.1875, user.findNodeById("B").getKnowledgeDistanceFromAvg(),DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(-0.25, user.findNodeById("C").getKnowledgeDistanceFromAvg(),DataUtil.OK_FLOAT_MARGIN);

        user = group.getUserGraph("student3");
        Assert.assertEquals(-0.2825, user.findNodeById("A").getKnowledgeDistanceFromAvg(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(-0.1875, user.findNodeById("B").getKnowledgeDistanceFromAvg(),DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(-0.5, user.findNodeById("C").getKnowledgeDistanceFromAvg(),DataUtil.OK_FLOAT_MARGIN);
	}
}



	