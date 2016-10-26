package com.tys.ms.dao;

import com.tys.ms.model.User;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
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
        if(user != null){
            Hibernate.initialize(user.getUserProfile());
        }
        return user;
    }

    @SuppressWarnings("unchecked")
    public List<User> findAllUsers() {
        Criteria criteria = createEntityCriteria().addOrder(Order.asc("name"));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); //  避免重复
        List<User> targetUsers = (List<User>) criteria.list();
        return targetUsers;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> findByType(String type) {
        String typeInSql = "\'" + type +"\'";
        String querySql = "SELECT u.job_id FROM ms.app_user AS u JOIN ms.app_user_user_profile AS up ON (u.id = up.user_id) JOIN ms.user_profile AS p ON (p.user_profile_id = p.id) WHERE p.type =" + typeInSql;
        List<String> jobIdList = (List<String>) getSession().createNativeQuery(querySql).list();
        List<User> targetUsers = new ArrayList<>();
        for (int i = 0; i < jobIdList.size(); i++) {
            User users = findByJobID(jobIdList.get(i));
            targetUsers.add(users);
        }
        return targetUsers;
    }

    @SuppressWarnings("unchecked")
    public List<User> findDownUsers(String leaderId) {
        logger.info("leaderId : {}", leaderId);
        Criteria criteria = createEntityCriteria().add(Restrictions.eq("leaderId", leaderId));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<User> targetUsers  =  (List<User>) criteria.list();
        return targetUsers;
    }

    @SuppressWarnings("unchecked")
    public List<User> findAllDownUsers(String leaderId) {
        String leaderIdInSql = "\'" + leaderId +"\'";
        String querySql = "select distinct c.job_id from ms.app_user a inner join ms.app_user b on a.job_id=b.leader_id or a.job_id=b.job_id inner join ms.app_user c on b.job_id=c.leader_id or b.job_id=c.job_id where a.job_id=" + leaderIdInSql;
        List<String> jobIdList = (List<String>) getSession().createNativeQuery(querySql).list();
        List<User> targetUsers = new ArrayList<>();
        for (int i = 0; i < jobIdList.size(); i++) {
            User users = findByJobID(jobIdList.get(i));
            targetUsers.add(users);
        }
        return targetUsers;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> findAllDownJobId(String jobId) {
        String leaderIdInSql = "\'" + jobId +"\'";
        String querySql = "select distinct c.job_id from ms.app_user a inner join ms.app_user b on a.job_id=b.leader_id or a.job_id=b.job_id inner join ms.app_user c on b.job_id=c.leader_id or b.job_id=c.job_id where a.job_id=" + leaderIdInSql;
        List<String> jobIdList = (List<String>) getSession().createNativeQuery(querySql).list();
        return jobIdList;
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
