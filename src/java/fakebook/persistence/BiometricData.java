/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.persistence;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import static javax.persistence.TemporalType.DATE;
import static javax.persistence.TemporalType.TIMESTAMP;

/**
 *
 * @author robin
 */
@Entity
@NamedQueries({@NamedQuery(name="BiometricData.getAll",query="SELECT b FROM BiometricData b"),
               @NamedQuery(name="BiometricData.getByUser", query="SELECT b FROM BiometricData b WHERE b.user.id = :uid")})

public class BiometricData implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private User user;      // The user whose heart rate is represented
    
    private Double heartrate;   // The measured heart rate
    
    @Temporal(TIMESTAMP)
    private Calendar timestamp; // The time the measurement was taken. 

    public BiometricData() {    // Default constructor needed for JAVA EE
    }

    public BiometricData(User user, Double heartrate, Calendar timestamp) {
        this.user = user;
        this.heartrate = heartrate;
        this.timestamp = timestamp;
    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getHeartrate() {
        System.out.println("HEARTRATE: " + heartrate);
        return heartrate;
    }

    public void setHeartrate(Double heartrate) {
        this.heartrate = heartrate;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }
    
    /**
     * A proper printable String of the timestamp, and not the massive nonsense of the Calendar object
     * @return 
     */
    public String getTimestampString() {
        return timestamp.get(Calendar.YEAR) + "/" + timestamp.get(Calendar.MONTH) + "/" + timestamp.get(Calendar.DAY_OF_MONTH) + " " + timestamp.get(Calendar.HOUR) + ":" +timestamp.get(Calendar.MINUTE) + ":" + timestamp.get(Calendar.SECOND);
    }

    public void setTimestamp(Calendar timestamp) {
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BiometricData)) {
            return false;
        }
        BiometricData other = (BiometricData) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fakebook.persistence.BiometricData[ id=" + id + " ]";
    }
    
}
