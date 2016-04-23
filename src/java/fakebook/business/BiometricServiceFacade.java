/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.business;

import fakebook.persistence.BiometricData;
import fakebook.persistence.User;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author robin
 */
@Stateless
public class BiometricServiceFacade implements BiometricServiceFacadeLocal {

    @PersistenceContext
    private EntityManager em;

    
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
     * get Heartrate data for a user round the timestamp.
     * @param userId
     * @param timestamp
     * @return 
     */
    @Override
    public List<BiometricData> getHeartrateData(long userId, Calendar timestamp) {
        List<BiometricData> list = em.createNamedQuery("BiometricData.getByUser").setParameter("uid", userId).getResultList();
        List<BiometricData> retList = new ArrayList<>();
        
        Calendar minTime = timestamp;
        Calendar maxTime = timestamp;
        minTime.add(Calendar.SECOND, -10);
        maxTime.add(Calendar.SECOND, 30);
        
        for (BiometricData bData : list) {
            if (bData.getTimestamp().after(minTime) && bData.getTimestamp().before(maxTime)) {
                retList.add(bData);
            }
        }
        return retList;
    }
    
    
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
}
