package cn.e3mall.item.pojo;

import cn.e3mall.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;

public class Item extends TbItem {
    public Item(TbItem tbItem){
        setBarcode(tbItem.getBarcode());
        setCid(tbItem.getCid());
        setCreated(tbItem.getCreated());
        setId(tbItem.getId());
        setImage(tbItem.getImage());
        setNum(tbItem.getNum());
        setPrice(tbItem.getPrice());
        setSellPoint(tbItem.getSellPoint());
        setStatus(tbItem.getStatus());
        setTitle(tbItem.getTitle());
        setUpdated(tbItem.getUpdated());
    }
    //将字符串转为数组传给页面
    public String[] getImages() {
        String images = super.getImage();
        if (StringUtils.isNotBlank(images)) {
            return images.split(",");
        } else {
            return null;
        }
    }
}
