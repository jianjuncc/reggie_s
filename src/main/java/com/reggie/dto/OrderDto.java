package com.reggie.dto;


import com.reggie.entity.OrderDetail;
import com.reggie.entity.Orders;
import lombok.Data;

import java.util.List;

/**
 * @author shu
 * @create 2022/5/3
 */
@Data
public class OrderDto extends Orders {

    private List<OrderDetail> orderDetails;
}
