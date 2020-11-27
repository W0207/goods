package cn.edu.xmu.goods.mapper;

import cn.edu.xmu.goods.model.po.FlashItemPo;
import cn.edu.xmu.goods.model.po.FlashItemPoExample;
import java.util.List;

public interface FlashItemPoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table flash_sale_item
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table flash_sale_item
     *
     * @mbg.generated
     */
    int insert(FlashItemPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table flash_sale_item
     *
     * @mbg.generated
     */
    int insertSelective(FlashItemPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table flash_sale_item
     *
     * @mbg.generated
     */
    List<FlashItemPo> selectByExample(FlashItemPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table flash_sale_item
     *
     * @mbg.generated
     */
    FlashItemPo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table flash_sale_item
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(FlashItemPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table flash_sale_item
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(FlashItemPo record);
}