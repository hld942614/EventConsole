package com.project.uhd.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.project.uhd.dto.CategoryContentUpdateRequest;
import com.project.uhd.dto.CategoryCreateRequest;
import com.project.uhd.dto.EventCategoryStatsDTO;
import com.project.uhd.dto.UpdateParentDto;
import com.project.uhd.entity.Category;
import com.project.uhd.enums.EventStatus;
import com.project.uhd.repository.CategoryRepository;

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
		String sql = "SELECT "
				+ "    CATEGORY_ID, "
				+ "    CATEGORY_PARENTID, "
				+ "    CATEGORY_CODE, "
				+ "    CATEGORY_TITLE, "
				+ "    CATEGORY_CONTENT "
				+ "FROM MUHD_CATEGORY "
				+ "WHERE LEVEL = 2 "
				+ "START WITH CATEGORY_PARENTID = ? "
				+ "CONNECT BY PRIOR CATEGORY_ID = CATEGORY_PARENTID";
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
		String sql = "SELECT * "
				+ "FROM MUHD_CATEGORY "
				+ "WHERE CATEGORY_PARENTID = ? "
				+ "ORDER BY CATEGORY_CODE ASC";
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
		String sql = "SELECT * "
				+ "FROM MUHD_CATEGORY "
				+ "START WITH CATEGORY_CODE = ? "
				+ "CONNECT BY PRIOR CATEGORY_PARENTID = CATEGORY_ID "
				+ "ORDER BY LEVEL DESC "
				+ "FETCH FIRST 1 ROW ONLY";
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

	public List<EventCategoryStatsDTO> getEventCategoryStats() {
		List<EventCategoryStatsDTO> rtnList = new ArrayList<>();

		String sql = "SELECT "
				+ "    c.CATEGORY_CODE AS MAINTYPE, "
				+ "    c.CATEGORY_ID AS CATEGORY_ID, "
				+ "    c.CATEGORY_CONTENT AS CATEGORY_CONTENT, "
				+ "    c.CATEGORY_TITLE AS CATEGORY_TITLE, "
				+ "    e.EVENT_STATUS AS EVENT_STATUS, "
				+ "    COUNT(e.ID) AS EVENT_COUNT "
				+ "FROM MUHD_CATEGORY c "
				+ "LEFT JOIN MUHD_EVENT e "
				+ "       ON e.MODULE_CODE = c.CATEGORY_CODE "
				+ "      AND e.CASE_ID IS NULL "
				+ "WHERE c.CATEGORY_PARENTID = 0 "
				+ "GROUP BY "
				+ "    c.CATEGORY_CODE, "
				+ "    c.CATEGORY_ID, "
				+ "    c.CATEGORY_CONTENT, "
				+ "    c.CATEGORY_TITLE, "
				+ "    e.EVENT_STATUS "
				+ "UNION ALL "
				+ "SELECT "
				+ "    'Others' AS MAINTYPE, "
				+ "    NULL AS CATEGORY_ID, "
				+ "    NULL AS CATEGORY_CONTENT, "
				+ "    '未分類' AS CATEGORY_TITLE, "
				+ "    e.EVENT_STATUS AS EVENT_STATUS, "
				+ "    COUNT(e.ID) AS EVENT_COUNT "
				+ "FROM MUHD_EVENT e "
				+ "WHERE e.CASE_ID IS NULL "
				+ "  AND (e.MODULE_CODE IS NULL "
				+ "    OR e.MODULE_CODE NOT IN (SELECT CATEGORY_CODE FROM MUHD_CATEGORY)) "
				+ "GROUP BY e.EVENT_STATUS "
				+ "ORDER BY CATEGORY_ID NULLS LAST";

		try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet result = stmt.executeQuery()) {

			Map<String, EventCategoryStatsDTO> categoryMap = new LinkedHashMap<>();
			while (result.next()) {
				String code = result.getString("MAINTYPE");
				Long id = result.getObject("CATEGORY_ID", Long.class);

				String key = code + "_" + id;

				EventCategoryStatsDTO row = categoryMap.get(key);

				if (row == null) {
					Map<String, Integer> count = new LinkedHashMap<>();

					for (EventStatus status : EventStatus.values()) {
						if (status == EventStatus.INVALID) {
							continue;
						}

						count.put(status.name().toLowerCase(), 0);
					}

					row = new EventCategoryStatsDTO(
							code,
							id,
							result.getString("CATEGORY_TITLE"),
							result.getString("CATEGORY_CONTENT"),
							count
					);
					categoryMap.put(key, row);
				}
				String statusCode = result.getString("EVENT_STATUS");
				if (statusCode != null) {
					try {
						EventStatus status = EventStatus.valueOf(statusCode);
						if (status == EventStatus.INVALID) {
							continue;
						}
						row.getCount().put(status.name().toLowerCase(),result.getInt("EVENT_COUNT"));
					} catch (IllegalArgumentException ex) {
						System.err.println("Unknown EVENT_STATUS: " + statusCode);
					}
				}
			}
			rtnList.addAll(categoryMap.values());
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
