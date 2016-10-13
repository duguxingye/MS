package com.tys.ms.dao;

import com.tys.ms.model.User;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.List;

@Repository("userDao")
public class UserDaoImpl extends AbstractDao<Integer, User> implements UserDao {
    static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    public User findById(int id) {
        User user = getByKey(id);
        if(user!=null){
            Hibernate.initialize(user.getUserProfile());
        }
        return user;
    }

    public User findByJobID(String jobId) {
        logger.info("JobID : {}", jobId);
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("jobId", jobId));
        User user = (User) criteria.uniqueResult();
        if(user!=null){
            Hibernate.initialize(user.getUserProfile());
            System.out.println(user.getUserProfile());
        } else {
            System.out.println("wrong");
        }
        return user;
    }

    @SuppressWarnings("unchecked")
    public List<User> findAllUsers() {
        Criteria criteria = createEntityCriteria().addOrder(Order.asc("name"));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); //  避免重复
        List<User> users = (List<User>) criteria.list();

//		for (User user : users) {
//			Hibernate.initialize(user.getUserProfile());
//		}

        return users;
    }

    @Override
    public List<User> findByType(String type) {
        return null;
    }

    public List<User> findDownUsers(String leaderId) {
        logger.info("leaderId : {}", leaderId);
        Criteria criteria = createEntityCriteria().add(Restrictions.eq("leaderId", leaderId));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<User> users  =  (List<User>) criteria.list();

//		for (User user : users) {
//			Hibernate.initialize(user.getUserProfile());
//		}

        return users;
    }

    public List<User> findAllDownUsers(String leaderId) {
        List<User> targetUsers = null;
        List<User> users1 = findDownUsers(leaderId);
        targetUsers = users1;

        for (int i = 0; i < users1.size(); i++) {
            List<User> users2 = findDownUsers(users1.get(i).getJobId());
            for (int j = 0; j < users2.size(); j++) {
                targetUsers.add(users2.get(j));
            }
        }

//        for (User user1 : users1) {
//            List<User> users2 = findDownUsers(user1.getJobId());
//            for (User user2 : users2) {
//                targetUsers.add(user2);
//            }
//        }

        return targetUsers;
    }

    public void save(User user) {
        persist(user);
    }

    public void deleteByJobId(String jobId) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("jobId", jobId));
        User user = (User)criteria.uniqueResult();
        delete(user);
    }

}
