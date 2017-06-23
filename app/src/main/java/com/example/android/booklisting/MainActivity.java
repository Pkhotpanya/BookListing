package com.example.android.booklisting;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private EditText mEditTextSearch;
    private Button mButtonSearch;
    private TextView mTextViewResponse;
    private ProgressBar mProgressBar;
    private static final String URL_BOOKS = "https://www.googleapis.com/books/v1/volumes?maxResults=10&q=";
    private BookAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTextSearch = (EditText) findViewById(R.id.edittext_genre);
        mEditTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL
                        && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    searchGenre();
                }
                return true;
            }
        });
        mButtonSearch = (Button) findViewById(R.id.button_search);
        mButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchGenre();
            }
        });

        mTextViewResponse = (TextView) findViewById(R.id.textview_response);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar_lookbusy);
        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        ListView bookList = (ListView) findViewById(R.id.listview_list);
        bookList.setAdapter(mAdapter);
        bookList.setEmptyView(mTextViewResponse);

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            getLoaderManager().initLoader(0, null, this).forceLoad();
        } else {
            mProgressBar.setVisibility(View.GONE);
            mTextViewResponse.setText(R.string.response_no_internet);
        }
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        return new BookAsycTaskLoader(this, URL_BOOKS + mEditTextSearch.getText().toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        mProgressBar.setVisibility(View.GONE);
        mAdapter.clear();

        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        } else {
            mTextViewResponse.setText(R.string.search_directions);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mProgressBar.setVisibility(View.VISIBLE);
        mTextViewResponse.setText("");
        mAdapter.clear();
    }

    private void searchGenre() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            getLoaderManager().restartLoader(0, null, MainActivity.this).forceLoad();
            mProgressBar.setVisibility(View.VISIBLE);
            mTextViewResponse.setText("");
            mAdapter.clear();
        } else {
            mProgressBar.setVisibility(View.GONE);
            mTextViewResponse.setText(R.string.response_no_internet);
        }
    }
}
