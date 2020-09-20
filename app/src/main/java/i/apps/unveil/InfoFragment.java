package i.apps.unveil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {

    public InfoFragment() {
        // Required empty public constructor
    }

    String name,id;
    String ur,url,r;

    TextView webChannel,status,genre,showtype,officialsite,summary;
    RatingBar rating;
    ImageView img;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.fragment_info, container, false);
        Episode activity = (Episode) getActivity();
        name = activity.getMyData();

        ur = name;
        url = ur.replaceAll(" ", "%20");

        Log.i("UUUUUU",url);

        webChannel = v.findViewById(R.id.webChannel);
        status = v.findViewById(R.id.status);
        genre = v.findViewById(R.id.genre);
        showtype = v.findViewById(R.id.showtype);
        rating = v.findViewById(R.id.rating);
        officialsite = v.findViewById(R.id.officialsite);
        summary = v.findViewById(R.id.summary);
        img = v.findViewById(R.id.img);

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

                    StringBuffer result = new StringBuffer();
                    for (int i = 0; i < jsonObject.getJSONArray("genres").length(); i++) {
                        result.append( jsonObject.getJSONArray("genres").get(i) + " ");
                    }
                    String mynewstring = result.toString();
                    String sum = jsonObject.getString("summary");

                    id = jsonObject.getString("id");

                    if(jsonObject.isNull("network")){
                        webChannel.setText("--");
                    }else{

                            webChannel.setText(jsonObject.getJSONObject("network").getString("name"));

                    }
                    status.setText(jsonObject.getString("status"));
                    showtype.setText(jsonObject.getString("type"));
                    genre.setText(mynewstring);

                    if(jsonObject.getJSONObject("rating").isNull("average")){
                        rating.setRating(0);
                    }else{
                        r = jsonObject.getJSONObject("rating").getString("average");
                        float rate = Float.parseFloat(r);
                        rating.setRating(rate);

                    }
                    officialsite.setText(jsonObject.getString("premiered"));
                    summary.setText(Html.fromHtml(Html.fromHtml(sum).toString()));


                    String imgV = jsonObject.getJSONObject("image").getString("original");

                    Picasso.get().load(imgV).into(img);




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
