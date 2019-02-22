package cn.e3mall.sso.service.impl;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 单点登录系统
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private TbUserMapper tbUserMapper;
    @Autowired
    private JedisClient jedisClient;

    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;

    /**
     * 注册用户有效性校验
     *
     * @param param
     * @param type
     * @return
     */
    public E3Result checkData(String param, Integer type) {
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        if (type == 1) {
            criteria.andUsernameEqualTo(param);
        } else if (type == 2) {
            criteria.andPhoneEqualTo(param);
        } else {
            return E3Result.build(400, "传入类型错误！");
        }
        List<TbUser> list = tbUserMapper.selectByExample(example);
        if (list == null || list.size() == 0) {
            return E3Result.ok(true);
        }
        return E3Result.ok(false);
    }

    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    public E3Result register(TbUser user) {
        //非空校验
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword()) ||
                StringUtils.isBlank(user.getPhone())) {
            return E3Result.build(400, "用户注册失败！");
        }
        //有效性校验
        E3Result result = checkData(user.getUsername(), 1);
        if ((Boolean) result.getData() == false) {
            return E3Result.build(400, "用户名已被注册！");
        }
        result = checkData(user.getPhone(), 2);
        if ((Boolean) result.getData() == false) {
            return E3Result.build(400, "手机号已被注册！");
        }
        //补全属性
        user.setCreated(new Date());
        user.setUpdated(new Date());
        //md5加密密码
        String md5 = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5);
        //插入数据
        tbUserMapper.insert(user);
        return E3Result.ok();
    }

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @return
     */
    public E3Result login(String username, String password) {
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria().andUsernameEqualTo(username);
        List<TbUser> list = tbUserMapper.selectByExample(example);
        if (list == null || list.size() == 0) {
            return E3Result.build(400, "用户名或密码错误！");
        }
        TbUser user = list.get(0);
        if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
            return E3Result.build(400, "用户名或密码错误！");
        }
        String token = UUID.randomUUID().toString();
        user.setPassword(null);
        jedisClient.set("SESSION:" + token, JsonUtils.objectToJson(user));
        //设置session过期时间
        jedisClient.expire("SESSION:" + token, SESSION_EXPIRE);
        return E3Result.ok(token);
    }

    /**
     * 从session中通过token取用户信息
     *
     * @param token
     * @return
     */
    public E3Result getUserByToken(String token) {
        String json = jedisClient.get("SESSION:" + token);
        if (StringUtils.isBlank(json)) {
            return E3Result.build(201, "非登录状态！");
        }
        TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
        //刷新登陆状态
        jedisClient.expire("SESSION:" + token, SESSION_EXPIRE);
        return E3Result.ok(user);
    }

}
