package com.wgh.springcloud.customer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * hello controller
 *
 * @author wangguanghui
 */
@RestController
@ApiIgnore
public class HelloController {

    @GetMapping("/hello/{name}")
    public String index(@PathVariable("name") String name) {
        return "hello " + name + ", this is very pig";
    }
}
