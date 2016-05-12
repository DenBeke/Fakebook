/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.business;

import fakebook.persistence.Attachment;
import fakebook.persistence.User;
import java.io.IOException;
import java.io.InputStream;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.Part;


@Stateless
public class AttachmentServiceFacade implements AttachmentServiceFacadeLocal {

    @PersistenceContext
    private EntityManager em;
    
    @Override
    public Long upload(User owner, Part file) {
        InputStream input;
        try {
            input = file.getInputStream();

            long length = file.getSize();
            if (length > 1024*1024*5) { // MAX 5MB
                return null;
            }

            byte[] bytes = new byte[(int)length];

            int offset = 0;
            int bytesRead = 0;
            while (offset < bytes.length && (bytesRead=input.read(bytes, offset, bytes.length-offset)) >= 0) {
                offset += bytesRead;
            }

            if (offset < bytes.length) {
                return null;
            }
            
            Attachment attachment = new Attachment(owner, bytes);
            em.persist(attachment);
            em.flush();
 
            return attachment.getId();
        }
        catch (IOException e) {
            return null;
        }
    }

    @Override
    public byte[] getAttachment(Long id) {
        Attachment attachment = em.find(Attachment.class, id);
        if (attachment == null) {
            return null;
        }
        else {
            return attachment.getBytes();
        }
    }
}
