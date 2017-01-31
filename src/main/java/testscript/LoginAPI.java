package testscript;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.HttpResponse;
import org.testng.Assert;
import org.testng.annotations.Test;


public class LoginAPI {

    public String authenticationToken=null;

    @Test(priority = 1)
    public void loginSucessfull() throws Exception{
        HttpResponse<JsonNode> jsonResponse = Unirest.post("http://10.10.9.165:3000/users/sign_in.json")
                .header("Content-type", "application/json")
                .body("{\"user\" : {\"email\" : \"lareb.nawab@forgeahead.io\", \"password\" : \"password\"}}")
                .asJson();
        int Code = jsonResponse.getCode();
        if (Code==200){
            authenticationToken=jsonResponse.getBody().getObject().get("authentication_token").toString();
            Assert.assertEquals(Code,200);
        }
    }

    @Test
    public void loggout() throws Exception{
        HttpResponse<JsonNode> jsonNodeHttpResponse = Unirest.delete("http://10.10.9.165:3000/users/sign_out.json")
                .header("Content-type", "application/json")
                .header("user-token", authenticationToken)
                .header("user-email", "lareb.nawab@forgeahead.io")
                .asJson();
        int Code = jsonNodeHttpResponse.getCode();
        Assert.assertEquals(Code, 204);

    }

    @Test(priority = 3)
    public void loginFailed() throws Exception{
        HttpResponse<JsonNode> jsonResponse = Unirest.post("http://10.10.9.165:3000/users/sign_in.json")
                .header("Content-type", "application/json")
                .body("{\"user\" : {\"email\" : \"labbbreb.nawab@forgeahead.io\", \"password\" : \"password\"}}")
                .asJson();
        int Code = jsonResponse.getCode();
        if (Code==401)
        {
            jsonResponse.getBody().getObject().get("error");
            Assert.assertEquals(Code,401);

        }
        else{
            authenticationToken=jsonResponse.getBody().getObject().get("authentication_token").toString();

        }
    }

    @Test(priority = 4)
    public void unSufficientData() throws Exception{
        HttpResponse<JsonNode> jsonResponse = Unirest.post("http://10.10.9.165:3000/users/sign_in.json")
                .header("Content-type", "application/json")
                .body("{\"user\" : {\"email\" : \"\", \"password\" : \"\"}}")
                .asJson();
        int code = jsonResponse.getCode();
        System.out.println(jsonResponse.getCode());
        if (code==200)
        {
            authenticationToken=jsonResponse.getBody().getObject().get("authentication_token").toString();
        }
        else{
            Assert.assertEquals(code,401);
            jsonResponse.getBody().getObject().get("error");
        }
    }

    @Test(priority = 5)
    public void wrongURL() throws Exception{
        HttpResponse<JsonNode> jsonResponse = Unirest.post("http://10.10.9.165:3000/uusers/sign_in.json")
                .header("Content-type", "application/json")
                .body("{\"user\" : {\"email\" : \"lareb.nawab@forgeahead.io\", \"password\" : \"password\"}}")
                .asJson();
        int Code = jsonResponse.getCode();
        Assert.assertEquals(Code,404);
    }
    

}
