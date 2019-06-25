package com.ch116221.document.server.controller;

import com.ch116221.document.server.domains.ConnectionInstance;
import com.ch116221.document.server.params.Param;
import com.ch116221.document.server.services.DocumentService;
import com.ch116221.document.server.utils.FileUtil;
import com.ch116221.document.server.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/document")
public class DocumentController {

    private final DocumentService service;

    public DocumentController(DocumentService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public Result<?> create(@RequestBody Param param) {
        return service.createDocument(param.getTables(), param.getInstance());
    }

    @PostMapping("/test")
    public Result<?> test(@RequestBody ConnectionInstance param) {
        return service.test(param);
    }

    @PostMapping("/getTables")
    public Result<?> getTables(@RequestBody ConnectionInstance param) {
        Result<Map<String, String>> result = service.getTableNameAndCommit(param, true);
        if (!result.isSuccess()) {
            return Result.fail(result.getMsg());
        }

        Map<String, String> data = result.getData();
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            list.add(entry.getKey() + ":" + entry.getValue());
        }
        return Result.success(list);
    }


    @GetMapping("/download")
    public void download(@RequestParam String fileName, HttpServletResponse response) {
        try {
            // 确保路径参数正确解码
            fileName = UriUtils.decode(fileName, StandardCharsets.UTF_8);

            log.info("下载文件:{}", fileName);
            Path filePath = Paths.get(FileUtil.TEMP_DIR).resolve(fileName).normalize();
            File file = filePath.toFile();

            // 验证文件是否存在
            if (!file.exists()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("文件未找到");
                return;
            }

            // 设置响应头
            response.setContentType("application/octet-stream");

            // 对文件名进行 URL 编码，确保浏览器正确显示中文文件名
            String encodedFileName = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8.toString());
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
            response.setContentLengthLong(file.length());

            try (OutputStream stream = response.getOutputStream(); InputStream in = Files.newInputStream(file.toPath())) {
                byte[] buffer = new byte[8 * 1024];
                int length;
                while ((length = in.read(buffer)) != -1) {
                    stream.write(buffer, 0, length);
                }
                stream.flush();
                log.info("文件下载完成");
            } catch (IOException e) {
                log.error(e.getMessage());
                response.reset();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("服务器内部错误");
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try {
                response.getWriter().write("请求错误");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

}

