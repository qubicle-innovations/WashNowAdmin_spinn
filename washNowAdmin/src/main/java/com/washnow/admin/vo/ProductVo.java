package com.washnow.admin.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by aswin on 23/02/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private double dryclean_sale_price;
    private double wash_sale_price;
    private double ironing_sale_price;
    private int washCount;
    private int dryCount;
    private int ironCount;
    private String type;
    private int quantity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDryclean_sale_price() {
        return dryclean_sale_price;
    }

    public void setDryclean_sale_price(double dryclean_sale_price) {
        this.dryclean_sale_price = dryclean_sale_price;
    }

    public double getWash_sale_price() {
        return wash_sale_price;
    }

    public void setWash_sale_price(double wash_sale_price) {
        this.wash_sale_price = wash_sale_price;
    }

    public double getIroning_sale_price() {
        return ironing_sale_price;
    }

    public void setIroning_sale_price(double ironing_sale_price) {
        this.ironing_sale_price = ironing_sale_price;
    }

    public int getWashCount() {
        return washCount;
    }

    public void setWashCount(int washCount) {
        this.washCount = washCount;
    }

    public int getDryCount() {
        return dryCount;
    }

    public void setDryCount(int dryCount) {
        this.dryCount = dryCount;
    }

    public int getIronCount() {
        return ironCount;
    }

    public void setIronCount(int ironCount) {
        this.ironCount = ironCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
