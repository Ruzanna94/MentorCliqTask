package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;

@Controller
public class UploadController {

    private static String UPLOADED_FOLDER = System.getProperty("java.io.tmpdir");
    @Autowired
    private FileUploadForm fileUploadForm;

    @GetMapping("/")
    public String index() {
        return "upload";
    }

    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
        }

        try {
            fileUploadForm.clearData();
            FileInputStream stream = new FileInputStream(
                    new File(UPLOADED_FOLDER + File.separator + file.getOriginalFilename()));
            fileUploadForm.readFile(stream);
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/recommendationPage";
    }

    @GetMapping("/recommendationPage")
    public String uploadStatus() {

        return "recommendationPage";
    }

}