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
import org.springframework.util.CollectionUtils;
import org.springframework.util.unit.DataSize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

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
        if (files.length == 0) {
            return bad("Must be present one or more files.");
        }
        for (MultipartFile file : files) {
            results.add(fileService.parseMultipartFileAsync(file));
        }
        Set<Client> clients = fileService.parseTransactions(results);
        List<Client> result = clientService.saveAll(clients);
        return CollectionUtils.isEmpty(result) ? bad("Data was not write to DB") : ok();
    }

    @ExceptionHandler
    private ResponseEntity handleExeption(Exception ex) {
        return response(true,
                BAD_REQUEST,
                ex.getClass().getCanonicalName(),
                ex.getMessage());
    }

    private ResponseEntity bad(String message) {
        return response(true,
                BAD_REQUEST,
                null,
                message);
    }

    private ResponseEntity ok() {
        return response(false,
                OK,
                null,
                null);
    }

    private ResponseEntity response(boolean isError,
                                    HttpStatus code,
                                    String name,
                                    String message) {
        Response response = ResponseBuilder.aResponse()
                .error(isError)
                .cid(MDC.get(Slf4jMDCFilter.DEFAULT_MDC_UUID_CID_KEY))
                .errorName(name == null ? "" : name)
                .errorMsg(message == null ? "" : message)
                .build();
        return ResponseEntity.status(code).body(response);
    }
}