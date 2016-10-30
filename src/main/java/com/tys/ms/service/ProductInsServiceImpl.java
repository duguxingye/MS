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
    public void update(ProductIns productIns) {
        ProductIns entity = productInsDao.findById(productIns.getId());
        if(entity != null) {
            entity.setCompany(productIns.getCompany());
            entity.setInsCompany(productIns.getInsCompany());
            entity.setProductType(productIns.getProductType());
            entity.setInsIllustration(productIns.getInsIllustration());
            entity.setInsPerson(productIns.getInsPerson());
            entity.setCarNumber(productIns.getCarNumber());
            entity.setInsTime(productIns.getInsTime());
            entity.setCarBusinessMoney(productIns.getCarBusinessMoney());
            entity.setCarMandatoryMoney(productIns.getCarMandatoryMoney());
            entity.setCarTaxMoney(productIns.getCarTaxMoney());
            entity.setInsMoney(productIns.getInsMoney());
        }
    }

    @Override
    public void deleteById(int id) {
        productInsDao.deleteById(id);
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
