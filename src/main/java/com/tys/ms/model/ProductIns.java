package com.tys.ms.model;

import org.hibernate.validator.constraints.NotEmpty;
import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import java.io.Serializable;

@Entity
@Table(name="PRODUCT_INS")
public class ProductIns implements Serializable{

    private static final long serialVersionUID = -4666041285389731203L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    @Column(name="company", nullable=false)
    private String company;

    @NotEmpty
    @Column(name="employee", nullable=false)
    private String employee;

    @NotEmpty
    @Column(name="employee_id", nullable=false)
    private String employeeId;

    @NotEmpty
    @Column(name="ins_company", nullable=false)
    private String insCompany;

    @NotEmpty
    @Column(name="ins_type", nullable=false)
    private String insType;

    @NotEmpty
    @Column(name="ins_illustration", nullable=false)
    private String insIllustration;

    @Column(name="ins_person")
    private String insPerson;

    @Column(name="car_number")
    private String carNumber;

    @NotEmpty
    @Column(name="ins_time", nullable=false)
    private String insTime;

    @Column(name="car_type")
    private String carType;

    @DecimalMin("0")
    @Column(name="car_business_money")
    private String carBusinessMoney;

    @DecimalMin("0")
    @Column(name="car_mandatory_money")
    private String carMandatoryMoney;

    @DecimalMin("0")
    @Column(name="car_tax_money")
    private String carTaxMoney;

    @NotEmpty
    @DecimalMin("0")
    @Column(name="ins_money", nullable=false)
    private String insMoney;

    @NotEmpty
    @Column(name = "product_type", nullable=false)
    private String productType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getInsCompany() {
        return insCompany;
    }

    public void setInsCompany(String insCompany) {
        this.insCompany = insCompany;
    }

    public String getInsType() {
        return insType;
    }

    public void setInsType(String insType) {
        this.insType = insType;
    }

    public String getInsIllustration() {
        return insIllustration;
    }

    public void setInsIllustration(String insIllustration) {
        this.insIllustration = insIllustration;
    }

    public String getInsPerson() {
        return insPerson;
    }

    public void setInsPerson(String insPerson) {
        this.insPerson = insPerson;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getInsTime() {
        return insTime;
    }

    public void setInsTime(String insTime) {
        this.insTime = insTime;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCarBusinessMoney() {
        return carBusinessMoney;
    }

    public void setCarBusinessMoney(String carBusinessMoney) {
        this.carBusinessMoney = carBusinessMoney;
    }

    public String getCarMandatoryMoney() {
        return carMandatoryMoney;
    }

    public void setCarMandatoryMoney(String carMandatoryMoney) {
        this.carMandatoryMoney = carMandatoryMoney;
    }

    public String getCarTaxMoney() {
        return carTaxMoney;
    }

    public void setCarTaxMoney(String carTaxMoney) {
        this.carTaxMoney = carTaxMoney;
    }

    public String getInsMoney() {
        return insMoney;
    }

    public void setInsMoney(String insMoney) {
        this.insMoney = insMoney;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }
}
