package guestbook.bean;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuestbookDTO {
    private int seq;
    private String name;
    private String email;
    private String homepage;
    private String subject;
    private String content;
    private Date logtime;

    @Override
    public String toString() {
        return seq + "\t"
                + name + "\t"
                + email + "\t"
                + homepage + "\t"
                + subject + "\t"
                + content;
    }
}






