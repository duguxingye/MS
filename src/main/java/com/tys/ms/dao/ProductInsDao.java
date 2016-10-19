package com.tys.ms.dao;

import com.tys.ms.model.ProductIns;
import java.util.List;


public interface ProductInsDao {
    ProductIns findById(int id);

    ProductIns findByEmployeeId(String employeeId);

    void save(ProductIns productIns);

    void deleteByEmployeeId(String employeeId);

    List<ProductIns> findAllProductIns();

    List<ProductIns> findByType(String insType);
}
