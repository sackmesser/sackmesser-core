package com.sackmesser.excel.web;

import com.sackmesser.excel.domain.ResultObject;
import com.sackmesser.excel.exceptions.NoDataFoundException;
import com.sackmesser.excel.service.IFileDownloadService;
import com.sackmesser.excel.service.IFileUploadService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ExcelAbstractController<T> {

    @RequestMapping(value = "/excel/upload", method = RequestMethod.GET)
    public
    @ResponseBody
    String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }

    @RequestMapping(value = "/excel/upload", method = RequestMethod.POST)
    @ResponseBody public ResultObject handleFileUpload(
            @RequestParam("flowChunkNumber") Integer flowChunkNumber,
            @RequestParam("flowChunkSize") Long flowChunkSize,
            @RequestParam("flowCurrentChunkSize") String flowCurrentChunkSize,
            @RequestParam("flowTotalSize") Long flowTotalSize,
            @RequestParam("flowIdentifier") String flowIdentifier,
            @RequestParam("flowFilename") String flowFilename,
            @RequestParam("flowRelativePath") String flowRelativePath,
            @RequestParam("flowTotalChunks") Integer flowTotalChunks,
            @RequestParam("file") MultipartFile file) throws Exception {

        if (!file.isEmpty()) {
            return getFileUploadService().execute(file.getInputStream());
        }

        throw new Exception("No file to be processed!");
    }

    /*// TODO think another way to send filters as parameters, maybe create a bean or use a map for this
    @RequestMapping(value = "/excel/download", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE )
    @ResponseBody public FileSystemResource export () throws IOException, IllegalAccessException {
        Map filter = new HashMap();
        List<Object> dataToSave = filterDataToDownload(filter);
        if(!CollectionUtils.isEmpty(dataToSave)){
            FileSystemResource fileResource = new FileSystemResource(getFileDownloadService().writeToFile(filterDataToDownload(filter)));
            return fileResource;
        }
        throw new NoDataFoundException();
    }*/

    // TODO think another way to send filters as parameters, maybe create a bean or use a map for this
    @RequestMapping(value = "/excel/download", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE )
    @ResponseBody public HttpEntity<byte[]> export () throws Exception {
        Map filter = new HashMap();
        List<T> dataToSave = filterDataToDownload(filter);
        if(!CollectionUtils.isEmpty(dataToSave)){
            byte[] fileBytes = getFileDownloadService().writeToFile(filterDataToDownload(filter));
            String fileName = getFileName();

            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            header.set("Content-Disposition",
                    "attachment; filename=" + fileName);
            header.setContentLength(fileBytes.length);

            return new HttpEntity(fileBytes, header);
        }
        throw new NoDataFoundException();
    }


    protected String getFileName(){
        SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy_hhmmssSSS");
        String fileName = formatter.format(new Date()) + ".xlsx";
        return fileName;
    }


    protected abstract IFileUploadService getFileUploadService();
    protected abstract IFileDownloadService getFileDownloadService();

    /**
     * Implement this method only when download feature will be used
     *  otherwise, it may return null
    */
    protected abstract List<T> filterDataToDownload(Map filter);

}