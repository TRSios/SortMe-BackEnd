package com.sortme.sortme.controller;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class MainController {

    private static final String FILE_PATH = "/Users/tarasios/Downloads/JustFile.txt";

    @GetMapping("/ping")
    public ResponseEntity<Boolean> ping() {
        return ResponseEntity.ok(true);
    }

    @SneakyThrows(IOException.class)
    @PostMapping("/file-download")
    public ResponseEntity<Boolean> downloadFile() {
        List<String> fileContent = new ArrayList<>();

        for (Integer variableLoopCounter = 0; variableLoopCounter < 1000000; variableLoopCounter++) {
            fileContent.add(String.valueOf(new Random().nextInt()));
        }

        writeToFile(fileContent);

        return ResponseEntity.ok(true);
    }

    @SneakyThrows(IOException.class)
    @PostMapping("/file-upload")
    public ResponseEntity<Long> uploadFile(MultipartFile file) {
        List<String> fileContent = readFromFile(file.getInputStream());

        int[] integerList = fileContent.stream()
                .map(Integer::parseInt)
                .mapToInt(i -> i)
                .toArray();

        StopWatch watch = new StopWatch();
        watch.start();
        cocktailSort(integerList);
        watch.stop();

        fileContent.clear();

        for (Integer variableLoopCounter = 0; variableLoopCounter < integerList.length; variableLoopCounter++) {
            fileContent.add(String.valueOf(integerList[variableLoopCounter]));
        }

        writeToFile(fileContent);

        return ResponseEntity.ok(watch.getTime());
    }

    private static void cocktailSort(int[] array) {
        boolean isSwapped = true;
        int begin = 0, i, variable;
        int end = array.length - 1;

        while (isSwapped) {
            isSwapped = false;

            for (i = begin; i < end; ++i) {
                if (array[i] > array[i + 1]) {
                    variable = array[i];
                    array[i] = array[i + 1];
                    array[i + 1] = variable;
                    isSwapped = true;
                }
            }

            if (!isSwapped)
                break;

            isSwapped = false;

            for (i = end - 1; i >= begin; --i) {
                if (array[i] > array[i + 1]) {
                    variable = array[i];
                    array[i] = array[i + 1];
                    array[i + 1] = variable;
                    isSwapped = true;
                }
            }

            ++begin;
        }
    }

    private List<String> readFromFile(InputStream inputStream) throws IOException {
        return IOUtils.readLines(inputStream, "UTF-8");
    }

    private void writeToFile(List<String> fileContent) throws IOException {
        Files.write(Paths.get(FILE_PATH), fileContent);
    }

}
