package com.tys.ms.dao;

import com.tys.ms.model.UserProfile;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository("userProfileDao")
public class UserProfileDaoImpl extends AbstractDao<Integer, UserProfile> implements UserProfileDao {

	public UserProfile findById(int id) {
		return getByKey(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserProfile> findDown(int upId) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.gt("id", upId));
        return (List<UserProfile>)criteria.list();
	}

	public UserProfile findByType(String type) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("type", type));
		UserProfile userProfile = (UserProfile) criteria.uniqueResult();
		return (UserProfile) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<UserProfile> findAll(){
		Criteria criteria = createEntityCriteria();
		criteria.addOrder(Order.asc("type"));
		return (List<UserProfile>)criteria.list();
	}

    @SuppressWarnings("unchecked")
    public Set<UserProfile> findAllSet(){
        Set<UserProfile> set = null;
        set.add(getByKey(1));
        set.add(getByKey(2));
        set.add(getByKey(3));
        set.add(getByKey(4));
        return set;
    }
}
