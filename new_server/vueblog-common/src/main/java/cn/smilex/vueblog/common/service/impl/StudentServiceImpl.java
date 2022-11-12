package cn.smilex.vueblog.common.service.impl;

import cn.smilex.vueblog.common.dao.StudentDao;
import cn.smilex.vueblog.common.entity.Student;
import cn.smilex.vueblog.common.service.StudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author smilex
 * @date 2022/11/11/21:29
 * @since 1.0
 */
@SuppressWarnings("unused")
@Service
public class StudentServiceImpl extends ServiceImpl<StudentDao, Student> implements StudentService {
}
