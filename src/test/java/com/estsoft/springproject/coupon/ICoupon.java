package com.estsoft.springproject.coupon;

public interface ICoupon {

    public String getName();
    public boolean isValid();
    public int getDiscountPercent();
    public boolean isAppliable(Item item);
    public void doExpire();
}
