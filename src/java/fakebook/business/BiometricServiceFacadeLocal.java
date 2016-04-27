/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.business;

import fakebook.persistence.BiometricData;
import fakebook.persistence.User;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * @author robin
 */
public interface BiometricServiceFacadeLocal {

    /**
     * Push heart rate to persistence layer.
     * @param user
     * @param heartrate
     * @param timestamp 
     */
    public void pushHeartrate(User user, Double heartrate, Calendar timestamp);

    /**
     * get Heartrate data for a user in the interval bounded by min and max time.
     * @param userId
     * @param minTime
     * @param maxTime
     * @return 
     */
    public List<BiometricData> getHeartrateData(long userId, Calendar minTime, Calendar maxTime);

    public void pushHeartrateJson(JsonObject json);
    
}
