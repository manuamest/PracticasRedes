package es.udc.redes.webserver;

public enum State {
    //Errores

    OK("200 OK"),
    NOT_MODIFIED("304 Not Modified"),
    BAD_REQUEST("400 Bad Request"),
    NOT_FOUND("404 Not Found")
    ;


    private final String state;

    State(String resposta) {
        this.state = resposta;
    }

    public String getState(){
        return state;
    }


}
