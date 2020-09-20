package i.apps.unveil;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class EpisodesFragment extends Fragment {

    public EpisodesFragment() {
        // Required empty public constructor
    }

    String url,name,ur,id,url1;
    RecyclerView recyclerView;
    EpisodesListAdapter adaptor;

    ArrayList<EpisodesList> episodesLists;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_episodes, container, false);
        Episode activity = (Episode) getActivity();
        name = activity.getMyData();

        ur = name;
        url = ur.replaceAll(" ", "%20");

        Log.i("RRRRRRRRRRRR",url);

        recyclerView = v.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adaptor = new EpisodesListAdapter();
        recyclerView.setAdapter(adaptor);
        episodesLists = new ArrayList<>();
        loadData();
        return v;
    }

    private void loadData() {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);


                    id = jsonObject.getString("id");

                    url1 = "http://api.tvmaze.com/shows/" + id + "/episodes";

                    Log.i("JJJJJJJJJ",url1);

                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url1, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject jsonObject = response.getJSONObject(i);

                                    EpisodesList episodesList = new EpisodesList();

                                    episodesList.setSeason("S"+jsonObject.getString("season"));

                                    int id1 = jsonObject.getInt("season");

                                    episodesList.setNumber("E" + jsonObject.getString("number"));
                                    episodesList.setDate(jsonObject.getString("airdate"));
                                    episodesList.setName(jsonObject.getString("name"));

                                    episodesLists.add(episodesList);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            adaptor.setData(episodesLists);
                            adaptor.notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Volley", error.toString());
                        }
                    });
                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    requestQueue.add(jsonArrayRequest);


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

}
