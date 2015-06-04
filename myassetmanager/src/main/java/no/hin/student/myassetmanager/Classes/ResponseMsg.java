package no.hin.student.myassetmanager.Classes;

import com.google.gson.Gson;

public class ResponseMsg {
    private Boolean result = false;     //Indikerer om forespÃ¸rselen gikk bra eller ikke.
    private String message ="";         //Melding eller feilmelding fra server.
    private String jsonResponse="";     //Dersom serveren returnerer data, f.eks. en liste med Equipment-objekter, ligger dette her som JSON.

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