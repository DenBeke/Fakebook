/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.business;

import fakebook.persistence.BiometricData;
import fakebook.persistence.User;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
/**
 *
 * @author robin
 */
@Stateless
public class BiometricServiceFacade implements BiometricServiceFacadeLocal {

    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private UserServiceFacadeLocal userService;
    
    /**
     * Push new heartrate data.
     * @param user
     * @param heartrate
     * @param timestamp 
     */
    @Override
    public void pushHeartrate(User user, Double heartrate, Calendar timestamp) {
        BiometricData data =  new BiometricData(user, heartrate, timestamp);
        em.persist(data);
    }
    
    /**
     * Pushes data from a json file to the database. 
     * Requires checking if format is correct, a user check.
     * @param json
     */
    @Override
    public void pushHeartrateJson(JsonObject json) {
        // get data from json object, and create a BiometricData object, persist it.
        JsonObject header = json.getJsonObject("header");
        JsonObject body = json.getJsonObject("body");
        
        // Parse the time, and date.
        String date = body.getJsonObject("effective_time_frame").getString("date_time");
        SimpleDateFormat timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Calendar time = Calendar.getInstance();
        try {
            Date t = timestamp.parse(date);
            time.setTime(t);
            System.out.println(t.toString());
        } catch (ParseException ex) {
            Logger.getLogger(BiometricServiceFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        // parse the heart rate value:
        double heart_rate;
        heart_rate = body.getJsonObject("heart_rate").getJsonNumber("value").doubleValue();
        
        // parse the user ID
        long user_id = (long)header.getInt("user_id");     // TODO: In Json data the user ID is "joe" which is a string -> adapt in biometric data sender.
        
        User user = userService.getUser(user_id);
        
        BiometricData data = new BiometricData(user, heart_rate, time);
        em.persist(data);
    }
    
    /**
     * get Heartrate data for a user in the interval bounded by min and max time.
     * @param userId
     * @param minTime
     * @param maxTime
     * @return 
     */
    @Override
    public List<BiometricData> getHeartrateData(long userId, Calendar minTime, Calendar maxTime) {
        System.out.println("GETTING HEARTRATE DATA");
        System.out.println(minTime.toString() + " - " + maxTime.toString());
        List<BiometricData> list = em.createNamedQuery("BiometricData.getByUser").setParameter("uid", userId).getResultList();
        List<BiometricData> retList = new ArrayList<>();
        
        for (BiometricData bData : list) {
            if (bData.getTimestamp().after(minTime) && bData.getTimestamp().before(maxTime)) {
                retList.add(bData);
            }
        }
        System.out.println("NR OF Values: " + retList.size());
        System.out.println(this.getJsonString(userId));
        return retList;
    }
    
    /**
     * returns a json string with the biometric data of a userID
     * @param userId
     * @return 
     */
    public String getJsonString(long userId){
        JSONObject obj = new JSONObject();
        JSONArray array = new JSONArray();
        
        List<BiometricData> list = em.createNamedQuery("BiometricData.getByUser").setParameter("uid", userId).getResultList();
        for(BiometricData dataPoint: list) {
            JSONObject dp = new JSONObject();
            dp.put("timestamp", dataPoint.getTimestampString());
            dp.put("heart_rate", dataPoint.getHeartrate());
            array.add(dp);
        }
        obj.put("User_id", userId);
        obj.put("Heart_rate_data", array);
        return obj.toJSONString();
    }
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
}
