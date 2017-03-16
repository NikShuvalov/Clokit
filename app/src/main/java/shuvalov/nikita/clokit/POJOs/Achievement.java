package shuvalov.nikita.clokit.pojos;

import android.support.annotation.Nullable;

import shuvalov.nikita.clokit.AppConstants;

/**
 * Created by NikitaShuvalov on 3/3/17.
 */

//ToDo: Idea? Might be best to have multiple types of Achievements: Repeatable, Temporal, Singular.
public class Achievement {


    private int mImageReference, mId, mTimeOrRepeats;
    private String mName, mGoalName;

    public Achievement(int imageReference, int id, @Nullable String goalName) {
        mImageReference = imageReference;
        mGoalName = goalName;
        mId = id;
        identifyAchievement(id);
        mTimeOrRepeats = 0;
    }

    private void identifyAchievement(int id){
        switch(id){
            case AppConstants.ACHIEVEMENT_PART_TIME_ID:
                mName = "Part-Time";
                break;
            case AppConstants.ACHIEVEMENT_FULL_TIME_ID:
                mName = "Full-Time";
                break;
            case AppConstants.ACHIEVEMENT_OVER_TIME_ID:
                mName = "Over-Time";
                break;
            case AppConstants.ACHIEVEMENT_DOUBLE_TIME_ID:
                mName = "Double-Time";
                break;
            case AppConstants.ACHIEVEMENT_CENT_OPUS_ID:
                mName = "Iron Will or Impending Deadline?";
                break;
            case AppConstants.ACHIEVEMENT_HERBALIST_ID:
                mName = "Herbalist";
                break;
            case AppConstants.ACHIEVEMENT_KEVIN_MALONE_ID:
                mName = "Hehehe";
                break;
            case AppConstants.ACHIEVEMENT_ELITIST_ID:
                mName = "Elitist";
                break;
            case AppConstants.ACHIEVEMENT_FORGETFUL_ID:
                mName = "Forgetful";
                break;
            case AppConstants.ACHIEVEMENT_MINUTEMAN_ID:
                mName = "Minuteman";
                break;
            case AppConstants.ACHIEVEMENT_THEBEAST_ID:
                mName = "The Beast";
                break;
            case AppConstants.ACHIEVEMENT_INITIATE_ID:
                mName = "Initiate";
                break;
            case AppConstants.ACHIEVEMENT_BEGINNER_ID:
                mName = "Beginner";
                break;
            case AppConstants.ACHIEVEMENT_NOVICE_ID:
                mName = "Novice";
                break;
            case AppConstants.ACHIEVEMENT_APPRENTICE_ID:
                mName = "Apprentice";
                break;
            case AppConstants.ACHIEVEMENT_JOURNEYMAN_ID:
                mName = "Journeyman";
                break;
            case AppConstants.ACHIEVEMENT_EXPERT_ID:
                mName = "Expert";
                break;
            case AppConstants.ACHIEVEMENT_PROFESSIONAL_ID:
                mName = "Professional";
                break;
            case AppConstants.ACHIEVEMENT_MASTER_ID:
                mName = "Master";
                break;
            case AppConstants.ACHIEVEMENT_GRANDMASTER_ID:
                mName= "Grandmaster";
                break;
            case AppConstants.ACHIEVEMENT_TRANSCENDED_ID:
                mName = "Transcendent";
                break;
            case AppConstants.ACHIEVEMENT_GOD_ID:
                mName = "God";
                break;
        }
    }


    public int getImageReference() {
        return mImageReference;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public int getTimeOrRepeats() {
        return mTimeOrRepeats;
    }

    public void setTimeOrRepeats(int timeOrRepeats) {
        mTimeOrRepeats = timeOrRepeats;
    }

    public String getGoalName() {
        return mGoalName;
    }
}
