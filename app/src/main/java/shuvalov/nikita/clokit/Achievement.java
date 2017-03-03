package shuvalov.nikita.clokit;

/**
 * Created by NikitaShuvalov on 3/3/17.
 */

public class Achievement {
    private int mImageReference, mId;
    private String mName;

    public Achievement(int imageReference, int id, String name) {
        mImageReference = imageReference;
        mId = id;
        mName = name;
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
}
