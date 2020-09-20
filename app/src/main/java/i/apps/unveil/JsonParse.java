package i.apps.unveil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class JsonParse {
    double current_latitude,current_longitude;
    public JsonParse(){}
    public JsonParse(double current_latitude,double current_longitude){
        this.current_latitude=current_latitude;
        this.current_longitude=current_longitude;
    }
    public List<SuggestGetSet> getParseJsonWCF(String sName)
    {
        List<SuggestGetSet> ListData = new ArrayList<SuggestGetSet>();
        try {
            String temp=sName.replace(" ", "%20");
            URL js = new URL("http://api.tvmaze.com/search/shows?q="+temp);
            URLConnection jc = js.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
            String line = reader.readLine();
            JSONArray jsonArray = new JSONArray(line);
            for(int i = 0; i < jsonArray.length(); i++){


                JSONObject r = jsonArray.getJSONObject(i);
                JSONObject jsonObjectShow = r.getJSONObject("show");

             //   JSONObject jsonObjectImage = jsonObjectShow.getJSONObject("image");

                SuggestGetSet suggestGetSet = new SuggestGetSet();

                suggestGetSet.setImage(r.getJSONObject("show").getJSONObject("image").getString("medium"));


                ListData.add(new SuggestGetSet(r.getJSONObject("show").getString("id"),r.getJSONObject("show").getString("name"),r.getJSONObject("show").getJSONObject("image").getString("medium")));
            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return ListData;

    }

}
