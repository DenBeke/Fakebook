/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.business;

import fakebook.persistence.Post;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class PostServiceFacade implements PostServiceFacadeLocal {

    
    @PersistenceContext
    private EntityManager em;

    
    @Override
    public Boolean newPost(Post p) {
        em.persist(p);
        return true;
    }

    @Override
    public Post getPost(long pId) {
        return em.find(Post.class, pId);
    }

    @Override
    public List<Post> getAllPosts() {
        return em.createNamedQuery("Posts.getAll").getResultList();
    }

    @Override
    public List<Post> getPostByPoster(long userId) {
        return em.createNamedQuery("Posts.getAll").setParameter("uId", userId).getResultList();
    }

    @Override
    public List<Post> getPostByMention(long userId) {
        //TODO
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
