package org.gcnc.calculate.excel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.multipart.Part;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ExcelServiceTest {

    private final String EXCEL_FILE_NAME = "test"; // Create this file
    private Path tempFilePath; // Store the path to the temporary file

    @InjectMocks
    private ExcelService excelService;

    @Test
    void readExcel_validRealFile_returnsListOfRows() throws IOException {
        // When
        writeValidExcelFile();
        Part filePart = createPartFromFile();

        // Then
        Mono<List<List<String>>> resultMono = excelService.readExcel(filePart);

        // Verify
        StepVerifier.create(resultMono)
                .assertNext(rows -> {
                    assertEquals(2, rows.size());
                    assertEquals(List.of("Real Value 1", "456.0"), rows.get(0));
                    assertEquals(List.of("false", "Real Value 2"), rows.get(1));
                })
                .verifyComplete();
    }

    @Test
    void readExcel_emptyRealFile_returnsEmptyList() throws IOException {

        // When
        writeEmptyFile();
        Part filePart = createPartFromFile();

        // Then
        Mono<@NonNull List<List<String>>> resultMono = excelService.readExcel(filePart);

        // Assert
        StepVerifier.create(resultMono)
                .assertNext(x -> assertEquals(0, x.size()))
                .verifyComplete();

    }

    @Test
    void readExcel_invalidRealFile_returnsEmptyList() throws IOException {
        // When
        writeInvalidExcelFile();
        Part filePart = createPartFromFile();

        // Then
        Mono<List<List<String>>> resultMono = excelService.readExcel(filePart);


        // Verify
        StepVerifier.create(resultMono)
                .assertNext(x -> assertEquals(0, x.size()))
                .verifyComplete();
    }

    @AfterEach
    public void cleanUp() throws IOException {
        Files.deleteIfExists(tempFilePath);
    }

    private void writeValidExcelFile() throws IOException {
        tempFilePath = Files.createTempFile(EXCEL_FILE_NAME, ".xlsx");
        // Create a sample Excel file for testing
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Sheet1");
            Row row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("Real Value 1");
            row1.createCell(1).setCellValue(456);
            Row row2 = sheet.createRow(1);
            row2.createCell(0).setCellValue(false);
            row2.createCell(1).setCellValue("Real Value 2");
            try (java.io.FileOutputStream outputStream = new java.io.FileOutputStream(tempFilePath.toFile())) {
                workbook.write(outputStream);
            }
        }
    }

    private void writeInvalidExcelFile() throws IOException {
        tempFilePath = Files.createTempFile(EXCEL_FILE_NAME, ".xlsx");
        Files.writeString(tempFilePath, "This is not an excel file");
    }

    private void writeEmptyFile() throws IOException {
        tempFilePath = Files.createTempFile(EXCEL_FILE_NAME, ".xlsx");
    }

    private Part createPartFromFile() throws IOException {
        byte[] bytes = Files.readAllBytes(tempFilePath);
        DataBuffer dataBuffer = new DefaultDataBufferFactory().wrap(bytes);
        return new Part() {
            @NotNull
            @Override
            public String name() {
                return "file";
            }

            @NotNull
            @Override
            public HttpHeaders headers() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM);
                return headers;
            }

            @NotNull
            @Override
            public Flux<DataBuffer> content() {
                return Flux.just(dataBuffer);
            }
        };
    }

}