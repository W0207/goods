package cn.edu.xmu.groupon.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限控制器
 **/
@Api(value = "团购服务", tags = "groupon")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/groupon", produces = "application/json;charset=UTF-8")
public class GrouponController {
}
