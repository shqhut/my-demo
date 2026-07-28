package com.shq.demo.base64.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class PhotoExtractService {

    @Value("${app.photo.output-dir}")
    private String outputDir;

    @Value("${app.photo.csv-file-path}")
    private String csvFilePath;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private Executor photoExtractExecutor;

    private final AtomicInteger successCount = new AtomicInteger(0);
    private final AtomicInteger failCount = new AtomicInteger(0);

    /**
     * 初始化输出目录
     */
    @PostConstruct
    public void init() {
        try {
            // 处理输出目录 - 如果是 classpath 开头，转为 user.dir 下的路径
            Path outputPath;
            if (outputDir.startsWith("classpath:")) {
                String relativePath = outputDir.substring("classpath:".length());
                String userDir = System.getProperty("user.dir");
                outputPath = Paths.get(userDir, relativePath);
            } else {
                outputPath = Paths.get(outputDir);
            }

            // 确保输出目录存在
            if (!Files.exists(outputPath)) {
                Files.createDirectories(outputPath);
                log.info("创建输出目录：{}", outputPath.toAbsolutePath());
            }

            // 更新 outputDir 为实际路径
            this.outputDir = outputPath.toAbsolutePath().toString();
            log.info("输出目录：{}", this.outputDir);

        } catch (IOException e) {
            log.error("初始化输出目录失败", e);
        }
    }

    /**
     * 执行批量照片提取
     */
    public void extractPhotos() {
        Instant start = Instant.now();
        log.info("开始解析 CSV 文件：{}", csvFilePath);

        try {
            // 读取CSV内容并去除BOM头
            String csvContent = readCSVContentWithBOMRemoval();

            // 解析CSV内容
            List<CSVRecord> records = parseCSVContent(csvContent);

            if (records.isEmpty()) {
                log.warn("CSV 文件中没有数据记录");
                return;
            }

            log.info("共检测到 {} 条记录", records.size());

            // 使用 CountDownLatch 控制并发
            CountDownLatch latch = new CountDownLatch(records.size());

            // 提交所有任务
            for (CSVRecord record : records) {
                String zjhm = record.get("zjhm");
                String index = record.get("photo_index");
                String base64 = record.get("photo_base64");

                photoExtractExecutor.execute(() -> {
                    try {
                        savePhoto(zjhm, Integer.parseInt(index), base64);
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        failCount.incrementAndGet();
                        log.error("照片保存失败，zjhm={}, index={}, error={}",
                                zjhm, index, e.getMessage());
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();
            printResult(start);

        } catch (Exception e) {
            log.error("解析过程发生异常", e);
        }
    }

    /**
     * 读取CSV文件内容并去除BOM头
     */
    private String readCSVContentWithBOMRemoval() throws IOException {
        InputStream csvInputStream;
        if (csvFilePath.startsWith("classpath:")) {
            Resource resource = resourceLoader.getResource(csvFilePath);
            csvInputStream = resource.getInputStream();
        } else {
            csvInputStream = new FileInputStream(csvFilePath);
        }

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(csvInputStream, StandardCharsets.UTF_8))) {

            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    // 第一行去除BOM头
                    line = removeBOM(line);
                    isFirstLine = false;
                }
                content.append(line).append("\n");
            }
        }

        return content.toString();
    }

    /**
     * 去除字符串开头的BOM头
     */
    private String removeBOM(String str) {
        if (str != null && !str.isEmpty()) {
            // UTF-8 BOM: \uFEFF
            if (str.charAt(0) == '\uFEFF') {
                return str.substring(1);
            }
            // 其他可能的不可见字符（ASCII码小于32的控制字符，排除换行回车）
            char firstChar = str.charAt(0);
            if (firstChar < 32 && firstChar != '\r' && firstChar != '\n') {
                return str.substring(1);
            }
        }
        return str;
    }

    /**
     * 解析CSV内容为记录列表
     */
    private List<CSVRecord> parseCSVContent(String csvContent) throws IOException {
        List<CSVRecord> records = new ArrayList<>();

        try (Reader reader = new StringReader(csvContent);
             CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .withIgnoreHeaderCase()
                     .withTrim())) {

            // 验证表头是否存在
            if (parser.getHeaderMap() == null) {
                log.error("CSV文件没有表头");
                return records;
            }

            // 打印表头（调试用）
            log.info("CSV表头: {}", parser.getHeaderNames());

            // 读取所有记录
            for (CSVRecord record : parser) {
                records.add(record);
            }
        }

        return records;
    }

    /**
     * 保存照片到文件
     */
    private void savePhoto(String zjhm, int photoIndex, String base64) throws IOException {
        if (base64 == null || base64.isEmpty()) {
            throw new IllegalArgumentException("Base64 数据为空");
        }

        // 去掉可能的前缀
        if (base64.contains(",")) {
            base64 = base64.substring(base64.indexOf(",") + 1);
        }

        byte[] imageBytes = Base64.getDecoder().decode(base64);

        // 生成文件名
        String fileName;
        if (photoIndex <= 1) {
            fileName = zjhm + ".jpg";
        } else {
            fileName = zjhm + "_" + photoIndex + ".jpg";
        }

        // 保存文件
        Path filePath = Paths.get(outputDir, fileName);
        Files.write(filePath, imageBytes);

        log.debug("保存照片：{}", filePath.toAbsolutePath());
    }

    private void printResult(Instant start) {
        log.info("========== 执行结果 ==========");
        log.info("成功：{} 条", successCount.get());
        log.info("失败：{} 条", failCount.get());
        log.info("耗时：{} ms", Duration.between(start, Instant.now()).toMillis());
        log.info("==============================");
    }
}