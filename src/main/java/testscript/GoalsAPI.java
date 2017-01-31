package testscript;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GoalsAPI {

    String baseURL = "http://idisruptors.forgeahead.io/";

    public String authenticationToken = null;

    @Test(priority = 1)
    public void login() throws Exception{
        HttpResponse<JsonNode> jsonResponse = Unirest.post(baseURL+"users/sign_in.json")
                .header("Content-type", "application/json")
                .body("{\"user\" : {\"email\" : \"shwetabh.saran@forgeahead.io\", \"password\" : \"password\"}}")
                .asJson();
        int Code = jsonResponse.getCode();
        if (Code==200){
            authenticationToken=jsonResponse.getBody().getObject().get("authentication_token").toString();
            Assert.assertEquals(Code,200);
        }

    }

    @Test(priority = 2)
    public void fetchGoals() throws Exception{
        String urlArr[] = {"companies/1/goals/5.json", "teams/2/goals.json", "employees/2/goals.json"};

        for(int i = 0; i<urlArr.length;i++){
            HttpResponse<JsonNode>  fetchGoal = Unirest.get(baseURL + urlArr[i])
                    .header("Content-type", "application/json")
                    .header("user-token", authenticationToken)
                    .header("user-email", "shwetabh.saran@forgeahead.io")
                    .asJson();
            int code = fetchGoal.getCode();
            if (code == 201) {
                Assert.assertEquals(code, 201);
                System.out.println("Title : " + fetchGoal.getBody().getObject().get("title").toString() + "\n" + "Description : " + fetchGoal.getBody().getObject().get("description").toString()+"\n");
            }
            else if(code == 200) {
                Assert.assertEquals(code, 200);
                JSONArray arr = fetchGoal.getBody().getObject().getJSONArray("goals");
                JSONObject object = arr.getJSONObject(0);
                System.out.println("Title :"+object.get("title")+"\n"+"Description :"+object.get("description")+"\n");
            }

            }

        }
    }




