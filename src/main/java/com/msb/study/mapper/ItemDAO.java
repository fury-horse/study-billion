package com.msb.study.mapper;

import com.msb.study.model.Item;
import com.msb.study.model.ItemExample;
import com.msb.study.model.ItemHtml;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ItemDAO继承基类
 */
@Repository
public interface ItemDAO extends MyBatisBaseDao<Item, Integer, ItemExample> {
    @Select("select * from item")
    List<ItemHtml> selectAllByItemHtml(ItemExample itemExample);
}