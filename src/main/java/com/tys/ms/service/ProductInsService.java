package com.tys.ms.service;

import com.tys.ms.model.ProductIns;

import java.util.List;

public interface ProductInsService {
    ProductIns findById(int id);

    ProductIns findByEmployeeId(String employeeId);

    void save(ProductIns productIns);

    void deleteById(int id);

    List<ProductIns> findAllProductIns();

    List<ProductIns> findByType(String insType);
}
