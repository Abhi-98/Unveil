package i.apps.unveil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class genreFilter extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

    String url = "https://api.tvmaze.com/schedule?country=US&date="+date;

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    RecyclerView recyclerView;
    ShowsAdapter adaptor;
    String selectedItem;

    Button comedy;

    ImageView filter;

    Button star5,star6,star7,star8,star9,star10;

    int num;
    String name;


    ArrayList<Shows> shows;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_filter);

        name = getIntent().getExtras().getString("num");

        num = Integer.parseInt(name);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark)); //status bar or the time bar at the top
        }

        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawerOpen,R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(genreFilter.this);
        navigationView.bringToFront();


        filter = findViewById(R.id.filter);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adaptor = new ShowsAdapter();
        recyclerView.setAdapter(adaptor);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));

        final AutoCompleteTextView acTextView = (AutoCompleteTextView) findViewById(R.id.___placeholder_label);
        acTextView.setAdapter(new SuggestionAdapter(this,R.layout.auto_complete,acTextView.getText().toString()));


        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCustomDialog();
            }
        });

//        ViewGroup viewGroup = findViewById(android.R.id.content);
//
//        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_content, viewGroup, false);
//
//        comedy = dialogView.findViewById(R.id.comedy);
//
//

        acTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = (String) parent.getItemAtPosition(position);
                Toast.makeText(genreFilter.this, selectedItem, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(genreFilter.this, Episode.class);
                intent.putExtra("name", selectedItem); // getText() SHOULD NOT be static!!!
                Bundle bundle1 = new Bundle();
                bundle1.putString("name",selectedItem);
                InfoFragment fragment1 = new InfoFragment();
                fragment1.setArguments(bundle1);

                startActivity(intent);



            }
        });

        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
        shows = new ArrayList<>();

        getData();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.home:
                startActivity(new Intent(getApplicationContext(),Home.class));
                break;
            case R.id.people:
                startActivity(new Intent(getApplicationContext(),People.class));
                break;
            case R.id.shows:
                startActivity(new Intent(getApplicationContext(),ShowsList.class));
                break;
            case R.id.about:
                Toast.makeText(getApplicationContext(), "About us Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // user is now signed out
                                // startActivity(new Intent(MyActivity.this, SignInActivity.class));
                                finish();
                            }
                        });

                break;
            default:
                break;
        }
        return false;
    }



    private void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        Shows show = new Shows();

                        JSONObject jsonObjectShow = jsonObject.getJSONObject("show");
                        JSONObject jsonObjectImage = jsonObjectShow.getJSONObject("image");

                        String sum = jsonObjectShow.getString("summary");

                        ArrayList<String> comedy = new ArrayList<>();


                            Double id = jsonObjectShow.getJSONObject("rating").getDouble("average");

                            //double rate = Double.parseDouble(id);

                            if (id > num) {

                                show.setImage(jsonObjectImage.getString("medium"));
                                show.setTitle(jsonObjectShow.getString("name"));
                                show.setBody(Html.fromHtml(Html.fromHtml(sum).toString()));
                                show.setRating(jsonObjectShow.getJSONObject("rating").getString("average"));
                                shows.add(show);

                            }



                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }
                adaptor.setData(shows);
                adaptor.notifyDataSetChanged();
                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    private void showCustomDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_content, viewGroup, false);

        comedy = dialogView.findViewById(R.id.comedy);

        star5 = dialogView.findViewById(R.id.star5);
        star6 = dialogView.findViewById(R.id.star6);
        star7 = dialogView.findViewById(R.id.star7);
        star8 = dialogView.findViewById(R.id.star8);
        star9 = dialogView.findViewById(R.id.star9);
//        comedy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Home.this,genreFilter.class));
//            }
//        });
        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(genreFilter.this, genreFilter.class);
                intent.putExtra("num", "5"); // getText() SHOULD NOT be static!!!
                startActivity(intent);
            }
        });

        star6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(genreFilter.this, genreFilter.class);
                intent.putExtra("num", "6"); // getText() SHOULD NOT be static!!!
                startActivity(intent);
            }
        });

        star7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(genreFilter.this, genreFilter.class);
                intent.putExtra("num", "7"); // getText() SHOULD NOT be static!!!
                startActivity(intent);
            }
        });

        star8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(genreFilter.this, genreFilter.class);
                intent.putExtra("num", "8"); // getText() SHOULD NOT be static!!!
                startActivity(intent);
            }
        });

        star9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(genreFilter.this, genreFilter.class);
                intent.putExtra("num", "9"); // getText() SHOULD NOT be static!!!
                startActivity(intent);
            }
        });

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
