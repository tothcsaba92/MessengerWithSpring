package edu.progmatic.messenger.services;

import edu.progmatic.messenger.model.Topic;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class TopicService {

    @PersistenceContext
    EntityManager em;

    @Transactional
    public void createNewTopic(String name){
        Topic topic = new Topic();
        topic.setName(name);
        em.persist(topic);
    }

    public List<Topic> findAllTopics(){
        return em.createQuery("SELECT t FROM Topic t")
                .getResultList();
    }

    public Topic findById(Long id){
        return (Topic) em.createQuery("SELECT t FROM Topic t WHERE t.id = :topicId")
                .setParameter("topicId",id)
                .getResultList().get(0);
    }
}
