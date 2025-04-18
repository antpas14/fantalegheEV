package org.gcnc.calculate.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ExcelService {

    public Mono<List<List<String>>> readExcel(Part file) {
        return file.content()
                .reduce((buffer1, buffer2) -> {
                    buffer1.write(buffer2);
                    return buffer1;
                })
                .map(dataBuffer -> {
                    List<List<String>> rows = new ArrayList<>();
                    try (InputStream is = dataBuffer.asInputStream();
                        Workbook workbook = new XSSFWorkbook(is)) {

                        Sheet sheet = workbook.getSheetAt(0);
                        for (Row row : sheet) {
                            List<String> rowData = new ArrayList<>();
                            for (Cell cell : row) {
                                cell.setCellType(CellType.STRING);
                                rowData.add(cell.getStringCellValue());
                            }
                            rows.add(rowData);
                        }
                    } catch (Exception e) {
                        log.error("Error parsing excel file ", e);
                    }
                    return rows;
                });
    }


}