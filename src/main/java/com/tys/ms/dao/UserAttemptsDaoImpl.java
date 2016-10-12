package com.tys.ms.dao;

import com.tys.ms.model.UserAttempts;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;

@Repository("userAttemptsDao")
@Transactional
public class UserAttemptsDaoImpl extends AbstractDao<Integer, UserAttempts>{
    static final Logger logger = LoggerFactory.getLogger(UserAttemptsDaoImpl.class);

    public void updateFailAttempts(String username) {
        UserAttempts userAttempts = getUserAttempts(username);
        if (userAttempts == null) {
            UserAttempts newUserAttempt = new UserAttempts();
            newUserAttempt.setUsername(username);
            newUserAttempt.setAttempts(1);
            Date date = new Date();
            Timestamp timeStamp = new Timestamp(date.getTime());
            newUserAttempt.setLastModified(timeStamp);
            persist(newUserAttempt);
        } else {
            int attempt = userAttempts.getAttempts();
            int newAttempt = attempt + 1;
            userAttempts.setAttempts(newAttempt);
            update(userAttempts);
        }
    }

    public void resetFailAttempts(String username) {
        UserAttempts userAttempts = getUserAttempts(username);
        if (userAttempts == null) {

        } else {
            userAttempts.setAttempts(0);
            update(userAttempts);
        }
    }

    public UserAttempts getUserAttempts(String username) {
        logger.info("UserAttempts {}", username);
        try {
            Criteria criteria = createEntityCriteria();
            criteria.add(Restrictions.eq("username", username));
            UserAttempts userAttempts = (UserAttempts) criteria.uniqueResult();
            return userAttempts;
        } catch (Exception e) {
            logger.info("UserAttempts not found...");
            return null;
        }
    }

}
