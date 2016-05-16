/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fakebook.business;

import fakebook.persistence.Post;
import java.util.Date;
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
    public void updatePost(Post post) {
        em.merge(post);
    }
    
    @Override
    public Boolean deletePost(Post post) {
        if (post == null) {
            return false;
        } else {
            em.remove(em.merge(post));
            return true;
        }
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
    public List<Post> getAllWallPosts() {
        return em.createNamedQuery("Posts.getAllWallPosts").getResultList();
    }
    
    @Override
    public List<Post> getPostsOnWall(long userId) {
        return em.createNamedQuery("Posts.getOnWall").setParameter("uId", userId).getResultList();
    }

    @Override
    public List<Post> getPostsByPoster(long userId) {
        return em.createNamedQuery("Posts.getByPoster").setParameter("uId", userId).getResultList();
    }

    @Override
    public int getWallPostCountInPeriod(Date periodStart, Date periodEnd) {
        return em.createNamedQuery("Posts.getWallPostsByPeriod").setParameter("startDate", periodStart).setParameter("endDate", periodEnd).getResultList().size();
    }
    
    @Override
    public int getCommentCountInPeriod(Date periodStart, Date periodEnd) {
        return em.createNamedQuery("Posts.getCommentsByPeriod").setParameter("startDate", periodStart).setParameter("endDate", periodEnd).getResultList().size();
    }

    @Override
    public int getPicturePostCountInPeriod(Date periodStart, Date periodEnd) {
        return em.createNamedQuery("Posts.getPicturePostsByPeriod").setParameter("startDate", periodStart).setParameter("endDate", periodEnd).getResultList().size();
    }
    
    @Override
    public int getVideoPostCountInPeriod(Date periodStart, Date periodEnd) {
        return em.createNamedQuery("Posts.getVideoPostsByPeriod").setParameter("startDate", periodStart).setParameter("endDate", periodEnd).getResultList().size();
    }
    
    @Override
    public int getPicturePostCount() {
        return em.createNamedQuery("Posts.getPicturePosts").getResultList().size();
    }
    
    @Override
    public int getVideoPostCount() {
        return em.createNamedQuery("Posts.getVideoPosts").getResultList().size();
    }

    @Override
    public int getPostCount() {
        return em.createNamedQuery("Posts.getWallPosts").getResultList().size();
    }

    @Override
    public int getCommentCount() {
        return em.createNamedQuery("Posts.getComments").getResultList().size();
    }
}
