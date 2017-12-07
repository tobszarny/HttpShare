package pl.biltech.httpshare.util;

public class MimeUtil {
    public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
    public static final String IMAGE_PNG = "image/png";
    public static final String TEXT_HTML = "text/html";
    public static final String TEXT_HTML_CHARSET_UTF_8 = "text/html; charset=utf-8";
    public static final String TEXT_CSS = "text/css";
    public static final String TEXT_JAVASCRIPT = "text/javascript";

    private MimeUtil() {
    }

    public static String classifyMimeAfterFileName(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        switch (extension) {
            case "png":
            case "jpg":
            case "jpeg":
            case "ico":
            case "bmp":
            case "gif":
                return "image/" + extension;
            case "js":
                return TEXT_JAVASCRIPT;
            case "css":
                return TEXT_CSS;
            case "htm":
            case "html":
            case "htmls":
                return TEXT_HTML_CHARSET_UTF_8;
            default:
                return APPLICATION_OCTET_STREAM;
        }
    }
}