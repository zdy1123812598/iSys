package com.mystic.screencapture;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class Upload
 */
public class UploadScreen extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public UploadScreen() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	@SuppressWarnings({ "unused", "rawtypes", "deprecation" })
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uname = "";// 		= request.getParameter("uid");
		String upass = "";// 		= request.getParameter("fid");
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List files = null;
		try {
			files = upload.parseRequest(request);
		} catch (FileUploadException e) {
		}
		
		FileItem imgFile = null;
		Iterator fileItr = files.iterator();
		while (fileItr.hasNext()) {
			imgFile = (FileItem) fileItr.next();
			if (imgFile.isFormField()) {
				String fn = imgFile.getFieldName();
				String fv = imgFile.getString();
				if (fn.equals("uname"))
					uname = fv;
				if (fn.equals("upass"))
					upass = fv;
			} else {
				break;
			}
		}
		ScreenCaptureHandler up = new ScreenCaptureHandler(((GenericServlet) request).getServletContext(), request);
		try {
			up.SaveFile(imgFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String url = up.GetFilePathRel();
		response.getWriter().write(url);
		response.setHeader("Content-Length",url.length()+"");
	}

}
