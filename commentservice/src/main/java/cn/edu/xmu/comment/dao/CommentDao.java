package cn.edu.xmu.comment.dao;

import cn.edu.xmu.comment.model.po.CommentPo;
import cn.edu.xmu.comment.mapper.CommentPoMapper;
import cn.edu.xmu.comment.model.bo.Comment;
import cn.edu.xmu.comment.model.po.CommentPo;
import cn.edu.xmu.comment.model.po.CommentPoExample;
import cn.edu.xmu.comment.model.vo.CommentAuditVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 菜鸡骞
 */
@Repository
public class CommentDao implements InitializingBean {

    @Autowired
    private CommentPoMapper commentPoMapper;

    private static final Logger logger = LoggerFactory.getLogger(CommentDao.class);

    public ReturnObject<PageInfo<VoObject>> showCommentBySkuid(Integer page, Integer pageSize, Long id) {
        CommentPoExample example = new CommentPoExample();
        CommentPoExample.Criteria criteria = example.createCriteria();

        criteria.andGoodsSkuIdEqualTo(id);
        PageHelper.startPage(page, pageSize);
        List<CommentPo> commentPos = null;
        try {
            commentPos = commentPoMapper.selectByExample(example);
            List<VoObject> ret = new ArrayList<>(commentPos.size());
            for (CommentPo po : commentPos) {
                Comment com = new Comment(po);
                if (po.getState() == 1) {
                    ret.add(com);
                }
            }
            PageInfo<VoObject> rolePage = PageInfo.of(ret);
            PageInfo<CommentPo> commentPoPage = PageInfo.of(commentPos);
            PageInfo<VoObject> commentPage = new PageInfo<>(ret);
            commentPage.setPages(commentPoPage.getPages());
            commentPage.setPageNum(commentPoPage.getPageNum());
            commentPage.setPageSize(commentPoPage.getPageSize());
            commentPage.setTotal(commentPoPage.getTotal());
            return new ReturnObject<>(rolePage);
        } catch (DataAccessException e) {
            logger.error("showCommentBySkuid: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    public ReturnObject<Object> auditCommentByID(Long id, CommentAuditVo commentAuditVo) {
        CommentPo commentPo = commentPoMapper.selectByPrimaryKey(id);
        if (commentPo == null) {
            logger.info("评论id= " + id + " 不存在");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if(commentPo.getState()!=0)
        {
            logger.info("评论id= " + id +" 已被审核");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        Comment comment = new Comment(commentPo);
        CommentPo po = comment.createAuditPo(commentAuditVo);
        commentPoMapper.updateByPrimaryKeySelective(po);
        logger.info("commentid = " + id + " 的信息已更新");
        return new ReturnObject<>();
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public ReturnObject<PageInfo<VoObject>> showCommentByUserid(Integer page, Integer pageSize, Long userid) {
        CommentPoExample example = new CommentPoExample();
        CommentPoExample.Criteria criteria = example.createCriteria();
        criteria.andCustomerIdEqualTo(userid);
        PageHelper.startPage(page, pageSize);
        List<CommentPo> commentPos = null;
        try {
            commentPos = commentPoMapper.selectByExample(example);
            List<VoObject> ret = new ArrayList<>(commentPos.size());
            for (CommentPo po : commentPos) {
                Comment com = new Comment(po);
                if (po.getState() == 1) {
                    ret.add(com);
                }
            }
            PageInfo<VoObject> rolePage = PageInfo.of(ret);
            PageInfo<CommentPo> commentPoPage = PageInfo.of(commentPos);
            PageInfo<VoObject> commentPage = new PageInfo<>(ret);
            commentPage.setPages(commentPoPage.getPages());
            commentPage.setPageNum(commentPoPage.getPageNum());
            commentPage.setPageSize(commentPoPage.getPageSize());
            commentPage.setTotal(commentPoPage.getTotal());
            return new ReturnObject<>(rolePage);
        } catch (DataAccessException e) {
            logger.error("showCommentByUserid: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    public ReturnObject<PageInfo<VoObject>> showUnAuditCommentsByCommentid(Integer page, Integer pageSize, Integer state) {
        CommentPoExample example = new CommentPoExample();
        CommentPoExample.Criteria criteria = example.createCriteria();
        criteria.andStateEqualTo(Byte.valueOf(state.toString()));
        PageHelper.startPage(page, pageSize);
        List<CommentPo> commentPos = null;
        try {
            commentPos = commentPoMapper.selectByExample(example);
            List<VoObject> ret = new ArrayList<>(commentPos.size());
            for (CommentPo po : commentPos) {
                Comment com = new Comment(po);
                ret.add(com);
            }
            PageInfo<VoObject> rolePage = PageInfo.of(ret);
            PageInfo<CommentPo> commentPoPage = PageInfo.of(commentPos);
            PageInfo<VoObject> commentPage = new PageInfo<>(ret);
            commentPage.setPages(commentPoPage.getPages());
            commentPage.setPageNum(commentPoPage.getPageNum());
            commentPage.setPageSize(commentPoPage.getPageSize());
            commentPage.setTotal(commentPoPage.getTotal());
            return new ReturnObject<>(rolePage);
        } catch (DataAccessException e) {
            logger.error("showUnAuditCommentsByCommentid: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }
}
