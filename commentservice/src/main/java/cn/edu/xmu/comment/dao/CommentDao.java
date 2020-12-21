package cn.edu.xmu.comment.dao;

import cn.edu.xmu.comment.model.bo.CommentInput;
import cn.edu.xmu.comment.model.po.CommentPo;
import cn.edu.xmu.comment.mapper.CommentPoMapper;
import cn.edu.xmu.comment.model.bo.Comment;
import cn.edu.xmu.comment.model.po.CommentPoExample;
import cn.edu.xmu.comment.model.vo.CommentAuditVo;
import cn.edu.xmu.comment.model.vo.CommentInputVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.otherinterface.service.OtherModulService;
import cn.edu.xmu.outer.service.IOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 菜鸡骞
 */
@Repository
public class CommentDao implements InitializingBean {

    @Autowired
    private CommentPoMapper commentPoMapper;

//    @DubboReference(version = "0.0.1", check = false)
//    private OtherModulService otherModulService;
//
//    @DubboReference(version = "0.0.1", check = false)
//    private IOrderService iOrderService;

    private static final Logger logger = LoggerFactory.getLogger(CommentDao.class);

    public ReturnObject<PageInfo<VoObject>> showCommentBySkuid(Integer page, Integer pageSize, Long id) {
        CommentPoExample example = new CommentPoExample();
        CommentPoExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsSkuIdEqualTo(id);
        criteria.andStateEqualTo((byte) 1);
        PageHelper.startPage(page, pageSize);
        List<CommentPo> commentPos = null;
        try {
            commentPos = commentPoMapper.selectByExample(example);
            List<VoObject> ret = new ArrayList<>(commentPos.size());
            for (CommentPo po : commentPos) {
                Comment com = new Comment(po);
                //com.setUserInfo(otherModulService.getUserInfo(po.getCustomerId()).getData());
                ret.add(com);
            }
            PageInfo<VoObject> rolePage = PageInfo.of(ret);
            rolePage.setPages((PageInfo.of(commentPos).getPages()));
            rolePage.setTotal((PageInfo.of(commentPos).getTotal()));
            rolePage.setPageSize(pageSize);
            rolePage.setPageNum(page);
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
        if (commentPo.getState() != 0) {
            logger.info("评论id= " + id + " 已被审核");
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);
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
        criteria.andStateEqualTo((byte) 1);
        PageHelper.startPage(page, pageSize);
        List<CommentPo> commentPos = null;
        try {
            commentPos = commentPoMapper.selectByExample(example);
            List<VoObject> ret = new ArrayList<>(commentPos.size());
            for (CommentPo po : commentPos) {
                Comment com = new Comment(po);
                //com.setUserInfo(otherModulService.getUserInfo(po.getCustomerId()).getData());
                ret.add(com);
            }
            PageInfo<VoObject> rolePage = PageInfo.of(ret);
            rolePage.setPages((PageInfo.of(commentPos).getPages()));
            rolePage.setPageNum(page);
            rolePage.setPageSize(pageSize);
            rolePage.setTotal((PageInfo.of(commentPos).getTotal()));
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
                //com.setUserInfo(otherModulService.getUserInfo(po.getCustomerId()).getData());
                ret.add(com);
            }
            PageInfo<VoObject> rolePage = PageInfo.of(ret);
            rolePage.setPages((PageInfo.of(commentPos).getPages()));
            rolePage.setPageNum(page);
            rolePage.setPageSize(pageSize);
            rolePage.setTotal((PageInfo.of(commentPos).getTotal()));
            return new ReturnObject<>(rolePage);
        } catch (DataAccessException e) {
            logger.error("showUnAuditCommentsByCommentid: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    public ReturnObject addComment(CommentInputVo commentInputVo, Long id, Long userId) {

        CommentPo commentPo = new CommentPo();
        commentPo.setState((byte) 0);
        commentPo.setType(commentInputVo.getType());
        commentPo.setOrderitemId(id);
        //commentPo.setGoodsSkuId(iOrderService.getSkuIdByOderItem(id).getData());
        commentPo.setGmtCreate(LocalDateTime.now());
        commentPo.setContent(commentInputVo.getContent());
        commentPo.setOrderitemId(id);

        CommentPoExample example = new CommentPoExample();
        CommentPoExample.Criteria criteria = example.createCriteria();
        criteria.andOrderitemIdEqualTo(id);
        ReturnObject<Comment> returnObject = null;
        if(commentInputVo.getType()!=0&&commentInputVo.getType()!=1&&commentInputVo.getType()!=2) {
            returnObject = new ReturnObject<>(ResponseCode.FIELD_NOTVALID);
            return returnObject;
        }
        if(commentPoMapper.selectByExample(example).size()!=0) {
            returnObject = new ReturnObject<>(ResponseCode.COMMENT_EXISTED);
            return returnObject;
        }
        try {
            int ret = commentPoMapper.insertSelective(commentPo);
            if (ret == 0) {
                //插入失败
                returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
            }
            else {
                //插入成功
                CommentInput commentInput = new CommentInput(commentPo);
                //commentInput.setCustomer(otherModulService.getUserInfo(commentPo.getCustomerId()).getData());
                returnObject = new ReturnObject(commentInput);
            }
        } catch (DataAccessException e) {
            returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
        return returnObject;


    }
}
