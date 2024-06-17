import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable {
    private String text;
    private byte[] imageBytes;
    private String senderName;
    private String timestamp;

    public Message(String text, String senderName) {
        this.text = text;
        this.senderName = senderName;
        this.timestamp = getCurrentTime();
    }

    public Message(byte[] imageBytes, String senderName) {
        this.imageBytes = imageBytes;
        this.senderName = senderName;
        this.timestamp = getCurrentTime();
    }

    public boolean isText() {
        return text != null;
    }

    public boolean isImage() {
        return imageBytes != null;
    }

    public String getText() {
        return text;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}
