package testscript;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class EmployeeList{

    public String authenticationToken=null;

    @Test(priority = 1)
    public void loginSucessfull() throws Exception{
        HttpResponse<JsonNode> login = Unirest.post("http://idisruptors.forgeahead.io/users/sign_in.json")
                .header("Content-type", "application/json")
                .body("{\"user\" : {\"email\" : \"shwetabh.saran@forgeahead.io\", \"password\" : \"password\"}}")
                .asJson();
        int Code = login.getCode();
        if (Code==200){
            authenticationToken=login.getBody().getObject().get("authentication_token").toString();
            Assert.assertEquals(Code, 200);
        }
    }

    @Test(priority = 2)
    public void viewEmployeeList() throws Exception{
        HttpResponse<JsonNode> employeeList = Unirest.get("http://idisruptors.forgeahead.io/employees.json")
                .header("Content-type", "application/json")
                .header("user-token", authenticationToken)
                .header("user-email", "shwetabh.saran@forgeahead.io")
                .asJson();
        int Code = employeeList.getCode();
        if (employeeList.getBody().getObject().getJSONArray("employees").length() == 0)
        {
            Assert.assertNotEquals(Code,200);
        }else {
            int employeeCount = employeeList.getBody().getObject().getJSONArray("employees").length();
            JSONArray arr = employeeList.getBody().getObject().getJSONArray("employees");
            for (int i =0; i<employeeCount;i++){
                JSONObject object = arr.getJSONObject(i);
                String id = object.get("id").toString();
                HttpResponse<JsonNode> jsonNodeHttpResponse1 = Unirest.get("http://idisruptors.forgeahead.io/employees/"+id+".json")
                        .header("Content-type", "application/json")
                        .header("user-token", authenticationToken)
                        .header("user-email", "shwetabh.saran@forgeahead.io")
                        .asJson();
                Assert.assertEquals(Code, 200);
                System.out.println(jsonNodeHttpResponse1.getBody());
            }
        }
        }

    @Test(priority = 3)
    public void loggout() throws Exception{
        HttpResponse<JsonNode> jsonNodeHttpResponse = Unirest.delete("http://idisruptors.forgeahead.io/users/sign_out.json")
                .header("Content-type", "application/json")
                .header("user-token", authenticationToken)
                .header("user-email", "shwetabh.saran@forgeahead.io")
                .asJson();
        int Code = jsonNodeHttpResponse.getCode();
        Assert.assertEquals(Code, 204);

    }



}
