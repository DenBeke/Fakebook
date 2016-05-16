/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.web;

import fakebook.business.BiometricServiceFacadeLocal;
import java.io.StringReader;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author robin
 */
@ServerEndpoint("/BiometricDataEndpoint")
public class BiometricDataSocket {

    @EJB
    private BiometricServiceFacadeLocal biometricService;

    
    @OnMessage
    public void onMessage(String message) {
        // Exctract json message, and push to database.
        System.out.println("got Data: " + message);
        JsonObject json = Json.createReader(new StringReader(message)).readObject();
        System.out.println("Created Json object");
        biometricService.pushHeartrateJson(json);
        System.out.println("Persisted object");
    }
    
    // Currently no session management, could add it for security reasons, but should work without.
}
