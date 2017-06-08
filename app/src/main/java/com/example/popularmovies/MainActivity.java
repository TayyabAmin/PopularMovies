package com.example.popularmovies;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.adapter.MoviesAdapter;
import com.example.model.MovieData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String apiKey = "";
    private final String popularMoviePath = "https://api.themoviedb.org/3/movie/popular?api_key="+apiKey;
    private final String topRatedMoviePath = "https://api.themoviedb.org/3/movie/top_rated?api_key="+apiKey;
    private ProgressDialog pDialog;
    private JSONObject jsonObj;
    private GridView gridView;
    private ArrayList<MovieData> arrayList;
//    private SharedPreferences sharedPreferences;
//    public static final String mypreference = "mypref";
//    public static final String orderPreference = "order";
    private static boolean isPopularMovieSelected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);

                intent.putExtra("title", arrayList.get(position).getMovieTitle());
                intent.putExtra("poster_path", arrayList.get(position).getPath());
                intent.putExtra("rating", arrayList.get(position).getMovieRating());
                intent.putExtra("release_date", arrayList.get(position).getReleaseDate());
                intent.putExtra("overview", arrayList.get(position).getMovieOverview());

                startActivity(intent);
            }
        });

//        sharedPreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putBoolean(orderPreference, )
        fetchMovies(popularMoviePath);
    }

    private void fetchMovies(String moviesPath) {
        // prepare the loading Dialog
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait while retrieving the data ...");
        pDialog.setCancelable(false);

        // Check if Internet is working
        if (!isNetworkAvailable(this)) {
            // Show a message to the user to check his Internet
            Toast.makeText(this, "Please check your Internet connection", Toast.LENGTH_SHORT).show();
        } else {
            pDialog.show();

            // make HTTP request to retrieve the weather
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    moviesPath, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        arrayList = new ArrayList<>();
                        int length = response.getJSONArray("results").length();
                        for (int i=0; i<length; i++) {
                            jsonObj = (JSONObject) response.getJSONArray("results").get(i);
//                            Log.d("TAG", "onResponse: "+jsonObj);

                            MovieData poster = new MovieData();

//                            poster.setMovieId(jsonObj.getInt("id"));
                            poster.setPath(jsonObj.getString("poster_path"));
                            poster.setMovieTitle(jsonObj.getString("original_title"));
                            poster.setMovieOverview(jsonObj.getString("overview"));
                            poster.setMovieRating(jsonObj.getString("vote_average"));
                            poster.setReleaseDate(jsonObj.getString("release_date"));
                            arrayList.add(poster);
                        }

                        pDialog.dismiss();
                        MoviesAdapter adapter = new MoviesAdapter(MainActivity.this, R.layout.grid_view, arrayList);
                        gridView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        pDialog.dismiss();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("tag", "Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(), "Error while loading ... ", Toast.LENGTH_SHORT).show();
                    // hide the progress dialog
                    pDialog.dismiss();
                }
            });
            // Adding request to request queue
            AppController.getInstance(this).addToRequestQueue(jsonObjReq);
        }
    }

    ////////////////////check internet connection
    private boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_setting) {

            View menuItemView = findViewById(R.id.action_setting);
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, menuItemView);
            popupMenu.getMenuInflater().inflate(R.menu.pop_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    switch (id) {
                        case R.id.popular:
                            if (!isPopularMovieSelected) {
                                isPopularMovieSelected = true;
                                fetchMovies(popularMoviePath);
                            }
                            break;
                        case R.id.rating:
                            if (isPopularMovieSelected) {
                                isPopularMovieSelected = false;
                                fetchMovies(topRatedMoviePath);
                            }
                            break;
                    }

                    return true;
                }
            });

            popupMenu.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
