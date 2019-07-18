package cn.e3mall.cart.service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.pojo.TbItem;

import java.util.List;

public interface CartService {

	E3Result addCart(Long userId, Long itemId, Integer num);
	E3Result mergeCart(Long userId, List<TbItem> itemList);
	List<TbItem> getCartList(Long userId);
	E3Result updateCartNum(Long userId, Long itemId, Integer num);
	E3Result deleteCartItem(Long userId, Long itemId);
}
