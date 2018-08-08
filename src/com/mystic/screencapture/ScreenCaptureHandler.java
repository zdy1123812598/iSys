package com.mystic.screencapture;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;


public class ScreenCaptureHandler {
	
	public ServletContext m_pc;
	String m_folder;		
	String m_curBasePath;	
	String m_filePathRel;	
	String m_fileName;		
	
	public ScreenCaptureHandler(ServletContext pc,HttpServletRequest sr)
	{
		this.m_pc = pc;
		String path = sr.getContextPath();
		this.m_curBasePath = sr.getScheme()+"://" + sr.getServerName()+":" + sr.getLocalPort() + path+"/";
	}

	public String GetFilePathRel()
	{
		return this.m_filePathRel + this.m_fileName;
	}
	
	public void CreateFolder()
	{
		Date timeCur = new Date();
		SimpleDateFormat fmtYY = new SimpleDateFormat("yyyy");
		SimpleDateFormat fmtMM = new SimpleDateFormat("MM");
		SimpleDateFormat fmtDD = new SimpleDateFormat("dd");
		String strYY = fmtYY.format(timeCur);
		String strMM = fmtMM.format(timeCur);
		String strDD = fmtDD.format(timeCur);
		
		String pathRel = "upload/" + strYY + "/" + strMM + "/" + strDD + "/";
		String pathAbs = "upload\\" + strYY + "\\" + strMM + "\\" + strDD + "\\";
		this.m_filePathRel = this.m_curBasePath + pathRel;
		
		this.m_folder = this.m_pc.getRealPath("/") + pathAbs;
		
		File f = new File(this.m_folder);
		if(!f.exists())
		{
			f.mkdirs();
		}		
	}
	
	public String GenerateFileName()
	{
		Date timeCur = new Date();
		SimpleDateFormat fmt = new SimpleDateFormat("HHmmssSSSS");
		String timeStr = fmt.format(timeCur);
		return timeStr;
	}
	
	@SuppressWarnings("unused")
	public void SaveFile(FileItem upFile) throws IOException
	{
		String fileName = upFile.getName();
		int pos = fileName.indexOf('.');
		String ext = fileName.substring(pos);
		ext.toLowerCase();
		this.m_fileName = this.GenerateFileName() + ext;
		
		this.CreateFolder();
		String filePath = this.m_folder + this.m_fileName;		

		InputStream stream = upFile.getInputStream();			
		byte[] data = new byte[(int)upFile.getSize()];
		int readLen = stream.read(data);
		stream.close();
		
		RandomAccessFile raf = new RandomAccessFile(filePath,"rw");
		raf.write(data);
		raf.close();
	}

}