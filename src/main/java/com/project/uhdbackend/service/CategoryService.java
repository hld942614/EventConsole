package com.project.uhdbackend.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.project.uhdbackend.dto.CategoryContentUpdateRequest;
import com.project.uhdbackend.dto.CategoryCreateRequest;
import com.project.uhdbackend.dto.UpdateParentDto;
import com.project.uhdbackend.entity.Category;
import com.project.uhdbackend.repository.CategoryRepository;

@Service
public class CategoryService {
	@Autowired
	private CategoryRepository repository;

	@Value("${spring.datasource.url}")
	private String DB_URL;
	@Value("${spring.datasource.username}")
	private String USER;
	@Value("${spring.datasource.password}")
	private String PASS;

	public Category save(CategoryCreateRequest request) {
		Category category = new Category();
		category.setCode(request.getCode());
		category.setTitle(request.getTitle());
		category.setContent(request.getContent());
		category.setParentId(request.getParentId());
		return repository.save(category);
	}

	public List<Category> getAll() {
		return repository.findAll();
	}

	public List<Category> getMain() {
		List<Category> list = new ArrayList<>();
		String sql = "SELECT * FROM MUHD_CATEGORY WHERE CATEGORY_PARENTID = 0 ORDER BY CATEGORY_CODE ASC";
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				PreparedStatement stmt = conn.prepareStatement(sql);) {
			try (ResultSet result = stmt.executeQuery();) {
				while (result.next()) {
					list.add(resultToCategory(result));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Category> getSub() {
		List<Category> list = new ArrayList<>();
		String sql = "SELECT * FROM MUHD_CATEGORY WHERE CATEGORY_PARENTID != 0 ORDER BY CATEGORY_CODE ASC";
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				PreparedStatement stmt = conn.prepareStatement(sql);) {
			try (ResultSet result = stmt.executeQuery();) {
				while (result.next()) {
					list.add(resultToCategory(result));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Category> getAlertByParentId(int parentId) {
		List<Category> list = new ArrayList<>();
		String sql = "SELECT \r\n" + "CATEGORY_ID, \r\n" + "CATEGORY_PARENTID, \r\n" + "CATEGORY_CODE, \r\n"
				+ "CATEGORY_TITLE, \r\n" + "CATEGORY_CONTENT\r\n" + "FROM MUHD_CATEGORY\r\n" + "WHERE LEVEL=2\r\n"
				+ "START WITH CATEGORY_PARENTID = ? \r\n" + "CONNECT BY PRIOR CATEGORY_ID = CATEGORY_PARENTID";
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				PreparedStatement stmt = conn.prepareStatement(sql);) {
			stmt.setInt(1, parentId);
			try (ResultSet result = stmt.executeQuery();) {
				while (result.next()) {
					list.add(resultToCategory(result));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Category> getSubByParentId(int parentId) {
		List<Category> list = new ArrayList<>();
		String sql = "SELECT * FROM MUHD_CATEGORY WHERE CATEGORY_PARENTID = ? ORDER BY CATEGORY_CODE ASC";
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				PreparedStatement stmt = conn.prepareStatement(sql);) {
			stmt.setInt(1, parentId);
			try (ResultSet result = stmt.executeQuery();) {
				while (result.next()) {
					list.add(resultToCategory(result));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public Category getMainByAlertCode(String alertCode) {
		String sql = "SELECT\r\n" + "    *\r\n" + "FROM\r\n"
				+ "    MUHD_CATEGORY START WITH CATEGORY_CODE = ? CONNECT BY PRIOR CATEGORY_PARENTID = CATEGORY_ID\r\n"
				+ "ORDER BY\r\n" + "    LEVEL DESC\r\n" + "FETCH FIRST\r\n" + "    1 ROW ONLY";
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				PreparedStatement stmt = conn.prepareStatement(sql);) {
			stmt.setString(1, alertCode);
			try (ResultSet result = stmt.executeQuery();) {
				while (result.next()) {
					return resultToCategory(result);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Map<String, Object>> getCategoryStats() {
		List<Map<String, Object>> rtnList = new ArrayList<>();
		String sql = "WITH CATEGORY_HIERARCHY AS (\r\n" + "    SELECT\r\n" + "        CATEGORY_ID,\r\n"
				+ "        CATEGORY_CODE,\r\n" + "        CATEGORY_PARENTID,\r\n" + "        CATEGORY_CONTENT,\r\n"
				+ "        CATEGORY_TITLE,\r\n" + "        CONNECT_BY_ROOT CATEGORY_CODE    AS ROOT_CATEGORY_CODE,\r\n"
				+ "        CONNECT_BY_ROOT CATEGORY_ID      AS ROOT_CATEGORY_ID,\r\n"
				+ "        CONNECT_BY_ROOT CATEGORY_TITLE   AS ROOT_CATEGORY_TITLE,\r\n"
				+ "        CONNECT_BY_ROOT CATEGORY_CONTENT AS ROOT_CATEGORY_CONTENT\r\n" + "    FROM MUHD_CATEGORY\r\n"
				+ "    START WITH CATEGORY_PARENTID = 0\r\n"
				+ "    CONNECT BY PRIOR CATEGORY_ID = CATEGORY_PARENTID\r\n" + ")\r\n" + "-- 各主類別（根）匯總\r\n"
				+ "SELECT\r\n" + "    CH.ROOT_CATEGORY_CODE    AS MAINTYPE,\r\n"
				+ "    CH.ROOT_CATEGORY_ID      AS CATEGORY_ID,\r\n"
				+ "    CH.ROOT_CATEGORY_CONTENT AS CATEGORY_CONTENT,\r\n"
				+ "    CH.ROOT_CATEGORY_TITLE   AS CATEGORY_TITLE,\r\n"
				+ "    SUM(CASE WHEN A.MESSAGE_STATUS = 'PROCESSING'   THEN 1 ELSE 0 END) AS PROCESSING_COUNT,\r\n"
				+ "    SUM(CASE WHEN A.MESSAGE_STATUS = 'UNPROCESSED' THEN 1 ELSE 0 END) AS UNPROCESSED_COUNT\r\n"
				+ "FROM CATEGORY_HIERARCHY CH\r\n" + "LEFT JOIN MUHD_MESSAGE A\r\n"
				+ "       ON A.MESSAGE_ALERTCODE = CH.CATEGORY_CODE\r\n" + "      AND NOT EXISTS (\r\n"
				+ "          SELECT 1\r\n" + "          FROM MUHD_CASE_MESSAGE CM\r\n"
				+ "          WHERE CM.MESSAGE_ID = A.MESSAGE_ID\r\n" + "      )\r\n" + "GROUP BY\r\n"
				+ "    CH.ROOT_CATEGORY_CODE,\r\n" + "    CH.ROOT_CATEGORY_ID,\r\n"
				+ "    CH.ROOT_CATEGORY_CONTENT,\r\n" + "    CH.ROOT_CATEGORY_TITLE\r\n" + "\r\n" + "UNION ALL\r\n"
				+ "\r\n" + "-- 未分類（Others）\r\n" + "SELECT\r\n" + "    'Others'               AS MAINTYPE,\r\n"
				+ "    NULL                   AS CATEGORY_ID,\r\n"
				+ "    NULL                   AS CATEGORY_CONTENT,\r\n"
				+ "    '未分類'               AS CATEGORY_TITLE,\r\n"
				+ "    SUM(CASE WHEN M.MESSAGE_STATUS = 'PROCESSING'   THEN 1 ELSE 0 END) AS PROCESSING_COUNT,\r\n"
				+ "    SUM(CASE WHEN M.MESSAGE_STATUS = 'UNPROCESSED' THEN 1 ELSE 0 END) AS UNPROCESSED_COUNT\r\n"
				+ "FROM MUHD_MESSAGE M\r\n" + "WHERE (M.MESSAGE_ALERTCODE IS NULL\r\n"
				+ "    OR M.MESSAGE_ALERTCODE NOT IN (SELECT CATEGORY_CODE FROM MUHD_CATEGORY))\r\n"
				+ "  AND NOT EXISTS (\r\n" + "      SELECT 1\r\n" + "      FROM MUHD_CASE_MESSAGE CM\r\n"
				+ "      WHERE CM.MESSAGE_ID = M.MESSAGE_ID\r\n" + "  )\r\n" + "\r\n"
				+ "ORDER BY CATEGORY_ID NULLS LAST";
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet result = stmt.executeQuery()) {

			while (result.next()) {
				Map<String, Object> row = new HashMap<>();
				row.put("code", result.getString("MAINTYPE"));
				row.put("id", result.getString("CATEGORY_ID"));
				row.put("content", result.getString("CATEGORY_CONTENT"));
				row.put("title", result.getString("CATEGORY_TITLE"));
				row.put("processing_count", result.getInt("PROCESSING_COUNT"));
				row.put("unprocessed_count", result.getInt("UNPROCESSED_COUNT"));
				rtnList.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtnList;
	}

	public List<Map<String, Object>> getEventCategoryStats() {
		List<Map<String, Object>> rtnList = new ArrayList<>();
		String sql = "SELECT\r\n" + "    c.CATEGORY_CODE AS MAINTYPE,\r\n" + "    c.CATEGORY_ID AS CATEGORY_ID,\r\n"
				+ "    c.CATEGORY_CONTENT AS CATEGORY_CONTENT,\r\n" + "    c.CATEGORY_TITLE AS CATEGORY_TITLE,\r\n"
				+ "    SUM(CASE WHEN e.EVENT_STATUS = 'UNREAD' THEN 1 ELSE 0 END) AS UNREAD_COUNT,\r\n"
				+ "    SUM(CASE WHEN e.EVENT_STATUS = 'ACKNOWLEDGED' THEN 1 ELSE 0 END) AS ACKNOWLEDGED_COUNT,\r\n"
				+ "    SUM(CASE WHEN e.EVENT_STATUS = 'CLASSIFIED' THEN 1 ELSE 0 END) AS CLASSIFIED_COUNT,\r\n"
				+ "    SUM(CASE WHEN e.EVENT_STATUS = 'PROCESSING' THEN 1 ELSE 0 END) AS PROCESSING_COUNT,\r\n"
				+ "    SUM(CASE WHEN e.EVENT_STATUS = 'RESOLVED' THEN 1 ELSE 0 END) AS RESOLVED_COUNT,\r\n"
				+ "    SUM(CASE WHEN e.EVENT_STATUS = 'CLOSED' THEN 1 ELSE 0 END) AS CLOSED_COUNT\r\n"
				+ "FROM MUHD_CATEGORY c\r\n" + "LEFT JOIN MUHD_EVENT e\r\n"
				+ "       ON e.MODULE_CODE = c.CATEGORY_CODE\r\n" + "      AND NOT EXISTS (\r\n"
				+ "          SELECT 1\r\n" + "          FROM MUHD_CASE_EVENT ce\r\n"
				+ "          WHERE ce.EVENT_PK = e.ID\r\n" + "      )\r\n" + "WHERE c.CATEGORY_PARENTID = 0\r\n"
				+ "GROUP BY\r\n" + "    c.CATEGORY_CODE,\r\n" + "    c.CATEGORY_ID,\r\n" + "    c.CATEGORY_CONTENT,\r\n"
				+ "    c.CATEGORY_TITLE\r\n" + "\r\n" + "UNION ALL\r\n" + "\r\n" + "SELECT\r\n"
				+ "    'Others' AS MAINTYPE,\r\n" + "    NULL AS CATEGORY_ID,\r\n" + "    NULL AS CATEGORY_CONTENT,\r\n"
				+ "    '未分類' AS CATEGORY_TITLE,\r\n"
				+ "    SUM(CASE WHEN e.EVENT_STATUS = 'UNREAD' THEN 1 ELSE 0 END) AS UNREAD_COUNT,\r\n"
				+ "    SUM(CASE WHEN e.EVENT_STATUS = 'ACKNOWLEDGED' THEN 1 ELSE 0 END) AS ACKNOWLEDGED_COUNT,\r\n"
				+ "    SUM(CASE WHEN e.EVENT_STATUS = 'CLASSIFIED' THEN 1 ELSE 0 END) AS CLASSIFIED_COUNT,\r\n"
				+ "    SUM(CASE WHEN e.EVENT_STATUS = 'PROCESSING' THEN 1 ELSE 0 END) AS PROCESSING_COUNT,\r\n"
				+ "    SUM(CASE WHEN e.EVENT_STATUS = 'RESOLVED' THEN 1 ELSE 0 END) AS RESOLVED_COUNT,\r\n"
				+ "    SUM(CASE WHEN e.EVENT_STATUS = 'CLOSED' THEN 1 ELSE 0 END) AS CLOSED_COUNT\r\n"
				+ "FROM MUHD_EVENT e\r\n" + "WHERE (e.MODULE_CODE IS NULL\r\n"
				+ "    OR e.MODULE_CODE NOT IN (SELECT CATEGORY_CODE FROM MUHD_CATEGORY))\r\n"
				+ "  AND NOT EXISTS (\r\n" + "      SELECT 1\r\n" + "      FROM MUHD_CASE_EVENT ce\r\n"
				+ "      WHERE ce.EVENT_PK = e.ID\r\n" + "  )\r\n" + "\r\n" + "ORDER BY CATEGORY_ID NULLS LAST";
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet result = stmt.executeQuery()) {

			while (result.next()) {
				Map<String, Object> row = new HashMap<>();

				row.put("code", result.getString("MAINTYPE"));
				row.put("id", result.getString("CATEGORY_ID"));
				row.put("content", result.getString("CATEGORY_CONTENT"));
				row.put("title", result.getString("CATEGORY_TITLE"));

				Map<String, Integer> count = new HashMap<>();
				count.put("unread", result.getInt("UNREAD_COUNT"));
				count.put("acknowledged", result.getInt("ACKNOWLEDGED_COUNT"));
				count.put("classified", result.getInt("CLASSIFIED_COUNT"));
				count.put("processing", result.getInt("PROCESSING_COUNT"));
				count.put("resolved", result.getInt("RESOLVED_COUNT"));
				count.put("closed", result.getInt("CLOSED_COUNT"));

				row.put("count", count);

				rtnList.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtnList;
	}

	public List<Map<String, Object>> getCategorySubCount() {
		List<Map<String, Object>> rtnList = new ArrayList<>();
		String sql = "WITH CATEGORY_TREE AS (\r\n"
				+ "    SELECT CATEGORY_ID, CATEGORY_CODE, CATEGORY_TITLE, CATEGORY_PARENTID,CATEGORY_CONTENT, LEVEL AS LVL\r\n"
				+ "    FROM MUHD_CATEGORY\r\n" + "    START WITH CATEGORY_PARENTID = 0\r\n"
				+ "    CONNECT BY PRIOR CATEGORY_ID = CATEGORY_PARENTID\r\n" + ")\r\n"
				+ "SELECT P.CATEGORY_ID AS CATEGORY_ID, \r\n" + "       P.CATEGORY_CODE AS CATEGORY_CODE, \r\n"
				+ "       P.CATEGORY_TITLE AS CATEGORY_TITLE,\r\n"
				+ "       P.CATEGORY_CONTENT AS CATEGORY_CONTENT,\r\n" + "       P.LVL AS CATEGORY_LEVEL,\r\n"
				+ "       COUNT(C.CATEGORY_ID) AS CHILD_COUNT\r\n" + "FROM CATEGORY_TREE P\r\n"
				+ "LEFT JOIN CATEGORY_TREE C ON P.CATEGORY_ID = C.CATEGORY_PARENTID\r\n" + "WHERE P.LVL < 3\r\n"
				+ "GROUP BY P.CATEGORY_ID, P.CATEGORY_CODE, P.CATEGORY_TITLE,P.CATEGORY_CONTENT,P.LVL\r\n"
				+ "ORDER BY P.CATEGORY_ID";
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				PreparedStatement stmt = conn.prepareStatement(sql);) {
//			stmt.setInt(1, level);
			try (ResultSet result = stmt.executeQuery();) {
				while (result.next()) {
					Map<String, Object> row = new HashMap<>();
					row.put("code", result.getString("MAINTYPE"));
					row.put("id", result.getString("CATEGORY_ID"));
					row.put("content", result.getString("CATEGORY_CONTENT"));
					row.put("title", result.getString("CATEGORY_TITLE"));
					row.put("level", result.getString("CATEGORY_LEVEL"));
					row.put("count", result.getInt("COUNT"));
					rtnList.add(row);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtnList;
	}

	public int getId(String code) {
		int id = -1;
		String sql = "SELECT * FROM MUHD_CATEGORY WHERE CATEGORY_CODE = ?";
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				PreparedStatement stmt = conn.prepareStatement(sql);) {
			stmt.setString(1, code);
			try (ResultSet result = stmt.executeQuery();) {
				while (result.next()) {
					id = result.getInt("CATEGORY_ID");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public void updateContent(CategoryContentUpdateRequest request) {
		String sql = "UPDATE MUHD_CATEGORY SET CATEGORY_CONTENT = ?,CATEGORY_TITLE= ? WHERE CATEGORY_ID = ?";
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				PreparedStatement stmt = conn.prepareStatement(sql);) {
			stmt.setString(1, request.getContent());
			stmt.setString(2, request.getTitle());
			stmt.setLong(3, request.getId());
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateParentId(UpdateParentDto dto) {
		String sql = "UPDATE MUHD_CATEGORY SET CATEGORY_PARENTID = ? WHERE CATEGORY_ID = ?";
		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				PreparedStatement stmt = conn.prepareStatement(sql);) {
			stmt.setInt(1, dto.getParentId());
			stmt.setInt(2, dto.getCategoryId());
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Optional<Category> findById(Long id) {
		return repository.findById(id);
	}

	private Category resultToCategory(ResultSet result) throws SQLException {
		Category category = new Category();
		category.setId(result.getLong("CATEGORY_ID"));
		category.setParentId(result.getInt("CATEGORY_PARENTID"));
		category.setCode(result.getString("CATEGORY_CODE"));
		category.setTitle(result.getString("CATEGORY_TITLE"));
		category.setContent(result.getString("CATEGORY_CONTENT"));
		return category;
	}
}
