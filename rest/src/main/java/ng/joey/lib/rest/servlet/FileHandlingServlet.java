package ng.joey.lib.rest.servlet;

import com.google.api.server.spi.ServiceException;
import com.google.api.server.spi.response.BadRequestException;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ng.joey.lib.java.security.Crypto;
import ng.joey.lib.java.util.Value;
import ng.joey.lib.rest.util.JsonUtils;

/**
 * Created by root on 5/18/17.
 */

public abstract class FileHandlingServlet extends EndpointServlet{

    private static final Logger logger = Logger.getLogger(FileHandlingServlet.class.getSimpleName());

    public static final class Constants {

        public static final int BUFFER_SIZE = 2 * 1024 * 1024;
        public static final String gcsUrl = "https://storage.googleapis.com/";

    }

    private static final GcsService GCS_SERVICE;

    static {
        GCS_SERVICE = GcsServiceFactory.createGcsService(new RetryParams.Builder()
                .initialRetryDelayMillis(10)
                .retryMaxAttempts(10)
                .totalRetryPeriodMillis(15000)
                .build());
    }

    public static GcsService getGCSService(){
        return GCS_SERVICE;
    }


    public String getFile(HttpServletRequest req, String fileName, String bucketName, boolean publicRead, boolean gcsUrl) throws IOException, BadRequestException{
        if(!itemMap.containsKey(fileName))
            return null;
        try {

            return handleGCSUpload(itemMap.get(fileName).bytes, bucketName, itemMap.get(fileName).contentType, publicRead, gcsUrl);

//            ServletFileUpload upload = new ServletFileUpload();
//            FileItemIterator iter = upload.getItemIterator(req);
//            logger.info("Getting file: "+fileName);
//            while (iter.hasNext()) {
//                FileItemStream item = iter.next();
//                if(item.getFieldName().matches(fileName)){
//                    logger.info("Found matching filename");
//                    String contentType = item.getContentType();
//                    InputStream stream = IOUtils.toBufferedInputStream(item.openStream());
//                    return handleGCSUpload(stream, bucketName, contentType, publicRead, gcsUrl);
//                }
//            }
        } catch (Exception ex) {
            BadRequestException exception = new BadRequestException("bad request :" +ex.getMessage());
            logger.info("exception getting file "+fileName + ": "+exception.getMessage());
            throw exception;
        }
    }

    private Map<String, UploadItem> itemMap = new HashMap<>();

    public class UploadItem{
        public String contentType;
        public byte[] bytes;

        public UploadItem(String contentType, byte[] bytes) {
            this.contentType = contentType;
            this.bytes = bytes;
        }
    }

    public void initialize(HttpServletRequest req){
        try {
            ServletFileUpload upload = new ServletFileUpload();
            FileItemIterator iter = upload.getItemIterator(req);
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                byte[] bytes = IOUtils.toByteArray(item.openStream());
                String contentType = new Tika().detect(bytes);
                itemMap.put(item.getFieldName(), new UploadItem(contentType, bytes));
            }
        } catch (Exception ex) {

        }
    }

    public <T> T getObject(Class<T> tClass, HttpServletRequest request, String objectName) throws BadRequestException {
        if(!itemMap.containsKey(objectName))
            return null;

        try {

            String string = IOUtils.toString(itemMap.get(objectName).bytes);
            return JsonUtils.getBuilder().create().fromJson(string, tClass);

//            ServletFileUpload upload = new ServletFileUpload();
//            FileItemIterator iter = upload.getItemIterator(request);
//            logger.info("Getting object: "+objectName);
//            while (iter.hasNext()) {
//                FileItemStream item = iter.next();
//                if(item.getFieldName().matches(objectName)){
//                    logger.info("Found matching ");
//                    InputStream stream = IOUtils.toBufferedInputStream(item.openStream());
//                    return JsonUtils.fromJson(IOUtils.toString(stream), tClass);
//                }
//            }
        } catch (Exception ex) {
            BadRequestException exception = new BadRequestException("bad request :" +ex.getMessage());
            logger.info("exception getting file "+objectName + ": "+exception.getMessage());
            throw exception;
        }
        //logger.info("file not found: "+objectName);
        //return null;
    }

    private String handleGCSUpload(byte[] bytes, String bucketName, String contentType, boolean publicRead, boolean gcsUrl) throws IOException {
        MimeTypes all = MimeTypes.getDefaultMimeTypes();
        MimeType type = null;
        String filePath =  contentType;
        try {
            type = all.forName(contentType.toLowerCase());
            filePath = type.getName();
        } catch (MimeTypeException e) {
            e.printStackTrace();
        }
        String ext = Value.IS.nullValue(type) ? "" : type.getExtension();
        String uuid = Crypto.Random.uuidClear();

        if(!filePath.startsWith("/"))
            filePath = "/"+filePath;
        if(filePath.endsWith("/"))
            filePath = filePath.substring(0, filePath.length() - 2);

        logger.info("File path is : "+filePath);

        String folder = bucketName + filePath;

        logger.info("Folder is "+folder);

        GcsFilename gcsFilename = new GcsFilename(folder, uuid + ext);
        GcsFileOptions.Builder builder = new GcsFileOptions.Builder();
        if(publicRead)
            builder.acl("public-read");
        GcsFileOptions options = builder.build();
        GcsOutputChannel gcsOutputChannel = getGCSService().createOrReplace(gcsFilename, options);
        handleIo(bytes, Channels.newOutputStream(gcsOutputChannel));
        if(!gcsUrl) {
            String filePath_ = filePath;
            if(filePath_.startsWith("/"))
                filePath_ = filePath_.substring(1, filePath_.length());
            return filePath_ + "/" + gcsFilename.getObjectName();
        }
        return Constants.gcsUrl + bucketName + filePath + "/" + gcsFilename.getObjectName();
    }

    private void handleIo(byte[] bytes, OutputStream o) throws IOException {
        ByteArrayInputStream bos = new ByteArrayInputStream(bytes);
        IOUtils.copy(bos, o);
        IOUtils.closeQuietly(bos);
        IOUtils.closeQuietly(o);
    }

    public void close(HttpServletResponse response, ServiceException e) throws IOException {
        response.setStatus(e.getStatusCode());
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        writer.println(e.getMessage());
        writer.flush();
        writer.close();
    }

    public void close(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(503);
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        writer.println(e.getMessage());
        writer.flush();
        writer.close();
    }

    public static void delete(GcsFilename filename){
        try {
            getGCSService().delete(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delete(String bucketName, String url) {
        try {
            if (Value.IS.emptyValue(url))
                return;
            String filePath = url.replace(Constants.gcsUrl, "");
            String fileName = filePath.replace(bucketName, "").replaceFirst("/", "");
            logger.info("File name: "+fileName);
            delete(new GcsFilename(bucketName, fileName));
        }catch (Exception ignored){

        }
    }

}
