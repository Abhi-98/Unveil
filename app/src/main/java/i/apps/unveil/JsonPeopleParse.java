package i.apps.unveil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class JsonPeopleParse {
    double current_latitude,current_longitude;
    public JsonPeopleParse(){}
    public JsonPeopleParse(double current_latitude,double current_longitude){
        this.current_latitude=current_latitude;
        this.current_longitude=current_longitude;
    }
    public List<PeopleSuggestGetSet> getParseJsonWCF1(String sName)
    {
        List<PeopleSuggestGetSet> ListData1 = new ArrayList<PeopleSuggestGetSet>();
        try {
            String temp=sName.replace(" ", "%20");
            URL js = new URL("http://api.tvmaze.com/search/people?q="+temp);
            URLConnection jc = js.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
            String line = reader.readLine();
            JSONArray jsonArray = new JSONArray(line);
            for(int i = 0; i < jsonArray.length(); i++){


                JSONObject r = jsonArray.getJSONObject(i);



                ListData1.add(new PeopleSuggestGetSet(r.getJSONObject("person").getString("name")));            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return ListData1;

    }

}
