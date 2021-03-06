package edu.ithaca.dragonlab.ckc.suggester.GroupSuggester;

import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptNode;
import edu.ithaca.dragonlab.ckc.suggester.LearningObjectSuggester;
import edu.ithaca.dragonlab.ckc.suggester.LearningObjectSuggestion;
import edu.ithaca.dragonlab.ckc.suggester.SuggestionResource;
import java.util.*;

/**
 * Created by mkimmitchell on 8/10/17.
 */


public class ResourceNCubeGroupSuggester extends GroupSuggester {

    public String getSimilarResourceLevel2 (SuggestionResource s1, SuggestionResource s2){

        List<LearningObjectSuggestion> incom1 = s1.incompleteList;

        List<LearningObjectSuggestion> incom2 = s2.incompleteList;

        List<LearningObjectSuggestion> wrong1 = s1.wrongList;
        List<LearningObjectSuggestion> wrong2 = s2.wrongList;

        /*
        student1   student2
        incomplete  ==true  2
        wrong == true       3
        incomplete wrong    4
        wrong incomplete    4
        empty empty         1
         */


        if(incom1.size()> 0 && incom2.size()>0 &&incom1.get(0).getId().equals(incom2.get(0).getId())){
            return 2 + "\n" + incom1.get(0).getId();

        } else if(wrong1.size()>0 && wrong2.size()>0&&wrong1.get(0).getId().equals(wrong2.get(0).getId())) {
            return 3 + "\n" + wrong1.get(0).getId();

        }else if(incom1.size()>0 && wrong2.size()>0 &&incom1.get(0).getId().equals(wrong2.get(0).getId())){
            return 4 + "\n"+ incom1.get(0).getId();

        } else if (wrong1.size()>0 && incom2.size()>0&&wrong1.get(0).getId().equals(incom2.get(0).getId())){
            return 4+ "\n" + wrong1.get(0).getId();
        } else if (wrong1.size()== 0&& incom1.size()==0 && wrong2.size()==0 && incom2.size()==0){
            return 1+ "\n"+ "something challenging";

        } else{
            return 0+"\n";

        }
    }


    public String getSimilarResourceLevel3 (SuggestionResource s1, SuggestionResource s2, SuggestionResource s3){

        List<LearningObjectSuggestion> incom1 = s1.incompleteList;
        List<LearningObjectSuggestion> incom2 = s2.incompleteList;
        List<LearningObjectSuggestion> incom3 = s3.incompleteList;

        List<LearningObjectSuggestion> wrong1 = s1.wrongList;
        List<LearningObjectSuggestion> wrong2 = s2.wrongList;
        List<LearningObjectSuggestion> wrong3 = s3.wrongList;

        /*
        student1   student2
        incomplete  ==true  2
        wrong == true       3
        incomplete wrong    4
        wrong incomplete    4
        empty empty         1
         */


        if(incom1.size()> 0 && incom2.size()>0 && incom3.size()>0 && incom1.get(0).getId().equals(incom2.get(0).getId()) && incom2.get(0).getId().equals(incom3.get(0).getId()) && incom1.get(0).getId().equals(incom3.get(0).getId())) {
            return 2 + "\n" + incom1.get(0).getId();

        }else if(wrong1.size()> 0 && wrong1.size()>0 && wrong1.size()>0 && wrong1.get(0).getId().equals(wrong1.get(0).getId()) && wrong1.get(0).getId().equals(wrong1.get(0).getId()) && wrong1.get(0).getId().equals(wrong1.get(0).getId())){
            return 3 + "\n" + wrong1.get(0).getId();

        }else if(incom1.size()>0 && wrong2.size()>0 && incom3.size()> 0 &&incom1.get(0).getId().equals(wrong2.get(0).getId()) && incom1.get(0).getId().equals(incom3.get(0).getId()) && wrong2.get(0).getId().equals(incom3.get(0).getId())){
            return 4 + "\n"+ incom1.get(0).getId();

        } else if (wrong1.size()>0 && wrong2.size()>0  && incom3.size()>0 && wrong1.get(0).getId().equals(wrong2.get(0).getId()) && wrong1.get(0).getId().equals(incom3.get(0).getId()) && wrong2.get(0).getId().equals(incom2.get(0).getId())){
            return 4+ "\n" + wrong1.get(0).getId();
        } else if (wrong1.size()== 0&& incom1.size()==0 && wrong2.size()==0 && incom2.size()==0 && incom3.size()==0 && wrong3.size()==0){
            return 1+ "\n"+ "something challenging";

        } else{
            return 0+"\n";

        }
    }

    public void makeGroups(List<List<String>> num, List<String> userTemp, List<List<String>> groupings, int choice){

        /*
        go through the groupings,
        see if the students in each possibly group has been already compared

        if not, add to subgroup called groupRe, add subgroup to master list called groupings

        remove the students from the copy of the userList

         */
        for(List<String> possiblePairing: num) {
            boolean flag = false;

            for(int i=0; i<possiblePairing.size()-choice; i++){

                if(choice==2){
                    String firstName = possiblePairing.get(i);
                    String secondName = possiblePairing.get(i+1);

                    if(!userTemp.contains(firstName) || !userTemp.contains(secondName) ){
                        flag=true;
                        break;
                    }
                }
                if(choice ==3){
                    String firstName = possiblePairing.get(i);
                    String secondName = possiblePairing.get(i+1);
                    String thirdname = possiblePairing.get(i+2);


                    if(!userTemp.contains(firstName) || !userTemp.contains(secondName) || !userTemp.contains(thirdname)){
                        flag=true;
                        break;
                    }
                }



            }


            if(!flag){
                List<String> groupRe = new ArrayList<>();
                groupRe.addAll(possiblePairing);
                groupings.add(groupRe);

                userTemp.removeAll(groupRe);

            }

        }
    }


    @Override
    public List<List<String>> suggestGroup(CohortConceptGraphs graphs, int choice) {

        /*
        create map of student IDs and their suggestionResource called user suggestion map

        create four lists of lists of strings

        nest for loop where each loop goes through the user suggestion map
        if the two users are not equal and have not been compared before then call getSimilarResourceLevel

        a int will be returned that matched one of the four lists already created. Add to corresponding list


        if groups of three,
        triple nested loop

         */

        Map <String, ConceptGraph> getMaps = getUserMap(graphs);
        Map<String, SuggestionResource> userSuggestionMap = new HashMap<>();


        for(ConceptGraph graph: getMaps.values()){
            List<ConceptNode> nodeList = LearningObjectSuggester.conceptsToWorkOn(graph);
            userSuggestionMap.put(graph.getName(),new SuggestionResource(graph, nodeList) );
        }

        List<List<String>> one = new ArrayList<>();
        List<List<String>> two = new ArrayList<>();
        List<List<String>> three = new ArrayList<>();
        List<List<String>> four = new ArrayList<>();

        List<String> repeats = new ArrayList<>();


        if(choice==2){
            for(String name: userSuggestionMap.keySet()){
                SuggestionResource sugres = userSuggestionMap.get(name);

                for(String name2: userSuggestionMap.keySet()){
                    SuggestionResource sugres2 = userSuggestionMap.get(name2);

                    if(!name.equals(name2) && !(repeats.contains(name+"+"+name2) || repeats.contains(name2+"+"+name))){

                        String ex = getSimilarResourceLevel2(sugres, sugres2);

                        List<String> groups = new ArrayList<>();

                        groups.add(name);
                        groups.add(name2);

                        if(ex.substring(0,1).equals("1")){
                            groups.add(ex.substring(2,ex.length()));
                            one.add(groups);
                        }else if(ex.substring(0,1).equals("2")){
                            groups.add(ex.substring(2,ex.length()));
                            two.add(groups);
                        }else if(ex.substring(0,1).equals("3")){
                            groups.add(ex.substring(2,ex.length()));
                            three.add(groups);
                        }else if (ex.substring(0,1).equals("4")){
                            groups.add(ex.substring(2,ex.length()));
                            four.add(groups);
                        }

                        repeats.add(name+"+"+name2);

                    }
                }
            }
        }
        if(choice==3){
            for(String name: userSuggestionMap.keySet()){
                SuggestionResource sugres = userSuggestionMap.get(name);

                for(String name2: userSuggestionMap.keySet()) {
                    SuggestionResource sugres2 = userSuggestionMap.get(name2);

                    for(String name3: userSuggestionMap.keySet()) {
                        SuggestionResource sugres3 = userSuggestionMap.get(name3);

                        boolean isRepeated = false;
                        if(!(repeats.contains(name + "+" + name2 +"+"+name3) || repeats.contains(name3 + "+" + name +"+"+name2)|| repeats.contains(name2+ "+" + name3 +"+"+name)|| repeats.contains(name + "+" + name3 +"+"+name2) || repeats.contains(name2 + "+" + name +"+"+name3) || repeats.contains(name3 + "+" + name2 +"+"+name))){
                            isRepeated=true;
                        }

                        if (!name.equals(name2) && !(name.equals(name3)) && !(name2.equals(name3))  && isRepeated) {

                            String ex = getSimilarResourceLevel3(sugres, sugres2, sugres3);

                            List<String> groups = new ArrayList<>();

                            groups.add(name);
                            groups.add(name2);
                            groups.add(name3);

                            if (ex.substring(0, 1).equals("1")) {
                                groups.add(ex.substring(2, ex.length()));
                                one.add(groups);
                            } else if (ex.substring(0, 1).equals("2")) {
                                groups.add(ex.substring(2, ex.length()));
                                two.add(groups);
                            } else if (ex.substring(0, 1).equals("3")) {
                                groups.add(ex.substring(2, ex.length()));
                                three.add(groups);
                            } else if (ex.substring(0, 1).equals("4")) {
                                groups.add(ex.substring(2, ex.length()));
                                four.add(groups);
                            }

                            repeats.add(name + "+" + name2+ "+"+name3);

                        }
                    }
                }
            }

        }


        /*

        in order, makeGroups is called on each of the four group List.
         */

        List<String> userTemp = new ArrayList<>();
        userTemp.addAll(userSuggestionMap.keySet());


        List<List<String>> groupings = new ArrayList<>();

        if(one.size()>0){
            makeGroups(one, userTemp, groupings, choice);

        }
        if(two.size()>0){
            makeGroups(two, userTemp, groupings, choice);

        }
        if(three.size()>0){
            makeGroups(three, userTemp, groupings, choice);

        }
        if(four.size()>0){
            makeGroups(four, userTemp, groupings, choice);

        }

        /*
        for the left over users

                The number of student will evenly divide into the group size
        while there are users unpaired
         get the first users in the shuffled list, choice number of times
        put the users in a sublist called group
        remove users from copy of user list
        put the group in the master list called groupings

        or

        there aren't as many students as there is the choice size
        add them to the sublist, and add sublist to main list

        or

        if there are most students than choice size, but not all of them will fit evenly into a group (there will be a group that's not completly filled up)
        the amount of full groups that can be created = userList.size/choice
        create for loop with the amount of full groups
        get the first users in the shuffled list, choice number of times
        remove from copy of userlist
        create subgroup, add the users, add subgroup to master list called groupings
        for left over students in userList, add in subgroup, add subgroup to master list
         */
        if(userTemp.size() % choice == 0 && userTemp.size() >= choice){

            int whileNum = userTemp.size();
            while(whileNum>0) {

                List<String> group = new ArrayList<>();

                int choices =0;
                choices = choice;
                while (choices>0){
                    group.add(userTemp.get(choices-1));
                    userTemp.remove(userTemp.get(choices-1));
                    choices--;
                }

                group.add("Random pairing");
                groupings.add(group);

                whileNum= whileNum-choice;
            }


        }else if (userTemp.size() < choice && userTemp.size() % choice != 0){


            List<String> group = new ArrayList<>();
            group.addAll(userTemp);
            group.add("No other students");

            groupings.add(group);


        } else {


            for(int i = userTemp.size()/choice; i> 0 ; i--){

                List<String> group = new ArrayList<>();

                int choices =0;
                choices = choice;
                while (choices>0){
                    group.add(userTemp.get(choices-1));
                    userTemp.remove(userTemp.get(choices-1));
                    choices--;
                }

                group.add("Random pairing");
                groupings.add(group);

            }

            //for the left over students
            List<String> group = new ArrayList<>();
            group.addAll(userTemp);
            group.add("No other students");

            groupings.add(group);


        }

        return groupings;
    }


}
