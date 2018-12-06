package demoapp.ece558.ryan.demoapp;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class WordListFragment extends Fragment {

    private static final String TAG = "WordListFragment";
    private static int INTERVAL_CHECK_WORD_CHANGE = 500;

    private Handler mPollHandler;

    private DatabaseReference mDatabaseReference;

    // data member for the fragment recycler view
    private RecyclerView mWordRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private WordAdapter mAdapter;

    private WordManager mWordManager;

    OnViewClickListener mCallback;

    // method to attach to each view that allows us to capture the onClick event
    // invoked in hosting activity when the fragment is attached
    // sets the fragment instance callback
    public void setOnViewClickListener (Activity activity) {
        mCallback = (OnViewClickListener) activity;
    }

    // create an interface to pass information back to the activity
    // interface method is instantiated in the hosting activity
    public interface OnViewClickListener {
        void onViewSelected(int wordPosition);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPollHandler.removeCallbacks(mPollWordAdd);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPollHandler = new Handler();
        mPollHandler.postDelayed(mPollWordAdd, INTERVAL_CHECK_WORD_CHANGE);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    // enable the fragment to use the layout file and to find the RecyclerView in the layout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // get reference for recycler view layout
        View view = inflater.inflate(R.layout.fragment_word_list, container, false);

        // get reference for the recyclerview
        mWordRecyclerView = (RecyclerView) view.findViewById(R.id.word_recycler_view);

        // create and set the layout manager
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mWordRecyclerView.setLayoutManager(mLayoutManager);

        // use this setting to improve performance if changes in content do not change the layout
        mWordRecyclerView.setHasFixedSize(true);

        // get the instance of word manager and pass the list of words to the adapter
        mWordManager = WordManager.getInstance();
        // create the word adapter and link it to the recycler view
        mAdapter = new WordAdapter(mWordManager.getWordList());
        mWordRecyclerView.setAdapter(mAdapter);

        return view;
    }

    // Runnable periodically checks the view model to see if a new word was added by the control fragment
    private Runnable mPollWordAdd = new Runnable() {
        @Override
        public void run () {
            String wordFromText;
            // store the word in the view model to update the recycler view fragment
            WordViewModel model = ViewModelProviders.of(getActivity()).get(WordViewModel.class);
            if (model == null)
                return;

            wordFromText = model.getWordFromText();
            if (wordFromText != null){
                //Toast.makeText(getActivity(), "New word! " + wordFromText, Toast.LENGTH_SHORT).show();
                //mWordManager.addWord(0, wordFromText);
                //mAdapter.notifyItemInserted(0);
                mWordManager.addWord(0, wordFromText);
            }
            mPollHandler.postDelayed(mPollWordAdd, INTERVAL_CHECK_WORD_CHANGE);
        }
    };

    //--------------------------------------------------------------------------------------

    public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordHolder> {

        private List<Word> mWords;

        // Constructor for the Adapter
        public WordAdapter(List<Word> words) {
           mWords = mWordManager.getWordList();
            //mWords = words;
        }


        /*  The views in the list are represented by ViewHolder objects.
            Provide a reference to the views for each data item.
            Complex data items may need more than one view per item, and
            you provide access to all the views for a data item in a view holder.
        */
        public class WordHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            // Each ViewHolder is made of a text string for the word and boxes for the colors
            public TextView mTextView;

            // Constructor for the RecyclerView ViewHolder extension
            // Pass the ViewHolder instance of the view layout
            public WordHolder (View v) {
                super(v);
                // we can deconstruct the view layout here
                mTextView = (TextView) v.findViewById(R.id.word_title);
                v.setOnClickListener(this);
            }

            //  Each ViewHold implements an onClickListener that tracks when to go to the word fragment
            @Override
            public void onClick (View view) {
                // get the selected adapter position and the UUID of the word at that position
                // then get the word with the matching UUID from the WordManager
                int color = 0;
                Word word = mWordManager.getWord(mWords.get(getAdapterPosition()).getId());

                // invoke the callback method that an onClickView has happened and pass the word back
                mCallback.onViewSelected(mWords.indexOf(word));
            }
        }

        // Create new views (invoked by the layout manager)
        @Override
        public WordAdapter.WordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view by getting the view layout file and passing it to the view holder
            View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_word, parent, false);
            // each view is a row whose format is specified in the list_item_word layout
            WordHolder vh = new WordHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(WordHolder holder, int position) {
            // get element from your dataset at this position and replace the contents of the view
            holder.mTextView.setText(mWords.get(position).getWord());
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mWordManager.getWordList().size();
            //return mWords.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void addItem(int position) {

        }

    }



}
