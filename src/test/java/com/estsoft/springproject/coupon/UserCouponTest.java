package com.estsoft.springproject.coupon;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserCouponTest {
    @Test
    public void testAddCoupon() {
        User user = new User("area00");
        assertEquals(0, user.getTotalCouponCount()); // 쿠폰 수령 전

        ICoupon coupon = new DummyCoupon();

        user.addCoupon(coupon);
        assertEquals(1, user.getTotalCouponCount()); // 쿠폰 수령 후 쿠폰수 검증
    }

    @Test
    public void 쿠폰이_유효한_경우에만_유저에게_발급() {
        User user = new User("area00");
        assertEquals(0, user.getTotalCouponCount()); // 쿠폰 수령 전

        ICoupon coupon = Mockito.mock(ICoupon.class);
        BDDMockito.given(coupon.isValid())
                        .willReturn(true);

        user.addCoupon(coupon);
        assertEquals(1, user.getTotalCouponCount()); // 쿠폰 수령 후 쿠폰수 검증
    }

    @Test
    public void 쿠폰이_유효하지_않는_경우() {
        User user = new User("area00");
        assertEquals(0, user.getTotalCouponCount()); // 쿠폰 수령 전

        ICoupon coupon = Mockito.mock(ICoupon.class);
        BDDMockito.given(coupon.isValid())
                .willReturn(false);

        user.addCoupon(coupon);
        assertEquals(0, user.getTotalCouponCount()); // 쿠폰 수령 후 쿠폰수 검증
    }

}
