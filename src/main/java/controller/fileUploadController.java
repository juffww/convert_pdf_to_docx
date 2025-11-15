package controller;

import model.bean.file;
import model.bean.user;
import model.dao.fileDAO;
import utils.DbConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.Timestamp;

@WebServlet("/upload")
@MultipartConfig(
	fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
	maxFileSize = 1024 * 1024 * 50,       // 50MB
	maxRequestSize = 1024 * 1024 * 100    // 100MB
)
public class fileUploadController extends HttpServlet {

	private static final String UPLOAD_DIR = "uploads";

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		// Kiểm tra session
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			response.sendRedirect("index.html?error=unauthorized");
			return;
		}
		
		user currentUser = (user) session.getAttribute("user");
		int userId = currentUser.getUserId();
		
		try {
			// Lấy file từ request
			Part filePart = request.getPart("pdfFile");
			if (filePart == null || filePart.getSize() == 0) {
				response.sendRedirect("dashboard?error=nofile");
				return;
			}
			
			String fileName = getFileName(filePart);
			
			// Kiểm tra file PDF
			if (!fileName.toLowerCase().endsWith(".pdf")) {
				response.sendRedirect("dashboard?error=invalidtype");
				return;
			}
			
			// Tạo thư mục upload nếu chưa có
			String applicationPath = request.getServletContext().getRealPath("");
			String uploadPath = applicationPath + File.separator + UPLOAD_DIR;
			File uploadDir = new File(uploadPath);
			if (!uploadDir.exists()) {
				uploadDir.mkdirs();
			}
			
			// Tạo tên file unique
			String uniqueFileName = System.currentTimeMillis() + "_" + userId + "_" + fileName;
			String fullPath = uploadPath + File.separator + uniqueFileName;
			
			// Lưu file
			Path filePath = Paths.get(fullPath);
			try (InputStream fileContent = filePart.getInputStream()) {
				Files.copy(fileContent, filePath, StandardCopyOption.REPLACE_EXISTING);
			}
			
			// Lưu thông tin vào database
			Connection conn = null;
			try {
				conn = DbConnection.getConnection();
				fileDAO fileDao = new fileDAO(conn);
				
				file newFile = new file();
				newFile.setUserId(userId);
				newFile.setOriginalFilename(fileName);
				newFile.setFilePath(fullPath);
				newFile.setFileSize(filePart.getSize());
				newFile.setUploadTime(new Timestamp(System.currentTimeMillis()));
				
				boolean success = fileDao.insert(newFile);
				
				if (success) {
					response.sendRedirect("dashboard?success=uploaded");
				} else {
					// Xóa file nếu lưu DB thất bại
					Files.deleteIfExists(filePath);
					response.sendRedirect("dashboard?error=dbfailed");
				}
				
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
		} catch (Exception e) {
			System.err.println("=== UPLOAD ERROR ===");
			System.err.println("User ID: " + userId);
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
			System.err.println("====================");
			
			response.sendRedirect("dashboard?error=upload");
		}
	}
	
	private String getFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		String[] tokens = contentDisp.split(";");
		for (String token : tokens) {
			if (token.trim().startsWith("filename")) {
				return token.substring(token.indexOf("=") + 2, token.length() - 1);
			}
		}
		return "";
	}
}
