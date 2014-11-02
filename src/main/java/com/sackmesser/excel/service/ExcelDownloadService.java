package com.sackmesser.excel.service;

import org.springframework.batch.item.excel.transform.DefaultExcelItemWriter;
import org.springframework.core.io.CustomByteArrayResource;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Diogo
 * Date: 09/10/14
 * Time: 10:53
 */

@Service
public class ExcelDownloadService<T> implements IFileDownloadService<T>{

    @Override
    public byte[] writeToFile(List<T> objects) throws Exception {
        DefaultExcelItemWriter<T> itemWriter = new DefaultExcelItemWriter<>();
        CustomByteArrayResource resource = new CustomByteArrayResource();
        itemWriter.setResource(resource);
        itemWriter.write(objects);

        return resource.getByteArray();
    }
}
