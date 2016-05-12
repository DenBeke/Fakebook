/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.business;

import fakebook.persistence.User;
import javax.ejb.Local;
import javax.servlet.http.Part;


@Local
public interface AttachmentServiceFacadeLocal {
    
    public Long upload(User owner, Part attachment);

    public byte[] getAttachment(Long id);
    
}

