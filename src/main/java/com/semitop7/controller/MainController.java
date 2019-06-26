package com.semitop7.controller;

import com.semitop7.component.Slf4jMDCFilter;
import com.semitop7.db.entity.Client;
import com.semitop7.dto.model.Response;
import com.semitop7.dto.model.Response.ResponseBuilder;
import com.semitop7.dto.model.TransactionsDto;
import com.semitop7.service.FileService;
import com.semitop7.service.db.ClientService;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.unit.DataSize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

@Controller
public class MainController {
    @Autowired
    private FileService fileService;

    @Autowired
    private ClientService clientService;

    @Value("${upload.file.max.size.gb}")
    private long maxUploadGB;

    @RequestMapping("/")
    public String main(Model model) {
        model.addAttribute("maxUploadB", DataSize.ofGigabytes(maxUploadGB).toBytes());
        return "index";
    }

    @RequestMapping("/**/{path:[^\\.]+}")
    public String forward() {
        return "redirect:/";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity handleFileUpload(@RequestParam("files[]") MultipartFile[] files) throws IOException, XMLStreamException {
        List<Future<TransactionsDto>> results = new ArrayList<>(files.length);
        for (MultipartFile file : files) {
            results.add(fileService.parseMultipartFileAsync(file));
        }
        Set<Client> clients = fileService.parseTransactionsFuture(results);
        clientService.saveAll(clients);
        return ResponseEntity.ok(ResponseBuilder.aResponse()
                .error(false)
                .cid(MDC.get(Slf4jMDCFilter.DEFAULT_MDC_UUID_CID_KEY))
                .build());
    }

    @ExceptionHandler
    private ResponseEntity handleExeption(Exception ex) {
        Response response = ResponseBuilder.aResponse()
                .error(true)
                .cid(MDC.get(Slf4jMDCFilter.DEFAULT_MDC_UUID_CID_KEY))
                .errorName(ex.getClass().getCanonicalName())
                .errorMsg(ex.getMessage() == null ? "" : ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}