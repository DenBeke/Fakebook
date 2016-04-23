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
     * Gets the heartrate data for user from 10 seconds before timestamp to 30 seconds after timestamp
     * @param userId
     * @param timestamp
     * @return 
     */
    public List<BiometricData> getHeartrateData(long userId, Calendar timestamp);
    
}
