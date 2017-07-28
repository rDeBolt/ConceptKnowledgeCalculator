package edu.ithaca.dragonlab.ckc.suggester;

import java.util.Comparator;

/**
 * Created by Mia Kimmich Mitchell on 4/10/2017.
 */
public class LearningObjectSuggestionComparator implements Comparator<LearningObjectSuggestion> {

    /**
     * compares first by level (will be in order of right, wrong, incomplete)
     * second by path number (higher path numbers first)
     * lastly by directConceptLinkCount ( the lower link counts are first)
     * @param one
     * @param two
     * @return int
     */
    public int compare(LearningObjectSuggestion one, LearningObjectSuggestion two){

        if (one.getLevel().compareTo(two.getLevel()) == 0) {
            if (one.getPathNum() < two.getPathNum()) {
                return 1;
            } else if (one.getPathNum() > two.getPathNum()) {
                return -1;

            } else {

                if(one.getDirectConceptLinkCount()< two.getDirectConceptLinkCount()){
                    return -1;
                }else if(one.getDirectConceptLinkCount() > two.getDirectConceptLinkCount()){
                    return 1;
                }else{
                    return 0;

                }
            }
        }


        return one.getLevel().compareTo(two.getLevel());

    }

}
