/**
* This is the ResponeMsg class the handles response message from the backend server API
* @author Kurt-Erik Karlsen and Aleksander V. Grunnvoll
* @version 1.1
*/

package no.hin.student.myassetmanager.Classes;

import com.google.gson.Gson;

public class ResponseMsg {
    private Boolean result = false;     // Indication for the request
    private String message = "";        // Message from server
    private String jsonResponse="";     // If server returns JSon data (Example: list with Equipment objects)

    public Boolean isResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getResult() {
        return result;
    }

    public String getJsonResponse() {
        return jsonResponse;
    }

    public void setJsonResponse(String jsonResponse) {
        this.jsonResponse = jsonResponse;
    }

    public String toJSONString() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }
}