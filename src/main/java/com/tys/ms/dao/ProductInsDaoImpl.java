package com.tys.ms.dao;

import com.tys.ms.model.ProductIns;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("productInsDao")
public class ProductInsDaoImpl extends AbstractDao<Integer, ProductIns> implements ProductInsDao {

    static final Logger logger = LoggerFactory.getLogger(ProductInsDaoImpl.class);

    @Override
    public ProductIns findById(int id) {
        ProductIns productIns = getByKey(id);
        return null;
    }

    @Override
    public ProductIns findByEmployeeId(String employeeId) {
        logger.info("EmployeeId : {}", employeeId);
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("employeeId", employeeId));
        ProductIns productIns = (ProductIns) criteria.uniqueResult();
        return productIns;
    }

    @Override
    public void save(ProductIns productIns) {
        persist(productIns);
    }

    @Override
    public void deleteByEmployeeId(String employeeId) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("employeeId", employeeId));
        ProductIns productIns = (ProductIns)criteria.uniqueResult();
        delete(productIns);
    }

    @Override
    public List<ProductIns> findAllProductIns() {
        Criteria criteria = createEntityCriteria().addOrder(Order.asc("id"));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); //  避免重复
        List<ProductIns> productInsList = (List<ProductIns>) criteria.list();
        return productInsList;
    }

    @Override
    public List<ProductIns> findByType(String insType) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("insType", insType));
        List<ProductIns> productInsList = (List<ProductIns>) criteria.list();
        return productInsList;
    }
}
