package cn.smilex.vueblog.before.controller;

import cn.smilex.vueblog.common.entity.Result;
import cn.smilex.vueblog.common.service.StudentService;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.ProducesJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author smilex
 * @date 2022/11/12/10:32
 * @since 1.0
 */
@SuppressWarnings("unused")
@Component
public class HelloController {

    private StudentService studentService;

    @Autowired
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    @Get("/hello")
    public HttpResponse hello() {
        return HttpResponse.of(HttpStatus.OK, MediaType.PLAIN_TEXT_UTF_8, "hello");
    }

    @Get("/selectAllStudent")
    public HttpResponse selectAllStudent() {
        return HttpResponse.ofJson(studentService.list());
    }

    @Get("/resultReturnTest")
    @ProducesJson
    public Result<?> resultReturnTest() {
        return Result.success();
    }
}
