package edu.progmatic.messenger.services;

import edu.progmatic.messenger.controllers.TopicController;
import edu.progmatic.messenger.model.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class TopicService {
    Logger logger = LoggerFactory.getLogger(TopicController.class);
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
    @Transactional
    public void deleteById(Long  topicId){
        logger.info(topicId +" =topic id");
        logger.info("meghivod a topic delete metodusa");
        Topic topicToDelete = em.find(Topic.class, topicId);
        logger.info(topicToDelete.getName() +" =topic neve");
        em.remove(topicToDelete);
    }
}
