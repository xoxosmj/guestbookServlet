package guestbook.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import guestbook.bean.GuestbookDTO;
import guestbook.dao.GuestbookDAO;

@WebServlet("/list")
public class GuestbookListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //데이터
        int pg = Integer.parseInt(request.getParameter("pg"));

        //1페이지당 3개씩
        int endNum = pg * 3;
        int startNum = endNum - 2;

        //DB
        GuestbookDAO guestbookDAO = GuestbookDAO.getInstance();
        List<GuestbookDTO> list = guestbookDAO.getAllGuestbooks(startNum, endNum); //부모 = 자식
        System.out.println(list);

        //응답
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<body>");

        if(list != null) {
            for(GuestbookDTO guestbookDTO : list) {
                out.println("<table border='1'>");
                out.println("<tr>");
                out.println("<th width='100'>작성자</th>");
                out.println("<td width='150' align='center'>" + guestbookDTO.getName() + "</td>");
                out.println("<th width='100'>작성일</th>");
                out.println("<td width='150' align='center'>" + guestbookDTO.getLogtime() + "</td>");
                out.println("</tr>");

                out.println("<tr>");
                out.println("<th>이메일</th>");
                out.println("<td colspan='3'>" + guestbookDTO.getEmail() + "</td>");
                out.println("</tr>");

                out.println("<tr>");
                out.println("<th>홈페이지</th>");
                out.println("<td colspan='3'>" + guestbookDTO.getHomepage() + "</td>");
                out.println("</tr>");

                out.println("<tr>");
                out.println("<th>제목</th>");
                out.println("<td colspan='3'>" + guestbookDTO.getSubject() + "</td>");
                out.println("</tr>");

                out.println("<tr>");
                out.println("<td colspan='4'><pre>" + guestbookDTO.getContent() + "</pre></td>");
                out.println("</tr>");
                out.println("</table>");
                out.println("<hr/>");
            }//for
        }//if

        out.println("</html>");
        out.println("</body>");
    }

}





