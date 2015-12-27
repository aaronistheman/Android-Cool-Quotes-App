package enterprises.wayne.coolquotes;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity {

    private SoundPool mSoundPool;
    private ArrayList<String> mImageNames;
    private HashMap<String, Integer> mImageIdMap;
    private HashMap<String, Integer> mSoundMap;
    private int mCurrentIndex = 0;

    // UI components
    private ImageButton mSpeaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        setUpUIComponents();

        // load the keys for the maps
        loadImageNames();

        loadImageIdMap();

        mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        loadSoundMap();

        updateSpeaker();
    }

    /**
     * @post components have been assigned to appropriate member variables
     */
    private void setUpUIComponents() {
        mSpeaker = (ImageButton) findViewById(R.id.ibSpeaker);
    }

    /**
     * @post mImageNames has been filled with appropriate data
     */
    private void loadImageNames() {
        mImageNames = new ArrayList<String>();

        mImageNames.add("robot_krabs");
        mImageNames.add("jackal");
        mImageNames.add("yup");
        mImageNames.add("nope");
        mImageNames.add("fallon");

        /**
         * These speakers' corresponding quotes contain explicit language.
         * Because I don't want to risk getting into trouble, their
         * quotes are not included in the Git repository (and are
         * ignored by use of .gitignore file and "explicit_" prefix).
         */
        mImageNames.add("explicit_jeselnik");
        mImageNames.add("explicit_randy");
        mImageNames.add("explicit_got_you_there");
    }

    /**
     * @pre mImageNames has been filled; for each name in mImageNames,
     * there is an image with the same name (ignoring the file extension)
     * in the "drawable" folder
     * @post mImageIdMap has been filled with pairs, each pair consisting
     * of an image name and the "resID" of the image
     */
    private void loadImageIdMap() {
        mImageIdMap = new HashMap<String, Integer>();
        for (String imageName : mImageNames) {
            int resID = getResources().
                    getIdentifier(imageName, "drawable", getPackageName());
            mImageIdMap.put(imageName, resID);
        }
    }

    /**
     * @pre mImageNames has been filled; for each name in mImageNames,
     * there is a sound effect with the same name (ending in ".ogg")
     * in the "assets" folder; mSoundPool has been initialized
     * @post mSoundMap has been filled with pairs, each pair consisting
     * of an image name and the sound effect (in a form that mSoundPool
     * can play)
     */
    private void loadSoundMap() {
        mSoundMap = new HashMap<String, Integer>();
        AssetManager assetManager = getAssets();
        AssetFileDescriptor descriptor;

        try {
            for (String imageName : mImageNames) {
                descriptor = assetManager.openFd(imageName + ".ogg");
                mSoundMap.put(imageName, mSoundPool.load(descriptor, 0));
            }
        }
        catch (IOException e) {
            //Print an error message to the console
            Log.e("error", "failed to load sound files");
        }
    }

    /**
     * @param view
     * @post current speaker has been switched to one before it
     */
    public void onPreviousClick(View view) {
        --mCurrentIndex;
        if (mCurrentIndex < 0)
            mCurrentIndex = mImageNames.size() - 1;
        updateSpeaker();
    }

    /**
     * @param view
     * @post current speaker has been switched to one after it
     */
    public void onNextClick(View view) {
        ++mCurrentIndex;
        if (mCurrentIndex > mImageNames.size() - 1)
            mCurrentIndex = 0;
        updateSpeaker();
    }

    /**
     * @param view
     * @pre mCurrentIndex is correct
     * @post appropriate sound effect (of the speaker shown in the image
     * button) has been played
     */
    public void onImageClick(View view) {
        mSoundPool.play(mSoundMap.get(
                mImageNames.get(mCurrentIndex)), 1, 1, 0, 0, 1);
    }

    /**
     * @pre mCurrentIndex is correct
     * @post the image on the speaker image button has been updated
     */
    private void updateSpeaker() {
        mSpeaker.setImageResource(mImageIdMap.get(
                mImageNames.get(mCurrentIndex)));
    }
}
