package com.semitop7.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

@Controller
public class MainController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/**/{path:[^\\.]+}")
    public String forward() {
        return "forward:/";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody
    String handleFileUpload(@RequestParam("name") String name,
                            @RequestParam("file") MultipartFile file) throws IOException {
        String result = new String(file.getBytes(), UTF_8);
        logger.info(result);
        return "forward:/";
    }
}