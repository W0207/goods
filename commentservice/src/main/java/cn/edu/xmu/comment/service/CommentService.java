package cn.edu.xmu.comment.service;

import cn.edu.xmu.comment.dao.CommentDao;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class CommentService {

    @Autowired
    private CommentDao commentDao;

    public ReturnObject<PageInfo<VoObject>> showCommentBySkuid(Integer page, Integer pageSize, Long id) {
        return commentDao.showCommentBySkuid(page, pageSize, id);
    }
}
