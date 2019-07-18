package cn.e3mall.cart.service.impl;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车处理服务
 * <p>Title: CartServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p>
 *
 * @version 1.0
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private JedisClient jedisClient;
    @Value("${REDIS_CART_PRE}")
    private String REDIS_CART_PRE;
    @Autowired
    private TbItemMapper itemMapper;

    @Override
    public E3Result addCart(Long userId, Long itemId, Integer num) {
        jedisClient.hset(REDIS_CART_PRE, "1", "1");
        return E3Result.ok();
    }

    @Override
    public E3Result mergeCart(Long userId, List<TbItem> itemList) {
        //遍历商品列表
        //把列表添加到购物车。
        //判断购物车中是否有此商品
        //如果有，数量相加
        //如果没有添加新的商品
        for (TbItem tbItem : itemList) {
            addCart(userId, tbItem.getId(), tbItem.getNum());
        }
        //返回成功
        return E3Result.ok();
    }

    @Override
    public List<TbItem> getCartList(Long userId) {
        //根据用户id查询购车列表
        List<String> jsonList = jedisClient.hvals(REDIS_CART_PRE + ":" + userId);
        List<TbItem> itemList = new ArrayList<>();
        for (String string : jsonList) {
            //创建一个TbItem对象
            TbItem item = JsonUtils.jsonToPojo(string, TbItem.class);
            //添加到列表
            itemList.add(item);
        }
        return itemList;
    }

    @Override
    public E3Result updateCartNum(Long userId, Long itemId, Integer num) {
        //从redis中取商品信息
        String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
        //更新商品数量
        TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
        tbItem.setNum(num);
        //写入redis
        jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
        return E3Result.ok();
    }

    @Override
    public E3Result deleteCartItem(Long userId, Long itemId) {
        // 删除购物车商品
        jedisClient.hdel(REDIS_CART_PRE + ":" + userId, itemId + "");
        return E3Result.ok();
    }


}
