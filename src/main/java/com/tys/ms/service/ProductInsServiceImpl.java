package com.tys.ms.service;

import com.tys.ms.dao.ProductInsDaoImpl;
import com.tys.ms.model.ProductIns;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service("productInsService")
@Transactional
public class ProductInsServiceImpl implements ProductInsService {

    @Autowired
    ProductInsDaoImpl productInsDao;

    @Override
    public ProductIns findById(int id) {
        return productInsDao.findById(id);
    }

    @Override
    public ProductIns findByEmployeeId(String employeeId) {
        return productInsDao.findByEmployeeId(employeeId);
    }

    @Override
    public void save(ProductIns productIns) {
        productInsDao.save(productIns);
    }

    @Override
    public void deleteByEmployeeId(String employeeId) {
        productInsDao.deleteByEmployeeId(employeeId);
    }

    @Override
    public List<ProductIns> findAllProductIns() {
        return productInsDao.findAllProductIns();
    }

    @Override
    public List<ProductIns> findByType(String insType) {
        return productInsDao.findByType(insType);
    }
}
