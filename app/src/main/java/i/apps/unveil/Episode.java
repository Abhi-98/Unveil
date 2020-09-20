package i.apps.unveil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Episode extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationViewUpper;
    ActionBarDrawerToggle toggle;
    //String name;
    public static String name;
    public static String ur;
    public static String url;
    String id,selectedItem;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode);

        name = getIntent().getExtras().getString("name");

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark)); //status bar or the time bar at the top
        }

        ur = "http://api.tvmaze.com/singlesearch/shows?q="+name;
        url = ur.replaceAll(" ", "%20");

        Bundle bundle = new Bundle();
        bundle.putString("url",url);
        InfoFragment fragment3 = new InfoFragment();
        fragment3.setArguments(bundle);
        EpisodesFragment fragment2 = new EpisodesFragment();
        fragment2.setArguments(bundle);
        CastFragment fragment = new CastFragment();
        fragment.setArguments(bundle);
        CrewFragment fragment1 = new CrewFragment();
        fragment1.setArguments(bundle);




        BottomNavigationView navigationView = findViewById(R.id.btm_nav);

        AutoCompleteTextView acTextView = (AutoCompleteTextView) findViewById(R.id.___placeholder_label);
        acTextView.setAdapter(new SuggestionAdapter(this,R.layout.auto_complete,acTextView.getText().toString()));

        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navigationViewUpper = findViewById(R.id.navigationView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawerOpen,R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationViewUpper.setNavigationItemSelectedListener(Episode.this);
        navigationViewUpper.bringToFront();



        acTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = (String) parent.getItemAtPosition(position);
                Toast.makeText(Episode.this, selectedItem, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Episode.this, Episode.class);
                intent.putExtra("name", selectedItem); // getText() SHOULD NOT be static!!!
                Bundle bundle1 = new Bundle();
                bundle1.putString("name",selectedItem);
                InfoFragment fragment1 = new InfoFragment();
                fragment1.setArguments(bundle1);

                startActivity(intent);
            }
        });

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if(id == R.id.episodes){

                    EpisodesFragment fragment = new EpisodesFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, fragment);
                    fragmentTransaction.commit();
                }else if(id == R.id.cast){

                    CastFragment fragment = new CastFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, fragment);
                    fragmentTransaction.commit();
                }else if(id == R.id.crew){

                    CrewFragment fragment = new CrewFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, fragment);
                    fragmentTransaction.commit();
                }else if(id == R.id.info){

                    InfoFragment fragment = new InfoFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, fragment);
                    fragmentTransaction.commit();
                }


                return true;
            }
        });

        navigationView.setSelectedItemId(R.id.info);

        loadData();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

//        int id = menuItem.getItemId();
//        menuItem.setChecked(true);
//        drawerLayout.closeDrawers();
//
        switch (menuItem.getItemId()){
            case R.id.home:
                startActivity(new Intent(getApplicationContext(),Home.class));
                break;
            case R.id.shows:
                startActivity(new Intent(getApplicationContext(),Home.class));
                break;
            case R.id.people:
                startActivity(new Intent(getApplicationContext(),People.class));
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

    private void loadData() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);

                    id = jsonObject.getString("id");


                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(stringRequest);
    }

    public String getMyData() {
        return url;
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


}
