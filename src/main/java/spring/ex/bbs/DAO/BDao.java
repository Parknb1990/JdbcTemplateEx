package spring.ex.bbs.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;

import spring.ex.bbs.DTO.BDto;
import spring.ex.bbs.util.Constant;

public class BDao {

	DataSource dataSource;
	JdbcTemplate template;
	
	public BDao() {
		
		template = Constant.template;
	}
	
	
	
	public BDto contentView(String strId) {
		
		upHit(strId);
		String sql = "select *from mvc_board where bId = "+strId;
		return template.queryForObject(sql, new BeanPropertyRowMapper<BDto>(BDto.class));
		
	}
	
	
	
	
	public void write(final String bName, final String bTitle, final String bContent) {
		
		template.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String sql = "insert into mvc_board(bId, bName, bTitle, bContent, bHit, bGroup, bStep, bIndent) values(mvc_board_seq.nextval, ?, ?, ?, 0, mvc_board_seq.currval, 0, 0)";
				PreparedStatement pstmt = con.prepareStatement(sql);
//				내부 메소드에서 바깥쪽 메소드를 사용하게되면 참조값이 변경될수도 있다. 그래서 변경되지 말라고 final 키워드를 사용한다.
				pstmt.setString(1, bName);
				pstmt.setString(2, bTitle);
				pstmt.setString(3, bContent);
				
				return pstmt;
			}
		});
	}
	
	
	public ArrayList<BDto> list() {
		
		//ArrayList<BDto> dtos = null;
		String sql = "select bId, bName, bTitle, bContent, bDate, bHit, bGroup, bStep, bIndent from mvc_board order by bGroup desc, bStep asc";
		return (ArrayList<BDto>) template.query(sql, new BeanPropertyRowMapper<BDto>(BDto.class));
		
		//return dtos;
	}
	
	private void upHit(final String strId) {
		
		String sql = "update mvc_board set bHit = bHit+1 where bId = ?";
		template.update(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
//				내부 메소드에서 바깥쪽 메소드를 사용하게되면 참조값이 변경될수도 있다. 그래서 변경되지 말라고 final 키워드를 사용한다.
				ps.setInt(1, Integer.parseInt(strId));
				
			}
		});
	}

	public void modify(final String bId, final String bName, final String bTitle, final String bContent) {

		String sql = "update mvc_board set bName = ?, bTitle = ?, bContent = ? where bId = ?";
		
		template.update(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
//				내부 메소드에서 바깥쪽 메소드를 사용하게되면 참조값이 변경될수도 있다. 그래서 변경되지 말라고 final 키워드를 사용한다.
				ps.setString(1,  bName);
				ps.setString(2,  bTitle);
				ps.setString(3,  bContent);
				ps.setInt(4,  Integer.parseInt(bId));
				
			}
		});
	}

	public void delete(final String bId) {
		
		String sql = "delete from mvc_board where bId = ?";
		template.update(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
//				내부 메소드에서 바깥쪽 메소드를 사용하게되면 참조값이 변경될수도 있다. 그래서 변경되지 말라고 final 키워드를 사용한다.
				ps.setInt(1, Integer.parseInt(bId));
				
			}
		});
	}

//	답글 데이터 가져오기
	
	public BDto reply_view(String strId) {
		
		String sql = "select *from mvc_board where bId = ?"+strId;
		return template.queryForObject(sql, new BeanPropertyRowMapper<BDto>(BDto.class));
	}

	
	
//	답글 메소드
	public void reply(final String bId, final String bName, final String bTitle, final String bContent, final String bGroup, final String bStep,
			final String bIndent) {
		
//		답글 들여쓰기 메소드
		replyShape(bGroup, bStep);
		
		String sql = "insert into mvc_board(bId, bName, bTitle, bContent, bGroup, bStep, bIndent) values (mvc_board_sql.nextval, ?,?,?,?,?,?)";
		template.update(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
//				내부 메소드에서 바깥쪽 메소드를 사용하게되면 참조값이 변경될수도 있다. 그래서 변경되지 말라고 final 키워드를 사용한다.
				ps.setString(1, bName);
				ps.setString(2, bTitle);
				ps.setString(3, bContent);
				ps.setInt(1, Integer.parseInt(bGroup));
				ps.setInt(1, Integer.parseInt(bStep));
				ps.setInt(1, Integer.parseInt(bIndent));
				
			}
		});	
	}

	
//	답글 들여쓰기 모양 잡기
	private void replyShape(final String bGroup, final String bStep) {
		
		String sql = "update mvc_board set bStep = bStep+1 where bGroup=? and bSep > ?";
		template.update(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
//				내부 메소드에서 바깥쪽 메소드를 사용하게되면 참조값이 변경될수도 있다. 그래서 변경되지 말라고 final 키워드를 사용한다.
				ps.setInt(1, Integer.parseInt(bGroup));
				ps.setInt(1, Integer.parseInt(bStep));
				
				
			}
		});

	}

}
