package com.bizconnectivity.gino.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.adapters.SearchResultsListAdapter;
import com.bizconnectivity.gino.models.DealList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static android.app.Activity.RESULT_OK;

public class SearchFragment extends Fragment implements SearchResultsListAdapter.AdapterCallBack{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.floating_search_view)
    FloatingSearchView mSearchView;

    @BindView(R.id.search_results_list)
    RecyclerView mRecyclerView;

    public static final long FIND_SUGGESTION_SIMULATED_DELAY = 250;
    private boolean mIsDarkSearchTheme = false;
    private String mLastQuery = "";
    private static final int SPEECH_REQUEST_CODE = 0;
    private SearchResultsListAdapter mSearchResultsAdapter;
    Realm realm;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // Layout Binding
        ButterKnife.bind(this, view);

        // Action Bar
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

        // Initial Realm
        realm = Realm.getDefaultInstance();

        // Setup Search Bar
        setupSearchBar();

        // Setup RecycleView
        List<DealList> dealLists = new ArrayList<>();
        searchRecyclerView(dealLists);
    }

    private void searchRecyclerView(List<DealList> dealLists) {

        mSearchResultsAdapter = new SearchResultsListAdapter(getContext(), dealLists, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mSearchResultsAdapter);
    }

    private void setupSearchBar() {

//        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
//            @Override
//            public void onSearchTextChanged(String oldQuery, String newQuery) {
//
//                if (!oldQuery.equals("") && newQuery.equals("")) {
//
//                    mSearchView.clearSuggestions();
//
//                } else {
//
//                    //this shows the top left circular progress
//                    //you can call it where ever you want, but
//                    //it makes sense to do it when loading something in
//                    //the background.
//                    mSearchView.showProgress();
//
//                    //simulates a query call to a data source
//                    //with a new query.
//                    DataHelper.findSuggestions(getActivity(), newQuery, 5,
//                            FIND_SUGGESTION_SIMULATED_DELAY, new DataHelper.OnFindSuggestionsListener() {
//
//                                @Override
//                                public void onResults(List<DealSearchSuggestion> results) {
//
//                                    //this will swap the data and
//                                    //render the collapse/expand animations as necessary
//                                    mSearchView.swapSuggestions(results);
//
//                                    //let the users know that the background
//                                    //process has completed
//                                    mSearchView.hideProgress();
//                                }
//                    });
//                }
//            }
//        });

//        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
//            @Override
//            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
//
//                DealSearchSuggestion colorSuggestion = (DealSearchSuggestion) searchSuggestion;
//                DataHelper.findColors(getContext(), colorSuggestion.getBody(),
//                        new DataHelper.OnFindColorsListener() {
//
//                            @Override
//                            public void onResults(List<ColorWrapper> results) {
//                                //show search results
//                                mSearchResultsAdapter.swapData(getContext(), results);
//                            }
//
//                        });
//
//                mLastQuery = searchSuggestion.getBody();
//            }
//
//            @Override
//            public void onSearchAction(String query) {
//                mLastQuery = query;
//
//                DataHelper.findColors(getActivity(), query,
//                        new DataHelper.OnFindColorsListener() {
//
//                            @Override
//                            public void onResults(List<ColorWrapper> results) {
//                                //show search results
//                                mSearchResultsAdapter.swapData(getContext(), results);
//                            }
//
//                        });
//            }
//        });

//        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
//            @Override
//            public void onFocus() {
//
//                //show suggestions when search bar gains focus (typically history suggestions)
//                mSearchView.swapSuggestions(DataHelper.getHistory(getActivity(), 5));
//            }
//
//            @Override
//            public void onFocusCleared() {
//
//                //set the title of the bar so that when focus is returned a new query begins
//                mSearchView.setSearchBarTitle(mLastQuery);
//
//                //you can also set setSearchText(...) to make keep the query there when not focused and when focus returns
//                //mSearchView.setSearchText(searchSuggestion.getBody());
//
//                Log.d(TAG, "onFocusCleared()");
//            }
//        });

        // Menu Item Click
        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {

                if (item.getItemId() == R.id.action_voice) {

                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    // Start the activity, the intent will be populated with the speech text
                    startActivityForResult(intent, SPEECH_REQUEST_CODE);

                } else {

                    if (checkCameraHardware(getContext())) {

                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        startActivity(intent);

                    } else {

                        Toast.makeText(getContext(), "This device don't has a camera.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

//        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
//            @Override
//            public void onBindSuggestion(View suggestionView, ImageView leftIcon,
//                                         TextView textView, SearchSuggestion item, int itemPosition) {
//                DealSearchSuggestion colorSuggestion = (DealSearchSuggestion) item;
//
//                String textColor = mIsDarkSearchTheme ? "#ffffff" : "#000000";
//                String textLight = mIsDarkSearchTheme ? "#bfbfbf" : "#787878";
//
//                if (colorSuggestion.getIsHistory()) {
//                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
//                            R.drawable.ic_history_black_24dp, null));
//
//                    Util.setIconColor(leftIcon, Color.parseColor(textColor));
//                    leftIcon.setAlpha(.36f);
//                } else {
//                    leftIcon.setAlpha(0.0f);
//                    leftIcon.setImageDrawable(null);
//                }
//
//                textView.setTextColor(Color.parseColor(textColor));
//                String text = colorSuggestion.getBody()
//                        .replaceFirst(mSearchView.getQuery(),
//                                "<font color=\"" + textLight + "\">" + mSearchView.getQuery() + "</font>");
//                textView.setText(Html.fromHtml(text));
//            }
//
//        });

        //listen for when suggestion list expands/shrinks in order to move down/up the
        //search results list
//        mSearchView.setOnSuggestionsListHeightChanged(new FloatingSearchView.OnSuggestionsListHeightChanged() {
//            @Override
//            public void onSuggestionsListHeightChanged(float newHeight) {
////                mRecyclerView.setTranslationY(newHeight);
//            }
//        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction(String currentQuery) {

                RealmResults<DealList> results;
                results = search(realm, DealList.class, currentQuery, false);

                List<DealList> dealLists = new ArrayList<>();
                for (DealList dealList : results) {

                    dealLists.add(dealList);
                }

                searchRecyclerView(dealLists);
            }
        });
    }

    public static <DealSearch extends RealmObject> RealmResults<DealList> search(Realm realm, Class<DealList> modelClass, String query, boolean partialSearch){

        RealmResults<DealList> realmResults = realm.where(modelClass).findAll();

        if (TextUtils.isEmpty(query)) {
            return realmResults;
        }

        realmResults = realmResults.where()
                .like("dealTitle", query, Case.INSENSITIVE)
                .or()
                .contains("dealTitle", query, Case.INSENSITIVE)
                .or()
                .like("dealLocation", query, Case.INSENSITIVE)
                .or()
                .contains("dealLocation", query, Case.INSENSITIVE)
                .findAll();

        return realmResults;
    }

    // Check if this device has a camera
    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    // This callback is invoked when the Speech Recognizer returns.
    // This is where you process the intent and extract the speech text from the intent.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {

            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);

            // Place the spoken text to search view
            mSearchView.setSearchText(spokenText);

            // Search spoken text
            RealmResults<DealList> dealResults;
            dealResults = search(realm, DealList.class, spokenText, false);

            List<DealList> dealLists = new ArrayList<>();
            for (DealList dealList : dealResults) {

                dealLists.add(dealList);
            }

            searchRecyclerView(dealLists);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void adapterOnClick(int adapterPosition) {

    }
}
