package testscript;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SearchEmployee {
    public String authenticationToken=null;

    @Test(priority = 1)
    public void loginSucessfull() throws Exception{
        HttpResponse<JsonNode> jsonResponse = Unirest.post("http://idisruptors.forgeahead.io/users/sign_in.json")
                .header("Content-type", "application/json")
                .body("{\"user\" : {\"email\" : \"shwetabh.saran@forgeahead.io\", \"password\" : \"password\"}}")
                .asJson();
        int Code = jsonResponse.getCode();
        if (Code==200){
            authenticationToken=jsonResponse.getBody().getObject().get("authentication_token").toString();
            Assert.assertEquals(Code, 200);
        }
    }


    @Test(priority = 2)
    public void searchEmployee() throws Exception{
        HttpResponse<JsonNode>  searchEmployee = Unirest.post("http://idisruptors.forgeahead.io/employees/search.json")
                .header("Content-type", "application/json")
                .header("user-token", authenticationToken)
                .header("user-email", "shwetabh.saran@forgeahead.io")
                .body("{\"search\":{\"name\":\"Ashish\",\"skills\":[],\"interests\":[],\"departments\":[],\"teams\":[],\"business_unit\":\"\",\"business_unit_ids\":[],\"department_ids\":[],\"team_ids\":[]}}")
                .asJson();
        int Code = searchEmployee.getCode();

        if (searchEmployee.getBody().getObject().getJSONArray("employees").length()!=0 || Code==200){
            JSONArray arr = searchEmployee.getBody().getObject().getJSONArray("employees");
            int employeeCount = arr.length();
            for (int i =0; i<employeeCount;i++){
                JSONObject object = arr.getJSONObject(i);
                String firstName = object.get("first_name").toString();
                Assert.assertEquals("Ashish",firstName);
            }
        }

    }
}
