package com.semitop7.command;

import com.semitop7.db.entity.Client;
import com.semitop7.model.TransactionsDto;
import com.semitop7.service.FileService;
import com.semitop7.service.db.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.util.CollectionUtils;
import org.springframework.util.unit.DataSize;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

@ShellComponent
@ShellCommandGroup("Upload file")
public class MainCommand {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${upload.file.max.size.gb}")
    private long maxUploadGb;

    @Autowired
    private FileService fileService;

    @Autowired
    private ClientService clientService;

    @ShellMethod(value = "Script for upload files", group = "Upload file")
    public void uploadFile(@ShellOption(
            value = {"--files"},
            help = "Input path to file." +
                    " Also you can input paths do different files." +
                    " Paths must be separated by coma." +
                    " Sample: --files \"\\tmp\\file1.xml,\\tmp\\file2.xml\"") String[] files)
            throws IOException, XMLStreamException {
        List<Future<TransactionsDto>> results = new ArrayList<>(files.length);
        if (files.length == 0) {
            LOGGER.error("Must be present one or more files.");
            return;
        }
        for (String name : files) {
            if (!fileService.isValidFileSize(name, DataSize.ofGigabytes(maxUploadGb).toBytes())) {
                LOGGER.error(String.format("File size must be less then %d GB, file name: %s", maxUploadGb, name));
                return;
            }
        }
        for (String name : files) {
            results.add(fileService.parseFileAsync(name));
        }
        Set<Client> clients = fileService.parseTransactions(results);
        List<Client> result = clientService.saveAll(clients);
        if (CollectionUtils.isEmpty(result)) {
            LOGGER.error("Data was not write to DB");
        } else {
            LOGGER.info("Data was stored to DB");
        }
    }
}