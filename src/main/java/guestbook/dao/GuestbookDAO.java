package guestbook.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import guestbook.bean.GuestbookDTO;

public class GuestbookDAO {
    private String driver = "oracle.jdbc.driver.OracleDriver";
    private String url = "jdbc:oracle:thin:@localhost:1521:xe";
    private String username = "c##java";
    private String password = "1234";

    private Connection con;
    private PreparedStatement pstmt;
    private ResultSet rs;

    private static GuestbookDAO guestbookDAO = new GuestbookDAO();

    public static GuestbookDAO getInstance() {
        return guestbookDAO;
    }

    public GuestbookDAO() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void getConnection() {
        try {
            con = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            con=null;
        }
    }

    public void guestbookWrite(GuestbookDTO guestbookDTO) {
        getConnection();
        if (con == null) {
            System.err.println("데이터베이스 연결에 실패했습니다.");
            // 추가적인 오류 처리 로직을 넣을 수 있습니다.
            return; // 연결 실패 시 메서드 종료
        }

        String sql = """
			insert into guestbook values(seq_guestbook.nextval, ?,?,?,?,?, sysdate)
				""";

        try {
            pstmt = con.prepareStatement(sql); //생성

            //?에 데이터 대입
            pstmt.setString(1, guestbookDTO.getName());
            pstmt.setString(2, guestbookDTO.getEmail());
            pstmt.setString(3, guestbookDTO.getHomepage());
            pstmt.setString(4, guestbookDTO.getSubject());
            pstmt.setString(5, guestbookDTO.getContent());

            pstmt.executeUpdate(); //실행 - 개수 리턴

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(pstmt != null) pstmt.close();
                if(con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public List<GuestbookDTO> getAllGuestbooks(int startNum, int endNum) {
        List<GuestbookDTO> list = new ArrayList<GuestbookDTO>();

        getConnection();

        String sql = """
				select * from
				(select rownum rn, tt.* from
				(select * from guestbook order by seq desc) tt
				) where rn>=? and rn<=?
				""";

        try {
            pstmt = con.prepareStatement(sql);

            pstmt.setInt(1, startNum);
            pstmt.setInt(2, endNum);

            rs = pstmt.executeQuery();

            while(rs.next()) {
                GuestbookDTO guestbookDTO = new GuestbookDTO();
                guestbookDTO.setSeq(rs.getInt("seq"));
                guestbookDTO.setName(rs.getString("name"));
                guestbookDTO.setEmail(rs.getString("email"));
                guestbookDTO.setHomepage(rs.getString("homepage"));
                guestbookDTO.setSubject(rs.getString("subject"));
                guestbookDTO.setContent(rs.getString("content"));
                guestbookDTO.setLogtime(rs.getDate("logtime"));

                list.add(guestbookDTO);
            }//while

        } catch (SQLException e) {
            e.printStackTrace();
            list = null;
        } finally {
            try {
                if(rs != null) rs.close();
                if(pstmt != null) pstmt.close();
                if(con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    public int getTotal() {
        return 1;
    }
}








