package org.gcnc.calculate.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class ExcelService {

    public Mono<List<List<String>>> readExcel(Part file) {
        return file.content()
                .single()
                .map(dataBuffer -> {
                    List<List<String>> rows = new ArrayList<>();
                    try (InputStream is = dataBuffer.asInputStream();
                        Workbook workbook = new XSSFWorkbook(is)) {

                        Sheet sheet = workbook.getSheetAt(0);
                        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(sheet.iterator(), Spliterator.ORDERED), false)
                                .map(this::processRow)
                                .toList();
                    } catch (Exception e) {
                        log.error("Error parsing excel file ", e);
                    }
                    return rows;
                });
    }

    private List<String> processRow(Row row) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                        row.cellIterator(), Spliterator.ORDERED), false)
                .map(cell -> {
                    if (CellType.STRING.equals(cell.getCellType())) {
                        return cell.getStringCellValue();
                    } else if (CellType.NUMERIC.equals(cell.getCellType())) {
                        return String.valueOf(cell.getNumericCellValue());
                    } else if (CellType.BOOLEAN.equals(cell.getCellType())) {
                        return String.valueOf(cell.getBooleanCellValue());
                    } else {
                        return "";
                    }
                })
                .toList();
    }
}